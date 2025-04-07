package com.mycompany.tictactoegame.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.HashMap;

public class MusicController {
    private static MediaPlayer backgroundPlayer;
    private static final HashMap<String, MediaPlayer> soundEffects = new HashMap<>();
    private static volatile boolean isMuted = false;

    // Sound effect constants
    public static final String SOUND_CLICK = "placeX";
    public static final String SOUND_PLACE_X = "placeX";
    public static final String SOUND_PLACE_O = "placeO";
    public static final String SOUND_WIN = "win";
    public static final String SOUND_LOSE = "lose";
    public static final String SOUND_DRAW = "draw";

    public static void initializeMusic() {
        try {
            // Load background music (use simpler filename)
            String bgMusicPath = MusicController.class.getResource("/audio/background_music.mp3").toString();
            backgroundPlayer = new MediaPlayer(new Media(bgMusicPath));
            backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundPlayer.setVolume(0.3); // Lower volume for background
            
            // Load all sound effects
            loadSoundEffect(SOUND_CLICK, "/audio/click.mp3");
            loadSoundEffect(SOUND_PLACE_X, "/audio/placeX.mp3");
            loadSoundEffect(SOUND_PLACE_O, "/audio/placeO.mp3");
            loadSoundEffect(SOUND_WIN, "/audio/win.mp3");
            loadSoundEffect(SOUND_LOSE, "/audio/lose.mp3");
            loadSoundEffect(SOUND_DRAW, "/audio/draw.mp3");
            
            // Start background music
            backgroundPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio system: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadSoundEffect(String name, String path) {
        try {
            String url = MusicController.class.getResource(path).toString();
            if (url != null) {
                MediaPlayer player = new MediaPlayer(new Media(url));
                player.setVolume(0.7); // Default volume for sound effects
                soundEffects.put(name, player);
            } else {
                System.err.println("Sound effect not found: " + path);
            }
        } catch (Exception e) {
            System.err.println("Failed to load sound effect " + name + ": " + e.getMessage());
        }
    }

    public static void playSound(String effectName) {
        if (!isMuted && soundEffects.containsKey(effectName)) {
            MediaPlayer player = soundEffects.get(effectName);
            // Stop and reset to beginning if already playing
            player.stop();
            player.play();
        }
    }

    public static void toggleMute() {
        isMuted = !isMuted;
        if (backgroundPlayer != null) {
            backgroundPlayer.setVolume(isMuted ? 0 : 0.3);
        }
        // Mute all sound effects
        soundEffects.values().forEach(player -> player.setVolume(isMuted ? 0 : 0.7));
    }

    public static boolean isMuted() {
        return isMuted;
    }

    public static void cleanup() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
            backgroundPlayer.dispose();
        }
        soundEffects.values().forEach(MediaPlayer::dispose);
        soundEffects.clear();
    }
}