package main;

import javax.swing.JPanel;

import controller.Controller;

public abstract class Screen extends JPanel {
	private Controller controller;
	
	public abstract void render();
}
