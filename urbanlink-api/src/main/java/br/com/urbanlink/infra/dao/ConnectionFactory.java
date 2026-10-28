package br.com.urbanlink.infra.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fornece conexões ao banco Oracle. URL, usuário e senha vêm de variáveis de ambiente
 * (DB_URL, DB_USER, DB_PASSWORD), para que a senha não fique no código nem no GitHub.
 */
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
