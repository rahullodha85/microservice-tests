#!/bin/bash
inputTags=$1
inputEnv=$2
echo 'Environment: '$inputEnv
echo "Running tests for following services: "$inputTags
if [ "$inputTags" == "" ] ; then
	echo "No tags selected, Running test for all services"
	mvn clean test
else
	IFS=',' read -ra ADDR <<< "$inputTags"
	for i in "${ADDR[@]}"; do
    	if [ "$tags" == "" ]; then
			tags=$tags"@"$i
		else
			tags=$tags",@"$i
		fi
	done
	if [ "$inputEnv" == "" ] ; then
		mvn clean test -Dcucumber.options="--tags $tags"
	else
        mvn clean test -DtestEnv="$inputEnv" -Dcucumber.options="--tags $tags"
	fi

fi