FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:class-native = " file://0001-glib-gspawn-fall-back-when-close_range-is-blocked.patch"
