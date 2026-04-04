do_install:append() {
    install -d ${D}${libdir}/cmake/usb-1.0

    cat > ${D}${libdir}/cmake/usb-1.0/usb-1.0Config.cmake << 'EOF'
# Minimal config file for config-mode package discovery.
if(NOT TARGET usb-1.0)
    add_library(usb-1.0 SHARED IMPORTED)
    get_filename_component(_LIBUSB_PREFIX "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)
    set_target_properties(usb-1.0 PROPERTIES
        IMPORTED_LOCATION "${_LIBUSB_PREFIX}/lib/libusb-1.0.so"
        INTERFACE_INCLUDE_DIRECTORIES "${_LIBUSB_PREFIX}/include/libusb-1.0"
    )
endif()
EOF

    ln -sf usb-1.0Config.cmake ${D}${libdir}/cmake/usb-1.0/usb-1.0-config.cmake
}

FILES:${PN}-dev:append = " ${libdir}/cmake/usb-1.0"
