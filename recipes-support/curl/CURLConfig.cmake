# Adapted from the CMake package files in /mnt/docker-data/exper/yocto_build_depthai/curl
# for Yocto's curl package layout.

get_filename_component(PACKAGE_PREFIX_DIR "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)

macro(set_and_check _var _file)
  set(${_var} "${_file}")
  if(NOT EXISTS "${_file}")
    message(FATAL_ERROR "File or directory ${_file} referenced by variable ${_var} does not exist")
  endif()
endmacro()

macro(check_required_components _NAME)
  foreach(comp ${${_NAME}_FIND_COMPONENTS})
    if(NOT ${_NAME}_${comp}_FOUND)
      if(${_NAME}_FIND_REQUIRED_${comp})
        set(${_NAME}_FOUND FALSE)
      endif()
    endif()
  endforeach()
endmacro()

include(CMakeFindDependencyMacro)
find_dependency(OpenSSL)
find_dependency(ZLIB)

include("${CMAKE_CURRENT_LIST_DIR}/CURLTargets.cmake")

if(NOT TARGET CURL::libcurl)
  add_library(CURL::libcurl ALIAS CURL::libcurl_shared)
endif()

set(CURL_VERSION_STRING "8")
set(CURL_LIBRARIES CURL::libcurl)
set_and_check(CURL_INCLUDE_DIRS "${PACKAGE_PREFIX_DIR}/include")

check_required_components("CURL")
