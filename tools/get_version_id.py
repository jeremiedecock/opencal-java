#!/usr/bin/env jython
# -*- coding: utf-8 -*-

# OpenCAL
# Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)

# Usage:
#
#     jython -Dpython.path="./bin/" get_version_id.py
#
# Declare the CLASSPATH variable in your environment to avoid the "-cp"
# option, e.g. add the following line to the end of your bashrc or
# bash_profile:
#
#     export CLASSPATH="/YOUR/PATH/TO/OPENCAL/bin:$CLASSPATH"
#
# For Unix users, don't use the shorten notation of your home path ("~/") in
# CLASSPATH definition, it won't work!
#
# Then you can simply run this script with:
#
#     jython get_version_id.py
#
# or simply:
#
#     ./get_version_id.py

from org.jdhp.opencal import OpenCAL

print(OpenCAL.PROGRAM_VERSION)
