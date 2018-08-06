#!/bin/bash

# We also flatten the path. This should be fairly platform-agnostic (realpath 
# doesn't exist everywhere).
APP_ROOT=`readlink -m $0/../../..`

CLONE_ROOT="$(mktemp -d)"

echo "Build root: $CLONE_ROOT"


# git-archive will fail unless we're in the root.
cd $APP_ROOT


echo "Cloning directory."

# Create a pristine directory (so our build process in Docker established as 
# "root" doesn't interfere with the build process outside of it under our 
# normal user). This will not clone anything that isn't pushed.
git clone -l --single-branch $APP_ROOT $CLONE_ROOT


echo "Building image."

# Relocate Dockerfile to root.
cp $APP_ROOT/docker/assembleArm7DebugTestLocal/Dockerfile $CLONE_ROOT

IMAGE_ID="$(docker build -q $CLONE_ROOT)"
if [ $? -ne 0 ]; then
    echo "Build failed."
    exit 1
fi

rm -fr $CLONE_ROOT

echo "Image ID: [$IMAGE_ID]"


echo "Running image."

#docker run -it --memory=4g $IMAGE_ID
docker run -it --memory=4g --entrypoint="/bin/bash" $IMAGE_ID
