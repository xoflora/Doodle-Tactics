package graphics;

import javax.swing.JPanel;

import main.DoodleTactics;
import main.Screen;

public class ScreenChangeMenuItem extends MenuItem {

	private Screen _nextScreen;
	
	
	public ScreenChangeMenuItem(JPanel container, String defltPath, String hoveredPath,
			DoodleTactics dt, Screen screen) {
		super(container, defltPath, hoveredPath, dt);
		_nextScreen = screen;
	}

	/**
	 * 
	 */

	@Override
	public void activate() {
		System.out.println("next screen");
		//_dt.setScreen(_nextScreen);
	}
	
}
