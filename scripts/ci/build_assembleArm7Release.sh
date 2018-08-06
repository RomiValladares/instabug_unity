#!/bin/sh

mkdir "${ANDROID_HOME}/licenses" || true
echo "8933bad161af4178b1185d1a37fbf41ea5269c55" > "${ANDROID_HOME}/licenses/android-sdk-license"

echo $JKS_CONTENT_BASE64_PLAYANDEARN_PLAYSTORE_BETA | base64 -d > app/playandearn_playstore_beta.jks

./gradlew assembleArm7Release
