package ufsc.presencaufsc.model;

public class Instituicao {

    private String cod_instituicao;
    private String nome_instituicao;
    private String longitude_max;
    private String latitude_max;
    private String longitude_min;
    private String latitude_min;

    /* Construtor */
    public Instituicao () {

    }

    /* Setters e Getters */

    public String getCod_instituicao() {
        return cod_instituicao;
    }

    public void setCod_instituicao(String cod_instituicao) {
        this.cod_instituicao = cod_instituicao;
    }

    public String getNome_instituicao() {
        return nome_instituicao;
    }

    public void setNome_instituicao(String nome_instituicao) {
        this.nome_instituicao = nome_instituicao;
    }

    public String getLongitude_max() {
        return longitude_max;
    }

    public void setLongitude_max(String longitude_max) {
        this.longitude_max = longitude_max;
    }

    public String getLatitude_max() {
        return latitude_max;
    }

    public void setLatitude_max(String latitude_max) {
        this.latitude_max = latitude_max;
    }

    public String getLongitude_min() {
        return longitude_min;
    }

    public void setLongitude_min(String longitude_min) {
        this.longitude_min = longitude_min;
    }

    public String getLatitude_min() {
        return latitude_min;
    }

    public void setLatitude_min(String latitude_min) {
        this.latitude_min = latitude_min;
    }

    @Override
    public String toString() {
        return "Instituicao{" +
                "cod_instituicao='" + cod_instituicao + '\'' +
                ", nome_instituicao='" + nome_instituicao + '\'' +
                ", longitude_max='" + longitude_max + '\'' +
                ", latitude_max='" + latitude_max + '\'' +
                ", longitude_min='" + longitude_min + '\'' +
                ", latitude_min='" + latitude_min + '\'' +
                '}';
    }
}
