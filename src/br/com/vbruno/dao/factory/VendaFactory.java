package br.com.vbruno.dao.factory;

import br.com.vbruno.domain.Cliente;
import br.com.vbruno.domain.Venda;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VendaFactory {
    public static Venda convert(ResultSet rs) throws SQLException {
        Cliente cliente = ClienteFactory.convert(rs);
        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setId(rs.getLong("id_venda"));
        venda.setCodigo(rs.getString("codigo"));
        venda.setValorTotal(rs.getBigDecimal("valor_total"));
        venda.setDataVenda(rs.getTimestamp("data_venda").toInstant());
        venda.setStatus(Venda.Status.getByName(rs.getString("status_venda")));
        return venda;
    }
}
