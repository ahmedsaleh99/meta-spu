SUMMARY = "Luxonis DepthAI core library with Python bindings"
HOMEPAGE = "https://github.com/luxonis/depthai-core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=39a6484daa66eaf84c7ee9c45e34db02"

SRC_URI = "git://github.com/luxonis/depthai-core.git;protocol=https;branch=main"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake python3native python3-dir pkgconfig

DEPENDS = "
    bzip2
    cmake-native
    eigen
    libarchive
    libusb1
    lz4
    openssl
    pybind11
    python3
    python3-dev
    python3-native
    python3-numpy-native
    xtensor
    xtl
    spdlog
    zlib
"

PACKAGECONFIG ??= ""
PACKAGECONFIG[opencv] = "-DDEPTHAI_OPENCV_SUPPORT=ON,-DDEPTHAI_OPENCV_SUPPORT=OFF,opencv"
PACKAGECONFIG[pcl] = "-DDEPTHAI_PCL_SUPPORT=ON,-DDEPTHAI_PCL_SUPPORT=OFF,pcl"

EXTRA_OECMAKE = " \
    -DDEPTHAI_BOOTSTRAP_VCPKG=OFF \
    -DDEPTHAI_VCPKG_INTERNAL_ONLY=OFF \
    -DDEPTHAI_BUILD_PYTHON=ON \
    -DDEPTHAI_BUILD_TESTS=OFF \
    -DDEPTHAI_BUILD_EXAMPLES=OFF \
    -DDEPTHAI_BUILD_DOCS=OFF \
    -DDEPTHAI_BUILD_ZOO_HELPER=OFF \
    -DDEPTHAI_PYTHON_BUILD_DOCSTRINGS=OFF \
    -DDEPTHAI_ENABLE_BACKWARD=OFF \
    -DDEPTHAI_ENABLE_REMOTE_CONNECTION=OFF \
    -DDEPTHAI_ENABLE_CURL=OFF \
    -DDEPTHAI_ENABLE_PROTOBUF=OFF \
    -DDEPTHAI_ENABLE_APRIL_TAG=OFF \
    -DDEPTHAI_ENABLE_MP4V2=OFF \
    -DDEPTHAI_FETCH_ARTIFACTS=OFF \
    -DDEPTHAI_ENABLE_LIBUSB=ON \
    -DDEPTHAI_MERGED_TARGET=ON \
"

PACKAGES += "${PN}-python"

do_install:append() {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
    install -m 0755 ${B}/bindings/python/depthai*.so ${D}${PYTHON_SITEPACKAGES_DIR}/

    install -d ${D}${PYTHON_SITEPACKAGES_DIR}/depthai_cli
    install -m 0644 ${S}/bindings/python/depthai_cli/*.py ${D}${PYTHON_SITEPACKAGES_DIR}/depthai_cli/
    install -m 0644 ${S}/utilities/cam_test.py ${D}${PYTHON_SITEPACKAGES_DIR}/depthai_cli/cam_test.py
    install -m 0644 ${S}/utilities/cam_test_gui.py ${D}${PYTHON_SITEPACKAGES_DIR}/depthai_cli/cam_test_gui.py
    install -m 0644 ${S}/utilities/stress_test.py ${D}${PYTHON_SITEPACKAGES_DIR}/depthai_cli/stress_test.py
}

FILES:${PN} += "${libdir}/cmake/depthai ${libdir}/libdepthai* ${datadir}/depthai"
FILES:${PN}-dev += "${includedir}"
FILES:${PN}-python = "${PYTHON_SITEPACKAGES_DIR}/depthai*.so ${PYTHON_SITEPACKAGES_DIR}/depthai_cli ${PYTHON_SITEPACKAGES_DIR}/depthai_cli/*"

RDEPENDS:${PN}-python += "python3-core python3-numpy"