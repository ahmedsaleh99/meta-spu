SUMMARY = "Header-only semver library with CMake package config"
HOMEPAGE = "https://github.com/Neargye/semver"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/Neargye/semver.git;protocol=https;branch=master"
SRCREV = "c333d59698765039d09e6b7bb41836886273cfaa"

S = "${WORKDIR}/git"

inherit allarch

do_install() {
    install -d ${D}${includedir}
    install -m 0644 ${S}/include/semver.hpp ${D}${includedir}/semver.hpp

    install -d ${D}${libdir}/cmake/semver
    cat > ${D}${libdir}/cmake/semver/semverConfig.cmake << 'EOF'
if(NOT TARGET semver::semver)
    add_library(semver::semver INTERFACE IMPORTED)
    get_filename_component(_SEMVER_PREFIX "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)
    set_target_properties(semver::semver PROPERTIES
        INTERFACE_INCLUDE_DIRECTORIES "${_SEMVER_PREFIX}/include"
    )
endif()
EOF

    cat > ${D}${libdir}/cmake/semver/semverConfigVersion.cmake << 'EOF'
set(PACKAGE_VERSION "1.0.0")

if(PACKAGE_FIND_VERSION VERSION_GREATER PACKAGE_VERSION)
    set(PACKAGE_VERSION_COMPATIBLE FALSE)
else()
    set(PACKAGE_VERSION_COMPATIBLE TRUE)
    if(PACKAGE_FIND_VERSION STREQUAL PACKAGE_VERSION)
        set(PACKAGE_VERSION_EXACT TRUE)
    endif()
endif()
EOF

    ln -sf semverConfig.cmake ${D}${libdir}/cmake/semver/semver-config.cmake
}

ALLOW_EMPTY:${PN} = "1"
FILES:${PN}-dev += " ${includedir}/semver.hpp ${libdir}/cmake/semver"
