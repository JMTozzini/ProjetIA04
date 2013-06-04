package com.ia04.agents;

import com.ia04.constantes.ConstantesAgents;
import com.ia04.main.Model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Int2D;
import sim.util.Valuable;

public class AgentEnvironnement implements Steppable, Valuable {

	private static final long serialVersionUID = 1L;
	
	// Position
	private int x=-1;
	private int y=-1;

	// Autres
	private Stoppable stp;
	
	// Attributs
	private int type;
	private boolean inflammable;
	private int resInterne;
	private int resExterne;
	private int altitude;
	private int sens; // Uniquement pour Eau et Route
		
	public AgentEnvironnement(AgentEnvironnement iAgent)
	{
		super();
		this.type = iAgent.getType();
		this.altitude = iAgent.getAltitude();
		this.inflammable = iAgent.isInflammable();
		this.resExterne = iAgent.getResExterne();
		this.resInterne = iAgent.getResInterne();
		this.sens = iAgent.getSens() ;
		this.stp = iAgent.getStp();
		this.x = iAgent.getX();
		this.y = iAgent.getY();
	}
	
	public AgentEnvironnement(int iType, int iAltitude) {
		super();
		this.type = iType;
		this.altitude = iAltitude;
		this.inflammable = true;
		this.sens = -1 ;
		this.stp = null;
		
		switch (type) {
		case 1: // TYPE_VEG_FAIBLE
			resInterne = ConstantesAgents.RES_INT_VEG_FAIBLE;
			resExterne = ConstantesAgents.RES_EXT_VEG_FAIBLE;
			break;
		case 3: // TYPE_VEG_MOY
			resInterne = ConstantesAgents.RES_INT_VEG_MOY;
			resExterne = ConstantesAgents.RES_EXT_VEG_MOY;
			break;
		case 4: // TYPE_VEG_FORTE
			resInterne = ConstantesAgents.RES_INT_VEG_FORTE;
			resExterne = ConstantesAgents.RES_EXT_VEG_FORTE;
			break;
		case 5: // TYPE_HABITATION
			resInterne = ConstantesAgents.RES_INT_HABITATION;
			resExterne = ConstantesAgents.RES_EXT_HABITATION;
			break;
		default: // Autres types non-inflammables
			resInterne = 0;
			resExterne = 0;
			inflammable = false;
			break;
		}
	}

	public double doubleValue() {
		return (type-1); // 0 -> 7
	}

	public void step(SimState iModel) {
		if(stp==null)
			System.out.println(this.toString() + " stp null");
		
		Model aModel = (Model) iModel;
		if(this.isInflammable() && this.resInterne == 0 && this.getType() != ConstantesAgents.TYPE_BRULE)
		{
			AgentEnvironnement aAgentEnv = new AgentEnvironnement(ConstantesAgents.TYPE_BRULE, 0);
			Int2D aLocation = new Int2D(this.getX(), this.getY());if(this.stp!=null)
			this.stp.stop();
			aModel.getYard().removeObjectsAtLocation(aLocation.x, aLocation.y);
			aAgentEnv.setLocation(aLocation);
			aModel.getYard().setObjectLocation(aAgentEnv, aLocation);
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isInflammable() {
		return inflammable;
	}

	public void setInflammable(boolean inflammable) {
		this.inflammable = inflammable;
	}

	public int getResInterne() {
		return resInterne;
	}

	public void setResInterne(int resInterne) {
		this.resInterne = resInterne;
	}
	
	public void reduceResInterne(int iForce)
	{
		if(this.getResInterne()>0)
			this.resInterne -= iForce;
	}

	public int getResExterne() {
		return resExterne;
	}

	public void setResExterne(int resExterne) {
		this.resExterne = resExterne;
	}
	
	public void reduceResExterne(int iForce)
	{
		if(this.getResExterne()>0)
			this.resExterne -= iForce;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
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

	public int getSens() {
		return sens;
	}

	public void setSens(int sens) {
		this.sens = sens;
	}

	public Stoppable getStp() {
		return stp;
	}

	public void setStp(Stoppable stp) {
		this.stp = stp;
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
	
	public String toString()
	{
		return "Agent " + type + ", location ("+x+", "+y+")"; 
	}
}
