package ufsc.presencaufsc;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import ufsc.presencaufsc.dao.DBLocal;
import ufsc.presencaufsc.dao.DBOpenHelper;
import ufsc.presencaufsc.model.Aluno;

public class TelaInformacoesAluno extends AppCompatActivity {

    private TextView nomeAluno;
    private TextView matriculaAluno;
    private TextView emailAluno;
    private TextView codInstituicao;
    private SQLiteDatabase conexao;
    private DBOpenHelper dbOpenHelper;
    private DBLocal dbLocal;
    private Aluno aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_informacoes_aluno);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Informações do Aluno");
        nomeAluno = (TextView) findViewById(R.id.textViewNomeAluno);
        matriculaAluno = (TextView) findViewById(R.id.textViewMatriculaAluno);
        emailAluno = (TextView) findViewById(R.id.textViewEmailALuno);
        codInstituicao = (TextView) findViewById(R.id.textViewCodInstituicaoALuno);
        criarConexao();
        carregarAlunoTela();
    }

    /* 'Menu' da toolbar */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, TelaInicial.class));
                finishAffinity();
                break;
            default:
                break;
        }
        return true;
    }

    /* Carregamento do aluno */
    public void carregarAlunoTela() {
        if (dbLocal.verificaAlunoLocal() == true) {
            this.aluno = dbLocal.buscarAlunoLocal();
            nomeAluno.setText(aluno.getNome());
            matriculaAluno.setText(aluno.getMatricula());
            emailAluno.setText(aluno.getEmail());
            codInstituicao.setText(aluno.getCod_instituicao_cadastrada());
        } else {
            nomeAluno.setText("Sem aluno ainda");
            matriculaAluno.setText("Sem aluno ainda");
            emailAluno.setText("Sem aluno ainda");
            codInstituicao.setText("Sem aluno ainda");
        }
    }

    /* Método responsável pela criação da conexão com o banco de dados local */
    private void criarConexao() {
        try {
            dbOpenHelper = new DBOpenHelper(this);
            conexao = dbOpenHelper.getWritableDatabase();
            dbLocal = new DBLocal(conexao);
        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Erro");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("Ok", null);
            dlg.show();
        }
    }
}
