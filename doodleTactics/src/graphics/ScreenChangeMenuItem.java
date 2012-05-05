package graphics;

import items.HealthPotion;
import items.ItemException;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import controller.Controller;

import main.DoodleTactics;
import main.Screen;

public class ScreenChangeMenuItem extends MenuItem {
	
	private static final String DEFAULT_MAP_PATH = "src/tests/data/testMapDemo";
	private static final int DEFAULT_X = 15;
	private static final int DEFAULT_Y = 15;

	private Screen<? extends Controller> _nextScreen;
	
	public ScreenChangeMenuItem(JPanel container, BufferedImage defltPath, BufferedImage hoveredPath,
			DoodleTactics dt, Screen<? extends Controller> screen) {
		
		super(container, defltPath, hoveredPath, dt);
		_nextScreen = screen;
	}

	@Override
	public void activate(int type) {		
		_dt.changeScreens(_nextScreen);
		_dt.getGameScreen().setMap(DEFAULT_MAP_PATH, DEFAULT_X, DEFAULT_Y);
		
		//Modify Character
		HealthPotion potion = new HealthPotion(_dt,"src/graphics/items/donut.png", "Magical Sprinkle Donut", 10);
		potion.setDescription("Sup bro");
		HealthPotion pot2 = new HealthPotion(_dt, "src/graphics/items/purple_potion.png", "Effervescent Potion", 1000);
		pot2.setDescription("Poisonous. Do not use.");
		HealthPotion pot3 = new HealthPotion(_dt, "src/graphics/items/purple_potion.png", "Effervescent Potion", 1000);
		pot3.setDescription("Use me.");
		HealthPotion pot4 = new HealthPotion(_dt, "src/graphics/items/purple_potion.png", "Effervescent Potion", 1000);
		pot4.setDescription("Fun stuff.");
		HealthPotion pot5 = new HealthPotion(_dt, "src/graphics/items/purple_potion.png", "Effervescent Potion", 1000);
		pot5.setDescription("A normal healing potion.");
		try {
			_dt.getParty().get(1).addToInventory(potion);
			_dt.getParty().get(1).updateHP(-10);
			_dt.getParty().get(1).addToInventory(pot2);
			_dt.getParty().get(1).addToInventory(pot3);
			_dt.getParty().get(1).addToInventory(pot4);
			_dt.getParty().get(1).addToInventory(pot5);

		} catch (ItemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		_nextScreen.repaint();

	}
}
