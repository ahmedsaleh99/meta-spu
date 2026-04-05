# In kas-container/Docker, glib-compile-resources can fail with:
# "Failed to close file descriptor for child process (Operation not permitted)".
# The stock recipe already disables tests, but gtk+3 still builds demo/example
# resource bundles that trigger the same subprocess path. Disable those extra
# build products so the recipe remains container-friendly.
EXTRA_OEMESON:append = " -Ddemos=false -Dexamples=false -Dinstalled_tests=false"

# GCC 13 on this Yocto/toolchain combination can trip over a maybe-uninitialized
# warning in gdk/x11/gdkglcontext-x11.c and stop the build. Keep the recipe
# building by downgrading just this warning class.
CFLAGS:append = " -Wno-error=maybe-uninitialized"
