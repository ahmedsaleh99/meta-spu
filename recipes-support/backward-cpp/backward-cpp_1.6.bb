SUMMARY = "Beautiful stack trace pretty printer for C++"
HOMEPAGE = "https://github.com/bombela/backward-cpp"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=74ea9734864a5514a5d672f385e3b382"

SRC_URI = "git://github.com/bombela/backward-cpp.git;branch=master;protocol=https"
SRCREV = "3bb9240cb15459768adb3e7d963a20e1523a6294"
PV = "1.6+git${SRCPV}"

S = "${WORKDIR}/git"

inherit cmake

DEPENDS = "elfutils libunwind"

EXTRA_OECMAKE += " \
    -DBACKWARD_TESTS=OFF \
    -DBACKWARD_EXAMPLES=OFF \
"

FILES:${PN}-dev += " \
    ${includedir}/backward.hpp \
    ${libdir}/backward \
"

RDEPENDS:${PN}-dev += "elfutils-dev libunwind-dev"
