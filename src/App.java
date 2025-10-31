
import model.TiendaOnline;
import view.Vista;
import controller.Controlador;

/**
 * Clase principal dónde se une el modelo la vista y el controlador.
 */
public class App {
    public static void main(String[] args) throws Exception {

        TiendaOnline modelo = new TiendaOnline(); // sin pasar DAOs

        //Se crea la vista
        Vista vista = new Vista();

        //Se crea el controlador pasandole la vista y el modelo
        Controlador controlador = new Controlador(vista, modelo);

        //Se manda a vista el controlador para que puedan interactuar
        vista.setControlador(controlador);

        //Se inicia la aplicación meidiante el controlador
        controlador.iniciar();
    }

}

