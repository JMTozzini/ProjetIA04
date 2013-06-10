package com.ia04.agents;

import com.ia04.main.Model;

import sim.engine.SimState;
import sim.util.Bag;

@SuppressWarnings("serial")
public class AgentPieton extends AgentPompier {

	public AgentPieton(int iResistance, int iDeplacement,int iForce, int iPerception) {
		super(iResistance, iDeplacement,iForce, iPerception);
	}
	
	@Override
	public void step(SimState iModel) {
		Model aModel = (Model) iModel;
		Bag aAgents = aModel.getYard().getNeighborsMaxDistance(x, y, 1, false, null, null, null);
		
		deplacement();
		eteindreFeu();
		survivre();
	}
	
	private void deplacement() {
		
	}
	
	private void eteindreFeu() {
		
	}

}
