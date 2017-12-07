package com.example.gabriel.jogodavelhaonline;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.gabriel.jogodavelhaonline.threads.VerificaOponenteEntrouThread;

import java.util.Timer;
import java.util.TimerTask;

public class LobbyActivity extends AppCompatActivity {

    TextView tvCodigoJogo;
    Timer timer;
    String nomeJogador1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        tvCodigoJogo = (TextView) findViewById(R.id.tvCodigoJogo);

        Integer codigoJogo = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            codigoJogo = extras.getInt("codigoJogo");
            nomeJogador1 = extras.getString("nomeJogador1");
        }

        tvCodigoJogo.setText("Código: " + codigoJogo);

        verificaOponente(codigoJogo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        timer.cancel();
    }

    private void verificaOponente(final Integer codigoJogo) {
        final Handler handler = new Handler();
        timer = new Timer();

        TimerTask verificaTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        new VerificaOponenteEntrouThread(LobbyActivity.this, codigoJogo, nomeJogador1).execute();
                        Log.d("codigojogo", codigoJogo+"");
                    }
                });
            }
        };
        timer.schedule(verificaTask, 0, 4000);
    }
}
