package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.ArcoGrafo;
import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Interactions;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}

	public List<String> getAllLocalizations() {
		String sql = "SELECT DISTINCT(localization)\n"
				+ "FROM classification";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				String localization = res.getString("localization");
				result.add(localization);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<ArcoGrafo> getArchi() {
		String sql = "SELECT c1.Localization as c1, c2.Localization as c2, COUNT(DISTINCT(i1.`Type`))as peso\n"
				+ "FROM classification c1, interactions i1, classification c2, interactions i2\n"
				+ "WHERE c1.GeneID = i1.GeneID1\n"
				+ "	AND c2.GeneID = i2.GeneID2\n"
				+ "	AND c1.Localization > c2.Localization\n"
				+ "	AND i1.`Type` =i2.`Type`\n"
				+ "	AND c1.GeneID=i1.GeneID1\n"
				+ "GROUP BY c1.Localization, c2.Localization";
		List<ArcoGrafo> result = new ArrayList<ArcoGrafo>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				ArcoGrafo temp = new ArcoGrafo(res.getString("c1"), 
												res.getString("c2"),
												res.getInt("peso"));
				
				result.add(temp);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	


	
}
