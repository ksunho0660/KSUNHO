package com.example.android.audiorecord;

import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.NoiseSuppressor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "AudioRecord";
    
	public static final String filedir = Environment.getExternalStorageDirectory().getAbsolutePath();
	private static String filename = null;
    TextView textView;
    Button play,record;
    public int D = 0;
    final Handler handler = new Handler();

   // int buffersize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
   // NoiseSuppressor.create()
    public int test_number=0;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        
		Log.d("TIMER", filedir);
		

        play = (Button) findViewById(R.id.play);
        record = (Button) findViewById(R.id.record);

        //timer test

        textView = (TextView)findViewById(R.id.test_number_view);

        record.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (recorder == null) {
					Log.d("TIMER", "____________첫 시작________________");
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					filename = filedir +  "/test_0.3gp";
					recorder.setOutputFile(filename);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					
					Log.d("TIMER", "____________시작 근처________________");
                    try {
                        recorder.prepare();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                    }
					Log.d("TIMER", "____________1111________________");
                    recorder.start();
					Log.d("TIMER", "____________2222________________");
                    record.setText("녹음 중지");
					
					final Timer myTimer = new Timer();
                    final TimerTask myTask = new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                public void run() {
                                    if(D ==1){
                                        Log.d("TIMER", "____________D 가 1이 되었나?________________");
                                        myTimer.cancel();

                                    }
									else{
                                    Log.d("TIMER", "____________타이머 안에 들어옴!!! 멈출 파일 이름 확인________________");
                                    Log.d("TIMER", filename);
                                    recorder.stop();
									recorder.release();
									recorder = null;
									Log.d("TIMER", "____________타이머 안에서 잘 release 되었나?________________");
									recorder = new MediaRecorder();
									recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
									recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
									test_number = test_number + 1;\ 
                                    textView.setText("" + test_number);
									filename = filedir +  "/test_" + test_number + ".3gp";
									recorder.setOutputFile(filename); 
									recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
									Log.d("TIMER", "____________그 다음 저장할 파일 이름 확인________________");
									Log.d("TIMER", filename);
									try {
										recorder.prepare();
									} catch (IOException e) {
										Log.e(LOG_TAG, "prepare() failed");
									}
									Log.d("TIMER", "____________그 다음 파일 재 시작!________________");
									recorder.start();
									}
								}
								});

                        }
                    };
					
					
                    myTimer.schedule(myTask, 5000, 5000);

                } else {
					Log.d("TIMER", "____________녹음 중지 누름!________________");
                    recorder.stop();
                    recorder.release();



					Log.d("TIMER", "____________녹음 최종 마지막 release 누름!________________");
                    recorder = null;
                    D = 1;
                    
                    record.setText("녹음 시작");

                }
            }
        });
        }



    @Override
        public void onPause(){
        super.onPause();
        if(recorder != null){
            recorder.release();
            recorder = null;

        }

        if(player != null){
            player.release();
            player = null;
        }
    }

    }




