package com.ia04.agents;

import com.ia04.main.Model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

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


	@Override
	public void step(SimState iState) {
		Model aModel = (Model) iState;
		Bag aAgents = aModel.getYard().getNeighborsMaxDistance(x, y, 1, false, null, null, null);
		for(Object i: aAgents)
		{
			if(i != this)
				System.out.println(i.toString());
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
