package com.lamentofashes.controllers.ui;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.media.AudioClip;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

public class AnimationManager {

    private final Set<Animation> animacionesActivas = new HashSet<>();
    private final java.util.Map<Node, Set<Animation>> animacionesPorNodo = new java.util.HashMap<>();

    // ----------------------------------------------------------
    // 🔊 MÉTODO UNIVERSAL PARA REPRODUCIR SONIDOS
    // ----------------------------------------------------------
    private void playSound(String fileName) {
        try {
            AudioClip sonido = new AudioClip(
                    getClass().getResource("/media/" + fileName).toExternalForm()
            );
            sonido.play();
        } catch (Exception e) {
            System.err.println("⚠ No se pudo reproducir el sonido: " + fileName);
        }
    }

    private void registrarAnimacion(Animation anim, Node... nodes) {
        if (anim == null) {
            return;
        }

        synchronized (animacionesActivas) {
            animacionesActivas.add(anim);

            if (nodes != null && nodes.length > 0) {
                synchronized (animacionesPorNodo) {
                    for (Node node : nodes) {
                        if (node != null) {
                            animacionesPorNodo.computeIfAbsent(node, k -> new HashSet<>()).add(anim);
                        }
                    }
                }
            }

            anim.setOnFinished(e -> {
                synchronized (animacionesActivas) {
                    animacionesActivas.remove(anim);

                    synchronized (animacionesPorNodo) {
                        animacionesPorNodo.values().forEach(set -> set.remove(anim));
                        animacionesPorNodo.entrySet().removeIf(entry -> entry.getValue().isEmpty());
                    }
                }
            });
        }
    }

    //-------------------------------------------------------------
    // ❤️ ANIMACIÓN DE DAÑO + SONIDO
    //-------------------------------------------------------------
    public void animarDamage(Node sprite) {
        if (sprite == null) {
            return;
        }
        playSound("hit.wav");

        ColorAdjust colorAdjust = new ColorAdjust();
        sprite.setEffect(colorAdjust);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    colorAdjust.setHue(0);
                    colorAdjust.setSaturation(1.0);
                    colorAdjust.setBrightness(0.3);
                    sprite.setOpacity(1.0);
                }),
                new KeyFrame(Duration.millis(80), e -> sprite.setOpacity(0.3)),
                new KeyFrame(Duration.millis(160), e -> sprite.setOpacity(1.0)),
                new KeyFrame(Duration.millis(240), e -> sprite.setOpacity(0.3)),
                new KeyFrame(Duration.millis(320), e -> sprite.setOpacity(1.0)),
                new KeyFrame(Duration.millis(480), e -> sprite.setEffect(null))
        );

        registrarAnimacion(timeline, sprite);
        timeline.play();
    }

    //-------------------------------------------------------------
    // ⚔ ATAQUE DEL JUGADOR + SONIDO
    //-------------------------------------------------------------
    public void animarAtaqueJugador(Node heroSprite, Runnable onComplete) {
        if (heroSprite == null) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        playSound("attack.wav");

        double originalX = heroSprite.getTranslateX();

        TranslateTransition forward = new TranslateTransition(Duration.millis(150), heroSprite);
        forward.setByX(80);

        TranslateTransition back = new TranslateTransition(Duration.millis(150), heroSprite);
        back.setByX(-80);

        back.setOnFinished(e -> {
            heroSprite.setTranslateX(originalX);
            if (onComplete != null) {
                onComplete.run();
            }
        });

        forward.setOnFinished(e -> back.play());
        forward.play();
    }

    //-------------------------------------------------------------
    // ⚔ ATAQUE DEL ENEMIGO + SONIDO
    //-------------------------------------------------------------
    public void animarAtaqueEnemigo(Node enemySprite, Runnable onComplete) {
        if (enemySprite == null) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        playSound("enemy_attack.wav");

        double originalX = enemySprite.getTranslateX();

        TranslateTransition forward = new TranslateTransition(Duration.millis(150), enemySprite);
        forward.setByX(-80);

        TranslateTransition back = new TranslateTransition(Duration.millis(150), enemySprite);
        back.setByX(80);

        back.setOnFinished(e -> {
            enemySprite.setTranslateX(originalX);
            if (onComplete != null) {
                onComplete.run();
            }
        });

        forward.setOnFinished(e -> back.play());
        forward.play();
    }

    //-------------------------------------------------------------
    // 🛡 ANIMACIÓN DE DEFENSA + SONIDO
    //-------------------------------------------------------------
    public void animarDefensa(Node sprite) {
        if (sprite == null) {
            return;
        }

        playSound("block.wav");

        ColorAdjust colorAdjust = new ColorAdjust();
        sprite.setEffect(colorAdjust);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), e -> {
                    colorAdjust.setHue(-0.5);
                    colorAdjust.setSaturation(0.8);
                    colorAdjust.setBrightness(0.4);
                }),
                new KeyFrame(Duration.millis(400), e -> sprite.setEffect(null))
        );

        registrarAnimacion(timeline, sprite);
        timeline.play();
    }

    //-------------------------------------------------------------
    // ✨ CURACIÓN + SONIDO
    //-------------------------------------------------------------
    public void animarCuracion(Node sprite) {
        if (sprite == null) {
            return;
        }

        playSound("heal.wav");

        ColorAdjust colorAdjust = new ColorAdjust();
        sprite.setEffect(colorAdjust);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), e -> {
                    colorAdjust.setHue(0.3);
                    colorAdjust.setSaturation(0.8);
                    colorAdjust.setBrightness(0.5);
                }),
                new KeyFrame(Duration.millis(450), e -> sprite.setEffect(null))
        );

        registrarAnimacion(timeline, sprite);
        timeline.play();
    }

    //-------------------------------------------------------------
    // 💀 MUERTE + SONIDO
    //-------------------------------------------------------------
    public void animarMuerte(Node sprite, Runnable onComplete) {
        if (sprite == null) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        playSound("death.wav");

        FadeTransition fade = new FadeTransition(Duration.millis(500), sprite);
        fade.setToValue(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(500), sprite);
        scale.setToX(1.3);
        scale.setToY(1.3);

        ParallelTransition parallel = new ParallelTransition(fade, scale);
        parallel.setOnFinished(e -> {
            if (onComplete != null) {
                onComplete.run();
            }
        });

        registrarAnimacion(parallel, sprite);
        parallel.play();
    }

    //-------------------------------------------------------------
    // 💥 GOLPE CRÍTICO + SONIDO
    //-------------------------------------------------------------
    public void animarCritico(Node sprite, Node container) {
        if (sprite == null) {
            return;
        }

        playSound("crit.wav");

        ColorAdjust colorAdjust = new ColorAdjust();
        sprite.setEffect(colorAdjust);

        Timeline flash = new Timeline(
                new KeyFrame(Duration.millis(0), e -> colorAdjust.setBrightness(1.0)),
                new KeyFrame(Duration.millis(300), e -> sprite.setEffect(null))
        );

        registrarAnimacion(flash, sprite);
        flash.play();

        if (container != null) {
            animarTemblor(container);
        }
    }

    public boolean isAnimating() {
        synchronized (animacionesActivas) {
            return !animacionesActivas.isEmpty();
        }
    }

    public boolean isAnimating(Node node) {
        synchronized (animacionesPorNodo) {
            return animacionesPorNodo.containsKey(node)
                    && !animacionesPorNodo.get(node).isEmpty();
        }
    }

    public void detenerTodasLasAnimaciones() {
        synchronized (animacionesActivas) {
            for (Animation anim : animacionesActivas) {
                anim.stop();
            }
            animacionesActivas.clear();
            animacionesPorNodo.clear();
        }
    }

    public void animarTemblor(Node container) {
        if (container == null) {
            return;
        }

        double originalX = container.getTranslateX();
        double originalY = container.getTranslateY();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), e -> {
                    container.setTranslateX(originalX + 5);
                    container.setTranslateY(originalY + 5);
                }),
                new KeyFrame(Duration.millis(50), e -> {
                    container.setTranslateX(originalX - 5);
                    container.setTranslateY(originalY - 5);
                }),
                new KeyFrame(Duration.millis(100), e -> {
                    container.setTranslateX(originalX + 5);
                    container.setTranslateY(originalY);
                }),
                new KeyFrame(Duration.millis(150), e -> {
                    container.setTranslateX(originalX - 5);
                    container.setTranslateY(originalY);
                }),
                new KeyFrame(Duration.millis(200), e -> {
                    container.setTranslateX(originalX);
                    container.setTranslateY(originalY);
                })
        );

        registrarAnimacion(timeline, container);
        timeline.play();
    }

    public void animarEntradaEnemigo(Node sprite) {
        if (sprite == null) {
            return;
        }

        sprite.setOpacity(0);
        sprite.setScaleX(0.5);
        sprite.setScaleY(0.5);

        FadeTransition fade = new FadeTransition(Duration.millis(400), sprite);
        fade.setToValue(1.0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(400), sprite);
        scale.setToX(1.0);
        scale.setToY(1.0);

        ParallelTransition parallel = new ParallelTransition(fade, scale);

        registrarAnimacion(parallel, sprite);
        parallel.play();
    }

    public void animarBloqueo(Node sprite) {
        if (sprite == null) {
            return;
        }

        double originalX = sprite.getTranslateX();

        TranslateTransition bounce = new TranslateTransition(Duration.millis(80), sprite);
        bounce.setByX(-15);
        bounce.setAutoReverse(true);
        bounce.setCycleCount(2);

        bounce.setOnFinished(e -> sprite.setTranslateX(originalX));

        registrarAnimacion(bounce, sprite);
        bounce.play();
    }

    public int getNumeroAnimacionesActivas() {
        synchronized (animacionesActivas) {
            return animacionesActivas.size();
        }
    }

    public int getNumeroAnimacionesActivas(Node node) {
        synchronized (animacionesPorNodo) {
            Set<Animation> anims = animacionesPorNodo.get(node);
            return anims != null ? anims.size() : 0;
        }
    }

    public boolean esperarAnimaciones(long timeoutMillis) {
        long startTime = System.currentTimeMillis();

        while (isAnimating()) {
            if (System.currentTimeMillis() - startTime > timeoutMillis) {
                detenerTodasLasAnimaciones();
                return false;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        return true;
    }

    public void animarPulsoSeleccion(Node node) {
        if (node == null) {
            return;
        }

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), node);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), node);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        SequentialTransition sequence = new SequentialTransition(scaleUp, scaleDown);
        sequence.setCycleCount(Animation.INDEFINITE);
        sequence.setAutoReverse(true);

        registrarAnimacion(sequence, node);
        sequence.play();
    }

    public void detenerPulso(Node node) {
        synchronized (animacionesPorNodo) {
            Set<Animation> animaciones = animacionesPorNodo.get(node);
            if (animaciones != null) {
                for (Animation anim : animaciones) {
                    if (anim instanceof SequentialTransition) {
                        anim.stop();
                    }
                }
            }
        }
    }

    public void detenerAnimacionesNodo(Node node) {
        synchronized (animacionesPorNodo) {
            Set<Animation> animaciones = animacionesPorNodo.get(node);
            if (animaciones != null) {
                Set<Animation> copiaAnimaciones = new HashSet<>(animaciones);
                for (Animation anim : copiaAnimaciones) {
                    anim.stop();
                }
            }
        }
    }

    public void imprimirEstadoAnimaciones() {
        synchronized (animacionesActivas) {
            System.out.println("=== ESTADO ANIMACIONES ===");
            System.out.println("Total animaciones activas: " + animacionesActivas.size());

            synchronized (animacionesPorNodo) {
                for (java.util.Map.Entry<Node, Set<Animation>> entry : animacionesPorNodo.entrySet()) {
                    System.out.println("Nodo: " + entry.getKey() + " - Animaciones: " + entry.getValue().size());
                }
            }
            System.out.println("==========================");
        }
    }

}
