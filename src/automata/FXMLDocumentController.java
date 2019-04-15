package automata;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TextField txtExpresion;
    
    @FXML
    private void comprobarExpresion(){
        String texto = txtExpresion.getText();
        //Cadena que da el formato de la expresión regular
        String alfabeto = "\\w|\\,|\\.|\\;|\\-|\\_|\\{|\\}|\\+|\\*|\\|";
        String expIndividual = "(" + alfabeto + ")+";
        String expParentesis = "\\({1}" + expIndividual + "\\){1}";
        //Unión de todas las expresiones posibles
        String expRegular = "(" + expIndividual + "|" + expParentesis + ")*";
        // Lo siguiente devuelve true
        boolean bandera;
        bandera = Pattern.matches(expRegular, texto);
        if (bandera == false) {
            //Mostrar mensaje de que la expresión es invalida
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error");
            alert.setHeaderText("Revisar sintaxis");
            alert.setContentText("La sintaxis ingresada es incorrecta");
            alert.showAndWait();
        }else{
            //Ir a la ventana para mostrar el autómata
            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
