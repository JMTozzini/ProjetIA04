package com.ia04.agents;

import sim.engine.SimState;
import sim.engine.Steppable;

public class AgentEnvironnement implements Steppable{

	private static final long serialVersionUID = 1L;
	
	private int type;
	private boolean inflammable;
	private int resInterne;
	private int resExterne;
	private int altitude;
		
	public AgentEnvironnement(int iType, boolean iInflammable, int iResInterne, int iResExterne, int iAltitude) {
		super();
		this.type = iType;
		this.inflammable = iInflammable;
		this.resInterne = iResInterne;
		this.resExterne = iResExterne;
		this.altitude = iAltitude;
	}

	public void step(SimState iModel) {
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

}
