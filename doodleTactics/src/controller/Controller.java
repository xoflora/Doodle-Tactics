package controller;

import java.awt.event.MouseListener;

import main.*;

public abstract class Controller implements MouseListener {
	
	private DoodleTactics dt;
	private Screen screen;
	
	abstract void release();
}
