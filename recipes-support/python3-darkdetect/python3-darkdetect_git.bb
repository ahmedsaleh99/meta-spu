SUMMARY = "Detect OS dark mode from Python"
HOMEPAGE = "https://github.com/albertosottile/darkdetect"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d67b2f1ea4ec516766de3c4f762dbe09"

SRC_URI = "git://github.com/albertosottile/darkdetect.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

PV = "0.8.0+git${SRCPV}"
S = "${WORKDIR}/git"

inherit python_setuptools_build_meta
