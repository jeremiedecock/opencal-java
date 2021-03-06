#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Copyright (c) 2012 Jérémie DECOCK (http://www.jdhp.org)

import argparse
import xml.sax

from xml.sax.handler import ContentHandler, ErrorHandler

# ContentHandler ############################

class MyContentHandler(ContentHandler):
    """Get known words in english questions"""

    def __init__(self):
        pass

    def startDocument(self):
        pass

    def endDocument(self):
        pass

    def startElement(self, name, attr):
        if name == "card":
            pass
        if name == "question":
            pass
        elif name == "tag":
            pass

    def endElement(self, name):
        if name == "card":
            pass
        if name == "question":
            pass
        elif name == "tag":
            pass

    def characters(self, ch):
        pass

# ErrorHandler ##############################

class MyErrorHandler(ErrorHandler):
    def fatalError(self, exception):
        print("Fatal error:", exception)

    def error(self, exception):
        print("Error:", exception)

    def warning(self, exception):
        print("Warning:", exception)


def main():
    """Main function"""

    # Parse program arguments
    argparser = argparse.ArgumentParser(description='OPC tool.')
    argparser.add_argument('filenames', nargs='+', metavar='FILE', help='file to read')
    args = argparser.parse_args()

    # Make XML parser
    xmlreader = xml.sax.make_parser()
    xmlreader.setContentHandler(MyContentHandler())
    xmlreader.setErrorHandler(MyErrorHandler())

    # Parse XML files
    for filename in args.filenames:
        xmlreader.parse(filename)

if __name__ == '__main__':
    main()
