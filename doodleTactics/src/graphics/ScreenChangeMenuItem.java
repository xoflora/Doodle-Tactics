package graphics;

import javax.swing.JPanel;

import controller.Controller;

import main.DoodleTactics;
import main.Screen;

public class ScreenChangeMenuItem extends MenuItem {

	private Screen _nextScreen;
	private Controller _control;
	
	public ScreenChangeMenuItem(JPanel container, String defltPath, String hoveredPath,
			DoodleTactics dt, Screen screen, Controller control) {
		super(container, defltPath, hoveredPath, dt);
		_nextScreen = screen;
		_control = control;
	}

	/**
	 * 
	 */

	@Override
	public void activate() {
		_dt.setScreen(_nextScreen);
		_dt.pushController(_control);
	}
	
}