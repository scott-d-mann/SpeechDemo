package com.example.speechdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private final String mQuestion = "Which company is the largest online retailer on the planet?";
    private String mAnswer = "";
    private Button ttsButton;
    private Button sttButton;
    private TextToSpeech ttsObject;
    private String TAG = "mainAct";

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
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mQuestion);
                // The launcher with the Intent you want to start
                mStartForResult.launch(intent);
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


    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intentData = result.getData();
                        // Handle the Intent
                        String response;
                        List<String> results = null;
                        if (intentData != null) {
                            results = intentData.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        }
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
            });
}








