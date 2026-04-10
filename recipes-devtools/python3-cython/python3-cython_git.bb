SUMMARY = "The Cython compiler for writing C extensions for Python"
DESCRIPTION = "Cython is an optimising static compiler for both the Python programming language and the extended Cython programming language. It makes writing C extensions for Python as easy as Python itself."
HOMEPAGE = "https://cython.org"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=61c3ee8961575861fa86c7e62bc9f69c"

SRC_URI = "git://github.com/cython/cython.git;protocol=https;branch=master"
# PyAV 17 uses the "freethreading_compatible" directive, which requires Cython 3.1+.
SRCREV = "21bda420d583bcf464fb9efb932b09c5f3db1efd"

S = "${WORKDIR}/git"

inherit python_setuptools_build_meta

DEFAULT_PREFERENCE = "1"

RDEPENDS:${PN} = ""

BBCLASSEXTEND = "native nativesdk"