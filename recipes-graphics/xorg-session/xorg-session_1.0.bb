SUMMARY = "Systemd Xorg session service for SPU"
DESCRIPTION = "Installs and enables an Xorg systemd unit that starts at boot."
LICENSE = "CLOSED"

inherit systemd

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "file://xorg.service"

S = "${WORKDIR}"


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

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/xorg.service ${D}${systemd_system_unitdir}/xorg.service
}

FILES:${PN} += " \
    ${systemd_system_unitdir}/xorg.service \
"
