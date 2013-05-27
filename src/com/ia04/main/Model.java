package com.ia04.main;

import com.ia04.constantes.ConstantesEnv;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;

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
		// add agents
	}

	public SparseGrid2D getYard() {
		return yard;
	}

//	public void setYard(SparseGrid2D yard) {
//		this.yard = yard;
//	}
}
