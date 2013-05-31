package com.ia04.main;

import com.ia04.agents.AgentEnvironnement;
import com.ia04.constantes.ConstantesAgents;
import com.ia04.constantes.ConstantesEnv;
import com.ia04.constantes.ConstantesGenerales;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class Model extends SimState {

	private static final long serialVersionUID = 1L;

	private SparseGrid2D yard;

	public Model(long iSeed) {
		super(iSeed);
		yard = new SparseGrid2D(ConstantesGenerales.GRID_SIZE, ConstantesGenerales.GRID_SIZE);
	}

	public void start()
	{
		super.start();
		yard.clear();
		setEnvironnement();
		setAgents();
	}

	public void setEnvironnement() // Set les agents environnement uniquement
	{
		setRoute();
//		setHabitation();
//		setEau();
//		setRoche();
//		setVegetationFaible();
//		setVegetationMoyenne();
//		setVegetationForte();
	}

	public void setRoute()
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
				int aNewSens = random.nextInt(aRandomCste); // Proba de tourner environ 2/aRandomCste
				Int2D aLocationStep1 = getNewDirection(aPrevAgent, aAgentRoute, aNewSens);
//				aLocation = correctionTrajectoire(aLocation, aPrevAgent, aAgentRoute);
				Int2D aLocationFinal = new Int2D(yard.stx(aLocationStep1.x), yard.sty(aLocationStep1.y));
				yard.setObjectLocation(aAgentRoute, aLocationFinal);
				yard.setObjectLocation(aAgentRoute, aLocationFinal);
				aAgentRoute.setLocation(aLocationFinal);
				aPrevAgent = aAgentRoute;
			}
		}
	}
	
	public void setEau()
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
				int aRandomCste = 100;
				int aNewSens = random.nextInt(aRandomCste); // Proba de tourner environ 2/aRandomCste
				Int2D aLocationStep1 = getNewDirection(aPrevAgent, aAgentEau, aNewSens);
//				aLocation = correctionTrajectoire(aLocation, aPrevAgent, aAgentEau);
				Int2D aLocationFinal = new Int2D(yard.stx(aLocationStep1.x),yard.sty(aLocationStep1.y));
				yard.setObjectLocation(aAgentEau, aLocationFinal);
				aAgentEau.setLocation(aLocationFinal);
				aPrevAgent = aAgentEau;
			}
		}
	}

//	public void setEau()
//	{
//		int aNbEau = (int) Math.round(Math.pow(ConstantesGenerales.GRID_SIZE,2)*ConstantesEnv.PROP_EAU);
//		int sens = ConstantesAgents.NORD;
//		int steps = 1;
//		AgentEnvironnement aPrevAgent = null;
//
//		for(int i=0; i<aNbEau;i++)
//		{
//			if(i==0)
//			{
//				AgentEnvironnement aAgentEau = new AgentEnvironnement(ConstantesAgents.TYPE_EAU, 0);
//				Int2D aLocation = new Int2D(random.nextInt(yard.getWidth()), random.nextInt(yard.getHeight()));
//				aAgentEau.setSens(ConstantesAgents.NORD);		
//				yard.setObjectLocation(aAgentEau, aLocation);	
//				aAgentEau.setLocation(aLocation);
//				aPrevAgent = new AgentEnvironnement(aAgentEau);
//			}
//			else
//			{
//				for(int j = 0;j<steps;j++){
//					AgentEnvironnement aAgentEau = new AgentEnvironnement(ConstantesAgents.TYPE_EAU, 0);
//					Int2D aLocation = getNewDirection(aPrevAgent, aAgentEau, sens);	
//					yard.setObjectLocation(aAgentEau, aLocation);
//					aAgentEau.setLocation(aLocation);
//					aPrevAgent = aAgentEau;
//
//				}
//			}
//			sens++;
//			if(sens == 5){
//				sens = 1; 
//			}
//			if(i%2 == 0){
//				steps++;
//			}
//		}
//
//	}
	
//	private Int2D correctionTrajectoire(Int2D ioLocation, AgentEnvironnement iPrevAgent, AgentEnvironnement iCurrentAgent)
//	{
//		int aNewSens = random.nextInt(2);
//		int aCst = 1; // Bordure
//		
//		if(ioLocation.x >= yard.getWidth()+aCst || ioLocation.x <= aCst)
//		{
//			switch (aNewSens) 
//			{
//			case 0:
//				iCurrentAgent.setSens(ConstantesAgents.SUD);
//				ioLocation = new Int2D(iPrevAgent.getX(), iPrevAgent.getY()+1);
//				break;
//			case 1:
//				iCurrentAgent.setSens(ConstantesAgents.NORD);
//				ioLocation = new Int2D(iPrevAgent.getX(), iPrevAgent.getY()-1);
//				break;
//			default:
//				break;
//			}
//		}
//		else if(ioLocation.y >= yard.getHeight()+aCst || ioLocation.y <= aCst)
//		{
//			switch (aNewSens)
//			{
//			case 0:
//				iCurrentAgent.setSens(ConstantesAgents.EST);
//				ioLocation = new Int2D(iPrevAgent.getX()+1, iPrevAgent.getY());
//				break;
//			case 1:
//				iCurrentAgent.setSens(ConstantesAgents.OUEST);
//				ioLocation = new Int2D(iPrevAgent.getX()-1, iPrevAgent.getY());
//				break;
//			default:
//				break;
//			}
//		}
//		return ioLocation;
//	}
	
	private Int2D getNewDirection(AgentEnvironnement iPrevAgent, AgentEnvironnement iCurAgent, int iNewSens)
	{		
//		System.out.println(iNewSens);
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
//			System.out.println("sud");
			oLocation = new Int2D(iPrevAgent.getX(), iPrevAgent.getY()+1);
			iCurAgent.setSens(ConstantesAgents.SUD);
		}
		else if (aNord) // NORD
		{
//			System.out.println("NORD");
			oLocation = new Int2D(iPrevAgent.getX(), iPrevAgent.getY()-1);
			iCurAgent.setSens(ConstantesAgents.NORD);			
		}
		else if (aEst) //EST
		{
//			System.out.println("EST");
			oLocation = new Int2D(iPrevAgent.getX()+1, iPrevAgent.getY());
			iCurAgent.setSens(ConstantesAgents.EST);
		}
		else if(aOuest) // OUEST
		{
//			System.out.println("OUEST");
			oLocation = new Int2D(iPrevAgent.getX()-1, iPrevAgent.getY());
			iCurAgent.setSens(ConstantesAgents.OUEST);

		}
		else
			System.out.println("Erreur : getNewDirection");

		return oLocation;
	}

	public void setAgents() // Set les agents autres que environnement
	{

	}

	public SparseGrid2D getYard() {
		return yard;
	}

	//	public void setYard(SparseGrid2D yard) {
	//		this.yard = yard;
	//	}
}
