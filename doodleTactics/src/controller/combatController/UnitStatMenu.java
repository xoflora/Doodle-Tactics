package controller.combatController;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import character.Character;

import main.DoodleTactics;
import graphics.MenuItem;

public class UnitStatMenu extends MenuItem {

	private static int LEFT_MARGIN = 10;
	private static int TOP_MARGIN = 10;
	private static int BUFFER = 16;
	private Character _character;
	private MenuItem _profile;
	
	public UnitStatMenu(JPanel container, BufferedImage defltPath,
			BufferedImage hoveredPath, DoodleTactics dt, Character c) {
		super(container, defltPath, hoveredPath, dt);
		_character = c;
		_profile = new MenuItem(container, _character.getProfileImage(),_character.getProfileImage(), dt);
	}
	
	@Override
	public void paint(Graphics2D brush, BufferedImage img) {
		super.paint(brush, img);
		_profile.paint(brush, img);
		brush.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		brush.setFont(new Font("Arial", Font.BOLD, 14));
		brush.setColor(new Color(0,0,1));
		brush.drawString(_character.getName() + ", Lvl " + _character.getLevel(), (int) this.getX() + LEFT_MARGIN, (int) (this.getY() + BUFFER));
		brush.drawString("HP: " + _character.getHP() + "/" + _character.getBaseStats()[7], (int) this.getX() + LEFT_MARGIN, (int) (this.getY() + BUFFER*2));
		brush.drawString("Atk: " + _character.getBaseStats()[0] + ", Def:" + _character.getBaseStats()[1], (int) this.getX() + LEFT_MARGIN, (int) (this.getY() + BUFFER*3));
	}
	
	@Override
	public void setLocation(double x, double y) {
		super.setLocation(x, y);
		_profile.setLocation(x + LEFT_MARGIN, y + TOP_MARGIN);
		
	}
}