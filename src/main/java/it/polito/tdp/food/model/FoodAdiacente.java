package it.polito.tdp.food.model;

public class FoodAdiacente implements Comparable<FoodAdiacente>{

	private Food food;
	private double peso;
	public FoodAdiacente(Food food, double peso) {
		super();
		this.food = food;
		this.peso = peso;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(FoodAdiacente o) {
		return Double.compare(o.peso, this.peso);
	}
	
}
