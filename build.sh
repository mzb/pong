#!/bin/sh

echo "Building pong.server and pong.client..."

ant -f build-server.xml clean
ant -f build-server.xml && ant -f build-client.xml
