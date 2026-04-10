SUMMARY = "Font Awesome and other iconic fonts in PyQt and PySide applications"
HOMEPAGE = "https://github.com/spyder-ide/qtawesome"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2606b1a6778457da767700bc97388d63"

PYPI_PACKAGE = "qtawesome"

inherit pypi setuptools3

SRC_URI[sha256sum] = "d6c5aa545b614fc562769e9eeadf18530e08b8eebe28af4ab5d38131194c9c7b"

RDEPENDS:${PN} += "python3-qtpy"
