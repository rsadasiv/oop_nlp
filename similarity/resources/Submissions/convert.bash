#!/bin/bash

for f in `find ./*.txt -type f`; do
    iconv -f cp1252 -t utf-8 $f > $f.converted
#	echo $f
done
