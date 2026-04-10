# Work around binutils gold linker build failures on this toolchain.
LDGOLD:pn-binutils = ""

# Disable SPDX task generation; native sstate may not include SPDX artifacts.
INHERIT_DISTRO:remove = "create-spdx"
INHERIT:remove = "create-spdx"
