SUMMARY = "smallstep step CLI native tool"
DESCRIPTION = "Installs the official smallstep step CLI for use during Yocto builds."
LICENSE = "CLOSED"

inherit native

SRC_URI = "https://github.com/smallstep/cli/releases/download/v${PV}/step_linux_${PV}_amd64.tar.gz"
SRC_URI[sha256sum] = "52e586351a2e05342a53fbaa9f91ce7b11622872a8a7a68ec27e9198d2d03460"

S = "${WORKDIR}/step_${PV}"

COMPATIBLE_HOST = "x86_64.*-linux"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/bin/step ${D}${bindir}/step
}
