#!/bin/bash
REPO_URL=https://github.com/ruben1191018/cogsi-1190914-1191018-1191042.git

if [ $CLONE_REPO == "true" ]; then
    echo "Cloning repository..."
    git clone $REPO_URL
else
    echo "Skipping repository clone."
fi

if [ $START_CHAT_SERVICE == "true" ]; then
    echo "Building chat service..."
    cd cogsi-1190914-1191018-1191042/ca2/part1/gradle_basic_demo-main
    gradle build 
    
    echo "Starting chat service..."
    gradle runServer &

    cd ../../../../
else
    echo "Skipping start chat service."
fi


if [ $START_REST_SERVICE == "true" ]; then
    echo "Building rest service..."
    cd cogsi-1190914-1191018-1191042/ca2/part2/app
    gradle build
    
    echo "Starting rest service..."
    gradle bootRun &
else
    echo "Skipping rest chat service."
fi


