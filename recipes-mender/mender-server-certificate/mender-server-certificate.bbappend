# Reuse the STEP CA root certificate as the Mender server certificate.
# mender-server-certificate expects ${WORKDIR}/server.crt during do_install.

DEPENDS:append = " step-cli-native"

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

do_install:prepend() {
    install -m 0644 ${STEP_SPU_ROOT_CA_CERT} ${WORKDIR}/server.crt
}

pkg_postinst:${PN} () {
    set -e
    sysroot_args=""
    [ -n "$D" ] && sysroot_args="--sysroot $D"
    $D${sbindir}/update-ca-certificates $sysroot_args
}
