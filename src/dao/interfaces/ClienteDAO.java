package dao.interfaces;

import model.Cliente;
import java.util.List;

public interface ClienteDAO {
    Cliente getClientePorEmail(String id);
    List<Cliente> getTodosLosClientes();
    List<Cliente> getClientesEstandar();
    List<Cliente> getClientesPremium();
}
