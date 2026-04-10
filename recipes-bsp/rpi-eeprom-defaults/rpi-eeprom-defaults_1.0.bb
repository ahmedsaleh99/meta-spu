SUMMARY = "Prebuilt Raspberry Pi EEPROM recovery/update payloads"
DESCRIPTION = "Installs prebuilt EEPROM update files into /boot/firmware for Raspberry Pi 5 systems."
LICENSE = "CLOSED"

# To regenerate the EEPROM payload files in recipes-bsp/rpi-eeprom-defaults/files,
# use a prepared target rootfs and adapt the rootfs path as $1:
#
#   #!/bin/bash
#   # update eeprom
#   set -e
#   TEMP_CONFIG="/tmp/config.txt"
#   cat <<'EOF' > $TEMP_CONFIG
#   [all]
#   BOOT_UART=1
#   POWER_OFF_ON_HALT=1
#   BOOT_ORDER=0xf461
#   PSU_MAX_CURRENT=5000
#   EOF
#
#   # get latest eeprom bin file from the current image
#   BASE_EEPROM="$(rpi-eeprom-update -l)"
#   sed -i '/^RPI_EEPROM_USE_FLASHROM=/d' /etc/default/rpi-eeprom-update
#   echo 'RPI_EEPROM_USE_FLASHROM=0' | tee -a /etc/default/rpi-eeprom-update
#   TEMP_UPDATE=/tmp/pieeprom.upd
#   rpi-eeprom-config --config $TEMP_CONFIG --out $TEMP_UPDATE "$BASE_EEPROM"
#   cp $TEMP_UPDATE /tmp/pieeprom.upd
#   rpi-eeprom-update -d -i -f $TEMP_UPDATE
#   # mv /boot/firmware/recovery.bin /boot/firmware/recovery.bin

inherit allarch

SRC_URI = " \
    file://pieeprom.sig \
    file://pieeprom.upd \
    file://recovery.bin \
"

COMPATIBLE_MACHINE = "spu-rpi5|raspberrypi5"

RDEPENDS:${PN} = "rpi-eeprom"

do_install() {
    install -d ${D}${MENDER_BOOT_PART_MOUNT_LOCATION}
    install -m 0644 ${WORKDIR}/pieeprom.sig ${D}${MENDER_BOOT_PART_MOUNT_LOCATION}/pieeprom.sig
    install -m 0644 ${WORKDIR}/pieeprom.upd ${D}${MENDER_BOOT_PART_MOUNT_LOCATION}/pieeprom.upd
    install -m 0644 ${WORKDIR}/recovery.bin ${D}${MENDER_BOOT_PART_MOUNT_LOCATION}/recovery.bin
}

FILES:${PN} = " \
    ${MENDER_BOOT_PART_MOUNT_LOCATION}/pieeprom.sig \
    ${MENDER_BOOT_PART_MOUNT_LOCATION}/pieeprom.upd \
    ${MENDER_BOOT_PART_MOUNT_LOCATION}/recovery.bin \
"
