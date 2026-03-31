# meta-spu

## Overview

`meta-spu` is a custom [Yocto Project](https://www.yoctoproject.org/) layer designed to build a Linux image for the **STEP-SPU** board. It provides board-specific configurations, recipes, and customizations needed to produce a tailored embedded Linux image for the STEP-SPU hardware platform.

## Dependencies

This layer depends on:

- **poky** – the Yocto Project reference distribution  
- **meta-openembedded** – a collection of additional OpenEmbedded layers

## Supported Release Series

- `scarthgap`

## Provided Recipes

- Image: `step-spu-image`
- Packagegroup: `packagegroup-step-spu`
- App payload: `step-spu`
- System config: `step-spu-config`
- Kiosk tweaks: `step-spu-kiosk-tweaks`

## Notes

- `step-spu` fetches application payload from:
  - `https://github.com/ahmedsaleh99/STEP-SPU` (`main` branch)
- The recipe pins a commit with `SRCREV` for reproducible builds.
  Update `SRCREV` in `recipes-spu/step-spu/step-spu.bb` when you want a newer app revision.
- Wi-Fi seed values can be controlled with:
  - `SPU_WIFI_SSID`
  - `SPU_WIFI_PSK`

## Getting Started

### 1. Clone the required layers

```bash
git clone https://git.yoctoproject.org/poky
git clone https://github.com/openembedded/meta-openembedded
git clone https://github.com/ahmedsaleh99/meta-spu
```

### 2. Initialize the build environment

```bash
source poky/oe-init-build-env build
```

### 3. Add the layer to your build

Edit `conf/bblayers.conf` to include the `meta-spu` layer:

```
BBLAYERS += "/path/to/meta-spu"
```

### 4. Set the machine

Edit `conf/local.conf`:

```
MACHINE = "step-spu"
```

### 5. Build the image

```bash
bitbake core-image-minimal
```

## Layer Structure

```
meta-spu/
├── conf/
│   ├── layer.conf          # Layer configuration
│   └── machine/            # Machine configuration files
├── recipes-core/           # Core system recipes and customizations
├── recipes-kernel/         # Linux kernel recipes and patches
└── README.md
```

## Contributing

Contributions are welcome. Please open an issue or submit a pull request on the [GitHub repository](https://github.com/ahmedsaleh99/meta-spu).

## License

This layer is provided under the terms described in the [LICENSE](LICENSE) file.


## Maintainers

- STEP-SPU Team
