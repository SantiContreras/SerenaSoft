package services;

import Dao.ProductoPresentacionDao;
import model.ProductoPresentacion;
import java.sql.SQLException;
import java.util.List;

/** Servicio de lectura y validación para el selector de entradas. */
public class ProductoPresentacionService {
    private final ProductoPresentacionDao dao = new ProductoPresentacionDao();

    public List<ProductoPresentacion> listarActivasPorProducto(int idProducto) throws SQLException {
        return dao.listarActivasPorProducto(idProducto);
    }

    public ProductoPresentacion buscarPorId(int idPresentacion) throws SQLException {
        return dao.buscarPorId(idPresentacion);
    }

    public int guardar(ProductoPresentacion presentacion) throws SQLException {
        return dao.guardar(presentacion);
    }

    public boolean desactivar(int idPresentacion) throws SQLException {
        return dao.desactivar(idPresentacion);
    }
}
