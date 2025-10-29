package onlinestore.tests;

import model.Cliente;
import model.ClientePremium;
import model.ClienteStandar;
import model.TiendaOnline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GestionClientesTest {

    private TiendaOnline tienda;

    @BeforeEach
    void setUp() {
        tienda = new TiendaOnline();
        tienda.cargarDatosDePrueba(); // Carga clientes de ejemplo
    }


    @Test
    void testAnadirClienteEstandarValido() {
        tienda.anadirCliente("nuevo@mail.com", "Pedro López", "Calle Nueva 10", "99999999A", false);

        Cliente cliente = tienda.buscarClientePorEmail("nuevo@mail.com");
        assertNotNull(cliente);
        assertTrue(cliente instanceof ClienteStandar);
        assertEquals("Pedro López", cliente.getNombre());
        assertEquals("99999999A", cliente.getNIF());
    }

    @Test
    void testAnadirClientePremiumValido() {
        tienda.anadirCliente("premium@mail.com", "Laura Pérez", "Avenida del Sol 5", "88888888B", true);

        Cliente cliente = tienda.buscarClientePorEmail("premium@mail.com");
        assertNotNull(cliente);
        assertTrue(cliente instanceof ClientePremium);
        assertEquals("Laura Pérez", cliente.getNombre());
        assertEquals("88888888B", cliente.getNIF());
    }


    @Test
    void testAnadirClienteDuplicadoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            tienda.anadirCliente("ana.g@mail.com", "Ana García", "Otra calle", "60000000A", false);
        });
    }

    @Test
    void testBuscarClientePorEmailExistente() {
        Cliente cliente = tienda.buscarClientePorEmail("ana.g@mail.com");
        assertNotNull(cliente);
        assertEquals("Ana García", cliente.getNombre());
    }

    @Test
    void testBuscarClientePorEmailInexistente() {
        Cliente cliente = tienda.buscarClientePorEmail("noexiste@mail.com");
        assertNull(cliente);
    }

    @Test
    void testBuscarClientePorNIFExistente() {
        Cliente cliente = tienda.buscarClientePorNIF("12345678A");
        assertNotNull(cliente);
        assertEquals("Ana García", cliente.getNombre());
    }

    @Test
    void testBuscarClientePorNIFInexistente() {
        Cliente cliente = tienda.buscarClientePorNIF("99999999Z");
        assertNull(cliente);
    }


    @Test
    void testMostrarClientes() {
        List<Cliente> lista = tienda.mostrarClientes();
        assertFalse(lista.isEmpty());
        assertTrue(lista.size() >= 5); // Por los datos de prueba
    }


    @Test
    void testMostrarClientesEstandar() {
        List<Cliente> estandar = tienda.mostrarClientesEstandar();
        assertFalse(estandar.isEmpty());
        assertTrue(estandar.stream().allMatch(c -> c instanceof ClienteStandar));
    }


    @Test
    void testMostrarClientesPremium() {
        List<Cliente> premium = tienda.mostrarClientesPremium();
        assertFalse(premium.isEmpty());
        assertTrue(premium.stream().allMatch(c -> c instanceof ClientePremium));
    }
}
