package ufsc.presencaufsc.dao;

import java.util.ArrayList;

public class ScriptDLL {

    public static ArrayList<String> getCreateTableTarefa() {

        ArrayList<String> listaSQLs = new ArrayList<String>();

        final StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE IF NOT EXISTS ALUNO ( ");
        sql.append("	COD_ALUNO INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
        sql.append("	MATRICULA VARCHAR(50) NOT NULL , ");
        sql.append("	NOME VARCHAR(255) NOT NULL, ");
        sql.append("    SENHA VARCHAR(255) NOT NULL, ");
        sql.append("	EMAIL VARCHAR(255) NOT NULL, ");
        sql.append("    COD_INSTITUICAO_CADASTRADA VARCHAR(255) NOT NULL )");

        listaSQLs.add(sql.toString());

        final StringBuilder sql2 = new StringBuilder();
        sql2.append("CREATE TABLE IF NOT EXISTS AULA ( ");
        sql2.append("   COD_AULA INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
        sql2.append("   DATA_AULA VARCHAR(255) NOT NULL, ");
        sql2.append("   HORA_AULA VARCHAR(255) NOT NULL, ");
        sql2.append("   COD_DISCIPLINA VARCHAR(255) NOT NULL, ");
        sql2.append("   DENTRO_PERIMETRO INTEGER NOT NULL, ");
        sql2.append("   LOCALIZACAO_ALUNO VARCHAR(255) NOT NULL, ");
        sql2.append("   COD_ALUNO VARCHAR(255) NOT NULL )");

        listaSQLs.add(sql2.toString());

        final StringBuilder sql3 = new StringBuilder();
        sql3.append("CREATE TABLE IF NOT EXISTS DISCIPLINA ( ");
        sql3.append("   ID_DISCIPLINA INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
        sql3.append("   COD_DISCIPLINA VARCHAR(255) NOT NULL, ");
        sql3.append("   NOME_DISCIPLINA VARCHAR(255) NOT NULL, ");
        sql3.append("   PROFESSOR_DISCIPLINA VARCHAR(255) NOT NULL, ");
        sql3.append("   COD_INSTITUICAO_CADASTRADA VARCHAR(255) NOT NULL ) ");

        listaSQLs.add(sql3.toString());

        final StringBuilder sql4 = new StringBuilder();
        sql4.append("CREATE TABLE IF NOT EXISTS INSTITUICAO ( ");
        sql4.append("   ID_INSTITUICAO INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
        sql4.append("   COD_INSTITUICAO VARCHAR(255) NOT NULL, ");
        sql4.append("   NOME_INSTITUICAO VARCHAR(255) NOT NULL, ");
        sql4.append("   LATITUDE_MIN VARCHAR(255) NOT NULL, ");
        sql4.append("   LONGITUDE_MIN VARCHAR(255) NOT NULL, ");
        sql4.append("   LATITUDE_MAX VARCHAR(255) NOT NULL, ");
        sql4.append("   LONGITUDE_MAX VARCHAR(255) NOT NULL )");

        listaSQLs.add(sql4.toString());

        final StringBuilder sql5 = new StringBuilder();
        sql5.append("CREATE TABLE IF NOT EXISTS DADOS_PENDENTES (");
        sql5.append("   ID_DADO INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
        sql5.append("   QTD_DADO_PENDENTE INTEGER )");

        listaSQLs.add(sql5.toString());

        return listaSQLs;
    }


}
