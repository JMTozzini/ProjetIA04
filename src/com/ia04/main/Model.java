package com.ia04.main;

import com.ia04.agents.AgentEnvironnement;
import com.ia04.constantes.ConstantesAgents;
import com.ia04.constantes.ConstantesEnv;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class Model extends SimState {

	private static final long serialVersionUID = 1L;

	private SparseGrid2D yard;
	
	public Model(long iSeed) {
		super(iSeed);
		yard = new SparseGrid2D(ConstantesEnv.GRID_SIZE, ConstantesEnv.GRID_SIZE);
	}
	
	public void start()
	{
		super.start();
		yard.clear();
		setEnvironnement();
	}

	public void setEnvironnement()
	{
		AgentEnvironnement aAgentEnv = new AgentEnvironnement(ConstantesAgents.TYPE_VEG_MOY, 0);
		Int2D aLocation = new Int2D(random.nextInt(yard.getWidth()), random.nextInt(yard.getHeight()));
		yard.setObjectLocation(aAgentEnv, aLocation);
		aAgentEnv.setX(aLocation.x);
		aAgentEnv.setY(aLocation.y);
	}
	
	public SparseGrid2D getYard() {
		return yard;
	}

//	public void setYard(SparseGrid2D yard) {
//		this.yard = yard;
//	}
}
