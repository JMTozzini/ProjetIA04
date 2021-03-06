package com.ia04.agents;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

import com.ia04.constantes.ConstantesAgents;
import com.ia04.main.Model;

public class AgentFeu implements Steppable {
	
	private static final long serialVersionUID = 1L;
	
	// Position
	private int x=-1;
	private int y=-1;
			
	// Attributs
	private int force;
	private int resistance;
	
	// Autres
	private Stoppable stp;
	
	
	public AgentFeu(int iForce, int iResistance) {
		super();
		this.force = iForce;
		this.resistance = iResistance;
	}


	public void step(SimState iState) {
		Model aModel = (Model) iState;
		if(resistance <= 0){
			aModel.decNbFire();
			this.getStp().stop();
			aModel.getYard().remove(this);
			
			// rÃ©tablissement de l'environnement
			Bag aAgents = aModel.getYard().getObjectsAtLocation(this.getLocation());
			for(Object aAgent: aAgents)
			{
				if(aAgent instanceof AgentEnvironnement)
				{
					AgentEnvironnement aAgentEnv = (AgentEnvironnement) aAgent;
					aAgentEnv.setInitRes();
				}
			}
		}
		
		Bag aAgents = aModel.getYard().getNeighborsMaxDistance(x, y, 1, false, null, null, null);
		for(Object i: aAgents)
		{		
			if(i instanceof AgentEnvironnement)
			{
				AgentEnvironnement aAgentEnv = (AgentEnvironnement)i;
				if(aAgentEnv.isInflammable())
				{
					// Feu et AgentEnv inflammable sur la mÃªme case
					if(aAgentEnv.getX() == this.getX() && aAgentEnv.getY() == this.getY())
					{
						aAgentEnv.reduceResInterne(this.getForce());
						if(aAgentEnv.getResInterne()<=0) // Destruction du Feu car plus rien a bruler
						{
							aModel.incNbBurnt();
							aModel.decNbFire();
							this.getStp().stop();
							aModel.getYard().removeObjectsAtLocation(this.getX(), this.getY());
						}
					}
					// AgentEnv inflammable adjacent du Feu
					else if(aAgentEnv.getX()!=this.getX() || aAgentEnv.getY()!=this.getY())
					{
						aAgentEnv.reduceResExterne(this.getForce());
						Int2D aLocation = new Int2D(aAgentEnv.getX(), aAgentEnv.getY());
						if(aAgentEnv.getResExterne()<=0 && aModel.getYard().numObjectsAtLocation(aLocation)==1) // Expansion du Feu
						{
							AgentFeu aAgentFeu = new AgentFeu(ConstantesAgents.FEU_FORCE, ConstantesAgents.FEU_RES);
							aAgentFeu.setLocation(aLocation);
							aModel.getYard().setObjectLocation(aAgentFeu, aLocation);
							aAgentFeu.setStp(aModel.schedule.scheduleRepeating(aAgentFeu));
							aModel.incNbFire();
						}
					}
				}
				else if(aAgentEnv.getType()==ConstantesAgents.TYPE_ROUTE && aModel.schedule.getSteps()%ConstantesAgents.VIT_PASSAGE_ROUTE==0)
				{
					int aSens = aAgentEnv.getSens();
					if(aSens==ConstantesAgents.NORD || aSens==ConstantesAgents.SUD)
					{
						if(aAgentEnv.getX() > this.getX() && aAgentEnv.getY() == this.getY() 
								&& aAgentEnv.getX()+1 < aModel.getYard().getWidth())
							actionFeu(aModel, aAgentEnv, aAgentEnv.getX()+1, aAgentEnv.getY());
						else if(aAgentEnv.getX() < this.getX() && aAgentEnv.getY() == this.getY()
								&& aAgentEnv.getX()-1 > 0)
							actionFeu(aModel, aAgentEnv, aAgentEnv.getX()-1, aAgentEnv.getY());
					}
					
					if(aSens==ConstantesAgents.OUEST || aSens==ConstantesAgents.EST)
					{
						if(aAgentEnv.getY() > this.getY() && aAgentEnv.getX() == this.getX() 
								&& aAgentEnv.getY()+1 < aModel.getYard().getHeight())
							actionFeu(aModel, aAgentEnv, aAgentEnv.getX(), aAgentEnv.getY()+1);
						else if(aAgentEnv.getY() < this.getY() && aAgentEnv.getX() == this.getX()
								&& aAgentEnv.getY()-1 > 0)
							actionFeu(aModel, aAgentEnv, aAgentEnv.getX(), aAgentEnv.getY()-1);
					}
				}
			}
			else if(i instanceof AgentPieton && ((AgentPompier)i).getFeu() != this){
				//Les pompiers ne peuvent se faire attaquer par le feu qu'ils sont en train d'�teindre
				((AgentPompier)i).reduceRes(force);
			}
		}
	}
	
	private void actionFeu(Model iModel, AgentEnvironnement iAgentEnv, Integer iX, Integer iY)
	{
		Int2D aLocation = new Int2D(iX, iY);
		Bag aAgents = iModel.getYard().getObjectsAtLocation(aLocation);
		if(aAgents != null && aAgents.size()==1)
		{
			AgentEnvironnement aAgentEnv = (AgentEnvironnement)aAgents.get(0);
			if(aAgentEnv.isInflammable())
			{
				aAgentEnv.reduceResExterne(this.getForce());
				if(aAgentEnv.getResExterne()<=0)
				{
					AgentFeu aAgentFeu = new AgentFeu(ConstantesAgents.FEU_FORCE, ConstantesAgents.FEU_RES);
					aAgentFeu.setLocation(aLocation);
					iModel.getYard().setObjectLocation(aAgentFeu, aLocation);
					aAgentFeu.setStp(iModel.schedule.scheduleRepeating(aAgentFeu));
					iModel.incNbFire();
				}				
			}
		}
	}


	public int getForce() {
		return force;
	}


	public void setForce(int force) {
		this.force = force;
	}


	public int getResistance() {
		return resistance;
	}


	public void setResistance(int resistance) {
		this.resistance = resistance;
	}
	
	public void reduceRes(Model iModel, int force) {
		this.resistance -= force;
		if(resistance<=0){
			for(Object agent: iModel.getYard().getObjectsAtLocation(this.getX(), this.getY())){
			//L'environnement o� le feu a �t� �teint ne peut s'enflammer � nouveau
				if(agent instanceof AgentEnvironnement)
					((AgentEnvironnement)agent).setInflammable(false);
			}
			this.getStp().stop();
			iModel.getYard().remove(this);
			iModel.decNbFire();
			iModel.incNbExtinguished();
		}
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}
	
	public void setLocation(Int2D iLocation)
	{
		this.x = iLocation.x;
		this.y = iLocation.y;
	}
	
	public Int2D getLocation()
	{
		return (new Int2D(this.x, this.y));
	}

	public Stoppable getStp() {
		return stp;
	}

	public void setStp(Stoppable stp) {
		this.stp = stp;
	}

	@Override
	public String toString() {
		return "Agent Feu [x=" + x + ", y=" + y + "]";
	}
}
