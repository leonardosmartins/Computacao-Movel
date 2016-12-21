package com.example.usuario.appalunos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;

public class CadastroAluno extends AppCompatActivity {

    private Button botao;
    private FormularioHelper helper;
    private Aluno alunoParaSerAlterado;
    private ImageView mImage;

    // Variaveis para o controle de camera
    private String localArquivo;
    // Constante usada como requestCode
    private static final int FAZER_FOTO = 123;

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data){
        // Verificacao do resultado da nossa requisicao
        if (requestCode == FAZER_FOTO){
            if (resultCode == Activity.RESULT_OK)
                helper.carregarFoto(this.localArquivo);
            else
                localArquivo = null;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_aluno);


        mImage = (ImageView) findViewById(R.id.foto);

        //Criacao do objeto Helper
        helper = new FormularioHelper(this);

        // Listener da foto
        helper.getFoto().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localArquivo = Environment.getExternalStorageDirectory()
                        + "/" + System.currentTimeMillis() + ".jpg";
                File arquivo = new File(localArquivo);
                // URI que informa onde o arquivo resultado deve ser armazenado
                Uri localFoto = Uri.fromFile(arquivo);

                Intent irParaCamera = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);

                irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, localFoto);
                // chamando a camera
                startActivityForResult(irParaCamera, FAZER_FOTO);
            }
        });

        //Busca o aluno a ser alterado
        alunoParaSerAlterado = (Aluno) getIntent().getSerializableExtra(
                "ALUNO_SELECIONADO");
        if (alunoParaSerAlterado != null){
            //Atualiza a tela com dados do Aluno
            helper.setAluno(alunoParaSerAlterado);
        }


        //Associar componentes de view ao controller
        EditText edNome = (EditText) findViewById(R.id.edNome);
        EditText edTelefone = (EditText) findViewById(R.id.edTelefone);
        EditText edSite = (EditText) findViewById(R.id.edSite);
        EditText edEmail = (EditText) findViewById(R.id.edEmail);
        EditText edEndereco = (EditText) findViewById(R.id.edEndereco);
        SeekBar sbNota = (SeekBar) findViewById(R.id.sbNota);

        //Definicao do novo objeto Aluno
        Aluno aluno = new Aluno();

        //Pegar dados da tela e setar nos atributos de Aluno
        aluno.setNome(edNome.getText().toString());
        aluno.setTelefone(edTelefone.getText().toString());
        aluno.setSite(edSite.getText().toString());
        aluno.setEmail(edEmail.getText().toString());
        aluno.setEndereco(edEndereco.getText().toString());
        aluno.setNota(Double.valueOf(sbNota.getProgress()));

        botao = (Button) findViewById(R.id.sbSalvar);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Utilizacao do Helper
                Aluno aluno = helper.getAluno();

                // Criacao do objeto DAO - inicio da conexao com o BD
                AlunoDAO dao = new AlunoDAO(CadastroAluno.this);

                if (aluno.getId() == null)
                    dao.cadastrar(aluno);
                else
                    dao.alterar(aluno);
                // Encerramento da conexao com o BD
                dao.close();

                //Encerrar a tela atual
                finish();
            }
        });
    }
}
