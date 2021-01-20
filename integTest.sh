#!/bin/bash

./gradlew clean publishToMavenLocal -Prelease.version=0.0.0-SNAPSHOT
cd demo-project
./gradlew clean check --info
