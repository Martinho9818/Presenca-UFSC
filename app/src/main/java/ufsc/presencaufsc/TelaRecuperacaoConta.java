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

import java.util.ArrayList;
import java.util.List;

import ufsc.presencaufsc.dao.DB;
import ufsc.presencaufsc.dao.DBLocal;
import ufsc.presencaufsc.dao.DBOpenHelper;
import ufsc.presencaufsc.model.Aluno;
import ufsc.presencaufsc.model.Aula;
import ufsc.presencaufsc.model.Disciplina;
import ufsc.presencaufsc.model.Instituicao;

public class TelaRecuperacaoConta extends AppCompatActivity {

    private EditText txtMatricula;
    private EditText txtEmail;
    private EditText txtSenhaConfirmacao;
    private EditText txtCodInstituicao;

    private DB db = new DB();
    Toast toast;
    private SQLiteDatabase conexao;
    private DBOpenHelper dbOpenHelper;
    private DBLocal dbLocal;

    private String matricula;
    private String email;
    private String senhaRecuperacao;
    private String codInstituicao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_recuperacao_conta);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Recuperação de Conta");

        txtMatricula = (EditText) findViewById(R.id.editTextMatriculaRecuperacao);
        txtEmail = (EditText) findViewById(R.id.editTextEmailRecuperacao);
        txtSenhaConfirmacao = (EditText) findViewById(R.id.editTextSenhaRecuperacao);
        txtCodInstituicao = (EditText) findViewById(R.id.editTextCodInstituicaoRecuperacao);

        criarConexao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tela_recuperacao, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* 'Menu' da toolbar */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                verificaDadosRecuperacao();
                break;

            case android.R.id.home:
                startActivity(new Intent(this, TelaInicial.class));
                finishAffinity();
                break;
            default:
                break;
        }
        return true;
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

    /* Método responsável por ver se o campo está vazio */
    private boolean isEmpty(EditText etText) {
        String text = etText.getText().toString().trim();
        if (text.length() < 1)
            return true;
        return false;
    }

    /* Método responsável por verificar se o numero é um int */
    public boolean verificandoInt(String pVariavel) {
        String variavel = pVariavel;
        try {
            Integer num = new Integer(variavel);
            return true;
        } catch (NumberFormatException nfc) {
            return false;
        }
    }

    /* Método que verifica os dados requisitados para recuperação e então se estiver tudo certo puxa os dados do banco externo para interno */
    public void verificaDadosRecuperacao() {
        /* Verificando matricula */
        if (!this.isEmpty(txtMatricula)) {
            String variavel = txtMatricula.getText().toString();
            if (verificandoInt(variavel)) {
                this.matricula = txtMatricula.getText().toString();
                /* Verificando email */
                if (!this.isEmpty(txtEmail)) {
                    this.email = txtEmail.getText().toString();
                    /* Verificando o código da instituição */
                    if (!this.isEmpty(txtCodInstituicao)) {
                        this.codInstituicao = txtCodInstituicao.getText().toString();
                        /* Verificando senha */
                        if (!this.isEmpty(txtSenhaConfirmacao)) {
                            this.senhaRecuperacao = txtSenhaConfirmacao.getText().toString();
                            try {
                                if (db.verificacaoParaRecuperacaoAluno(matricula, email, senhaRecuperacao, codInstituicao) == true) {
                                    /* Pega as informações do aluno no db externo e cadastra no interno */
                                    Aluno aluno1 = db.retornaAluno(matricula);
                                    try {
                                        dbLocal.inserirAluno(aluno1);
                                        dbLocal.inserirDadosPendentes(0);
                                        /* Verifica se é possivel cadastrar a instituição e cadastra no banco local */
                                        if (aluno1.getCod_instituicao_cadastrada() != null && !aluno1.getCod_instituicao_cadastrada().equals(" ")) {
                                            if (db.verificaInstituicao(aluno1.getCod_instituicao_cadastrada()) != false) {
                                                Instituicao instituicao = db.retornaInstituicao(aluno1.getCod_instituicao_cadastrada());
                                                if (instituicao != null && !instituicao.getCod_instituicao().equals(" ")) {
                                                    try {
                                                        dbLocal.inserirInstituicao(instituicao);
                                                    } catch (Exception e) {
                                                        System.out.println(e);
                                                    }
                                                } else {
                                                    //System.out.println("Instituicao está null");
                                                }
                                            } else {
                                                //System.out.println("Instituicao não cadastrada no sistema.");
                                            }
                                        }
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
                                    toast = Toast.makeText(this, R.string.sucesso_sincronizando, Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                    toast.show();
                                    recuperaDados(aluno1.getMatricula(), aluno1.getCod_instituicao_cadastrada());
                                    startActivity(new Intent(this, TelaInicial.class));
                                    finishAffinity();
                                } else {
                                    toast = Toast.makeText(this, R.string.erro_dados_nao_batem, Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                    toast.show();
                                }
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        } else {
                            toast = Toast.makeText(this, R.string.digite_senha_recuperacao, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    } else {
                        toast = Toast.makeText(this, R.string.digite_cod_instituicao, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }
                } else {
                    toast = Toast.makeText(this, R.string.campo_email_vazio, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            } else {
                toast = Toast.makeText(this, R.string.digite_numero_matricula, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        } else {
            toast = Toast.makeText(this, R.string.campo_matricula_vazio, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    /* Método responsável por recuperar os dados do banco externo para interno */
    public void recuperaDados(String pCod_aluno, String pCod_instituicao) {
        List<Disciplina> listaDisciplinas = new ArrayList<Disciplina>();
        List<String> listaNomeDisciplinas = null;
        List<Aula> listaAulas = null;
        String cod_aluno = pCod_aluno;
        String cod_instituicao = pCod_instituicao;
        Disciplina obj;
        try {
            listaNomeDisciplinas = db.buscarDisciplinasCadastradas(cod_aluno, cod_instituicao);
            int t = 0;
            for (t = 0; t < listaNomeDisciplinas.size(); t++) {
                obj = new Disciplina();
                obj = db.pegarDisciplina(listaNomeDisciplinas.get(t));
                listaDisciplinas.add(obj);
            }
            listaAulas = db.buscarAulas(cod_aluno);
            try {
                int i = 0;
                for (i = 0; i < listaDisciplinas.size(); i++) {
                    dbLocal.inserirDisciplina(listaDisciplinas.get(i));
                }
                for (i = 0; i < listaAulas.size(); i++) {
                    dbLocal.salvarPresencaLocal(listaAulas.get(i));
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
