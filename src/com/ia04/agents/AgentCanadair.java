package com.ia04.agents;

import com.ia04.main.Model;

import sim.engine.SimState;

@SuppressWarnings("serial")
public class AgentCanadair extends AgentPompier {
	
	public AgentCanadair(int iResistance, int iDeplacement,int iForce) {
		super(iResistance, iDeplacement,iForce);
	}
	
	@Override
	public void step(SimState iModel) {
		Model aModel = (Model) iModel;
		deplacement();
		eteindreFeu();
		survivre();
	}
	
	private void deplacement(){
		
	}

	private void eteindreFeu(){
		
	}
}