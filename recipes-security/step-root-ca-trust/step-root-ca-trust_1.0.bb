SUMMARY = "Install STEP root CA into system trust store"
DESCRIPTION = "Fetches the STEP root CA and installs it so update-ca-certificates trusts it system-wide."
HOMEPAGE = "https://smallstep.com"
LICENSE = "CLOSED"

inherit allarch

DEPENDS = "step-cli-native"
RDEPENDS:${PN} = "ca-certificates"

S = "${WORKDIR}"
localdatadir = "${prefix}/local/share"

STEP_SPU_ROOT_CA_CERT ?= "${WORKDIR}/root_ca.crt"

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

do_fetch_root_ca[network] = "1"
addtask do_fetch_root_ca after do_prepare_recipe_sysroot before do_install

do_install() {
    install -m 0755 -d ${D}${localdatadir}/ca-certificates/step
    install -m 0444 ${STEP_SPU_ROOT_CA_CERT} ${D}${localdatadir}/ca-certificates/step/root_ca.crt
}

FILES:${PN} += " \
    ${localdatadir}/ca-certificates/step/ \
"

pkg_postinst:${PN} () {
    set -e
    sysroot_args=""
    [ -n "$D" ] && sysroot_args="--sysroot $D"
    $D${sbindir}/update-ca-certificates $sysroot_args
}
