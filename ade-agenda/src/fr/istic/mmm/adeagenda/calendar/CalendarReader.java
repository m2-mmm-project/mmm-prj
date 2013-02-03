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

/**
 * @author Clément Hardouin
 *
 */
public class CalendarReader implements ICalendarReader {

	private Calendar calendar;

	/**
	 * @param c
	 */
	public CalendarReader(Calendar c) {
		calendar = c;
	}

	/**
	 * @param in
	 */
	public CalendarReader(InputStream in) {
		CalendarBuilder builder = new CalendarBuilder();
		try {
			this.calendar = builder.build(in);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.istic.mmm.adeagenda.calendar.ICalendarReader#getEventAt(net.fortuna.ical4j.model.DateTime)
	 */
	@Override
	public Event getEventAt(DateTime datetime) {
		// create a period starting now with a duration of one (1) day..
		Period period = new Period(datetime, new Dur(0,3,0,0));
		Rule rule = new PeriodRule(period);
		Rule[] rules = { rule };
		Filter filter = new Filter(rules, Filter.MATCH_ALL);

		Collection<VEvent> col = filter.filter(calendar.getComponents(Component.VEVENT));
		VEvent e = col.iterator().next();
		
		return new Event(e.getSummary().getValue(), e.getStartDate()
					.getDate(), e.getEndDate().getDate(), e.getLocation()
					.getValue(), e.getDescription().getValue());
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.istic.mmm.adeagenda.calendar.ICalendarReader#getCurrentEvent()
	 */
	@Override
	public Event getCurrentEvent() {
		java.util.Calendar now = java.util.Calendar.getInstance();
		// create a period starting now with a duration of one (1) day..
		Period period = new Period(new DateTime(now.getTime()), new Dur(0,3,0,0));
		Rule rule = new PeriodRule(period);
		Rule[] rules = { rule };
		Filter filter = new Filter(rules, Filter.MATCH_ALL);

		Collection<VEvent> col = filter.filter(calendar.getComponents(Component.VEVENT));
		VEvent e = col.iterator().next();
		
		return new Event(e.getSummary().getValue(), e.getStartDate()
					.getDate(), e.getEndDate().getDate(), e.getLocation()
					.getValue(), e.getDescription().getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.istic.mmm.adeagenda.calendar.ICalendarReader#allEvents()
	 */
	@Override
	public List<Event> allEvents() {

		Collection<VEvent> col = calendar.getComponents(Component.VEVENT);
		List<Event> events = new ArrayList<Event>();
		for (VEvent e : col) {
			events.add(new Event(e.getSummary().getValue(), e.getStartDate()
					.getDate(), e.getEndDate().getDate(), e.getLocation()
					.toString(), e.getDescription().toString()));
		}
		return events;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.istic.mmm.adeagenda.calendar.ICalendarReader#eventsOfTheDay()
	 */
	@Override
	public List<Event> eventsOfTheDay() {
		java.util.Calendar today = java.util.Calendar.getInstance();
		today.set(java.util.Calendar.HOUR_OF_DAY, 0);
		today.clear(java.util.Calendar.MINUTE);
		today.clear(java.util.Calendar.SECOND);
		// create a period starting now with a duration of one (1) day..
		Period period = new Period(new DateTime(today.getTime()), new Dur(1, 0, 0, 0));
		Rule rule = new PeriodRule(period);
		Rule[] rules = { rule };
		Filter filter = new Filter(rules, Filter.MATCH_ALL);

		Collection<VEvent> col = filter.filter(calendar.getComponents(Component.VEVENT));
		List<Event> events = new ArrayList<Event>();
		for (VEvent e : col) {
			events.add(new Event(e.getSummary().getValue(), e.getStartDate()
					.getDate(), e.getEndDate().getDate(), e.getLocation()
					.getValue(), e.getDescription().getValue()));
		}

		return events;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.istic.mmm.adeagenda.calendar.ICalendarReader#eventsOfTheDay(net.fortuna
	 * .ical4j.model.DateTime)
	 */
	@Override
	public List<Event> eventsOfTheDay(DateTime date) {
		// create a period starting now with a duration of one (1) day..
		Period period = new Period(date, new Dur(0, 24, 0, 0));
		Rule rule = new PeriodRule(period);
		Rule[] rules = { rule };
		Filter filter = new Filter(rules, Filter.MATCH_ALL);

		Collection<VEvent> col = filter.filter(calendar.getComponents(Component.VEVENT));
		List<Event> events = new ArrayList<Event>();
		for (VEvent e : col) {
			events.add(new Event(e.getSummary().getValue(), e.getStartDate()
					.getDate(), e.getEndDate().getDate(), e.getLocation()
					.getValue(), e.getDescription().getValue()));
		}

		return events;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.istic.mmm.adeagenda.calendar.ICalendarReader#eventsOfTheWeek(net.fortuna
	 * .ical4j.model.DateTime)
	 */
	@Override
	public List<Event> eventsOfTheWeek(DateTime date) {
		//TODO : Not tested !!
		java.util.Calendar lastMonday = java.util.Calendar.getInstance();
		lastMonday.set(java.util.Calendar.DAY_OF_WEEK, 0);
		lastMonday.set(java.util.Calendar.HOUR_OF_DAY, 0);
		lastMonday.clear(java.util.Calendar.MINUTE);
		lastMonday.clear(java.util.Calendar.SECOND);
		// create a period starting now with a duration of one (1) day..
		Period period = new Period(new DateTime(lastMonday.getTime()), new Dur(7, 0, 0, 0));
		Rule rule = new PeriodRule(period);
		Rule[] rules = { rule };
		Filter filter = new Filter(rules, Filter.MATCH_ALL);

		Collection<VEvent> col = filter.filter(calendar.getComponents(Component.VEVENT));
		List<Event> events = new ArrayList<Event>();
		for (VEvent e : col) {
			events.add(new Event(e.getSummary().getValue(), e.getStartDate()
					.getDate(), e.getEndDate().getDate(), e.getLocation()
					.getValue(), e.getDescription().getValue()));
		}

		return events;
	}
}
