package main;

import java.awt.Graphics2D;

import map.Tile;

public class MainMenu extends Screen {

	private Tile test;
	private Tile [][] kirbies;
	
	public MainMenu(controller.Controller control) {
		super(control);
		this.setBackground(java.awt.Color.LIGHT_GRAY);
		
		kirbies = new Tile[21][17];
		
		for(int i = 0; i < 21; i++) {
			for(int j = 0; j < 17; j++) {
				kirbies[i][j] = new Tile(this,"src/graphics/kirby.png", i, j, 1);
				kirbies[i][j].setFillColor(java.awt.Color.BLACK);
				kirbies[i][j].setLocation(i*48, j*48);
				kirbies[i][j].setVisible(true);
			}
		}
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
	public void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		for(int i = 0; i < 21; i++) {
			for(int j = 0; j < 17; j++) {
				kirbies[i][j].paint((Graphics2D) g, kirbies[i][j].getPath());
			}
		}
	}

}
