package dao.impl;

import dao.interfaces.ArticuloDAO;
import dao.interfaces.ClienteDAO;
import dao.interfaces.PedidoDAO;
import model.Articulo;
import model.Cliente;
import model.Pedido;
import util.ConexionBD; // Usa la clase de conexión del proyecto

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidosDAOImpl implements PedidoDAO {

    //VARIABLES DE INSTANCIA PARA GUARDAR LOS OTROS DAOs
    private ArticuloDAO articuloDAO;
    private ClienteDAO clienteDAO;

    // CONSTRUCTOR PARA LA INYECCIÓN DE DEPENDENCIAS
    //    El Miembro 4 (Fábrica) llamará a este constructor.
    public PedidosDAOImpl(ArticuloDAO articuloDAO, ClienteDAO clienteDAO) {
        this.articuloDAO = articuloDAO;
        this.clienteDAO = clienteDAO;
    }

    @Override
    public Pedido getPedidoPorNumero(String numeroPedido) throws SQLException {

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareCall("{CALL sp_getPedidoByNumero(?)}")) {

            // Asignamos el parámetro del PreparedStatement
            ps.setString(1, numeroPedido);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String numeroPedidoDB = rs.getString("numero_pedido"); // número de pedido desde BD
                    String emailCliente = rs.getString("email");      // email del cliente
                    String codigoArticulo = rs.getString("codigo");   // código del artículo
                    int cantidad = rs.getInt("cantidad");
                    LocalDateTime fechaHora = rs.getObject("fecha_hora", LocalDateTime.class);
                    boolean estado = rs.getBoolean("estado");

                    // Buscamos el cliente y el artículo usando sus DAOs mediante Factory
                    Cliente cliente = this.clienteDAO.getClientePorEmail(emailCliente);
                    Articulo articulo = this.articuloDAO.getArticuloPorCodigo(codigoArticulo);

                    return new Pedido(numeroPedidoDB, cliente, articulo, cantidad, fechaHora, estado);
                } else {
                    return null;
                }
            } catch (Exception e) {
                throw new SQLException("No hay ningún pedido con este número: "+ e.getMessage());
            }
        }
    }

    @Override
    public List<Pedido> getTodosLosPedidos() throws SQLException {
        List<Pedido> listaPedidos = new ArrayList<>();
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareCall("{CALL sp_getAllPedidos()}");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String numeroPedidoDB = rs.getString("numero_pedido");
                String emailCliente = rs.getString("email");
                String codigoArticulo = rs.getString("codigo");
                int cantidad = rs.getInt("cantidad");
                LocalDateTime fechaHora = rs.getObject("fecha_hora", LocalDateTime.class);
                boolean estado = rs.getBoolean("estado");

                Cliente cliente = this.clienteDAO.getClientePorEmail(emailCliente);
                Articulo articulo = this.articuloDAO.getArticuloPorCodigo(codigoArticulo);

                listaPedidos.add(new Pedido(numeroPedidoDB, cliente, articulo, cantidad, fechaHora, estado));
            }
        } catch (Exception e) {
            throw new SQLException("Error al obtener todos los pedidos: "+ e.getMessage(), e);
        }

        return listaPedidos;
    }

    @Override
    public List<Pedido> getPedidosPorCliente(String emailCliente) throws SQLException {
        List<Pedido> listaPedidos = new ArrayList<>();

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareCall("{CALL: sp_getAllPedidosByCliente(?)}")) {

            ps.setString(1, emailCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String numeroPedidoDB = rs.getString("numero_pedido");
                    String emailClienteBD = rs.getString("email");
                    String codigoArticulo = rs.getString("codigo");
                    int cantidad = rs.getInt("cantidad");
                    LocalDateTime fechaHora = rs.getObject("fecha_hora", LocalDateTime.class);
                    boolean estado = rs.getBoolean("estado");

                    Cliente cliente = this.clienteDAO.getClientePorEmail(emailClienteBD);
                    Articulo articulo = this.articuloDAO.getArticuloPorCodigo(codigoArticulo);

                    listaPedidos.add(new Pedido(numeroPedidoDB, cliente, articulo, cantidad, fechaHora, estado));
                }
            } catch (Exception e) {
                throw new SQLException("Error al obtener pedidos por cliente: " + e.getMessage(), e);
            }
        }

        return listaPedidos;
    }

    @Override
    public List<Pedido> getPedidosPendientes() throws SQLException {
        List<Pedido> listaPedidos = new ArrayList<>();

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareCall("{CALL: sp_getPedidosPendientes()}")) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String numeroPedidoDB = rs.getString("numero_pedido");
                    String emailClienteBD = rs.getString("email");
                    String codigoArticulo = rs.getString("codigo");
                    int cantidad = rs.getInt("cantidad");
                    LocalDateTime fechaHora = rs.getObject("fecha_hora", LocalDateTime.class);
                    boolean estado = rs.getBoolean("estado");

                    Cliente cliente = this.clienteDAO.getClientePorEmail(emailClienteBD);
                    Articulo articulo = this.articuloDAO.getArticuloPorCodigo(codigoArticulo);

                    listaPedidos.add(new Pedido(numeroPedidoDB, cliente, articulo, cantidad, fechaHora, estado));
                }
            } catch (Exception e) {
                throw new SQLException("error al obtener pedidos pendientes: "+ e.getMessage());
            }
        }

        return listaPedidos;
    }

    @Override
    public List<Pedido> getPedidosEnviados() throws SQLException {
        List<Pedido> listaPedidos = new ArrayList<>();

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareCall("{CALL: sp_getPedidosEnviados()}")) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String numeroPedidoDB = rs.getString("numero_pedido");
                    String emailClienteBD = rs.getString("email");
                    String codigoArticulo = rs.getString("articulo");
                    int cantidad = rs.getInt("cantidad");
                    LocalDateTime fechaHora = rs.getObject("fecha_hora", LocalDateTime.class);
                    boolean estado = rs.getBoolean("estado");

                    Cliente cliente = this.clienteDAO.getClientePorEmail(emailClienteBD);
                    Articulo articulo = this.articuloDAO.getArticuloPorCodigo(codigoArticulo);

                    listaPedidos.add(new Pedido(numeroPedidoDB, cliente, articulo, cantidad, fechaHora, estado));
                }
            } catch (Exception e) {
                throw new SQLException("Error al obtener pedidos enviados: " + e.getMessage(), e);
            }
        }

        return listaPedidos;
    }

    @Override
    public void anadirPedido(Pedido pedido) throws SQLException {
        Connection conn = null;

        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false); // inicia la transacción

            try (CallableStatement cs = conn.prepareCall("{CALL sp_addPedido(?,?,?,?)}")) {
                cs.setString(1, pedido.getNumeroPedido());
                cs.setString(2, pedido.getCliente().getEmail());
                cs.setString(3, pedido.getArticulo().getCodigo());
                cs.setInt(4, pedido.getCantidad());

                cs.executeUpdate();
            }
            conn.commit(); //confirma la transacción

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); //se deshace la transacción si algo falla.
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            throw e; // lanza error al controlador
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public boolean eliminarPedido(String numeroPedido) throws SQLException {
        Connection conn = null;
        int filasAfectadas = 0;

        try{
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false);//inicia la transacción

            try (CallableStatement cs = conn.prepareCall("{CALL sp_deletePedido(?)}")){
                cs.setString(1, numeroPedido);
                filasAfectadas = cs.executeUpdate();
                conn.commit();
            }
        }catch (SQLException e){
            if (conn != null){
                try{
                    conn.rollback(); //se deshace la transacción si algo falla.
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: "+ ex.getMessage());
                }
            }
            throw e; // lanza error al controlador
        }finally {
            if (conn != null){
                try{
                    conn.close();
                }catch(SQLException e){
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
        return filasAfectadas > 0; //devuelve true si se han borrado 1 o + filas
    }

    @Override
    public void marcarPedidoEnviado(String numeroPedido) throws SQLException {
        try (Connection conn = ConexionBD.getConnection();
        CallableStatement cs = conn.prepareCall("{CALL sp_marcarPedidoEnviado(?)}")){
            cs.setString(1, numeroPedido);
            cs.executeUpdate();
        }catch (SQLException e){
            throw e;
        }
    }
}
