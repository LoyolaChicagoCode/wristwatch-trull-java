package edu.luc.cs.trull.demo.wristwatch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.luc.cs.trull.Scheduling;

/**
 * A driver for running this demo.
 */
public class Main implements EventLabels {

  public static void main(String[] args) throws Exception {
    BasicConfigurator.configure();
    Logger.getRootLogger().setLevel(Level.OFF);
    //	  Logger.getLogger(AbstractEmit.class).setLevel(Level.DEBUG);
    Scheduling.setMode(Scheduling.SWINGFAST);

    // the control logic (translation)
    final WristWatch wristwatch = new WristWatch();

    // the timer providing the clock ticks
    final Timer timer = new Timer(1000, new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        System.out.println(TICK);
        wristwatch.propertyChange(new PropertyChangeEvent(this, TICK, null, TICK));
      }
    });

    // the presentation
    final Presentation presentation = new Presentation();

    // connect presentation and translation
    wristwatch.addPropertyChangeListener(presentation);
    presentation.addPropertyChangeListener(wristwatch);
    
    // create frame for presentation
    final JFrame frame = new JFrame("WristWatch");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(presentation);

    // start wristwatch
    Scheduling.getScheduler().start(wristwatch);

    // start timer
    timer.start();

    frame.pack();
    frame.setVisible(true);
  }
}