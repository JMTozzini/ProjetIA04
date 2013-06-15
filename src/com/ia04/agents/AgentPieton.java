package com.ia04.agents;

import com.ia04.main.Model;
import com.ia04.constantes.ConstantesAgents;

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
		if(!vivant())
			return;
		Bag aAgents = null;
		for(int i=0;i<ConstantesAgents.PERCEPTION_PIETON;i++){
			aAgents = aModel.getYard().getNeighborsMaxDistance(x, y, i, false, null, null, null);
			if(!aAgents.isEmpty()){
				for(int j=0;i<aAgents.size();j++){
					if(aAgents.objs[j] instanceof AgentFeu){
						deplacement((AgentFeu) aAgents.objs[j]);
						eteindreFeu();
						break;
					}
				}		
			}
		}
	}
	
	private void deplacement(AgentFeu aFeu) {
		System.out.println("Feu x:"+ aFeu.getX() + " y:" + aFeu.getY());
		Integer deltaX = this.getX() - aFeu.getX();
		Integer deltaY = this.getY() - aFeu.getY();
		
		
	
	}
	
	private void eteindreFeu() {
		
	}
	private boolean deplacementPossibleHaut(){
		return true;
	}
	private boolean deplacementPossibleBas(){
		return true;
	}
	private boolean deplacementPossibleDroite(){
		return true;
	}
	private boolean deplacementPossibleGauche(){
		return true;
	}

}
