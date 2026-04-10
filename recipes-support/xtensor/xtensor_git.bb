SUMMARY = "Multi-dimensional arrays with broadcasting and lazy computing"
HOMEPAGE = "https://github.com/luxonis/xtensor"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5c67ec4d3eb9c5b7eed4c37e69571b93"

SRC_URI = "git://github.com/luxonis/xtensor.git;protocol=https;branch=master"
SRCREV = "d070cfcba9418ec45e5399708100eee1181a9573"

PV = "0.26.0+git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS += "xtl"

do_install() {
    install -d ${D}${includedir}
    cp -r ${S}/include/* ${D}${includedir}/

    install -d ${D}${libdir}/cmake/xtensor
    cat > ${D}${libdir}/cmake/xtensor/xtensorConfig.cmake <<'EOF'
include(CMakeFindDependencyMacro)
find_dependency(xtl 0.7.0 CONFIG)

get_filename_component(PACKAGE_PREFIX_DIR "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)

set(XTENSOR_VERSION_MAJOR 0)
set(XTENSOR_VERSION_MINOR 26)
set(XTENSOR_VERSION_PATCH 0)
set(xtensor_VERSION "${XTENSOR_VERSION_MAJOR}.${XTENSOR_VERSION_MINOR}.${XTENSOR_VERSION_PATCH}")

if(NOT TARGET xtensor)
    add_library(xtensor INTERFACE IMPORTED)
    set_target_properties(xtensor PROPERTIES
        INTERFACE_INCLUDE_DIRECTORIES "${PACKAGE_PREFIX_DIR}/include"
        INTERFACE_SYSTEM_INCLUDE_DIRECTORIES "${PACKAGE_PREFIX_DIR}/include"
        INTERFACE_COMPILE_FEATURES "cxx_std_14"
        INTERFACE_LINK_LIBRARIES "xtl"
    )
endif()

set(xtensor_INCLUDE_DIRS "${PACKAGE_PREFIX_DIR}/include")
EOF

    cat > ${D}${libdir}/cmake/xtensor/xtensorConfigVersion.cmake <<'EOF'
set(PACKAGE_VERSION "0.26.0")

if(PACKAGE_VERSION VERSION_LESS PACKAGE_FIND_VERSION)
    set(PACKAGE_VERSION_COMPATIBLE FALSE)
else()
    set(PACKAGE_VERSION_COMPATIBLE TRUE)
    if(PACKAGE_FIND_VERSION STREQUAL PACKAGE_VERSION)
        set(PACKAGE_VERSION_EXACT TRUE)
    endif()
endif()
EOF

    ln -sf xtensorConfig.cmake ${D}${libdir}/cmake/xtensor/xtensor-config.cmake
}

ALLOW_EMPTY:${PN} = "1"

FILES:${PN}-dev += " \
    ${includedir} \
    ${libdir}/cmake/xtensor \
"
