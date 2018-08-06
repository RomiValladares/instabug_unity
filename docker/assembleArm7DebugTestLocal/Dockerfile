FROM uber/android-build-environment:latest
ADD . /android-play-earn
WORKDIR /android-play-earn
RUN /android-play-earn/scripts/ci/build_assembleArm7Debug.sh
