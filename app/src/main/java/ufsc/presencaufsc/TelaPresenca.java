package ufsc.presencaufsc;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.List;

import ufsc.presencaufsc.adapter.AulaAdapter;
import ufsc.presencaufsc.dao.DBLocal;
import ufsc.presencaufsc.dao.DBOpenHelper;
import ufsc.presencaufsc.model.Aluno;
import ufsc.presencaufsc.model.Aula;
import ufsc.presencaufsc.model.Disciplina;

public class TelaPresenca extends AppCompatActivity {

    private SQLiteDatabase conexao;
    private DBOpenHelper dbOpenHelper;
    private DBLocal dbLocal;
    private RecyclerView lstPresenca;
    private AulaAdapter aulaAdapter;
    private List<Aula> listaPresenca;
    private Aluno aluno;
    private Disciplina disciplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_presenca);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Presenças");
        lstPresenca = (RecyclerView) findViewById(R.id.lstPresencas);
        criarConexao();
        verificaParametro();
        lstPresenca.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lstPresenca.setLayoutManager(linearLayoutManager);

        try {
            aluno = dbLocal.buscarAlunoLocal();
            this.listaPresenca = dbLocal.buscarPresencaDaDisciplina(aluno.getMatricula(), disciplina.getCodigoDisciplina());
        } catch (Exception e) {
            System.out.println(e);
        }
        aulaAdapter = new AulaAdapter(listaPresenca);
        lstPresenca.setAdapter(aulaAdapter);
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

    /* Método que verifica os parametros do adapter e seta a posiçaõ do RecicledView (no caso disciplina) */
    public void verificaParametro() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("DISCIPLINA")) {
            this.disciplina = (Disciplina) bundle.getSerializable("DISCIPLINA");
        }
    }
}
