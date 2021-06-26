/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.FoodAdiacente;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Creazione grafo...");
    	String pString = this.txtPorzioni.getText();
    	if (pString.isEmpty()) { //superfluo, basta il try e catch
    		this.txtResult.setText("Inserire un numero minimo max di porzioni");
    		return;
    	}
    	int p; //o int o float...
    	try {
    		p=Integer.parseInt(pString);
    	} catch(NumberFormatException e) {
    		this.txtResult.setText("Inseire un valore numerico come numero max di porzioni ");
    		return;
    	}
    	if (p<0) { //eventuali altri controlli
    		this.txtResult.setText("Inserire un numero positivo come numero max di porzioni");
    		return;
    	}
    	this.model.creaGrafo(p);
    	this.txtResult.setText("GRAFO CREATO:\n");
    	this.txtResult.appendText("# Vertici: "+model.getNumVertici() );
    	this.txtResult.appendText("\n# Archi: "+model.getNumArchi() );
    	this.boxFood.getItems().clear(); //pulisco le varie comboBox dipendenti dal grafo
    	this.boxFood.getItems().addAll(model.getVertici()); //creo le varie comboBox dipendenti dal grafo
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Analisi calorie...");
    	if (!model.isGrafoCreato()) {
    		this.txtResult.setText("Errore, creare prima il grafo");
    		return;
    	}
    	Food food= this.boxFood.getValue();
    	if (food==null) {
    		this.txtResult.setText("Errore, selezionare un cibo");
    		return;
    	}
    	List<FoodAdiacente> elenco = model.adiacenti(food);
    	this.txtResult.appendText("Adiacenti migliori: \n");
    	int i=1;
    	for (FoodAdiacente f : elenco) {
    		if (i==5)
    			break;
    		this.txtResult.appendText(f.getFood().getDisplay_name()+" "+f.getPeso()+"\n");
    		i++;
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Simulazione...");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
