SUMMARY = "Preconfigured NetworkManager wifi connection"
DESCRIPTION = "Installs a default NetworkManager wifi profile generated from build-time WIFI_* variables."
LICENSE = "CLOSED"

WIFI_SSID ?= "ulsponsor"
WIFI_PASSKEY ?= ""
WIFI_HIDDEN ?= "false"

RDEPENDS:${PN} = "networkmanager-daemon networkmanager-wifi"

do_install() {
    install -d ${D}${sysconfdir}/NetworkManager/system-connections

    cat >${D}${sysconfdir}/NetworkManager/system-connections/preconfigured.nmconnection <<EOF
[connection]
id=preconfigured
type=wifi

[wifi]
mode=infrastructure
ssid=${WIFI_SSID}
hidden=${WIFI_HIDDEN}

[ipv4]
method=auto

[ipv6]
addr-gen-mode=default
method=auto

[proxy]
EOF

    if [ -n "${WIFI_PASSKEY}" ]; then
        cat >>${D}${sysconfdir}/NetworkManager/system-connections/preconfigured.nmconnection <<EOF

[wifi-security]
key-mgmt=wpa-psk
psk=${WIFI_PASSKEY}
EOF
    fi

    chmod 0600 ${D}${sysconfdir}/NetworkManager/system-connections/preconfigured.nmconnection
}

FILES:${PN} = "${sysconfdir}/NetworkManager/system-connections/preconfigured.nmconnection"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
