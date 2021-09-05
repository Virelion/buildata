#!/bin/bash

./gradlew clean publishToMavenLocal -Prelease.version=0.0.0-SNAPSHOT -PisMainHost=true
cd integration-test-project
./gradlew clean check --info
