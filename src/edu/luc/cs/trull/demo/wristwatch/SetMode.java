package edu.luc.cs.trull.demo.wristwatch;

import java.beans.PropertyChangeEvent;

import edu.luc.cs.trull.Cycle;
import edu.luc.cs.trull.EmitComponent;

/**
 * setmode =
 *   cycle RESET
 *     setHour
 *     setMinute
 *     setDay
 *     setMonth
 */
public class SetMode extends Cycle implements EventLabels {
	public SetMode(DateModel d) {
  		setCycleEvent(RESET);
		addComponent(new SetHour(d));
		addComponent(new SetMinute(d));
		addComponent(new SetDay(d));
		addComponent(new SetMonth(d));
	}
}

/**
 * ( emit HOUR
 * ; emit NOW(time / 60)
 * ; loop
 *  	START -> emit NOW(time = (time + 3600) % ...)
)
*/

class SetHour extends EmitComponent implements EventLabels, DateConstants {

	private DateModel data;
	
	public SetHour(DateModel data) {
		this.data = data;
	}

	public void start(PropertyChangeEvent incoming) { 
		scheduleEvent(HOUR);
		scheduleEvent(NOW, new Integer(data.getTime() / SEC_PER_MIN));
	}
	
	public void stop() { }

	public void propertyChange(final PropertyChangeEvent event) {
		if (START.equals(event.getPropertyName())) {
			data.setTime((data.getTime() + SEC_PER_HOUR) % SEC_PER_DAY);					
			scheduleEvent(NOW, new Integer(data.getTime() / SEC_PER_MIN));
		}
	}
}

/**
 * ( emit MIN
 *     ; emit NOW(time / 60)
 *     ; loop
 *         START -> emit NOW(time = time / 3600 * 3600 + (time + 60) % ...)
 *     )
 */
class SetMinute extends EmitComponent implements EventLabels, DateConstants {
	
	private DateModel data;
	
	public SetMinute(DateModel data) {
		this.data = data;
	}

	public void start(PropertyChangeEvent incoming) { 
		scheduleEvent(MIN);
		scheduleEvent(NOW, new Integer(data.getTime() / SEC_PER_MIN));
	}
	
	public void stop() { }

	public void propertyChange(final PropertyChangeEvent event) {
		if (START.equals(event.getPropertyName())) {
			data.setTime(data.getTime() / SEC_PER_HOUR * SEC_PER_HOUR
					+ (data.getTime() + SEC_PER_MIN) % SEC_PER_HOUR);
			scheduleEvent(NOW, new Integer(data.getTime() / SEC_PER_MIN));
		}
	}
}

/**
 * ( emit DAY
 *     ; emit NOW(60 * month + day)
 *     ; loop
 *         START -> emit NOW(60 * month + (day = ...))
 *     )	
 */
class SetDay extends EmitComponent implements EventLabels, DateConstants {
	
	private DateModel data;
	
  public SetDay(DateModel data) {
		this.data = data;
	}

	public void start(PropertyChangeEvent incoming) { 
		scheduleEvent(DAY);
		scheduleEvent(NOW, new Integer(SEC_PER_MIN * data.getMonth() + data.getDay()));
	}
	
	public void stop() { }

	public void propertyChange(final PropertyChangeEvent event) {
		if (START.equals(event.getPropertyName())) {
			data.setDay(data.getDay() % data.daysPerMonth() + 1);
			scheduleEvent(NOW, new Integer(SEC_PER_MIN * data.getMonth() + data.getDay()));
		}
	}
}

/**
 *     ( emit MONTH
 *     ; emit NOW(60 * month + day)
 *     ; loop
 *         START -> emit NOW(60 * (month = ...) + day)
 *     )
*/
class SetMonth extends EmitComponent implements EventLabels, DateConstants {
	
	private DateModel data;
	
	public SetMonth(DateModel data) {
		this.data = data;
	}
	
	public void start(PropertyChangeEvent incoming) { 
		scheduleEvent(MONTH);
		scheduleEvent(NOW, new Integer(SEC_PER_MIN * data.getMonth() + data.getDay()));
	}
	
	public void stop() { }

	public void propertyChange(final PropertyChangeEvent event) {
		if (START.equals(event.getPropertyName())) {
			data.setMonth(data.getMonth() % MONTHS_PER_YEAR + 1);
			data.setDay((data.getDay() - 1) % data.daysPerMonth() + 1);
			scheduleEvent(NOW, new Integer(SEC_PER_MIN * data.getMonth() + data.getDay()));
		}
	}
}
