package controller.combatController.player;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.DoodleTactics;
import graphics.MenuItem;

public class EquipmentMenu extends MenuItem implements CombatMenu {

	public EquipmentMenu(JPanel container, BufferedImage defltPath,
			BufferedImage hoveredPath, DoodleTactics dt) {
		super(container, defltPath, hoveredPath, dt);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addToDrawingQueue() {
		
	}

	@Override
	public void removeFromDrawingQueue() {
		
	}
}
