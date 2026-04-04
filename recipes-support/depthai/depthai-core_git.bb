SUMMARY = "Luxonis DepthAI core library with Python bindings"
HOMEPAGE = "https://github.com/luxonis/depthai-core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=39a6484daa66eaf84c7ee9c45e34db02"

SRC_URI = "gitsm://github.com/luxonis/depthai-core.git;protocol=https;branch=main"
PV = "3.5.0+git${SRCPV}"
SRCREV = "527ad9d64294d73ba0e1ab58e762be720f039b42"

S = "${WORKDIR}/git"

inherit cmake python3native python3-dir pkgconfig

# depthai installs an unversioned shared object (libdepthai-core.so) as the
# real runtime library, so treat .so as runtime content instead of -dev symlink.
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

DEPENDS = " \
    bzip2 \
    cmake-native \
    fmt \
    libeigen \
    libarchive \
    libnop \
    libusb1 \
    fp16 \
    httplib \
    lz4 \
    magic-enum \
    nlohmann-json \
    openssl \
    python3-pybind11 \
    python3 \
    python3-native \
    python3-numpy-native \
    semver \
    spdlog \
    xtl \
    xtensor \
    xsimd \
    xlink \
    yaml-cpp \
    zlib \
"

PACKAGECONFIG ??= "opencv"
PACKAGECONFIG[opencv] = "-DDEPTHAI_OPENCV_SUPPORT=ON,-DDEPTHAI_OPENCV_SUPPORT=OFF,opencv"
PACKAGECONFIG[pcl] = "-DDEPTHAI_PCL_SUPPORT=ON,-DDEPTHAI_PCL_SUPPORT=OFF,pcl"

EXTRA_OECMAKE = " \
    -DBUILD_SHARED_LIBS=ON \
    -DDEPTHAI_BOOTSTRAP_VCPKG=OFF \
    -DDEPTHAI_XTENSOR_EXTERNAL=ON \
    -DDEPTHAI_XLINK_EXTERNAL=ON \
    -DDEPTHAI_LIBNOP_EXTERNAL=ON \
    -DDEPTHAI_JSON_EXTERNAL=ON \
    -DDEPTHAI_BUILD_PYTHON=ON \
    -DDEPTHAI_BUILD_TESTS=OFF \
    -DDEPTHAI_BUILD_EXAMPLES=OFF \
    -DDEPTHAI_BUILD_DOCS=OFF \
    -DDEPTHAI_BUILD_ZOO_HELPER=OFF \
    -DDEPTHAI_ENABLE_BACKWARD=OFF \
    -DDEPTHAI_ENABLE_CURL=OFF \
    -DDEPTHAI_ENABLE_APRIL_TAG=OFF \
    -DDEPTHAI_ENABLE_DEVICE_BOOTLOADER_FW=ON \
    -DDEPTHAI_ENABLE_DEVICE_FW=ON \
    -DDEPTHAI_ENABLE_DEVICE_RVC3_FW=OFF \
    -DDEPTHAI_ENABLE_DEVICE_RVC4_FW=ON \
    -DDEPTHAI_ENABLE_MP4V2=OFF \
    -DDEPTHAI_ENABLE_PROTOBUF=OFF \
    -DDEPTHAI_ENABLE_REMOTE_CONNECTION=OFF \
    -DDEPTHAI_FETCH_ARTIFACTS=OFF \
    -DDEPTHAI_BINARIES_RESOURCE_COMPILE=ON \
    -DDEPTHAI_DYNAMIC_CALIBRATION_SUPPORT=OFF \
    -DDEPTHAI_XTENSOR_SUPPORT=ON \
    -DDEPTHAI_PYTHON_BUILD_DOCSTRINGS=ON \
    -DDEPTHAI_ENABLE_LIBUSB=ON \
    -DDEPTHAI_INSTALL=ON \
    -DDEPTHAI_MERGED_TARGET=ON \
"

do_configure:prepend() {
    # In Yocto, the external xlink recipe exports XLink, not XLinkPublic.
    # Replace XLinkPublic with XLink in source, but exclude from depthai's own exports.
    sed -i 's/\<XLinkPublic\>/XLink/g' ${S}/CMakeLists.txt
    sed -i 's/\<XLinkPublic\>/XLink/g' ${S}/cmake/depthaiConfig.cmake.in
    sed -i 's/\<XLinkPublic\>/XLink/g' ${S}/cmake/depthaiDependencies.cmake
    sed -i 's/\<XLinkPublic\>/XLink/g' ${S}/bindings/python/CMakeLists.txt

    # XLink is imported from external package; don't export it as a target built by depthai-core
    sed -i '/list(APPEND targets_to_export XLink)/d' ${S}/cmake/depthaiDependencies.cmake

    # ndarray_converter.h is required by multiple bindings sources regardless of
    # DEPTHAI_MERGED_TARGET state in this Yocto build.
    sed -i '/target_include_directories(${TARGET_NAME} PRIVATE src .*${CMAKE_CURRENT_LIST_DIR}\/..\/..\/src")/a\
    target_include_directories(${TARGET_NAME} PRIVATE external/pybind11_opencv_numpy)' ${S}/bindings/python/CMakeLists.txt
}

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