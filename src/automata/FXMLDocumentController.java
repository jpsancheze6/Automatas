package automata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Label lblAutomata, lblAutomataImagen;
    @FXML
    private ImageView imgAutomata;
    @FXML
    private TextField txtCadena;

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
            colocarImagen();
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

    private void colocarImagen() {
        //Prueba del archivo
        File f = new File("grafo.jpg");
        String ruta = f.getPath();
        //Colocar imagen
        File file = new File(ruta);
        Image image = new Image(file.toURI().toString());
        imgAutomata.setImage(image);
        Image img = imgAutomata.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;
            double ratioX = imgAutomata.getFitWidth() / img.getWidth();
            double ratioY = imgAutomata.getFitHeight() / img.getHeight();
            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }
            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;
            imgAutomata.setX((imgAutomata.getFitWidth() - w) / 2);
            imgAutomata.setY((imgAutomata.getFitHeight() - h) / 2);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colocarImagen();
        try {
            // TODO
            RandomAccessFile raf = new RandomAccessFile("expresion.txt", "rw");
            int cont = raf.readInt();
            String a = "";
            for (int x = 0; x < cont; x++) {
                a = a + raf.readChar();
            }
            raf.close();
            lblAutomata.setText(a);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
