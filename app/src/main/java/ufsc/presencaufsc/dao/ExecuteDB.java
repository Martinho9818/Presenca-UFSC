package ufsc.presencaufsc.dao;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.ResultSet;

public class ExecuteDB extends AsyncTask<String, Void, ResultSet> {

    private Connection connection;
    private String query;

    /* Construtor */
    public ExecuteDB(Connection connection, String query) {
        this.connection = connection;
        this.query = query;
    }

    /* Métodos */
    protected ResultSet doInBackground(String... strings) {
        ResultSet resultSet = null;
        try {
            /* retorna o comando para manipular o BD */
            resultSet = connection.prepareStatement(query).executeQuery();
        } catch (Exception e) {

        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return resultSet;
    }
}
