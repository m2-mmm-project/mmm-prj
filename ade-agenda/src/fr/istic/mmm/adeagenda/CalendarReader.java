package fr.istic.mmm.adeagenda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.widget.Toast;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;

public class CalendarReader {

	private Calendar calendar;
	
	public CalendarReader(Calendar c){
		calendar = c;
	}
	
	public CalendarReader(InputStream in){
		CalendarBuilder builder = new CalendarBuilder();
		try {
			this.calendar = builder.build(in);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
	
	public void example(){

        VEvent event = (VEvent)calendar.getComponents().get(0);
		
        event.getSummary();
		event.getDescription();
        
        event.getStartDate();
        event.getEndDate();
        event.getDuration();
        
        event.getLocation();
        event.getGeographicPos();
           
	}
	
}
