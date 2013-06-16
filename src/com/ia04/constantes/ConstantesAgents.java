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

	// Resistances externe li√©es au type
	public static int RES_EXT_VEG_FAIBLE	= 10;
	public static int RES_EXT_VEG_MOY		= 20;
	public static int RES_EXT_VEG_FORTE		= 30;
	public static int RES_EXT_HABITATION	= 40;
	
	// Resistances interne li√©es au type
	public static int RES_INT_VEG_FAIBLE	= 50;
	public static int RES_INT_VEG_MOY		= 60;
	public static int RES_INT_VEG_FORTE		= 70;
	public static int RES_INT_HABITATION	= 80;
	
	// Sens pour Eau et Route
	public static int NORD	= 1;
	public static int SUD	= 2;
	public static int EST	= 3;
	public static int OUEST	= 4;
	
	// Nombres Agents Pompiers
	public static int NB_CAMION            = 3;
	public static int NB_PIETON_PAR_CAMION = 5;
	public static int NB_CANDAIR           = 1;
	
	// Resistance Pompiers
	public static int RES_PIETON   = 100;
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
	public static int FEU_FORCE	= 1;
	public static int FEU_RES	= 4;
	
	// Perception Pompiers
	public static int PERCEPTION_PIETON   = 10;
	public static int PERCEPTION_CAMION   = 50;
	public static int PERCEPTION_CANADAIR = 100;
	
	// Distance d'extinction du feu
	public static int DIST_EXTINCTION_CAMION   = 1;
	public static int DIST_EXTINCTION_CANADAIR = 2;
	
	
	//Probabilités pour le déplacement des pompiers
	//Proba qu'il essaie de réduire la distance la + longue
	public static int REDUCE_HIGHER = 50;
	//Proba qu'il essaie de réduire la distance la + courte	
	public static int REDUCE_LOWER = 30;
	//Proba qu'il essaie d'augmenter la distance la + courte	
	public static int INCREASE_LOWER = 15;
	//Proba qu'il essaie d'augmenter la distance la + longue	
	public static int INCREASE_HIGHER = 5;
	
	//Les piétons ont un certain nombre d'essai afin de trouver une bonne position pour se déplace
	//Si a l'issue de ce nombre d'essai ils n'ont pas réussi à trouver une place correcte
	//Ils passent leur tour
	public static int TRY_POSITION = 5;
}
