SUMMARY = "Header-only C++ HTTP/HTTPS library"
HOMEPAGE = "https://github.com/yhirose/cpp-httplib"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/yhirose/cpp-httplib.git;protocol=https;branch=master"
SRCREV = "3af7f2c16147f3fbc6e4d717032daf505dc1652c"

S = "${WORKDIR}/git"

inherit allarch

do_install() {
    install -d ${D}${includedir}
    install -m 0644 ${S}/httplib.h ${D}${includedir}/httplib.h

    install -d ${D}${libdir}/cmake/httplib
    cat > ${D}${libdir}/cmake/httplib/httplibConfig.cmake << 'EOF'
if(NOT TARGET httplib::httplib)
    add_library(httplib::httplib INTERFACE IMPORTED)
    get_filename_component(_HTTPLIB_PREFIX "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)
    set_target_properties(httplib::httplib PROPERTIES
        INTERFACE_INCLUDE_DIRECTORIES "${_HTTPLIB_PREFIX}/include"
    )
endif()
EOF

    cat > ${D}${libdir}/cmake/httplib/httplibConfigVersion.cmake << 'EOF'
set(PACKAGE_VERSION "0.20.1")

if(PACKAGE_FIND_VERSION VERSION_GREATER PACKAGE_VERSION)
    set(PACKAGE_VERSION_COMPATIBLE FALSE)
else()
    set(PACKAGE_VERSION_COMPATIBLE TRUE)
    if(PACKAGE_FIND_VERSION STREQUAL PACKAGE_VERSION)
        set(PACKAGE_VERSION_EXACT TRUE)
    endif()
endif()
EOF
}

ALLOW_EMPTY:${PN} = "1"
FILES:${PN}-dev += " ${includedir}/httplib.h ${libdir}/cmake/httplib"
