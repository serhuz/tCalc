#!/usr/bin/env bash

GRADLE_PROPS=${HOME}/tcalc/gradle.properties
export GRADLE_PROPS

if [ ! -f "$GRADLE_PROPS" ]; then

    touch -f ${GRADLE_PROPS} &&

    echo "android.enableJetifier=true" >> ${GRADLE_PROPS} &&
    echo "android.useAndroidX=true" >> ${GRADLE_PROPS}

fi
