FILESEXTRAPATHS:prepend := "${THISDIR}:"

SRC_URI:append:class-target = " \
    file://CURLConfig.cmake \
    file://CURLConfigVersion.cmake \
    file://CURLTargets.cmake \
"

do_install:append:class-target() {
    install -d ${D}${libdir}/cmake/CURL
    install -m 0644 ${WORKDIR}/CURLConfig.cmake ${D}${libdir}/cmake/CURL/CURLConfig.cmake
    install -m 0644 ${WORKDIR}/CURLConfigVersion.cmake ${D}${libdir}/cmake/CURL/CURLConfigVersion.cmake
    install -m 0644 ${WORKDIR}/CURLTargets.cmake ${D}${libdir}/cmake/CURL/CURLTargets.cmake

    ln -sf CURLConfig.cmake ${D}${libdir}/cmake/CURL/curl-config.cmake
}

FILES:${PN}-dev:append:class-target = " ${libdir}/cmake/CURL"
