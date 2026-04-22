SUMMARY = "Luxonis pybind11 fork (linux branch) for depthai-core"
HOMEPAGE = "https://github.com/luxonis/pybind11"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=774f65abd8a7fe3124be2cdf766cd06f"

SRC_URI = "gitsm://github.com/luxonis/pybind11.git;protocol=https;branch=luxonis_smart_holder"
SRCREV = "f760e2b984b66be2cfa202c93da9d341a557fb5d"

S = "${WORKDIR}/git"

inherit cmake python3native
DEPENDS += "python3 python3-native"

BBCLASSEXTEND = "native"
