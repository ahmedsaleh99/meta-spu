do_install:append() {
    install -d ${D}${libdir}/cmake/lz4

    cat > ${D}${libdir}/cmake/lz4/lz4Config.cmake << 'EOF'
# Minimal config file for consumers that require config-mode package discovery.
# Mirrors common target name expected by projects: lz4::lz4
if(NOT TARGET lz4::lz4)
    add_library(lz4::lz4 SHARED IMPORTED)
    get_filename_component(_LZ4_PREFIX "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)
    set_target_properties(lz4::lz4 PROPERTIES
        IMPORTED_LOCATION "${_LZ4_PREFIX}/lib/liblz4.so"
        INTERFACE_INCLUDE_DIRECTORIES "${_LZ4_PREFIX}/include"
    )
endif()
EOF

    ln -sf lz4Config.cmake ${D}${libdir}/cmake/lz4/lz4-config.cmake
}

FILES:${PN}-dev:append = " ${libdir}/cmake/lz4"