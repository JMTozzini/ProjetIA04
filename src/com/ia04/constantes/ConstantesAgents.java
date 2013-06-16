package com.ia04.constantes;

public class ConstantesAgents {
	
	// Types d'environnement
	public static int TYPE_VEG_FAIBLE	= 1;
	public static int TYPE_ROCHE		= 2;
	public static int TYPE_VEG_MOY		= 3;
	public static int TYPE_VEG_FORTE	= 4;
	public static int TYPE_HABITATION	= 5;
	public static int TYPE_EAU			= 6;
	public static int TYPE_ROUTE		= 7;
	public static int TYPE_BRULE		= 8;
	// !!! L'ordre de ces attribut influence l'ordre du FacetedPortrayal2D dans la vue !!!

	// Resistances externe liées au type
	public static int RES_EXT_VEG_FAIBLE	= 10;
	public static int RES_EXT_VEG_MOY		= 110;
	public static int RES_EXT_VEG_FORTE		= 160;
	public static int RES_EXT_HABITATION	= 210;
	
	// Resistances interne liées au type
	public static int RES_INT_VEG_FAIBLE	= 100;
	public static int RES_INT_VEG_MOY		= 150;
	public static int RES_INT_VEG_FORTE		= 200;
	public static int RES_INT_HABITATION	= 250;
	
	// Sens pour Eau et Route
	public static int NORD	= 1;
	public static int SUD	= 2;
	public static int EST	= 3;
	public static int OUEST	= 4;
	
	// Nombres Agents Pompiers
	public static int NB_CAMION            = 4;
	public static int NB_PIETON_PAR_CAMION = 6;
	public static int NB_CANDAIR           = 2;
	
	// Resistance Pompiers
	public static int RES_PIETON   = 300;
	public static int RES_CAMION   = 1000;
	public static int RES_CANADAIR = 10000;
	
	// Deplacement Pompiers
	public static int DEP_PIETON   = 1;
	public static int DEP_CAMION   = 3;
	public static int DEP_CANADAIR = 6;
	
	// Force Pompiers
	public static int FORCE_PIETON   = 2;
	public static int FORCE_CAMION   = 6;
	public static int FORCE_CANADAIR = 10;
	
	// Autres attributs
	public static int INTERVAL_ACTION_CANADAIR = 50;
	public static int NEW_OBJECTIF_MIN_DISTANCE = 3;
	
	// Caracteristiques Feu
	public static int FEU_FORCE	= 3;
	public static int FEU_RES	= 8;
	public static int VIT_PASSAGE_ROUTE = 5;
	
	// Perception Pompiers
	public static int PERCEPTION_PIETON   = 10;
	public static int PERCEPTION_CAMION   = 50;
	public static int PERCEPTION_CANADAIR = 100;
	
	// Distance d'extinction du feu
	public static int DIST_EXTINCTION_CAMION   = 3;
	public static int DIST_EXTINCTION_CANADAIR = 3;
	
	
	//Probabilit�s pour le d�placement des pompiers
	//Proba qu'il essaie de r�duire la distance la + longue
	public static int REDUCE_HIGHER = 50;
	//Proba qu'il essaie de r�duire la distance la + courte	
	public static int REDUCE_LOWER = 30;
	//Proba qu'il essaie d'augmenter la distance la + courte	
	public static int INCREASE_LOWER = 15;
	//Proba qu'il essaie d'augmenter la distance la + longue	
	public static int INCREASE_HIGHER = 5;
	
	//Les pi�tons ont un certain nombre d'essai afin de trouver une bonne position pour se d�place
	//Si a l'issue de ce nombre d'essai ils n'ont pas r�ussi � trouver une place correcte
	//Ils passent leur tour
	public static int TRY_POSITION = 5;
}
