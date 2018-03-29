#!/usr/bin/env bash

FABRIC_PROPS=${HOME}/tcalc/fabric.properties
export FABRIC_PROPS

if [ ! -f "$FABRIC_PROPS" ]; then

    echo "Creating Fabric props file..."
    touch ${FABRIC_PROPS}
    echo "Done."

    echo "Writing data"
    echo "apiKey=$FABRIC_API_KEY" >> ${FABRIC_PROPS}
    echo "Done."

fi
