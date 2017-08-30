package by.mastihin.volumedetector.windows;

public class Hamming extends Window {

    public Hamming(int frameSize) {
        super(frameSize);
    }

    public double function(double value) {
        return 0.53836 - 0.46164 * Math.cos((2 * Math.PI * value) / (double)(this.getFrameSize() - 1));
    }
}
