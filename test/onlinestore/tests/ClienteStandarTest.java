package onlinestore.tests;

import org.junit.jupiter.api.Test;
import model.ClienteStandar;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteStandarTest {
    // Creo la subclase
    @Test
    void testClienteStandarConstructorYGetters() {
        ClienteStandar cliente = new ClienteStandar("ana@mail.com", "Ana", "Barcelona, 123", "11111111A", 0.0);

        assertEquals("ana@mail.com", cliente.getEmail());
        assertEquals("Ana", cliente.getNombre());
        assertEquals("Barcelona, 123", cliente.getDomicilio());
        assertEquals("11111111A", cliente.getNIF());
        assertEquals(0.0, cliente.getDescuentoEnvio());
    }

    @Test
    void testClienteStandarStters(){
        ClienteStandar cliente = new ClienteStandar("anaM@mail.com", "Ana", "Barcelona, 123", "11111111A", 0.0);

        cliente.setEmail("maria@mail.com");
        cliente.setNombre("Maria");
        cliente.setDomicilio("Tarragona, 123");
        cliente.setNIF("22222222A");
        cliente.setDescuentoEnvio(0.0);


        assertEquals("maria@mail.com", cliente.getEmail());
        assertEquals("Maria", cliente.getNombre());
        assertEquals("Tarragona, 123", cliente.getDomicilio());
        assertEquals("22222222A", cliente.getNIF());
        assertEquals(0.0, cliente.getDescuentoEnvio());

    }

    @Test
    void testClienteStandarToString(){
        ClienteStandar cliente = new ClienteStandar("ana@mail.com", "Ana", "Barcelona, 123", "11111111A", 0.0);

        String esperado = "Cliente{email='ana@mail.com', nombre='Ana', domicilio='Barcelona, 123', NIF='11111111A'}";
        assertEquals(esperado, cliente.toString());

    }


    }
