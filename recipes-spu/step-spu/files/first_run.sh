#!/bin/sh

# This script is intended to be run once on first boot,
# and will perform various configuration tasks to
# update boot loader and expand filesystem.


set +e

# =========================
# User-configurable values
# =========================
LOG_FILE="/var/log/first_run.log"
FIRST_RUN_MARKER="$0"
BOOTFS="/boot/firmware"
SPIDEV=/dev/spidev10.0
# ===== Logging function =====
log() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') | $1" | tee -a "$LOG_FILE"
}

# =========================
# Helpers
# =========================
runFlashrom()
{
   attempts=5
   freq=16000
   log "Updating bootloader using flashrom with initial frequency ${freq} kHz and SPIDEV ${SPIDEV}"

   # If flashrom fails then retry at a lower speed.
   while [ ${attempts} -gt 0 ]; do
      log "flashrom -p linux_spi:dev=${SPIDEV},spispeed=${freq} -w ${BOOTFS}/pieeprom.upd"
      if flashrom -p linux_spi:dev=${SPIDEV},spispeed=${freq} -w "${BOOTFS}/pieeprom.upd" >> "${LOG_FILE}"; then
         log "Verifying update"
         if flashrom -p linux_spi:dev=${SPIDEV},spispeed=${freq} -v "${BOOTFS}/pieeprom.upd" >> "${LOG_FILE}"; then
            log "VERIFY: SUCCESS"
            return 0
         else
            log "VERIFY: FAIL"
         fi
      fi
      freq=8000
      attempts=$((attempts - 1))
      if [ ${attempts} -gt 0 ]; then
         log "Failed to update bootloader using flashrom. Retrying."
      fi
   done
   return 1
}

get_disk_and_partnum() {
    part="$1"

    case "$part" in
        /dev/mmcblk*p[0-9]*|/dev/nvme*n*p[0-9]*)
            disk="${part%p*}"
            partnum="${part##*p}"
            ;;
        /dev/sd[a-z][0-9]*|/dev/vd[a-z][0-9]*|/dev/xvd[a-z][0-9]*)
            disk="${part%%[0-9]*}"
            partnum="${part##*[!0-9]}"
            ;;
        *)
            return 1
            ;;
    esac

    if [ -z "$disk" ] || [ -z "$partnum" ]; then
        return 1
    fi

    echo "$disk $partnum"
    return 0
}

get_partnum_from_path() {
    part="$1"
    case "$part" in
        /dev/mmcblk*p[0-9]*|/dev/nvme*n*p[0-9]*)
            echo "${part##*p}"
            ;;
        /dev/sd[a-z][0-9]*|/dev/vd[a-z][0-9]*|/dev/xvd[a-z][0-9]*)
            echo "${part##*[!0-9]}"
            ;;
        *)
            return 1
            ;;
    esac
    return 0
}


# =========================
# Expand persistent filesystem
# =========================
log "=== expand-persistent: start $(date -Iseconds 2>/dev/null || date) ==="

PERSIST_SRC="$(findmnt -n -o SOURCE /persistent 2>/dev/null)"
if [ -z "${PERSIST_SRC:-}" ]; then
    log "ERROR: /persistent is not mounted"
    exit 1
fi

log "Persistent source: $PERSIST_SRC"

set -- $(get_disk_and_partnum "$PERSIST_SRC")
if [ $# -ne 2 ]; then
    log "ERROR: could not determine parent disk or partition number"
    exit 1
fi

DISK="$1"
PARTNUM="$2"
PART="$PERSIST_SRC"

log "Disk: $DISK"
log "Partition: $PART"
log "Partition number: $PARTNUM"

# Optional safety check: only grow if this is the last partition
LAST_PART="$(lsblk -nrpo NAME,TYPE "$DISK" 2>/dev/null | awk '$2=="part"{print $1}' | tail -n 1)"
if [ -z "${LAST_PART:-}" ]; then
    log "ERROR: could not determine last partition on $DISK"
    exit 1
fi

LAST_PARTNUM="$(get_partnum_from_path "$LAST_PART")"
if [ -z "${LAST_PARTNUM:-}" ]; then
    log "ERROR: could not determine last partition number for $LAST_PART"
    exit 1
fi

if [ "$PARTNUM" != "$LAST_PARTNUM" ]; then
    log "ERROR: $PART is not the last partition on $DISK"
    log "Refusing to resize automatically."
    exit 1
fi

if ! command -v growpart >/dev/null 2>&1; then
    log "ERROR: growpart not found"
    exit 1
fi

if ! command -v resize2fs >/dev/null 2>&1; then
    log "ERROR: resize2fs not found"
    exit 1
fi

log "Growing partition $PARTNUM on $DISK"
if ! growpart "$DISK" "$PARTNUM"; then
    log "ERROR: growpart failed"
    exit 1
fi

partprobe "$DISK" >/dev/null 2>&1 || true
udevadm settle >/dev/null 2>&1 || true
sleep 2

log "Growing ext4 filesystem on $PART"
if ! resize2fs "$PART"; then
    log "ERROR: resize2fs failed"
    exit 1
fi

# =========================
# Update bootloader using flashrom
# =========================

runFlashrom || log "WARNING: flashrom failed after multiple attempts. Please check $LOG_FILE for details."
# =========================
# Final cleanup
# =========================
if [ -e "$FIRST_RUN_MARKER" ]; then
    rm -f "$FIRST_RUN_MARKER"
fi

log "=== First run completed ==="
exit 0