package onlinestore.tests;

import org.junit.jupiter.api.Test;
import model.Articulo;
import static org.junit.jupiter.api.Assertions.*;

public class ArticuloTest {

    @Test
    void testConstructorYGetters() {
        Articulo articulo = new Articulo("A001", "Libro", 25.0, 5.0, 2);

        assertEquals("A001", articulo.getCodigo());
        assertEquals("Libro", articulo.getDescripcion());
        assertEquals(25.0, articulo.getPrecioVenta());
        assertEquals(5.0, articulo.getGastosEnvio());
        assertEquals(2, articulo.getTiempoPreparacion());
    }


    @Test
    void testSetters() {
        Articulo articulo = new Articulo("A001", "Libro", 25.0, 5.0, 2);

        articulo.setCodigo("B002");
        articulo.setDescripcion("Cuaderno");
        articulo.setPrecioVenta(30.0);
        articulo.setGastosEnvio(7.5);
        articulo.setTiempoPreparacion(3);

        assertEquals("B002", articulo.getCodigo());
        assertEquals("Cuaderno", articulo.getDescripcion());
        assertEquals(30.0, articulo.getPrecioVenta());
        assertEquals(7.5, articulo.getGastosEnvio());
        assertEquals(3, articulo.getTiempoPreparacion());
    }


    @Test
    void testToString() {
        Articulo articulo = new Articulo("A001", "Libro", 25.0, 5.0, 2);

        String esperado = "Articulo{codigo='A001', descripcion='Libro', precioVenta=25.0, gastosEnvio=5.0, tiempoPreparacion=2}";
        assertEquals(esperado, articulo.toString());
    }
}