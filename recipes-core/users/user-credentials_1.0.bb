SUMMARY = "Create an image user with Raspberry Pi style group membership"
DESCRIPTION = "Adds a configurable local user, assigns the standard Raspberry Pi supplementary groups, optionally configures sudo access, and locks the root password."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit allarch useradd

S = "${WORKDIR}"

SPU_USER_HOME ?= "/home/${SPU_USER}"


USERADDEXTENSION = "useradd-staticids"

RDEPENDS:${PN} += "sudo"

USERADD_PACKAGES = "${PN}"

GROUPADD_PARAM:${PN} = " \
    ${SPU_USER}; \
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
    -f tty; \
    -f video; \
    -f games; \
    -f plugdev; \
    -f render; \
"

USERADD_PARAM:${PN} = " \
    -m -d ${SPU_USER_HOME} -s /usr/sbin/nologin \
    -g ${SPU_USER} \
    -p '*' ${SPU_USER} \
"

GROUPMEMS_PARAM:${PN} = " \
    -g adm -a ${SPU_USER}; \
    -g dialout -a ${SPU_USER}; \
    -g cdrom -a ${SPU_USER}; \
    -g audio -a ${SPU_USER}; \
    -g users -a ${SPU_USER}; \
    -g sudo -a ${SPU_USER}; \
    -g tty -a ${SPU_USER}; \
    -g video -a ${SPU_USER}; \
    -g games -a ${SPU_USER}; \
    -g plugdev -a ${SPU_USER}; \
    -g input -a ${SPU_USER}; \
    -g spi -a ${SPU_USER}; \
    -g i2c -a ${SPU_USER}; \
    -g gpio -a ${SPU_USER}; \
    -g render -a ${SPU_USER}; \
"
do_check_spu_user() {
    if [ -z "${SPU_USER}" ]; then
        bbfatal "SPU_USER must be set in the distro config"
    fi
}
addtask check_spu_user after do_prepare_recipe_sysroot before do_install

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

    cat > ${D}${sysconfdir}/sudoers.d/${SPU_USER} <<EOF
# Allow user to run specific commands with sudo without password
${SPU_USER} ALL=(root) NOPASSWD: /sbin/reboot
${SPU_USER} ALL=(root) NOPASSWD: /sbin/shutdown
EOF
    chmod 0755 ${D}${sysconfdir}/profile.d/01local.sh
    chmod 0440 ${D}${sysconfdir}/sudoers.d/${SPU_USER}
}


FILES:${PN} += " \
    ${sysconfdir}/modules \
    ${sysconfdir}/profile.d/01local.sh \
    ${sysconfdir}/sudoers.d/${SPU_USER} \
    ${sysconfdir}/udev/rules.d/80-movidius.rules \
"
