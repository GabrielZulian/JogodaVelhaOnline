package com.example.gabriel.jogodavelhaonline.threads;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.gabriel.jogodavelhaonline.InicialActivity;
import com.example.gabriel.jogodavelhaonline.JogoActivity;
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

public class EntraJogoThread extends AsyncTask<String, String, String> {
    private Integer codigoJogo;
    private InicialActivity inicialActivity;
    private String nomeJogador2;
    ProgressDialog dialog;

    public EntraJogoThread(InicialActivity inicialActivity, Integer codigoJogo, String nomeJogador2) {
        this.inicialActivity = inicialActivity;
        this.codigoJogo = codigoJogo;
        this.nomeJogador2 = nomeJogador2;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(inicialActivity );
        dialog.setTitle("Carregando...");
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String url = "http://aporteconstrutora.com.br/json/tictactoe/entra_partida.php";
            String charset = "UTF-8";

            String query = URLEncoder.encode("codigo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(codigoJogo), "UTF-8" );
            query += "&" + URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nomeJogador2, "UTF-8" );

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
            String jogador1 = jObjectGeral.getString("jogador1");

            if (success == 0 && jogador1.equals("null")) {
                Toast.makeText(inicialActivity, "Partida não encontrada", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(inicialActivity, "Oponente encontrado!", Toast.LENGTH_LONG).show();

                abrePartida(jogador1);
            }

        } catch (JSONException e) {
            Toast.makeText(inicialActivity, "OOPs! Algo deu errado..." + e.getMessage(), Toast.LENGTH_LONG).show();
        }
//        dialog.dismiss();
    }

    private void abrePartida(String jogador1) {
        Intent intent = new Intent(inicialActivity, JogoActivity.class);
        intent.putExtra("codigoJogo", codigoJogo);
        intent.putExtra("jogador1", jogador1);
        intent.putExtra("jogador2", nomeJogador2);
        intent.putExtra("criador", false);
        inicialActivity.startActivity(intent);
        inicialActivity.finish();
    }
}
