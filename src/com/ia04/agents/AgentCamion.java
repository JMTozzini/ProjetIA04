package com.ia04.agents;

import com.ia04.main.Model;

import sim.engine.SimState;

@SuppressWarnings("serial")
public class AgentCamion extends AgentPompier {

	public AgentCamion(int iResistance, int iDeplacement,int iForce, int iPerception) {
		super(iResistance, iDeplacement,iForce, iPerception);
	}
	
	@Override
	public void step(SimState iModel) {
		Model aModel = (Model) iModel;
		deplacement();
		setPieton();
		eteindreFeu();
		survivre();
	}
	
	private void deplacement() {
		// Les camions peuvent se doubler mais pas s'arrêter sur la même case
	}
	
	private void setPieton() {
		
	}
	
	private void eteindreFeu() {
		
	}
}
