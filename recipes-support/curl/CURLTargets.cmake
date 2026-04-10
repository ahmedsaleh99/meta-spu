# Adapted from the CMake package files in /mnt/docker-data/exper/yocto_build_depthai/curl
# for Yocto's curl package layout.

get_filename_component(_IMPORT_PREFIX "${CMAKE_CURRENT_LIST_FILE}" PATH)
get_filename_component(_IMPORT_PREFIX "${_IMPORT_PREFIX}" PATH)
get_filename_component(_IMPORT_PREFIX "${_IMPORT_PREFIX}" PATH)
get_filename_component(_IMPORT_PREFIX "${_IMPORT_PREFIX}" PATH)
if(_IMPORT_PREFIX STREQUAL "/")
  set(_IMPORT_PREFIX "")
endif()

if(NOT TARGET CURL::libcurl_shared)
  add_library(CURL::libcurl_shared SHARED IMPORTED)
  set_target_properties(CURL::libcurl_shared PROPERTIES
    IMPORTED_LOCATION "${_IMPORT_PREFIX}/lib/libcurl.so"
    INTERFACE_INCLUDE_DIRECTORIES "${_IMPORT_PREFIX}/include"
    INTERFACE_LINK_LIBRARIES "OpenSSL::SSL;OpenSSL::Crypto;ZLIB::ZLIB"
  )
endif()
