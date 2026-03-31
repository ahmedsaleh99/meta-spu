SUMMARY = "STEP-SPU system configuration (chrony, sudoers, i2c, wifi seed)"
DESCRIPTION = "Installs baseline configuration files for STEP-SPU runtime behavior"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
    file://chrony.conf \
    file://sudoers.spu-user \
    file://preconfigured.nmconnection \
"

S = "${WORKDIR}"
SPU_USER ?= "spu-user"
SPU_INSTALL_DIR ?= "/home/${SPU_USER}/step-spu"
SPU_WIFI_SSID ?= "ChangeSSID"
SPU_WIFI_PSK ?= ""

do_install() {
    install -d ${D}${sysconfdir}/chrony
    install -d ${D}${sysconfdir}/sudoers.d
    install -d ${D}${sysconfdir}/modules-load.d
    install -d ${D}${sysconfdir}/NetworkManager/system-connections

    install -m 0644 ${WORKDIR}/chrony.conf ${D}${sysconfdir}/chrony/chrony.conf

    sed -e 's|$SPU_USER|${SPU_USER}|g' ${WORKDIR}/sudoers.spu-user > ${D}${sysconfdir}/sudoers.d/${SPU_USER}
    chmod 0440 ${D}${sysconfdir}/sudoers.d/${SPU_USER}

    printf '%s\n' 'i2c-dev' 'i2c-bcm2835' > ${D}${sysconfdir}/modules-load.d/step-spu-i2c.conf

    sed -e 's|__SPU_WIFI_SSID__|${SPU_WIFI_SSID}|g' ${WORKDIR}/preconfigured.nmconnection \
        > ${D}${sysconfdir}/NetworkManager/system-connections/preconfigured.nmconnection

    if [ -n "${SPU_WIFI_PSK}" ]; then
        printf '\n[wifi-security]\nkey-mgmt=wpa-psk\npsk=%s\n' "${SPU_WIFI_PSK}" \
            >> ${D}${sysconfdir}/NetworkManager/system-connections/preconfigured.nmconnection
    fi

    chmod 0600 ${D}${sysconfdir}/NetworkManager/system-connections/preconfigured.nmconnection
}

FILES:${PN} += " \
    ${sysconfdir}/chrony/chrony.conf \
    ${sysconfdir}/sudoers.d/${SPU_USER} \
    ${sysconfdir}/modules-load.d/step-spu-i2c.conf \
    ${sysconfdir}/NetworkManager/system-connections/preconfigured.nmconnection \
"

CONFFILES:${PN} += " \
    ${sysconfdir}/chrony/chrony.conf \
    ${sysconfdir}/sudoers.d/${SPU_USER} \
    ${sysconfdir}/modules-load.d/step-spu-i2c.conf \
    ${sysconfdir}/NetworkManager/system-connections/preconfigured.nmconnection \
"

RDEPENDS:${PN} += "chrony sudo networkmanager"
