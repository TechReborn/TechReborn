#!/bin/bash


#This makes a change log between two git commits.

#Run like ./genChangelog.sh 65f3b30 8aafd11

if [ "$#" -ne 2 ]; then
  echo "Usage: $1 COMMIT1 $2 COMMIT2" >&2
  exit 1
fi

git shortlog $1...$2 > changelog.txt

echo "The changelog has been saved into chnagelog.txt!"
