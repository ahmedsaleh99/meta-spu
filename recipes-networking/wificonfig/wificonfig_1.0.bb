SUMMARY = "systemd-networkd matcher config for wlan*"
DESCRIPTION = "Installs wlan.network so systemd-networkd can manage WLAN interfaces."
LICENSE = "CLOSED"

SRC_URI = "file://wlan.network"

RDEPENDS:${PN} = "systemd wpa-supplicant"

do_install() {
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/wlan.network ${D}${sysconfdir}/systemd/network/wlan.network
}

FILES:${PN} = "${sysconfdir}/systemd/network/wlan.network"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
