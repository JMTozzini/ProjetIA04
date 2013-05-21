package com.ia04.main;

import sim.display.Console;

public class Main {

	public static void main(String[] args) {
		Model gModel = new Model(System.currentTimeMillis());
		Vue gVue = new Vue(gModel);
		Console gConsole = new Console(gVue);
		gConsole.setVisible(true);
	}

}
