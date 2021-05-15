#!/usr/bin/env bash

# only run this script if xcode version is > 11
# see https://stackoverflow.com/questions/41451018/using-bash-to-get-the-second-word-of-the-first-line
# and https://stackoverflow.com/questions/6245293/extract-version-number-from-file-in-shell-script

version=$(xcodebuild -version | awk 'NR==1 {print $2}')
parsed=( ${version//./ } )
if [ "${parsed[0]}" -lt 12 ]
then
  echo "xcode version < 12, no compilation needed"
  exit
else
  echo "xcode version >= 12, recompiling dependencies"
  # exit
fi

# procedure found here :
# https://github.com/polarofficial/polar-ble-sdk/issues/97#issuecomment-702174877

# go to current script directory
# (see https://stackoverflow.com/questions/3349105/how-to-set-current-working-directory-to-the-directory-of-the-script-in-bash)
cd $(cd -P -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd -P)

# get custom carthage script (needs carthage installed)
source ./carthage.sh

# then go to PolarBleSdk source folder
cd ../../polar-ble-sdk/sources/iOS/ios-communications

# now call the modified carthage script to rebuild RxSwift
cart update RxSwift --no-use-binaries --platform iOS

# then build the PolarBleSdk framework
source ./build_sdk.sh

# for some reason the build_sdk.h script ends up cd'ing into 3rd_party_sdk
# so we come back where we were before
cd ..

# and finally copy the two new frameworks into the ios sdk folder,
# replacing the original ones :
cp -r ./Carthage/Build/iOS/RxSwift.framework ../../../polar-sdk-ios
cp -r ./3rd_party_sdk/PolarBleSdk.framework ../../../polar-sdk-ios
