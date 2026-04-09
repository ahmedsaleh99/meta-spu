# meta-spu

Custom Yocto layer for STEP-SPU.

This layer now carries:
- image content in recipes like [core-image-step-spu.bb](/mnt/docker-data/yocto_build/meta-spu/recipes-core/images/core-image-step-spu.bb)
- Raspberry Pi machine policy in [step-spu-rpi5.conf](/mnt/docker-data/yocto_build/meta-spu/conf/machine/step-spu-rpi5.conf)
- shared Raspberry Pi settings in [spu-raspberrypi.inc](/mnt/docker-data/yocto_build/meta-spu/conf/machine/include/spu-raspberrypi.inc)
- distro-level build workarounds in [step-spu.conf](/mnt/docker-data/yocto_build/meta-spu/conf/distro/step-spu.conf) and [spu-workarounds.inc](/mnt/docker-data/yocto_build/meta-spu/conf/distro/include/spu-workarounds.inc)
- local support recipes for the STEP-SPU stack, including Picamera2-related Python packages

## Recommended Build Settings

To use the layer-managed machine and distro configuration, build with:

```conf
MACHINE = "step-spu-rpi5"
DISTRO = "step-spu"
```

If you are using `kas`, put those in `local_conf_header` in your `kas` file.

## Raspberry Pi Policy

The Raspberry Pi include currently enables:
- U-Boot on Raspberry Pi machines
- I2C support and `i2c-dev` autoload
- extra firmware config for display autodetect
- USB current enable
- the `vc4-kms-dsi-ili9881-7inch` DSI overlay with `rotation=270`

The image recipe also installs `i2c-tools`.

## Python/Camera Notes

Picamera2 in this layer depends on several local recipes such as:
- `python3-pidng`
- `python3-piexif`
- `python3-simplejpeg`
- `python3-openexr`

For DRM preview support, this layer also adds an alias so the existing `kmsxx-python`
package can satisfy names like `python3-pykms`.

## Build Example

```bash
export KAS_WORK_DIR=/mnt/docker-data/yocto_build
kas-container --ssh-dir $HOME/.ssh build ./scripts/kas/step-spu.yaml
```

When using the example above, make sure your `kas` file sets:

```yaml
local_conf_header:
  machine: |
    MACHINE = "step-spu-rpi5"
  distro: |
    DISTRO = "step-spu"
```

## Layer Compatibility

- Yocto series: `scarthgap`
- Layer name: `meta-spu`
