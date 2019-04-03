package ufsc.presencaufsc;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ufsc.presencaufsc.adapter.DisciplinaAdapter;
import ufsc.presencaufsc.dao.DBLocal;
import ufsc.presencaufsc.dao.DBOpenHelper;
import ufsc.presencaufsc.model.Disciplina;

public class TelaDisciplinas extends AppCompatActivity {

    private RecyclerView lstDisciplinas;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    private SQLiteDatabase conexao;
    private DBOpenHelper dbOpenHelper;
    private DBLocal dbLocal;
    private DisciplinaAdapter disciplinaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_disciplinas);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbLocal.verificaAlunoLocal() != true) {
                    Toast.makeText(getApplicationContext(),
                            R.string.tutorialAlunoNaoCadastrado,
                            Toast.LENGTH_LONG).show();
                } else {
                    Intent it = new Intent(TelaDisciplinas.this, TelaCadastroDisciplina.class);
                    startActivity(it);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Disciplinas");
        lstDisciplinas = (RecyclerView) findViewById(R.id.lstDisciplina);
        criarConexao();
        lstDisciplinas.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lstDisciplinas.setLayoutManager(linearLayoutManager);
        List<Disciplina> listaDisciplina = dbLocal.buscarTodasDisciplinas(dbLocal.buscarAlunoLocal().getCod_instituicao_cadastrada());
        disciplinaAdapter = new DisciplinaAdapter(listaDisciplina);
        lstDisciplinas.setAdapter(disciplinaAdapter);

        tutorial();
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

    /* Método responsável por criar a conexão com o banco local */
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

    /* Método responsável por avisar se o aluno não esta cadastrado. E disciplina */
    public void tutorial() {
        if (dbLocal.verificaAlunoLocal() == true) {
            if (dbLocal.verificaDisciplinaLocal() != true) {
                Toast.makeText(getApplicationContext(),
                        R.string.cadastre_disciplina_tutorial,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.tutorialAlunoNaoCadastrado,
                    Toast.LENGTH_LONG).show();
        }
    }

}
