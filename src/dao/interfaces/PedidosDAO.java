package dao.interfaces;

import model.Pedido;
import java.util.List;

public interface PedidosDAO {
    Pedido getPedidoporId (int id);
    List<Pedido> getTodosLosPedidos ();
    List<Pedido> getPedidosPorCliente(String emailCliente);
    List<Pedido> getPedidosPendientes();
    List<Pedido> getPedidosEnviados();

}
