package manager;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundManager {

    private boolean musicEnabled = true;
    private boolean shotEnabled = true;
    private boolean explosionEnabled = true;
    private boolean gameOverEnabled = true;

    private Clip backgroundClip;
    private Clip shotClip;
    private Clip explosionClip;
    private Clip gameOverClip;
    private Clip winClip;

    private static final String BACKGROUND_PATH = "src/resources/sounds/background.wav";
    private static final String SHOT_PATH = "src/resources/sounds/shot.wav";
    private static final String EXPLOSION_PATH = "src/resources/sounds/explosion.wav";
    private static final String GAME_OVER_PATH = "src/resources/sounds/gameover.wav";
    private static final String WIN_PATH = "src/resources/sounds/win.wav";

    public SoundManager() {

        backgroundClip = loadSound(BACKGROUND_PATH);
        shotClip = loadSound(SHOT_PATH);
        explosionClip = loadSound(EXPLOSION_PATH);
        gameOverClip = loadSound(GAME_OVER_PATH);
        winClip = loadSound(WIN_PATH);

    }

    private Clip loadSound(String path) {

        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path))) {

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;

        } catch (Exception e) {

            System.out.println("Sound not loaded: " + path);
            System.out.println(e.getMessage());
            return null;

        }
    }

    public void setSoundSettings(boolean musicEnabled, boolean shotEnabled,
            boolean explosionEnabled, boolean gameOverEnabled) {

        this.musicEnabled = musicEnabled;
        this.shotEnabled = shotEnabled;
        this.explosionEnabled = explosionEnabled;
        this.gameOverEnabled = gameOverEnabled;

        //چون نیازه در لحظه بررسی و اتفاق بیوفته
        if (musicEnabled) {
            playBackgroundMusic();
        } else {
            stopBackgroundMusic();
        }

    }

    public void playBackgroundMusic() {

        if (!musicEnabled || backgroundClip == null) {
            return;
        }
        if (backgroundClip.isRunning()) {
            return;
        }
        backgroundClip.setFramePosition(0);
        backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);

    }

    public void stopEndSounds() {

        if (winClip != null) {
            winClip.stop();
            winClip.setFramePosition(0);
        }
        if (gameOverClip != null) {
            gameOverClip.stop();
            gameOverClip.setFramePosition(0);
        }

    }

    public void stopBackgroundMusic() {

        if (backgroundClip == null) {
            return;
        }
        backgroundClip.stop();
        backgroundClip.setFramePosition(0);

    }

    public void playShot() {
        if (shotEnabled) {
            playSoundEffect(shotClip);
        }
    }

    public void playExplosion() {
        if (explosionEnabled) {
            playSoundEffect(explosionClip);
        }
    }

    public void playGameOver() {
        if (gameOverEnabled) {
            playSoundEffect(gameOverClip);
        }
    }

    public void playWin() {
        if (gameOverEnabled) {
            playSoundEffect(winClip);
        }
    }

    private void playSoundEffect(Clip clip) {

        if (clip == null) {
            return;
        }
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();

    }

}
