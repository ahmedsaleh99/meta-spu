SUMMARY = "Cross-platform frameless window library for PySide6"
DESCRIPTION = "A cross-platform frameless window implementation for PySide6 on Linux, Windows, and macOS."
HOMEPAGE = "https://github.com/zhiyiYo/PyQt-Frameless-Window/tree/PySide6"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b5b6bed06dd8ed68f00c26d0b4cede89"

SRC_URI = "git://github.com/zhiyiYo/PyQt-Frameless-Window.git;protocol=https;branch=PySide6"
SRCREV = "${AUTOREV}"

PV = "0.8.1+git${SRCPV}"
S = "${WORKDIR}/git"

inherit python_setuptools_build_meta

RDEPENDS:${PN} += " \
    python3-core \
    python3-pyside6 \
"