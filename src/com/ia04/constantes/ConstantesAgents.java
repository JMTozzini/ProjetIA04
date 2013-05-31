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
	// !!! L'ordre de ces attribut influence l'ordre du FacetedPortrayal2D dans la vue !!!
	
	// Resistances interne liées au type
	public static int RES_INT_VEG_FAIBLE	= 10;
	public static int RES_INT_VEG_MOY		= 20;
	public static int RES_INT_VEG_FORTE		= 30;
	public static int RES_INT_HABITATION	= 40;

	// Resistances externe liées au type
	public static int RES_EXT_VEG_FAIBLE	= 10;
	public static int RES_EXT_VEG_MOY		= 20;
	public static int RES_EXT_VEG_FORTE		= 30;
	public static int RES_EXT_HABITATION	= 40;
	
	// Sens pour Eau et Route
	public static int NORD	= 1;
	public static int SUD	= 2;
	public static int EST	= 3;
	public static int OUEST	= 4;
	
	
}
