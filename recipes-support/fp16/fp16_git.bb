SUMMARY = "Header-only FP16 conversion library"
HOMEPAGE = "https://github.com/maratyszcza/fp16"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/maratyszcza/fp16.git;protocol=https;branch=master"
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