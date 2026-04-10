SUMMARY = "Basic tools for mixed type expression template libraries"
HOMEPAGE = "https://github.com/luxonis/xtl"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c12cbcb0f50cce3b0c58db4e3db8c2da"

SRC_URI = "git://github.com/luxonis/xtl.git;protocol=https;branch=master"
SRCREV = "2da8e13ef3d7d9d6ccae3fd68f07892160e8b6c6"

PV = "0.7.4+git${SRCPV}"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${includedir}
    cp -r ${S}/include/* ${D}${includedir}/

    install -d ${D}${datadir}/cmake/xtl
    cat > ${D}${datadir}/cmake/xtl/xtlConfig.cmake <<'EOF'
get_filename_component(PACKAGE_PREFIX_DIR "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)

if(NOT TARGET xtl)
    add_library(xtl INTERFACE IMPORTED)
    set_target_properties(xtl PROPERTIES
        INTERFACE_INCLUDE_DIRECTORIES "${PACKAGE_PREFIX_DIR}/include"
        INTERFACE_SYSTEM_INCLUDE_DIRECTORIES "${PACKAGE_PREFIX_DIR}/include"
        INTERFACE_COMPILE_FEATURES "cxx_std_14"
    )
endif()

set(xtl_INCLUDE_DIRS "${PACKAGE_PREFIX_DIR}/include")
EOF

    cat > ${D}${datadir}/cmake/xtl/xtlConfigVersion.cmake <<'EOF'
set(PACKAGE_VERSION "0.7.4")

if(PACKAGE_VERSION VERSION_LESS PACKAGE_FIND_VERSION)
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

FILES:${PN}-dev += " \
    ${includedir} \
    ${datadir}/cmake/xtl \
"
