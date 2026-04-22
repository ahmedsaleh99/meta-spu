SUMMARY = "DepthAI core library"
HOMEPAGE = "https://github.com/luxonis/depthai-core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=39a6484daa66eaf84c7ee9c45e34db02"

SRC_URI = "gitsm://github.com/luxonis/depthai-core.git;protocol=https;branch=main \
           file://0001-cmake-add-yocto-sysroot-fallback-for-dynamic-calibration.patch \
           file://0002-cmake-fix-xlinkpublic-propagation.patch \
           file://0003-cmake-dont-export-imported-xlink.patch \
           file://0004-cmake-use-system-xlink.patch \
           file://0005-protos-use-protobuf-protoc-executable.patch \
           "
SRCREV = "c524312b9ffd21ddfe2478602b834b450c3251bb"

S = "${WORKDIR}/git"

inherit cmake pkgconfig python3native python3-dir

PACKAGES =+ "${PN}-python"
ALLOW_EMPTY:${PN} = "1"

# depthai-core installs a real unversioned shared object, not a symlinked
# linker stub, so keep it in the runtime package.
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

DEPENDS = " \
    libusb1 \
    fp16 \
    python3 \
    python3-numpy \
    python3-numpy-native \
    pybind11-luxonis \
    lz4 \
    libarchive \
    xz \
    httplib \
    libeigen  \
    spdlog \
    fmt \
    backward-cpp \
    yaml-cpp \
    semver \
    curl \
    libcpr \
    apriltag \
    magic-enum \
    nlohmann-json \
    opencv \
    protobuf \
    protobuf-native \
    websocketpp \
    xlink \
    dynamic-calibration \
    xtensor \
    xtl \
    libnop \
    "
RDEPENDS:${PN} = " \
    python3-numpy \
    "
EXTRA_OECMAKE += " \
    -DDEPTHAI_BOOTSTRAP_VCPKG=OFF \
    -DDEPTHAI_BUILD_PYTHON=ON \
    -DDEPTHAI_ENABLE_REMOTE_CONNECTION=OFF \
    -DDEPTHAI_JSON_EXTERNAL=ON \
    -DDEPTHAI_LIBNOP_EXTERNAL=ON \
    -DDEPTHAI_XTENSOR_EXTERNAL=ON \
    -DDEPTHAI_ENABLE_MP4V2=OFF \
    "

do_install:append() {
    rm -f ${D}${libdir}/libdynamic_calibration.so
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
    rm -f ${D}${PYTHON_SITEPACKAGES_DIR}/depthai*.so
    install -m 0755 ${B}/bindings/python/depthai*.so ${D}${PYTHON_SITEPACKAGES_DIR}/depthai.so
}

FILES:${PN} += "${libdir}/libdepthai-core.so"
FILES:${PN}-python = "${PYTHON_SITEPACKAGES_DIR}/depthai.so"
RDEPENDS:${PN}-python += "python3-core"
INSANE_SKIP:${PN}-src += "buildpaths"
