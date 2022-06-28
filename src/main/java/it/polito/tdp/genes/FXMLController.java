package it.polito.tdp.genes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.genes.model.ArcoGrafo;
import it.polito.tdp.genes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnStatistiche;

    @FXML
    private Button btnRicerca;

    @FXML
    private ComboBox<String> boxLocalizzazione;

    @FXML
    private TextArea txtResult;

    @FXML
    void doRicerca(ActionEvent event) {

    	txtResult.clear();
    	if(!this.model.grafoCreato()) {
    		txtResult.setText("Crea prima il grafo!");
    		return;
    	}
    	String localization = this.boxLocalizzazione.getValue();
    	if(localization==null) {
    		txtResult.setText("Seleziona una localizzazione!");
    		return;
    	}
    	
    	this.model.init(localization);
    	txtResult.appendText("CAMMINO TROVATO : \n");
    	for(ArcoGrafo e : this.model.getBestCammino()) {
    		txtResult.appendText(e.toString()+"\n");
    	}
    }

    @FXML
    void doStatistiche(ActionEvent event) {

    	txtResult.clear();
    	this.model.creaGrafo();
    	txtResult.appendText("Grafo creato!\n");
    	txtResult.appendText("# VERTICI : "+this.model.getNVertici()+"\n");
    	txtResult.appendText("# ARCHI : "+this.model.getNArchi()+"\n");
    	
    	String localization = this.boxLocalizzazione.getValue();
    	if(localization!=null) {
    		List<ArcoGrafo> result = this.model.getAdiacenze(localization);
    		txtResult.appendText("Adiacenti a : "+localization+"\n");
    		for(ArcoGrafo e : result) {
    			txtResult.appendText(e.toString()+"\n");
    		}
    	}
    }

    @FXML
    void initialize() {
        assert btnStatistiche != null : "fx:id=\"btnStatistiche\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRicerca != null : "fx:id=\"btnRicerca\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxLocalizzazione != null : "fx:id=\"boxLocalizzazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxLocalizzazione.getItems().addAll(this.model.getVertici());
	}
}
