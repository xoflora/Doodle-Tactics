package main;

import graphics.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;

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
		try {
			BufferedImage titleD = ImageIO.read(new File("src/graphics/menu/title.png"));
			BufferedImage newGameD = ImageIO.read(new File("src/graphics/menu/new_game.png"));
			BufferedImage newGameH = ImageIO.read(new File("src/graphics/menu/new_game_hovered.png"));
			BufferedImage continueD = ImageIO.read(new File("src/graphics/menu/continue.png"));
			BufferedImage continueH = ImageIO.read(new File("src/graphics/menu/continue_hovered.png"));
			BufferedImage quitD = ImageIO.read(new File("src/graphics/menu/quit.png"));
			BufferedImage quitH = ImageIO.read(new File("src/graphics/menu/quit_hovered.png"));
			
			_title = new MenuItem(this, titleD, titleD, dt);
			_newGame = new ScreenChangeMenuItem(this, newGameD,newGameH, dt, dt.getGameScreen());
			
			_continue = new MenuItem(this, continueD,continueH, dt);
			_quit = new MenuItem(this, quitD,quitH, dt);
			_title.setLocation(((DoodleTactics.TILE_COLS*map.Tile.TILE_SIZE) - _title.getCurrentImage().getWidth())/2, 50);
			int offset = ((DoodleTactics.TILE_COLS*map.Tile.TILE_SIZE) - _newGame.getCurrentImage().getWidth())/2;
		
		_newGame.setLocation(offset, 250);
		_continue.setLocation(offset, 400);
		_quit.setLocation(offset, 550);
		_title.setVisible(true);
		_newGame.setVisible(true);
		_continue.setVisible(true);
		_quit.setVisible(true);
		} catch (IOException e) {
			
		}
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

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
	public void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		_title.paint((Graphics2D) g, _title.getCurrentImage());
		_newGame.paint((Graphics2D) g, _newGame.getCurrentImage());
		_continue.paint((Graphics2D) g, _continue.getCurrentImage());
		_quit.paint((Graphics2D) g, _quit.getCurrentImage());
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
			
		//	System.out.println("WEORIUEW");
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
