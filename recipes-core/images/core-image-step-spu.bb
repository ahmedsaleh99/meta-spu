SUMMARY = "A console-only image that fully supports the target device hardware."

inherit core-image

IMAGE_INSTALL:append = " \
    ca-certificates \
    fontconfig \
    python3 \
    python3-modules \
    python3-packaging \
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

EXTRA_IMAGE_FEATURES:append = " ssh-server-openssh splash debug-tweaks"
