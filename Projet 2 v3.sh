#!/bin/sh

MOUNT_SCRIPT="/media/data/gremy/cryptoloop/developpement_logiciel.mount.sh"
UMOUNT_SCRIPT="/media/data/gremy/cryptoloop/developpement_logiciel.umount.sh"
OPENCAL_SCRIPT="/media/data/gremy/developpement_logiciel/workspaces/eclipse/projets_java/project_0002/run.sh"

if [ -e "$OPENCAL_SCRIPT" ]; then
{
	echo -e "\nLa partition est déjà montée."
	$OPENCAL_SCRIPT
}
else
{
	echo -n -e "\nLa partition cryptée n'est pas montée. Souhaitez vous la monter ? (o/N) : "
	read REPONSE_UTILISATEUR

	if [ "$REPONSE_UTILISATEUR" = "o" ] || [ "$REPONSE_UTILISATEUR" = "O" ]; then
	{
		$MOUNT_SCRIPT &&
		$OPENCAL_SCRIPT &&
		echo -n -e "\nSouhaitez vous démonter la partition cryptée ? (o/N) : " &&
		read REPONSE_UTILISATEUR &&

		if [ "$REPONSE_UTILISATEUR" = "o" ] || [ "$REPONSE_UTILISATEUR" = "O" ]; then
		{
			# * Vérifie avec fuser que la partition n'est pas utilisée par un processus
			#    -> Si non, démonte la partition et quite
			#    -> Si oui, affiche une alerte (indiquant entre autre le(s) PID concerné(s)) et repose la question (avec une 3e option : killer automatiquement le(s) processus concerné(s))
			#    -> Si réponse non correcte, oui par défaut (?)
			$UMOUNT_SCRIPT
		}
		fi
	}
	elif [ "$REPONSE_UTILISATEUR" = "n" ] || [ "$REPONSE_UTILISATEUR" = "N" ]; then
	{
		echo "Requête annulée."
	}
	else
	{
		echo "Réponse incorrect. Requête annulée."
	}
	fi
}
fi
