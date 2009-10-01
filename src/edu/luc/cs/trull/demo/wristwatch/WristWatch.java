package edu.luc.cs.trull.demo.wristwatch;

import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Cycle;
import edu.luc.cs.trull.Rename;
import edu.luc.cs.trull.demo.stopwatch.Translation;

/**
 * The top-level control logic for the wristwatch.
 *
 * <PRE>
 * wristwatch =
 *   local UPDATE
 *     light
 *   | update
 *   | cycle MODE 
 *       timemode
 *       datemode
 *       stopwatch
 *       setmode
 * </PRE>
 */

public class WristWatch extends Rename implements EventLabels {
	public WristWatch() {
		DateModel data = new DateModel();
		Composite top = new Composite();
		Cycle set = new Cycle();
		setLocalEvent(UPDATE);
		top.addComponent(new Light());
		top.addComponent(new Update(data));
		top.addComponent(set);
		set.setCycleEvent(MODE);
		set.addComponent(new TimeMode(data));
		set.addComponent(new DateMode(data));
		set.addComponent(new Translation()); // from stopwatch!!
		set.addComponent(new SetMode(data));
		addComponent(top);
	}
}
