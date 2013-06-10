package com.ia04.agents;

import com.ia04.constantes.ConstantesAgents;
import com.ia04.main.Model;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;

import sim.engine.SimState;
import sim.util.Bag;
import sim.util.Int2D;

@SuppressWarnings("serial")
public class AgentCanadair extends AgentPompier {

	boolean feuAtteint=false;	
	
<<<<<<< HEAD
	public AgentCanadair(int iResistance, int iDeplacement,int iForce) {
		super(iResistance, iDeplacement, iForce);
=======
	public AgentCanadair(int iResistance, int iDeplacement,int iForce, int iPerception) {
		super(iResistance, iDeplacement,iForce, iPerception);
>>>>>>> 40d0a89a6d980394545adf192301dc2e16af0cfb
	}
	
	@Override
	public void step(SimState iModel) {
		Model aModel = (Model) iModel;
		if((aModel.schedule.getSteps()%ConstantesAgents.INTERVAL_ACTION < aModel.getYard().getHeight()/this.getDeplacement())
				&& (aModel.schedule.getSteps()>aModel.getYard().getHeight()/this.getDeplacement()))
		{
			// detection agent feu le plus proche
			AgentFeu aAgentFeu = null;
			int i = 1;
			for (i=1; i < aModel.getYard().getHeight(); i++)
			{
				Bag aAgents = aModel.getYard().getNeighborsMaxDistance(this.getX(), this.getY(), i, false, null, null, null);
				aAgentFeu = checkAgentFeu(aAgents);
				if(aAgentFeu != null)
				{
					System.out.println(aAgentFeu);
					break;				
				}
			}
			if(!feuAtteint)
				feuAtteint=RapprochementFeu(aAgentFeu, aModel, i);
			eteindreFeu(aModel);
			if(feuAtteint)
				feuAtteint=EloignerFeu(aModel);
		}
	}

	
	private AgentFeu checkAgentFeu(Bag iAgents)
	{
		for(Object i : iAgents)
		{
			if(i instanceof AgentFeu)
				return (AgentFeu)i;
		}
		return null;
	}

	private void eteindreFeu(Model iModel){
		Bag aAgents = iModel.getYard().getNeighborsAndCorrespondingPositionsMaxDistance(getX(), getY(), 2, false, null, null, null);
		if(aAgents != null)
		{
			for(Object i:aAgents)
			{
				if(i instanceof AgentFeu)
				{
					AgentFeu aAgent = (AgentFeu)i;
					aAgent.setResistance(aAgent.getResistance()-getForce());
				}
			}			
		}
	}
	
	private boolean EloignerFeu(Model iModel)
	{
		int aX = getX();
		int aY = getY();
		
		// temporaire
		Int2D aLocation = new Int2D(-1, iModel.random.nextInt(iModel.getYard().getHeight()));
		iModel.getYard().setObjectLocation(this, aLocation);
		
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
		return true;
	}
}
