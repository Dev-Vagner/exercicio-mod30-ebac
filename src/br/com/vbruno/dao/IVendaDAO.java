package br.com.vbruno.dao;

import br.com.vbruno.dao.generic.IGenericDAO;
import br.com.vbruno.domain.Venda;
import br.com.vbruno.exceptions.DAOException;
import br.com.vbruno.exceptions.TipoChaveNaoEncontradaException;

public interface IVendaDAO extends IGenericDAO<Venda, String> {
    public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;

    public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
}
