package by.mastihin.volumedetector.recorders;

import android.media.AudioRecord;
import android.util.Log;

public class AndroidRecorder implements Recorder {

    private int mChunkSize;
    private AudioRecord mAudioRecord;
    private short[] mBuffer;

    public AndroidRecorder(int audioSource, int sampleRate, int channelConfig, int audioFormat, int chunkSize) {
        this.mChunkSize = chunkSize;

        int internalBufferSize = mChunkSize;
        int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        if(internalBufferSize < minInternalBufferSize){
            internalBufferSize = minInternalBufferSize;
        }

        mAudioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, internalBufferSize);
        mBuffer = new short[this.mChunkSize];
    }

    public void recordStart() {
        if (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED) {
            mAudioRecord.startRecording();
        }
    }

    public void recordStop() {
        mAudioRecord.stop();
    }

    @Override
    public short[] getData() {
        recordStart();
        mAudioRecord.read(mBuffer, 0, mChunkSize);
        return mBuffer;
    }

    public int getChunkSize() {
        return mChunkSize;
    }
}
