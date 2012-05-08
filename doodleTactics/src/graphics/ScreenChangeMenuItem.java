package graphics;

import items.Axe;
import items.Bow;
import items.Cuirass;
import items.Dagger;
import items.Footgear;
import items.HealthPotion;
import items.ItemException;
import items.Shield;
import items.Staff;

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
		potion.setDescription("A delicious donut. Heals 10 HP.");
		HealthPotion pot2 = new HealthPotion(_dt, "src/graphics/items/purple_potion.png", "Effervescent Potion", 1000);
		pot2.setDescription("Poisonous. Do not use.");
		HealthPotion pot3 = new HealthPotion(_dt, "src/graphics/items/purple_potion.png", "Effervescent Potion", 1000);
		pot3.setDescription("Use me. Heals to full health.");
		HealthPotion pot4 = new HealthPotion(_dt, "src/graphics/items/purple_potion.png", "Effervescent Potion", 1000);
		pot4.setDescription("Fun stuff. Heals to full health.");
		HealthPotion pot5 = new HealthPotion(_dt, "src/graphics/items/purple_potion.png", "Effervescent Potion", 1000);
		pot5.setDescription("A normal healing potion.");
		Axe axe = new Axe(_dt, "src/graphics/items/axe_icon.png", "Shiny Axe");
		axe.setDescription("An axe");
		axe.setStats(1, 1, 5, 70);
		Footgear boots = new Footgear(_dt, "src/graphics/items/boots_icon.png", "Cool boots");
		boots.setDescription("Lots of traction.");
		boots.setSpeed(2);
		Bow bow = new Bow(_dt, "src/graphics/items/bow_icon.png", "Doodle Bow");
		bow.setDescription("A decent bow.");
		bow.setStats(2, 4, 2, 85);
		Cuirass cuirass = new Cuirass(_dt, "src/graphics/items/cuirass_icon.png", "Doodle Cuirass");
		cuirass.setDefense(2);
		cuirass.setResistance(2);
		cuirass.setDescription("Shields you from the cold.");
		Dagger dagger = new Dagger(_dt, "src/graphics/items/dagger_icon.png", "Doodle Dagger");
		dagger.setDescription("Dagger.");
		dagger.setStats(1, 1, 3, 90);
		Staff staff = new Staff(_dt, "src/graphics/items/staff_icon.png", "Doodle Staff");
		staff.setDescription("A staff thing");
		staff.setStats(1, 2, 1, 80);
		Shield shield = new Shield(_dt, "src/graphics/items/shield_icon.png", "Shield thing");
		shield.setDescription("thing");
		shield.setDefense(5);
		Shield shield2 = new Shield(_dt, "src/graphics/items/shield_icon.png", "Shiny shield");
		shield2.setDefense(10);
		shield2.setDescription("Better shield.");
		try {
			_dt.getParty().get(0).addToInventory(potion);
			_dt.getParty().get(1).updateHP(-10);
			_dt.getParty().get(1).addToInventory(pot2);
			_dt.getParty().get(2).addToInventory(pot3);
			_dt.getParty().get(3).addToInventory(pot4);
			_dt.getParty().get(4).addToInventory(pot5);
			_dt.getParty().get(2).changeWeapon(axe);
			_dt.getParty().get(3).changeFootgear(boots);
			_dt.getParty().get(4).addToInventory(bow);
			_dt.getParty().get(2).changeCuirass(cuirass);
			_dt.getParty().get(1).changeWeapon(dagger);
			_dt.getParty().get(3).changeWeapon(staff);
			_dt.getParty().get(2).changeShield(shield);
			_dt.getParty().get(0).changeShield(shield2);

		} catch (ItemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		_nextScreen.repaint();
		_dt.getGameScreen().switchToClassChooserMenu();

	}
}
