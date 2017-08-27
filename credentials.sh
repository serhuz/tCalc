# from https://stackoverflow.com/questions/35440907/can-circle-ci-reference-gradle-properties-credentials
#!/usr/bin/env bash

KEYSTORE_CREDENTIALS=${HOME}/tcalc/keystore.properties
export KEYSTORE_CREDENTIALS

if [ ! -f "$KEYSTORE_CREDENTIALS" ]; then
    echo "Creating credentials file..."
    touch $KEYSTORE_CREDENTIALS

    echo "Writing data"
    echo "storeFile=$STORE_FILE" >> ${KEYSTORE_CREDENTIALS}
    echo "storePassword=$STORE_PASSWORD" >> ${KEYSTORE_CREDENTIALS}
    echo "keyAlias=$KEY_ALIAS" >> ${KEYSTORE_CREDENTIALS}
    echo "keyPassword=$KEY_PASSWORD" >> ${KEYSTORE_CREDENTIALS}
    echo "Done."
fi
