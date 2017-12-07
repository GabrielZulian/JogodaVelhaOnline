package com.example.gabriel.jogodavelhaonline.threads;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

public class ReiniciaJogoThread extends AsyncTask<String, String, String> {
    private Integer codigo;
    private JogoActivity jogoActivity;
    ProgressDialog dialog;

    public ReiniciaJogoThread(JogoActivity jogoActivity, Integer codigo) {
        this.jogoActivity = jogoActivity;
        this.codigo = codigo;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(jogoActivity);
        dialog.setTitle("Carregando...");
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String url = "http://aporteconstrutora.com.br/json/tictactoe/zera_partida.php";
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

            if (success == 0) {

            } else {
                jogoActivity.reiniciaPartida();
                Toast.makeText(jogoActivity, "zerado!", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Toast.makeText(jogoActivity, "OOPs! Algo deu errado..." + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }
}
