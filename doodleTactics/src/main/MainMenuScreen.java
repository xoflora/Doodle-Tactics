package main;

import graphics.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import controller.Controller;
import controller.MainMenuController;
import controller.OverworldController;

/** 
 * This screen models the main menu and is initially displayed
 * upon running doodle tactics
 * @author jeshapir
 */

@SuppressWarnings("serial")
public class MainMenuScreen extends Screen<MainMenuController> {

	private MenuItem _title;
	private ScreenChangeMenuItem _newGame;
	private MenuItem _continue;
	private MenuItem _quit;

	public MainMenuScreen(DoodleTactics dt) {
		super(dt);

		this.setBackground(java.awt.Color.GRAY);
		BufferedImage titleD = dt.importImage("src/graphics/menu/title.png");
		BufferedImage newGameD = dt.importImage("src/graphics/menu/new_game.png");
		BufferedImage newGameH = dt.importImage("src/graphics/menu/new_game_hovered.png");
		BufferedImage continueD = dt.importImage("src/graphics/menu/continue.png");
		BufferedImage continueH = dt.importImage("src/graphics/menu/continue_hovered.png");
		BufferedImage quitD = dt.importImage("src/graphics/menu/quit.png");
		BufferedImage quitH = dt.importImage("src/graphics/menu/quit_hovered.png");

		_title = new MenuItem(this, titleD, titleD, dt);
		_newGame = new ScreenChangeMenuItem(this, newGameD,newGameH, dt, dt.getGameScreen());

		_continue = new LoadGameMenuItem(this, continueD,continueH, dt);
		_quit = new QuitMenuButton(this, quitD,quitH, dt);
		_title.setLocation(((DoodleTactics.TILE_COLS*map.Tile.TILE_SIZE) - _title.getImage().getWidth())/2, 50);
		int offset = ((DoodleTactics.TILE_COLS*map.Tile.TILE_SIZE) - _newGame.getImage().getWidth())/2;

		_newGame.setLocation(offset, 250);
		_continue.setLocation(offset, 400);
		_quit.setLocation(offset, 550);
		_title.setVisible(true);
		_newGame.setVisible(true);
		_continue.setVisible(true);
		_quit.setVisible(true);
	}


@Override
/**
 * @return the default controller for a main menu screen
 */
protected MainMenuController defaultController() {
	//	MainMenuController cont = new MainMenuController(this);

	//	System.out.println(cont.getScreen() == this);


	//	System.out.println(this);
	return new MainMenuController(_dt, this);
}

public void paintComponent(java.awt.Graphics g) {
	super.paintComponent(g);
	_title.paint((Graphics2D) g, _title.getImage());
	_newGame.paint((Graphics2D) g, _newGame.getImage());
	_continue.paint((Graphics2D) g, _continue.getImage());
	_quit.paint((Graphics2D) g, _quit.getImage());
}

public MenuItem checkContains(java.awt.Point point) {

	/* set all of the buttons to default */
	_newGame.setDefault();
	_continue.setDefault();
	_quit.setDefault();

	/* check if the point is in any of the buttons */
	if(_newGame.contains(point)) {
		_newGame.setHovered();
		this.repaint();

		return _newGame;
	}

	if(_continue.contains(point)) {
		_continue.setHovered();
		this.repaint();
		return _continue;
	}

	if(_quit.contains(point)) {
		_quit.setHovered();
		this.repaint();
		return _quit;
	}

	this.repaint();
	return null;
}
}
