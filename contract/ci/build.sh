#!/bin/sh

buildctl --addr tcp://buildkitd:1234 \
  build \
  --frontend=dockerfile.v0 \
  --local context=. \
  --local dockerfile=. \
  --output type=image,name=${REGISTRY_SERVER}/${REGISTRY_USER}/contract:${BUILD_NUMBER}-kiamol,push=true