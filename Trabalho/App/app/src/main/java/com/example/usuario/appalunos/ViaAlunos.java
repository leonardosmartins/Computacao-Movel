package com.example.usuario.appalunos;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ViaAlunos {

    public List<Aluno> listaAlunos = new ArrayList<Aluno>();

    public List<Aluno> download(){
        Aluno aluno;

        String url = "http://192.168.0.101:8080/CM/webresources/contato/download";
        try{
            JSONObject primeiro = new JSONObject(HTTPRequest.get(url));
            JSONArray array = primeiro.getJSONArray("contatos");

            for (int i = 0; i < array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                if (!obj.has("erro")){
                    aluno = new Aluno();
                    aluno.setId(Long.parseLong((String) obj.get("id")));
                    aluno.setNome((String) obj.get("nome"));
                    aluno.setTelefone((String) obj.get("telefone"));
                    aluno.setEndereco((String) obj.get("endereco"));
                    aluno.setSite((String) obj.get("site"));
                    aluno.setEmail((String) obj.get("email"));
                   // aluno.setFoto((String) obj.get("foto"));
                    aluno.setNota(Double.parseDouble((String) obj.get("nota")));
                    listaAlunos.add(aluno);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listaAlunos;
    }

    public void upload(List<Aluno> alunos){

        String url = "http://192.168.0.101:8080/CM/webresources/contato/upload";
        HTTPRequest.post(url, alunos);
    }

}