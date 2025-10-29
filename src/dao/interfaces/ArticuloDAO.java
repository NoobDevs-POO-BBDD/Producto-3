package dao.interfaces;

import model.Articulo;
import java.util.List;

public interface ArticuloDAO {
    Articulo getArticuloPorld (String id);
    List<Articulo> getTodosLosArticulos ();

}

