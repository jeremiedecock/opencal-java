#!/usr/bin/env jython
# -*- coding: utf-8 -*-

# OpenCAL
# Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)

# Usage:
#
#     jython -Dpython.path="./bin/" check_css.py
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
#     jython check_css.py
#
# or simply:
#
#     ./check_css.py

# Java import
from java.net import URI

# OpenCAL import
from org.jdhp.opencal.ui.css import CSS

def main():
    print("*** EDITABLE_BROWSER_CSS ***")
    html = CSS.EDITABLE_BROWSER_CSS
    print(html)

    print()
    print("*** REVIEW_CSS ***")
    html = CSS.REVIEW_CSS
    print(html)

if __name__ == '__main__':
    main()

