package com.example.assistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.media.Image;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;

public class MainActivity extends AppCompatActivity {

    ImageView micImageView;
    TextView userTextView;
    TextView agentTextView;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 121);
        micImageView = findViewById(R.id.mic);
        userTextView = findViewById(R.id.userTextView);
        agentTextView = findViewById(R.id.agentTextView);
        tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });

        final AIConfiguration config = new AIConfiguration("b822267ef1804cde811d0483b8448aea",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        final AIService aiService = AIService.getService(getApplicationContext(), config);
        aiService.setListener(new AIListener() {
            @Override
            public void onResult(AIResponse result) {
                tts.speak(result.getResult().getFulfillment().getSpeech(), TextToSpeech.QUEUE_ADD, null);
                agentTextView.setText(result.getResult().getFulfillment().getSpeech());
                userTextView.setText(result.getResult().getResolvedQuery());
            }

            @Override
            public void onError(AIError error) {
                micImageView.setImageResource(R.drawable.mic23);

            }

            @Override
            public void onAudioLevel(float level) {

            }

            @Override
            public void onListeningStarted() {
                micImageView.setImageResource(R.drawable.mic1);
            }

            @Override
            public void onListeningCanceled() {
                micImageView.setImageResource(R.drawable.mic23);
            }

            @Override
            public void onListeningFinished() {
                micImageView.setImageResource(R.drawable.mic23);
            }
        });
        micImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiService.startListening();
            }
        });

    }
}
