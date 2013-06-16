package com.ia04.agents;
import java.util.ArrayList;
import java.util.Random;

import sim.engine.SimState;
import sim.util.Bag;

import com.ia04.main.Model;

@SuppressWarnings("serial")
public class AgentPieton extends AgentPompier {

	public AgentPieton(int iResistance, int iDeplacement,int iForce, int iPerception) {
		super(iResistance, iDeplacement,iForce, iPerception);
	}
	
	public void step(SimState iModel) {
		Model aModel = (Model) iModel;
		if(!vivant())
		{
			this.getStp().stop();
			aModel.getYard().remove(this);
			aModel.incNbDied();
		}
		else if (aModel.schedule.getSteps()%5 == 0) // petite régénération
		{
			this.setResistance(getResistance()+1);
		}
		
		AgentFeu aAgentFeu = null;
		int i;
		
		for (i=0; i < aModel.getYard().getHeight(); i++)
		{
			Bag aAgents = aModel.getYard().getNeighborsMaxDistance(this.getX(), this.getY(), i, false, null, null, null);
			ArrayList<AgentFeu> agentsFeu = new ArrayList<AgentFeu>();
			for(Object item : aAgents)
			{
				if(item instanceof AgentFeu && ((AgentFeu) item).getResistance()>0)
					agentsFeu.add((AgentFeu)item);
			}
			if(agentsFeu.size()>0)
			{
				Random random = new Random();
				int index = random.nextInt(agentsFeu.size());
				aAgentFeu = agentsFeu.get(index);
//				System.out.println(aAgentFeu);
				break;				
			}
		}
		
		if(i==0 || i==1)
			eteindreFeu(aAgentFeu);
		else if(aAgentFeu != null)
			RapprochementFeu(aAgentFeu, aModel, i);
		
	}
	
	private void eteindreFeu(AgentFeu aAgentFeu) {
		aAgentFeu.reduceRes(this.getForce());
		
	}
	
	protected boolean RapprochementFeu(AgentFeu iAgentFeu, Model iModel, Integer iDist)
	{
		Integer deplacementRestant = this.getDeplacement();
		Integer newX, newY, currentX, currentY;
		currentX = newX = this.getX();
		currentY = newY = this.getY();
		while(deplacementRestant > 0)
		{
			Integer deltaX = currentX - iAgentFeu.getX(); 
			Integer deltaY = currentY - iAgentFeu.getY();
			int[][] newPosition = new int[4][2];
			int dividerX = Math.abs(deltaX)!=0?Math.abs(deltaX):1;
			int dividerY = Math.abs(deltaY)!=0?Math.abs(deltaY):1;
			if((Math.min(Math.abs(deltaX), Math.abs(deltaY)) == Math.abs(deltaX)) && deltaX!=0){
				newPosition = getNewAbscissePosition(currentX, currentY, deltaX, deltaY, dividerX, dividerY);
			}else if(deltaY != 0){
				newPosition = getNewOrdonneePosition(currentX, currentY, deltaX, deltaY, dividerX, dividerY);
			}else{
				return true;
			}
			Integer nb_test = 0;
			
			do{
				Random random = new Random();
				Integer directionProbability = random.nextInt(100)+1;
				Integer direction;
				if(directionProbability<=97){
					direction = 0;
				}else if(directionProbability<=98){
					direction = 1;
				}else if(directionProbability<=99){
					direction = 2;
				}else{
					direction = 3;
				}
				newX = newPosition[direction][0];
				newY = newPosition[direction][1];
				nb_test++;
//				System.out.println("Je suis l'agent "+this.toString());
//				System.out.println("Ma cible est "+ iAgentFeu.toString());
//				System.out.println("Je veux aller en x:"+newX+" y:"+newY);
			}while(!deplacementPossible(iModel, newX, newY) && nb_test<=10);
				
			if(nb_test<=4){
				deplace(iModel, newX, newY);
			}else{
				newX = currentX;
				newY = currentY;
			}
			deplacementRestant--;
		}
		return false;
	}
	public int[][] getNewAbscissePosition(int currentX, int currentY, int deltaX, int deltaY, int dividerX, int dividerY){
		int[][] newPosition = new int[4][2];
		newPosition[0][0] = currentX - deltaX/dividerX;
		newPosition[0][1] = currentY;
		newPosition[1][0] = currentX;
		newPosition[1][1] = currentY - deltaY/dividerY;
		newPosition[2][0] = currentX;
		newPosition[2][1] = currentY + deltaY/dividerY;
		newPosition[3][0] = currentX + deltaX/dividerX;
		newPosition[3][1] = currentY;
		return newPosition;
	}
	public int[][] getNewOrdonneePosition(int currentX, int currentY, int deltaX, int deltaY, int dividerX, int dividerY){
		int[][] newPosition = new int[4][2];
		newPosition[0][0] = currentX;
		newPosition[0][1] = currentY - deltaY/dividerY;
		newPosition[1][0] = currentX - deltaX/dividerX;
		newPosition[1][1] = currentY;
		newPosition[2][0] = currentX;
		newPosition[2][1] = currentY + deltaY/dividerY;
		newPosition[3][0] = currentX;
		newPosition[3][1] = currentY + deltaX/dividerX;
		return newPosition;
	}
	public boolean deplacementPossible(Model iModel, int x, int y){
		try{
			for(Object item: iModel.getYard().getObjectsAtLocation(x, y)){
				if(item instanceof AgentPieton)
					return false;
			}
		}
		catch(Exception e){
//			System.out.println("["+x+","+y+"]");
//			System.exit(0);
			return false;
		}
		return true;
	}
	
	public void deplace(Model iModel, int x, int y){
		this.setX(x);
		this.setY(y);
		iModel.getYard().setObjectLocation(this, x, y);
	}
}
