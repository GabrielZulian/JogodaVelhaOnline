package com.example.gabriel.jogodavelhaonline.threads;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.gabriel.jogodavelhaonline.InicialActivity;
import com.example.gabriel.jogodavelhaonline.LobbyActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Created by gabriel on 05/12/2017.
 */

public class CriaJogoThread extends AsyncTask<String, String, String> {
    private String nome;
    private InicialActivity inicialActivity;
    ProgressDialog dialog;

    public CriaJogoThread(InicialActivity inicialActivity, String nome) {
        this.inicialActivity = inicialActivity;
        this.nome = nome;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(inicialActivity);
        dialog.setTitle("Carregando...");
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String url = "http://aporteconstrutora.com.br/json/tictactoe/cria_partida.php";
            String charset = "UTF-8";

            String query = URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8" );

            URLConnection connection = new URL(url + "?" + query).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            InputStream response = connection.getInputStream();

            String responseBody;
            try (Scanner scanner = new Scanner(response)) {
                responseBody = scanner.useDelimiter("\\A").next();
            }

            Log.d("Result", "[" + responseBody + "]");
            return responseBody;
        } catch(Exception e) {
            Log.d("Erro", e.getMessage() + "Erro");
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("Resultado", "[" + result + "]");

        try {
            JSONObject jObjectGeral = new JSONObject(result);

            int success = jObjectGeral.getInt("success");

            if (success == 0) {
                Toast.makeText(inicialActivity, "OOPs! Algo deu errado.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(inicialActivity, "Jogo criado com sucesso!", Toast.LENGTH_LONG).show();
                int codigoJogo = jObjectGeral.getInt("codigo");

                abreLobby(codigoJogo);
            }

        } catch (JSONException e) {
            Toast.makeText(inicialActivity, "OOPs! Algo deu errado." + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }

    private void abreLobby(int codigoJogo) {
        Intent intent = new Intent(inicialActivity, LobbyActivity.class);

        intent.putExtra("codigoJogo", codigoJogo);
        intent.putExtra("nomeJogador1", nome);
        inicialActivity.startActivity(intent);
    }
}
