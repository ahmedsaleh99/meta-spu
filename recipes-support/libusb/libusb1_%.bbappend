do_install:append() {
    install -d ${D}${libdir}/cmake/usb-1.0

    cat > ${D}${libdir}/cmake/usb-1.0/usb-1.0Config.cmake <<'EOF'
get_filename_component(_IMPORT_PREFIX "${CMAKE_CURRENT_LIST_FILE}" PATH)
get_filename_component(_IMPORT_PREFIX "${_IMPORT_PREFIX}" PATH)
get_filename_component(_IMPORT_PREFIX "${_IMPORT_PREFIX}" PATH)
get_filename_component(_IMPORT_PREFIX "${_IMPORT_PREFIX}" PATH)
if(_IMPORT_PREFIX STREQUAL "/")
  set(_IMPORT_PREFIX "")
endif()

if(NOT TARGET usb-1.0)
  find_library(_USB_1_0_LIBRARY
    NAMES usb-1.0 libusb-1.0.so.0 libusb-1.0.so
    PATHS "${_IMPORT_PREFIX}/lib" "${_IMPORT_PREFIX}/../lib"
    NO_DEFAULT_PATH
  )
  if(NOT _USB_1_0_LIBRARY)
    message(FATAL_ERROR "usb-1.0 library not found under ${_IMPORT_PREFIX}/lib or ${_IMPORT_PREFIX}/../lib")
  endif()

  add_library(usb-1.0 SHARED IMPORTED)
  set_target_properties(usb-1.0 PROPERTIES
    IMPORTED_LOCATION "${_USB_1_0_LIBRARY}"
    INTERFACE_INCLUDE_DIRECTORIES "${_IMPORT_PREFIX}/include"
  )
  unset(_USB_1_0_LIBRARY)
endif()
EOF
}

FILES:${PN}-dev += " ${libdir}/cmake/usb-1.0"
