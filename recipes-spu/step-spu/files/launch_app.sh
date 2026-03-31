#!/usr/bin/env bash
set -eu

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
FIRST_RUN_SCRIPT="$SCRIPT_DIR/first_run.sh"

if [ -x "$FIRST_RUN_SCRIPT" ]; then
    echo "Running one-time first-run setup from launch_app.sh"
    if ! sudo -n "$FIRST_RUN_SCRIPT"; then
        echo "[WARN] First-run setup failed; continuing app launch"
    fi
fi

xset s off

# DPMS: standby=20s, suspend=25s, off=30s
xset dpms 20 25 30

DSI_DISPLAY=""

# Check DSI-1
if xrandr --query | grep -q "^DSI-1 connected"; then
    DSI_DISPLAY="DSI-1"
fi

# Check DSI-2
if xrandr --query | grep -q "^DSI-2 connected"; then
    DSI_DISPLAY="DSI-2"
fi

MODEL=$(tr -d '\0' </proc/device-tree/model 2>/dev/null)


if [ -n "$DSI_DISPLAY" ]; then
    echo "Raspberry Pi Touch Display detected on $DSI_DISPLAY"

    # ----- Auto-detect Goodix touchscreen pointer device -----
    TOUCH_ID=$(xinput list | \
        grep "Goodix Capacitive TouchScreen" | \
        grep pointer | \
        sed -E 's/.*id=([0-9]+).*/\1/')

    if [ -n "$TOUCH_ID" ]; then
        echo "Goodix touchscreen ID=$TOUCH_ID. Applying touch rotation."
        # ----- Rotate the DSI display -----
        if echo "$MODEL" | grep -q "Raspberry Pi 5"; then
            echo "Raspberry Pi 5 detected  rotating display left."
            xrandr --output "$DSI_DISPLAY" --rotate left
            sleep 0.3
            # 90 LEFT matrix (correct for xrandr rotate left)
            xinput set-prop "$TOUCH_ID" \
                "Coordinate Transformation Matrix" 0 -1 1 1 0 0 0 0 1
        else
            xrandr --output "$DSI_DISPLAY" --rotate right
            sleep 0.3
            # 90 RIGHT matrix (correct for xrandr rotate right)
            xinput set-prop "$TOUCH_ID" \
                "Coordinate Transformation Matrix" 0 1 0 -1 0 1 0 0 1
        fi
    else
        echo "Goodix touchscreen not found!"
    fi

else
    echo "No Raspberry Pi DSI Touch Display (DSI-1/DSI-2) found  skipping rotation"
fi

if [ -d "$SCRIPT_DIR/src" ]; then
    APP_ROOT="$SCRIPT_DIR"
elif [ -d "$SCRIPT_DIR/../src" ]; then
    APP_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
else
    APP_ROOT="$SCRIPT_DIR"
fi

echo "Launching app from $APP_ROOT"
cd "$APP_ROOT"
exec python3 -m src.spu_app

