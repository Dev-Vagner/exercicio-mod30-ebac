import br.com.vbruno.dao.*;
import br.com.vbruno.dao.jdbc.ConnectionFactory;
import br.com.vbruno.domain.Cliente;
import br.com.vbruno.domain.Produto;
import br.com.vbruno.domain.Venda;
import br.com.vbruno.exceptions.DAOException;
import br.com.vbruno.exceptions.MaisDeUmRegistroException;
import br.com.vbruno.exceptions.TableException;
import br.com.vbruno.exceptions.TipoChaveNaoEncontradaException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;

import static br.com.vbruno.dao.jdbc.ConnectionFactory.getConnection;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class VendaDAOTest {
    private IVendaDAO vendaDAO;
    private IClienteDAO clienteDAO;
    private IProdutoDAO produtoDAO;
    private Cliente cliente;
    private Produto produto;

    public VendaDAOTest() {
        vendaDAO = new VendaDAO();
        clienteDAO = new ClienteDAO();
        produtoDAO = new ProdutoDAO();
    }

    @Before
    public void init() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        this.cliente = cadastrarCliente();
        this.produto = cadastrarProduto("A1", BigDecimal.TEN);
    }

    @After
    public void end() throws DAOException {
        excluirVendas();
        excluirProdutos();
        clienteDAO.excluir(this.cliente.getCpf());
    }

    @Test
    public void cadastrarVenda() throws DAOException, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException {
        Venda venda = criarVenda("A2");
        Boolean vendaCadastrada = vendaDAO.cadastrar(venda);
        assertTrue(vendaCadastrada);

        assertTrue(venda.getValorTotal().equals(BigDecimal.valueOf(20)));
        assertTrue(venda.getStatus().equals(Venda.Status.INICIADA));

        Venda vendaConsultada = vendaDAO.consultar(venda.getCodigo());
        assertTrue(vendaConsultada.getId() != null);
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void buscarVenda() throws DAOException, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException {
        Venda venda = criarVenda("A1");
        Boolean retorno = vendaDAO.cadastrar(venda);
        assertTrue(retorno);

        Venda vendaConsultada = vendaDAO.consultar(venda.getCodigo());
        assertNotNull(vendaConsultada);
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void cancelarVenda() throws DAOException, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException {
        String codigoVenda = "A3";
        Venda venda = criarVenda(codigoVenda);
        Boolean vendaCadastrada = vendaDAO.cadastrar(venda);
        assertTrue(vendaCadastrada);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        vendaDAO.cancelarVenda(venda);

        Venda vendaConsultada = vendaDAO.consultar(codigoVenda);
        assertEquals(codigoVenda, vendaConsultada.getCodigo());
        assertEquals(Venda.Status.CANCELADA, vendaConsultada.getStatus());
    }

    @Test
    public void adicionarMaisProdutosDoMesmo() throws DAOException, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException {
        String codigoVenda = "A4";
        Venda venda = criarVenda(codigoVenda);
        Boolean vendaCadastrada = vendaDAO.cadastrar(venda);
        assertTrue(vendaCadastrada);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Venda vendaConsultada = vendaDAO.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(produto, 1);

        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void adicionarMaisProdutosDiferentes() throws DAOException, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException {
        String codigoVenda = "A5";
        Venda venda = criarVenda(codigoVenda);
        Boolean vendaCadastrada = vendaDAO.cadastrar(venda);
        assertTrue(vendaCadastrada);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDAO.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(prod, 1);

        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test(expected = DAOException.class)
    public void salvarVendaMesmoCodigoExistente() throws TipoChaveNaoEncontradaException, DAOException {
        Venda venda = criarVenda("A6");
        Boolean retorno = vendaDAO.cadastrar(venda);
        assertTrue(retorno);

        Boolean retorno1 = vendaDAO.cadastrar(venda);
        assertFalse(retorno1);
        assertTrue(venda.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A7";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDAO.cadastrar(venda);
        assertTrue(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDAO.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerApenasUmProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A8";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDAO.cadastrar(venda);
        assertTrue(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDAO.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerTodosProdutos() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A9";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDAO.cadastrar(venda);
        assertTrue(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDAO.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(prod, 1);
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerTodosProdutos();
        assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 0);
        assertTrue(vendaConsultada.getValorTotal().equals(BigDecimal.valueOf(0)));
        assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void finalizarVenda() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A10";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDAO.cadastrar(venda);
        assertTrue(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        vendaDAO.finalizarVenda(venda);

        Venda vendaConsultada = vendaDAO.consultar(codigoVenda);
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        assertEquals(Venda.Status.CONCLUIDA, vendaConsultada.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void tentarAdicionarProdutosVendaFinalizada() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A11";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDAO.cadastrar(venda);
        assertTrue(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());

        vendaDAO.finalizarVenda(venda);
        Venda vendaConsultada = vendaDAO.consultar(codigoVenda);
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        assertEquals(Venda.Status.CONCLUIDA, vendaConsultada.getStatus());

        vendaConsultada.adicionarProduto(this.produto, 1);

    }

    private Cliente cadastrarCliente() throws DAOException, TipoChaveNaoEncontradaException {
        Cliente cliente = new Cliente();
        cliente.setCpf(12312312312L);
        cliente.setNome("Cliente Teste");
        cliente.setCidade("Campina Grande");
        cliente.setEnd("End");
        cliente.setEstado("PB");
        cliente.setNumero(10);
        cliente.setTel(1199999999L);
        cliente.setIdade(20);
        clienteDAO.cadastrar(cliente);
        return cliente;
    }

    private Produto cadastrarProduto(String codigo, BigDecimal valor) throws DAOException, TipoChaveNaoEncontradaException {
        Produto produto = new Produto();
        produto.setCodigo(codigo);
        produto.setNome("Produto 1");
        produto.setDescricao("Descrição do Produto 1");
        produto.setValor(valor);
        produto.setMarca("Marca do Produto 1");
        produtoDAO.cadastrar(produto);
        return produto;
    }

    private void excluirProdutos() throws DAOException {
        Collection<Produto> list = this.produtoDAO.buscarTodos();
        for (Produto prod : list) {
            this.produtoDAO.excluir(prod.getCodigo());
        }
    }

    private Venda criarVenda(String codigo) {
        Venda venda = new Venda();
        venda.setCodigo(codigo);
        venda.setDataVenda(Instant.now());
        venda.setCliente(this.cliente);
        venda.setStatus(Venda.Status.INICIADA);
        venda.adicionarProduto(this.produto, 2);
        return venda;
    }

    private void excluirVendas() throws DAOException {
        String sqlProd = "DELETE FROM TB_PRODUTO_QUANTIDADE";
        executeDelete(sqlProd);

        String sqlV = "DELETE FROM TB_VENDA";
        executeDelete(sqlV);
    }

    private void executeDelete(String sql) throws DAOException {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            stm.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("ERRO EXLUINDO OBJETO ", e);
        } finally {
            closeConnection(connection, stm, rs);
        }
    }

    protected Connection getConnection() throws DAOException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new DAOException("ERRO ABRINDO CONEXAO COM BANCO DE DADOS ", e);
        }
    }

    protected void closeConnection(Connection connection, PreparedStatement stm, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
            if (connection != null && !stm.isClosed()) {
                connection.close();
            }
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
