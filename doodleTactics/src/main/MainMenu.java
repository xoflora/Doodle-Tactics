package main;

import map.Tile;

public class MainMenu extends Screen {

	private Tile test;
	
	public MainMenu(controller.Controller control) {
		super(control);
		this.setBackground(java.awt.Color.LIGHT_GRAY);
		test = new Tile("/home/fjin/Doodle Tactics/");
		render();
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
	public void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
	}

}
