SUMMARY = "Fluent design widgets library for PySide6"
DESCRIPTION = "PySide6-Fluent-Widgets provides a Fluent Design widget set for PySide6 applications."
HOMEPAGE = "https://github.com/zhiyiYo/PyQt-Fluent-Widgets"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

inherit python_setuptools_build_meta

SRC_URI = "git://github.com/zhiyiYo/PyQt-Fluent-Widgets.git;protocol=https;branch=PySide6"
SRCREV = "${AUTOREV}"
PV = "1.8.1+git${SRCPV}"

S = "${WORKDIR}/git"

RDEPENDS:${PN} += " \
    python3-core \
    python3-pyside6 \
"
