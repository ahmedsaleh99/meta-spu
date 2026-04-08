SUMMARY = "Pythonic bindings for FFmpeg's libraries."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=a9c4cea4308c4521674ecd7c3255d8af"

DEPENDS = "python3-cython-native python3-setuptools-native ffmpeg"

PYPI_PACKAGE = "av"

inherit pypi python_setuptools_build_meta pkgconfig

SRC_URI += "file://0001-pyproject-license-use-table-form.patch"
SRC_URI[sha256sum] = "c275830f9ad575a2da941177b81d11b442271d205ddb5cd27fa82930aa69b38e"

RDEPENDS:${PN} = "ffmpeg"
