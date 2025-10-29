package onlinestore.tests;

import org.junit.jupiter.api.Test;
import model.ClientePremium;
import static org.junit.jupiter.api.Assertions.*;

public class ClientePremiumTest {


    @Test
    void testClientePremiumConstructorYGetters() {
        ClientePremium cliente = new ClientePremium("juan@mail.com", "Juan", "Barcelona, 123", "33333333C", 20.0, 30);


        assertEquals("juan@mail.com", cliente.getEmail());
        assertEquals("Juan", cliente.getNombre());
        assertEquals("Barcelona, 123", cliente.getDomicilio());
        assertEquals("33333333C", cliente.getNIF());
        assertEquals(20.0, cliente.getDescuentoEnvio());
        assertEquals(30, cliente.getCuotaAnual());

    }

    @Test
    void testClientePremiumSetters() {
        ClientePremium cliente = new ClientePremium("juan@mail.com", "Juan", "Barcelona, 123", "33333333C", 20.0, 30);

        cliente.setEmail("maria@mail.com");
        cliente.setNombre("María");
        cliente.setDomicilio("Avenida Madrid");
        cliente.setNIF("44444444D");
        cliente.setDescuentoEnvio(25.0);
        cliente.setCuotaAnual(35);

        assertEquals("maria@mail.com", cliente.getEmail());
        assertEquals("María", cliente.getNombre());
        assertEquals("Avenida Madrid", cliente.getDomicilio());
        assertEquals("44444444D", cliente.getNIF());
        assertEquals(25.0, cliente.getDescuentoEnvio());
        assertEquals(35, cliente.getCuotaAnual());
    }

    @Test
    void testClientePremiumToString() {
        ClientePremium cliente = new ClientePremium("juan@mail.com", "Juan", "Barcelona, 123", "33333333C", 20.0, 30);

        String esperado = "Cliente{email='juan@mail.com', nombre='Juan', domicilio='Barcelona, 123', NIF='33333333C'}" +
                "ClientePremium{descuentoEnvio=20.0, cuotaAnual=30}";
        assertEquals(esperado, cliente.toString());
    }
}
