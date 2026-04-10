SUMMARY = "AprilTag fiducial marker detection library"
HOMEPAGE = "https://github.com/AprilRobotics/apriltag"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=a1f3be4f2bc2416cc9773098becba4fb"

SRC_URI = "git://github.com/AprilRobotics/apriltag.git;protocol=https;branch=master"
SRCREV = "fb2a4096ec5f84b9ec18e501dfa129ffeaaf774e"

PV = "3.4.3+git${SRCPV}"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

DEPENDS = "zlib"

EXTRA_OECMAKE += " \
    -DBUILD_SHARED_LIBS=ON \
"

# Install fixes (optional but safe)
FILES:${PN}-dev += " \
    ${libdir}/cmake \
    ${libdir}/pkgconfig \
"
