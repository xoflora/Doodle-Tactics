package graphics;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.DoodleTactics;
import main.LoadGameScreen;
public class SaveGameMenuItem extends MenuItem{
	public SaveGameMenuItem(JPanel container, BufferedImage defltPath,
			BufferedImage hoveredPath, DoodleTactics dt) {
		super(container, defltPath, hoveredPath, dt);
		// TODO Auto-generated constructor stub
	}
	
	
	public void activate(int type) {		
		_dt.changeScreens(new SaveGameScreen(_dt));

	}
}
