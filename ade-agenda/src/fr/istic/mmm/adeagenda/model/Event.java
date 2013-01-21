package fr.istic.mmm.adeagenda.model;

import java.util.Date;

public class Event {

	private String name;
	private Date start;
	private Date end;
	private String place;
	private GPSPosition position;
	private String description;
	
	/**
	 * Creates a resources with the given value.
	 * If end > start then the two dates will be swap resulting in start being < end
	 * @param name name of the resource
	 * @param start starting time
	 * @param end ending time
	 * @param place where the resource takes place
	 * @param description informations about the resource
	 */
	public Event(String name, Date start, Date end, String place, String description) {
		this.name = name;
		
		// check if end > start, swap them if not
		if (start.compareTo(end) > 0) {
			Date tmp = start;
			start = end;
			end = tmp;
		}
		
		this.start = start;
		this.end = end;
		this.place = place;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
	
	public GPSPosition getPosition() {
		return this.position;
	}
	
	public void setPosition(GPSPosition pos) {
		this.position = pos;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getDuration() {
		if (start != null && end != null) {
			return end.getTime() - start.getTime();
		}
		return 0;
	}
	
	@Override
	public String toString() {
		return name+" - "+place+" ("+start+", "+end+")";
	}
}
