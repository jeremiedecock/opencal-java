#!/usr/bin/env jython
# -*- coding: utf-8 -*-

# OpenCAL
# Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)

# Usage:
#
#     jython -Dpython.path="./bin/" opencal.py
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
#     jython opencal.py
#
# or simply:
#
#     ./opencal.py

# Java import
from java.net import URI

# OpenCAL import
from org.jdhp.opencal.data.pkb import PersonalKnowledgeBaseFactory
from org.jdhp.opencal.data.properties import ApplicationProperties
import org.jdhp.opencal.model.card.Card
from org.jdhp.opencal.model.professor import ProfessorFactory

def main():

    # Load UserProperties
    ApplicationProperties.loadApplicationProperties()

    # Create Professor
    professor = ProfessorFactory.createProfessor(ApplicationProperties.getProfessorName());
    print "load professor", professor.getName()

    # Open default PKB File and create card set
    uri = URI(ApplicationProperties.getPkbPath())

    print "load pkb", uri
    pkb = PersonalKnowledgeBaseFactory.createPersonalKnowledgeBase("DOM")
    card_collection = pkb.load(uri)

    # Assess cards
    print len(card_collection)
    for card in card_collection:
        card.setGrade(professor.assess(card))

#    # Make and run GUI
#    MainWindow.getInstance().run()
#
#    # Save PKB file
#    pkb.save(card_collection, uri)
#
#    # Save UserProperties
#    ApplicationProperties.saveApplicationProperties()

if __name__ == '__main__':
    main()

