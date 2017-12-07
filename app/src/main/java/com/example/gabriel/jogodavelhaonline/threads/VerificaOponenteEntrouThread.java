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

public class VerificaOponenteEntrouThread extends AsyncTask<String, String, String> {
    private Integer codigo;
    private LobbyActivity lobbyActivity;
    private String nomeJogador1;
    ProgressDialog dialog;

    public VerificaOponenteEntrouThread(LobbyActivity lobbyActivity, Integer codigo, String nomeJogador1) {
        this.lobbyActivity = lobbyActivity;
        this.codigo = codigo;
        this.nomeJogador1 = nomeJogador1;
    }

    @Override
    protected void onPreExecute() {
//        dialog = new ProgressDialog(lobbyActivity);
//        dialog.setTitle("Carregando...");
//        dialog.setIndeterminate(true);
//        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String url = "http://aporteconstrutora.com.br/json/tictactoe/verifica_oponente_entrou.php";
            String charset = "UTF-8";

            String query = URLEncoder.encode("codigo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(codigo), "UTF-8" );

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
            String jogador2 = jObjectGeral.getString("jogador2");

            if (success == 0 && jogador2.equals("null")) {

            } else {
                Toast.makeText(lobbyActivity, "Oponente encontrado!", Toast.LENGTH_LONG).show();

                abrePartida(jogador2);
            }

        } catch (JSONException e) {
            Toast.makeText(lobbyActivity, "OOPs! Algo deu errado..." + e.getMessage(), Toast.LENGTH_LONG).show();
        }
//        dialog.dismiss();
    }

    private void abrePartida(String jogador2) {
        Intent intent = new Intent(lobbyActivity, JogoActivity.class);
        intent.putExtra("codigoJogo", codigo);
        intent.putExtra("jogador1", nomeJogador1);
        intent.putExtra("jogador2", jogador2);
        intent.putExtra("criador", true);
        lobbyActivity.startActivity(intent);
        lobbyActivity.finish();
    }


}
