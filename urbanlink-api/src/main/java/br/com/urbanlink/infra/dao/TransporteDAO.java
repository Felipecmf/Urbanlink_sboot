package br.com.urbanlink.infra.dao;

import br.com.urbanlink.dominio.RepositorioTransportes;
import br.com.urbanlink.dominio.TransporteIntegrado;
import br.com.urbanlink.dominio.TransportePublico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class TransporteDAO implements RepositorioTransportes {

    private static final String COLUNAS =
            "id, nome, modalidade, tarifa_base, co2_evitado, integrado, sistema_bilhetagem, total_viagens, aceita_voucher";

    private ConnectionFactory fabrica = new ConnectionFactory();

    @Override
    public void adicionar(TransportePublico t) {
        String sql = "INSERT INTO transporte (" + COLUNAS + ") VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getId());
            ps.setString(2, t.getNome());
            ps.setString(3, t.getModalidade());
            ps.setDouble(4, t.getTarifaBase());
            ps.setDouble(5, t.getCo2EvitadoPorUso());
            ps.setInt(6, t.isIntegrado() ? 1 : 0);
            if (t instanceof TransporteIntegrado) {
                TransporteIntegrado ti = (TransporteIntegrado) t;
                ps.setString(7, ti.getSistemaBilhetagem());
                ps.setInt(8, ti.getTotalViagensProcessadas());
                ps.setInt(9, ti.isAceitaVoucher() ? 1 : 0);
            } else {
                ps.setNull(7, Types.VARCHAR);
                ps.setInt(8, 0);
                ps.setInt(9, 0);
            }
            ps.execute();
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    /** Atualiza dados cadastrais; o total de viagens processadas não é alterado aqui. */
    @Override
    public boolean atualizar(TransportePublico t) {
        String sql = "UPDATE transporte SET nome=?, modalidade=?, tarifa_base=?, co2_evitado=?, integrado=?, "
                   + "sistema_bilhetagem=?, aceita_voucher=? WHERE id=?";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getNome());
            ps.setString(2, t.getModalidade());
            ps.setDouble(3, t.getTarifaBase());
            ps.setDouble(4, t.getCo2EvitadoPorUso());
            ps.setInt(5, t.isIntegrado() ? 1 : 0);
            if (t instanceof TransporteIntegrado) {
                TransporteIntegrado ti = (TransporteIntegrado) t;
                ps.setString(6, ti.getSistemaBilhetagem());
                ps.setInt(7, ti.isAceitaVoucher() ? 1 : 0);
            } else {
                ps.setNull(6, Types.VARCHAR);
                ps.setInt(7, 0);
            }
            ps.setString(8, t.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public boolean remover(String id) {
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM transporte WHERE id=?")) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public TransportePublico buscarPorId(String id) {
        String sql = "SELECT " + COLUNAS + " FROM transporte WHERE id=?";
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
    public ArrayList<TransportePublico> obterTodos() {
        String sql = "SELECT " + COLUNAS + " FROM transporte ORDER BY nome";
        ArrayList<TransportePublico> transportes = new ArrayList<>();
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                transportes.add(montar(rs));
            }
            return transportes;
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public void incrementarViagens(String id) {
        String sql = "UPDATE transporte SET total_viagens = total_viagens + 1 WHERE id=?";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    private TransportePublico montar(ResultSet rs) throws SQLException {
        String sistema = rs.getString("sistema_bilhetagem");
        TransportePublico t;
        if (sistema != null) {
            TransporteIntegrado ti = new TransporteIntegrado();
            ti.setSistemaBilhetagem(sistema);
            ti.setTotalViagensProcessadas(rs.getInt("total_viagens"));
            ti.setAceitaVoucher(rs.getInt("aceita_voucher") == 1);
            t = ti;
        } else {
            t = new TransportePublico();
        }
        t.setId(rs.getString("id"));
        t.setNome(rs.getString("nome"));
        t.setModalidade(rs.getString("modalidade"));
        t.setTarifaBase(rs.getDouble("tarifa_base"));
        t.setCo2EvitadoPorUso(rs.getDouble("co2_evitado"));
        t.setIntegrado(rs.getInt("integrado") == 1);
        return t;
    }
}
