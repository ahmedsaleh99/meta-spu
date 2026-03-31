SUMMARY = "STEP-SPU Yocto image with Mender OTA support"
DESCRIPTION = "Image recipe for STEP-SPU appliance deployments with systemd and Mender support"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit core-image

IMAGE_FEATURES += "package-management ssh-server-openssh"
IMAGE_INSTALL:append = " \
    packagegroup-step-spu \
    step-spu \
    step-spu-config \
    step-spu-kiosk-tweaks \
    mender-client \
"

# Keep parity with current spu_img_gen partition intent.
MENDER_BOOT_PART_SIZE_MB ??= "256"
MENDER_DATA_PART_SIZE_MB ??= "512"

# Keep systemd behavior like existing image.
DISTRO_FEATURES:append = " systemd"
VIRTUAL-RUNTIME_init_manager = "systemd"

# Avoid tty login in kiosk mode similar to spu-kiosk layer behavior.
ROOTFS_POSTPROCESS_COMMAND += "step_spu_mask_getty;"

step_spu_mask_getty () {
    for n in 1 2 3 4 5 6; do
        ln -sf /dev/null ${IMAGE_ROOTFS}${sysconfdir}/systemd/system/getty@tty${n}.service
    done
    ln -sf /dev/null ${IMAGE_ROOTFS}${sysconfdir}/systemd/system/autovt@.service
}
