package by.mastihin.volumedetector.fft;



import org.jtransforms.fft.DoubleFFT_1D;

import by.mastihin.volumedetector.windows.Window;

public class JTransformFFT extends FFT {

    private double[] fft;

    private DoubleFFT_1D fftDo;
    private Window mWindow;

    public JTransformFFT(int fftSize, Window window) {
        super(fftSize);
        this.fft = new double[fftSize * 2];
        fftDo = new DoubleFFT_1D(fftSize);
        mWindow = window;
    }

    @Override
    public void doFFT(final short[] rawData, double[] fftResult) {

        prepareData(rawData);

        fftDo.realForwardFull(fft);

        for (int i = 0; i < getFftSize(); i++) {
            fftResult[i] = Math.sqrt(fft[2 * i] * fft[2 * i] + fft[2 * i + 1] * fft[2 * i + 1]);
        }
    }

    private void prepareData(short[] rawData) {
        for (int i = 0; i < fft.length; i++) {
            if (i < rawData.length) {
                fft[i] = (double) rawData[i] / (double) Short.MAX_VALUE * mWindow.function(i);
            } else {
                fft[i] = 0;
            }
        }
    }
}