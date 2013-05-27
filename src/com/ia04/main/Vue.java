package com.ia04.main;

import java.awt.Color;

import javax.swing.JFrame;

import com.ia04.agents.AgentEnvironnement;
import com.ia04.constantes.ConstantesEnv;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;

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
		yardPortrayal.setPortrayalForClass(AgentEnvironnement.class, getAgentEnvPortrayal());
		display.reset();
		display.setBackdrop(Color.WHITE);
		display.repaint();
	}
	
	public void init(Controller iController)
	{
		super.init(iController);
		display = new Display2D(ConstantesEnv.FRAME_SIZE, ConstantesEnv.FRAME_SIZE, this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("simulateur d'incendie");
		iController.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.attach(yardPortrayal, "yard");
	}
	
	public OvalPortrayal2D getAgentEnvPortrayal()
	{
		OvalPortrayal2D oOvPor2D = new OvalPortrayal2D(Color.green, true);
		return oOvPor2D;
	}
}
