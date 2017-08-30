package by.mastihin.volumedetector.recorders;

public interface Recorder {
    short[] getData();

    void recordStart();

    void recordStop();

    int getChunkSize();
}
