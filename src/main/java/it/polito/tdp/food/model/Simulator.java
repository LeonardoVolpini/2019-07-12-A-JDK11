package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.model.Event.EventType;
import it.polito.tdp.food.model.Food.Stato;

public class Simulator {

	//modello del mondo 
	private SimpleWeightedGraph<Food, DefaultWeightedEdge> grafo;
	//private List<Food> lista; //spesso serve qualche lista dei vertici o di qualcos altro
	private List<FoodAdiacente> adiacenti;
	private List<Stazione> stazioni;
	
	//coda degli eventi
	private PriorityQueue<Event> queue;
	
	//parametri in input
	private int k; //indica il numero di stazioni di lavoro
	private Food partenza;
	
	//parametri in output
	private int numCibi;
	private double tempoTotale;
	
	public void init(int k, SimpleWeightedGraph<Food, DefaultWeightedEdge> g, Food inizio) {
		this.grafo=g;
		this.partenza=inizio;
		this.k=k;
		this.adiacenti= new ArrayList<>();
		this.adiacenti=this.trovaAdiacenti(partenza);
		this.stazioni= new ArrayList<>();
		for (int i=1; i<=k; i++)
			stazioni.add(new Stazione(i,null,true));
		this.queue= new PriorityQueue<>();
		
		this.numCibi=0;
		this.tempoTotale=0.0;
		
		//LocalDate oggi=this.inizio;
		int inseriti=0;
		for (FoodAdiacente f : adiacenti) {
			if (inseriti==k)
				break;
			Stazione s= stazioni.get(inseriti);
			Event e = new Event(f.getPeso(),EventType.FINE_PREPARAZIONE,f.getFood(),s);
			s.setLibera(false);
			s.setFood(f.getFood());
			this.queue.add(e);
			inseriti++;
		}
	}
	
	public void run() {	
		while(!this.queue.isEmpty()) {
			Event e = queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		double tempo= e.getTime();
		Stazione stazione = e.getStazione();
		Food food= e.getFood();
		
		switch(e.getType()) {
		case INIZIO_PREPARAZIONE:
			this.adiacenti= this.trovaAdiacenti(food);
			FoodAdiacente prossimo= null;
			for(FoodAdiacente f: adiacenti) {
				if(f.getFood().getStato().equals(Stato.DA_PREPARARE)) {
					prossimo=f;
					break;
				}
			}
			if (prossimo!=null) { //se uguale a null non faccia nulla e termina la simulazione
				prossimo.getFood().setStato(Stato.PREPARAZIONE_IN_CORSO);
				stazione.setLibera(false);
				stazione.setFood(prossimo.getFood());
				this.queue.add( new Event(tempo+prossimo.getPeso(),EventType.FINE_PREPARAZIONE,prossimo.getFood(),stazione) );
			}
			break;
		case FINE_PREPARAZIONE:
			this.numCibi++;
			this.tempoTotale+=e.getTime();
			stazione.setLibera(true);
			food.setStato(Stato.PREPARAZIONE_FINITA);
			this.queue.add(new Event (tempo,EventType.INIZIO_PREPARAZIONE,food,stazione)); //stesso food per calcolare il prossimo food ad inizio evento INIZIO_PREPARAZIONE
			break;
		}
		
	}
	
	private List<FoodAdiacente> trovaAdiacenti(Food food){ 
		List<FoodAdiacente> ris= new ArrayList<>();
		for (DefaultWeightedEdge e : grafo.edgesOf(food)) {
			FoodAdiacente f = new FoodAdiacente(Graphs.getOppositeVertex(grafo, e, food), grafo.getEdgeWeight(e));
			ris.add(f);
		}
		Collections.sort(ris);
		return ris;
	}

	public int getNumCibi() {
		return numCibi;
	}

	public double getTempoTotale() {
		return tempoTotale;
	}

	
	
	
}
