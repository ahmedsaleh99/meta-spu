PACKAGECONFIG:remove:class-native = "tests"

# In kas-container/Docker, glib-compile-resources can fail during the native
# gdk-pixbuf test resource generation path with:
# "Failed to close file descriptor for child process (Operation not permitted)".
# Force native builds to skip the test suite entirely so Meson never enters
# tests/ and avoids the subprocess path that trips over container restrictions.
EXTRA_OEMESON:append:class-native = " -Dtests=false -Dinstalled_tests=false"
