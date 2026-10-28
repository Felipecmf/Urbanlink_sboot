package br.com.urbanlink.infra.dao;

import br.com.urbanlink.dominio.RepositorioVouchers;
import br.com.urbanlink.dominio.StatusVoucher;
import br.com.urbanlink.dominio.Voucher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class VoucherDAO implements RepositorioVouchers {

    private static final String COLUNAS =
            "codigo, usuario_id, transporte_id, valor_credito, pontos_usados, status, data_geracao, antifraude";

    private ConnectionFactory fabrica = new ConnectionFactory();

    @Override
    public void adicionar(Voucher v) {
        String sql = "INSERT INTO voucher (" + COLUNAS + ") VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, v.getCodigo());
            ps.setString(2, v.getUsuarioId());
            ps.setString(3, v.getTransporteId());
            ps.setDouble(4, v.getValorCredito());
            ps.setDouble(5, v.getPontosUsados());
            ps.setString(6, v.getStatus().name());
            ps.setTimestamp(7, Timestamp.valueOf(v.getDataGeracao()));
            ps.setInt(8, v.isAntifraude() ? 1 : 0);
            ps.execute();
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public Voucher buscarPorCodigo(String codigo) {
        String sql = "SELECT " + COLUNAS + " FROM voucher WHERE codigo=?";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigo);
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
    public ArrayList<Voucher> obterTodos() {
        String sql = "SELECT " + COLUNAS + " FROM voucher ORDER BY data_geracao DESC";
        ArrayList<Voucher> vouchers = new ArrayList<>();
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                vouchers.add(montar(rs));
            }
            return vouchers;
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public ArrayList<Voucher> obterPorUsuario(String usuarioId) {
        String sql = "SELECT " + COLUNAS + " FROM voucher WHERE usuario_id=? ORDER BY data_geracao DESC";
        ArrayList<Voucher> vouchers = new ArrayList<>();
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vouchers.add(montar(rs));
                }
            }
            return vouchers;
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public int contarEmitidosDesde(String usuarioId, LocalDateTime desde) {
        String sql = "SELECT COUNT(*) FROM voucher WHERE usuario_id=? AND data_geracao >= ?";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, usuarioId);
            ps.setTimestamp(2, Timestamp.valueOf(desde));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    /** O UPDATE condicional impede uso duplo do mesmo voucher em requisições simultâneas. */
    @Override
    public boolean marcarComoUtilizado(String codigo) {
        String sql = "UPDATE voucher SET status='UTILIZADO' WHERE codigo=? AND status='ATIVO' AND antifraude=1";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public boolean alterarStatusSeAtivo(String codigo, StatusVoucher novoStatus) {
        String sql = "UPDATE voucher SET status=? WHERE codigo=? AND status='ATIVO'";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, novoStatus.name());
            ps.setString(2, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public boolean remover(String codigo) {
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM voucher WHERE codigo=?")) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    private Voucher montar(ResultSet rs) throws SQLException {
        Voucher v = new Voucher();
        v.setCodigo(rs.getString("codigo"));
        v.setUsuarioId(rs.getString("usuario_id"));
        v.setTransporteId(rs.getString("transporte_id"));
        v.setValorCredito(rs.getDouble("valor_credito"));
        v.setPontosUsados(rs.getDouble("pontos_usados"));
        v.setStatus(StatusVoucher.valueOf(rs.getString("status")));
        v.setDataGeracao(rs.getTimestamp("data_geracao").toLocalDateTime());
        v.setAntifraude(rs.getInt("antifraude") == 1);
        return v;
    }
}
