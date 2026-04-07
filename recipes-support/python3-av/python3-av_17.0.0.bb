SUMMARY = "Pythonic bindings for FFmpeg's libraries."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=a9c4cea4308c4521674ecd7c3255d8af"

DEPENDS = "python3-cython-native ffmpeg"

inherit python_setuptools_build_meta pkgconfig

SRC_URI = "git://github.com/PyAV-Org/PyAV.git;protocol=https;branch=main"
SRCREV = "1f2b3ad8268b2bbf116567e2f60ac3570d041d92"

PV = "17.0.0+git${SRCPV}"
S = "${WORKDIR}/git"

RDEPENDS:${PN} = "ffmpeg"
