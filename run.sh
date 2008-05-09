#!/bin/sh

# OpenCAL v3
# Copyright (c) 2006,2007,2008 Jérémie DECOCK

OPENCAL_ROOT="/media/data/gremy/developpement_logiciel/workspaces/eclipse/projets_java/project_0002"
OPENCAL_LIBS="$OPENCAL_ROOT/bin/:$OPENCAL_ROOT/ext/lib/org.eclipse.swt_3.3.2.v3347.jar:$OPENCAL_ROOT/ext/lib/org.eclipse.swt.gtk.linux.x86_3.3.2.v3347.jar:$OPENCAL_ROOT/ext/lib/jcommon-1.0.12.jar:$OPENCAL_ROOT/ext/lib/jfreechart-1.0.9.jar"

java -cp $OPENCAL_LIBS org.jdhp.opencal.OpenCAL