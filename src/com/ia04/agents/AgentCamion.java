package com.ia04.agents;

import java.util.Iterator;

import com.ia04.constantes.ConstantesAgents;
import com.ia04.main.Model;

import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Int2D;

@SuppressWarnings("serial")
public class AgentCamion extends AgentPompier {

	private boolean empty = false;
	public AgentCamion(int iResistance, int iDeplacement,int iForce, int iPerception) {
		super(iResistance, iDeplacement,iForce, iPerception);
	}

	@Override
	public void step(SimState iModel) {
		Model aModel = (Model) iModel;
		int status = deplacement();
		if(status == 0 && empty==false){
			setPieton(aModel,this.getLocation());
			empty = true;
		}
		eteindreFeu();
		survivre();
	}

	private int deplacement() {
		// Les camions peuvent se doubler mais pas s'arrêter sur la même case
		return 0;
	}

	private void setPieton(Model aModel, Int2D location) {
		// Le camion est à l'arrêt
		int nbPietonsRestant = ConstantesAgents.NB_PIETON_PAR_CAMION, dist=0;
		System.out.println(location);

		Bag aVoisins = null;
		do{
			dist++;
			aVoisins = getEmptyNeighbors(aModel, location, dist);
		}while(aVoisins.size() < ConstantesAgents.NB_PIETON_PAR_CAMION);

		System.out.println(aVoisins.size());
		Iterator itVoisin = aVoisins.iterator();
		AgentEnvironnement aCase = null;


		while(nbPietonsRestant > 0 && itVoisin.hasNext()){
			aCase = (AgentEnvironnement) itVoisin.next();
			AgentPieton pieton = new AgentPieton(ConstantesAgents.RES_PIETON, ConstantesAgents.DEP_PIETON, ConstantesAgents.FORCE_PIETON, ConstantesAgents.PERCEPTION_PIETON);
			aModel.getYard().setObjectLocation(pieton, aCase.getLocation());
			pieton.setLocation(aCase.getLocation());
			nbPietonsRestant--;
		}
	}

	private Bag getEmptyNeighbors(Model aModel, Int2D location, int dist){
		Bag aPlaces = null, aAgents = null;
		aPlaces = aModel.getYard().getNeighborsMaxDistance(location.getX(), location.getY(), dist, false, aPlaces, null, null);
		for(Object object : aPlaces){
			System.out.println(object.getClass());
			System.out.println();
			if(object instanceof AgentPompier){
				aAgents = aModel.getYard().getObjectsAtLocation(((AgentPompier) object).getLocation());
			} else if(object instanceof AgentFeu){
				aAgents = aModel.getYard().getObjectsAtLocation(((AgentFeu) object).getLocation());
			} else if(object instanceof AgentCanadair){
				aAgents = aModel.getYard().getObjectsAtLocation(((AgentCanadair) object).getLocation());
			} else{
				aAgents = aModel.getYard().getObjectsAtLocation(((AgentEnvironnement) object).getLocation());
			}
			for(Object agent : aAgents){
				if(agent instanceof AgentFeu || agent instanceof AgentCamion || agent instanceof AgentPieton){
					aPlaces.remove(object);
				}
			}
		}
		return aPlaces;
	}

	private void eteindreFeu() {

	}
}
