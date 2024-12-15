#!/bin/bash

DROPDRIVE_DIR=.
DROPDRIVE_ARTIFACT=${project.artifactId}-${project.version}

java -jar $DROPDRIVE_DIR/lib/$DROPDRIVE_ARTIFACT-spring-boot.jar "$@"
