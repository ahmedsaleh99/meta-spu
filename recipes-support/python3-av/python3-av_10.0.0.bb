SUMMARY = "Pythonic bindings for FFmpeg's libraries."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=a9c4cea4308c4521674ecd7c3255d8af"

DEPENDS = "python3-cython-native ffmpeg"


# important to uUse older Cython 2.29.36 for compatibility with PyAV FFmpeg 4.4.3, see
# https://github.com/PyAV-Org/PyAV/issues/1140
PREFERRED_VERSION_python3-cython-native = "2.29.36"

inherit python_setuptools_build_meta pkgconfig

SRC_URI = "git://github.com/PyAV-Org/PyAV.git;protocol=https;branch=main"

SRCREV = "bc4eedd5fc474e0f25b22102b2771fe5a42bb1c7"

S = "${WORKDIR}/git"

RDEPENDS:${PN} = "ffmpeg"
