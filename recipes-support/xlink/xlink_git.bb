SUMMARY = "Luxonis XLink library"
HOMEPAGE = "https://github.com/luxonis/XLink"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRC_URI = "git://github.com/luxonis/XLink.git;protocol=https;branch=v3_develop"
SRCREV = "f001d710be6a4010db913510da08caaa3a58466c"

PV = "3.0+git${SRCPV}"
S = "${WORKDIR}/git"

inherit cmake pkgconfig

DEPENDS = "libusb1 pkgconfig-native"

EXTRA_OECMAKE += " \
    -DBUILD_SHARED_LIBS=OFF \
    -DXLINK_ENABLE_LIBUSB=ON \
    -DXLINK_LIBUSB_SYSTEM=OFF \
    -DXLINK_BUILD_EXAMPLES=OFF \
    -DXLINK_BUILD_TESTS=OFF \
"

FILES:${PN} += " \
    ${libdir}/cmake/XLink \
    ${libdir}/libXLink* \
    ${libdir}/libxlink* \
"

FILES:${PN}-dev += " \
    ${includedir}/XLink \
    ${includedir}/XLink/XLinkExport.h \
"
