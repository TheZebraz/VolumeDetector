package by.mastihin.volumedetector.windows;

public class Gausse extends Window {

    private double sigma;
    private double A;

    public Gausse(int frameSize, double sigma) {
        super(frameSize);
        this.sigma = sigma;
        A = (double) (frameSize - 1) / (double) 2;
    }

    public double function(double value) {
        return Math.exp(-(Math.pow((value - A) / (sigma * A), 2) / (double) 2));
    }
}
