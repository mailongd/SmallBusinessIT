package com.example.soundpool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.speech.RecognizerIntent;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SoundPool soundPool;
    private int sound1, sound2, sound3, sound4, sound5, sound6, sound7;
    private int sound3StreamId;

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    //views from activity
    TextView mTextTv;
    TextView mTextResponses;
    ImageButton mVoiceBtn;
    List<String> wordBank = new ArrayList<>();
    List<String> responses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView t = (TextView) findViewById(R.id.text1);
        t.setMovementMethod(LinkMovementMethod.getInstance());

        mTextTv = (TextView) findViewById(R.id.textTv);
        mTextResponses = (TextView) findViewById(R.id.textTv);
        mVoiceBtn = (ImageButton) findViewById(R.id.voiceBtn);

        //create array list and connect it to xml file
        wordBank = Arrays.asList(getResources().getStringArray(R.array.Words));
        responses = Arrays.asList(getResources().getStringArray(R.array.responses));

        //button click to show speech to text dialog
        mVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

    }

    // create SPEAK intent and Respond --> from Wordbank
    public void speak() {
        //intent to show speech to text dialog
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi say something");

        //start intent
        try {
            //in there was no error
            //show dialog
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            //if there was some error
            //get message of error and show
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        //this is the counter for the responses loop
        int responseItem =0;
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK) {
            ArrayList<String> result = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mTextTv.setText(result.get(0).toUpperCase());
            //loops every word in a phrase until it keyword from wordBank
            for(String wordItem: wordBank) {
                if (result.get(0).contains(wordItem)) {
                    mTextResponses.setText(responses.get(responseItem).toString());
                    break;
                }
                //item +1
                //this moves to the next word, and it will check if there is key word in word item
                responseItem++;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        sound1 = soundPool.load(this, R.raw.sound1, 1);
        sound2 = soundPool.load(this, R.raw.sound2, 1);
        sound3 = soundPool.load(this, R.raw.sound3, 1);
        sound4 = soundPool.load(this, R.raw.sound4, 1);
    }
    public void playSound(View v) {
        switch (v.getId()) {
            case R.id.button_sound1:
                soundPool.play(sound1, 1, 1, 0, 0, 1);
                soundPool.autoPause();
                break;
           case R.id.button_sound2:
                soundPool.play(sound2, 1, 1, 0, 0, 1);
                break;
            case R.id.button_sound3:
                sound3StreamId = soundPool.play(sound3, 1, 1, 0, 0, 1);
                break;
            case R.id.button_sound4:
                soundPool.play(sound4, 1, 1, 0, 0, 1);
                break;
            case R.id.button_sound5: soundPool.play(sound5, 1, 1, 0, 0, 1);
                break;
            case R.id.button_sound6:
                soundPool.play(sound6, 1, 1, 0, 0, 1);
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
}