package onlinestore.tests;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

//Solo comprueba la inicialización y que las listas se cargan correctamente.
class TiendaOnlineTest {

    TiendaOnline tienda;

    @BeforeEach
    void setUp() {
        tienda = new TiendaOnline();
        tienda.cargarDatosDePrueba();
    }

    @Test
    void testArraysVacios() {
        // comprobaciones básicas
        assertNotNull(tienda.mostrarArticulos());
        assertNotNull(tienda.mostrarClientes());
        assertEquals(5, tienda.mostrarArticulos().size());
        assertEquals(5, tienda.mostrarClientes().size());
    }
    @Test
    void testToString() {
        String resultado = tienda.toString();

        // Comprobamos que el resultado contiene los números correctos de artículos, clientes y pedidos
        assertTrue(resultado.contains("articulos=" + tienda.getTotalArticulos()));
        assertTrue(resultado.contains("clientes=" + tienda.getTotalClientes()));
        assertTrue(resultado.contains("Estandar: " + tienda.getTotalClientesEstandar()));
        assertTrue(resultado.contains("Premium: " + tienda.getTotalClientesPremium()));
        assertTrue(resultado.contains("pedidos=" + tienda.getTotalPedidos()));
        assertTrue(resultado.contains("Pendientes: " + tienda.getTotalPedidosPendientes()));
        assertTrue(resultado.contains("Enviados: " + tienda.getTotalPedidosEnviados()));

        System.out.println(resultado);
    }
}