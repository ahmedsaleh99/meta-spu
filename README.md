# meta-spu-custom

Custom meta-layer for STEP-SPU Yocto distribution providing local recipes and configuration.

## Structure

```
meta-spu-custom/
├── conf/
│   └── layer.conf          # Layer configuration for Bitbake
├── recipes-graphics/
│   └── plymouth/
│       └── plymouth-theme-spu-splash_1.0.bb  # Plymouth theme recipe
└── README.md               # This file
```

## Recipes

### plymouth-theme-spu-splash

**Purpose:** Packages and installs the SPU custom Plymouth splash theme

**Files Included:**
- `spu-splash.plymouth` - Theme metadata configuration
- `spu-splash.script` - Plymouth theme script (controls animation/display)
- `SPU-Logo.png` - Splash screen image asset

**Installation Path:** `/usr/share/plymouth/themes/spu-splash/`

**Auto-set Default:** The recipe automatically sets this as the default Plymouth theme via post-install script

**Dependencies:**
- `plymouth` (runtime and build-time)

## Integration with KAS

The layer is integrated into `scripts/kas/step-spu.yaml`:

1. **Layer Registration:**
   ```yaml
   repos:
     meta-spu-custom:
       path: ../../meta-spu-custom
       layers:
         .:
   ```

2. **Recipe Installation:**
   ```yaml
   IMAGE_INSTALL:append = "plymouth-theme-spu-splash"
   ```

3. **Theme Configuration:**
   ```yaml
   SPLASH = "plymouth"
   PACKAGECONFIG:pn-plymouth = "pango drm udev systemd"
   PLYMOUTH_DEFAULT_THEME = "spu-splash"
   APPEND:append = " quiet splash"
   ```

## Building

The theme is automatically built when you run:

```bash
cd /home/Shared/assale02/STEP-SPU
KAS_WORK_DIR=/mnt/docker-data/yocto_build kas-container build ./scripts/kas/step-spu.yaml
```

## Customization

To customize the theme:

1. **Edit Theme Script:**
   - Modify `recipes-graphics/plymouth/spu-splash.script` for display logic

2. **Update Theme Config:**
   - Edit `recipes-graphics/plymouth/spu-splash.plymouth` for metadata

3. **Replace Logo:**
   - Swap `SPU-Logo.png` with your custom image (keep same filename)

4. **Update Recipe:**
   - Modify `recipes-graphics/plymouth/plymouth-theme-spu-splash_1.0.bb` if adding new files

## Layer Compatibility

- **Yocto Version:** scarthgap
- **Bitbake Priority:** 6
- **Dependencies:** meta-openembedded, openembedded-core

## Footer

Created as part of STEP-SPU Yocto build configuration.
