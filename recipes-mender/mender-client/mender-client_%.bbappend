FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://mender.conf.in"

SPU_MENDER_SERVER_URL ?= "https://mender.example.com"

do_install:append() {
    install -d ${D}${sysconfdir}/mender
    sed -e 's|__MENDER_SERVER_URL__|${SPU_MENDER_SERVER_URL}|g' \
        ${WORKDIR}/mender.conf.in > ${D}${sysconfdir}/mender/mender.conf
}
