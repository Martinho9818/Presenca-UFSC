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
import ufsc.presencaufsc.model.Aluno;
import ufsc.presencaufsc.model.Instituicao;

public class TelaCadastroAluno extends AppCompatActivity {

    private EditText editText;
    private Aluno aluno;
    private DB db = new DB();
    Toast toast;
    private SQLiteDatabase conexao;
    private DBOpenHelper dbOpenHelper;
    private DBLocal dbLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tela_cadastro_aluno);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Cadastrar");

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
                salvarAluno();
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

    /* Método utilizado para cadastrar o aluno no banco de dados interno e externo */
    public void salvarAluno() {
        aluno = new Aluno();
        editText = (EditText) findViewById(R.id.editTextNome);
        /* Verificação do Nome */
        if (!this.isEmpty(editText)) {
            aluno.setNome(editText.getText().toString());
            editText = (EditText) findViewById(R.id.editTextMatricula);
            /* Verificação da Matricula */
            if (!this.isEmpty(editText)) {
                String variavel = editText.getText().toString();
                /* Verificando se a matrícula é um int */
                if (verificandoInt(variavel)) {
                    aluno.setMatricula(editText.getText().toString());
                    /* Verificando se o aluno ja esta cadastrado */
                    if (!db.verificarAluno(aluno.getMatricula())) {
                        editText = (EditText) findViewById(R.id.editTextEmail);
                        /* Verificação do Email */
                        if (!this.isEmpty(editText)) {
                            aluno.setEmail(editText.getText().toString());
                            editText = (EditText) findViewById(R.id.editTextCodInstituicao);
                            /* Verificação do código da Instituição */
                            if (!this.isEmpty(editText)) {
                                aluno.setCod_instituicao_cadastrada(editText.getText().toString());
                                if (db.verificaInstituicao(aluno.getCod_instituicao_cadastrada()) == true) {
                                    editText = (EditText) findViewById(R.id.editTextSenhaRecuperacao);
                                    /* Verificando senhas */
                                    if (!this.isEmpty(editText)) {
                                        String senha = editText.getText().toString();
                                        editText = (EditText) findViewById(R.id.editTextConfirmacaoSenhaRecuperacao);
                                        if (!this.isEmpty(editText)) {
                                            String senhaConfirmacao = editText.getText().toString();
                                            /* Se as senhas são iguais */
                                            if (senha.equals(senhaConfirmacao)) {
                                                aluno.setSenhaRecuperacao(senha);

                                                try {
                                                    /* Salva no banco de dados EXTERNO */
                                                    db.salvarAluno(aluno);
                                                    /* Verifica se é possivel cadastrar a instituição e cadastra no banco local */
                                                    if (aluno.getCod_instituicao_cadastrada() != null && !aluno.getCod_instituicao_cadastrada().equals(" ")) {
                                                        if (db.verificaInstituicao(aluno.getCod_instituicao_cadastrada()) != false) {
                                                            Instituicao instituicao = db.retornaInstituicao(aluno.getCod_instituicao_cadastrada());
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
                                                try {
                                                    /* Salva no banco de dados INTERNO */
                                                    dbLocal.inserirAluno(aluno);
                                                    dbLocal.inserirDadosPendentes(0);
                                                } catch (Exception ex) {
                                                    System.out.println(ex);
                                                }

                                                /* Verficação aluno cadastrado */
                                                if (db.verificarAluno(aluno.getMatricula()) && dbLocal.verificaAlunoLocal()) {
                                                    toast = Toast.makeText(this, R.string.salvo, Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                                    toast.show();

                                                } else {
                                                    toast = Toast.makeText(this, R.string.nao_foi_possivel_salvar_o_aluno, Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                                    toast.show();
                                                }

                                                startActivity(new Intent(this, TelaInicial.class));
                                                finishAffinity();
                                                enviarEmail();

                                            } else {
                                                toast = Toast.makeText(this, R.string.senhas_nao_batem, Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                                toast.show();
                                            }
                                        } else {
                                            toast = Toast.makeText(this, R.string.digite_confirmacao_senha, Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                            toast.show();
                                        }
                                    } else {
                                        toast = Toast.makeText(this, R.string.digite_senha_recuperacao, Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                        toast.show();
                                    }
                                } else {
                                    toast = Toast.makeText(this, R.string.instituicao_nao_encontrada_sistema, Toast.LENGTH_SHORT);
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
                        toast = Toast.makeText(this, R.string.aluno_ja_cadastrado, Toast.LENGTH_SHORT);
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
        } else {
            toast = Toast.makeText(this, R.string.campo_nome_vazio, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
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

    /* Método responsável por enviar uma mensagem para o cliente que o usuário escolher (redes sociais ou email) */
    private void enviarEmail() {
        String recipientList = aluno.getEmail();
        String[] recipients = recipientList.split(",");
        String subject = "Recuperação de senha";
        String message = "Sua senha de recuperação do aplicativo de presença é: " + aluno.getSenhaRecuperacao();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, getString(R.string.escolher_cliente_senha)));
    }
}
