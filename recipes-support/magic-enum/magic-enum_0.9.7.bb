SUMMARY = "Static reflection for enums for modern C++"
HOMEPAGE = "https://github.com/Neargye/magic_enum"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7e7717cf723eb72f57e80fdb651cb318"

SRC_URI = "git://github.com/Neargye/magic_enum.git;protocol=https;branch=master"
SRCREV = "e046b69a3736d314fad813e159b1c192eaef92cd"
PV = "0.9.7+git${SRCPV}"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE += " \
    -DMAGIC_ENUM_OPT_BUILD_EXAMPLES=OFF \
    -DMAGIC_ENUM_OPT_BUILD_TESTS=OFF \
    -DMAGIC_ENUM_OPT_INSTALL=ON \
"

ALLOW_EMPTY:${PN} = "1"

FILES:${PN}-dev += " \
    ${includedir}/magic_enum \
    ${libdir}/cmake/magic_enum \
    ${libdir}/pkgconfig \
    ${datadir}/magic_enum \
"
