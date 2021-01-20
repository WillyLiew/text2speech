package com.example.text2speech;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_SPEECH = 100;
    TextView tv;
    EditText et, pitchET, speechET;
    Button t2sBtn, s2tBtn, setSpeechBtn;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // need to add android.intent.action.TTS_SERVICE in Manifest
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=findViewById(R.id.textView);
        et=findViewById(R.id.editText);
        pitchET=findViewById(R.id.pitchEditText);
        speechET=findViewById(R.id.speechEditText);
        t2sBtn=findViewById(R.id.text2speech);
        s2tBtn=findViewById(R.id.speech2text);

        textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    int lang=textToSpeech.setLanguage(Locale.ENGLISH);
                    if(lang==TextToSpeech.LANG_MISSING_DATA||lang==TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(MainActivity.this,"Language is not supported",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Text to speech initialization failed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        s2tBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,"en-us");
                startActivityForResult(intent,RESULT_SPEECH);

            }
        });

        t2sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech=et.getText().toString();
                Toast.makeText(MainActivity.this,speech,Toast.LENGTH_SHORT).show();
                textToSpeech.setPitch(Float.parseFloat(pitchET.getText().toString()));
                textToSpeech.setSpeechRate(Float.parseFloat(speechET.getText().toString()));
                textToSpeech.speak(speech,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case RESULT_SPEECH:
                if(resultCode==RESULT_OK&&data!=null){
                    ArrayList<String>text=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tv.setText(text.get(0));
                }
        }
    }
}