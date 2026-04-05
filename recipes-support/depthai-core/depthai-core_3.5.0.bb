SUMMARY = "DepthAI core library"
HOMEPAGE = "https://github.com/luxonis/depthai-core"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=39a6484daa66eaf84c7ee9c45e34db02"

SRC_URI = "gitsm://github.com/luxonis/depthai-core.git;protocol=https;branch=main \
           file://0001-cmake-add-yocto-sysroot-fallback-for-dynamic-calibration.patch \
           file://0002-cmake-fix-xlinkpublic-propagation.patch \
           "
SRCREV = "v3.5.0"

S = "${WORKDIR}/git"

inherit cmake pkgconfig python3native python3-dir

PACKAGES =+ "${PN}-python"

# depthai-core installs a real unversioned shared object, not a symlinked
# linker stub, so keep it in the runtime package.
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

DEPENDS = " \
    libusb1 \
    fp16 \
    python3 \
    python3-numpy\
    python3-pybind11 \
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

EXTRA_OECMAKE += " \
    -DBUILD_SHARED_LIBS=ON \
    -DDEPTHAI_BOOTSTRAP_VCPKG=OFF \
    -DDEPTHAI_BUILD_PYTHON=ON \
    -DFETCHCONTENT_FULLY_DISCONNECTED:BOOL=OFF \
    -DDEPTHAI_ENABLE_REMOTE_CONNECTION=OFF \
    -DDEPTHAI_JSON_EXTERNAL=ON \
    -DDEPTHAI_LIBNOP_EXTERNAL=ON \
    -DDEPTHAI_XTENSOR_EXTERNAL=ON \
    -DDEPTHAI_ENABLE_MP4V2=OFF \
    -DProtobuf_PROTOC_EXECUTABLE=${RECIPE_SYSROOT_NATIVE}${bindir}/protoc \
    "

do_configure:prepend() {
    awk '
        BEGIN {
            skip = 0
        }
        /^# XLink$/ {
            print "# XLink"
            print "# Use the Yocto-provided XLink package instead of FetchContent."
            print "set(_BUILD_SHARED_LIBS_SAVED \"${BUILD_SHARED_LIBS}\")"
            print "set(BUILD_SHARED_LIBS OFF)"
            print "if(DEPTHAI_ENABLE_LIBUSB)"
            print "    find_package(usb-1.0 ${_QUIET} CONFIG REQUIRED)"
            print "endif()"
            print "find_package(XLink ${_QUIET} CONFIG REQUIRED)"
            print "if(TARGET XLinkPublic)"
            print "    get_target_property(_depthai_xlinkpublic_imported XLinkPublic IMPORTED)"
            print "    if(NOT _depthai_xlinkpublic_imported)"
            print "        list(APPEND targets_to_export XLinkPublic)"
            print "    endif()"
            print "    unset(_depthai_xlinkpublic_imported)"
            print "endif()"
            print "set(BUILD_SHARED_LIBS \"${_BUILD_SHARED_LIBS_SAVED}\")"
            print "unset(_BUILD_SHARED_LIBS_SAVED)"
            skip = 1
            next
        }
        skip && /^# OpenCV 4 - \(optional\)$/ {
            skip = 0
            print
            next
        }
        !skip {
            print
        }
    ' ${S}/cmake/depthaiDependencies.cmake > ${S}/cmake/depthaiDependencies.cmake.new
    mv ${S}/cmake/depthaiDependencies.cmake.new ${S}/cmake/depthaiDependencies.cmake

    sed -i 's#COMMAND protobuf::protoc #COMMAND ${RECIPE_SYSROOT_NATIVE}${bindir}/protoc #g' ${S}/protos/CMakeLists.txt
    sed -i 's#<fmt/base.h>#<fmt/core.h>#g' ${S}/src/utility/ObjectTrackerImpl.cpp
}

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
