package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private SimpleWeightedGraph<Food, DefaultWeightedEdge> grafo;
	private FoodDao dao;
	private Map<Integer, Food> idMap;
	private boolean grafoCreato;
	
	private Simulator sim;
	
	public Model() {
		this.dao = new FoodDao();
		this.idMap= new HashMap<>();
		this.dao.loadIdMap(idMap);
		this.grafoCreato=false;
		this.sim= new Simulator();
	}
	
	public void creaGrafo(int porzioni) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici(idMap,porzioni));
		for (Adiacenza a : this.dao.getAdiacenze(idMap)) {
			if (grafo.vertexSet().contains(a.getF1()) && grafo.vertexSet().contains(a.getF2()) && a.getPeso()!=0) {
				Graphs.addEdgeWithVertices(grafo, a.getF1(), a.getF2(), (double)a.getPeso());
			}
		}
		this.grafoCreato=true;
		//this.ci= new ConnectivityInspector<>(grafo);
	}
	
	public boolean isGrafoCreato() {
		return grafoCreato;
	}

	public int getNumVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return grafo.edgeSet().size();
	}

	public Set<Food> getVertici(){
		return grafo.vertexSet();
	}
	
	public List<FoodAdiacente> adiacenti(Food food){ 
		List<FoodAdiacente> ris= new ArrayList<>();
		for (DefaultWeightedEdge e : grafo.edgesOf(food)) {
			FoodAdiacente f = new FoodAdiacente(Graphs.getOppositeVertex(grafo, e, food), grafo.getEdgeWeight(e));
			ris.add(f);
		}
		Collections.sort(ris);
		return ris;
	}
	
	public void simula(int k, Food partenza) {
		sim.init(k, grafo, partenza);
		sim.run();
	}
	
	public int getNumCibiPreparati() {
		return sim.getNumCibi();
	}
	
	public double getTempoTotale() {
		return sim.getTempoTotale();
	}
	
}
