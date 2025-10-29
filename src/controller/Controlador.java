package controller;

import model.Articulo;
import model.Cliente;
import model.Pedido;
import model.TiendaOnline;
import view.Vista;
import java.util.List;

public class Controlador {
    private Vista vista;
    private TiendaOnline modelo;

    //Constructor
    public Controlador(Vista vista, TiendaOnline modelo){
        this.vista = vista;
        this.modelo = modelo;
    }

    // Gestión de clientes.
    public void solicitarAnadirCliente(String email, String nombre, String domicilio, String nif, Boolean premium){
        try {
            modelo.anadirCliente(email, nombre, domicilio, nif,premium);
            vista.clienteAnadido();
        } catch (IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void solicitarMostrarClientes(){
        List<Cliente> listaDeClientes = modelo.mostrarClientes();
        vista.mostrarListaClientes(listaDeClientes);
    }

    public void solicitarMostrarClientesEstandar(){
        List<Cliente> listaDeClientesEstandar = modelo.mostrarClientesEstandar();
        vista.mostarListaClientesEstandar(listaDeClientesEstandar);
    }

    public void solicitarMostrarClientesPremium(){
        List<Cliente> listaDeClientesPremium = modelo.mostrarClientesPremium();
        vista.mostrarListaClientesPremium(listaDeClientesPremium);
    }

    //Gestión de artículos

    public void solicitarAnadirArticulo(String codigo, String descripcion, double precioVenta, double gastosEnvio, int tiempoPreparacion){
        try {
            modelo.anadirArticulo(codigo,descripcion,precioVenta,gastosEnvio,tiempoPreparacion);
            vista.articuloAnadido();
        } catch (IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    public void solicitarMostrarArticulos(){
        List<Articulo> listaArticulo = modelo.mostrarArticulos();
        vista.mostrarListaArticulos(listaArticulo);
    }

    public void solicitarBuscarArticulo(String codigoBuscar) {
        Articulo articuloBuscado = modelo.buscarArticulo(codigoBuscar);
        vista.articuloBuscado(articuloBuscado);
    }

    //Gestión de pedidos.

    public void solicitarAnadirPedido(String numeroPedido,String cliente,String articulo,int cantidad){
        try {
            modelo.anadirPedido(numeroPedido,cliente,articulo,cantidad);
            vista.pedidoAnadido();
        } catch (IllegalArgumentException e) {
            String error = e.getMessage();
            if (error != null && error.startsWith("No existe el cliente con email")){
                vista.mostrarError(error);
                vista.solicitarDatosNuevoCliente(cliente);
                Cliente c = modelo.buscarClientePorEmail(cliente);

                if (c !=null ){
                    solicitarAnadirPedido(numeroPedido, cliente, articulo, cantidad);
                }else {
                    vista.mostrarError("No se pudo registrar el cliente. Pedido cancelado.");
                }
            }else{
                vista.mostrarError(e.getMessage());
            }
        }
    }

    public void solicitarEliminarPedido(String numeroPedidoBorrar){
        boolean verdadero = modelo.eliminarPedido(numeroPedidoBorrar);
        if(verdadero){
            List<Pedido> listaActualizada = modelo.mostrarPedidosPendientes();
            vista.mostrarListaPedidosPendientes(listaActualizada);
            vista.pedidoEliminado();
        } else {
            String message = "No se pudo eliminar el pedido " + numeroPedidoBorrar + ". Es posible que ya esté enviado o se ha eliminado con anterioridad.";
            vista.mostrarError(message);
        }
    }

    public void solicitarMostrarPedidosPendientes() {
        List<Pedido> pendientes = modelo.mostrarPedidosPendientes();
        vista.mostrarListaPedidosPendientes(pendientes);
    }

    public void solicitarMostrarPedidosPendientesEmail(String emailCliente){
        List<Pedido> pendientesEmail = modelo.mostrarPedidosPendientes(emailCliente);
        vista.mostrarListaPedidosPendientes(pendientesEmail);
    }

    public void solicitarMostrarPedidosEnviados() {
        List<Pedido> enviados = modelo.mostrarPedidosEnviados();
        vista.mostrarListaPedidosEnviados(enviados);
    }
    public void solicitarMostrarPedidosEnviadosEmail(String emailCliente){
        List<Pedido> enviadosEmail = modelo.mostrarPedidosEnviados(emailCliente);
        vista.mostrarListaPedidosEnviados(enviadosEmail);
    }

    /**
     * Se inicia la aplicación llamando a la función principal de vista
     */
    public void iniciar(){
        vista.menu();
    }

}