SUMMARY = "DepthAI Python bindings built from depthai-core"
DESCRIPTION = "Builds and installs the depthai Python module from luxonis/depthai-core"
HOMEPAGE = "https://github.com/luxonis/depthai-core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/luxonis/depthai-core.git;branch=main;protocol=https"
SRCREV = "c524312b9ffd21ddfe2478602b834b450c3251bb"

PV = "3.5.0+git${SRCPV}"
S = "${WORKDIR}/git"

inherit cmake python3-dir pkgconfig

DEPENDS = " \
    python3 \
    pybind11 \
"

EXTRA_OECMAKE += " \
    -DDEPTHAI_BUILD_PYTHON=ON \
    -DDEPTHAI_BUILD_TESTS=OFF \
    -DDEPTHAI_BUILD_EXAMPLES=OFF \
    -DDEPTHAI_BUILD_DOCS=OFF \
    -DDEPTHAI_INSTALL=ON \
    -DBUILD_SHARED_LIBS=ON \
    -DDEPTHAI_BOOTSTRAP_VCPKG=OFF \
    -DDEPTHAI_FETCH_ARTIFACTS=OFF \
    -DDEPTHAI_ENABLE_DEVICE_FW=OFF \
    -DDEPTHAI_ENABLE_DEVICE_BOOTLOADER_FW=OFF \
    -DDEPTHAI_ENABLE_DEVICE_RVC3_FW=OFF \
    -DDEPTHAI_ENABLE_DEVICE_RVC4_FW=OFF \
    -DDEPTHAI_BINARIES_RESOURCE_COMPILE=OFF \
    -DDEPTHAI_ENABLE_PROTOBUF=OFF \
    -DDEPTHAI_ENABLE_CURL=OFF \
    -DDEPTHAI_ENABLE_REMOTE_CONNECTION=OFF \
    -DDEPTHAI_ENABLE_EVENTS_MANAGER=OFF \
    -DDEPTHAI_ENABLE_MP4V2=OFF \
    -DDEPTHAI_ENABLE_BACKWARD=OFF \
    -DDEPTHAI_DYNAMIC_CALIBRATION_SUPPORT=OFF \
"

do_install:append() {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}

    module_path="$(find ${B} -maxdepth 4 -type f -name 'depthai*.so' | head -n 1)"
    if [ -z "${module_path}" ]; then
        bbfatal "Unable to find built depthai python module in ${B}"
    fi

    install -m 0755 ${module_path} ${D}${PYTHON_SITEPACKAGES_DIR}/
}

FILES:${PN} += " \
    ${PYTHON_SITEPACKAGES_DIR} \
    ${libdir}/*.so* \
"

RDEPENDS:${PN} += " \
    python3-core \
    python3-numpy \
"
