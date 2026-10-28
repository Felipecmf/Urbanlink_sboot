package br.com.urbanlink.infra.dao;

import br.com.urbanlink.dominio.RepositorioUsuarios;
import br.com.urbanlink.dominio.Usuario;
import br.com.urbanlink.dominio.UsuarioPremium;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class UsuarioDAO implements RepositorioUsuarios {

    private static final String COLUNAS =
            "id, nome, email, cpf, saldo_pontos, tipo, multiplicador_bonus, limite_resgates, resgates_mes";

    private ConnectionFactory fabrica = new ConnectionFactory();

    @Override
    public void adicionar(Usuario u) {
        String sql = "INSERT INTO usuario (" + COLUNAS + ") VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getId());
            ps.setString(2, u.getNome());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getCpf());
            ps.setDouble(5, u.getSaldoPontos());
            if (u instanceof UsuarioPremium) {
                UsuarioPremium p = (UsuarioPremium) u;
                ps.setString(6, "PREMIUM");
                ps.setDouble(7, p.getMultiplicadorBonus());
                ps.setInt(8, p.getLimiteResgatesMensais());
                ps.setInt(9, p.getResgatesRealizadosMes());
            } else {
                ps.setString(6, "COMUM");
                ps.setNull(7, Types.NUMERIC);
                ps.setNull(8, Types.NUMERIC);
                ps.setNull(9, Types.NUMERIC);
            }
            ps.execute();
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    /** Atualiza dados cadastrais. Saldo e resgates do mês NÃO são alterados aqui. */
    @Override
    public boolean atualizar(Usuario u) {
        String sql = "UPDATE usuario SET nome=?, email=?, cpf=?, tipo=?, multiplicador_bonus=?, limite_resgates=?, "
                   + "resgates_mes=? WHERE id=?";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getCpf());
            if (u instanceof UsuarioPremium) {
                UsuarioPremium p = (UsuarioPremium) u;
                ps.setString(4, "PREMIUM");
                ps.setDouble(5, p.getMultiplicadorBonus());
                ps.setInt(6, p.getLimiteResgatesMensais());
                ps.setInt(7, p.getResgatesRealizadosMes());
            } else {
                ps.setString(4, "COMUM");
                ps.setNull(5, Types.NUMERIC);
                ps.setNull(6, Types.NUMERIC);
                ps.setNull(7, Types.NUMERIC);
            }
            ps.setString(8, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public boolean remover(String id) {
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM usuario WHERE id=?")) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public Usuario buscarPorId(String id) {
        String sql = "SELECT " + COLUNAS + " FROM usuario WHERE id=?";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public ArrayList<Usuario> obterTodos() {
        String sql = "SELECT " + COLUNAS + " FROM usuario ORDER BY nome";
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(montar(rs));
            }
            return usuarios;
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public void depositar(String id, double pontos) {
        String sql = "UPDATE usuario SET saldo_pontos = saldo_pontos + ? WHERE id=?";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, pontos);
            ps.setString(2, id);
            ps.execute();
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    /** Débito atômico: só debita se o saldo for suficiente (protege contra requisições simultâneas). */
    @Override
    public boolean debitar(String id, double pontos) {
        String sql = "UPDATE usuario SET saldo_pontos = saldo_pontos - ? WHERE id=? AND saldo_pontos >= ?";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, pontos);
            ps.setString(2, id);
            ps.setDouble(3, pontos);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public void registrarResgate(String id) {
        String sql = "UPDATE usuario SET resgates_mes = resgates_mes + 1 WHERE id=?";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    private Usuario montar(ResultSet rs) throws SQLException {
        Usuario u;
        if ("PREMIUM".equals(rs.getString("tipo"))) {
            UsuarioPremium p = new UsuarioPremium();
            p.setMultiplicadorBonus(rs.getDouble("multiplicador_bonus"));
            p.setLimiteResgatesMensais(rs.getInt("limite_resgates"));
            p.setResgatesRealizadosMes(rs.getInt("resgates_mes"));
            u = p;
        } else {
            u = new Usuario();
        }
        u.setId(rs.getString("id"));
        u.setNome(rs.getString("nome"));
        u.setEmail(rs.getString("email"));
        u.setCpf(rs.getString("cpf"));
        u.setSaldoPontos(rs.getDouble("saldo_pontos"));
        return u;
    }
}
