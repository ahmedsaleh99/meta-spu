# Shared STEP-SPU Yocto build policy.
# Include this from local.conf to avoid hardcoding build policy in setup scripts.

DISTRO_FEATURES:append = " systemd"
VIRTUAL-RUNTIME_init_manager = "systemd"

INHERIT += "mender-full"
MENDER_ARTIFACT_NAME ?= "step-spu-release-1.0.0"
MENDER_STORAGE_DEVICE ?= "/dev/mmcblk0"
MENDER_BOOT_PART_SIZE_MB ?= "256"
MENDER_DATA_PART_SIZE_MB ?= "512"

SPU_USER ?= "spu-user"
SPU_INSTALL_DIR ?= "/home/${SPU_USER}/step-spu"
SPU_WIFI_SSID ?= "ChangeSSID"
SPU_WIFI_PSK ?= ""
SPU_HOSTNAME ?= "cvip-spu"
SPU_MENDER_SERVER_URL ?= "https://mender.example.com"

BB_NUMBER_THREADS ?= "4"
PARALLEL_MAKE ?= "-j4"
