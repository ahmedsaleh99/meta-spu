SUMMARY = "Luxonis Dynamic Calibration runtime"
HOMEPAGE = "https://github.com/luxonis/depthai-core"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

DEPTHAI_DYNAMIC_CALIBRATION_VERSION = "003c5040b732498ae4547f0c5464fe431170bef1"

DEPTHAI_HOST_PLATFORM_ARCH = "linux-x86_64"
DEPTHAI_HOST_PLATFORM_ARCH:aarch64 = "linux-arm64"
DEPTHAI_HOST_PLATFORM_ARCH:x86-64 = "linux-x86_64"

DEPTHAI_DYNAMIC_CALIBRATION_ZIP = "dynamic_calibration_${DEPTHAI_DYNAMIC_CALIBRATION_VERSION}_${DEPTHAI_HOST_PLATFORM_ARCH}.zip"

SRC_URI = "https://artifacts.luxonis.com/artifactory/luxonis-depthai-helper-binaries/dynamic_calibration/${DEPTHAI_DYNAMIC_CALIBRATION_VERSION}/${DEPTHAI_DYNAMIC_CALIBRATION_ZIP};downloadfilename=${DEPTHAI_DYNAMIC_CALIBRATION_ZIP};name=dyncalib"

SRC_URI[dyncalib.sha256sum] = "4b3be34562b29587e394c919f862579a94e318f9e349db427f4dc2a5e08762c1"

S = "${WORKDIR}"
PACKAGE_ARCH = "${TUNE_PKGARCH}"

# The archive ships a real unversioned shared object, not a symlinked
# development linker stub, so keep it in the runtime package.
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

do_install() {
    install -d ${D}${includedir} ${D}${libdir} ${D}${datadir}/dynamic-calibration
    cp -R --no-dereference ${S}/include/. ${D}${includedir}/
    cp -R --no-dereference ${S}/lib/. ${D}${libdir}/

    if [ -f "${WORKDIR}/${DEPTHAI_DYNAMIC_CALIBRATION_ZIP}" ]; then
        install -m 0644 "${WORKDIR}/${DEPTHAI_DYNAMIC_CALIBRATION_ZIP}" ${D}${datadir}/dynamic-calibration/${DEPTHAI_DYNAMIC_CALIBRATION_ZIP}
    elif [ -f "${DL_DIR}/${DEPTHAI_DYNAMIC_CALIBRATION_ZIP}" ]; then
        install -m 0644 "${DL_DIR}/${DEPTHAI_DYNAMIC_CALIBRATION_ZIP}" ${D}${datadir}/dynamic-calibration/${DEPTHAI_DYNAMIC_CALIBRATION_ZIP}
    fi
}

FILES:${PN} += "${libdir}/*.so*"
FILES:${PN} += "${datadir}/dynamic-calibration/${DEPTHAI_DYNAMIC_CALIBRATION_ZIP}"
FILES:${PN}-dev += "${includedir}"

INSANE_SKIP:${PN} += "already-stripped"
