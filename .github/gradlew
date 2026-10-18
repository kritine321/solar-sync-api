#!/bin/sh
set -e
PROJECT_DIR="$( pwd )"
GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
chmod +x "${PROJECT_DIR}/gradlew"
${PROJECT_DIR}/gradlew assembleDebug --no-daemon
