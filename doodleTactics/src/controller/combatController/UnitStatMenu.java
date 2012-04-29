package controller.combatController;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import character.Character;

import main.DoodleTactics;
import graphics.MenuItem;

public class UnitStatMenu extends MenuItem {

	private Character _character;
	
	public UnitStatMenu(JPanel container, BufferedImage defltPath,
			BufferedImage hoveredPath, DoodleTactics dt, Character c) {
		super(container, defltPath, hoveredPath, dt);
		_character = c;
	}
	
	@Override
	public void paint(Graphics2D brush, BufferedImage img) {
		super.paint(brush, img);
	}
	

}