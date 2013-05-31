package com.ia04.main;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JFrame;

import com.ia04.agents.AgentEnvironnement;
import com.ia04.constantes.ConstantesAgents;
import com.ia04.constantes.ConstantesGenerales;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;

public class Vue extends GUIState {

	private Display2D display;
	private JFrame displayFrame;
	private SparseGridPortrayal2D yardPortrayal;
	
	public Vue(SimState iState) {
		super(iState);
		yardPortrayal = new SparseGridPortrayal2D();
	}
	
	public static String getName()
	{
		return "simulation d'incendie";
	}
	
	public void start()
	{
		super.start();
		setupEnvironnement();
	}
	
	public void setupEnvironnement()
	{
		Model aModel = (Model) state;
		yardPortrayal.setField(aModel.getYard());
		yardPortrayal.setPortrayalForClass(AgentEnvironnement.class, getAgentEnvPortrayal(0));
//		yardPortrayal.setPortrayalForObject(new AgentEnvironnement(ConstantesAgents.TYPE_ROUTE, 0), getAgentEnvPortrayal(ConstantesAgents.TYPE_ROUTE));
//		yardPortrayal.setPortrayalForObject(new AgentEnvironnement(ConstantesAgents.TYPE_EAU, 0), getAgentEnvPortrayal(ConstantesAgents.TYPE_EAU));
		display.reset();
		display.setBackdrop(Color.WHITE);
		display.repaint();
	}
	
	public void init(Controller iController)
	{
		super.init(iController);
		display = new Display2D(ConstantesGenerales.FRAME_SIZE, ConstantesGenerales.FRAME_SIZE, this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("simulateur d'incendie");
		iController.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.attach(yardPortrayal, "yard");
	}
	
	public RectanglePortrayal2D getAgentEnvPortrayal(int iType)
	{
		RectanglePortrayal2D oRectPor2D = null;
		System.out.println(iType);
		if(iType == ConstantesAgents.TYPE_ROUTE || iType == 0)
			oRectPor2D = new RectanglePortrayal2D(Color.GRAY, 1, true);
		else if(iType == ConstantesAgents.TYPE_EAU)
			oRectPor2D = new RectanglePortrayal2D(Color.BLUE, 1, true);
		return oRectPor2D;
	}
}
