package by.mastihin.volumedetector.windows;

public abstract class Window {

    private int frameSize = 1;

    public Window(int frameSize) {
        this.frameSize = frameSize;
    }

    public double function(int i) {
        return 1;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(int frameSize) {
        this.frameSize = frameSize;
    }
}
