package graphics;

import javax.swing.JPanel;

import controller.Controller;

import main.DoodleTactics;
import main.Screen;

public class ScreenChangeMenuItem extends MenuItem {

	private Screen<? extends Controller> _nextScreen;
	
	public ScreenChangeMenuItem(JPanel container, String defltPath, String hoveredPath,
			DoodleTactics dt, Screen<? extends Controller> screen) {
		
		super(container, defltPath, hoveredPath, dt);
		_nextScreen = screen;
	}

	@Override
	public void activate() {
		
	//	System.out.println("crucnh" + (_nextScreen == _dt.getGameScreen()));
		
		_dt.changeScreens(_nextScreen);
	
	//	_dt.pushController(_control);
	}
}
