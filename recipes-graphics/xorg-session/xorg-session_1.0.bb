SUMMARY = "Systemd Xorg session service for SPU"
DESCRIPTION = "Installs and enables an Xorg systemd unit that starts at boot."
LICENSE = "CLOSED"

inherit systemd

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "file://xorg.service"

S = "${WORKDIR}"

DEPENDS += "user-credentials"

RDEPENDS:${PN} += " \
    xserver-xorg \
    xserver-xorg-extension-glx \
    xf86-video-modesetting \
    xf86-video-fbdev \
    xinit \
    xauth \
    xinput \
    xset \
    xrandr \
    xf86-input-libinput \
    libinput \
    libevdev \
    mesa \
    xcb-util-cursor \
"

SYSTEMD_SERVICE:${PN} = "xorg.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_check_spu_user() {
    if [ -z "${SPU_USER}" ]; then
        bbfatal "SPU_USER must be set in the distro config"
    fi
}

addtask check_spu_user after do_prepare_recipe_sysroot before do_install

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${sysconfdir}/systemd/system
    install -d ${D}${sysconfdir}/systemd/system.conf.d

    for n in 1 2 3 4 5 6; do
        ln -sf /dev/null ${D}${sysconfdir}/systemd/system/getty@tty${n}.service
    done

    ln -sf /dev/null ${D}${sysconfdir}/systemd/system/autovt@.service

    sed -e "s|@SPU_USER@|${SPU_USER}|g" \
        ${WORKDIR}/xorg.service > ${D}${systemd_system_unitdir}/xorg.service
    chmod 0644 ${D}${systemd_system_unitdir}/xorg.service
    printf '%s\n' '[Manager]' 'ShowStatus=no' > ${D}${sysconfdir}/systemd/system.conf.d/quiet.conf
}

FILES:${PN} += " \
    ${systemd_system_unitdir}/xorg.service \
    ${sysconfdir}/systemd/system/autovt@.service \
    ${sysconfdir}/systemd/system/getty@tty*.service \
    ${sysconfdir}/systemd/system.conf.d/quiet.conf \
"
