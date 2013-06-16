package com.ia04.agents;

import java.util.HashMap;

import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Int2D;

import com.ia04.constantes.ConstantesAgents;
import com.ia04.main.Model;

@SuppressWarnings("serial")
public class AgentCanadair extends AgentPompier {

	boolean action = false;
	boolean rapprochement = true;
	int numero;
	int origineX;
	int origineY;
	
	public AgentCanadair(int iResistance, int iDeplacement,int iForce, int iPerception, int numero) {
		super(iResistance, iDeplacement,iForce, iPerception);
	}
	
	@Override
	public void step(SimState iModel) {
		Model aModel = (Model) iModel;
		AgentFeu aAgentFeu = null;
		if(iModel.schedule.getSteps()%(ConstantesAgents.INTERVAL_ACTION_CANADAIR+5*numero)==0 && iModel.schedule.getSteps()>0)
			action = true;
		
		if(action && rapprochement)
		{
			HashMap<String, Object> iValues = new HashMap<String, Object>();
			iValues = detectionFeu(aModel);
			aAgentFeu = (AgentFeu)iValues.get("agent");
			Integer aDistance = (Integer)iValues.get("distance");
			if(RapprochementFeu(aAgentFeu, aModel, aDistance))
			{
				rapprochement = false;
				eteindreFeu(aModel);
			}
		}
		
		if(action && !rapprochement)
		{
			if(EloignerFeu(aModel))
			{
				action = false;
				rapprochement = true;
			}
		}
	}
	
	private HashMap<String, Object> detectionFeu(Model iModel){
		HashMap<String, Object> oValues = new HashMap<String, Object>();
		AgentFeu oAgent = null;
		for (int i=1; i < iModel.getYard().getHeight(); i++)
		{
			Bag aAgents = iModel.getYard().getNeighborsMaxDistance(this.getX(), this.getY(), i, false, null, null, null);
			oAgent = checkAgentFeu(aAgents);
			if(oAgent != null)
			{
				oValues.put("agent", oAgent);
				oValues.put("distance", new Integer(i));
				return oValues;			
			}
		}
		return oValues;
	}
	
	private void eteindreFeu(Model iModel){
		Bag aAgents = iModel.getYard().getNeighborsMaxDistance(getX(), getY(), ConstantesAgents.DIST_EXTINCTION_CANADAIR, false, null, null, null);
		if(aAgents != null)
		{
			for(Object i:aAgents)
			{
				if(i instanceof AgentFeu)
				{
					AgentFeu aAgent = (AgentFeu)i;
					aAgent.reduceRes(iModel, getForce());
				}
			}			
		}
	}
	
	private boolean EloignerFeu(Model iModel)
	{		
		// temporaire
		iModel.getYard().remove(this);
		AgentFeu aFakeAgentFeu = new AgentFeu(1, 1);
		aFakeAgentFeu.setLocation(new Int2D(origineX, origineY));
		int aDist = Math.abs(this.getY()-origineY)+ Math.abs(this.getX() - origineX);
		if(RapprochementFeu(aFakeAgentFeu, iModel, aDist))
			return true;
		else
			return false;
	}
	
	public void setOrigine(Int2D iLocation)
	{
		origineX = iLocation.x;
		origineY = iLocation.y;
	}
}
