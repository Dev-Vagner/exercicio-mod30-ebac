import br.com.vbruno.dao.IProdutoDAO;
import br.com.vbruno.dao.ProdutoDAO;
import br.com.vbruno.domain.Produto;
import br.com.vbruno.exceptions.DAOException;
import br.com.vbruno.exceptions.MaisDeUmRegistroException;
import br.com.vbruno.exceptions.TableException;
import br.com.vbruno.exceptions.TipoChaveNaoEncontradaException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

public class ProdutoDAOTest {
    private IProdutoDAO produtoDAO;

    public ProdutoDAOTest() {
        produtoDAO = new ProdutoDAO();
    }

    @Test
    public void cadastrarProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Produto produto = criarProduto("A2", "Produto1", "Descrição do produto 1", BigDecimal.TEN, "Marca do Produto 1");
        Assert.assertNotNull(produto);

        Produto produtoBD = this.produtoDAO.consultar(produto.getCodigo());
        Assert.assertNotNull(produtoBD);

        deletar(produto.getCodigo());
        produtoBD = this.produtoDAO.consultar(produto.getCodigo());
        Assert.assertNull(produtoBD);
    }

    @Test
    public void buscarProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Produto produto = criarProduto("A2", "Produto1", "Descrição do produto 1", BigDecimal.TEN, "Marca do produto 1");
        Assert.assertNotNull(produto);

        Produto produtoBD = this.produtoDAO.consultar(produto.getCodigo());
        Assert.assertNotNull(produtoBD);

        deletar(produto.getCodigo());
        produtoBD = this.produtoDAO.consultar(produto.getCodigo());
        Assert.assertNull(produtoBD);
    }

    @Test
    public void excluirProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Produto produto = criarProduto("A2", "Produto1", "Descrição do produto 1", BigDecimal.TEN, "Marca do produto 1");
        Assert.assertNotNull(produto);

        Produto produtoBD = this.produtoDAO.consultar(produto.getCodigo());
        Assert.assertNotNull(produtoBD);

        deletar(produto.getCodigo());
        produtoBD = this.produtoDAO.consultar(produto.getCodigo());
        Assert.assertNull(produtoBD);
    }

    @Test
    public void alterarProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Produto produto = criarProduto("A2", "Produto1", "Descrição do produto 1", BigDecimal.TEN, "Marca do produto 1");
        Assert.assertNotNull(produto);
        Assert.assertEquals("Produto1", produto.getNome());

        produto.setNome("Produto Alterado");
        produto.setMarca("Marca Alterada");
        produtoDAO.alterar(produto);

        Produto produtoBD = this.produtoDAO.consultar(produto.getCodigo());
        Assert.assertNotNull(produtoBD);
        Assert.assertEquals("Produto Alterado", produtoBD.getNome());
        Assert.assertEquals("Marca Alterada", produtoBD.getMarca());

        deletar(produto.getCodigo());
        produtoBD = this.produtoDAO.consultar(produto.getCodigo());
        Assert.assertNull(produtoBD);
    }

    @Test
    public void buscarTodosProdutos() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Produto produto1 = criarProduto("A2", "Produto1", "Descrição do produto 1", BigDecimal.TEN, "Marca do produto 1");
        Produto produto2 = criarProduto("A3", "Produto2", "Descrição do produto 2", BigDecimal.TEN, "Marca do produto 2");
        Assert.assertNotNull(produto1);
        Assert.assertNotNull(produto2);

        Collection<Produto> produtosBD = this.produtoDAO.buscarTodos();
        Assert.assertNotNull(produtosBD);
        Assert.assertTrue(produtosBD.size() >= 2);

        deletar(produto1.getCodigo());
        produto1 = this.produtoDAO.consultar(produto1.getCodigo());
        Assert.assertNull(produto1);

        deletar(produto2.getCodigo());
        produto2 = this.produtoDAO.consultar(produto2.getCodigo());
        Assert.assertNull(produto2);
    }

    private Produto criarProduto(String codigo, String nome, String descricao, BigDecimal valor, String marca) throws DAOException, TipoChaveNaoEncontradaException {
        Produto produto = new Produto();
        produto.setCodigo(codigo);
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setValor(valor);
        produto.setMarca(marca);
        produtoDAO.cadastrar(produto);
        return produto;
    }

    private void deletar(String codigo) throws DAOException {
        this.produtoDAO.excluir(codigo);
    }

}
