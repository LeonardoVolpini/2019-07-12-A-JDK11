package it.polito.tdp.food.model;

public class Stazione {

	private Integer numStazione;
	private Food food;
	private boolean libera;
	public Stazione(Integer numStazione, Food food, boolean stato) {
		super();
		this.numStazione = numStazione;
		this.food = food;
		this.libera=stato;
	}
	public Integer getNumStazione() {
		return numStazione;
	}
	public void setNumStazione(Integer numStazione) {
		this.numStazione = numStazione;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
	public boolean isLibera() {
		return libera;
	}
	public void setLibera(boolean libera) {
		this.libera = libera;
	}
	
	
}
