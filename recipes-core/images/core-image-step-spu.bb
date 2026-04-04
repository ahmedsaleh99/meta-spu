SUMMARY = "A console-only image that fully supports the target device hardware."

inherit core-image

IMAGE_INSTALL:append = " \
    depthai-core \
    depthai-core-python \
    wificonfig \
    xorg-session \
"