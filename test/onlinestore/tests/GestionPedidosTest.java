package onlinestore.tests;

import model.Pedido;
import model.TiendaOnline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestionPedidosTest {

    TiendaOnline tienda;

    @BeforeEach
    void setUp() {
        tienda = new TiendaOnline();
        tienda.cargarDatosDePrueba();
    }

    @Test
    void testAnadirPedidoValido() {
        tienda.anadirPedido("P006", "ana.g@mail.com", "A001", 2);
        Pedido pedido = tienda.buscarPedido("P006");
        assertNotNull(pedido);
        assertEquals(2, pedido.getCantidad());
        assertEquals("ana.g@mail.com", pedido.getCliente().getEmail());
        assertFalse(pedido.isEstado()); // Pendiente por defecto
    }

    @Test
    void testAnadirPedidoArticuloNoExiste() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tienda.anadirPedido("P007", "ana.g@mail.com", "A999", 1);
        });
        assertEquals("No existe el artículo con código: A999", exception.getMessage());
    }

    @Test
    void testAnadirPedidoClienteNoExiste() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tienda.anadirPedido("P008", "no.existe@mail.com", "A001", 1);
        });
        assertTrue(exception.getMessage().contains("No existe el cliente con email: no.existe@mail.com"));
    }

    @Test
    void testAnadirPedidoCantidadInvalida() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tienda.anadirPedido("P009", "ana.g@mail.com", "A001", 0);
        });
        assertEquals("La cantidad debe ser mayor a 0", exception.getMessage());
    }

    @Test
    void testEliminarPedidoPendienteCancelable() {
        boolean eliminado = tienda.eliminarPedido("P001"); // Pedido reciente y pendiente
        assertTrue(eliminado);
        assertNull(tienda.buscarPedido("P001"));
    }

    @Test
    void testEliminarPedidoNoCancelable() {
        boolean eliminado = tienda.eliminarPedido("P003"); // Pedido antiguo
        assertFalse(eliminado);
    }

    @Test
    void testEliminarPedidoEnviado() {
        boolean eliminado = tienda.eliminarPedido("P004"); // Pedido enviado
        assertFalse(eliminado);
    }

    @Test
    void testMarcarPedidoComoEnviado() {
        tienda.marcarPedidoComoEnviado("P002");
        Pedido pedido = tienda.buscarPedido("P002");
        assertTrue(pedido.isEstado());
    }

    @Test
    void testMostrarPedidosPendientesYEnviados() {
        List<Pedido> pendientes = tienda.mostrarPedidosPendientes();
        List<Pedido> enviados = tienda.mostrarPedidosEnviados();

        assertTrue(pendientes.stream().allMatch(p -> !p.isEstado()));
        assertTrue(enviados.stream().allMatch(Pedido::isEstado));
    }

    @Test
    void testMostrarPedidosFiltradosPorCliente() {
        List<Pedido> pendientesAna = tienda.mostrarPedidosPendientes("ana.g@mail.com");
        List<Pedido> enviadosSofia = tienda.mostrarPedidosEnviados("sofia.l@mail.com");

        assertTrue(pendientesAna.stream().allMatch(p -> p.getCliente().getEmail().equals("ana.g@mail.com") && !p.isEstado()));
        assertTrue(enviadosSofia.stream().allMatch(p -> p.getCliente().getEmail().equals("sofia.l@mail.com") && p.isEstado()));
    }
}

