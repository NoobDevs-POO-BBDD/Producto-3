package dao.factory;

import dao.interfaces.ArticuloDAO;
import dao.interfaces.ClienteDAO;
import dao.interfaces.PedidoDAO;

/**
 * Esta es la INTERFAZ (el contrato).
 */
public interface DAOFactory {

    ArticuloDAO getArticuloDAO();

    ClienteDAO getClienteDAO();

    PedidoDAO getPedidoDAO();
}