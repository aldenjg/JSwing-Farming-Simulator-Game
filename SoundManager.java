import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.sound.sampled.*;

// Sound effect manager
class SoundManager {
    private HashMap<String, Clip> soundClips;
    private boolean soundEnabled;
    private boolean musicEnabled;
    private Clip backgroundMusic;
    
    public SoundManager() {
        soundClips = new HashMap<>();
        soundEnabled = true;
        musicEnabled = true;
        
        // Preload sound effects
        try {
            loadSound("plant", "sounds/plant.wav");
            loadSound("water", "sounds/water.wav");
            loadSound("harvest", "sounds/harvest.wav");
            loadSound("buy", "sounds/buy.wav");
            loadSound("night", "sounds/night.wav");
            loadSound("bug", "sounds/bug.wav");
            loadSound("flood", "sounds/flood.wav");
            loadSound("win", "sounds/win.wav");
            loadSound("lose", "sounds/lose.wav");
            
            // Load background music
            loadBackgroundMusic("sounds/farm_music.wav");
        } catch (Exception e) {
            System.out.println("Error loading sounds: " + e.getMessage());
            // Continue without sound if files aren't found
        }
    }
    
    private void loadSound(String name, String path) {
        try {
            File soundFile = new File(path);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                soundClips.put(name, clip);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error loading sound " + name + ": " + e.getMessage());
        }
    }
    
    private void loadBackgroundMusic(String path) {
        try {
            File musicFile = new File(path);
            if (musicFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioIn);
                // Loop continuously
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error loading background music: " + e.getMessage());
        }
    }
    
    public void playSound(String name) {
        if (!soundEnabled) return;
        
        Clip clip = soundClips.get(name);
        if (clip != null) {
            // Stop the clip if it's already playing
            if (clip.isRunning()) {
                clip.stop();
            }
            // Reset to beginning
            clip.setFramePosition(0);
            // Start playing
            clip.start();
        }
    }
    
    public void startBackgroundMusic() {
        if (!musicEnabled || backgroundMusic == null) return;
        
        if (!backgroundMusic.isRunning()) {
            backgroundMusic.setFramePosition(0);
            backgroundMusic.start();
        }
    }
    
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
    
    public void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }
    
    public void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
        if (musicEnabled) {
            startBackgroundMusic();
        } else {
            stopBackgroundMusic();
        }
    }
}