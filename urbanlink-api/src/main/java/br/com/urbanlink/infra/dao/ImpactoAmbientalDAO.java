package br.com.urbanlink.infra.dao;

import br.com.urbanlink.dominio.ImpactoAmbiental;
import br.com.urbanlink.dominio.RepositorioImpactos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ImpactoAmbientalDAO implements RepositorioImpactos {

    private static final String COLUNAS =
            "usuario_id, total_co2, total_viagens, equiv_arvores, pontos_investidos";

    private ConnectionFactory fabrica = new ConnectionFactory();

    @Override
    public ImpactoAmbiental buscarPorUsuario(String usuarioId) {
        String sql = "SELECT " + COLUNAS + " FROM impacto_ambiental WHERE usuario_id=?";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, usuarioId);
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
    public ArrayList<ImpactoAmbiental> obterTodos() {
        String sql = "SELECT " + COLUNAS + " FROM impacto_ambiental ORDER BY total_co2 DESC";
        ArrayList<ImpactoAmbiental> impactos = new ArrayList<>();
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                impactos.add(montar(rs));
            }
            return impactos;
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    @Override
    public void salvar(ImpactoAmbiental i) {
        String atualizar = "UPDATE impacto_ambiental SET total_co2=?, total_viagens=?, equiv_arvores=?, pontos_investidos=? WHERE usuario_id=?";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(atualizar)) {
            ps.setDouble(1, i.getTotalCo2Evitado());
            ps.setInt(2, i.getTotalViagens());
            ps.setDouble(3, i.getEquivalenciaArvores());
            ps.setDouble(4, i.getPontosInvestidos());
            ps.setString(5, i.getUsuarioId());
            if (ps.executeUpdate() > 0) {
                return;
            }
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
        String inserir = "INSERT INTO impacto_ambiental (" + COLUNAS + ") VALUES (?,?,?,?,?)";
        try (Connection c = fabrica.getConnection(); PreparedStatement ps = c.prepareStatement(inserir)) {
            ps.setString(1, i.getUsuarioId());
            ps.setDouble(2, i.getTotalCo2Evitado());
            ps.setInt(3, i.getTotalViagens());
            ps.setDouble(4, i.getEquivalenciaArvores());
            ps.setDouble(5, i.getPontosInvestidos());
            ps.execute();
        } catch (SQLException e) {
            throw ErroSql.traduzir(e);
        }
    }

    private ImpactoAmbiental montar(ResultSet rs) throws SQLException {
        ImpactoAmbiental i = new ImpactoAmbiental();
        i.setUsuarioId(rs.getString("usuario_id"));
        i.setTotalCo2Evitado(rs.getDouble("total_co2"));
        i.setTotalViagens(rs.getInt("total_viagens"));
        i.setEquivalenciaArvores(rs.getDouble("equiv_arvores"));
        i.setPontosInvestidos(rs.getDouble("pontos_investidos"));
        return i;
    }
}
