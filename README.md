# Geode Wallpaper Guard

An Android app that sets your supplied `Backround.png` as the system home-screen wallpaper and attempts to restore it when Android reports that the wallpaper has changed.

## Add your image

Put your image at `app/src/main/assets/Backround.png`, commit it, and push to GitHub.

## Build and install

1. Push your changes to GitHub.
2. Open [Actions](https://github.com/SolanceGMD/geode/actions).
3. Download the `geode-wallpaper-debug-apk` artifact from a completed build.
4. Install `app-debug.apk` on your Android device.
5. Open **Geode Wallpaper Guard** and tap **Enable wallpaper protection**.
6. Keep the persistent notification active while protection is wanted. Use **Disable protection** to stop it.

The GitHub Actions workflow builds the debug APK and uploads it as an artifact.

## Android limitation

This is best-effort protection, not a system-level lock. Android's wallpaper-change broadcast is deprecated, and device manufacturers can restrict background services. The app cannot guarantee instant restoration on every phone or prevent the user/system from temporarily applying another wallpaper. The foreground service notification indicates when protection is active.
