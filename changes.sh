#!/bin/bash
[ -f .project ] || {
	echo "Must be called from the root directory of the workspace (.project not found)!"
	exit 1
}
export AOSP_HOME="`grep -A 1 "<name>AOSP_HOME</name>" .project  | sed -ne 's#.*<value>file:\([^<]*\)</value>.*#\1#p'`"
export PROJECT_HOME=$PWD

cp -r $AOSP_HOME/packages/apps/UnlockTrustAgent/* $PROJECT_HOME/src/main

