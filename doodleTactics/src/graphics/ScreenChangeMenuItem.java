package graphics;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import controller.Controller;

import main.DoodleTactics;
import main.Screen;

public class ScreenChangeMenuItem extends MenuItem {

	private Screen<? extends Controller> _nextScreen;
	
	public ScreenChangeMenuItem(JPanel container, BufferedImage defltPath, BufferedImage hoveredPath,
			DoodleTactics dt, Screen<? extends Controller> screen) {
		
		super(container, defltPath, hoveredPath, dt);
		_nextScreen = screen;
	}

	@Override
	public void activate(int type) {		
		_dt.changeScreens(_nextScreen);
		_dt.getGameScreen().setMap("src/tests/data/testMapDemo");

	}
}
