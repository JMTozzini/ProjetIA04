package com.ia04.agents;

import java.util.Iterator;

import com.ia04.constantes.ConstantesAgents;
import com.ia04.main.Model;

import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Int2D;

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
	
	private boolean empty = false;
	public AgentCamion(int iResistance, int iDeplacement,int iForce, int iPerception) {
		super(iResistance, iDeplacement, iForce, iPerception);
	}

	@Override
	public void step(SimState iModel) {
		aModel = (Model) iModel;
		setObjectif();
		int status = deplacement();
		if(status == 0 && empty==false){
			setPieton(aModel,this.getLocation());
			empty = true;
		}
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

	private int deplacement() {
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
		return 1;
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
