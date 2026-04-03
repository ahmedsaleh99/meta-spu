SUMMARY = "A console-only image that fully supports the target device hardware."

inherit core-image

IMAGE_INSTALL:append = " \
    wificonfig \
    xserver-xorg \
    xserver-xorg-extension-glx \
    xf86-video-modesetting \
    xf86-video-fbdev \
    xinit \
    xauth \
    xinput \
    xset \
    xrandr \
    xf86-input-libinput \
    libinput \
    libevdev \
    mesa \
    xorg-session \
    xcb-util-cursor \
"