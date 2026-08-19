# Exam App + GitHub Actions APK Builder

This project includes a GitHub Actions workflow that builds a debug APK in GitHub's hosted runner and uploads the APK as a workflow artifact.

## Phone-only setup

1. Create a GitHub repository.
2. Upload all files in this project to the repository, preserving folders.
3. Commit to the `main` branch.
4. Open the repository's **Actions** tab.
5. Select **Build Android APK** and run it if it did not start automatically.
6. Open the completed workflow run.
7. Under **Artifacts**, download `ExamApp-debug-apk`.
8. Extract the downloaded ZIP and install `app-debug.apk` on your Android phone.

The workflow uses GitHub-hosted Ubuntu runners, Java 17, Android SDK setup, Gradle, and `actions/upload-artifact`.
