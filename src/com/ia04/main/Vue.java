package com.ia04.main;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;

import com.ia04.agents.AgentEnvironnement;
import com.ia04.agents.AgentFeu;
import com.ia04.constantes.ConstantesGenerales;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.FacetedPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;

public class Vue extends GUIState{

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
		yardPortrayal.setPortrayalForClass(AgentEnvironnement.class, new FacetedPortrayal2D(
                new SimplePortrayal2D[]
                {
                		new RectanglePortrayal2D(new Color(173, 255, 47), 1, true), // TYPE_VEG_FAIBLE
                		new RectanglePortrayal2D(new Color(255, 127, 36), 1, true), // TYPE_ROCHE
                		new OvalPortrayal2D(new Color(34, 139, 34), 1, true), 		// TYPE_VEG_MOY 
                		new OvalPortrayal2D(new Color(0, 100, 0), 1, true), 		// TYPE_VEG_FORTE
                		new RectanglePortrayal2D(new Color(92, 51, 23), 1, true), 	// TYPE_HABITATION
                		new RectanglePortrayal2D(new Color(0, 127, 255), 1, true), 	// TYPE_EAU
                		new RectanglePortrayal2D(new Color(115, 115, 115), 1, true),// TYPE_ROUTE
                		new OvalPortrayal2D(Color.BLACK, 1, true),					// TYPE_BRULE
                		// Ordre inversement liée à l'importance (plus important en dernier)
                })
		);
		yardPortrayal.setPortrayalForClass(AgentFeu.class, new OvalPortrayal2D(Color.RED, 1, true));
		
		display.reset();
		display.setBackdrop(Color.WHITE/*new Color(173, 255, 47)*/);
		display.repaint();
	}
	
	public void init(Controller iController)
	{
		super.init(iController);
		display = new Display2D(ConstantesGenerales.FRAME_WIDTH, ConstantesGenerales.FRAME_HEIGHT, this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("simulateur d'incendie");
		iController.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.attach(yardPortrayal, "yard");
	}
}
