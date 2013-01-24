package fr.istic.mmm.adeagenda.calendar;

import java.util.List;

import net.fortuna.ical4j.model.DateTime;
import fr.istic.mmm.adeagenda.model.Event;

/**
 * @author Clément Hardouin
 *
 */
public interface ICalendarReader {

	/**
	 * Return the list of all events
	 * 
	 * @return the list of all events
	 */
	public List<Event> allEvents();

	/**
	 * Return the list of events taking place at the current system date
	 * 
	 * @return the list of events
	 */
	public List<Event> eventsOfTheDay();

	/**
	 * Return the list of events taking place at the given date
	 * 
	 * @param date
	 * @return the list of events
	 */
	public List<Event> eventsOfTheDay(DateTime date);

	/**
	 * Return the list of events taking place during a week
	 * 
	 * @param date
	 *            of a day in the week
	 * @return the list of events
	 */
	public List<Event> eventsOfTheWeek(DateTime date);

	/**
	 * Return the event taking place at the given date and time
	 * 
	 * @param datetime
	 * @return the event
	 */
	public Event getEventAt(DateTime datetime);

	/**
	 * Return the event taking place at the system date and time
	 * 
	 * @return the event
	 */
	public Event getCurrentEvent();

}
