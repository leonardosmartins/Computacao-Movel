package com.example.usuario.appalunos;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class HTTPRequest {

    public static String get(String urlToRead) {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public static void post(String urlToRead, List<Aluno> alunos) {

        try {
            Gson gson = new Gson();
            String alunosJSON = gson.toJson(alunos);
            String param = "{\"contatos\":" + alunosJSON + "}";

            StringBuilder result = new StringBuilder();

            result.append(URLEncoder.encode("list", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(param, "UTF-8"));

            String query = result.toString();
            URL url = new URL(urlToRead + "?" + query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(query.getBytes("UTF-8"));
            os.flush();
            os.close();
            conn.getResponseCode();


        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}