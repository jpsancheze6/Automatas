package automata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField txtExpresion, txtCadena;
    @FXML
    private Button btnComprobar;
    @FXML
    private Label lblAutomata;

    @FXML
    private void comprobarExpresion() throws IOException {
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
        } else {
            try {
                //Ir a la ventana para mostrar el autómata
                //Colocar la expresión en un archivo txt
                RandomAccessFile raf = new RandomAccessFile("expresion.txt", "rw");
                raf.writeInt(texto.length());
                raf.writeChars(texto);
                raf.close();
                try {
                    //Llamar a una nueva ventana
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLAutomata.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Comprobación de cadenas");
                    stage.setScene(new Scene(root1));
                    stage.show();
                    //Cerrar ventana actual
                    Stage actual = (Stage) btnComprobar.getScene().getWindow();
                    actual.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //<------------------------------------------------------------------------>
    //Controlador del segundo fxml -> FXMLAutomata.fxml
    @FXML
    private void comprobarCadena() throws IOException {
        try {
            RandomAccessFile raf = new RandomAccessFile("expresion.txt", "rw");
            int cont = raf.readInt();
            String a = "";
            for (int x = 0; x < cont; x++) {
                a = a + raf.readChar();
            }
            raf.close();
            lblAutomata.setText(a);
            String texto = txtCadena.getText();
            if (Pattern.matches(a, texto) == true) {
                //Mensaje de que la palabra es correcta
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Correcto");
                alert.setHeaderText("Palabra aceptada");
                alert.setContentText("El autómata ha reconocido la palabra ingresada.");
                alert.showAndWait();
            } else {
                //Mensaje de que la palabra es incorrecta
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Error");
                alert.setHeaderText("Revisar sintaxis");
                alert.setContentText("El autómata no reconoce la palabra ingresada.");
                alert.showAndWait();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //<------------------------------------------------------------------------>
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}