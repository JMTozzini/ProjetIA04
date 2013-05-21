package com.ia04.main;

import java.awt.Color;

import javax.swing.JFrame;

import com.ia04.constantes.ConstantesEnv;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;

public class Vue extends GUIState {

	private Display2D display;
	private JFrame displayFrame;
	//private portrayal
	
	public Vue(SimState iState) {
		super(iState);
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
		// portrayal.setField(model.yard)
		// portrayal.setPortrayalForClass(agent.class, getAgentPortrayal)
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
		//display.attach(portrayal, "yaard");
	}
}
