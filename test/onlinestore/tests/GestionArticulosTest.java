package onlinestore.tests;

import model.Articulo;
import model.TiendaOnline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class GestionArticulosTest {
        private TiendaOnline tienda;

        @BeforeEach
        void setUp() {
            tienda = new TiendaOnline();
            tienda.cargarDatosDePrueba(); // Carga los artículos iniciales
        }


        @Test
        void testAnadirArticuloValido() {
            tienda.anadirArticulo("A999", "Tablet Samsung", 300.0, 10.0, 2);

            Articulo articulo = tienda.buscarArticulo("A999");
            assertNotNull(articulo);
            assertEquals("Tablet Samsung", articulo.getDescripcion());
            assertEquals(300.0, articulo.getPrecioVenta());
            assertEquals(10.0, articulo.getGastosEnvio());
            assertEquals(2, articulo.getTiempoPreparacion());
        }


        @Test
        void testAnadirArticuloDuplicadoLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> {
                tienda.anadirArticulo("A001", "Laptop Pro 16", 1499.99, 15.00, 120);
            });
        }


        @Test
        void testBuscarArticuloExistente() {
            Articulo articulo = tienda.buscarArticulo("A001");
            assertNotNull(articulo);
            assertEquals("Laptop Pro 16", articulo.getDescripcion());
        }


        @Test
        void testBuscarArticuloInexistente() {
            Articulo articulo = tienda.buscarArticulo("AXXX");
            assertNull(articulo);
        }


        @Test
        void testMostrarArticulos() {
            List<Articulo> articulos = tienda.mostrarArticulos();
            assertFalse(articulos.isEmpty());
            assertTrue(articulos.size() >= 5); // según cargarDatosDePrueba()
        }
    }

