package com.example.gabriel.jogodavelhaonline;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.jogodavelhaonline.threads.ConsultaJogoThread;
import com.example.gabriel.jogodavelhaonline.threads.FazJogadaThread;

import java.util.Timer;
import java.util.TimerTask;

public class JogoActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton[] btns = new ImageButton[9];
    TextView tvNomeJogador1, tvNomeJogador2, tvVitoriasJogador1, tvVitoriasJogador2, tvVisor;
    Drawable suaImagem = null;
    Drawable imagemOponente = null;

    Integer codigoJogo;

    public Integer campo[] = {0,0,0,0,0,0,0,0,0};
    static final Integer VAZIO = 0;
    static final Integer XIS = 1;
    static final Integer CIRCULO = 2;
    boolean jogador1 = false;
    public boolean suaVez = true;

    String seuShape;
    String shapeOponente;
    Timer timer;

    Integer vitoriasJog1 = 0;
    Integer vitoriasJog2 = 0;
    private boolean primeiraJogada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        btns[0] = (ImageButton) findViewById(R.id.btn0);
        btns[1] = (ImageButton) findViewById(R.id.btn1);
        btns[2] = (ImageButton) findViewById(R.id.btn2);
        btns[3] = (ImageButton) findViewById(R.id.btn3);
        btns[4] = (ImageButton) findViewById(R.id.btn4);
        btns[5] = (ImageButton) findViewById(R.id.btn5);
        btns[6] = (ImageButton) findViewById(R.id.btn6);
        btns[7] = (ImageButton) findViewById(R.id.btn7);
        btns[8] = (ImageButton) findViewById(R.id.btn8);

        tvNomeJogador1 = (TextView) findViewById(R.id.txtNomeJogador1);
        tvNomeJogador2 = (TextView) findViewById(R.id.txtNomeJogador2);
        tvVitoriasJogador1  = (TextView) findViewById(R.id.tvVitoriasJogador1);
        tvVitoriasJogador2  = (TextView) findViewById(R.id.tvVitoriasJogador2);
        tvVisor = (TextView) findViewById(R.id.txtVisorJogo);

        for (ImageButton btn : btns) {
            btn.setOnClickListener(this);
            btn.setImageDrawable(null);
        }

        Bundle extras = getIntent().getExtras();
        String nomeJogador1="", nomeJogador2="";
        if (extras != null) {
            nomeJogador1 = extras.getString("jogador1");
            nomeJogador2 = extras.getString("jogador2");
            codigoJogo = extras.getInt("codigoJogo");
            jogador1 = extras.getBoolean("criador", false);
        }

        tvNomeJogador1.setText(nomeJogador1);
        tvNomeJogador2.setText(nomeJogador2);

        iniciaJogo();
    }

    private void iniciaJogo() {
        if(jogador1) {
            primeiraJogada = true;
            tvVisor.setText("Sua vez!");
            seuShape = "x";
            shapeOponente = "o";
            suaImagem = ContextCompat.getDrawable(getApplicationContext(), R.drawable.xis);
            imagemOponente = ContextCompat.getDrawable(getApplicationContext(), R.drawable.circulo);
            suaVez = true;
            for (ImageButton btn : btns) {
                btn.setEnabled(true);
            }
        } else {
            seuShape = "o";
            shapeOponente = "x";
            suaImagem = ContextCompat.getDrawable(getApplicationContext(), R.drawable.circulo);
            imagemOponente = ContextCompat.getDrawable(getApplicationContext(), R.drawable.xis);
            finalizaJogada();
        }
    }

    @Override
    public void onClick(View view) {

        ImageButton botao = (ImageButton) view;

        switch (botao.getId()) {
            case R.id.btn0: realizajogada(btns[0], 0);
                break;
            case R.id.btn1: realizajogada(btns[1], 1);
                break;
            case R.id.btn2: realizajogada(btns[2], 2);
                break;
            case R.id.btn3: realizajogada(btns[3], 3);
                break;
            case R.id.btn4: realizajogada(btns[4], 4);
                break;
            case R.id.btn5: realizajogada(btns[5], 5);
                break;
            case R.id.btn6: realizajogada(btns[6], 6);
                break;
            case R.id.btn7: realizajogada(btns[7], 7);
                break;
            case R.id.btn8: realizajogada(btns[8], 8);
                break;
        }
    }

    private void realizajogada(ImageButton btn, Integer posicao) {
        if (suaVez) {
            if (campo[posicao] == VAZIO) {
                btn.setImageDrawable(suaImagem);
                new FazJogadaThread(JogoActivity.this, codigoJogo, posicao, (seuShape.equals("x")?XIS:CIRCULO)).execute();
                if(!verificaSeGanhou())
                    finalizaJogada();
            }
        }
    }

    private void finalizaJogada() {
        suaVez = false;
        tvVisor.setText("Vez do oponente...");
        for (ImageButton btn : btns) {
            btn.setEnabled(false);
        }
        final Handler handler = new Handler();
        timer = new Timer();

        final TimerTask verificaTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        new ConsultaJogoThread(JogoActivity.this, codigoJogo, seuShape).execute();

                        if(suaVez) {
                            Log.d("teste", "ttt");
                            timer.cancel();
                            atualizaCampo();
                        }
                    }
                });
            }
        };
        timer.schedule(verificaTask, 0, 4000);
    }

    public void atualizaCampo() {
        for (int x = 0; x<9; x++) {
            if (campo[x] == XIS)
                btns[x].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.xis));
            else if (campo[x] == CIRCULO)
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.circulo);
        }
        if(!verificaSeGanhou())
            habilitaVez();
    }

    private void habilitaVez() {
        for (ImageButton btn : btns) {
            btn.setEnabled(true);
        }
    }

    private boolean verificaSeGanhou() {
        if (campo[0] == XIS && campo[1] == XIS && campo[2] == XIS
                || campo[3] == XIS && campo[4] == XIS && campo[5] == XIS
                || campo[6] == XIS && campo[7] == XIS && campo[8] == XIS
                || campo[0] == XIS && campo[3] == XIS && campo[6] == XIS
                || campo[1] == XIS && campo[4] == XIS && campo[7] == XIS
                || campo[2] == XIS && campo[5] == XIS && campo[8] == XIS
                || campo[0] == XIS && campo[4] == XIS && campo[8] == XIS
                || campo[2] == XIS && campo[4] == XIS && campo[6] == XIS) {
            if (jogador1) {
                Toast.makeText(this, "Parabéns " + tvNomeJogador1.getText().toString() + ", você venceu!", Toast.LENGTH_LONG).show();
                vitoriasJog1++;
                tvVitoriasJogador1.setText(vitoriasJog1+"");
            } else {
                Toast.makeText(this, "Você perdeu!", Toast.LENGTH_LONG).show();
                vitoriasJog2++;
                tvVitoriasJogador2.setText(vitoriasJog2+"");
            }
            reiniciaPartida();
            return true;
        } else if (campo[0] == CIRCULO && campo[1] == CIRCULO && campo[2] == CIRCULO
                || campo[3] == CIRCULO && campo[4] == CIRCULO && campo[5] == CIRCULO
                || campo[6] == CIRCULO && campo[7] == CIRCULO && campo[8] == CIRCULO
                || campo[0] == CIRCULO && campo[3] == CIRCULO && campo[6] == CIRCULO
                || campo[1] == CIRCULO && campo[4] == CIRCULO && campo[7] == CIRCULO
                || campo[2] == CIRCULO && campo[5] == CIRCULO && campo[8] == CIRCULO
                || campo[0] == CIRCULO && campo[4] == CIRCULO && campo[8] == CIRCULO
                || campo[2] == CIRCULO && campo[4] == CIRCULO && campo[6] == CIRCULO) {
            if (!jogador1) {
                Toast.makeText(this, "Parabéns " + tvNomeJogador1.getText().toString() + ", você venceu!", Toast.LENGTH_LONG).show();
                vitoriasJog2++;
                tvVitoriasJogador2.setText(vitoriasJog2+"");
            }  else {
                Toast.makeText(this, "Você perdeu!", Toast.LENGTH_LONG).show();
                vitoriasJog1++;
                tvVitoriasJogador1.setText(vitoriasJog1+"");
            }
            reiniciaPartida();
            return true;
        }
        return false;
    }

    public void reiniciaPartida() {
        for (int x = 0; x < 9; x++){
            btns[x].setImageDrawable(null);
            campo[x] = VAZIO;
        }
    }
}
