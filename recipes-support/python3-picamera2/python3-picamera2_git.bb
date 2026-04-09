SUMMARY = "Python interface to the Raspberry Pi camera module via libcamera"
DESCRIPTION = "This package provides a pure Python interface to the Raspberry Pi camera module via libcamera for Python 3"
HOMEPAGE = "https://github.com/raspberrypi/picamera2" 

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6541a38108b5accb25bd55a14e76086d"

RDEPENDS:${PN} = " \
    python3-av \
    python3-numbers \
    python3-ctypes \
    python3-colorzero \
    python3-jsonschema \
    python3-libarchive-c \
    libcamera \
    libcamera-pycamera \
    kmsxx-python \
    python3-numpy \
    python3-openexr \
    python3-pidng \
    python3-pillow \
    python3-prctl \
    python3-tqdm \
    python3-videodev2 \
    python3-piexif \
    python3-simplejpeg \
"

SRC_URI = "git://github.com/raspberrypi/picamera2.git;protocol=https;branch=main"
SRCREV = "767ad8c2ba6bb9e80ab30009fcbfed521ffac662"

S = "${WORKDIR}/git"

inherit setuptools3

COMPATIBLE_HOST = "null"
COMPATIBLE_HOST:rpi:libc-glibc = "(arm|aarch64).*-linux"
