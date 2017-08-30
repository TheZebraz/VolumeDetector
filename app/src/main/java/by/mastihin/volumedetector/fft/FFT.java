package by.mastihin.volumedetector.fft;

abstract public class FFT {
    private int fftSize;

    public FFT(int fftSize) {
        this.fftSize = fftSize;
    }

    abstract public void doFFT(short[] rawData, double[] fftResult);

    public int getFftSize() {
        return fftSize;
    }

    public void setFftSize(int fftSize) {
        this.fftSize = fftSize;
    }
}
