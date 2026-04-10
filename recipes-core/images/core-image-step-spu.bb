SUMMARY = "A console-only image that fully supports the target device hardware."
IMAGE_BASENAME = "step"

inherit core-image

IMAGE_INSTALL:append = " \
    ca-certificates \
    chrony \
    fake-hwclock \
    fontconfig \
    i2c-tools \
    python3 \
    python3-modules \
    python3-packaging \
    networkmanager-nmcli \
    networkmanager-wifi \
    rpi-eeprom \
    rpi-eeprom-defaults \
    ttf-dejavu-sans \
    ttf-dejavu-sans-mono \
    ttf-dejavu-serif \
    user-credentials \
    wificonfig \
    systemd \
    plymouth \
    xorg-session \
    step-spu \
"

EXTRA_IMAGE_FEATURES:append = " splash"
