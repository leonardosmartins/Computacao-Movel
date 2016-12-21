package com.example.usuario.appalunos;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AlunoDAO extends SQLiteOpenHelper{

    //Constantes para auxilio no controle de versoes
    private static final int VERSAO = 1;
    private static final String TABELA = "Aluno";
    private static final String DATABASE = "MPAlunos";

    //Constante para log no Logcat
    private static final String TAG = "CADASTRO_ALUNO";

    public AlunoDAO(Context context){
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        //Definicao do comando DDL a ser executado
        String ddl = "CREATE TABLE " + TABELA + "( "
                + "id INTEGER PRIMARY KEY, "
                + "nome TEXT, telefone TEXT, endereco TEXT, "
                + "site TEXT, email TEXT, foto TEXT, "
                + "nota REAL)";

        //Execucao do comando no SQLite
        database.execSQL(ddl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int versaoAntiga, int versaoNova) {

        // Definicao do comando para destruir a tabela Aluno
        String sql = "DROP TABLE IF EXISTS " + TABELA;

        // Execucao do comando de destruicao
        database.execSQL(sql);

        // Chamada ao metodo de construcao da base de dados
        onCreate(database);

    }

    public void cadastrar(Aluno aluno){

        // Objeto para armazenar os valores dos campos
        ContentValues values = new ContentValues();

        // Definicao de valores dos campos da tabela
        values.put("nome", aluno.getNome());
        values.put("id", aluno.getId());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("email", aluno.getEmail());
       // values.put("foto", aluno.getFoto());
        values.put("nota", aluno.getNota());

        // Inserir dados do Aluno no BD
        getWritableDatabase().insert(TABELA, null, values);
        Log.i(TAG, "Aluno cadastrado: " + aluno.getNome());
    }

    public List<Aluno> listar(){
        //Definicao da colecao de alunos
        List<Aluno> lista = new ArrayList<Aluno>();

        //Definicao da instrucao SQL
        String sql = "Select * from Aluno order by nome";

        //Objeto que recebe os registros do BD
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);

        try{
            while(cursor.moveToNext()){
                // Criacao de nova referencia para Aluno
                Aluno aluno = new Aluno();
                // Carregar os atributos de Aluno com dados do BD
                aluno.setId(cursor.getLong(0));
                aluno.setNome(cursor.getString(1));
                aluno.setTelefone(cursor.getString(2));
                aluno.setEndereco(cursor.getString(3));
                aluno.setSite(cursor.getString(4));
                aluno.setEmail(cursor.getString(5));
                aluno.setFoto(cursor.getString(6));
                aluno.setNota(cursor.getDouble(7));
                // Adicionar novo Aluno aa lista
                lista.add(aluno);
            }
        } catch (SQLException e){
            Log.e(TAG, e.getMessage());
        } finally {
            cursor.close();
        }
        return lista;
    }

    public void deleteAll(){
        //Definicao da instrucao SQL
        String sql = "DELETE FROM Aluno";
        getWritableDatabase().execSQL(sql);

    }

    public void deletar(Aluno aluno){
        // Definicao de array de parametros
        String[] args = {aluno.getId().toString()};

        //Exclusao do Aluno
        getWritableDatabase().delete(TABELA, "id=?", args);

        Log.i(TAG, "Aluno deletado: " + aluno.getNome());
    }

    public void alterar(Aluno aluno){
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("email", aluno.getEmail());
        values.put("foto", aluno.getFoto());
        values.put("nota", aluno.getNota());

        // Colecao de valores de parametros do SQL
        // (valores dos parametros da clausula WHERE)
        String[] args = {aluno.getId().toString()};

        // Altera dados do Aluno no BD
        getWritableDatabase().update(TABELA, values, "id=?", args);
        Log.i(TAG, "Aluno alterado: " + aluno.getNome());
    }
}
