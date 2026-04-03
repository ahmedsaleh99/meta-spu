FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://wpa_supplicant-wlan0.conf"

FILES:${PN}:append = " /data/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf"

WIFI_SSID ?= "ulsponsor"
WIFI_PASSKEY ?= ""

do_install:append() {
    install -d ${D}${sysconfdir}/wpa_supplicant

    if [ -n "${WIFI_PASSKEY}" ]; then
        WIFI_KEY_MGMT="WPA-PSK"
        WIFI_PSK_LINE="    psk=\"${WIFI_PASSKEY}\""
    else
        WIFI_KEY_MGMT="NONE"
        WIFI_PSK_LINE=""
    fi

    sed -i \
        -e "s#[@]WIFI_SSID[@]#${WIFI_SSID}#" \
        -e "s#[@]WIFI_KEY_MGMT[@]#${WIFI_KEY_MGMT}#" \
        -e "s#[@]WIFI_PSK_LINE[@]#${WIFI_PSK_LINE}#" \
        ${WORKDIR}/wpa_supplicant-wlan0.conf

    install -d ${D}/data/wpa_supplicant
    install -m 0600 ${WORKDIR}/wpa_supplicant-wlan0.conf ${D}/data/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf
    ln -sf /data/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf ${D}${sysconfdir}/wpa_supplicant/wpa_supplicant-nl80211-wlan0.conf

    install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants
    ln -sf ${systemd_unitdir}/system/wpa_supplicant-nl80211@.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/wpa_supplicant-nl80211@wlan0.service
}
