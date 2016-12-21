package com.example.usuario.appalunos;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Duas constantes
    private final String TAG = "CADASTRO_ALUNO";
    private final String ALUNOS_KEY = "LISTA";

    //Atributos de tela
    private EditText edNome;
    private Button botao;
    private ListView lvListagem;
    private List<Aluno> listaAlunos;

    private ArrayAdapter<Aluno> adapter;

    private int adapterLayout = android.R.layout.simple_list_item_1;

    //Aluno selecionado no click longo da ListView
    private Aluno alunoSelecionado = null;
/*
    @Override //Metodo para salvar o estado da activity
    protected void onSaveInstanceState(Bundle outState){
        //Inclusao da lista de alunos no objeto Bundle.map
        outState.putStringArrayList(ALUNOS_KEY, (ArrayList<Aluno>) listaAlunos);
        //Persistencia do objeto Bundle
        super.onSaveInstanceState(outState);
        //Lancamento de mensagem de log
        Log.i(TAG, "onSaveInstanceState(): " + listaAlunos);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        //Recupera o estado do objeto Bundle
        super.onRestoreInstanceState(savedInstanceState);
        //Carrega lista de alunos do bundle.map
        listaAlunos = savedInstanceState.getStringArrayList(ALUNOS_KEY);
        //Lancamento de mensagem de log
        Log.i(TAG, "onSaveRestoreState(): " + listaAlunos);
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Definicao do objeto Inflater
        MenuInflater inflater = this.getMenuInflater();

        //Inflar um XML em um Menu vazio
        inflater.inflate(R.menu.menu_principal, menu);

        //Exibir o menu
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        getMenuInflater().inflate(R.menu.menu_contexto, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        Intent intent;

        switch (item.getItemId()){
            case R.id.menuDeletar:
                excluirAluno();
                break;
            case R.id.menuLigar:
                intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + alunoSelecionado.getTelefone()));
                startActivity(intent);
                break;
            case R.id.menuEnviarSMS:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("sms:" + alunoSelecionado.getTelefone()));
                intent.putExtra("sms_body", "Mensagem de boas vindas :-)");
                startActivity(intent);
                break;
            case R.id.menuAcharNoMapa:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:0,0?z=14&q=" + alunoSelecionado.getEndereco()));
                intent.putExtra("sms_body", "Mensagem de boas vindas :-)");
                startActivity(intent);
                break;
            case R.id.menuNavegar:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http:" + alunoSelecionado.getSite()));
                startActivity(intent);
                break;
            case R.id.menuEnviarEmail:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[] { alunoSelecionado.getEmail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Falando sobre o curso");
                intent.putExtra(Intent.EXTRA_TEXT, "O curso foi muito legal");
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Verifica o item do menu selecionado
        switch (item.getItemId()){
            case R.id.menu_novo:
                //Criacao do especialista em mudanca de telas
                Intent intent = new Intent(MainActivity.this,
                        CadastroAluno.class);
                //Carregar nova tela
                startActivity(intent);

                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clickDownload(){
        ViaAlunos via = new ViaAlunos();

        List<Aluno> lista = via.download();
        AlunoDAO dao = new AlunoDAO(this);
        dao.deleteAll();
        for(Aluno aluno: lista){
            dao.cadastrar(aluno);
        }
    }

    private void clickUpload(){

        ViaAlunos via = new ViaAlunos();
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> lista = dao.listar();
        via.upload(lista);
    }

    private void carregarLista(){
        //Criacao do objeto DAO - inicio da conexao com o BD
        AlunoDAO dao = new AlunoDAO(this);
        ViaAlunos via = new ViaAlunos();
        // chamada ao metodo listar

        this.listaAlunos = dao.listar();
        // fim da conexao com o BD
        dao.close();

        // O objeto ArrayAdapter sabe converter listas ou vetores em View
        this.adapter = new ArrayAdapter<Aluno>(this, adapterLayout, listaAlunos);
        // Associacao do Adapter aa ListView
        this.lvListagem.setAdapter(adapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        // Carga da colecao de Alunos
        this.carregarLista();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ligacao da tela ao controlador
        setContentView(R.layout.activity_main);

       Button botaodownload = (Button) findViewById(R.id.botaodownload);
        botaodownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        clickDownload();
                    }
                }).start();
            }
        });

        Button botaoupload = (Button) findViewById(R.id.botaoupload);
        botaoupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        clickUpload();
                    }
                }).start();
            }
        });


        // Ligacao dos componentes de TELA aos atributos da Activity
        lvListagem = (ListView) findViewById(R.id.lvListagem);
        // Informa que a ListView tem Menu de Contexto
        registerForContextMenu(lvListagem);
/*
        // Inicializacao da Colecao de Alunos
        listaAlunos = new ArrayList<Aluno>();
        // O objeto ArrayAdapter sabe converter listas ou vetores em View
        adapter = new ArrayAdapter<Aluno>(this, adapterLayout, listaAlunos);
        // Associacao do Adapter aa ListView
        lvListagem.setAdapter(adapter);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaAlunos.add(edNome.getText().toString());
                edNome.setText("");
                adapter.notifyDataSetChanged();
            }
        });
*/
        lvListagem.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id){
                Intent form = new Intent(MainActivity.this, CadastroAluno.class);

                alunoSelecionado = (Aluno) lvListagem.getItemAtPosition(posicao);
                form.putExtra("ALUNO_SELECIONADO", alunoSelecionado);
                startActivity(form);
//                Toast.makeText(MainActivity.this,
//                        "Aluno: " + listaAlunos.get(posicao), Toast.LENGTH_LONG)
//                        .show();
            }
        });

        lvListagem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int posicao, long id) {
                //Marca o aluno selecionado na ListView
                alunoSelecionado = (Aluno) adapter.getItem(posicao);
                Log.i(TAG, "Aluno selecionado ListView.longClick()" +
                alunoSelecionado.getNome());
//                Toast.makeText(MainActivity.this,
//                        "Aluno: " + listaAlunos.get(posicao) + " [click longo]",
//                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void excluirAluno(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirma a exclusão de: " +
            alunoSelecionado.getNome());

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int witch) {
                AlunoDAO dao = new AlunoDAO(MainActivity.this);
                dao.deletar(alunoSelecionado);
                dao.close();
                carregarLista();
                alunoSelecionado = null;
            }
        });

        builder.setNegativeButton("Não", null);
        AlertDialog dialog = builder.create();
        dialog.setTitle("Confirmação de operação");
        dialog.show();
    }
}
