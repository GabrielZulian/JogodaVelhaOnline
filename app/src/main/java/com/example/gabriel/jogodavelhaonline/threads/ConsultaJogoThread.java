package com.example.gabriel.jogodavelhaonline.threads;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.gabriel.jogodavelhaonline.JogoActivity;
import com.example.gabriel.jogodavelhaonline.LobbyActivity;

import org.json.JSONArray;
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

public class ConsultaJogoThread extends AsyncTask<String, String, String> {
    private Integer codigo;
    private JogoActivity jogoActivity;
    String shape;
    ProgressDialog dialog;

    public ConsultaJogoThread(JogoActivity jogoActivity, Integer codigo, String shape) {
        this.jogoActivity = jogoActivity;
        this.codigo = codigo;
        this.shape = shape;
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
            String url = "http://aporteconstrutora.com.br/json/tictactoe/con_jogo.php";
            String charset = "UTF-8";

            String query = URLEncoder.encode("codigo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(codigo), "UTF-8" );
            query += "&" + URLEncoder.encode("shape", "UTF-8") + "=" + URLEncoder.encode(shape, "UTF-8" );

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

            JSONArray jArrayjogo = jObjectGeral.getJSONArray("jogo");
            if (success == 0) {
                Toast.makeText(jogoActivity, "Oponente ainda n jogou!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(jogoActivity, "Oponente Jogou!", Toast.LENGTH_SHORT).show();
                jogoActivity.suaVez = true;
                jogoActivity.campo[0] = jArrayjogo.getJSONObject(0).getInt("0");
                jogoActivity.campo[1] = jArrayjogo.getJSONObject(0).getInt("1");
                jogoActivity.campo[2] = jArrayjogo.getJSONObject(0).getInt("2");
                jogoActivity.campo[3] = jArrayjogo.getJSONObject(0).getInt("3");
                jogoActivity.campo[4] = jArrayjogo.getJSONObject(0).getInt("4");
                jogoActivity.campo[5] = jArrayjogo.getJSONObject(0).getInt("5");
                jogoActivity.campo[6] = jArrayjogo.getJSONObject(0).getInt("6");
                jogoActivity.campo[7] = jArrayjogo.getJSONObject(0).getInt("7");
                jogoActivity.campo[8] = jArrayjogo.getJSONObject(0).getInt("8");
                jogoActivity.atualizaCampo();
                Log.d("Ultimo a jogar= ", jArrayjogo.getJSONObject(0).getString("ultimoJogar"));
            }

        } catch (JSONException e) {
           Toast.makeText(jogoActivity, "OOPs! Algo deu errado..." + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
//        dialog.dismiss();
    }


}
