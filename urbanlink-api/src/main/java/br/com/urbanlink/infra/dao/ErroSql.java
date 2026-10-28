package br.com.urbanlink.infra.dao;

import java.sql.SQLException;

/** Converte erros conhecidos do Oracle (ORA-xxxxx) em mensagens que o cliente da API entende. */
class ErroSql {

    static RuntimeException traduzir(SQLException e) {
        int codigo = e.getErrorCode();
        if (codigo == 1) {
            return new IllegalArgumentException("Já existe um registro com este id, e-mail ou CPF.");
        }
        if (codigo == 2291) {
            return new IllegalArgumentException("Referência inválida: o registro relacionado não existe.");
        }
        if (codigo == 2292) {
            return new IllegalArgumentException("O registro possui dependências e não pode ser removido.");
        }
        if (codigo == 12899 || codigo == 1438) {
            return new IllegalArgumentException("Algum valor informado excede o limite permitido.");
        }
        return new RuntimeException(e);
    }
}
