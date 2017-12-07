package com.example.gabriel.jogodavelhaonline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gabriel.jogodavelhaonline.threads.CriaJogoThread;
import com.example.gabriel.jogodavelhaonline.threads.EntraJogoThread;

public class InicialActivity extends AppCompatActivity {

    EditText edtNomeJogador, edtCodigoJogo;
    Button btnCriarJogo, btnEntrarJogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        edtNomeJogador = (EditText) findViewById(R.id.edtNome);
        edtCodigoJogo = (EditText) findViewById(R.id.edtCodigoJogo);
        btnCriarJogo = (Button) findViewById(R.id.btnCriarJogo);
        btnEntrarJogo = (Button) findViewById(R.id.btnEntrarJogo);

        btnCriarJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = edtNomeJogador.getText().toString();
                new CriaJogoThread(InicialActivity.this, nome).execute();
            }
        });

        btnEntrarJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = edtNomeJogador.getText().toString();
                Integer codigoJogo = Integer.valueOf(edtCodigoJogo.getText().toString());
                new EntraJogoThread(InicialActivity.this, codigoJogo, nome).execute();
            }
        });
    }
}
