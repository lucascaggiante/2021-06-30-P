package it.polito.tdp.genes.db;

import java.util.List;

import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Model;

public class TestDao {

	public static void main(String[] args) {

		GenesDao dao = new GenesDao();
	//	List<Genes> list = dao.getAllGenes();
		Model model= new Model();
	
		
		String result = model.creaGrafo();
		System.out.println(result);
	}
	
}
