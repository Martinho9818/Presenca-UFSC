package ufsc.presencaufsc.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ufsc.presencaufsc.model.Aluno;
import ufsc.presencaufsc.model.Aula;
import ufsc.presencaufsc.model.Disciplina;
import ufsc.presencaufsc.model.Instituicao;

public class DBLocal {
    private SQLiteDatabase conexao;

    public DBLocal(SQLiteDatabase conexao) {
        this.conexao = conexao;
    }

    private Instituicao instituicao;
    private Aluno aluno;
    private Disciplina disciplina;
    private Aula aula;
    private int qtdDado;

    /* --------------- Aluno --------------- */
    public void inserirAluno(Aluno pAluno) {
        this.aluno = pAluno;
        ContentValues contentValues = new ContentValues();
        contentValues.put("MATRICULA", aluno.getMatricula());
        contentValues.put("NOME", aluno.getNome());
        contentValues.put("EMAIL", aluno.getEmail());
        contentValues.put("SENHA", aluno.getSenhaRecuperacao());
        contentValues.put("COD_INSTITUICAO_CADASTRADA", aluno.getCod_instituicao_cadastrada());
        conexao.insertOrThrow("ALUNO", null, contentValues);
    }

    public Aluno buscarAlunoLocal() {
        aluno = new Aluno();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT MATRICULA, NOME, EMAIL, SENHA, COD_INSTITUICAO_CADASTRADA ");
        sql.append("FROM ALUNO");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);
        if (resultado.getCount() > 0) {
            /* Pega o primeiro registro (unico) */
            resultado.moveToFirst();
            aluno.setMatricula(resultado.getString(resultado.getColumnIndexOrThrow("MATRICULA")));
            aluno.setNome(resultado.getString(resultado.getColumnIndexOrThrow("NOME")));
            aluno.setEmail(resultado.getString(resultado.getColumnIndexOrThrow("EMAIL")));
            aluno.setSenhaRecuperacao(resultado.getString(resultado.getColumnIndexOrThrow("SENHA")));
            aluno.setCod_instituicao_cadastrada(resultado.getString(resultado.getColumnIndexOrThrow("COD_INSTITUICAO_CADASTRADA")));
        }
        return aluno;
    }

    public boolean verificaAlunoLocal() {
        Boolean confirmacao;
        aluno = new Aluno();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT MATRICULA, NOME, EMAIL, SENHA, COD_INSTITUICAO_CADASTRADA ");
        sql.append("FROM ALUNO");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);
        if (resultado.getCount() > 0) {
            /* Pega o primeiro registro (unico) */
            resultado.moveToFirst();
            aluno.setMatricula(resultado.getString(resultado.getColumnIndexOrThrow("MATRICULA")));
            aluno.setNome(resultado.getString(resultado.getColumnIndexOrThrow("NOME")));
            aluno.setEmail(resultado.getString(resultado.getColumnIndexOrThrow("EMAIL")));
            aluno.setSenhaRecuperacao(resultado.getString(resultado.getColumnIndexOrThrow("SENHA")));
            aluno.setCod_instituicao_cadastrada(resultado.getString(resultado.getColumnIndexOrThrow("COD_INSTITUICAO_CADASTRADA")));

            confirmacao = true;
            return confirmacao;
        } else {
            confirmacao = false;
        }
        return confirmacao;
    }

    /* --------------- DISCIPLINA --------------- */
    public void inserirDisciplina(Disciplina pDisciplina) {
        disciplina = pDisciplina;
        ContentValues contentValues = new ContentValues();
        contentValues.put("COD_DISCIPLINA", disciplina.getCodigoDisciplina());
        contentValues.put("NOME_DISCIPLINA", disciplina.getNomeDisciplina());
        contentValues.put("PROFESSOR_DISCIPLINA", disciplina.getProfessor());
        contentValues.put("COD_INSTITUICAO_CADASTRADA", disciplina.getCodInstituicaoDisciplina());
        conexao.insertOrThrow("DISCIPLINA", null, contentValues);
    }

    public boolean verificaDisciplinaLocal() {
        Boolean confirmacao;
        disciplina = new Disciplina();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * ");
        sql.append("FROM DISCIPLINA");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);
        if (resultado.getCount() > 0) {
            /* Pega o primeiro registro (unico) */
            resultado.moveToFirst();
            disciplina.setCodigoDisciplina(resultado.getString(resultado.getColumnIndexOrThrow("COD_DISCIPLINA")));
            confirmacao = true;
            return confirmacao;
        } else {
            confirmacao = false;
        }
        return confirmacao;
    }

    public List<Disciplina> buscarTodasDisciplinas(String pCod_instituicao) {
        List<Disciplina> disciplinas = new ArrayList<Disciplina>();

        String cod_instituicao = pCod_instituicao;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COD_DISCIPLINA, NOME_DISCIPLINA, PROFESSOR_DISCIPLINA, COD_INSTITUICAO_CADASTRADA  ");
        sql.append("FROM DISCIPLINA ");
        sql.append("WHERE COD_INSTITUICAO_CADASTRADA = '");
        sql.append(cod_instituicao);
        sql.append("'");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);
        if (resultado.getCount() > 0) {
            resultado.moveToFirst();
            do {
                disciplina = new Disciplina();
                disciplina.setCodigoDisciplina(resultado.getString(resultado.getColumnIndexOrThrow("COD_DISCIPLINA")));
                disciplina.setNomeDisciplina(resultado.getString(resultado.getColumnIndexOrThrow("NOME_DISCIPLINA")));
                disciplina.setProfessor(resultado.getString(resultado.getColumnIndexOrThrow("PROFESSOR_DISCIPLINA")));
                disciplina.setCodInstituicaoDisciplina(resultado.getString(resultado.getColumnIndexOrThrow("COD_INSTITUICAO_CADASTRADA")));
                disciplinas.add(disciplina);
            } while (resultado.moveToNext());
        }
        return disciplinas;
    }

    public boolean verificaDisciplina(String cod_disciplina) {
        Boolean confirmacao;
        String cod = cod_disciplina;
        disciplina = new Disciplina();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COD_DISCIPLINA, NOME_DISCIPLINA, PROFESSOR_DISCIPLINA, COD_INSTITUICAO_CADASTRADA  ");
        sql.append("FROM DISCIPLINA ");
        sql.append("WHERE COD_DISCIPLINA = '");
        sql.append(cod);
        sql.append("'");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);

        if (resultado.getCount() > 0) {
            /* Pega o primeiro registro (unico) */
            resultado.moveToFirst();
            disciplina.setCodigoDisciplina(resultado.getString(resultado.getColumnIndexOrThrow("COD_DISCIPLINA")));
            disciplina.setNomeDisciplina(resultado.getString(resultado.getColumnIndexOrThrow("NOME_DISCIPLINA")));
            disciplina.setNomeDisciplina(resultado.getString(resultado.getColumnIndexOrThrow("PROFESSOR_DISCIPLINA")));
            disciplina.setCodInstituicaoDisciplina(resultado.getString(resultado.getColumnIndexOrThrow("COD_INSTITUICAO_CADASTRADA")));

            if (disciplina.getCodigoDisciplina().equals(cod)) {
                confirmacao = true;
                return confirmacao;
            } else {
                confirmacao = false;
            }
        } else {
            confirmacao = false;
        }
        return confirmacao;
    }


    /* --------------- AULA --------------- */
    public void salvarPresencaLocal(Aula pAula) {
        aula = pAula;
        ContentValues contentValues = new ContentValues();
        contentValues.put("DATA_AULA", aula.getDataAula());
        contentValues.put("HORA_AULA", aula.getHoraAula());
        contentValues.put("COD_DISCIPLINA", aula.getCod_disciplina());
        contentValues.put("COD_ALUNO", aula.getCod_aluno());
        contentValues.put("DENTRO_PERIMETRO", aula.getLocalizacaoNoPerimetro());
        contentValues.put("LOCALIZACAO_ALUNO", aula.getLocalizacaoAluno());
        conexao.insertOrThrow("AULA", null, contentValues);
    }

    public List<Aula> buscarPresencaDaDisciplina(String pMatriculaAluno, String pCodDisciplina) {
        List<Aula> aulas = new ArrayList<Aula>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COD_AULA, DATA_AULA, HORA_AULA, COD_DISCIPLINA, COD_ALUNO, LOCALIZACAO_ALUNO, DENTRO_PERIMETRO ");
        sql.append("FROM AULA ");
        sql.append("WHERE COD_ALUNO = '");
        sql.append(pMatriculaAluno);
        sql.append("' AND COD_DISCIPLINA = '");
        sql.append(pCodDisciplina);
        sql.append("'");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);

        if (resultado.getCount() > 0) {
            resultado.moveToFirst();
            do {
                aula = new Aula();
                aula.setCod_disciplina(resultado.getString(resultado.getColumnIndexOrThrow("COD_DISCIPLINA")));
                aula.setDataAula(resultado.getString(resultado.getColumnIndexOrThrow("DATA_AULA")));
                aula.setHoraAula(resultado.getString(resultado.getColumnIndexOrThrow("HORA_AULA")));
                aula.setCod_aluno(resultado.getString(resultado.getColumnIndexOrThrow("COD_ALUNO")));
                aula.setLocalizacaoAluno(resultado.getString(resultado.getColumnIndexOrThrow("LOCALIZACAO_ALUNO")));
                aula.setLocalizacaoNoPerimetro(resultado.getInt(resultado.getColumnIndexOrThrow("DENTRO_PERIMETRO")));
                aulas.add(aula);
            } while (resultado.moveToNext());
        }
        return aulas;
    }


    /* --------------- INSTITUICAO --------------- */
    public void inserirInstituicao(Instituicao pInstituicao) {
        this.instituicao = pInstituicao;
        ContentValues contentValues = new ContentValues();
        contentValues.put("COD_INSTITUICAO", instituicao.getCod_instituicao());
        contentValues.put("NOME_INSTITUICAO", instituicao.getNome_instituicao());
        contentValues.put("LATITUDE_MIN", instituicao.getLatitude_min());
        contentValues.put("LONGITUDE_MIN", instituicao.getLongitude_min());
        contentValues.put("LATITUDE_MAX", instituicao.getLatitude_max());
        contentValues.put("LONGITUDE_MAX", instituicao.getLongitude_max());
        conexao.insertOrThrow("INSTITUICAO", null, contentValues);
    }

    public Instituicao buscarInstituicaoLocal() {
        instituicao = new Instituicao();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COD_INSTITUICAO, NOME_INSTITUICAO, LATITUDE_MIN, LONGITUDE_MIN, LATITUDE_MAX, LONGITUDE_MAX ");
        sql.append("FROM INSTITUICAO");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);
        if (resultado.getCount() > 0) {
            /* Pega o primeiro registro (unico) */
            resultado.moveToFirst();
            instituicao.setCod_instituicao(resultado.getString(resultado.getColumnIndexOrThrow("COD_INSTITUICAO")));
            instituicao.setNome_instituicao(resultado.getString(resultado.getColumnIndexOrThrow("NOME_INSTITUICAO")));
            instituicao.setLatitude_min(resultado.getString(resultado.getColumnIndexOrThrow("LATITUDE_MIN")));
            instituicao.setLongitude_min(resultado.getString(resultado.getColumnIndexOrThrow("LONGITUDE_MIN")));
            instituicao.setLatitude_max(resultado.getString(resultado.getColumnIndexOrThrow("LATITUDE_MAX")));
            instituicao.setLongitude_max(resultado.getString(resultado.getColumnIndexOrThrow("LONGITUDE_MAX")));
        }
        return instituicao;
    }

    /* --------------- DADOS_PENDENTES --------------- */
    public void inserirDadosPendentes(Integer pQtdDado) {
        this.qtdDado = pQtdDado;
        ContentValues contentValues = new ContentValues();
        contentValues.put("QTD_DADO_PENDENTE", qtdDado);
        conexao.insertOrThrow("DADOS_PENDENTES", null, contentValues);
    }

    public Integer buscarDadosPendentes() {
        qtdDado = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT QTD_DADO_PENDENTE ");
        sql.append("FROM DADOS_PENDENTES");

        Cursor resultado = conexao.rawQuery(sql.toString(), null);
        if (resultado.getCount() > 0) {
            /* Pega o primeiro registro (unico) */
            resultado.moveToFirst();
            this.qtdDado = (resultado.getInt(resultado.getColumnIndexOrThrow("QTD_DADO_PENDENTE")));
        }
        return qtdDado;
    }

    public void atualizaDadosPendentes(Integer pQtdDado) {
        this.qtdDado = pQtdDado;
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE DADOS_PENDENTES SET QTD_DADO_PENDENTE = ");
        sql.append(qtdDado);
        sql.append(" WHERE ID_DADO = 1");

        conexao.execSQL(sql.toString());
    }

}
