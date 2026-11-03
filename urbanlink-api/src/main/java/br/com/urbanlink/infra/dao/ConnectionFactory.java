package br.com.urbanlink.infra.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getConnection() {
        String url = ("jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL");
        String usuario = ("RM569432");
        String senha = ("170408");
        try {
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
