package com.ia04.agents;

import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Int2D;

import com.ia04.constantes.ConstantesAgents;
import com.ia04.main.Model;

@SuppressWarnings("serial")
public class AgentCamion extends AgentPompier {

	/**
	 * L'objectif est un agent route, contigu par des routes à l'agent camion,
	 * sans autres agents pompiers dessus,
	 * et pour lequel il y a le feu le plus proche
	 */
	private AgentEnvironnement aObjectif = null;
	private Bag aRoutes = new Bag();
	private Model aModel = null;
	
	public AgentCamion(int iResistance, int iDeplacement,int iForce, int iPerception) {
		super(iResistance, iDeplacement, iForce, iPerception);
	}
	
	@Override
	public void step(SimState iModel) {
		aModel = (Model) iModel;
		setObjectif();
		deplacement();
		setPieton();
		eteindreFeu();
		// survivre(); // invincibles
	}
	
	private void setObjectif() {
		AgentEnvironnement oldObjectif = aObjectif;
		aObjectif = null;
		
		// détection des routes dans l'espace de perception
		aRoutes = aModel.getNeighborsByType(getLocation(), getPerception(), ConstantesAgents.TYPE_ROUTE);
		
		// elimination des routes non accessibles
		Bag aRoutesAccessibles = aModel.getNeighborsByType(getLocation(), 0, ConstantesAgents.TYPE_ROUTE);
		boolean addAccessible = true;
		while (addAccessible){
			addAccessible = false;
			for(Object aRoute : aRoutes){ // pour chaque route percu et non encore marquées comme accessible
				Bag aVoisinsRoute = aModel.getNeighborsByType(((AgentEnvironnement)aRoute).getLocation(), 1, ConstantesAgents.TYPE_ROUTE);
				for(Object aVoisin : aVoisinsRoute){ // pour chaque voisin de cette route, cherche si l'une d'entre elles est accessible
					if(aRoutesAccessibles.contains(aVoisin)){
						aRoutesAccessibles.add(aRoute);
						aRoutes.remove(aRoute);
						addAccessible = true;
					}
				}
			}
		}
		
		aRoutes = aRoutesAccessibles; // réaffectation des routes candidates dans aRoutes
		
		// élimination des routes occupées
		for (Object aRoute : aRoutes){
			Bag aAgents = aModel.getYard().getObjectsAtLocation(((AgentEnvironnement)aRoute).getLocation());
			for (Object i : aAgents){
				if((i instanceof AgentCamion) || (i instanceof AgentPieton)){
					aRoutes.remove(aRoute);
				}
			}
		}
		
		// recherche des feu visibles
		Bag aBagFeu = getFeuNeighbors(aModel, getPerception());
		
		// recherche de la route avec le feu le plus proche
		Integer minDistance = null;
		for (Object aRoute : aRoutes){
			Integer distance = getNearestFireDistance(aBagFeu,((AgentEnvironnement)aRoute).getLocation());
			if(distance != null){
				if(aObjectif == null || (minDistance > distance)){
					aObjectif = (AgentEnvironnement)aRoute;
					minDistance = distance;
				}
			}
		}
		
		// rajout de la case où se trouve le camion lui même
		aRoutes.addAll(aModel.getNeighborsByType(getLocation(), 0, ConstantesAgents.TYPE_ROUTE));
		
		if (aObjectif != null && oldObjectif != null && (getDistance(aObjectif.getLocation(), oldObjectif.getLocation()) < ConstantesAgents.NEW_OBJECTIF_MIN_DISTANCE) && aRoutes.contains(oldObjectif)){
			aObjectif = oldObjectif;
		}
		if (aObjectif == null){
			System.out.println("objectif : null");
		} else {
			System.out.println("objectif : " + aObjectif.toString());
		}
	}
	
	private void deplacement() {
		// Les camions peuvent se doubler mais pas s'arrêter sur la même case
		Bag aRoutesDeplacement = new Bag();
		if (aRoutes.numObjs > 0 && !getLocation().equals(aObjectif.getLocation())){
			// cherche les routes accessibles qui sont à une distance de getDeplacement() au maximum
			aRoutesDeplacement = aModel.getNeighborsByType(getLocation(), getDeplacement(), ConstantesAgents.TYPE_ROUTE);
			// elimination des routes non accessibles
			for(Object aRoute : aRoutesDeplacement){
				if(!aRoutes.contains(aRoute)){
					aRoutesDeplacement.remove(aRoute);
				}
			}
		}
		AgentEnvironnement aObjectifDeplacement = getNearestAgent(aRoutesDeplacement, aObjectif.getLocation());
		if(aObjectifDeplacement != null){
			setLocation(aObjectifDeplacement.getLocation());
			aModel.getYard().setObjectLocation(this, aObjectifDeplacement.getLocation());
		}
	}
	
	private void setPieton() {
		
	}
	
	private void eteindreFeu() {
		
	}
	
	private Integer getNearestFireDistance(Bag iBagFeu, Int2D iLocation){
		Integer distance = null;
		for(Object aFeu : iBagFeu){
			Int2D feuLocation = ((AgentFeu)aFeu).getLocation();
			if (distance == null || getDistance(feuLocation, iLocation) < distance){
				distance = getDistance(feuLocation, iLocation);
			}
		}
		return distance;
	}
	
	private AgentEnvironnement getNearestAgent(Bag iBagEnvironnement, Int2D iLocation){
		Integer distance = null;
		AgentEnvironnement agent = null;
		for(Object aAgent : iBagEnvironnement){
			Int2D agentLocation = ((AgentEnvironnement)aAgent).getLocation();
			if (distance == null || getDistance(agentLocation, iLocation) < distance){
				distance = getDistance(agentLocation, iLocation);
				agent = (AgentEnvironnement)aAgent;
			}
		}
		return agent;
	}
	
	private int getDistance(Int2D iLocation1, Int2D iLocation2){
		return Math.max(Math.abs(iLocation1.x - iLocation2.x), Math.abs(iLocation1.y - iLocation2.y));
	}
}
