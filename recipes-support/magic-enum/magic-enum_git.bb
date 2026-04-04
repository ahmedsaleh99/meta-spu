SUMMARY = "Header-only magic_enum library with CMake package config"
HOMEPAGE = "https://github.com/Neargye/magic_enum"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/Neargye/magic_enum.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit allarch

do_install() {
    install -d ${D}${includedir}/magic_enum
    install -m 0644 ${S}/include/magic_enum/magic_enum.hpp ${D}${includedir}/magic_enum/magic_enum.hpp

    install -d ${D}${libdir}/cmake/magic_enum
    cat > ${D}${libdir}/cmake/magic_enum/magic_enumConfig.cmake << 'EOF'
# Minimal config file for config-mode package discovery.
if(NOT TARGET magic_enum::magic_enum)
    add_library(magic_enum::magic_enum INTERFACE IMPORTED)
    get_filename_component(_MAGIC_ENUM_PREFIX "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)
    set_target_properties(magic_enum::magic_enum PROPERTIES
        INTERFACE_INCLUDE_DIRECTORIES "${_MAGIC_ENUM_PREFIX}/include"
    )
endif()
EOF

    ln -sf magic_enumConfig.cmake ${D}${libdir}/cmake/magic_enum/magic_enum-config.cmake
}

ALLOW_EMPTY:${PN} = "1"
FILES:${PN}-dev += " ${includedir}/magic_enum ${libdir}/cmake/magic_enum"
