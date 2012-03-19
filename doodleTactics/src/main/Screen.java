package main;

import javax.swing.JPanel;

import controller.Controller;
/**
 * 
 * @author rroelke
 * Represents a screen in a game; for example, the main menu versus normal gameplay
 */
public abstract class Screen extends JPanel {
	private Controller _controller;
	
	public abstract void render();
}
