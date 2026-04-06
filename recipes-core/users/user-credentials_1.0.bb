SUMMARY = "Create an image user with Raspberry Pi style group membership"
DESCRIPTION = "Adds a configurable local user, assigns the standard Raspberry Pi supplementary groups, optionally configures sudo access, and locks the root password."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit allarch useradd

S = "${WORKDIR}"

SPU_USER ?= "spu-user"
SPU_IMAGE_USER ?= "${SPU_USER}"
SPU_IMAGE_USER_HOME ?= "/home/${SPU_IMAGE_USER}"

USERADDEXTENSION = "useradd-staticids"

RDEPENDS:${PN} += "sudo"

USERADD_PACKAGES = "${PN}"

GROUPADD_PARAM:${PN} = " \
    ${SPU_IMAGE_USER}; \
    -f -r input; \
    -f -r spi; \
    -f -r i2c; \
    -f -r gpio; \
    -f adm; \
    -f dialout; \
    -f cdrom; \
    -f audio; \
    -f users; \
    -f sudo; \
    -f video; \
    -f games; \
    -f plugdev; \
    -f render; \
"

USERADD_PARAM:${PN} = " \
    -m -d ${SPU_IMAGE_USER_HOME} -s /usr/sbin/nologin \
    -g ${SPU_IMAGE_USER} \
    -p '*' ${SPU_IMAGE_USER} \
"

GROUPMEMS_PARAM:${PN} = " \
    -g adm -a ${SPU_IMAGE_USER}; \
    -g dialout -a ${SPU_IMAGE_USER}; \
    -g cdrom -a ${SPU_IMAGE_USER}; \
    -g audio -a ${SPU_IMAGE_USER}; \
    -g users -a ${SPU_IMAGE_USER}; \
    -g sudo -a ${SPU_IMAGE_USER}; \
    -g video -a ${SPU_IMAGE_USER}; \
    -g games -a ${SPU_IMAGE_USER}; \
    -g plugdev -a ${SPU_IMAGE_USER}; \
    -g input -a ${SPU_IMAGE_USER}; \
    -g spi -a ${SPU_IMAGE_USER}; \
    -g i2c -a ${SPU_IMAGE_USER}; \
    -g gpio -a ${SPU_IMAGE_USER}; \
    -g render -a ${SPU_IMAGE_USER}; \
"

do_install() {
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/profile.d
    install -d ${D}${sysconfdir}/udev/rules.d
    install -d ${D}${sysconfdir}/sudoers.d

    cat > ${D}${sysconfdir}/profile.d/01local.sh <<'EOF'
#!/bin/sh
if [ "$(id -u)" -ne 0 ]; then
    PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/local/games:/usr/games
fi
EOF

    cat > ${D}${sysconfdir}/udev/rules.d/80-movidius.rules <<'EOF'
SUBSYSTEM=="usb", ATTRS{idVendor}=="03e7", GROUP="plugdev", MODE="0660"
EOF

    cat > ${D}${sysconfdir}/modules <<'EOF'
i2c-dev
i2c-bcm2835
EOF

    cat > ${D}${sysconfdir}/sudoers.d/${SPU_IMAGE_USER} <<EOF
# Allow user to run specific commands with sudo without password
${SPU_IMAGE_USER} ALL=(root) NOPASSWD: /sbin/reboot
${SPU_IMAGE_USER} ALL=(root) NOPASSWD: /sbin/shutdown
EOF
    chmod 0755 ${D}${sysconfdir}/profile.d/01local.sh
    chmod 0440 ${D}${sysconfdir}/sudoers.d/${SPU_IMAGE_USER}
}


FILES:${PN} += " \
    ${sysconfdir}/modules \
    ${sysconfdir}/profile.d/01local.sh \
    ${sysconfdir}/sudoers.d/${SPU_IMAGE_USER} \
    ${sysconfdir}/udev/rules.d/80-movidius.rules \
"
