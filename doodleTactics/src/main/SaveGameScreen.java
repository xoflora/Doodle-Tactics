package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import controller.LoadGameController;
import controller.SaveGameController;

public class SaveGameScreen extends Screen<SaveGameController>{

	private BufferedImage _bg;
	public SaveGameScreen(DoodleTactics dt) {
		super(dt);
		_bg = dt.importImage("src/graphics/menu/save_menu.png");
		
	}

	@Override
	protected SaveGameController defaultController() {
		return new SaveGameController(_dt, this);
	}
	
	@Override
	public void paintComponent( Graphics brush ){
		Graphics2D g = (Graphics2D) brush;
		g.drawImage(_bg, null, 0, 0);
	}

}
