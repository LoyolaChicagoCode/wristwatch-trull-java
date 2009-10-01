package edu.luc.cs.trull.demo.wristwatch;

import java.beans.PropertyChangeEvent;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.luc.cs.trull.Scheduling;
import edu.luc.cs.trull.test.Tester;

/**
 * A JUnit test case for testing the WristWatch component.
 */

public class TestWristWatch extends TestCase implements EventLabels {

    WristWatch wristwatch;
    Tester tester;

  	protected void setUp() throws Exception {
		  super.setUp();
  		  BasicConfigurator.configure();
  		  Logger.getRootLogger().setLevel(Level.OFF);
//  		  Logger.getLogger(Cycle.class).setLevel(Level.DEBUG);
//		  Logger.getLogger(Cycle.class).setLevel(Level.OFF);
  		  Logger.getLogger(Tester.class).setLevel(Level.DEBUG);

//  		  Logger.getLogger(Tester.class).setLevel(Level.DEBUG);
      Scheduling.setMode(Scheduling.TEST);

      wristwatch = new WristWatch();
      tester = new Tester();
      wristwatch.addPropertyChangeListener(tester);
      tester.clear();
      Scheduling.getScheduler().start(wristwatch);
  	}
  	
  	protected void tearDown() throws Exception {
      Scheduling.getScheduler().stop(wristwatch);
      wristwatch.removePropertyChangeListener(tester);
      wristwatch = null;
      tester = null;
		  BasicConfigurator.resetConfiguration();
      Scheduling.setMode(Scheduling.DEFAULT);
    		super.tearDown();
  	}
  	
    public void testBasic() throws Exception {
        waitForEvents();
        tester.matchSome(TIME);
        
        Integer t1 = (Integer)tester.getValue(NOW);
 
        for (int i = 0; i < 600; i ++) { updateSync(TICK); }
        waitForEvents();
      
        tester.matchValue(NOW, new Integer(t1.intValue() + 10));
        
    }

    public void testDate() throws Exception {
     	updateSync(MODE);
        waitForEvents();
        tester.matchSome(DATE);
        tester.matchValue(NOW,new Integer(61));
    }

    public void testModes() {
        updateSync(MODE);
        updateSync(MODE);
        updateSync(MODE);
        for (int i = 0; i < 3; i ++) {
            tester.clear();
            updateSync(MODE);
            waitForEvents();
            tester.matchSome(new Object[] { TIME, NOW });
            tester.clear();
            updateSync(MODE);
            waitForEvents();
            tester.matchSome(new Object[] { DATE, NOW });
            tester.clear();
            updateSync(MODE);
            waitForEvents();
            tester.matchSome(new Object[] { STOP, NOW });
            tester.clear();
            updateSync(MODE);
            waitForEvents();
            tester.matchSome(new Object[] { HOUR, NOW });
            for (int j = 0; j < 5; j ++) {
                tester.clear();
                updateSync(RESET);
                waitForEvents();
                tester.matchSome(new Object[] { MIN, NOW });
                tester.clear();
                updateSync(RESET);
                waitForEvents();
                tester.matchSome(new Object[] { DAY, NOW });
                tester.clear();
                updateSync(RESET);
                waitForEvents();
                tester.matchSome(new Object[] { MONTH, NOW });
                tester.clear();
                updateSync(RESET);
                waitForEvents();
                tester.matchSome(new Object[] { HOUR, NOW });
            }
        }
    }

    public void testStopwatch() { 
    	waitForEvents();
        Integer t1 = (Integer)tester.getValue(NOW);
        updateSync(MODE);
        tester.clear();
        updateSync(MODE);
        waitForEvents();
        tester.matchSome(STOP);
        tester.matchValue(NOW,new Integer(0));
        for (int l = 0; l < 2; l ++) {
            for (int k = 0; k < 2; k ++) {
                Integer t0 = (Integer)tester.getValue(NOW);
                for (int i = 0; i < 10; i ++) { updateSync(TICK); }
                waitForEvents();
                tester.matchValue(NOW,t0);
                updateSync(START);
                for (int i = 0; i < 100; i ++) { updateSync(TICK); }
                waitForEvents();
                tester.matchValue(NOW,new Integer(t0.intValue() + 100));
                updateSync(START);
                for (int i = 0; i < 10; i ++) { updateSync(TICK); }
                waitForEvents();
                tester.matchValue(NOW,new Integer(t0.intValue() + 100));
                updateSync(START);
                for (int i = 0; i < 10; i ++) { updateSync(TICK); }
                waitForEvents();
                tester.matchValue(NOW,new Integer(t0.intValue() + 110));
                updateSync(RESET);
                for (int i = 0; i < 10; i ++) { updateSync(TICK); }
                waitForEvents();
                tester.matchValue(NOW,new Integer(t0.intValue() + 110));
                updateSync(RESET);
                for (int i = 0; i < 10; i ++) { updateSync(TICK); }
                waitForEvents();
                tester.matchValue(NOW,new Integer(t0.intValue() + 130));
                updateSync(START);
                for (int i = 0; i < 10; i ++) { updateSync(TICK); }
                waitForEvents();
                tester.matchValue(NOW,new Integer(t0.intValue() + 130));
                updateSync(START);
                for (int i = 0; i < 10; i ++) { updateSync(TICK); }
                waitForEvents();
                tester.matchValue(NOW,new Integer(t0.intValue() + 140));
                updateSync(RESET);
                for (int i = 0; i < 10; i ++) { updateSync(TICK); }
                waitForEvents();
                tester.matchValue(NOW,new Integer(t0.intValue() + 140));
                updateSync(RESET);
                updateSync(START);
                waitForEvents();
                tester.matchValue(NOW,new Integer(t0.intValue() + 150));
                //START RESET START RESET RESET
                updateSync(START);
                for (int i = 0; i < 10; i ++) { updateSync(TICK); }
                waitForEvents();
                tester.matchValue(NOW,new Integer(t0.intValue() + 160));
                updateSync(RESET);
                waitForEvents();
                for (int i = 0; i < 10; i ++) { updateSync(TICK); }
                waitForEvents();
                tester.matchValue(NOW,new Integer(t0.intValue() + 160));
                updateSync(START);
                waitForEvents();
                updateSync(RESET);
                waitForEvents();
                tester.matchValue(NOW,new Integer(t0.intValue() + 170));
                updateSync(RESET);
                waitForEvents();
                tester.matchValue(NOW,t0);
            }
            updateSync(RESET);
            waitForEvents();
            tester.matchValue(NOW,new Integer(0));
        }

        // Timekeeping while using stopwatch, should become separate test
        updateSync(MODE);
        tester.clear();
        updateSync(MODE);
        waitForEvents();
        tester.matchSome(TIME);
        tester.matchValue(NOW,new Integer(t1.intValue() + 13));
    }

    public void testSet() {
        updateSync(MODE);
        updateSync(MODE);
        tester.clear();
        updateSync(MODE);
        waitForEvents();
        tester.matchSome(HOUR);
        Integer t0 = (Integer)tester.getValue(NOW);
        updateSync(START);
        updateSync(START);
        updateSync(START);
        updateSync(START);
        updateSync(START);
        waitForEvents();
        tester.matchValue(NOW,new Integer((t0.intValue() + 300) % 1440));
        tester.clear();
        updateSync(RESET);
        waitForEvents();
        tester.matchSome(MIN);
        t0 = (Integer)tester.getValue(NOW);
        updateSync(START);
        updateSync(START);
        updateSync(START);
        updateSync(START);
        updateSync(START);
        waitForEvents();
        tester.matchValue(NOW,new Integer(t0.intValue() / 60 * 60 + (t0.intValue() + 5) % 60));
        tester.clear();
        updateSync(RESET);
        waitForEvents();
        tester.matchSome(DAY);
        t0 = (Integer)tester.getValue(NOW);
        updateSync(START);
        updateSync(START);
        updateSync(START);
        updateSync(START);
        updateSync(START);
        waitForEvents();
        tester.matchValue(NOW,new Integer(t0.intValue() / 60 * 60 + (t0.intValue() % 60 + 4) % 31 + 1));
        tester.clear();
        updateSync(RESET);
        waitForEvents();
        tester.matchSome(MONTH);
        t0 = (Integer)tester.getValue(NOW);
        updateSync(START);
        updateSync(START);
        updateSync(START);
        updateSync(START);
        updateSync(START);
        updateSync(START);
        waitForEvents();
        Integer t1 = (Integer)tester.getValue(NOW);
        tester.matchValue(NOW,new Integer((t0.intValue() / 60 * 60 + 300) % 720 + 60 + t1.intValue() % 60));
    }

    public void testTurn() {
        updateSync(MODE);
        waitForEvents();
        tester.matchSome(DATE);
        Integer date = (Integer)tester.getValue(NOW);
        tester.clear();
        updateSync(MODE);
        updateSync(MODE);
        waitForEvents();
        tester.matchSome(HOUR);
        Integer t0 = (Integer)tester.getValue(NOW);
        for (int i = t0.intValue() / 60; i < 23; i ++) {
            updateSync(START);
        }
        waitForEvents();
        tester.matchValue(NOW,new Integer(23 * 60 + t0.intValue() % 60));
        updateSync(RESET);
        waitForEvents();
        tester.matchSome(MIN);
        t0 = (Integer)tester.getValue(NOW);
        for (int i = t0.intValue() % 60; i < 58; i ++) {
            updateSync(START);
        }
        waitForEvents();
        tester.matchValue(NOW,new Integer(t0.intValue() / 60 * 60 + 58));
        tester.clear();
        updateSync(MODE);
        updateSync(MODE);
        waitForEvents();
        tester.matchSome(DATE);
        for (int i = 0; i < 120; i ++) {
            updateSync(TICK);
        }
        waitForEvents();
        tester.matchValue(NOW,new Integer(date.intValue() + 1));
    }
    
    public void testLight() throws Exception {
     	updateSync(LIGHT);
     	for (int i = 0; i < 3; i ++) { updateSync(TICK); } 
    	  waitForEvents();
     	tester.matchOrdered(new Object[] { TIME, NOW, LIGHT_ON, NOW, NOW, LIGHT_OFF, NOW });
    }

    public void testLocal() {
   		updateSync(UPDATE);
    		updateSync(UPDATE);
    		updateSync(UPDATE);
    		updateSync(UPDATE);
    		updateSync(TICK);
    		waitForEvents();
    		tester.matchOrdered(new String[] { TIME, NOW, NOW });
    }
  
    public void updateSync(String label) {
        updateSync(new PropertyChangeEvent(this, label, null, label));
    }

    public void updateSync(final PropertyChangeEvent event) {
    	try {
	    	Scheduling.getScheduler().propertyChange(wristwatch, event);
    	} catch (InterruptedException e) { 
    		throw new RuntimeException(e);
    	}
    }

  public void waitForEvents() {
    try {
      Scheduling.getScheduler().await();
    } catch (InterruptedException e) { 
      throw new RuntimeException(e);
    }
  }
}
