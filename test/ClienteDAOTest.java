import br.com.vbruno.dao.ClienteDAO;
import br.com.vbruno.dao.IClienteDAO;
import br.com.vbruno.domain.Cliente;
import br.com.vbruno.exceptions.DAOException;
import br.com.vbruno.exceptions.MaisDeUmRegistroException;
import br.com.vbruno.exceptions.TableException;
import br.com.vbruno.exceptions.TipoChaveNaoEncontradaException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertTrue;

public class ClienteDAOTest {
    private IClienteDAO clienteDAO;

    public ClienteDAOTest() {
        clienteDAO = new ClienteDAO();
    }

    @Test
    public void cadastrarCliente() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Cliente cliente = new Cliente();
        cliente.setCpf(12312312312L);
        cliente.setNome("Vagner");
        cliente.setCidade("Campina Grande");
        cliente.setEnd("Endereço teste");
        cliente.setEstado("PB");
        cliente.setNumero(13);
        cliente.setTel(1199999999L);
        cliente.setIdade(20);
        Boolean retorno = clienteDAO.cadastrar(cliente);
        assertTrue(retorno);

        Cliente clienteConsultado = clienteDAO.consultar(cliente.getCpf());
        Assert.assertNotNull(clienteConsultado);

        clienteDAO.excluir(cliente.getCpf());
        clienteConsultado = clienteDAO.consultar(cliente.getCpf());
        Assert.assertNull(clienteConsultado);
    }

    @Test
    public void buscarCliente() throws DAOException, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException {
        Cliente cliente = new Cliente();
        cliente.setCpf(12312312312L);
        cliente.setNome("Vagner");
        cliente.setCidade("Campina Grande");
        cliente.setEnd("Endereço teste");
        cliente.setEstado("PB");
        cliente.setNumero(13);
        cliente.setTel(1999999999L);
        cliente.setIdade(20);
        Boolean retorno = clienteDAO.cadastrar(cliente);
        assertTrue(retorno);

        Cliente clienteConsultado = clienteDAO.consultar(cliente.getCpf());
        Assert.assertNotNull(clienteConsultado);

        clienteDAO.excluir(cliente.getCpf());
        clienteConsultado = clienteDAO.consultar(cliente.getCpf());
        Assert.assertNull(clienteConsultado);
    }

    @Test
    public void excluirCliente() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Cliente cliente = new Cliente();
        cliente.setCpf(12312312312L);
        cliente.setNome("Vagner");
        cliente.setCidade("Campina Grande");
        cliente.setEnd("Endereço teste");
        cliente.setEstado("PB");
        cliente.setNumero(13);
        cliente.setTel(1199999999L);
        cliente.setIdade(20);
        Boolean retorno = clienteDAO.cadastrar(cliente);
        assertTrue(retorno);

        Cliente clienteConsultado = clienteDAO.consultar(cliente.getCpf());
        Assert.assertNotNull(clienteConsultado);

        clienteDAO.excluir(cliente.getCpf());
        clienteConsultado = clienteDAO.consultar(cliente.getCpf());
        Assert.assertNull(clienteConsultado);
    }

    @Test
    public void alterarCliente() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Cliente cliente = new Cliente();
        cliente.setCpf(12312312312L);
        cliente.setNome("Vagner");
        cliente.setCidade("Campina Grande");
        cliente.setEnd("Endereço teste");
        cliente.setEstado("PB");
        cliente.setNumero(13);
        cliente.setTel(1199999999L);
        cliente.setIdade(20);
        Boolean retorno = clienteDAO.cadastrar(cliente);
        assertTrue(retorno);

        Cliente clienteConsultado = clienteDAO.consultar(cliente.getCpf());
        Assert.assertNotNull(clienteConsultado);

        clienteConsultado.setNome("Vagner Bruno");
        clienteConsultado.setIdade(33);
        clienteDAO.alterar(clienteConsultado);

        Cliente clienteAlterado = clienteDAO.consultar(clienteConsultado.getCpf());
        Assert.assertNotNull(clienteAlterado);
        Assert.assertEquals("Vagner Bruno", clienteAlterado.getNome());
        Assert.assertTrue(clienteAlterado.getIdade() == 33);

        clienteDAO.excluir(cliente.getCpf());
        clienteConsultado = clienteDAO.consultar(cliente.getCpf());
        Assert.assertNull(clienteConsultado);
    }

    @Test
    public void buscarTodosClientes() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
        Cliente cliente = new Cliente();
        cliente.setCpf(12312312312L);
        cliente.setNome("Vagner");
        cliente.setCidade("Campina Grande");
        cliente.setEnd("Endereço de Campina");
        cliente.setEstado("PB");
        cliente.setNumero(5);
        cliente.setTel(1199999999L);
        cliente.setIdade(20);
        Boolean retorno = clienteDAO.cadastrar(cliente);
        assertTrue(retorno);

        Cliente cliente1 = new Cliente();
        cliente1.setCpf(56565656569L);
        cliente1.setNome("Rodrigo");
        cliente1.setCidade("São Paulo");
        cliente1.setEnd("End");
        cliente1.setEstado("SP");
        cliente1.setNumero(10);
        cliente1.setTel(1199999999L);
        cliente1.setIdade(30);
        Boolean retorno1 = clienteDAO.cadastrar(cliente1);
        assertTrue(retorno1);

        Collection<Cliente> list = clienteDAO.buscarTodos();
        assertTrue(list != null);
        assertTrue(list.size() >= 2);

        clienteDAO.excluir(cliente.getCpf());
        Cliente clienteConsultado = clienteDAO.consultar(cliente.getCpf());
        Assert.assertNull(clienteConsultado);
        clienteDAO.excluir(cliente1.getCpf());
        Cliente clienteConsultado1 = clienteDAO.consultar(cliente1.getCpf());
        Assert.assertNull(clienteConsultado1);
    }

}
