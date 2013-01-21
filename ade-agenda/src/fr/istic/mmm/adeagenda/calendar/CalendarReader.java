package fr.istic.mmm.adeagenda.calendar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.filter.Rule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.component.VEvent;
import fr.istic.mmm.adeagenda.model.Event;

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
	
	public List<Event> eventsOfTheDay(){
		java.util.Calendar today = java.util.Calendar.getInstance();
		today.set(java.util.Calendar.HOUR_OF_DAY, 0);
		today.clear(java.util.Calendar.MINUTE);
		today.clear(java.util.Calendar.SECOND);
		// create a period starting now with a duration of one (1) day..
		Period period = new Period(new DateTime(today.getTime()), new Dur(1, 0, 0, 0));
		Rule rule = new PeriodRule(period);
		Rule[] rules = {rule};
		Filter filter = new Filter(rules, Filter.MATCH_ALL);
		
		Collection<VEvent> col = filter.filter(calendar.getComponents(Component.VEVENT));
		List<Event> events = new ArrayList<Event>();
		for (VEvent e : col){
			events.add(new Event(e.getName(),e.getStartDate().getDate(), e.getEndDate().getDate(),e.getLocation().getValue(),e.getDescription().getValue()));
		}
		
		return events;
	}
	
	public List<Event> allEvents(){
		
		Collection<VEvent> col = calendar.getComponents(Component.VEVENT);
		List<Event> events = new ArrayList<Event>();
		for (VEvent e : col){
			events.add(new Event(e.getName(),e.getStartDate().getDate(), e.getEndDate().getDate(),e.getLocation().toString(),e.getDescription().toString()));
		}
		return events;
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
