SUMMARY = "Fast JPEG encoding and decoding for Python"
DESCRIPTION = "simplejpeg provides fast JPEG encoding and decoding for Python using libturbojpeg and NumPy arrays."
HOMEPAGE = "https://github.com/jfolz/simplejpeg"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1d325f91caffc43417b4c7b97d45f574"

SRCREV = "85c7be6c3c23e411fa5dfaadd05198bb767becf3"
SRC_URI = "git://github.com/jfolz/simplejpeg;protocol=https;branch=master \
           file://0001-add-dynamic-linking-mode-for-yocto.patch \
           file://0002-relax-pep517-build-requirements-for-yocto.patch \
"

S = "${WORKDIR}/git"

inherit python_setuptools_build_meta

DEPENDS += " \
    cmake-native \
    libjpeg-turbo \
    python3-cython-native \
    python3-numpy-native \
    python3-setuptools-native \
    python3-wheel-native \
"

RDEPENDS:${PN} += " \
    libturbojpeg \
    python3-numpy \
"

export SIMPLEJPEG_DYNAMIC_LINKING = "1"
export SKIP_YASM_BUILD = "1"
