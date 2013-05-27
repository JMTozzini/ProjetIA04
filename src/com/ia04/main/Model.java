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
			else //if (i < yard.getHeight())
			{
				if(i%2 == 0) // Tout droit
				{
					Int2D aLocation = null;
					
					if(aPrevAgent.getSens() == ConstantesAgents.SUD)
						aLocation = new Int2D(aPrevAgent.getX(), aPrevAgent.getY()+1);
					else if (aPrevAgent.getSens() == ConstantesAgents.NORD)
						aLocation = new Int2D(aPrevAgent.getX(), aPrevAgent.getY()-1);
					else if (aPrevAgent.getSens() == ConstantesAgents.EST)
						aLocation = new Int2D(aPrevAgent.getX()+1, aPrevAgent.getY());
					else if (aPrevAgent.getSens() == ConstantesAgents.OUEST)
						aLocation = new Int2D(aPrevAgent.getX()-1, aPrevAgent.getY());
					else
						System.out.println("Erreur : Tout droit");
					
					aAgentRoute.setSens(aPrevAgent.getSens());					
					yard.setObjectLocation(aAgentRoute, aLocation);
					aAgentRoute.setLocation(aLocation);
					aPrevAgent = aAgentRoute;
					
				}
				else // Possibilité de tourner
				{
					int aRandomCste = 40;
					int aNewSens = random.nextInt(aRandomCste); // Proba de tourner 2/aRandomCste
					Int2D aLocation = getNewDirection(aPrevAgent, aAgentRoute, aNewSens);
										
					if(aLocation == null)
						System.out.println(aPrevAgent.getSens() + " " + aNewSens);
					
					yard.setObjectLocation(aAgentRoute, aLocation);
					aAgentRoute.setLocation(aLocation);
					aPrevAgent = aAgentRoute;
				}
			}
			System.out.println("Previous : " + aPrevAgent);
			System.out.println("Current : " + aAgentRoute);
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
