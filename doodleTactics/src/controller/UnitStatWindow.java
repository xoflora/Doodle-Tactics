package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import character.Character;

import main.DoodleTactics;
import main.GameScreen;
import graphics.MenuItem;

public class UnitStatWindow extends MenuItem {
	
	private static final String RIGHT_PATH = "src/graphics/menu/unit_stats_box.png";
	private static final String LEFT_PATH = "src/graphics/menu/unit_stats_box_left.png";

	private static final int LEFT_MARGIN = 7;
	private static final int TOP_MARGIN = 10;
	private static final int BUFFER = 16;
	
	private Character _character;
	private GameScreen _screen;
	
	public UnitStatWindow(GameScreen container, DoodleTactics dt, Character c, boolean left) {
		super(container, left ? dt.importImage(LEFT_PATH) : dt.importImage(RIGHT_PATH),
				left ? dt.importImage(LEFT_PATH) : dt.importImage(RIGHT_PATH), dt);

		_screen = container;
		_character = c;
		
		if(left) {
			setLocation(c.getX() - 100, c.getY() - 60);
		} else {
			setLocation(c.getX() + 40, c.getY() - 60);
		}
	}
	
	@Override
	public void paint(Graphics2D brush, BufferedImage img) {
		super.paint(brush, img);
		brush.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		brush.setFont(new Font("Arial", Font.BOLD, 14));
		brush.setColor(new Color(0,0,1));
		brush.drawString(_character.getName(), (int) this.getX() + LEFT_MARGIN, (int) (this.getY() + BUFFER));
		brush.drawString("HP: " + _character.getHP() + "/" + _character.getCurrStats()[Character.MAX_HP] +
				", Lvl " + _character.getLevel(), (int) this.getX() + LEFT_MARGIN, (int) (this.getY() + BUFFER*2));
		brush.drawString("Atk: " + _character.getFullAttackStrength() + ", Def: "
				+ _character.getFullDefense(), (int) this.getX() + LEFT_MARGIN, (int) (this.getY() + BUFFER*3));
	}
	
	@Override
	public void setLocation(double x, double y) {
		super.setLocation(x, y);
		
	}
	
	/**
	 * adds this item to the game screen drawing queue
	 */
	public void addToDrawingQueue() {
		_screen.addMenuItem(this);
		setVisible(true);
		_screen.repaint();
	}
	
	/**
	 * removes this unitStatMenu from the drawing queue
	 */
	public void removeFromDrawingQueue() {
		_screen.removeMenuItem(this);
		setVisible(false);
		_screen.repaint();
	}
}