package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	private Graph<String, DefaultWeightedEdge> grafo ;
	private List<String> vertici ;
	private List<String> percorsoBest;
	private Double controlloPeso;
	
	public String creaGrafo() {
		
	this.grafo= new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	GenesDao dao = new GenesDao();
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
	
	public void percorsoLungo(String localizzazione) {
		this.percorsoBest = null;
		this.controlloPeso = 0.0;
		List<String> parziale = new ArrayList<String>();
		
		parziale.add(localizzazione);
		
		cerca(parziale,1,localizzazione,0.0);
		
		
	}

	private void cerca(List<String> parziale, int livello,String localizzazione, Double pesoParziale) {
			
		
		if(pesoParziale>controlloPeso) {
			this.controlloPeso=pesoParziale;
			this.percorsoBest=parziale;
		}
		
		
		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(localizzazione)) {
			String prossimo = Graphs.getOppositeVertex(this.grafo, e, localizzazione);
			if(!parziale.contains(prossimo)) { // evita i cicli
				parziale.add(prossimo);
				Double newPeso = pesoParziale+this.grafo.getEdgeWeight(e);
				cerca(parziale, livello + 1, prossimo, newPeso);
				parziale.remove(parziale.size()-1) ;
			}
		}
		
	}

	public List<String> getPercorsoLungo() {
		// TODO Auto-generated method stub
		return this.percorsoBest;
	}
}

