SUMMARY = "Header-only FP16 conversion library"
HOMEPAGE = "https://github.com/Maratyszcza/FP16"
LICENSE = "CLOSED"

# Upstream uses Git commits rather than an explicit "2021-02-21#2" Git tag.
# Keep the package version aligned with the requested release naming.
PV = "2021-02-21+2"

SRC_URI = "git://github.com/Maratyszcza/FP16.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit allarch

do_install() {
    install -d ${D}${includedir}/fp16
    install -m 0644 ${S}/include/fp16.h ${D}${includedir}/fp16.h
    install -m 0644 ${S}/include/fp16/bitcasts.h ${D}${includedir}/fp16/bitcasts.h
    install -m 0644 ${S}/include/fp16/fp16.h ${D}${includedir}/fp16/fp16.h
    install -m 0644 ${S}/include/fp16/macros.h ${D}${includedir}/fp16/macros.h
}

ALLOW_EMPTY:${PN} = "1"
FILES:${PN}-dev += " ${includedir}/fp16.h ${includedir}/fp16"
