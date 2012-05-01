#!/bin/sh

rm -rfv $(cat .gitignore | grep debian)
rm ../opencal*.dsc
rm ../opencal*.changes
rm ../opencal*.tar.gz
rm ../opencal*.deb
