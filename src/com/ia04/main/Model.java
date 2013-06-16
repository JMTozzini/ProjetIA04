package com.ia04.main;

import java.beans.PropertyChangeSupport;
import java.util.Iterator;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;

import com.ia04.agents.AgentCamion;
import com.ia04.agents.AgentCanadair;
import com.ia04.agents.AgentEnvironnement;
import com.ia04.agents.AgentFeu;
import com.ia04.constantes.ConstantesAgents;
import com.ia04.constantes.ConstantesEnv;
import com.ia04.constantes.ConstantesGenerales;

public class Model extends SimState {

	private static final long serialVersionUID = 1L;

	private SparseGrid2D yard;
	private int nbBurnt=0, nbFire=0, nbExtinguished=0, nbFiremen=0;
	PropertyChangeSupport pcs= new PropertyChangeSupport(this);

	public Model(long iSeed) {
		super(iSeed);
		yard = new SparseGrid2D(ConstantesGenerales.GRID_SIZE, ConstantesGenerales.GRID_SIZE);
		pcs = new PropertyChangeSupport(this);
	}

	public void start()
	{
		super.start();
		yard.clear();
		setMonithoringCst();
		setEnvironnement();
		setAgents();
	}

	private void setMonithoringCst() {
		nbBurnt=0;
		nbFire=0; 
		nbExtinguished=0;
		nbFiremen=0;
	}

	private void setEnvironnement() // Set les agents environnement uniquement
	{
		setVegetationFaible();
		setEau();
		setRoche();
		setVegetationMoyenne();
		setVegetationForte();
		setRoute();
		setHabitation();
		nettoyageEnvironnement();
		ajoutSchedule();
	}

	private void setVegetationFaible()
	{
		for(int i=0; i<ConstantesGenerales.GRID_SIZE; i++)
		{
			for(int j=0; j<ConstantesGenerales.GRID_SIZE; j++)
			{
				AgentEnvironnement aAgentVegetation = new AgentEnvironnement(ConstantesAgents.TYPE_VEG_FAIBLE, 0);
				Int2D aLocation = new Int2D(i, j);
				yard.setObjectLocation(aAgentVegetation, aLocation);
				aAgentVegetation.setLocation(aLocation);
			}
		}
	}

	private void setEau()
	{
		int aNbEau = (int) Math.round(Math.pow(ConstantesGenerales.GRID_SIZE,2)*ConstantesEnv.PROP_EAU);
		AgentEnvironnement aPrevAgent = null;

		for(int i=0; i < aNbEau; i++)
		{
			AgentEnvironnement aAgentEau = new AgentEnvironnement(ConstantesAgents.TYPE_EAU, 0);
			if(i==0)
			{
				Int2D aLocation = new Int2D(random.nextInt(yard.getWidth()), 1);
				aAgentEau.setSens(ConstantesAgents.SUD);
				yard.setObjectLocation(aAgentEau, aLocation);
				aAgentEau.setLocation(aLocation);
				aPrevAgent = new AgentEnvironnement(aAgentEau);
			}
			else
			{
				int aRandomCste = 50;
				int aNewSens = random.nextInt(aRandomCste); // Proba de tourner environ 2/aRandomCste
				Int2D aLocationStep1 = getNewDirection(aPrevAgent, aAgentEau, aNewSens);
				Int2D aLocationFinal = new Int2D(yard.stx(aLocationStep1.x),yard.sty(aLocationStep1.y));
				yard.setObjectLocation(aAgentEau, aLocationFinal);
				aAgentEau.setLocation(aLocationFinal);
				aPrevAgent = aAgentEau;
			}
		}
	}

	private void setRoche()
	{
		int aNbRoche = (int) Math.round(Math.pow(ConstantesGenerales.GRID_SIZE,2)*ConstantesEnv.PROP_ROCHE);

		for(int i=0; i < aNbRoche; i++)
		{
			AgentEnvironnement aAgentRoche = new AgentEnvironnement(ConstantesAgents.TYPE_ROCHE, 0);
			Int2D aLocation = new Int2D(random.nextInt(yard.getWidth()), random.nextInt(yard.getHeight()));
			yard.setObjectLocation(aAgentRoche, aLocation);
			aAgentRoche.setLocation(aLocation);
		}
	}

	private void setVegetationMoyenne()
	{
		int aNbVegMoy = (int) Math.round(Math.pow(ConstantesGenerales.GRID_SIZE,2)*ConstantesEnv.PROP_VEG_MOY);

		for(int i=0; i < aNbVegMoy; i++)
		{
			AgentEnvironnement aAgentVegMoy = new AgentEnvironnement(ConstantesAgents.TYPE_VEG_MOY, 0);
			Int2D aLocation = new Int2D(random.nextInt(yard.getWidth()), random.nextInt(yard.getHeight()));
			yard.setObjectLocation(aAgentVegMoy, aLocation);
			aAgentVegMoy.setLocation(aLocation);
		}
	}

	private void setVegetationForte()
	{
		int aNbVegForte = (int) Math.round(Math.pow(ConstantesGenerales.GRID_SIZE,2)*ConstantesEnv.PROP_VEG_FORTE);

		for(int i=0; i < aNbVegForte; i++)
		{
			AgentEnvironnement aAgentVegForte = new AgentEnvironnement(ConstantesAgents.TYPE_VEG_FORTE, 0);
			Int2D aLocation = new Int2D(random.nextInt(yard.getWidth()), random.nextInt(yard.getHeight()));
			yard.setObjectLocation(aAgentVegForte, aLocation);
			aAgentVegForte.setLocation(aLocation);
		}
	}

	private void setRoute()
	{
		int aNbRoute = (int) Math.round(Math.pow(ConstantesGenerales.GRID_SIZE,2)*ConstantesEnv.PROP_ROUTE);
		AgentEnvironnement aPrevAgent = null;

		for(int i=0; i < aNbRoute; i++)
		{
			AgentEnvironnement aAgentRoute = new AgentEnvironnement(ConstantesAgents.TYPE_ROUTE, 0);
			if(i==0)
			{
				Int2D aLocation = new Int2D(random.nextInt(yard.getWidth()), 0);
				aAgentRoute.setSens(ConstantesAgents.SUD);
				yard.setObjectLocation(aAgentRoute, aLocation);
				aAgentRoute.setLocation(aLocation);
				aPrevAgent = new AgentEnvironnement(aAgentRoute);
			}
			else
			{
				int aRandomCste = 100;
				int aNewSens = random.nextInt(aRandomCste); // Proba de tourner : 2/aRandomCste
				Int2D aLocationStep1 = getNewDirection(aPrevAgent, aAgentRoute, aNewSens);
				Int2D aLocationFinal = new Int2D(yard.stx(aLocationStep1.x), yard.sty(aLocationStep1.y));
				yard.setObjectLocation(aAgentRoute, aLocationFinal);
				yard.setObjectLocation(aAgentRoute, aLocationFinal);
				aAgentRoute.setLocation(aLocationFinal);
				aPrevAgent = aAgentRoute;
			}
		}
	}

	private void setHabitation()
	{
		int aNbHab = (int) Math.round(Math.pow(ConstantesGenerales.GRID_SIZE,2)*ConstantesEnv.PROP_HABITATION);

		for(int i=0; i < aNbHab; i++)
		{
			AgentEnvironnement aAgentHab = new AgentEnvironnement(ConstantesAgents.TYPE_HABITATION, 0);
			Int2D aLocation = new Int2D(random.nextInt(yard.getWidth()), random.nextInt(yard.getHeight()));
			yard.setObjectLocation(aAgentHab, aLocation);
			aAgentHab.setLocation(aLocation);
		}
	}

	private void nettoyageEnvironnement()
	{
		for(int i=0; i<ConstantesGenerales.GRID_SIZE;i++)
		{
			for(int j=0; j<ConstantesGenerales.GRID_SIZE;j++)
			{
				if(yard.numObjectsAtLocation(i, j) > 1) // Conflit entre objets
				{
					Int2D aLocation = new Int2D(i, j);
					Bag aAgentsEnv = yard.getObjectsAtLocation(aLocation);
					AgentEnvironnement aAgentEnv = (AgentEnvironnement) aAgentsEnv.get(0);
					for(Object aAgent : aAgentsEnv) // Selection de l'agent restant
					{
						AgentEnvironnement aAgentTmp = (AgentEnvironnement) aAgent;
						if(aAgentEnv.getType() < aAgentTmp.getType())
							aAgentEnv = aAgentTmp;
					}
					yard.removeObjectsAtLocation(aLocation.x, aLocation.y);
					yard.setObjectLocation(aAgentEnv, aLocation);
				}
			}
		}
	}

	private void ajoutSchedule()
	{
		for(int i=0; i<ConstantesGenerales.GRID_SIZE;i++)
		{
			for(int j=0; j<ConstantesGenerales.GRID_SIZE;j++)
			{
				Int2D aLocation = new Int2D(i, j);
				Bag aAgentsEnv = yard.getObjectsAtLocation(aLocation);
				AgentEnvironnement aAgentEnv = (AgentEnvironnement) aAgentsEnv.get(0);
				if(aAgentEnv.isInflammable())
				{
					Stoppable aStop = schedule.scheduleRepeating(aAgentEnv);
					aAgentEnv.setStp(aStop);						
				}
			}
		}
	}

	private Int2D getNewDirection(AgentEnvironnement iPrevAgent, AgentEnvironnement iCurAgent, int iNewSens)
	{
		Int2D oLocation = null;

		boolean aSud = (iPrevAgent.getSens() == ConstantesAgents.SUD && iNewSens > 1) || 
				(iNewSens == 0 && (iPrevAgent.getSens() == ConstantesAgents.EST || iPrevAgent.getSens() == ConstantesAgents.OUEST));

		boolean aNord = (iPrevAgent.getSens() == ConstantesAgents.NORD && iNewSens > 1) ||
				(iNewSens == 1 && (iPrevAgent.getSens() == ConstantesAgents.EST || iPrevAgent.getSens() == ConstantesAgents.OUEST));

		boolean aOuest = (iPrevAgent.getSens() == ConstantesAgents.OUEST && iNewSens > 1) ||
				(iNewSens == 0 && (iPrevAgent.getSens() == ConstantesAgents.NORD || iPrevAgent.getSens() == ConstantesAgents.SUD));

		boolean aEst = (iPrevAgent.getSens() == ConstantesAgents.EST && iNewSens > 1) ||
				(iNewSens == 1 && (iPrevAgent.getSens() == ConstantesAgents.NORD || iPrevAgent.getSens() == ConstantesAgents.SUD));

		if(aSud) // SUD
		{
			oLocation = new Int2D(iPrevAgent.getX(), iPrevAgent.getY()+1);
			iCurAgent.setSens(ConstantesAgents.SUD);
		}
		else if (aNord) // NORD
		{
			oLocation = new Int2D(iPrevAgent.getX(), iPrevAgent.getY()-1);
			iCurAgent.setSens(ConstantesAgents.NORD);			
		}
		else if (aEst) //EST
		{
			oLocation = new Int2D(iPrevAgent.getX()+1, iPrevAgent.getY());
			iCurAgent.setSens(ConstantesAgents.EST);
		}
		else if(aOuest) // OUEST
		{
			oLocation = new Int2D(iPrevAgent.getX()-1, iPrevAgent.getY());
			iCurAgent.setSens(ConstantesAgents.OUEST);

		}
		else
			System.out.println("Erreur : getNewDirection");

		return oLocation;
	}

	private void setAgents() // Set les agents autres que environnement
	{
		setFeu();
		setPompier();
	}

	private void setFeu()
	{
		boolean aValide = false;
		AgentFeu aAgentFeu = new AgentFeu(ConstantesAgents.FEU_FORCE, ConstantesAgents.FEU_RES);
		do
		{
			Int2D aLocation = new Int2D(random.nextInt(yard.getWidth()), random.nextInt(yard.getWidth()));
			Bag aAgents = yard.getObjectsAtLocation(aLocation);
			AgentEnvironnement aAgentEnv = (AgentEnvironnement) aAgents.get(0); // unique 
			if(aAgentEnv.isInflammable())
			{
				yard.setObjectLocation(aAgentFeu, aLocation);
				aAgentFeu.setLocation(aLocation);
				aValide = true;
			}			
		}while(!aValide);
		aAgentFeu.setStp(schedule.scheduleRepeating(aAgentFeu));
	}

	private void setPompier(){
		// setPieton(); // fait par l'IA des camions
		setCamion();
		setCanadair();
	}

	private void setCamion(){
		// les camions sont placé à partir d'une route prise au hasard
		Bag aAllAgents = yard.getAllObjects();
		// élimination des agents autres que routes
		Bag aRoutes = new Bag();
		Iterator itAllAgents = aAllAgents.iterator();
		Object object;
		while(itAllAgents.hasNext()){
			object = itAllAgents.next();
			if (object instanceof AgentEnvironnement){
				if (((AgentEnvironnement)object).getType() == ConstantesAgents.TYPE_ROUTE){
					aRoutes.add(object);
				}
			}
		}
		
		if(aRoutes.numObjs != 0){
			AgentEnvironnement caserne = (AgentEnvironnement)aRoutes.get(random.nextInt(aRoutes.numObjs));

			// Placement des camions
			int distance = 0;
			Bag routesCaserne;
			do {
				distance++;
				routesCaserne = getNeighborsByType(caserne.getLocation(), distance, ConstantesAgents.TYPE_ROUTE);
			} while(ConstantesAgents.NB_CAMION > routesCaserne.numObjs);

			AgentEnvironnement route = null;
			int nbCamion = ConstantesAgents.NB_CAMION;
			Iterator itRoutesCaserne = routesCaserne.iterator();
			while(nbCamion > 0 && itRoutesCaserne.hasNext()){
				route = (AgentEnvironnement)itRoutesCaserne.next();
				AgentCamion aAgentCamion = new AgentCamion(ConstantesAgents.RES_CAMION, ConstantesAgents.DEP_CAMION,  ConstantesAgents.FORCE_CAMION, ConstantesAgents.PERCEPTION_CANADAIR);
				yard.setObjectLocation(aAgentCamion, route.getLocation());
				aAgentCamion.setLocation(route.getLocation());
				aAgentCamion.setStp(schedule.scheduleRepeating(aAgentCamion));
				nbCamion--;
			}
		}
	}
	
	private void setCamionAvecCaserne(){
		// Les camions viennent tous de la mï¿½me caserne

		// L'emplacement de la caserne est dï¿½terminï¿½ au hasard parmi les habitations au bord d'une route
		// Int2D aLocation = null;
		Bag aAgents = yard.getAllObjects();
		Iterator it = aAgents.iterator();
		Object object = null;
		Bag aCaserne = new Bag();
		while(it.hasNext()){
			object = it.next();
			if (object instanceof AgentEnvironnement){
				AgentEnvironnement envAgent = (AgentEnvironnement)object;
				if (envAgent.getType() == ConstantesAgents.TYPE_HABITATION &&
						getNeighborsByType(envAgent.getLocation(), 1, ConstantesAgents.TYPE_ROUTE).numObjs > 0){
					aCaserne.add(envAgent);
				}
			}
		}

		if(aCaserne.numObjs != 0){
			AgentEnvironnement caserne = (AgentEnvironnement)aCaserne.get(random.nextInt(aCaserne.numObjs));
			// TODO caserne portrayal

			// Placement des camions
			int distance = 0;
			Bag routesCaserne;
			do {
				distance++;
				routesCaserne = getNeighborsByType(caserne.getLocation(), distance, ConstantesAgents.TYPE_ROUTE);
			} while(ConstantesAgents.NB_CAMION > routesCaserne.numObjs);

			AgentEnvironnement route = null;
			int nbCamion = ConstantesAgents.NB_CAMION;
			Iterator itRoutesCaserne = routesCaserne.iterator();
			while(nbCamion > 0 && itRoutesCaserne.hasNext()){
				route = (AgentEnvironnement)itRoutesCaserne.next();
				AgentCamion aAgentCamion = new AgentCamion(ConstantesAgents.RES_CAMION, ConstantesAgents.DEP_CAMION,  ConstantesAgents.FORCE_CAMION, ConstantesAgents.PERCEPTION_CANADAIR);
				yard.setObjectLocation(aAgentCamion, route.getLocation());
				aAgentCamion.setLocation(route.getLocation());
				aAgentCamion.setStp(schedule.scheduleRepeating(aAgentCamion));
				nbCamion--;
			}
		} else {
			setCamion();
		}
	}

	public Bag getNeighborsByType(Int2D location, int dist, int type){
		Bag aVoisins = null;
		aVoisins = yard.getNeighborsMaxDistance(
				location.getX(),
				location.getY(),
				dist,
				false,
				aVoisins,
				null,
				null
				);
		Iterator itVoisin = aVoisins.iterator();
		Object object;
		Bag aVoisinsType = new Bag();
		while(itVoisin.hasNext()){
			object = itVoisin.next();
			if (object instanceof AgentEnvironnement){
				AgentEnvironnement voisinAgent = (AgentEnvironnement)object;
				if (voisinAgent.getType() == type){
					aVoisinsType.add(voisinAgent);
				}
			}
		}
		return aVoisinsType;
	}

	private void setCanadair(){
		for (int i = 0; i < ConstantesAgents.NB_CANDAIR; i++)
		{
			AgentCanadair aAgentCan = new AgentCanadair(
					ConstantesAgents.RES_CANADAIR, 
					ConstantesAgents.DEP_CANADAIR, 
					ConstantesAgents.FORCE_CANADAIR,
					ConstantesAgents.PERCEPTION_CANADAIR);
			Int2D aLocation = new Int2D(-1, random.nextInt(yard.getHeight()));
			yard.setObjectLocation(aAgentCan, aLocation);
			aAgentCan.setStp(schedule.scheduleRepeating(aAgentCan));
		}
	}

	public SparseGrid2D getYard() {
		return yard;
	}


	public PropertyChangeSupport getPcs() {
		return pcs;
	}

	public void incNbBurnt(){
		nbBurnt++;
	}


	public void incNbFire(){
		nbFire++;
	}

	public void decNbFire(){
		nbFire--;
	}
	
	public void incNbExtinguished(){
		nbExtinguished++;
	}

	public void incNbFiremen(){
		nbFiremen++;
	}
	
	public void decNbFiremen(){
		nbFiremen--;
	}

	public int getNbBurnt(){
		return nbBurnt;
	}

	public int getNbFire(){
		return nbFire;
	}
	
	public int getNbExtinguished(){
		return nbExtinguished;
	}

	public int getNbFiremen(){
		return nbFiremen;
	}
}
