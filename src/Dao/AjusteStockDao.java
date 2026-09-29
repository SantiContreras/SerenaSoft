package Dao;

import Configuracion.conexion;

import model.AjusteStock;
import model.AjusteStockDetalle;
import model.Deposito;
import model.Producto;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class AjusteStockDao {

    // =========================================================
    // SQL BASE CABECERA
    // =========================================================

    private static final String SQL_BASE_CABECERA =
            "SELECT "
            + "a.id_ajuste, "
            + "a.id_deposito, "
            + "a.id_usuario, "
            + "a.fecha, "
            + "a.motivo, "
            + "a.observaciones, "
            + "a.estado, "
            + "d.codigo AS deposito_codigo, "
            + "d.nombre AS deposito_nombre, "
            + "d.descripcion AS deposito_descripcion, "
            + "d.es_principal AS deposito_principal, "
            + "d.activo AS deposito_activo, "
            + "u.nombre_completo AS usuario_nombre, "
            + "u.username AS usuario_username, "
            + "u.email AS usuario_email, "
            + "u.estado AS usuario_estado "
            + "FROM ajuste_stock a "
            + "INNER JOIN deposito d "
            + "ON d.id_deposito = a.id_deposito "
            + "INNER JOIN usuario u "
            + "ON u.id_usuario = a.id_usuario ";


    // =========================================================
    // GUARDAR CABECERA
    // =========================================================

    public long guardar(AjusteStock ajuste)
            throws SQLException {

        try (Connection cn = conexion.getConexion()) {

            return guardar(
                    ajuste,
                    cn
            );
        }
    }


    // =========================================================
    // GUARDAR CABECERA - CONEXIÓN EXTERNA
    // =========================================================

    public long guardar(
            AjusteStock ajuste,
            Connection cn)
            throws SQLException {

        String sql =
                "INSERT INTO ajuste_stock "
                + "(id_deposito, id_usuario, motivo, "
                + "observaciones, estado) "
                + "VALUES (?, ?, ?, ?, ?)";


        try (PreparedStatement ps =
                     cn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setInt(
                    1,
                    ajuste.getDeposito()
                            .getIdDeposito()
            );

            ps.setInt(
                    2,
                    ajuste.getUsuario()
                            .getIdUsuario()
            );

            ps.setString(
                    3,
                    ajuste.getMotivo()
            );

            ps.setString(
                    4,
                    ajuste.getObservaciones()
            );

            ps.setString(
                    5,
                    ajuste.getEstado()
            );


            int filas =
                    ps.executeUpdate();


            if (filas == 0) {

                return 0;
            }


            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {

                    long idAjuste =
                            rs.getLong(1);

                    ajuste.setIdAjuste(
                            idAjuste
                    );

                    return idAjuste;
                }
            }
        }

        return 0;
    }


    // =========================================================
    // ACTUALIZAR CABECERA
    // =========================================================

    public boolean actualizar(
            AjusteStock ajuste)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            return actualizar(
                    ajuste,
                    cn
            );
        }
    }


    // =========================================================
    // ACTUALIZAR CABECERA - CONEXIÓN EXTERNA
    // =========================================================

    public boolean actualizar(
            AjusteStock ajuste,
            Connection cn)
            throws SQLException {

        String sql =
                "UPDATE ajuste_stock "
                + "SET id_deposito = ?, "
                + "motivo = ?, "
                + "observaciones = ? "
                + "WHERE id_ajuste = ?";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    ajuste.getDeposito()
                            .getIdDeposito()
            );

            ps.setString(
                    2,
                    ajuste.getMotivo()
            );

            ps.setString(
                    3,
                    ajuste.getObservaciones()
            );

            ps.setLong(
                    4,
                    ajuste.getIdAjuste()
            );


            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // GUARDAR DETALLE
    // =========================================================

    public long guardarDetalle(
            AjusteStockDetalle detalle)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            return guardarDetalle(
                    detalle,
                    cn
            );
        }
    }


    // =========================================================
    // GUARDAR DETALLE - CONEXIÓN EXTERNA
    // =========================================================

    public long guardarDetalle(
            AjusteStockDetalle detalle,
            Connection cn)
            throws SQLException {

        String sql =
                "INSERT INTO ajuste_stock_detalle "
                + "(id_ajuste, id_producto, "
                + "stock_sistema, stock_fisico, diferencia) "
                + "VALUES (?, ?, ?, ?, ?)";


        try (PreparedStatement ps =
                     cn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setLong(
                    1,
                    detalle.getIdAjuste()
            );

            ps.setInt(
                    2,
                    detalle.getProducto()
                            .getIdProducto()
            );

            ps.setBigDecimal(
                    3,
                    detalle.getStockSistema()
            );

            ps.setBigDecimal(
                    4,
                    detalle.getStockFisico()
            );

            ps.setBigDecimal(
                    5,
                    detalle.getDiferencia()
            );


            int filas =
                    ps.executeUpdate();


            if (filas == 0) {

                return 0;
            }


            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {

                    long idDetalle =
                            rs.getLong(1);

                    detalle.setIdDetalle(
                            idDetalle
                    );

                    return idDetalle;
                }
            }
        }

        return 0;
    }


    // =========================================================
    // GUARDAR VARIOS DETALLES
    // =========================================================

    public void guardarDetalles(
            long idAjuste,
            List<AjusteStockDetalle> detalles,
            Connection cn)
            throws SQLException {

        if (detalles == null) {

            return;
        }


        for (AjusteStockDetalle detalle
                : detalles) {

            detalle.setIdAjuste(
                    idAjuste
            );

            guardarDetalle(
                    detalle,
                    cn
            );
        }
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public AjusteStock buscarPorId(
            long idAjuste)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            return buscarPorId(
                    idAjuste,
                    cn
            );
        }
    }


    // =========================================================
    // BUSCAR POR ID - CONEXIÓN EXTERNA
    // =========================================================

    public AjusteStock buscarPorId(
            long idAjuste,
            Connection cn)
            throws SQLException {

        String sql =
                SQL_BASE_CABECERA
                + "WHERE a.id_ajuste = ?";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idAjuste
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    AjusteStock ajuste =
                            mapearAjuste(rs);

                    ajuste.setDetalles(
                            listarDetalles(
                                    idAjuste,
                                    cn
                            )
                    );

                    return ajuste;
                }
            }
        }

        return null;
    }


    // =========================================================
    // BUSCAR POR ID PARA ACTUALIZAR
    // FOR UPDATE
    //
    // IMPORTANTE:
    // Este método debe utilizarse dentro de una transacción.
    // Bloquea el ajuste mientras se está confirmando.
    // =========================================================

    public AjusteStock buscarPorIdParaActualizar(
            long idAjuste,
            Connection cn)
            throws SQLException {

        String sql =
                SQL_BASE_CABECERA
                + "WHERE a.id_ajuste = ? "
                + "FOR UPDATE";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idAjuste
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    AjusteStock ajuste =
                            mapearAjuste(rs);

                    ajuste.setDetalles(
                            listarDetalles(
                                    idAjuste,
                                    cn
                            )
                    );

                    return ajuste;
                }
            }
        }

        return null;
    }


    // =========================================================
    // LISTAR DETALLES
    // =========================================================

    public List<AjusteStockDetalle> listarDetalles(
            long idAjuste)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            return listarDetalles(
                    idAjuste,
                    cn
            );
        }
    }


    // =========================================================
    // LISTAR DETALLES - CONEXIÓN EXTERNA
    // =========================================================

    public List<AjusteStockDetalle> listarDetalles(
            long idAjuste,
            Connection cn)
            throws SQLException {

        List<AjusteStockDetalle> lista =
                new ArrayList<>();


        String sql =
                "SELECT "
                + "ad.id_detalle, "
                + "ad.id_ajuste, "
                + "ad.id_producto, "
                + "ad.stock_sistema, "
                + "ad.stock_fisico, "
                + "ad.diferencia, "
                + "p.codigo, "
                + "p.codigo_interno, "
                + "p.codigo_barra, "
                + "p.nombre, "
                + "p.descripcion, "
                + "p.ubicacion, "
                + "p.precio_compra, "
                + "p.precio_venta, "
                + "p.stock_minimo, "
                + "p.stock_maximo, "
                + "p.controla_stock, "
                + "p.permite_venta_sin_stock, "
                + "p.es_pesable, "
                + "p.permite_descuento, "
                + "p.activo "
                + "FROM ajuste_stock_detalle ad "
                + "INNER JOIN producto p "
                + "ON p.id_producto = ad.id_producto "
                + "WHERE ad.id_ajuste = ? "
                + "ORDER BY ad.id_detalle";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idAjuste
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    AjusteStockDetalle detalle =
                            mapearDetalle(rs);

                    lista.add(
                            detalle
                    );
                }
            }
        }

        return lista;
    }


    // =========================================================
    // ACTUALIZAR DETALLE
    // =========================================================

    public boolean actualizarDetalle(
            AjusteStockDetalle detalle)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            return actualizarDetalle(
                    detalle,
                    cn
            );
        }
    }


    // =========================================================
    // ACTUALIZAR DETALLE - CONEXIÓN EXTERNA
    // =========================================================

    public boolean actualizarDetalle(
            AjusteStockDetalle detalle,
            Connection cn)
            throws SQLException {

        String sql =
                "UPDATE ajuste_stock_detalle "
                + "SET stock_sistema = ?, "
                + "stock_fisico = ?, "
                + "diferencia = ? "
                + "WHERE id_detalle = ?";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setBigDecimal(
                    1,
                    detalle.getStockSistema()
            );

            ps.setBigDecimal(
                    2,
                    detalle.getStockFisico()
            );

            ps.setBigDecimal(
                    3,
                    detalle.getDiferencia()
            );

            ps.setLong(
                    4,
                    detalle.getIdDetalle()
            );


            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // ELIMINAR DETALLE
    // =========================================================

    public boolean eliminarDetalle(
            long idDetalle)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            return eliminarDetalle(
                    idDetalle,
                    cn
            );
        }
    }


    // =========================================================
    // ELIMINAR DETALLE - CONEXIÓN EXTERNA
    // =========================================================

    public boolean eliminarDetalle(
            long idDetalle,
            Connection cn)
            throws SQLException {

        String sql =
                "DELETE FROM ajuste_stock_detalle "
                + "WHERE id_detalle = ?";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idDetalle
            );


            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<AjusteStock> listarTodos()
            throws SQLException {

        List<AjusteStock> lista =
                new ArrayList<>();


        String sql =
                SQL_BASE_CABECERA
                + "ORDER BY a.fecha DESC, "
                + "a.id_ajuste DESC";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {


            while (rs.next()) {

                AjusteStock ajuste =
                        mapearAjuste(rs);

                ajuste.setDetalles(
                        listarDetalles(
                                ajuste.getIdAjuste(),
                                cn
                        )
                );

                lista.add(
                        ajuste
                );
            }
        }

        return lista;
    }


    // =========================================================
    // LISTAR POR ESTADO
    // =========================================================

    public List<AjusteStock> listarPorEstado(
            String estado)
            throws SQLException {

        List<AjusteStock> lista =
                new ArrayList<>();


        String sql =
                SQL_BASE_CABECERA
                + "WHERE a.estado = ? "
                + "ORDER BY a.fecha DESC, "
                + "a.id_ajuste DESC";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    estado
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    AjusteStock ajuste =
                            mapearAjuste(rs);

                    ajuste.setDetalles(
                            listarDetalles(
                                    ajuste.getIdAjuste(),
                                    cn
                            )
                    );

                    lista.add(
                            ajuste
                    );
                }
            }
        }

        return lista;
    }


    // =========================================================
    // LISTAR POR DEPÓSITO
    // =========================================================

    public List<AjusteStock> listarPorDeposito(
            int idDeposito)
            throws SQLException {

        List<AjusteStock> lista =
                new ArrayList<>();


        String sql =
                SQL_BASE_CABECERA
                + "WHERE a.id_deposito = ? "
                + "ORDER BY a.fecha DESC, "
                + "a.id_ajuste DESC";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idDeposito
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    AjusteStock ajuste =
                            mapearAjuste(rs);

                    ajuste.setDetalles(
                            listarDetalles(
                                    ajuste.getIdAjuste(),
                                    cn
                            )
                    );

                    lista.add(
                            ajuste
                    );
                }
            }
        }

        return lista;
    }


    // =========================================================
    // CAMBIAR ESTADO
    // =========================================================

    public boolean cambiarEstado(
            long idAjuste,
            String estado)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            return cambiarEstado(
                    idAjuste,
                    estado,
                    cn
            );
        }
    }


    // =========================================================
    // CAMBIAR ESTADO - CONEXIÓN EXTERNA
    // =========================================================

    public boolean cambiarEstado(
            long idAjuste,
            String estado,
            Connection cn)
            throws SQLException {

        String sql =
                "UPDATE ajuste_stock "
                + "SET estado = ? "
                + "WHERE id_ajuste = ?";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    estado
            );

            ps.setLong(
                    2,
                    idAjuste
            );


            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // CAMBIAR ESTADO SOLO SI COINCIDE
    //
    // Ejemplo:
    // BORRADOR -> CONFIRMADO
    //
    // Solamente cambia si todavía sigue en BORRADOR.
    // =========================================================

    public boolean cambiarEstadoSiCoincide(
            long idAjuste,
            String estadoActual,
            String nuevoEstado,
            Connection cn)
            throws SQLException {

        String sql =
                "UPDATE ajuste_stock "
                + "SET estado = ? "
                + "WHERE id_ajuste = ? "
                + "AND estado = ?";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    nuevoEstado
            );

            ps.setLong(
                    2,
                    idAjuste
            );

            ps.setString(
                    3,
                    estadoActual
            );


            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // EXISTE AJUSTE
    // =========================================================

    public boolean existe(
            long idAjuste)
            throws SQLException {

        String sql =
                "SELECT 1 "
                + "FROM ajuste_stock "
                + "WHERE id_ajuste = ?";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idAjuste
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                return rs.next();
            }
        }
    }


    // =========================================================
    // CONTAR DETALLES
    // =========================================================

    public int contarDetalles(
            long idAjuste)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) "
                + "FROM ajuste_stock_detalle "
                + "WHERE id_ajuste = ?";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idAjuste
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }


    // =========================================================
    // MAPEAR CABECERA
    // =========================================================

    private AjusteStock mapearAjuste(
            ResultSet rs)
            throws SQLException {

        AjusteStock ajuste =
                new AjusteStock();


        ajuste.setIdAjuste(
                rs.getLong("id_ajuste")
        );


        // =====================================================
        // DEPÓSITO
        // =====================================================

        Deposito deposito =
                new Deposito();

        deposito.setIdDeposito(
                rs.getInt("id_deposito")
        );

        deposito.setCodigo(
                rs.getString(
                        "deposito_codigo"
                )
        );

        deposito.setNombre(
                rs.getString(
                        "deposito_nombre"
                )
        );

        deposito.setDescripcion(
                rs.getString(
                        "deposito_descripcion"
                )
        );

        deposito.setEsPrincipal(
                rs.getBoolean(
                        "deposito_principal"
                )
        );

        deposito.setActivo(
                rs.getBoolean(
                        "deposito_activo"
                )
        );


        ajuste.setDeposito(
                deposito
        );


        // =====================================================
        // USUARIO
        //
        // CORREGIDO SEGÚN TU MODELO REAL Usuario.java
        // =====================================================

        Usuario usuario =
                new Usuario();

        usuario.setIdUsuario(
                rs.getInt(
                        "id_usuario"
                )
        );

        usuario.setNombreCompleto(
                rs.getString(
                        "usuario_nombre"
                )
        );

        usuario.setUsername(
                rs.getString(
                        "usuario_username"
                )
        );

        usuario.setEmail(
                rs.getString(
                        "usuario_email"
                )
        );

        usuario.setEstado(
                rs.getString(
                        "usuario_estado"
                )
        );


        ajuste.setUsuario(
                usuario
        );


        // =====================================================
        // FECHA
        // =====================================================

        Timestamp timestamp =
                rs.getTimestamp(
                        "fecha"
                );

        if (timestamp != null) {

            ajuste.setFecha(
                    timestamp.toLocalDateTime()
            );
        }


        // =====================================================
        // DATOS DEL AJUSTE
        // =====================================================

        ajuste.setMotivo(
                rs.getString(
                        "motivo"
                )
        );

        ajuste.setObservaciones(
                rs.getString(
                        "observaciones"
                )
        );

        ajuste.setEstado(
                rs.getString(
                        "estado"
                )
        );


        return ajuste;
    }


    // =========================================================
    // MAPEAR DETALLE
    // =========================================================

    private AjusteStockDetalle mapearDetalle(
            ResultSet rs)
            throws SQLException {

        AjusteStockDetalle detalle =
                new AjusteStockDetalle();


        detalle.setIdDetalle(
                rs.getLong(
                        "id_detalle"
                )
        );

        detalle.setIdAjuste(
                rs.getLong(
                        "id_ajuste"
                )
        );


        // =====================================================
        // PRODUCTO
        // =====================================================

        Producto producto =
                new Producto();

        producto.setIdProducto(
                rs.getInt(
                        "id_producto"
                )
        );

        producto.setCodigo(
                rs.getString(
                        "codigo"
                )
        );

        producto.setCodigoInterno(
                rs.getString(
                        "codigo_interno"
                )
        );

        producto.setCodigoBarra(
                rs.getString(
                        "codigo_barra"
                )
        );

        producto.setNombre(
                rs.getString(
                        "nombre"
                )
        );

        producto.setDescripcion(
                rs.getString(
                        "descripcion"
                )
        );

        producto.setUbicacion(
                rs.getString(
                        "ubicacion"
                )
        );

        producto.setPrecioCompra(
                rs.getBigDecimal(
                        "precio_compra"
                )
        );

        producto.setPrecioVenta(
                rs.getBigDecimal(
                        "precio_venta"
                )
        );

        producto.setStockMinimo(
                rs.getBigDecimal(
                        "stock_minimo"
                )
        );

        producto.setStockMaximo(
                rs.getBigDecimal(
                        "stock_maximo"
                )
        );

        producto.setControlaStock(
                rs.getBoolean(
                        "controla_stock"
                )
        );

        producto.setPermiteVentaSinStock(
                rs.getBoolean(
                        "permite_venta_sin_stock"
                )
        );

        producto.setPesable(
                rs.getBoolean(
                        "es_pesable"
                )
        );

        producto.setPermiteDescuento(
                rs.getBoolean(
                        "permite_descuento"
                )
        );

        producto.setActivo(
                rs.getBoolean(
                        "activo"
                )
        );


        detalle.setProducto(
                producto
        );


        // =====================================================
        // STOCK DEL DETALLE
        // =====================================================

        detalle.setStockSistema(
                rs.getBigDecimal(
                        "stock_sistema"
                )
        );

        detalle.setStockFisico(
                rs.getBigDecimal(
                        "stock_fisico"
                )
        );

        detalle.setDiferencia(
                rs.getBigDecimal(
                        "diferencia"
                )
        );


        return detalle;
    }
}