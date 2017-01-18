#!/bin/sh

# Remove trailing spaces

find ./src/ -name "*.java" -exec perl -pi -e 's/[ \t]*$$//' {} \;
