package ufsc.presencaufsc.model;

import java.io.Serializable;

public class Aula implements Serializable{

    private String dataAula;
    private String cod_aluno;
    private String cod_disciplina;
    private String horaAula;
    private String localizacaoAluno;
    private int localizacaoNoPerimetro;

    /* Setters e Getters */
    public String getDataAula() {
        return dataAula;
    }

    public void setDataAula(String dataAula) {
        this.dataAula = dataAula;
    }

    public String getCod_aluno() {
        return cod_aluno;
    }

    public void setCod_aluno(String cod_aluno) {
        this.cod_aluno = cod_aluno;
    }

    public String getCod_disciplina() {
        return cod_disciplina;
    }

    public void setCod_disciplina(String cod_disciplina) {
        this.cod_disciplina = cod_disciplina;
    }

    public String getHoraAula() {
        return horaAula;
    }

    public void setHoraAula(String horaAula) {
        this.horaAula = horaAula;
    }

    public String getLocalizacaoAluno() {
        return localizacaoAluno;
    }

    public void setLocalizacaoAluno(String localizacaoAluno) {
        this.localizacaoAluno = localizacaoAluno;
    }

    public Integer getLocalizacaoNoPerimetro() {
        return localizacaoNoPerimetro;
    }

    public void setLocalizacaoNoPerimetro(Integer localizacaoNoPerimetro) {
        this.localizacaoNoPerimetro = localizacaoNoPerimetro;
    }


    @Override
    public String toString() {
        return "Aula{" +
                "dataAula='" + dataAula + '\'' +
                ", cod_aluno='" + cod_aluno + '\'' +
                ", cod_disciplina='" + cod_disciplina + '\'' +
                ", horaAula='" + horaAula + '\'' +
                ", localizacaoAluno='" + localizacaoAluno + '\'' +
                ", localizacaoNoPerimetro=" + localizacaoNoPerimetro +
                '}';
    }
}
