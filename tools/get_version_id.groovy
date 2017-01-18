#!/usr/bin/env groovy

// OpenCAL
// Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie Decock

// Usage:
//
//     groovy -cp ./bin/ get_version_id.groovy
//
// Declare the CLASSPATH variable in your environment to avoid the "-cp"
// option, e.g. add the following line to the end of your bashrc or
// bash_profile:
//
//     export CLASSPATH="/home/YOUR/PATH/TO/OPENCAL/bin:$CLASSPATH"
//
// Then you can simply run this script with:
//
//     groovy get_version_id.groovy
//
// or simply:
//
//     ./get_version_id.groovy

import org.jdhp.opencal.OpenCAL;

println(OpenCAL.PROGRAM_VERSION);
