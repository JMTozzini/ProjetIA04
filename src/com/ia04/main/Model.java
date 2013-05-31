package com.ia04.main;

import com.ia04.agents.AgentEnvironnement;
import com.ia04.agents.AgentFeu;
import com.ia04.constantes.ConstantesAgents;
import com.ia04.constantes.ConstantesEnv;
import com.ia04.constantes.ConstantesGenerales;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
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
//						System.out.println(aAgent.toString());
						AgentEnvironnement aAgentTmp = (AgentEnvironnement) aAgent;
						if(aAgentEnv.getType() < aAgentTmp.getType())
							aAgentEnv = aAgentTmp;
					}
					yard.removeObjectsAtLocation(aLocation);
					yard.setObjectLocation(aAgentEnv, aLocation);
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
	}
	
	private void setFeu()
	{
		boolean aValide = false;
		AgentFeu aAgentFeu = new AgentFeu(ConstantesEnv.FEU_FORCE, ConstantesEnv.FEU_RES);
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
		schedule.scheduleOnce(aAgentFeu);
//		aAgentFeu.setStp(schedule.scheduleRepeating(aAgentFeu));
	}

	public SparseGrid2D getYard() {
		return yard;
	}
}
