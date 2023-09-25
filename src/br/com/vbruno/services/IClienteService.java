package br.com.vbruno.services;

import br.com.vbruno.domain.Cliente;
import br.com.vbruno.exceptions.DAOException;
import br.com.vbruno.exceptions.TipoChaveNaoEncontradaException;
import br.com.vbruno.services.generic.IGenericService;

public interface IClienteService extends IGenericService<Cliente, Long> {
//    Boolean cadastrar(Cliente cliente) throws TipoChaveNaoEncontradaException;
    Cliente buscarPorCPF(Long cpf) throws DAOException;

//	void excluir(Long cpf);
//
//	void alterar(Cliente cliente) throws TipoChaveNaoEncontradaException;
}
