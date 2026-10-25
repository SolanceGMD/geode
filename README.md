# Geode Wallpaper

A simple Android live wallpaper app that displays your own `Backround.png` image and pans it as the launcher reports horizontal home-screen page swipes.

## Add your image

1. Upload your image to `app/src/main/assets/Backround.png` (the filename spelling is intentional).
2. Commit and push it to GitHub.
3. Open **Actions** and select **Build Android APK**. The workflow builds a debug APK and uploads it as the `geode-wallpaper-debug-apk` artifact.
4. Download and install `app-debug.apk` on your Android phone.
5. Open **Geode Wallpaper**, tap **Set live wallpaper**, and confirm in Android's wallpaper picker.

## Notes

- This is an Android live wallpaper, not a static wallpaper setter. Android launchers report wallpaper offsets as users swipe between home-screen pages; exact behavior depends on the launcher and device.
- The image is center-cropped to fill the screen. Horizontal panning is most noticeable when the image is wider than the screen.
- The repository does not include your image yet. Until you add it, the wallpaper displays a dark fallback background.
- GitHub Actions builds the APK; it does not automatically install it on your phone.
