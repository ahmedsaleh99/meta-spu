SUMMARY = "Abstraction layer for PyQt5/PySide2/PyQt6/PySide6"
HOMEPAGE = "https://github.com/spyder-ide/qtpy"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b2830f54500be1314b9ec6096989f983"

PYPI_PACKAGE = "qtpy"

inherit pypi setuptools3

SRC_URI[sha256sum] = "db744f7832e6d3da90568ba6ccbca3ee2b3b4a890c3d6fbbc63142f6e4cdf5bb"

RDEPENDS:${PN} += "python3-pyside6"
