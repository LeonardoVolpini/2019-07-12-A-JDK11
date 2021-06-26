package it.polito.tdp.food.model;

public class Event implements Comparable<Event>{

	public enum EventType{
		INIZIO_PREPARAZIONE,
		FINE_PREPARAZIONE;
	}
	private double time;
	private EventType type;
	private Food food;
	private Stazione stazione;
	public Event(double time, EventType type, Food food, Stazione stazione) {
		super();
		this.time = time;
		this.type = type;
		this.food = food;
		this.stazione = stazione;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
	public Stazione getStazione() {
		return stazione;
	}
	public void setStazione(Stazione stazione) {
		this.stazione = stazione;
	}
	@Override
	public int compareTo(Event o) {
		return Double.compare(this.time, o.time);
	}
	
	
	
}
