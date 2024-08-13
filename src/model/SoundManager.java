package model;

/*
 * Description:This code creates the SoundManager class, which controls how sound
 * effects are played in JavaFX applications. The sound effects are played by the
 * class using the MediaPlayer class from JavaFX.
 */

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;

public class SoundManager {
    private MediaPlayer clickSound;
    private MediaPlayer winSound;

    public SoundManager() {
        this.clickSound = createMediaPlayer("click.mp3");
        this.winSound = createMediaPlayer("win.mp3");
    }

    /*
     * Description: The sound file is loaded as a URL by the createMediaPlayer
     * function using the getResource method. RuntimeException is thrown with a
     * message if the resource is null, indicating that the sound file was not found.
     */
    private MediaPlayer createMediaPlayer(String filename) {
        URL resource = getClass().getResource("/sfx/" + filename);
        if (resource == null) {
            throw new RuntimeException("Sound file not found: " + filename);
        }
        return new MediaPlayer(new Media(resource.toString()));
    }

    /*
     * Description: this plays the click sound when the user click on a cell.
     */
    public void playClickSound() {
        clickSound.stop();
        clickSound.seek(clickSound.getStartTime());
        clickSound.play();
    }

    /*
     * Description: this plays the win sound when the user wins the game.
     */
    public void playWinSound() {
        winSound.stop();
        winSound.seek(winSound.getStartTime());
        winSound.play();
    }
}
