SUMMARY = "Luxonis XLink library"
HOMEPAGE = "https://github.com/luxonis/XLink"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/luxonis/XLink.git;protocol=https;branch=v3_develop"
SRCREV = "f001d710be6a4010db913510da08caaa3a58466c"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

DEPENDS = "libusb1 pkgconfig-native"

EXTRA_OECMAKE = " \
    -DBUILD_SHARED_LIBS=OFF \
    -DXLINK_ENABLE_LIBUSB=ON \
    -DXLINK_LIBUSB_SYSTEM=ON \
    -DXLINK_BUILD_EXAMPLES=OFF \
    -DXLINK_BUILD_TESTS=OFF \
"

FILES:${PN} += "${libdir}/cmake/XLink ${libdir}/libXLink* ${libdir}/libxlink*"
FILES:${PN}-dev += "${includedir}/XLink ${includedir}/XLink/XLinkExport.h"