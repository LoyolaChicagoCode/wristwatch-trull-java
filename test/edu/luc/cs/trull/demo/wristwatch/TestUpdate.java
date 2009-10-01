package edu.luc.cs.trull.demo.wristwatch;

import java.beans.PropertyChangeEvent;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.luc.cs.trull.Scheduling;

/**
 * @author slusarekb
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestUpdate extends TestCase implements EventLabels {
	Update update;
	DateModel data;

	protected void setUp() throws Exception {
		super.setUp();
	  BasicConfigurator.configure();
	  Logger.getRootLogger().setLevel(Level.OFF);
    Scheduling.setMode(Scheduling.TEST);
		data = new DateModel();
		update = new Update(data);
	}
	
	protected void tearDown() throws Exception {
		data = null;
		update = null;
    BasicConfigurator.resetConfiguration();
    Scheduling.setMode(Scheduling.DEFAULT);
    super.tearDown();
	}
	
	public void testUpdate() throws Exception {
		assertEquals(0, data.getTime());
		int reps = 1000;
		for (int i = 0; i < reps; i ++) {
			Scheduling.getScheduler().propertyChange(update, new PropertyChangeEvent(this, TICK, null, TICK));
		}
		Scheduling.getScheduler().await();
		assertEquals(reps, data.getTime());
	}
}
