SUMMARY = "Kiosk UI tweaks (lightdm/xwrapper/systemd quiet)"
DESCRIPTION = "Provides kiosk-specific display and startup configuration snippets"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "file://quiet.conf file://rcS"
S = "${WORKDIR}"

do_install() {
    install -d ${D}${sysconfdir}/lightdm
    install -d ${D}${sysconfdir}/X11
    install -d ${D}${sysconfdir}/systemd/system.conf.d
    install -d ${D}${sysconfdir}/default

    echo 'wayland-session=false' > ${D}${sysconfdir}/lightdm/lightdm.conf
    echo 'allowed_users=anybody' > ${D}${sysconfdir}/X11/Xwrapper.config

    install -m 0644 ${WORKDIR}/quiet.conf ${D}${sysconfdir}/systemd/system.conf.d/quiet.conf
    install -m 0644 ${WORKDIR}/rcS ${D}${sysconfdir}/default/rcS
}

FILES:${PN} += " \
    ${sysconfdir}/lightdm/lightdm.conf \
    ${sysconfdir}/X11/Xwrapper.config \
    ${sysconfdir}/systemd/system.conf.d/quiet.conf \
    ${sysconfdir}/default/rcS \
"

CONFFILES:${PN} += " \
    ${sysconfdir}/lightdm/lightdm.conf \
    ${sysconfdir}/X11/Xwrapper.config \
    ${sysconfdir}/systemd/system.conf.d/quiet.conf \
    ${sysconfdir}/default/rcS \
"
