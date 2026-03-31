SUMMARY = "STEP-SPU application payload"
DESCRIPTION = "Installs STEP-SPU application payload, helper scripts, and systemd units"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
    file://launch_app.sh \
    file://first_run.sh \
    file://step-spu.service.in \
    file://xorg.service.in \
"

S = "${WORKDIR}"

SPU_USER ?= "spu-user"
SPU_INSTALL_DIR ?= "/home/${SPU_USER}/step-spu"
SPU_PROJECT_DIR ?= "/home/cvip/Work/STEP-SPU"

inherit systemd
inherit useradd

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "-r ${SPU_USER}"
USERADD_PARAM:${PN} = "-r -m -d /home/${SPU_USER} -s /bin/bash -g ${SPU_USER} ${SPU_USER}"

SYSTEMD_SERVICE:${PN} = "step-spu.service xorg.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install() {
    install -d ${D}${SPU_INSTALL_DIR}/src
    install -d ${D}${SPU_INSTALL_DIR}/resources
    install -d ${D}${SPU_INSTALL_DIR}/scripts
    install -d ${D}${sysconfdir}/systemd/system

    cp -r ${SPU_PROJECT_DIR}/src/* ${D}${SPU_INSTALL_DIR}/src/
    cp -r ${SPU_PROJECT_DIR}/resources/* ${D}${SPU_INSTALL_DIR}/resources/

    install -m 0755 ${WORKDIR}/launch_app.sh ${D}${SPU_INSTALL_DIR}/scripts/launch_app.sh
    install -m 0755 ${WORKDIR}/first_run.sh ${D}${SPU_INSTALL_DIR}/scripts/first_run.sh

    # Render service files with the same placeholders used in spu_img_gen.
    sed \
      -e 's|$SPU_USER|${SPU_USER}|g' \
      -e 's|$SPU_WORK_DIR|${SPU_INSTALL_DIR}|g' \
      -e 's|$SPU_APP|${SPU_INSTALL_DIR}/scripts/launch_app.sh|g' \
      ${WORKDIR}/step-spu.service.in \
      > ${D}${sysconfdir}/systemd/system/step-spu.service

    sed \
      -e 's|$SPU_USER|${SPU_USER}|g' \
      ${WORKDIR}/xorg.service.in \
      > ${D}${sysconfdir}/systemd/system/xorg.service

}

pkg_postinst:${PN} () {
    if [ -n "$D" ]; then
        exit 0
    fi

    if id "${SPU_USER}" >/dev/null 2>&1; then
        chown -R "${SPU_USER}:${SPU_USER}" "/home/${SPU_USER}/step-spu" || true
    fi
}

FILES:${PN} += " \
    ${SPU_INSTALL_DIR} \
    ${sysconfdir}/systemd/system/step-spu.service \
    ${sysconfdir}/systemd/system/xorg.service \
"

RDEPENDS:${PN} += "bash python3 sudo"
