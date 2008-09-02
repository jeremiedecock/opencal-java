#!/bin/sh

PKB_SRC="/home/gremy/gremy.pkb"
PKB_DEST="/media/data/gremy/developpement_logiciel/workspaces/svn/opencal/gremy.pkb"

echo "Sauvegarde de la base de connaissances"
rm $PKB_DEST &&
cp $PKB_SRC $PKB_DEST &&

CURRENT_DIR=`pwd` &&
cd "`dirname $PKB_DEST`" &&
svn ci -m "Update Gremy's PKB file." &&
cd "$CURRENT_DIR"
