package com.example.usuario.appalunos;


import java.io.Serializable;

public class Aluno implements Serializable{
    private Long id;
    private String nome;
    private String telefone;
    private String endereco;
    private String site;
    private String email;
    private String foto;
    private Double nota;

    @Override
    public String toString(){
        return nome;
    }

    public Long getId(){
        return id;
    }
    public String getNome(){
        return nome;
    }
    public String getTelefone(){
        return telefone;
    }
    public String getEndereco(){
        return endereco;
    }
    public String getSite(){
        return site;
    }
    public String getEmail(){
        return email;
    }
    public String getFoto(){
        return foto;
    }
    public Double getNota(){
        return nota;
    }

    public void setId(Long id){
        this.id = id;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setTelefone(String telefone){
        this.telefone = telefone;
    }
    public void setEndereco(String endereco){
        this.endereco = endereco;
    }
    public void setSite(String site){
        this.site = site;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setFoto(String foto){
        this.foto = foto;
    }
    public void setNota(Double nota){
        this.nota = nota;
    }


}
