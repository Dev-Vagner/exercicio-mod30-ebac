package br.com.vbruno.services;

import br.com.vbruno.dao.IProdutoDAO;
import br.com.vbruno.domain.Produto;
import br.com.vbruno.services.generic.GenericService;
import br.com.vbruno.services.generic.IGenericService;

public class ProdutoService extends GenericService<Produto, String> implements IProdutoService {
    public ProdutoService(IProdutoDAO dao) {
        super(dao);
    }
}
