// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AudioClip {

    private Clip clip;

    public AudioClip(File f) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f);
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.setLoopPoints(0, -1);
    }

    public AudioClip(InputStream inputStream) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.setLoopPoints(0, -1);
    }

    public void play() {
        clip.start();
    }

    public void stop() {
        clip.stop();
    }

    public int getPlaybackPosition() {
        return clip.getFramePosition();
    }

    public void setPlaybackPosition(int position) {
        clip.setFramePosition(position);
    }

    private void loop(int i) {
        clip.loop(i);
    }

    public void setLooping(int looping) {
        loop(looping);
    }

    public void startLooping() {
        loop(Integer.MAX_VALUE);
    }

    public void stopLooping() {
        loop(0);
    }

    public void addLineListener(LineListener lineListener) {
        clip.addLineListener(lineListener);
    }

    public void removeLineListener(LineListener lineListener) {
        clip.removeLineListener(lineListener);
    }

    public boolean isRunning() {
        return clip.isRunning();
    }

    public boolean isActive() {
        return clip.isActive();
    }

    public boolean isOpen() {
        return clip.isOpen();
    }

    public boolean isControlSupported(Control.Type type) {
        return clip.isControlSupported(type);
    }

    public Line.Info getLineInfo() {
        return clip.getLineInfo();
    }

    public void setLoopPoints(int start, int stop) {
        clip.setLoopPoints(start, stop);
    }

}