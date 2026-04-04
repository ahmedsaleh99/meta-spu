do_install:append() {
    install -d ${D}${libdir}/cmake/liblzma

    cat > ${D}${libdir}/cmake/liblzma/liblzmaConfig.cmake << 'EOF'
# Minimal config file for consumers that require config-mode package discovery.
# Provides common imported target names for liblzma.
if(NOT TARGET liblzma::liblzma)
    add_library(liblzma::liblzma SHARED IMPORTED)
    get_filename_component(_LIBLZMA_PREFIX "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)
    set_target_properties(liblzma::liblzma PROPERTIES
        IMPORTED_LOCATION "${_LIBLZMA_PREFIX}/lib/liblzma.so"
        INTERFACE_INCLUDE_DIRECTORIES "${_LIBLZMA_PREFIX}/include"
    )
endif()

if(NOT TARGET lzma::lzma)
    add_library(lzma::lzma INTERFACE IMPORTED)
    target_link_libraries(lzma::lzma INTERFACE liblzma::liblzma)
endif()
EOF

    ln -sf liblzmaConfig.cmake ${D}${libdir}/cmake/liblzma/liblzma-config.cmake
}

FILES:${PN}-dev:append = " ${libdir}/cmake/liblzma"
