package com.ia04.agents;

import com.ia04.main.Model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

@SuppressWarnings("serial")
public abstract class AgentPompier implements Steppable {

	// Position
	private int x=-1;
	private int y=-1;
	
	// Attributs
	private int resistance;
	private int deplacement;
	private int force;
	
	// Autres
	private Stoppable stp;
	
	public AgentPompier(int iResistance, int iDeplacement, int iForce){
		resistance = iResistance;
		deplacement = iDeplacement;
		force = iForce;
	}
	
	public void step(SimState iModel){
	}
	
	protected void survivre(){
		// TODO
	}
	
	protected boolean RapprochementFeu(AgentFeu iAgentFeu, Model iModel, Integer iDist)
	{
		// induire un random 
		if(iDist<this.getDeplacement()) // téléportation
		{
			iModel.getYard().setObjectLocation(this, iAgentFeu.getX(), iAgentFeu.getY());
			this.setX(iAgentFeu.getX());
			this.setY(iAgentFeu.getY());
			return true;
		}
		else
		{
			Integer aDeplacementRestant = this.getDeplacement();
			Integer aDeltaX = this.getX() - iAgentFeu.getX();
			Integer aDeltaY = this.getY() - iAgentFeu.getY();
			
			int aX = this.getX();
			int aY = this.getY();
			
			while(aDeplacementRestant > 0)
			{
				if(aDeltaX>0)
				{
					aDeltaX--;
					this.setX(this.getX()-1);
					aDeplacementRestant--;
				}
				else if(aDeltaX<0)
				{
					aDeltaX++;
					this.setX(this.getX()+1);
					aDeplacementRestant--;
				}
				else if(aDeltaY>0)
				{
					aDeltaY--;
					this.setY(this.getY()-1);
					aDeplacementRestant--;
				}
				else if(aDeltaY<0)
				{
					aDeltaY++;
					this.setY(this.getY()+1);
					aDeplacementRestant--;
				}
			}
			iModel.getYard().setObjectLocation(this, this.getX(), this.getY());
			
			Bag aAgents = iModel.getYard().getObjectsAtLocation(aX, aY);
			if(aAgents!=null)
			{
				iModel.getYard().removeObjectsAtLocation(aX, aY);
				for(Object i:aAgents)
				{
					if(!(i instanceof AgentCanadair))
					{
						if(i instanceof AgentPompier)
						{
							AgentPompier aAgent = (AgentPompier) i;
							iModel.getYard().setObjectLocation(i, aAgent.getLocation());
						}
						else if(i instanceof AgentEnvironnement)
						{
							AgentEnvironnement aAgent = (AgentEnvironnement) i;
							iModel.getYard().setObjectLocation(i, aAgent.getLocation());
						}
						else if(i instanceof AgentFeu)
						{
							AgentFeu aAgent = (AgentFeu) i;
							iModel.getYard().setObjectLocation(i, aAgent.getLocation());	
						}
					}
				}
			}
		}
		return false;
	}

	public int getResistance() {
		return resistance;
	}

	public void setResistance(int resistance) {
		this.resistance = resistance;
	}
	
	public int getDeplacement() {
		return deplacement;
	}

	public void setDeplacement(int deplacement) {
		this.deplacement = deplacement;
	}
	
	public int getForce() {
		return force;
	}

	public void setForce(int force) {
		this.force = force;
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
		return "Agent Pompier [x=" + x + ", y=" + y + "]";
	}
}
