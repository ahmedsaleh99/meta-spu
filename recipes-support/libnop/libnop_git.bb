SUMMARY = "Header-only libnop library with CMake package config"
HOMEPAGE = "https://github.com/luxonis/libnop"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/luxonis/libnop.git;protocol=https;nobranch=1"
SRCREV = "ab842f51dc2eb13916dc98417c2186b78320ed10"

S = "${WORKDIR}/git"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}/nop
    cp -R ${S}/include/nop/* ${D}${includedir}/nop/

    install -d ${D}${libdir}/cmake/libnop
    cat > ${D}${libdir}/cmake/libnop/libnopConfig.cmake << 'EOF'
if(NOT TARGET libnop)
    add_library(libnop INTERFACE IMPORTED)
    get_filename_component(_LIBNOP_PREFIX "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)
    set_target_properties(libnop PROPERTIES
        INTERFACE_INCLUDE_DIRECTORIES "${_LIBNOP_PREFIX}/include"
    )
endif()
EOF

    ln -sf libnopConfig.cmake ${D}${libdir}/cmake/libnop/libnop-config.cmake
}

ALLOW_EMPTY:${PN} = "1"
FILES:${PN}-dev += " ${includedir}/nop ${libdir}/cmake/libnop"
