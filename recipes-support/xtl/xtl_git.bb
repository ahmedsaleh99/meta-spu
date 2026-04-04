SUMMARY = "Header-only xtl library with CMake package config"
HOMEPAGE = "https://github.com/luxonis/xtl"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/luxonis/xtl.git;protocol=https;branch=master"
SRCREV = "2da8e13ef3d7d9d6ccae3fd68f07892160e8b6c6"

S = "${WORKDIR}/git"

inherit allarch

do_install() {
    install -d ${D}${includedir}/xtl
    cp -R ${S}/include/xtl/* ${D}${includedir}/xtl/

    install -d ${D}${libdir}/cmake/xtl
    cat > ${D}${libdir}/cmake/xtl/xtlConfig.cmake << 'EOF'
if(NOT TARGET xtl::xtl)
    add_library(xtl::xtl INTERFACE IMPORTED)
    get_filename_component(_XTL_PREFIX "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)
    set_target_properties(xtl::xtl PROPERTIES
        INTERFACE_INCLUDE_DIRECTORIES "${_XTL_PREFIX}/include"
    )
endif()
EOF

    ln -sf xtlConfig.cmake ${D}${libdir}/cmake/xtl/xtl-config.cmake
}

ALLOW_EMPTY:${PN} = "1"
FILES:${PN}-dev += " ${includedir}/xtl ${libdir}/cmake/xtl"
