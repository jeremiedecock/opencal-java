#!/bin/sh

cd debian
rm opencal.manifest
ln --symbolic opencal.manifest.squeeze opencal.manifest
cd ..
dpkg-buildpackage -rfakeroot -us -uc
