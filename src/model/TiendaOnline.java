package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TiendaOnline {
    private List<Articulo> articulos;
    private List<Cliente> clientes;
    private List<Pedido> pedidos;

    public TiendaOnline() {
        this.articulos = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }

    // === GESTIÓN DE ARTÍCULOS ===

    public void añadirArticulo(String codigo, String descripcion, Double precioVenta, Double gastosEnvio, int tiempoPreparacion) {
        // Verificar que no existe un artículo con el mismo código
        if (buscarArticulo(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un artículo con el código: " + codigo);
        }else{
            Articulo articulo = new Articulo(codigo, descripcion, precioVenta, gastosEnvio, tiempoPreparacion);
            articulos.add(articulo);
        }
    }

    public List<Articulo> mostrarArticulos() {
        return new ArrayList<>(articulos);
    }

    public Articulo buscarArticulo(String codigo) {
        return articulos.stream()
                .filter(articulo -> articulo.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    // === GESTIÓN DE CLIENTES ===

    public void añadirCliente(String email, String nombre, String domicilio, String nif, Boolean premium) {
        // Verificar que no existe un cliente con el mismo email (identificador)
        if (buscarClientePorEmail(email) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con el email: " + email);
        }else{
            if (!premium){
                ClienteStandar clienteStandar = new ClienteStandar(email, nombre, domicilio, nif, ClienteStandar.DESCUENTO_ENVIO_STANDAR);
                clientes.add(clienteStandar);
            }else{
                ClientePremium clientePremium = new ClientePremium(email, nombre, domicilio, nif, ClientePremium.DESCUENTO_ENVIO_PREMIUM,ClientePremium.CUOTA_ANUAL_PREMIUM);
                clientes.add(clientePremium);
            }
        }
    }

    public List<Cliente> mostrarClientes() {
        return new ArrayList<>(clientes);
    }

    public List<Cliente> mostrarClientesEstandar() {
        return clientes.stream()
                .filter(cliente -> cliente instanceof ClienteStandar)
                .toList();
    }

    public List<Cliente> mostrarClientesPremium() {
        return clientes.stream()
                .filter(cliente -> cliente instanceof ClientePremium)
                .toList();
    }

    public Cliente buscarClientePorEmail(String email) {
        return clientes.stream()
                .filter(cliente -> cliente.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public Cliente buscarClientePorNIF(String nif) {
        return clientes.stream()
                .filter(cliente -> cliente.getNIF().equals(nif))
                .findFirst()
                .orElse(null);
    }

    // === GESTIÓN DE PEDIDOS ===

    public void anadirPedido(String numeroPedido, String emailCliente, String codigoArticulo, int cantidad) {
        // Verificar que el artículo existe
        Articulo articulo = buscarArticulo(codigoArticulo);
        if (articulo == null) {
            throw new IllegalArgumentException("No existe el artículo con código: " + codigoArticulo);
        }

        // Buscar cliente por email
        Cliente cliente = buscarClientePorEmail(emailCliente);

        // Si el cliente no existe, lanzamos excepción (según requisitos, se deberían pedir los datos)
        if (cliente == null) {
            throw new IllegalArgumentException("No existe el cliente con email: " + emailCliente +
                    ". Se deben pedir los datos del nuevo cliente primero.");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        Pedido pedido = new Pedido(
                numeroPedido,
                cliente,
                articulo,
                cantidad,
                LocalDateTime.now(),
                false // estado inicial: pendiente (no enviado)
        );
        pedidos.add(pedido);  // CORREGIDO: pedidos.add() no pedido.add()
    }

    public boolean eliminarPedido(String numeroPedido) {
        Pedido pedido = buscarPedido(numeroPedido);
        if (pedido != null && !estaEnviado(pedido) && puedeSerCancelado(pedido)) {
            return pedidos.remove(pedido);  // CORREGIDO: pedidos.remove() no pedido.remove()
        }
        return false;
    }

    public List<Pedido> mostrarPedidosPendientes() {  // CORREGIDO: mostrarPedidosPendientes (plural)
        return pedidos.stream()  // CORREGIDO: pedidos.stream() no pedido.stream()
                .filter(pedido -> !pedido.isEstado()) // estado false = pendiente
                .toList();
    }

    public List<Pedido> mostrarPedidosPendientes(String emailCliente) {  // CORREGIDO: plural
        return pedidos.stream()  // CORREGIDO: pedidos.stream()
                .filter(pedido -> !pedido.isEstado() &&
                        pedido.getCliente().getEmail().equals(emailCliente))
                .toList();
    }

    public List<Pedido> mostrarPedidosEnviados() {  // CORREGIDO: plural
        return pedidos.stream()  // CORREGIDO: pedidos.stream()
                .filter(Pedido::isEstado) // estado true = enviado
                .toList();
    }

    public List<Pedido> mostrarPedidosEnviados(String emailCliente) {  // CORREGIDO: plural
        return pedidos.stream()  // CORREGIDO: pedidos.stream()
                .filter(pedido -> pedido.isEstado() &&
                        pedido.getCliente().getEmail().equals(emailCliente))
                .toList();
    }

    public void marcarPedidoComoEnviado(String numeroPedido) {
        Pedido pedido = buscarPedido(numeroPedido);
        if (pedido != null) {
            pedido.setEstado(true);
        }
    }

    // === MÉTODOS AUXILIARES ===

    public Pedido buscarPedido(String numeroPedido) {
        return pedidos.stream()  // CORREGIDO: pedidos.stream()
                .filter(pedido -> pedido.getNumeroPedido().equals(numeroPedido))
                .findFirst()
                .orElse(null);
    }

    private boolean estaEnviado(Pedido pedido) {
        return pedido.isEstado();
    }

    private boolean puedeSerCancelado(Pedido pedido) {
        LocalDateTime fechaPedido = pedido.getFechaHora();
        LocalDateTime ahora = LocalDateTime.now();
        long minutosTranscurridos = ChronoUnit.MINUTES.between(fechaPedido, ahora);

        return minutosTranscurridos <= pedido.getArticulo().getTiempoPreparacion();
    }


    // === ESTADÍSTICAS ===

    public int getTotalArticulos() {
        return articulos.size();
    }

    public int getTotalClientes() {
        return clientes.size();
    }

    public int getTotalClientesEstandar() {
        return mostrarClientesEstandar().size();
    }

    public int getTotalClientesPremium() {
        return mostrarClientesPremium().size();
    }

    public int getTotalPedidos() {  // CORREGIDO: getTotalPedidos (plural)
        return pedidos.size();  // CORREGIDO: pedidos.size()
    }

    public int getTotalPedidosPendientes() {  // CORREGIDO: plural
        return mostrarPedidosPendientes().size();
    }

    public int getTotalPedidosEnviados() {  // CORREGIDO: plural
        return mostrarPedidosEnviados().size();
    }

    @Override
    public String toString() {
        return "TiendaOnline{" +
                "articulos=" + getTotalArticulos() +
                ", clientes=" + getTotalClientes() +
                " (Estandar: " + getTotalClientesEstandar() +
                ", Premium: " + getTotalClientesPremium() + ")" +
                ", pedidos=" + getTotalPedidos() +
                " (Pendientes: " + getTotalPedidosPendientes() +
                ", Enviados: " + getTotalPedidosEnviados() + ")" +
                '}';
    }

    /**
     * Carga un conjunto de datos de prueba en el modelo.
     */
    public void cargarDatosDePrueba() {
        System.out.println("Cargando datos de prueba...");
        try {
            // 1. Añadir 5 Artículos
            anadirArticulo("A001", "Laptop Pro 16", 1499.99, 15.00, 120); // 120 min prep
            anadirArticulo("A002", "Mouse Inalámbrico", 35.50, 5.00, 10);    // 10 min prep
            anadirArticulo("A003", "Teclado Mecánico RGB", 110.00, 10.00, 30); // 30 min prep
            anadirArticulo("A004", "Monitor Curvo 32", 450.00, 20.00, 180); // 180 min prep
            anadirArticulo("A005", "Silla Ergonómica Pro", 220.00, 30.00, 60);  // 60 min prep

            // 2. Añadir 5 Clientes (3 Estandar, 2 Premium)
            // Estandar
            anadirCliente("ana.g@mail.com", "Ana García", "Calle Sol 1", "12345678A", false);
            anadirCliente("luis.m@mail.com", "Luis Martínez", "Av. Luna 2", "23456789B", false);
            anadirCliente("eva.p@mail.com", "Eva Pena", "Plaza Mar 3", "34567890C", false);
            // Premium
            anadirCliente("carlos.r@mail.com", "Carlos Ruiz", "Calle Río 4", "45678901D", true);
            anadirCliente("sofia.l@mail.com", "Sofia López", "Av. Monte 5", "56789012E", true);

            // 3. Añadir 5 Pedidos (3 Pendientes, 2 Enviados)
            // Para esto, necesitamos acceder a los objetos creados:
            Articulo art1 = buscarArticulo("A002"); // 10 min prep
            Articulo art2 = buscarArticulo("A001"); // 120 min prep
            Articulo art3 = buscarArticulo("A003"); // 30 min prep
            Articulo art4 = buscarArticulo("A004");
            Articulo art5 = buscarArticulo("A005");

            Cliente cli1 = buscarClientePorEmail("ana.g@mail.com");
            Cliente cli2 = buscarClientePorEmail("luis.m@mail.com");
            Cliente cli3 = buscarClientePorEmail("carlos.r@mail.com"); // Premium
            Cliente cli4 = buscarClientePorEmail("sofia.l@mail.com"); // Premium
            Cliente cli5 = buscarClientePorEmail("eva.p@mail.com");

            // --- Pedidos Pendientes ---
            // Pedido 1 (Pendiente, Reciente -> Cancelable)
            // Usamos LocalDateTime.now() para que la lógica de cancelación funcione (ver corrección abajo)
            Pedido p1 = new Pedido("P001", cli1, art1, 2, LocalDateTime.now(), false);
            pedidos.add(p1);

            // Pedido 2 (Pendiente, Reciente -> Cancelable)
            Pedido p2 = new Pedido("P002", cli3, art3, 1, LocalDateTime.now(), false);
            pedidos.add(p2);

            // Pedido 3 (Pendiente, Antiguo -> NO Cancelable)
            // Creado hace 1 día. Su tiempo (120 min) ya pasó.
            Pedido p3 = new Pedido("P003", cli2, art2, 1, LocalDateTime.now().minusDays(1), false);
            pedidos.add(p3);


            // --- Pedidos Enviados ---
            // Pedido 4 (Enviado)
            Pedido p4 = new Pedido("P004", cli4, art4, 1, LocalDateTime.now().minusDays(2), true); // true = enviado
            pedidos.add(p4);

            // Pedido 5 (Enviado)
            Pedido p5 = new Pedido("P005", cli5, art5, 1, LocalDateTime.now().minusDays(3), true); // true = enviado
            pedidos.add(p5);

            System.out.println("Datos de prueba cargados correctamente.");

        } catch (Exception e) {
            System.err.println("ERROR al cargar datos de prueba: " + e.getMessage());
        }
    }
}