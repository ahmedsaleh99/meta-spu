SUMMARY = "Header-only xtensor library with CMake package config"
HOMEPAGE = "https://github.com/luxonis/xtensor"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/luxonis/xtensor.git;protocol=https;branch=master"
SRCREV = "d070cfcba9418ec45e5399708100eee1181a9573"

S = "${WORKDIR}/git"

inherit allarch

DEPENDS = "xtl xsimd"

do_install() {
    install -d ${D}${includedir}/xtensor
    cp -R ${S}/include/xtensor/* ${D}${includedir}/xtensor/

    install -d ${D}${libdir}/cmake/xtensor
    cat > ${D}${libdir}/cmake/xtensor/xtensorConfig.cmake << 'EOF'
if(NOT TARGET xtensor)
    add_library(xtensor INTERFACE IMPORTED)
    get_filename_component(_XTENSOR_PREFIX "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)
    set_target_properties(xtensor PROPERTIES
        INTERFACE_INCLUDE_DIRECTORIES "${_XTENSOR_PREFIX}/include"
    )
endif()
if(NOT TARGET xtensor::xtensor)
    add_library(xtensor::xtensor INTERFACE IMPORTED)
    set_target_properties(xtensor::xtensor PROPERTIES
        INTERFACE_LINK_LIBRARIES "xtensor"
    )
endif()
EOF

    cat > ${D}${libdir}/cmake/xtensor/xtensorConfigVersion.cmake << 'EOF'
set(PACKAGE_VERSION "0.26.0")

if(PACKAGE_FIND_VERSION VERSION_GREATER PACKAGE_VERSION)
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
FILES:${PN}-dev += " ${includedir}/xtensor ${libdir}/cmake/xtensor"
