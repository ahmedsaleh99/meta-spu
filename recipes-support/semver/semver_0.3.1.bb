SUMMARY = "Semantic Versioning for modern C++"
HOMEPAGE = "https://github.com/Neargye/semver"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=02fc04bd1a5ef0fd8e98ab66156d37bc"

SRC_URI = "git://github.com/Neargye/semver.git;branch=master;protocol=https"
SRCREV = "c333d59698765039d09e6b7bb41836886273cfaa"
PV = "0.3.1+git${SRCPV}"

S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${includedir}
    install -m 0644 ${S}/include/semver.hpp ${D}${includedir}/semver.hpp

    install -d ${D}${libdir}/cmake/semver
    cat > ${D}${libdir}/cmake/semver/semverConfig.cmake <<'EOF'
get_filename_component(_SEMVER_PREFIX "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)

if(NOT TARGET semver::semver)
    add_library(semver::semver INTERFACE IMPORTED)
    set_target_properties(semver::semver PROPERTIES
        INTERFACE_INCLUDE_DIRECTORIES "${_SEMVER_PREFIX}/include"
    )
endif()
EOF

    ln -sf semverConfig.cmake ${D}${libdir}/cmake/semver/semver-config.cmake
}

ALLOW_EMPTY:${PN} = "1"

FILES:${PN}-dev += " \
    ${includedir}/semver.hpp \
    ${libdir}/cmake/semver \
"
