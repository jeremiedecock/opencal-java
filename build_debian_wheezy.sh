#!/bin/sh

cd debian
rm opencal.manifest
ln --symbolic opencal.manifest.wheezy opencal.manifest
cd ..
dpkg-buildpackage -rfakeroot -us -uc

