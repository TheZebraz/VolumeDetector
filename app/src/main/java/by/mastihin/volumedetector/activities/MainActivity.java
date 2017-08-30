package by.mastihin.volumedetector.activities;

import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.ForkJoinWorkerThread;

import by.mastihin.volumedetector.R;
import by.mastihin.volumedetector.fft.FFT;
import by.mastihin.volumedetector.fft.JTransformFFT;
import by.mastihin.volumedetector.recorders.AndroidRecorder;
import by.mastihin.volumedetector.recorders.Recorder;
import by.mastihin.volumedetector.windows.Gausse;

public class MainActivity extends AppCompatActivity {

    private Recorder mRecorder;
    private FFT mFFT;
    private double[] mFftResult;

    private TextView mTextView;
    private Handler mHandler;

    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;

    private static final String LEVEL = "LEVEL";

    private static final int CHUNK_SIZE = (int) Math.pow(2, 12);
    private static final int SAMPLE_RATE = 44100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textview_main_level);

        mRecorder = new AndroidRecorder(AUDIO_SOURCE, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, CHUNK_SIZE);
        mFFT = new JTransformFFT(CHUNK_SIZE, new Gausse(CHUNK_SIZE, 0.3));

        mFftResult = new double[CHUNK_SIZE];

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                double valueInDB = msg.getData().getDouble(LEVEL);
                mTextView.setText(getString(R.string.sound_level_format, valueInDB));
                return true;
            }
        });

        startAnalyze();
        Log.d("START", "Norm" );

    }

    private void startAnalyze() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    Log.d("START", "Its run!" );
                    short[] data = mRecorder.getData();
                    double value = processData(data);
                    sendResult(value);
                }
            }
        });
        thread.start();
    }

    private void sendResult(double value) {
        double valueInDB = 20 * Math.log10(value);
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putDouble(LEVEL, valueInDB);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    private double processData(short[] data) {
        mFFT.doFFT(data, mFftResult);
        double maxAmplitude = 0;
        for (int i = 0; i < 500; i++) {
            if(mFftResult[i] > maxAmplitude){
                maxAmplitude = mFftResult[i];
            }
        }
        return maxAmplitude;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecorder.recordStop();
    }
}
