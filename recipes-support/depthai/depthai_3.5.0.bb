SUMMARY = "DepthAI core C++ library and firmware assets"
DESCRIPTION = "DepthAI core library for Luxonis OAK devices, packaged without ROS integration."
HOMEPAGE = "https://www.luxonis.com/"
SECTION = "devel"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=39a6484daa66eaf84c7ee9c45e34db02"

SRC_URI = "gitsm://github.com/luxonis/depthai-core.git;protocol=https;nobranch=1"
SRCREV = "c524312b9ffd21ddfe2478602b834b450c3251bb"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE += " \
    -DDEPTHAI_BOOTSTRAP_VCPKG=OFF \
    -DBUILD_SHARED_LIBS=ON \
    -DDEPTHAI_BUILD_EXAMPLES=OFF \
    -DDEPTHAI_BUILD_TESTS=OFF \
    -DDEPTHAI_BUILD_DOCS=OFF \
"

DEPENDS = " \
    curl \
    fmt \
    libusb1 \
    nlohmann-json \
    opencv \
    spdlog \
    udev \
"

RDEPENDS:${PN} += " \
    libusb1 \
    udev \
"
