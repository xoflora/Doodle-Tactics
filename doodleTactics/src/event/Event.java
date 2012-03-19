package event;

import java.util.List;

import controller.Controller;

/**
 * 
 * @author rroelke
 *
 */
public abstract class Event extends Controller {
	
	public void release() {
		_dt.releaseControl();
	}
	
}
