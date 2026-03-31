SPU_HOSTNAME ?= "cvip-spu"

do_install:append() {
    echo "${SPU_HOSTNAME}" > ${D}${sysconfdir}/hostname
}
