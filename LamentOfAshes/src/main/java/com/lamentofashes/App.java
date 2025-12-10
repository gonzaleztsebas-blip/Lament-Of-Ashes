package com.lamentofashes;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación JavaFX. Gestiona la inicialización de la
 * ventana, la navegación de escenas (FXML) y la reproducción de música de
 * fondo.
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage; // Referencia estática al Stage principal
    private static MediaPlayer musicPlayer;

    // Se mantiene la carga de la fuente, aunque su uso directo se realiza en los controladores de la UI.
    // Font font = Font.loadFont(App.class.getResourceAsStream("/fonts/pixelplay.ttf"), 18);
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage; // Guarda la referencia al Stage principal

        // Cargar el ícono de la aplicación
        Image icon = new Image(App.class.getResourceAsStream("/ICON.png"));
        stage.getIcons().add(icon);

        // Inicializar la escena con el FXML de la pantalla de título
        scene = new Scene(loadFXML("TitleScene"), 640, 480);
        stage.setScene(scene);

        stage.setTitle("Lament of Ashes DEMO");
        stage.setResizable(false);
        stage.show();

        // Iniciar la música de fondo
        reproducirMusica("/media/mainTheme.mp3");
    }

    /**
     * Cambia el nodo raíz de la escena actual para navegar a una nueva pantalla
     * FXML.
     *
     * @param fxml Nombre del archivo FXML (sin extensión) a cargar.
     * @throws IOException Si el archivo FXML no se puede cargar.
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Carga y retorna el nodo raíz de un archivo FXML desde la ruta de
     * recursos.
     *
     * @param fxml Nombre del archivo FXML (sin extensión).
     * @return El nodo Parent cargado.
     * @throws IOException Si el archivo FXML no se encuentra o hay un error de
     * lectura.
     */
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/lamentofashes/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Obtiene una referencia al Stage principal de la aplicación. Útil para
     * crear diálogos modales o acceder a propiedades de la ventana.
     *
     * @return El Stage principal.
     */
    public static Stage getStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Gestiona la reproducción de música de fondo, deteniendo cualquier pista
     * anterior.
     *
     * @param ruta La ruta relativa al archivo de audio dentro de los recursos
     * (ej: "/media/musica.mp3").
     */
    public static void reproducirMusica(String ruta) {
        try {
            if (musicPlayer != null) {
                musicPlayer.stop(); // Detener música anterior
            }

            // Crear Media a partir de la URL de recurso
            Media media = new Media(App.class.getResource(ruta).toExternalForm());
            musicPlayer = new MediaPlayer(media);

            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop infinito
            musicPlayer.setVolume(0.50); // Volumen por defecto
            musicPlayer.play();

        } catch (Exception e) {
            // Manejo silencioso: La música es un elemento de adorno, no crítico.
        }
    }
}
