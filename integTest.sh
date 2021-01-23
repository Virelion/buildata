#!/bin/bash

./gradlew clean publishToMavenLocal -Prelease.version=0.0.0-SNAPSHOT
cd integration-test-project
./gradlew clean check --info
