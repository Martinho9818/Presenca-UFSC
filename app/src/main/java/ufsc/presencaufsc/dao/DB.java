package ufsc.presencaufsc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ufsc.presencaufsc.model.Aluno;
import ufsc.presencaufsc.model.Aula;
import ufsc.presencaufsc.model.Disciplina;
import ufsc.presencaufsc.model.Instituicao;

public class DB implements Runnable {

    private Connection conn;
    private String host = " ";
    private String db = " ";
    private int port = 0000;
    private String user = " ";
    private String pass = " ";
    private String url = "jdbc:postgresql://%s:%d/%s";

    /* Construtor */
    public DB() {
        this.url = String.format(this.url, this.host, this.port, this.db);
        this.conecta();
//        this.desconecta();
    }

    /* Métodos */
    @Override
    public void run() {
        try {
            Class.forName("org.postgresql.Driver");
            this.conn = DriverManager.getConnection(this.url, this.user, this.pass);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void conecta() {
        Thread thread = new Thread(this);
        /* Vai rodar o método run */
        thread.start();

        try {
            /* Aplicação espera a conclusao do método run */
            thread.join();
        } catch (Exception e) {
            //System.out.println(e);
        }
    }

    private void desconecta() {
        System.out.println("Desconectando.");
        if (conn != null) {
            try {
                this.conn.close();
            } catch (Exception e) {
                //System.out.println(e);
            } finally {
                this.conn = null;
            }
        }
    }

    public ResultSet select(String query) {
        this.conecta();
        ResultSet resultSet = null;
        /* instancia a classe, executa o que tiver dentro do método doInBackground e o get espera o retorno*/
        try {
            resultSet = new ExecuteDB(this.conn, query).execute().get();
        } catch (Exception e) {
            //System.out.println(e);
        }

        return resultSet;
    }

    public ResultSet execute(String query) {
        this.conecta();
        ResultSet resultSet = null;
        /* instancia a classe, executa o que tiver dentro do método doInBackground e o get espera o retorno*/
        try {
            resultSet = new ExecuteDB(this.conn, query).execute().get();
        } catch (Exception e) {
            //System.out.println(e);
        }
        return resultSet;
    }

    /* ---------------- aluno ---------------- */
    public void salvarAluno(Aluno pAluno) {
        Aluno aluno = pAluno;
        String comando = "";
        comando = String.format("INSERT INTO aluno (matricula, nome, email, senha, cod_instituicao_cadastrada) VALUES ('%s', '%s', '%s', '%s', '%s');",
                aluno.getMatricula(), aluno.getNome(), aluno.getEmail(), aluno.getSenhaRecuperacao(), aluno.getCod_instituicao_cadastrada());
        this.execute(comando);
    }

    public boolean verificarAluno(String pMatricula) {
        String comando = "";
        String matricula = pMatricula;
        comando = String.format("SELECT * FROM aluno WHERE matricula = '%s';",
                matricula);
        Boolean retorno = false;
        try {
            ResultSet resultSet = this.select(comando);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Aluno obj = new Aluno();
                    obj.setMatricula(resultSet.getString("matricula"));
                    obj.setNome(resultSet.getString("nome"));
                    obj.setEmail(resultSet.getString("email"));
                    obj.setSenhaRecuperacao(resultSet.getString("senha"));
                    obj.setCod_instituicao_cadastrada(resultSet.getString("cod_instituicao_cadastrada"));

                    if (obj.getMatricula().equals(matricula)) {
                        retorno = true;
                    }
                }
            } else {
                retorno = false;
            }
            return retorno;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return retorno;
    }

    public Aluno retornaAluno(String pMatricula) {
        String comando = "";
        String matricula = pMatricula;
        comando = String.format("SELECT * FROM aluno WHERE matricula = '%s';",
                matricula);
        Aluno obj = new Aluno();
        try {
            ResultSet resultSet = this.select(comando);
            if (resultSet != null) {
                while (resultSet.next()) {
                    obj.setMatricula(resultSet.getString("matricula"));
                    obj.setNome(resultSet.getString("nome"));
                    obj.setEmail(resultSet.getString("email"));
                    obj.setSenhaRecuperacao(resultSet.getString("senha"));
                    obj.setCod_instituicao_cadastrada(resultSet.getString("cod_instituicao_cadastrada"));
                    return obj;
                }
            } else {
                System.out.println("Sem aluno.");
            }
            return obj;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return obj;
    }

    /* Método responsável por verificar a recuperacao do aluno */
    public boolean verificacaoParaRecuperacaoAluno(String pMatricula, String pEmail, String pSenha, String codInstituicao) {
        boolean retorno = false;
        String comando = "";
        String matricula = pMatricula;
        String email = pEmail;
        String senha = pSenha;
        String instituicao = codInstituicao;
        comando = String.format("SELECT * FROM aluno WHERE matricula = '%s' AND cod_instituicao_cadastrada = '%s';",
                matricula, instituicao);
        try {
            ResultSet resultSet = this.select(comando);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Aluno obj = new Aluno();
                    obj.setMatricula(resultSet.getString("matricula"));
                    obj.setNome(resultSet.getString("nome"));
                    obj.setEmail(resultSet.getString("email"));
                    obj.setSenhaRecuperacao(resultSet.getString("senha"));
                    obj.setCod_instituicao_cadastrada(resultSet.getString("cod_instituicao_cadastrada"));

                    if (obj.getEmail().equals(email) && obj.getSenhaRecuperacao().equals(senha)) {
                        retorno = true;
                    }
                }
            } else {
                retorno = false;
            }
            return retorno;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return retorno;
    }

    /* ---------------- aula ---------------- */
    public void salvarAula(Aula pAula) {
        Aula aula = pAula;
        String comando = "";
        comando = String.format("SET datestyle = dmy; INSERT INTO aula (data_aula, hora_aula, cod_disciplina, cod_aluno, localizacao_aluno, dentro_perimetro) VALUES ('%s', '%s', '%s', '%s', '%s', '%s');",
                aula.getDataAula(), aula.getHoraAula(), aula.getCod_disciplina(), aula.getCod_aluno(), aula.getLocalizacaoAluno(), aula.getLocalizacaoNoPerimetro());
        this.execute(comando);
    }

    public boolean verificarAula(String pHora, String pCod_aluno) {
        String comando = "";
        String hora = pHora;
        String cod_aluno = pCod_aluno;
        comando = String.format("SELECT * FROM aula WHERE cod_aluno = '%s' AND hora_aula = '%s';",
                cod_aluno, hora);
        Boolean retorno = false;
        try {
            ResultSet resultSet = this.select(comando);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Aula obj = new Aula();
                    obj.setHoraAula(resultSet.getString("hora_aula"));

                    if (obj.getHoraAula().equals(hora)) {
                        //System.out.println("Data já cadastrada.");
                        retorno = true;
                    } else {
                        retorno = false;
                    }
                }
            } else {

            }
            return retorno;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return retorno;
    }


    public List<Aula> buscarAulas(String pCod_Aluno) {
        List<Aula> listaAulas = new ArrayList<Aula>();
        String comando = "";
        String cod_aluno = pCod_Aluno;
        comando = String.format("SELECT * FROM aula WHERE cod_aluno = '%s';",
                cod_aluno);

        System.out.println(comando);
        try {
            ResultSet resultSet = this.select(comando);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Aula obj = new Aula();
                    obj.setDataAula(resultSet.getString("data_aula"));
                    obj.setHoraAula(resultSet.getString("hora_aula"));
                    obj.setCod_disciplina(resultSet.getString("cod_disciplina"));
                    obj.setCod_aluno(resultSet.getString("cod_aluno"));
                    obj.setLocalizacaoAluno(resultSet.getString("localizacao_aluno"));
                    obj.setLocalizacaoNoPerimetro(resultSet.getInt("dentro_perimetro"));
                    listaAulas.add(obj);
                }
            } else {
                //System.out.println("Sem Aulas cadastradas.");
            }
            return listaAulas;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return listaAulas;
    }


    /* ---------------- disciplina ---------------- */
    public boolean verificaDisciplina(String pCod_Disciplina, String pCod_Instituicao) {
        String comando = "";
        String cod_disciplina = pCod_Disciplina;
        String cod_instituicao = pCod_Instituicao;
        comando = String.format("SELECT * FROM disciplina WHERE cod_disciplina = '%s' AND cod_instituicao = '%s';",
                cod_disciplina, cod_instituicao);
        Boolean retorno = false;
        try {
            ResultSet resultSet = this.select(comando);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Disciplina obj = new Disciplina();
                    obj.setCodigoDisciplina(resultSet.getString("cod_disciplina"));
                    obj.setNomeDisciplina(resultSet.getString("nome_disciplina"));
                    obj.setProfessor(resultSet.getString("professor_disciplina"));
                    obj.setCodInstituicaoDisciplina(resultSet.getString("cod_instituicao"));
                    if (obj.getCodigoDisciplina().equals(cod_disciplina)) {
                        retorno = true;
                    }
                }
            } else {
                retorno = false;
            }
            return retorno;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return retorno;
    }

    public Disciplina retornaDisciplina(String pCod_Disciplina, String pCod_Instituicao) {
        String comando = "";
        String cod_disciplina = pCod_Disciplina;
        String cod_instituicao = pCod_Instituicao;
        comando = String.format("SELECT * FROM disciplina WHERE cod_disciplina = '%s' AND cod_instituicao = '%s';",
                cod_disciplina, cod_instituicao);
        Disciplina obj = null;
        try {
            ResultSet resultSet = this.select(comando);
            if (resultSet != null) {
                while (resultSet.next()) {
                    obj = new Disciplina();
                    obj.setCodigoDisciplina(resultSet.getString("cod_disciplina"));
                    obj.setNomeDisciplina(resultSet.getString("nome_disciplina"));
                    obj.setProfessor(resultSet.getString("professor_disciplina"));
                    obj.setCodInstituicaoDisciplina(resultSet.getString("cod_instituicao"));
                    return obj;
                }
            }
            return obj;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return obj;
    }

//    /* Retorna todas as disciplinas cadastradas no banco de dados externo */
//    public List<Disciplina> buscarDisciplinas() {
//        List<Disciplina> listaDisciplinas = new ArrayList<Disciplina>();
//        String comando = "";
//        comando = String.format("SELECT * FROM disciplina;");
//        try {
//            ResultSet resultSet = this.select(comando);
//            if (resultSet != null) {
//                while (resultSet.next()) {
//                    Disciplina obj = new Disciplina();
//                    obj.setCodigoDisciplina(resultSet.getString("cod_disciplina"));
//                    obj.setNomeDisciplina(resultSet.getString("nome_disciplina"));
//                    obj.setProfessor(resultSet.getString("professor_disciplina"));
//                    obj.setCodInstituicaoDisciplina(resultSet.getString("cod_instituicao"));
//                    listaDisciplinas.add(obj);
//                }
//            } else {
//                //System.out.println("Sem disciplinas cadastradas.");
//            }
//            return listaDisciplinas;
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
//        return listaDisciplinas;
//    }

    public Disciplina pegarDisciplina(String pCod_Disciplina) {
        String comando = "";
        String cod_disciplina = pCod_Disciplina;
        comando = String.format("SELECT * FROM disciplina WHERE cod_disciplina = '%s';",
                cod_disciplina);
        Disciplina obj = null;
        try {
            ResultSet resultSet = this.select(comando);
            if (resultSet != null) {
                while (resultSet.next()) {
                    obj = new Disciplina();
                    obj.setCodigoDisciplina(resultSet.getString("cod_disciplina"));
                    obj.setNomeDisciplina(resultSet.getString("nome_disciplina"));
                    obj.setProfessor(resultSet.getString("professor_disciplina"));
                    obj.setCodInstituicaoDisciplina(resultSet.getString("cod_instituicao"));
                    return obj;
                }
            }
            return obj;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return obj;
    }


    /* ---------------- Instituição ---------------- */
    public boolean verificaInstituicao(String pCod_Instituicao) {
        String comando = "";
        String cod_instituicao = pCod_Instituicao;
        comando = String.format("SELECT * FROM instituicao WHERE cod_instituicao = '%s';",
                cod_instituicao);
        Boolean retorno = false;
        try {
            ResultSet resultSet = this.select(comando);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Instituicao obj = new Instituicao();
                    obj.setCod_instituicao(resultSet.getString("cod_instituicao"));
                    obj.setNome_instituicao(resultSet.getString("nome_instituicao"));
                    obj.setLatitude_min(resultSet.getString("latitude_instituicao_min"));
                    obj.setLongitude_min(resultSet.getString("longitude_instituicao_min"));
                    obj.setLatitude_max(resultSet.getString("latitude_instituicao_max"));
                    obj.setLongitude_max(resultSet.getString("longitude_instituicao_max"));
                    if (obj.getCod_instituicao().equals(cod_instituicao)) {
                        retorno = true;
                    }
                }
            } else {
                retorno = false;
            }
            return retorno;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return retorno;
    }

    public Instituicao retornaInstituicao(String pCod_Instituicao) {
        String comando = "";
        String cod_instituicao = pCod_Instituicao;
        comando = String.format("SELECT * FROM instituicao WHERE cod_instituicao = '%s';",
                cod_instituicao);
        Instituicao obj = null;
        try {
            ResultSet resultSet = this.select(comando);
            if (resultSet != null) {
                while (resultSet.next()) {
                    obj = new Instituicao();
                    obj.setCod_instituicao(resultSet.getString("cod_instituicao"));
                    obj.setNome_instituicao(resultSet.getString("nome_instituicao"));
                    obj.setLatitude_min(resultSet.getString("latitude_instituicao_min"));
                    obj.setLongitude_min(resultSet.getString("longitude_instituicao_min"));
                    obj.setLatitude_max(resultSet.getString("latitude_instituicao_max"));
                    obj.setLongitude_max(resultSet.getString("longitude_instituicao_max"));
                    return obj;
                }
            }
            return obj;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return obj;
    }

    /* ---------------- Cadastro ---------------- */
    public List<String> buscarDisciplinasCadastradas(String pCod_aluno, String pCod_instituicao) {
        List<String> listaNomeDisciplinas = new ArrayList<String>();
        String cod_aluno = pCod_aluno;
        String cod_instituicao = pCod_instituicao;
        String comando = "";
        comando = String.format("SELECT cod_disciplina FROM cadastro WHERE cod_aluno = '%s' AND cod_instituicao = '%s';",
                cod_aluno, cod_instituicao);
        try {
            ResultSet resultSet = this.select(comando);
            if (resultSet != null) {
                while (resultSet.next()) {
                    Disciplina obj = new Disciplina();
                    obj.setCodigoDisciplina(resultSet.getString("cod_disciplina"));
                    listaNomeDisciplinas.add(obj.getCodigoDisciplina());
                }
            } else {
                //System.out.println("Sem disciplinas cadastradas.");
            }
            return listaNomeDisciplinas;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return listaNomeDisciplinas;
    }

}
