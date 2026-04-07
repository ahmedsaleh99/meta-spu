SUMMARY = "The Cython compiler for writing C extensions for Python"
DESCRIPTION = "Cython is an optimising static compiler for both the Python programming language and the extended Cython programming language. It makes writing C extensions for Python as easy as Python itself."
HOMEPAGE = "https://cython.org"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e23fadd6ceef8c618fc1c65191d846fa"

SRC_URI = "git://github.com/cython/cython.git;protocol=https;branch=master;tag=0.29.36"
# this version is compatible with required by PyAV FFmpeg 4.4.3, see
# https://github.com/PyAV-Org/PyAV/issues/1140
SRCREV = "0.29.36"

S = "${WORKDIR}/git"

inherit python_setuptools_build_meta

DEFAULT_PREFERENCE = "1"

RDEPENDS:${PN} = ""

BBCLASSEXTEND = "native nativesdk"
