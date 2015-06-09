package com.teco.vindi.touchdynamics;

import android.media.MediaRecorder;
import java.io.IOException;

public class SoundMeter {

    private MediaRecorder mRecorder = null;
    private Thread mSoundLevelThread = null;
    private SoundLevelListener mListener;

    public void start() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();

            mSoundLevelThread = new Thread() {
                @Override
                public void run() {
                    try {
                        while(true) {
                            sleep(100);
                            mListener.onSoundLevelChanged(getAmplitude());
                        }
                    } catch (InterruptedException e) {
                        // Thread stopped.
                    }
                }
            };

            mSoundLevelThread.start();
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mSoundLevelThread.interrupt();
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return  mRecorder.getMaxAmplitude();
        else
            return 0;

    }

    public void registerListener(SoundLevelListener listener) {
        this.mListener = listener;
        start();
    }

    public void unregisterListener(SoundLevelListener listener) {
        stop();
        this.mListener = null;
    }

    public interface SoundLevelListener {
        void onSoundLevelChanged(double soundLevel);
    }
}