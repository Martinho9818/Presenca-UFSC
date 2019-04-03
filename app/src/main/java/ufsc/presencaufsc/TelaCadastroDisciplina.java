package ufsc.presencaufsc;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import ufsc.presencaufsc.dao.DB;
import ufsc.presencaufsc.dao.DBLocal;
import ufsc.presencaufsc.dao.DBOpenHelper;
import ufsc.presencaufsc.model.Disciplina;

public class TelaCadastroDisciplina extends AppCompatActivity {

    private EditText editText;
    private Disciplina disciplina;
    private DB db = new DB();
    Toast toast;
    private SQLiteDatabase conexao;
    private DBOpenHelper dbOpenHelper;
    private DBLocal dbLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro_disciplina);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.cadastrar_disciplina);
        criarConexao();
    }

    /* Opções que apareceram na toolbar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tela_cadastro, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* 'Menu' da toolbar */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_salvar:
                salvarDisciplina();
                break;

            case android.R.id.home:
                startActivity(new Intent(this, TelaDisciplinas.class));
                finishAffinity();
                break;
            default:
                break;
        }
        return true;
    }

    /* Método utilizado para cadastrar a disciplina no banco de dados interno e externo */
    public void salvarDisciplina() {
        if (dbLocal.verificaAlunoLocal() != true) {
            Toast.makeText(getApplicationContext(),
                    R.string.tutorialAlunoNaoCadastrado,
                    Toast.LENGTH_LONG).show();
        } else {
            disciplina = new Disciplina();
            editText = (EditText) findViewById((R.id.editTextCodDisciplina));
            /* Verifica codigo da disciplina */
            if (!isEmpty(editText)) {
                disciplina.setCodigoDisciplina(editText.getText().toString());
                editText = (EditText) findViewById(R.id.editTextCodInstituicaoCadastroDisciplina);

                /* Verificando codigo da Instituicao */
                if (!isEmpty(editText)) {
                    disciplina.setCodInstituicaoDisciplina(editText.getText().toString());

                    try {
                        /* Tentando puxar os dados do banco externo para o cadastro da disciplina */
                        String cod_disciplina = disciplina.getCodigoDisciplina();
                        String cod_instituicao = disciplina.getCodInstituicaoDisciplina();
                        if (db.verificaDisciplina(cod_disciplina, cod_instituicao) != false) {
                            disciplina = db.retornaDisciplina(cod_disciplina, cod_instituicao);

                            if (disciplina != null && !disciplina.getNomeDisciplina().equals(" ")) {
                                try {
                                    /* Salva no banco de dados INTERNO */
                                    dbLocal.inserirDisciplina(disciplina);
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            } else {
                                //System.out.println("Não foi possivel retornar a disciplina do banco de dados externo");
                            }
                        } else {
                            toast = Toast.makeText(this, R.string.disciplina_nao_encontrada_no_sistema, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                    if (dbLocal.verificaDisciplina(disciplina.getCodigoDisciplina())) {
                        toast = Toast.makeText(this, R.string.cadastrado_com_sucesso, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();

                        startActivity(new Intent(this, TelaDisciplinas.class));
                        finishAffinity();
                    }
                } else {
                    toast = Toast.makeText(this, R.string.cod_instituicao_vazio, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }

            } else {
                toast = Toast.makeText(this, R.string.cod_disciplina_vazio, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        }
    }

    /* Método responsável por ver se o campo está vazio */
    private boolean isEmpty(EditText etText) {
        String text = etText.getText().toString().trim();
        if (text.length() < 1)
            return true;
        return false;
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
