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
		Int2D aPrevLocation = new Int2D();
		AgentEnvironnement aPrevAgent = new AgentEnvironnement();
		
		for(int i=0; i < aNbRoute; i++)
		{
			AgentEnvironnement aAgentRoute = new AgentEnvironnement(ConstantesAgents.TYPE_ROUTE, 0);
			if(i==0)
			{
				Int2D aLocation = new Int2D(random.nextInt(yard.getWidth()), 0);
				aAgentRoute.setSens(ConstantesAgents.SUD);
				yard.setObjectLocation(aAgentRoute, aLocation);
				aAgentRoute.setLocation(aLocation);
				aPrevAgent = aAgentRoute;
			}
			else if (i < yard.getHeight())
			{
				if(i%2 == 0) // Tout droit
				{
					Int2D aLocation = new Int2D();
					
					if(aPrevAgent.getSens() == ConstantesAgents.SUD)
						aLocation = new Int2D(aPrevAgent.getX(), aPrevAgent.getY()+1);
					else if (aPrevAgent.getSens() == ConstantesAgents.NORD)
						aLocation = new Int2D(aPrevAgent.getX(), aPrevAgent.getY()-1);
					else if (aPrevAgent.getSens() == ConstantesAgents.EST)
						aLocation = new Int2D(aPrevAgent.getX()+1, aPrevAgent.getY());
					else	
						aLocation = new Int2D(aPrevAgent.getX()-1, aPrevAgent.getY());
					
					yard.setObjectLocation(aAgentRoute, aLocation);
					aAgentRoute.setLocation(aLocation);
					aPrevAgent = aAgentRoute;
				}
				else // Possibilité de tourner
				{
					
				}
			}
		}
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
