package com.mycompany.tictactoegame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.HashMap;
import javafx.fxml.FXML;

public class MusicController {
    private static MediaPlayer backgroundPlayer;
    private static final HashMap<String, MediaPlayer> soundEffects = new HashMap<>();
    private static volatile boolean isMuted = false;

    public static void initializeMusic() {
        try {
            String bgMusic = MusicController.class.getResource("/audio/Fluffing-a-Duck(chosic.com).mp3").toString();
            if (bgMusic == null) {
                System.err.println("Background music file not found!");
                return;
            }
            
            backgroundPlayer = new MediaPlayer(new Media(bgMusic));
            backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundPlayer.setVolume(0.5);
            backgroundPlayer.play();
            
            // Load sound effects
            loadSoundEffect("lose", "/audio/lose_sound.mp3");
            loadSoundEffect("win", "/audio/win_sound.mp3");
        } catch (Exception e) {
            System.err.println("Error initializing music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadSoundEffect(String name, String path) {
        try {
            String url = MusicController.class.getResource(path).toString();
            if (url != null) {
                MediaPlayer player = new MediaPlayer(new Media(url));
                player.setVolume(0.7);
                soundEffects.put(name, player);
            } else {
                System.err.println("Sound effect not found: " + path);
            }
        } catch (Exception e) {
            System.err.println("Failed to load sound effect: " + path);
            e.printStackTrace();
        }
    }

    public static synchronized void playSound(String effect) {
        if (!isMuted && soundEffects.containsKey(effect)) {
            MediaPlayer player = soundEffects.get(effect);
            player.stop(); // Reset if already playing
            player.play();
        }
    }

   
    public static synchronized void toggleMute() {
        isMuted = !isMuted;
        if (backgroundPlayer != null) {
            backgroundPlayer.setVolume(isMuted ? 0 : 0.5);
        }
        soundEffects.values().forEach(player -> player.setVolume(isMuted ? 0 : 0.7));
    }
    
    public static synchronized boolean isMuted() {
        return isMuted;
    }

    public static synchronized void cleanup() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
            backgroundPlayer.dispose();
        }
        soundEffects.values().forEach(MediaPlayer::dispose);
        soundEffects.clear();
    }
}