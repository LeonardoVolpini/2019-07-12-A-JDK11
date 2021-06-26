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
	//private List<Adiacenza> adiacenze; //puo essere superfluo
	private boolean grafoCreato;
	//private ConnectivityInspector<Vertice, DefaultWeightedEdge> ci; //se chiede roba connessa ad un vertice
	
	//private List<Vertice> percorsoBest; //nel caso di ricorsione per percorso max
	//private Integer pesoMax; //peso del percorso
	
	public Model() {
		this.dao = new FoodDao();
		this.idMap= new HashMap<>();
		this.dao.loadIdMap(idMap);
		//this.adiacenze= new ArrayList<>();
		this.grafoCreato=false;
		//this.percorsoBest= new ArrayList<>(); //per ricorsione
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
}
