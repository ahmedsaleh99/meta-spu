SUMMARY = "Build backend for CMake based projects"
HOMEPAGE = "https://github.com/scikit-build/scikit-build-core"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b4e748e5f102e31c9390dcd6fa66f09"

PYPI_PACKAGE = "scikit_build_core"

inherit pypi python_hatchling

SRC_URI += "file://0001-relax-build-system-version-pins.patch"

DEPENDS += " \
    python3-hatch-vcs-native \
"

RDEPENDS:${PN} += " \
    python3-packaging \
    python3-pathspec \
"

SRC_URI[sha256sum] = "562e0bbc9de1a354c87825ccf732080268d6582a0200f648e8c4a2dcb1e3736d"

BBCLASSEXTEND = "native"
