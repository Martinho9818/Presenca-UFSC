package ufsc.presencaufsc.model;


import java.io.Serializable;

public class Aluno implements Serializable{

    private String matricula;
    private String email;
    private String nome;
    private String senhaRecuperacao;
    private String cod_instituicao_cadastrada;

    /* Construtor */
    public Aluno () {

    }

    public Aluno (String matricula, String nome, String email, String senhaRecuperacao) {
        this.setMatricula(matricula);
        this.setNome(nome);
        this.setEmail(email);
        this.setSenhaRecuperacao(senhaRecuperacao);
    }

    /* Setters e Getters */
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenhaRecuperacao() {
        return senhaRecuperacao;
    }

    public void setSenhaRecuperacao(String senhaRecuperacao) {
        this.senhaRecuperacao = senhaRecuperacao;
    }

    public String getCod_instituicao_cadastrada() {
        return cod_instituicao_cadastrada;
    }

    public void setCod_instituicao_cadastrada(String cod_instituicao_cadastrada) {
        this.cod_instituicao_cadastrada = cod_instituicao_cadastrada;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "matricula='" + matricula + '\'' +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", senhaRecuperacao='" + senhaRecuperacao + '\'' +
                ", cod_instituicao_cadastrada='" + cod_instituicao_cadastrada + '\'' +
                '}';
    }

}
