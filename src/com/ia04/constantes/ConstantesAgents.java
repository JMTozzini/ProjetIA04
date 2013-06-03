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
	public static int RES_EXT_VEG_MOY		= 20;
	public static int RES_EXT_VEG_FORTE		= 30;
	public static int RES_EXT_HABITATION	= 40;
	
	// Resistances interne liées au type
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
	public static int NB_CANDAIR           = 2;
	
	// Resistance Pompiers
	public static int RES_PIETON   = 100;
	public static int RES_CAMION   = 1000;
	public static int RES_CANADAIR = 10000;
	
	// Deplacement Pompiers
	public static int DEP_PIETON   = 5;
	public static int DEP_CAMION   = 30;
	public static int DEP_CANADAIR = 60;
	
	// Force Pompiers
	public static int FORCE_PIETON   = 2;
	public static int FORCE_CAMION   = 5;
	public static int FORCE_CANADAIR = 10;
	
}
