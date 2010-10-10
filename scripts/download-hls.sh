#! /bin/sh

BASE="$( cd "$( dirname "$0" )" && pwd )"

java -jar "$BASE/download-hls.jar" "$@"