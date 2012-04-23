package controller;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.*;

public abstract class Controller implements MouseListener, KeyListener, MouseMotionListener {
	
	protected DoodleTactics _dt;
	
	public Controller(DoodleTactics dt) {
		_dt = dt;
	}
	
	public abstract void take();
	public abstract void release();
	
	public abstract Screen<? extends Controller> getScreen();
}
