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
	
	public AgentCanadair(int iResistance, int iDeplacement,int iForce, int iPerception) {
		super(iResistance, iDeplacement,iForce, iPerception);
	}
	
	@Override
	public void step(SimState iModel) {
		Model aModel = (Model) iModel;
		AgentFeu aAgentFeu = null;
//		System.out.println("agent canadair" + this);
		if(iModel.schedule.getSteps()%ConstantesAgents.INTERVAL_ACTION_CANADAIR==0 && iModel.schedule.getSteps()>0)
			action = true;
		
		if(action && rapprochement)
		{
			HashMap<String, Object> iValues = new HashMap<String, Object>();
			iValues = detectionFeu(aModel);
//			System.out.println(iValues);
			aAgentFeu = (AgentFeu)iValues.get("agent");
			Integer aDpc = (Integer)iValues.get("distance");
			if(RapprochementFeu(aAgentFeu, aModel, aDpc))
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
//		System.out.println("detection");
		HashMap<String, Object> oValues = new HashMap<String, Object>();
		AgentFeu oAgent = null;
		for (int i=1; i < iModel.getYard().getHeight(); i++)
		{
			Bag aAgents = iModel.getYard().getNeighborsMaxDistance(this.getX(), this.getY(), i, false, null, null, null);
			oAgent = checkAgentFeu(aAgents);
			if(oAgent != null)
			{;
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
				System.out.println(i);
				if(i instanceof AgentFeu)
				{
					AgentFeu aAgent = (AgentFeu)i;
					aAgent.reduceRes(getForce());
				}
			}			
		}
	}
	
	private boolean EloignerFeu(Model iModel)
	{
//		System.out.println("éloignement");
		
		// temporaire
		iModel.getYard().remove(this);
		Int2D aLocation = new Int2D(-1, iModel.random.nextInt(iModel.getYard().getHeight()));
		iModel.getYard().setObjectLocation(this, aLocation);
		this.setLocation(aLocation);
		return true;
	}
}
