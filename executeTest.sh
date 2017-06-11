#!/bin/bash
inputEnv=$1
inputTags=$2
echo 'Environment: '$inputEnv
if [ "$inputEnv" == "" ] ; then
    echo "No environment provided, exiting without executing tests"
    exit 1
else
    if [ "$inputTags" == "" ] ; then
        tags='~@ignored'
    else
        tags='@'$inputTags
    fi
    BASE_URL='https://'$inputEnv'.digital.hbc.com/v1'
    echo 'Running integration tests against: '${BASE_URL}
    mvn clean test -DtestEnvironment="$BASE_URL" -Dcucumber.options="--tags $tags"
fi