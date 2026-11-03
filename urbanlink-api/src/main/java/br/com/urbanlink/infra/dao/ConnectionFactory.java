package br.com.urbanlink.infra.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getConnection() {
        String url = lerVariavel("DB_URL", "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL");
        String usuario = lerVariavel("DB_USER", "");
        String senha = lerVariavel("DB_PASSWORD", "");
        try {
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String lerVariavel(String nome, String padrao) {
        String valor = System.getenv(nome);
        if (valor == null || valor.isEmpty()) {
            return padrao;
        }
        return valor;
    }
}
