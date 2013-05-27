package com.ia04.agents;

import com.ia04.constantes.ConstantesAgents;

import sim.engine.SimState;
import sim.engine.Steppable;

public class AgentEnvironnement implements Steppable{

	private static final long serialVersionUID = 1L;
	
	// Position
	private int x=0;
	private int y=0;
		
	// Attributs
	private int type;
	private boolean inflammable;
	private int resInterne;
	private int resExterne;
	private int altitude;
		
	public AgentEnvironnement(int iType, int iAltitude) {
		super();
		this.type = iType;
		this.altitude = iAltitude;
		this.inflammable = true;
		
		switch (type) {
		case 1: // TYPE_VEG_FAIBLE
			resInterne = ConstantesAgents.RES_INT_VEG_FAIBLE;
			resExterne = ConstantesAgents.RES_EXT_VEG_FAIBLE;
			break;
		case 2: // TYPE_VEG_MOY
			resInterne = ConstantesAgents.RES_INT_VEG_MOY;
			resExterne = ConstantesAgents.RES_EXT_VEG_MOY;
			break;
		case 3: // TYPE_VEG_FORTE
			resInterne = ConstantesAgents.RES_INT_VEG_FORTE;
			resExterne = ConstantesAgents.RES_EXT_VEG_FORTE;
			break;
		case 4: // TYPE_HABITATION
			resInterne = ConstantesAgents.RES_INT_HABITATION;
			resExterne = ConstantesAgents.RES_EXT_HABITATION;
			break;
		default: // Autres types non-inflammables
			resInterne = -1;
			resExterne = -1;
			inflammable = false;
			break;
		}
	}

	public void step(SimState iModel) {
		// Implementation du comportement de l'environnement en fonction du type
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isInflammable() {
		return inflammable;
	}

	public void setInflammable(boolean inflammable) {
		this.inflammable = inflammable;
	}

	public int getResInterne() {
		return resInterne;
	}

	public void setResInterne(int resInterne) {
		this.resInterne = resInterne;
	}

	public int getResExterne() {
		return resExterne;
	}

	public void setResExterne(int resExterne) {
		this.resExterne = resExterne;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
