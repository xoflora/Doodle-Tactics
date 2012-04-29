package main;

import controller.ErrorScreenController;

public class ErrorScreen extends Screen<ErrorScreenController> {

	public ErrorScreen(DoodleTactics dt) {
		super(dt);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ErrorScreenController defaultController() {
		return new ErrorScreenController(_dt);
	}

}
