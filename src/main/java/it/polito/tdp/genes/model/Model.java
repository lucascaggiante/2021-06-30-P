package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;


public class Model {
	private Graph<String, DefaultWeightedEdge> grafo ;
	private List<String> vertici ;
	private List<String> best;
	private ConnectivityInspector<String,DefaultWeightedEdge> ispettore;
	private int maxLenght;
	private GenesDao dao;
	
	public Model() {
		this.dao=new GenesDao();
	}
	
	public String creaGrafo() {
		
	this.grafo= new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	
	this.vertici=dao.getAllLocalizations();
	
	Graphs.addAllVertices(this.grafo, this.vertici) ;
	
	List<ArcoGrafo> archi = dao.getArchi() ;
	
	
	for(ArcoGrafo arco : archi) {
		Graphs.addEdge(this.grafo,
				(arco.getLocalization1()),
				(arco.getLocalization2()), 
				arco.getPeso()*1.0) ;
	}

	return String.format("Grafo creato con %d vertici e %d archi\n",
			this.grafo.vertexSet().size(),
			this.grafo.edgeSet().size()) ;
}

	public List<String> getLocalizzazioni() {
		return this.vertici;
	}
	
	public String getVicini(String localizzazione) {
		String result="";
		List<String> vicini = Graphs.neighborListOf(this.grafo, localizzazione);
		for(String vicino : vicini) {
			DefaultWeightedEdge e = this.grafo.getEdge(localizzazione, vicino);
			Double peso= this.grafo.getEdgeWeight(e);
			result=result+vicino+" "+peso+"\n";
		}
		return "Adiacenti a "+localizzazione+":\n"+result+"\n";
	}
	
public List<String> init(String localization){
		
		this.best = new ArrayList<String>();
		List<String> parziale = new ArrayList<String>();
		this.ispettore = new ConnectivityInspector<String,DefaultWeightedEdge>(this.grafo);
		List<String> connessi = new ArrayList(this.ispettore.connectedSetOf(localization));
		this.maxLenght = Integer.MIN_VALUE;
		connessi.remove(localization);
		
		parziale.add(localization);
		
		cercaSequenza(parziale,0,connessi);
		
		return best;
	}
	
	public void cercaSequenza(List<String> parziale,int livello, List<String> connessi) {
		
		if(calcolaLunghezza(parziale)>this.maxLenght) {
			this.best = new ArrayList<String>(parziale);
			this.maxLenght = calcolaLunghezza(parziale);
		}
		
		if(livello==connessi.size()-1) {
			return;
		}
		
		if(this.grafo.containsEdge(parziale.get(parziale.size()-1),connessi.get(livello))) {
			parziale.add(connessi.get(livello));
			cercaSequenza(parziale,livello+1,connessi);
			
			parziale.remove(parziale.size()-1);
			cercaSequenza(parziale,livello+1,connessi);
		}
		
		cercaSequenza(parziale,livello+1,connessi);
		
	}
	private Double pesoCammino(List<String> parziale) {
		double peso = 0.0 ;
		for(int i=1; i<parziale.size(); i++) {
			double p = this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(i-1), parziale.get(i))) ;
			peso += p ;
		}
		return peso ;
	}

	public List<String> getPercorsoLungo() {
		// TODO Auto-generated method stub
		return this.best;
	}
	
private int calcolaLunghezza(List<String> localizations) {
		
		int lunghezza=0;
		for(int i=0; i<localizations.size()-1;i++) {
			lunghezza+=this.grafo.getEdgeWeight(this.grafo.getEdge(localizations.get(i),localizations.get(i+1)));
		}
		return lunghezza;
	}

	public boolean grafoCreato() {
		if(this.grafo==null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public List<ArcoGrafo> getBestCammino(){
		
		List<ArcoGrafo> cammino = new ArrayList<ArcoGrafo>();
		for(int i=0; i<best.size()-1;i++) {
			cammino.add(new ArcoGrafo(best.get(i),best.get(i+1),(int) this.grafo.getEdgeWeight(this.grafo.getEdge(best.get(i),best.get(i+1)))));
		}
		
		return cammino;
	}
	
	public List<String> getVertici(){
		return this.dao.getAllLocalizations();
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<ArcoGrafo> getAdiacenze(String localization){
		
		List<String> adiacenze = Graphs.neighborListOf(this.grafo, localization);
		List<ArcoGrafo> archi = new ArrayList<ArcoGrafo>();
		
		for(String a : adiacenze) {
			if(this.grafo.containsEdge(localization, a)) {
				archi.add(new ArcoGrafo(localization,a,(int) this.grafo.getEdgeWeight(this.grafo.getEdge(localization, a))));
			}
		}
		
		return archi;
	}
}

