FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://spu-splash.plymouth \
    file://spu-splash.script \
    file://SPU-Logo.png \
    file://plymouthd.conf \
"

PLYMOUTH_THEMES = "spu-splash"

PACKAGECONFIG = "drm udev ${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)}"


do_install:append() {
    install -d ${D}${datadir}/plymouth/themes/spu-splash
    install -d ${D}${sysconfdir}/plymouth
    install -m 0644 ${WORKDIR}/spu-splash.plymouth ${D}${datadir}/plymouth/themes/spu-splash/spu-splash.plymouth
    install -m 0644 ${WORKDIR}/spu-splash.script ${D}${datadir}/plymouth/themes/spu-splash/spu-splash.script
    install -m 0644 ${WORKDIR}/SPU-Logo.png ${D}${datadir}/plymouth/themes/spu-splash/SPU-Logo.png
    install -m 0644 ${WORKDIR}/plymouthd.conf ${D}${sysconfdir}/plymouth/plymouthd.conf
}
