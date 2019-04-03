package ufsc.presencaufsc.model;

import java.io.Serializable;

public class Disciplina implements Serializable{

    private String codigoDisciplina;
    private String nomeDisciplina;
    private String professor;
    private String codInstituicaoDisciplina;

    /* Setters e Getters */
    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(String codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getCodInstituicaoDisciplina() {
        return codInstituicaoDisciplina;
    }

    public void setCodInstituicaoDisciplina(String codInstituicaoDisciplina) {
        this.codInstituicaoDisciplina = codInstituicaoDisciplina;
    }

    @Override
    public String toString() {
        return "Disciplina{" +
                "codigoDisciplina='" + codigoDisciplina + '\'' +
                ", nomeDisciplina='" + nomeDisciplina + '\'' +
                ", professor='" + professor + '\'' +
                ", codInstituicaoDisciplina='" + codInstituicaoDisciplina + '\'' +
                '}';
    }
}
