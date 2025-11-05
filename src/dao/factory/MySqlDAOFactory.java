package dao.factory;

import dao.impl.ArticuloDAOImpl;
import dao.impl.ClienteDAOImpl;
import dao.impl.PedidosDAOImpl;
import dao.interfaces.ArticuloDAO;
import dao.interfaces.ClienteDAO;
import dao.interfaces.PedidoDAO;

public class MySqlDAOFactory implements DAOFactory {

    // Guarda las instancias para reutilizarlas
    private ArticuloDAO articuloDAO;
    private ClienteDAO clienteDAO;
    private PedidoDAO pedidoDAO;

    @Override
    public ArticuloDAO getArticuloDAO() {
        if (this.articuloDAO == null) {
            this.articuloDAO = new ArticuloDAOImpl();
        }
        return this.articuloDAO;
    }

    @Override
    public ClienteDAO getClienteDAO() {
        if (this.clienteDAO == null) {
            this.clienteDAO = new ClienteDAOImpl();
        }
        return this.clienteDAO;
    }

    @Override
    public PedidoDAO getPedidoDAO() {
        if (this.pedidoDAO == null) {

            // ¡AQUÍ ESTÁ LA MAGIA!
            // Cuando creas el PedidoDAO...
            // ...le pasas los otros DAOs que la fábrica ya conoce.

            this.pedidoDAO = new PedidosDAOImpl( getArticuloDAO(), getClienteDAO() );
        }
        return this.pedidoDAO;
    }
}