SUMMARY = "Save and restore system clock on machines without a working RTC"
HOMEPAGE = "https://git.einval.com/cgi-bin/gitweb.cgi?p=fake-hwclock.git"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3"

inherit allarch systemd

SRC_URI = "https://deb.debian.org/debian/pool/main/f/fake-hwclock/fake-hwclock_0.14.tar.xz"
SRC_URI[md5sum] = "f019ec24f7249556eef0cd7487504048"

PV = "0.14"
S = "${WORKDIR}/git"

RDEPENDS:${PN} = "coreutils"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${S}/fake-hwclock ${D}${sbindir}/fake-hwclock

    install -d ${D}${sysconfdir}
    if [ -f ${S}/etc/default/fake-hwclock ]; then
        install -d ${D}${sysconfdir}/default
        install -m 0644 ${S}/etc/default/fake-hwclock ${D}${sysconfdir}/default/fake-hwclock
    fi
    if [ -z "${FAKE_HWCLOCK_DATE}" ]; then
        bbfatal "FAKE_HWCLOCK_DATE must be set"
    fi
    date -u -d "${FAKE_HWCLOCK_DATE}" '+%Y-%m-%d %H:%M:%S' > "${D}${sysconfdir}/fake-hwclock.data"

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${S}/debian/fake-hwclock-load.service ${D}${systemd_system_unitdir}/fake-hwclock-load.service
    install -m 0644 ${S}/debian/fake-hwclock-save.service ${D}${systemd_system_unitdir}/fake-hwclock-save.service
    install -m 0644 ${S}/debian/fake-hwclock-save.timer ${D}${systemd_system_unitdir}/fake-hwclock-save.timer
}

SYSTEMD_SERVICE:${PN} = "fake-hwclock-load.service fake-hwclock-save.timer"
SYSTEMD_AUTO_ENABLE = "enable"

FILES:${PN} += " \
    ${sysconfdir}/default/fake-hwclock \
    ${sysconfdir}/fake-hwclock.data \
    ${sbindir}/fake-hwclock \
    ${systemd_system_unitdir}/fake-hwclock-load.service \
    ${systemd_system_unitdir}/fake-hwclock-save.service \
    ${systemd_system_unitdir}/fake-hwclock-save.timer \
"
