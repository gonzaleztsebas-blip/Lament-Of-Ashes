/*
 * Clase de utilidad para gestionar los efectos de sonido de botones.
 */
package com.lamentofashes.controllers.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * Gestiona y reproduce los sonidos de interacción (hover y click) de los
 * botones.
 */
public class ButtonSoundManager {

    private static MediaPlayer hoverPlayer;
    private static MediaPlayer clickPlayer;

    /**
     * Crea un MediaPlayer para un archivo de sonido de recurso específico.
     */
    private static MediaPlayer createPlayer(String fileName) {
        try {
            URL soundURL = ButtonSoundManager.class.getResource("/media/" + fileName);
            if (soundURL == null) {
                return null;
            }

            Media media = new Media(soundURL.toString());
            MediaPlayer player = new MediaPlayer(media);
            player.setVolume(0.25); // Volumen suave
            return player;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Reproduce el sonido de hover, reiniciándolo si ya está sonando.
     */
    public static void playHoverSound() {
        if (hoverPlayer == null) {
            hoverPlayer = createPlayer("hover.wav");
        }
        if (hoverPlayer != null) {
            hoverPlayer.stop();
            hoverPlayer.play();
        }
    }

    /**
     * Reproduce el sonido de click, reiniciándolo si ya está sonando.
     */
    public static void playClickSound() {
        if (clickPlayer == null) {
            clickPlayer = createPlayer("click.wav");
        }
        if (clickPlayer != null) {
            clickPlayer.stop();
            clickPlayer.play();
        }
    }

    /**
     * Aplica los manejadores de eventos de sonido a todos los nodos con la
     * clase CSS ".button" que son instancias de Button dentro del Parent.
     */
    public static void applyToAll(Parent root) {
        root.lookupAll(".button").forEach(node -> {
            if (node instanceof Button) {
                Button btn = (Button) node;

                btn.setOnMouseEntered(e -> ButtonSoundManager.playHoverSound());
                btn.setOnMouseClicked(e -> ButtonSoundManager.playClickSound());
            }
        });
    }

    /**
     * Aplica los manejadores de eventos de sonido a un Node específico.
     */
    public static void applyToNode(Node node) {
        if (node == null) {
            return;
        }
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> playHoverSound());
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> playClickSound());
    }
}
