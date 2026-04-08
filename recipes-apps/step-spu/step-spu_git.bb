SUMMARY = "STEP-SPU Python application"
DESCRIPTION = "Fetches the STEP-SPU application from Git, installs it under the target user's home directory, and enables a systemd service."
LICENSE = "CLOSED"

inherit systemd

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
    git://github.com/ahmedsaleh99/STEP-SPU.git;protocol=ssh;user=git;branch=${STEP_SPU_BRANCH} \
    file://step-spu.service.in \
    file://90-step-spu.conf \
"

PV = "1.0+git${SRCPV}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"


STEP_SPU_BRANCH ?= "main"
STEP_SPU_INSTALL_DIR ?= "/home/${SPU_USER}/step-spu"
STEP_SPU_CA_URL ?= ""
STEP_SPU_CA_FINGERPRINT ?= ""
STEP_SPU_ROOT_CA_CERT ?= "${WORKDIR}/root_ca.crt"

RDEPENDS:${PN} += " \
    bash \
    python3-core \
    python3-modules \
    systemd \
    user-credentials \
"

DEPENDS += "step-cli-native user-credentials"

SYSTEMD_SERVICE:${PN} = "step-spu.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_fetch_root_ca() {
    if [ -z "${STEP_SPU_CA_URL}" ]; then
        bbfatal "STEP_SPU_CA_URL must be set"
    fi

    if [ -z "${STEP_SPU_CA_FINGERPRINT}" ]; then
        bbfatal "STEP_SPU_CA_FINGERPRINT must be set"
    fi

    ${STAGING_BINDIR_NATIVE}/step ca root "${STEP_SPU_ROOT_CA_CERT}" \
        --force \
        --ca-url="${STEP_SPU_CA_URL}" \
        --fingerprint="${STEP_SPU_CA_FINGERPRINT}"
}

do_check_spu_user() {
    if [ -z "${SPU_USER}" ]; then
        bbfatal "SPU_USER must be set in the distro config"
    fi
}


addtask check_spu_user after do_prepare_recipe_sysroot before do_install

do_fetch_root_ca[network] = "1"
addtask do_fetch_root_ca after check_spu_user before do_install

do_install() {
    install -d ${D}${STEP_SPU_INSTALL_DIR}/certs
    install -d ${D}${STEP_SPU_INSTALL_DIR}/src
    install -d ${D}${STEP_SPU_INSTALL_DIR}/resources
    install -d ${D}${STEP_SPU_INSTALL_DIR}/scripts
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${sysconfdir}/environment.d

    cp -r --no-preserve=ownership ${S}/src/* ${D}${STEP_SPU_INSTALL_DIR}/src/

    if [ -f ${S}/resources/test_video.mp4 ]; then
        install -m 0644 ${S}/resources/test_video.mp4 ${D}${STEP_SPU_INSTALL_DIR}/resources/test_video.mp4
    fi

    install -m 0644 ${STEP_SPU_ROOT_CA_CERT} ${D}${STEP_SPU_INSTALL_DIR}/certs/root_ca.crt

    install -m 0755 ${S}/scripts/launch_app.sh ${D}${STEP_SPU_INSTALL_DIR}/scripts/launch_app.sh

    sed -e "s|@SPU_USER@|${SPU_USER}|g" \
        -e "s|@SPU_WORK_DIR@|${STEP_SPU_INSTALL_DIR}/src|g" \
        -e "s|@SPU_APP@|${STEP_SPU_INSTALL_DIR}/scripts/launch_app.sh|g" \
        ${WORKDIR}/step-spu.service.in > ${D}${systemd_system_unitdir}/step-spu.service

    install -m 0644 ${WORKDIR}/90-step-spu.conf ${D}${sysconfdir}/environment.d/90-step-spu.conf
}

FILES:${PN} += " \
    /home \
    ${STEP_SPU_INSTALL_DIR} \
    ${STEP_SPU_INSTALL_DIR}/* \
    ${systemd_system_unitdir}/step-spu.service \
    ${sysconfdir}/environment.d/90-step-spu.conf \
"
