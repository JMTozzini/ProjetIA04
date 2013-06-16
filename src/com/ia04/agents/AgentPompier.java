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
	protected int x=-1;
	protected int y=-1;
	
	// Attributs
	protected int resistance;
	protected int deplacement;
	protected int force;
	protected int perception;

	// Autres
	protected Stoppable stp;
	protected AgentFeu feu; //R�f�rence le feu en train d'�tre �teint par cet agent
	
	public AgentFeu getFeu() {
		return feu;
	}

	public void setFeu(AgentFeu feu) {
		this.feu = feu;
	}

	public AgentPompier(int iResistance, int iDeplacement, int iForce, int iPerception){
		resistance = iResistance;
		deplacement = iDeplacement;
		force = iForce;
		perception = iPerception;
	}
	
	public boolean vivant(){
		return resistance>0;
	}
	
	public void step(SimState iModel){
	}
	
	protected void survivre(){
		// TODO
	}
	
	protected boolean RapprochementFeu(AgentFeu iAgentFeu, Model iModel, Integer iDist)
	{
		if(iAgentFeu == null)
			return true;
		// induire un random 
		iModel.getYard().remove(this);
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
			
			while(aDeplacementRestant > 0)
			{
				int aRandom = iModel.random.nextInt(2);
				
				if(aDeltaX>0 && aRandom==0)
				{
					aDeltaX--;
					this.setX(this.getX()-1);
					aDeplacementRestant--;
				}
				else if(aDeltaX<0 && aRandom==0)
				{
					aDeltaX++;
					this.setX(this.getX()+1);
					aDeplacementRestant--;
				}
				else if(aDeltaY>0 && aRandom==1)
				{
					aDeltaY--;
					this.setY(this.getY()-1);
					aDeplacementRestant--;
				}
				else if(aDeltaY<0 && aRandom==1)
				{
					aDeltaY++;
					this.setY(this.getY()+1);
					aDeplacementRestant--;
				}
			}
			iModel.getYard().setObjectLocation(this, this.getX(), this.getY());
		}
		return false;
	}

	protected AgentFeu checkAgentFeu(Bag iAgents)
	{
		for(Object i : iAgents)
		{
			if(i instanceof AgentFeu)
				return (AgentFeu)i;
		}
		return null;
	}
	
	protected Bag getFeuNeighbors(Model iModel, int distance){
		Bag aVoisins = null;
		aVoisins = iModel.getYard().getNeighborsMaxDistance(
				getX(),
				getY(),
				distance,
				false,
				aVoisins,
				null,
				null
			);
		Bag aBagFeu = new Bag();
		for (Object aVoisin : aVoisins){
			if(aVoisin instanceof AgentFeu){
				aBagFeu.add((AgentFeu)aVoisin);
			}
		}
		return aBagFeu;
	}

	public int getResistance() {
		return resistance;
	}

	public void setResistance(int resistance) {
		this.resistance = resistance;
	}
	
	public void reduceRes(int force){
		this.resistance -= force;
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
	public int getPerception() {
		return perception;
	}

	public void setPerception(int perception) {
		this.perception = perception;
	}
}
