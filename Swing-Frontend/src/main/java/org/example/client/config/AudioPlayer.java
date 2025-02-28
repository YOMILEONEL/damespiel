package org.example.client.config;

import javax.sound.sampled.*;
import java.io.IOException;

public class AudioPlayer {
    private static AudioPlayer instance; // Singleton instance
    private Clip clip;
    private FloatControl volumeControl;

    private AudioPlayer() {

    }

    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }
        return instance;
    }

    public void playBackgroundMusic(String filePath) {
        try {
            if (clip == null || !clip.isRunning()) {
                //File audioFile = new File(filePath);
                //AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                var resource = getClass().getClassLoader().getResource(filePath);
                if (resource == null) {
                    throw new NullPointerException("Fichier audio introuvable : " + filePath);
                }
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(resource);

                clip = AudioSystem.getClip();
                clip.open(audioStream);
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(0.5f);
                clip.loop(Clip.LOOP_CONTINUOUSLY); // Joue la musique en boucle
                clip.start();
            }
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Format audio non supporté : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erreur d'accès au fichier : " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Erreur lors de l'ouverture de l'audio : " + e.getMessage());
        }
    }
    public void setVolume(float level) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float newVolume = min + (max - min) * level;
            volumeControl.setValue(newVolume);
        }
    }


    public void stopBackgroundMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
    public void playSoundEffect(String filePath) {
        try {
            var resource = getClass().getClassLoader().getResource(filePath);
            if (resource == null) {
                throw new NullPointerException("Fichier audio introuvable : " + filePath);
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(resource);
            Clip soundClip = AudioSystem.getClip();
            soundClip.open(audioStream);
            soundClip.start(); // Joue le son une seule fois
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Erreur de lecture du son : " + e.getMessage());
        }
    }

}
