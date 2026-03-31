SUMMARY = "STEP-SPU package set migrated from rpi-img-gen layers"
DESCRIPTION = "Packagegroup collecting runtime dependencies for STEP-SPU systems"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
PR = "r0"

inherit packagegroup

RDEPENDS:${PN} = " \
    bash \
    ca-certificates \
    chrony \
    curl \
    ffmpeg \
    git \
    i2c-tools \
    iw \
    networkmanager \
    networkmanager-nmcli \
    openssh \
    openssh-sftp-server \
    packagegroup-core-boot \
    plymouth \
    python3 \
    python3-core \
    python3-cryptography \
    python3-numpy \
    python3-opencv \
    python3-pip \
    python3-psutil \
    python3-requests \
    python3-setuptools \
    sudo \
    wpa-supplicant \
    xinit \
    xserver-xorg \
"
