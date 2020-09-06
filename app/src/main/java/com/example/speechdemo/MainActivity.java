package com.example.speechdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_SPEECH_RECOGNIZER = 3000;
    private TextView mTextView;
    private final String mQuestion = "Which company is the largest online retailer on the planet?";
    private String mAnswer = "";
    private Button ttsButton;
    private Button sttButton;
    private TextToSpeech ttsObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ttsButton = (Button)findViewById(R.id.ttsBtn);
        sttButton = (Button)findViewById(R.id.sttBtn);
        mTextView = (TextView) findViewById(R.id.result);

        ttsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextView.setText("");
                startTextToSpeech();
            }
        });

        sttButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSpeechRecognizer();
            }
        });


        ttsObject =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    ttsObject.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }

    private void startTextToSpeech()
    {
        ttsObject.speak(mQuestion, TextToSpeech.QUEUE_FLUSH, null, "Question");
    }

    private void startSpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mQuestion);
        startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String response;
        if (requestCode == REQUEST_SPEECH_RECOGNIZER) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra
                        (RecognizerIntent.EXTRA_RESULTS);
                if (results != null) {
                    mAnswer = results.get(0);
                }

                if (mAnswer.toUpperCase().contains("AMAZON"))
                {
                    response = "\n\nQuestion: " + mQuestion + "\n\nYour answer is '" + mAnswer + "' and it is correct!";
                    mTextView.setText(response);
                }
                else
                {
                    response = "\n\nQuestion: " + mQuestion + "\n\nYour answer is '" + mAnswer + "' and it is incorrect!";
                    mTextView.setText(response);
                }
            }
        }
    }
}








