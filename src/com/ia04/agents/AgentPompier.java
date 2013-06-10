package com.ia04.agents;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
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
	
	public AgentPompier(int iResistance, int iDeplacement, int iForce, int iPerception){
		resistance = iResistance;
		deplacement = iDeplacement;
		force = iForce;
		perception = iPerception;
	}
	
	public void step(SimState iModel){
	}
	
	protected void survivre(){
		// TODO
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
	public int getPerception() {
		return perception;
	}

	public void setPerception(int perception) {
		this.perception = perception;
	}
}
