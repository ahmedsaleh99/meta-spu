SUMMARY = "Header-only xsimd library with CMake package config"
HOMEPAGE = "https://github.com/xtensor-stack/xsimd"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/xtensor-stack/xsimd.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit allarch

do_install() {
    install -d ${D}${includedir}/xsimd
    cp -R ${S}/include/xsimd/* ${D}${includedir}/xsimd/

    install -d ${D}${libdir}/cmake/xsimd
    cat > ${D}${libdir}/cmake/xsimd/xsimdConfig.cmake << 'EOF'
if(NOT TARGET xsimd)
    add_library(xsimd INTERFACE IMPORTED)
    get_filename_component(_XSIMD_PREFIX "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)
    set_target_properties(xsimd PROPERTIES
        INTERFACE_INCLUDE_DIRECTORIES "${_XSIMD_PREFIX}/include"
    )
endif()
EOF

    ln -sf xsimdConfig.cmake ${D}${libdir}/cmake/xsimd/xsimd-config.cmake
}

ALLOW_EMPTY:${PN} = "1"
FILES:${PN}-dev += " ${includedir}/xsimd ${libdir}/cmake/xsimd"
