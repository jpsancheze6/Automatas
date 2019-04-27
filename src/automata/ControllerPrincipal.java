package automata;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerPrincipal implements Initializable {

    static String rutaDot = "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe";//cambiar
    static String textoBase = "codigo.txt"; //cambiar
    static String imagen = "grafo.jpg";//cambiar
    static String jp = "-Tjpg";
    static String O = "-o";

    @FXML
    private TextField txtExpresion;
    @FXML
    private Button btnComprobar;

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
                //Crea el archivo jpg con el automata
                try {
                    expresion(texto);
                    try {
                        //Cerrar ventana actual
                        Stage actual = (Stage) btnComprobar.getScene().getWindow();
                        actual.close();
                        //Llamar a una nueva ventana
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLAutomata.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setTitle("Comprobación de cadenas");
                        stage.setScene(new Scene(root1));
                        stage.show();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } catch (Exception e) {
                    //Mensaje de que la palabra es incorrecta
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initStyle(StageStyle.UTILITY);
                    alert.setTitle("Error");
                    alert.setHeaderText("Revisar sintaxis");
                    alert.setContentText("No es una expresión regular válida.");
                    alert.showAndWait();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ControllerPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //<------------------------------------------------------------------------>
    @FXML
    public void expresion(String expresion1) {
        RegExp r = new RegExp(expresion1);
        Automaton aut = r.toAutomaton();
        String grafo = aut.toDot();
        System.out.println(grafo);
        creararchivo(grafo);
    }

    public static void creararchivo(String grafo) {
        borrar(imagen);
        borrar(textoBase);
        try {
            FileWriter archivo = new FileWriter(textoBase);
            PrintWriter pw = new PrintWriter(archivo);
            grafo = grafo.replace("u0020", "Ɛ");
            pw.write(grafo);
            archivo.close();
            grafo = "";
            creaImagen();
        } catch (IOException e) {
            System.err.println("Error: " + e.toString());
        }
    }

    public static void borrar(String path) {
        File archivo = new File(path);
        archivo.delete();
    }

    public static void creaImagen() {
        try {
            String[] cmd = new String[5];
            cmd[0] = rutaDot;
            cmd[1] = jp;
            cmd[2] = textoBase;
            cmd[3] = O;
            cmd[4] = imagen;

            Runtime rt = Runtime.getRuntime();
            rt.exec(cmd);
        } catch (IOException ex) {
            System.out.println("IOException -> " + ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
