package model;

import dao.factory.DAOFactory;
import dao.factory.MySqlDAOFactory;
import dao.interfaces.ArticuloDAO;
import dao.interfaces.ClienteDAO;
import dao.interfaces.PedidoDAO;
import java.time.LocalDateTime;
import java.util.List;

public class TiendaOnline {

    private ArticuloDAO articuloDAO;
    private ClienteDAO clienteDAO;
    private PedidoDAO pedidoDAO;
    private DAOFactory factory;

    public TiendaOnline() {

        // 1. Instancia Mysql
        this.factory = new MySqlDAOFactory();

        //Usar DAOfactory
        this.articuloDAO = factory.getArticuloDAO();
        this.clienteDAO = factory.getClienteDAO();
        this.pedidoDAO = factory.getPedidoDAO();
    }


    // ===================== CLIENTES =====================
    public Cliente anadirCliente(String email, String nombre, String domicilio, String nif, boolean premium)throws Exception{
        Cliente cliente;
        if (premium) {
            // Creamos un cliente premium con los valores por defecto
            cliente = new ClientePremium(
                    email,
                    nombre,
                    domicilio,
                    nif,
                    ClientePremium.DESCUENTO_ENVIO_PREMIUM,
                    ClientePremium.CUOTA_ANUAL_PREMIUM
            );
        } else {
            // Creamos un cliente estándar con descuento por defecto
            cliente = new ClienteStandar(
                    email,
                    nombre,
                    domicilio,
                    nif,
                    ClienteStandar.DESCUENTO_ENVIO_STANDAR
            );
        }
        clienteDAO.anadirCliente(cliente);

        return cliente;
    }


    public List<Cliente> mostrarClientes() throws Exception {
        return clienteDAO.getTodosLosClientes();
    }

    public List<Cliente> mostrarClientesEstandar() throws Exception {
        return clienteDAO.getClientesEstandar();
    }

    public List<Cliente> mostrarClientesPremium() throws Exception {
        return clienteDAO.getClientesPremium();
    }

    public Cliente buscarClientePorEmail(String email) throws Exception {
        return clienteDAO.getClientePorEmail(email);
    }

    // ===================== ARTÍCULOS =====================
    public void anadirArticulo(String codigo, String descripcion, Double precioVenta, Double gastosEnvio, int tiempoPreparacion) throws Exception {
        Articulo existente = buscarArticulo(codigo);
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe un artículo con el código: " + codigo);
        }

        Articulo articulo = new Articulo(codigo, descripcion, precioVenta, gastosEnvio, tiempoPreparacion);
        articuloDAO.anadirArticulo(articulo);
    }

    public List<Articulo> mostrarArticulos() throws Exception {
        return articuloDAO.getTodosLosArticulos();
    }

    public Articulo buscarArticulo(String codigo) throws Exception {
        return articuloDAO.getArticuloPorCodigo(codigo);
    }

    // ===================== PEDIDOS =====================
    public void anadirPedido(String numeroPedido, String emailCliente, String codigoArticulo, int cantidad) throws Exception {
        Cliente cliente = clienteDAO.getClientePorEmail(emailCliente);
        Articulo articulo = articuloDAO.getArticuloPorCodigo(codigoArticulo);

        if (cliente == null)
            throw new IllegalArgumentException("No existe el cliente con email: " + emailCliente);
        if (articulo == null)
            throw new IllegalArgumentException("No existe el artículo con código: " + codigoArticulo);
        if (cantidad <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");

        Pedido pedido = new Pedido(numeroPedido, cliente, articulo, cantidad, LocalDateTime.now(), false);
        pedidoDAO.anadirPedido(pedido);
    }

    public boolean eliminarPedido(String numeroPedido) throws Exception {
        return pedidoDAO.eliminarPedido(numeroPedido);
    }

    public void marcarPedidoComoEnviado(String numeroPedido) throws Exception {
        pedidoDAO.marcarPedidoEnviado(numeroPedido);
    }

    public List<Pedido> mostrarPedidosPendientes() throws Exception {
        return pedidoDAO.getPedidosPendientes();
    }

    public List<Pedido> mostrarPedidosPendientes(String emailCliente) throws Exception {
        return pedidoDAO.getPedidosPorCliente(emailCliente)
                .stream().filter(p -> !p.isEstado()).toList();
    }

    public List<Pedido> mostrarPedidosEnviados() throws Exception {
        return pedidoDAO.getPedidosEnviados();
    }

    public List<Pedido> mostrarPedidosEnviados(String emailCliente) throws Exception {
        return pedidoDAO.getPedidosPorCliente(emailCliente)
                .stream().filter(Pedido::isEstado).toList();
    }

    public Pedido buscarPedido(String numeroPedido) throws Exception {
        return pedidoDAO.getPedidoPorNumero(numeroPedido);
    }

    // === ESTADÍSTICAS ===

    public int getTotalArticulos() throws Exception {
        return articuloDAO.getTodosLosArticulos().size();
    }

    public int getTotalClientes() throws Exception {
        return clienteDAO.getTodosLosClientes().size();
    }

    public int getTotalClientesEstandar() throws Exception {
        return clienteDAO.getClientesEstandar().size();
    }

    public int getTotalClientesPremium() throws Exception {
        return clienteDAO.getClientesPremium().size();
    }

    public int getTotalPedidos() throws Exception {
        // Suponiendo que tu DAO tiene un metodo que devuelve todos los pedidos
        return pedidoDAO.getTodosLosPedidos().size();
    }

    public int getTotalPedidosPendientes() throws Exception {
        return pedidoDAO.getPedidosPendientes().size();
    }

    public int getTotalPedidosEnviados() throws Exception {
        return pedidoDAO.getPedidosEnviados().size();
    }
    @Override
    public String toString() {
        try {
            return "TiendaOnline{" +
                    "articulos=" + getTotalArticulos() +
                    ", clientes=" + getTotalClientes() +
                    " (Estandar: " + getTotalClientesEstandar() +
                    ", Premium: " + getTotalClientesPremium() + ")" +
                    ", pedidos=" + getTotalPedidos() +
                    " (Pendientes: " + getTotalPedidosPendientes() +
                    ", Enviados: " + getTotalPedidosEnviados() + ")" +
                    '}';
        } catch (Exception e) {
            return "Error al generar toString: " + e.getMessage();
        }
    }



    // ===================== DATOS DE PRUEBA =====================
    public void cargarDatosDePrueba() throws Exception {
        System.out.println("Cargando datos de prueba...");

        // Artículos
        anadirArticulo("A001", "Laptop Pro 16", 1499.99, 15.0, 120);
        anadirArticulo("A002", "Mouse Inalámbrico", 35.5, 5.0, 10);
        anadirArticulo("A003", "Teclado Mecánico RGB", 110.0, 10.0, 30);
        anadirArticulo("A004", "Monitor Curvo 32", 450.0, 20.0, 180);
        anadirArticulo("A005", "Silla Ergonómica Pro", 220.0, 30.0, 60);

        // Clientes
        anadirCliente("ana.g@mail.com", "Ana García", "Calle Sol 1", "12345678A", false);
        anadirCliente("luis.m@mail.com", "Luis Martínez", "Av. Luna 2", "23456789B", false);
        anadirCliente("eva.p@mail.com", "Eva Pena", "Plaza Mar 3", "34567890C", false);
        anadirCliente("carlos.r@mail.com", "Carlos Ruiz", "Calle Río 4", "45678901D", true);
        anadirCliente("sofia.l@mail.com", "Sofia López", "Av. Monte 5", "56789012E", true);

        // Pedidos
        anadirPedido("P001", "ana.g@mail.com", "A002", 2);
        anadirPedido("P002", "carlos.r@mail.com", "A003", 1);
        anadirPedido("P003", "luis.m@mail.com", "A001", 1);

        // Pedidos enviados (creamos con DAO directamente para simular estado enviado)
        Pedido p4 = new Pedido("P004", buscarClientePorEmail("sofia.l@mail.com"), buscarArticulo("A004"), 1, LocalDateTime.now().minusDays(2), true);
        Pedido p5 = new Pedido("P005", buscarClientePorEmail("eva.p@mail.com"), buscarArticulo("A005"), 1, LocalDateTime.now().minusDays(3), true);
        pedidoDAO.anadirPedido(p4);
        pedidoDAO.anadirPedido(p5);

        System.out.println("Datos de prueba cargados correctamente.");
    }
}
