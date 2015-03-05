#!/bin/sh

find ./ -iname "*.proto*" -exec protoc -I=./src/main/proto/ --java_out=./src/main/java/ {} \;
