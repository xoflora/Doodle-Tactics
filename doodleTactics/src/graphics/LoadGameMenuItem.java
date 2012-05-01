package graphics;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.DoodleTactics;
import main.Screen;
import controller.Controller;

public class LoadGameMenuItem extends MenuItem{

	
	public LoadGameMenuItem(JPanel container, BufferedImage defltPath,
			BufferedImage hoveredPath, DoodleTactics dt) {
		super(container, defltPath, hoveredPath, dt);
		// TODO Auto-generated constructor stub
	}

	/**
	 * overrides method in MenuItem
	 */
	@Override
	public void activate(int type) {
		_dt.getGameScreen().loadGame("src/tests/data/testSave");
		_dt.setScreen(_dt.getGameScreen());
		_dt.getGameScreen().repaint();
		
	}


}
