package com.ia04.agents;

import java.util.Iterator;

import com.ia04.constantes.ConstantesAgents;
import com.ia04.main.Model;
import com.sun.org.apache.bcel.internal.classfile.ConstantValue;

import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Int2D;

@SuppressWarnings("serial")
public class AgentCamion extends AgentPompier {

	/**
	 * L'objectif est un agent route, contigu par des routes � l'agent camion,
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
		boolean done = false;
		setObjectif();
		done = deplacement();
		if(!done && empty==false){
			done = setPieton(aModel);
		}
		if(!done){
			eteindreFeu();
		}
		// survivre(); // invincibles
	}
	
	private void setObjectif() {
		AgentEnvironnement oldObjectif = aObjectif;
		aObjectif = null;
		
		// d�tection des routes dans l'espace de perception
		aRoutes = aModel.getNeighborsByType(getLocation(), getPerception(), ConstantesAgents.TYPE_ROUTE);
		
		// elimination des routes non accessibles
		Bag aRoutesAccessibles = aModel.getNeighborsByType(getLocation(), 0, ConstantesAgents.TYPE_ROUTE);
		boolean addAccessible = true;
		while (addAccessible){
			addAccessible = false;
			for(Object aRoute : aRoutes){ // pour chaque route percu et non encore marqu�es comme accessible
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
		
		aRoutes = aRoutesAccessibles; // r�affectation des routes candidates dans aRoutes
		
		// �limination des routes occup�es
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
		
		// rajout de la case o� se trouve le camion lui m�me
		aRoutes.addAll(aModel.getNeighborsByType(getLocation(), 0, ConstantesAgents.TYPE_ROUTE));
		
		if (aObjectif != null && oldObjectif != null && (getDistance(aObjectif.getLocation(), oldObjectif.getLocation()) < ConstantesAgents.NEW_OBJECTIF_MIN_DISTANCE) && aRoutes.contains(oldObjectif)){
			aObjectif = oldObjectif;
		}
		if (aObjectif == null){
//			System.out.println("objectif : null");
		} else {
//			System.out.println("objectif : " + aObjectif.toString());
		}
	}

	private boolean deplacement() {
		// Les camions peuvent se doubler mais pas s'arr�ter sur la m�me case
		Bag aRoutesDeplacement = new Bag();
		if (aRoutes.numObjs > 0 && aObjectif!= null && !getLocation().equals(aObjectif.getLocation())){
			// cherche les routes accessibles qui sont � une distance de getDeplacement() au maximum
			aRoutesDeplacement = aModel.getNeighborsByType(getLocation(), getDeplacement(), ConstantesAgents.TYPE_ROUTE);
			
			// elimination des routes non accessibles
			Bag aRoutesAccessibles = aModel.getNeighborsByType(getLocation(), 0, ConstantesAgents.TYPE_ROUTE);
			boolean addAccessible = true;
			while (addAccessible){
				addAccessible = false;
				for(Object aRoute : aRoutesDeplacement){ // pour chaque route percu dans l'espace de d�placement et non encore marqu�es comme accessible
					Bag aVoisinsRoute = aModel.getNeighborsByType(((AgentEnvironnement)aRoute).getLocation(), 1, ConstantesAgents.TYPE_ROUTE);
					for(Object aVoisin : aVoisinsRoute){ // pour chaque voisin de cette route, cherche si l'une d'entre elles est accessible
						if(aRoutesAccessibles.contains(aVoisin)){
							aRoutesAccessibles.add(aRoute);
							aRoutesDeplacement.remove(aRoute);
							addAccessible = true;
						}
					}
				}
			}
			aRoutesDeplacement.clear();
			aRoutesDeplacement = aRoutesAccessibles;
			
			// regarde si il s'agit bien de route possibles d�termin�es dans setObjectif
			for(Object aRoute : aRoutesDeplacement){
				if(!aRoutes.contains(aRoute)){
					aRoutesDeplacement.remove(aRoute);
				}
			}
			
			AgentEnvironnement aObjectifDeplacement = getNearestAgent(aRoutesDeplacement, aObjectif.getLocation());
			if(aObjectifDeplacement != null){
				setLocation(aObjectifDeplacement.getLocation());
				aModel.getYard().setObjectLocation(this, aObjectifDeplacement.getLocation());
				return true; // une action a �t� faite
			}
		}
		return false; // aucune action n'a �t� faite
	}

	private boolean setPieton(Model model) {
		// Le camion est � l'arr�t
		int nbPietonsRestant = ConstantesAgents.NB_PIETON_PAR_CAMION, dist=0;

		Bag aVoisins = null;
		do{
			dist++;
			aVoisins = getEmptyNeighbors(this.getLocation(), dist);
		}while(aVoisins.size() < ConstantesAgents.NB_PIETON_PAR_CAMION);

		Iterator itVoisin = aVoisins.iterator();
		AgentEnvironnement aCase = null;
		while(nbPietonsRestant > 0 && itVoisin.hasNext()){
			aCase = (AgentEnvironnement) itVoisin.next();
			AgentPieton pieton = new AgentPieton(ConstantesAgents.RES_PIETON, ConstantesAgents.DEP_PIETON, ConstantesAgents.FORCE_PIETON, ConstantesAgents.PERCEPTION_PIETON);
			model.incNbFiremen();
			aModel.getYard().setObjectLocation(pieton, aCase.getLocation());
			pieton.setLocation(aCase.getLocation());
			pieton.setStp(aModel.schedule.scheduleRepeating(pieton));
			nbPietonsRestant--;
		}		

		empty = true;
		
		return true;
	}

	private void eteindreFeu() {
		// recherche des feu assez proche pour �tre �teints
		Bag aBagFeu = getFeuNeighbors(aModel, ConstantesAgents.DIST_EXTINCTION_CAMION);
		for(Object i:  aBagFeu)
		{
			if(i instanceof AgentFeu)
			{
				AgentFeu aAgent = (AgentFeu)i;
				aAgent.reduceRes(aModel, getForce());
			}
		}
	}

	private Bag getEmptyNeighbors(Int2D location, int dist){
		Bag aVoisins = null;
		aVoisins = aModel.getYard().getNeighborsMaxDistance(location.getX(), location.getY(), dist, false, aVoisins, null, null);
		// �limination des agents autres que environnement
		Iterator itVoisins = aVoisins.iterator();
		Object object;
		Bag aPlaces = new Bag();
		while(itVoisins.hasNext()){
			object = itVoisins.next();
			if (object instanceof AgentEnvironnement){
				aPlaces.add(object);
			}
		}
		
		// �limination des places occup�es
		for (Object aPlace : aPlaces){
			Bag aAgents = aModel.getYard().getObjectsAtLocation(((AgentEnvironnement)aPlace).getLocation());
			for (Object i : aAgents){
				if((i instanceof AgentCamion) || (i instanceof AgentPieton) || (i instanceof AgentFeu)){
					aPlaces.remove(aPlace);
				}
			}
		}
		
		return aPlaces;
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
		// Diagonales biais�es
		// return Math.max(Math.abs(iLocation1.x - iLocation2.x), Math.abs(iLocation1.y - iLocation2.y));
		
		// Diagonales non biais�es
		return Math.abs(iLocation1.x - iLocation2.x) + Math.abs(iLocation1.y - iLocation2.y);
	}
}
