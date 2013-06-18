package com.ia04.main;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.grid.DrawPolicy;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.FacetedPortrayal2D;
import sim.portrayal.simple.ImagePortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.util.Bag;

import com.ia04.agents.AgentCamion;
import com.ia04.agents.AgentCanadair;
import com.ia04.agents.AgentEnvironnement;
import com.ia04.agents.AgentFeu;
import com.ia04.agents.AgentPieton;
import com.ia04.agents.AgentPompier;
import com.ia04.constantes.ConstantesGenerales;

public class Vue extends GUIState{

	private Display2D display, displayChart, displayChartFiremen;
	private JFrame displayFrame, chartFrame, chartFiremenFrame;
	private SparseGridPortrayal2D yardPortrayal;
	private org.jfree.data.xy.XYSeries seriesFire;    // les donn�es � afficher sur le chart
	private org.jfree.data.xy.XYSeries seriesBurnt;    // les donn�es � afficher sur le chart
	private org.jfree.data.xy.XYSeries seriesFiremen;    // les donn�es � afficher sur le chart
	private org.jfree.data.xy.XYSeries seriesExtinguished;    // les donn�es � afficher sur le chart
	private sim.util.media.chart.TimeSeriesChartGenerator chart, chartFiremen;  // les charts de monitoring

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
		setupChart();
	}

	public void setupEnvironnement()
	{
		Model aModel = (Model) state;
		yardPortrayal.setField(aModel.getYard());
		yardPortrayal.setPortrayalForClass(AgentEnvironnement.class, new FacetedPortrayal2D(
				new SimplePortrayal2D[]
						{
						new RectanglePortrayal2D(new Color(173, 255, 47), 1, true), // TYPE_VEG_FAIBLE
//						new RectanglePortrayal2D(new Color(255, 127, 36), 1, true), // TYPE_ROCHE
						new ImagePortrayal2D(new ImageIcon("res/roche2.png"), 1.4), // TYPE_ROCHE
						new OvalPortrayal2D(new Color(34, 139, 34), 1, true), 		// TYPE_VEG_MOY 
						new OvalPortrayal2D(new Color(0, 100, 0), 1, true), 		// TYPE_VEG_FORTE
//						new RectanglePortrayal2D(new Color(92, 51, 23), 1, true), 	// TYPE_HABITATION
						new ImagePortrayal2D(new ImageIcon("res/maison2.gif"), 1),	// TYPE_HABITATION
						new RectanglePortrayal2D(new Color(0, 127, 255), 1, true), 	// TYPE_EAU
						new RectanglePortrayal2D(new Color(115, 115, 115), 1, true),// TYPE_ROUTE
						new RectanglePortrayal2D(Color.BLACK, 1, true),				// TYPE_BRULE
//						new ImagePortrayal2D(new ImageIcon("res/brule3.png"), 1),	// TYPE_BRULE
						// Ordre inversement liée à l'importance (plus important en dernier)
						})
				);
//		yardPortrayal.setPortrayalForClass(AgentFeu.class, new OvalPortrayal2D(Color.RED, 1, true));
		yardPortrayal.setPortrayalForClass(AgentFeu.class, new ImagePortrayal2D(new ImageIcon("res/feu2.png"), 1));
		yardPortrayal.setPortrayalForClass(AgentCamion.class, new OvalPortrayal2D(Color.MAGENTA, 1, true));
//		yardPortrayal.setPortrayalForClass(AgentCamion.class, new LabelledPortrayal2D(new OvalPortrayal2D(Color.MAGENTA, 1, false), "C"));
//		yardPortrayal.setPortrayalForClass(AgentCamion.class, new ImagePortrayal2D(new ImageIcon("res/camion3.png"), 1));
//		yardPortrayal.setPortrayalForClass(AgentCanadair.class, new OvalPortrayal2D(Color.YELLOW, 1, true));
		yardPortrayal.setPortrayalForClass(AgentCanadair.class, new ImagePortrayal2D(new ImageIcon("res/canadair.png"), 2));
		yardPortrayal.setPortrayalForClass(AgentPieton.class, new OvalPortrayal2D(Color.WHITE, 1, true));
//		yardPortrayal.setPortrayalForClass(AgentPieton.class, new LabelledPortrayal2D(new OvalPortrayal2D(Color.WHITE, 1, false), "P"));
//		yardPortrayal.setPortrayalForClass(AgentPieton.class, new ImagePortrayal2D(new ImageIcon("res/pieton.png"), 0.9));
		yardPortrayal.setDrawPolicy(new DrawPolicy() { // Afficage de l'agent Feu prioritaire
			public boolean objectToDraw(Bag iBag, Bag oBag) {
				for(Object aAgent : iBag)
				{
					if(aAgent instanceof AgentCanadair)
						oBag.add(aAgent);
				}
				for(Object aAgent : iBag)
				{
					if(aAgent instanceof AgentCamion)
						oBag.add(aAgent);
				}
				for(Object aAgent : iBag)
				{
					if(aAgent instanceof AgentPieton)
						oBag.add(aAgent);
				}
				for(Object aAgent : iBag)
				{
					if(aAgent instanceof AgentFeu)
						oBag.add(aAgent);
				}
				for(Object aAgent : iBag)
				{
					if(!(aAgent instanceof AgentFeu) && !(aAgent instanceof AgentPompier))
						oBag.add(aAgent);
				}
				return false;
			}
		});
		display.reset();
		display.setBackdrop(/*Color.WHITE*/new Color(173, 255, 47));
		display.repaint();
	}

	@SuppressWarnings("serial")
	private void setupChart() {
		chart.removeAllSeries();
		chartFiremen.removeAllSeries();
		seriesFire = new org.jfree.data.xy.XYSeries(
				"FireSeries",
				false);
		seriesBurnt = new org.jfree.data.xy.XYSeries(
				"BurntSeries",
				false);
		seriesFiremen = new org.jfree.data.xy.XYSeries(
				"FiremenSeries",
				false);
		seriesExtinguished = new org.jfree.data.xy.XYSeries(
				"ExtinguishedSeries",
				false);
		chart.addSeries(seriesFire, null);
		chart.addSeries(seriesBurnt, null);
		chart.addSeries(seriesExtinguished, null);
		chartFiremen.addSeries(seriesFiremen, null);
		scheduleRepeatingImmediatelyAfter(new Steppable()
		{
			public void step(SimState state)
			{
				Model aModel = (Model) state;

				double time = state.schedule.getTime();
				double nbFire = aModel.getNbFire();
				double nbBurnt = aModel.getNbBurnt();
				double nbFiremen = aModel.getNbFiremen();
				double nbExtinguished = aModel.getNbExtinguished();

				// now add the data
				if (time >= Schedule.EPOCH && time < Schedule.AFTER_SIMULATION)
				{
					seriesFire.add(time, nbFire, true);
					seriesBurnt.add(time, nbBurnt, true);
					seriesExtinguished.add(time, nbExtinguished, true);
					seriesFiremen.add(time, nbFiremen, true);

					// we're in the model thread right now, so we shouldn't directly
					// update the chart.  Instead we request an update to occur the next
					// time that control passes back to the Swing event thread.
					chart.updateChartLater(state.schedule.getSteps());
					chartFiremen.updateChartLater(state.schedule.getSteps());
				}
			}
		});
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


		
		displayChart = new Display2D(ConstantesGenerales.CHART_FRAME_WIDTH, ConstantesGenerales.CHART_FRAME_HEIGHT, this);
		chart = new sim.util.media.chart.TimeSeriesChartGenerator();
		chart.setTitle("Chart");
		chart.setXAxisLabel("Steps");
		chart.setYAxisLabel("Cases");

		displayChartFiremen = new Display2D(ConstantesGenerales.CHART_FRAME_WIDTH, ConstantesGenerales.CHART_FRAME_HEIGHT, this);
		chartFiremen = new sim.util.media.chart.TimeSeriesChartGenerator();
		chartFiremen.setTitle("Chart");
		chartFiremen.setXAxisLabel("Steps");
		chartFiremen.setYAxisLabel("Firemen alive");
		
		
		chartFrame = displayChart.createFrame();
		chartFrame.setTitle("Monithoring chart");
		chartFrame.setLocationRelativeTo(null);
		iController.registerFrame(chartFrame);
		chartFrame.setVisible(true);
		chartFrame.pack();
		displayChart.add(chart);
		
		
		chartFiremenFrame = displayChartFiremen.createFrame();
		chartFiremenFrame.setTitle("Monithoring chart Firemen");
		chartFiremenFrame.setLocationRelativeTo(null);
		iController.registerFrame(chartFiremenFrame);
		chartFiremenFrame.setVisible(true);
		chartFiremenFrame.pack();
		displayChartFiremen.add(chartFiremen);
		// the console automatically moves itself to the right of all
		// of its registered frames -- you might wish to rearrange the
		// location of all the windows, including the console, at this
		// point in time....
	}

	public void finish(){

		super.finish();

		chart.update(state.schedule.getSteps(), true);
		chart.repaint();
		chart.stopMovie();
	}

	public void quit()
	{
		super.quit();
		chart.update(state.schedule.getSteps(), true);
		chart.repaint();
		chart.stopMovie();
		if (chartFrame != null)	chartFrame.dispose();
		chartFrame = null;
	}
}
