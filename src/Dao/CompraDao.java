package Dao;

import Configuracion.conexion;

import model.Compra;
import model.CompraDetalle;
import model.Deposito;
import model.Producto;
import model.Proveedor;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class CompraDao {

    // =========================================================
    // DAOS AUXILIARES
    // =========================================================

    private final ProductoDao productoDao;
    private final ProveedorDao proveedorDao;
    private final DepositoDao depositoDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public CompraDao() {

        this.productoDao =
                new ProductoDao();

        this.proveedorDao =
                new ProveedorDao();

        this.depositoDao =
                new DepositoDao();
    }


    // =========================================================
    // SQL BASE CABECERA
    // =========================================================

    private static final String SQL_BASE_CABECERA =
            "SELECT "
            + "c.id_compra, "
            + "c.id_proveedor, "
            + "c.id_usuario, "
            + "c.id_deposito, "
            + "c.numero_comprobante, "
            + "c.origen_carga, "
            + "c.fecha, "
            + "c.subtotal, "
            + "c.descuento, "
            + "c.total, "
            + "c.estado, "
            + "c.observaciones, "
            + "u.nombre_completo AS usuario_nombre, "
            + "u.username AS usuario_username, "
            + "u.email AS usuario_email, "
            + "u.estado AS usuario_estado "
            + "FROM compra c "
            + "INNER JOIN usuario u "
            + "ON u.id_usuario = c.id_usuario ";


    // =========================================================
    // GUARDAR COMPRA
    // =========================================================

    public long guardar(Compra compra)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            return guardar(
                    compra,
                    cn
            );
        }
    }


    // =========================================================
    // GUARDAR COMPRA - CONEXIÓN EXTERNA
    // =========================================================

    public long guardar(
            Compra compra,
            Connection cn)
            throws SQLException {

        String sql =
                "INSERT INTO compra ("
                + "id_proveedor, "
                + "id_usuario, "
                + "id_deposito, "
                + "numero_comprobante, "
                + "origen_carga, "
                + "fecha, "
                + "subtotal, "
                + "descuento, "
                + "total, "
                + "estado, "
                + "observaciones"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try (PreparedStatement ps =
                     cn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            ps.setInt(
                    1,
                    compra.getProveedor()
                            .getIdProveedor()
            );

            ps.setInt(
                    2,
                    compra.getUsuario()
                            .getIdUsuario()
            );

            ps.setInt(
                    3,
                    compra.getDeposito()
                            .getIdDeposito()
            );

            ps.setString(
                    4,
                    compra.getNumeroComprobante()
            );

            ps.setString(
                    5,
                    compra.getOrigenCarga()
            );


            if (compra.getFecha() != null) {

                ps.setTimestamp(
                        6,
                        Timestamp.valueOf(
                                compra.getFecha()
                        )
                );

            } else {

                ps.setTimestamp(
                        6,
                        new Timestamp(
                                System.currentTimeMillis()
                        )
                );
            }


            ps.setBigDecimal(
                    7,
                    compra.getSubtotal()
            );

            ps.setBigDecimal(
                    8,
                    compra.getDescuento()
            );

            ps.setBigDecimal(
                    9,
                    compra.getTotal()
            );

            ps.setString(
                    10,
                    compra.getEstado()
            );

            ps.setString(
                    11,
                    compra.getObservaciones()
            );


            int filas =
                    ps.executeUpdate();


            if (filas == 0) {

                return 0;
            }


            try (ResultSet rs =
                         ps.getGeneratedKeys()) {

                if (rs.next()) {

                    long idCompra =
                            rs.getLong(1);

                    compra.setIdCompra(
                            idCompra
                    );

                    return idCompra;
                }
            }
        }

        return 0;
    }


    // =========================================================
    // ACTUALIZAR CABECERA
    //
    // Pensado para una compra BORRADOR.
    // La regla de si se puede editar o no pertenece al Service.
    // =========================================================

    public boolean actualizar(
            Compra compra)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            return actualizar(
                    compra,
                    cn
            );
        }
    }


    public boolean actualizar(
            Compra compra,
            Connection cn)
            throws SQLException {

        String sql =
                "UPDATE compra SET "
                + "id_proveedor = ?, "
                + "id_deposito = ?, "
                + "numero_comprobante = ?, "
                + "origen_carga = ?, "
                + "fecha = ?, "
                + "subtotal = ?, "
                + "descuento = ?, "
                + "total = ?, "
                + "observaciones = ? "
                + "WHERE id_compra = ?";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    compra.getProveedor()
                            .getIdProveedor()
            );

            ps.setInt(
                    2,
                    compra.getDeposito()
                            .getIdDeposito()
            );

            ps.setString(
                    3,
                    compra.getNumeroComprobante()
            );

            ps.setString(
                    4,
                    compra.getOrigenCarga()
            );

            ps.setTimestamp(
                    5,
                    Timestamp.valueOf(
                            compra.getFecha()
                    )
            );

            ps.setBigDecimal(
                    6,
                    compra.getSubtotal()
            );

            ps.setBigDecimal(
                    7,
                    compra.getDescuento()
            );

            ps.setBigDecimal(
                    8,
                    compra.getTotal()
            );

            ps.setString(
                    9,
                    compra.getObservaciones()
            );

            ps.setLong(
                    10,
                    compra.getIdCompra()
            );


            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // GUARDAR DETALLE
    // =========================================================

    public long guardarDetalle(
            CompraDetalle detalle)
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
        CompraDetalle detalle,
        Connection cn)
        throws SQLException {

    String sql =
            "INSERT INTO compra_detalle ("
            + "id_compra, "
            + "id_producto, "
            + "unidad_compra, "
            + "factor_conversion, "
            + "cantidad, "
            + "cantidad_stock, "
            + "costo_unitario, "
            + "subtotal"
            + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


    try (PreparedStatement ps =
                 cn.prepareStatement(
                         sql,
                         Statement.RETURN_GENERATED_KEYS
                 )) {

        // =====================================================
        // COMPRA
        // =====================================================

        ps.setLong(
                1,
                detalle.getIdCompra()
        );


        // =====================================================
        // PRODUCTO
        // =====================================================

        ps.setInt(
                2,
                detalle.getProducto()
                        .getIdProducto()
        );


        // =====================================================
        // PRESENTACIÓN UTILIZADA EN ESTA COMPRA
        // =====================================================

        ps.setString(
                3,
                detalle.getUnidadCompra()
        );


        // =====================================================
        // FACTOR UTILIZADO EN ESTA COMPRA
        //
        // Ejemplo:
        // 1 CAJA = 20 UN
        // factor = 20
        // =====================================================

        ps.setBigDecimal(
                4,
                detalle.getFactorConversion()
        );


        // =====================================================
        // CANTIDAD COMPRADA
        //
        // Ejemplo:
        // 2 CAJAS
        // cantidad = 2
        // =====================================================

        ps.setBigDecimal(
                5,
                detalle.getCantidad()
        );


        // =====================================================
        // CANTIDAD QUE INGRESA AL STOCK
        //
        // Ejemplo:
        // 2 CAJAS x 20 = 40 UN
        // =====================================================

        ps.setBigDecimal(
                6,
                detalle.getCantidadStock()
        );


        // =====================================================
        // COSTO POR PRESENTACIÓN
        // =====================================================

        ps.setBigDecimal(
                7,
                detalle.getCostoUnitario()
        );


        // =====================================================
        // SUBTOTAL
        // =====================================================

        ps.setBigDecimal(
                8,
                detalle.getSubtotal()
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
    // GUARDAR LISTA DE DETALLES
    // =========================================================

    public void guardarDetalles(
            long idCompra,
            List<CompraDetalle> detalles,
            Connection cn)
            throws SQLException {

        if (detalles == null) {
            return;
        }


        for (CompraDetalle detalle
                : detalles) {

            detalle.setIdCompra(
                    idCompra
            );

            guardarDetalle(
                    detalle,
                    cn
            );
        }
    }


    // =========================================================
    // ACTUALIZAR DETALLE
    // =========================================================

    public boolean actualizarDetalle(
            CompraDetalle detalle)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            return actualizarDetalle(
                    detalle,
                    cn
            );
        }
    }


   public boolean actualizarDetalle(
        CompraDetalle detalle,
        Connection cn)
        throws SQLException {

    String sql =
            "UPDATE compra_detalle SET "
            + "id_producto = ?, "
            + "unidad_compra = ?, "
            + "factor_conversion = ?, "
            + "cantidad = ?, "
            + "cantidad_stock = ?, "
            + "costo_unitario = ?, "
            + "subtotal = ? "
            + "WHERE id_detalle = ?";


    try (PreparedStatement ps =
                 cn.prepareStatement(sql)) {

        // =====================================================
        // PRODUCTO
        // =====================================================

        ps.setInt(
                1,
                detalle.getProducto()
                        .getIdProducto()
        );


        // =====================================================
        // UNIDAD DE COMPRA
        // =====================================================

        ps.setString(
                2,
                detalle.getUnidadCompra()
        );


        // =====================================================
        // FACTOR DE ESTA COMPRA
        // =====================================================

        ps.setBigDecimal(
                3,
                detalle.getFactorConversion()
        );


        // =====================================================
        // CANTIDAD COMPRADA
        // =====================================================

        ps.setBigDecimal(
                4,
                detalle.getCantidad()
        );


        // =====================================================
        // CANTIDAD QUE INGRESA AL STOCK
        // =====================================================

        ps.setBigDecimal(
                5,
                detalle.getCantidadStock()
        );


        // =====================================================
        // COSTO UNITARIO
        // =====================================================

        ps.setBigDecimal(
                6,
                detalle.getCostoUnitario()
        );


        // =====================================================
        // SUBTOTAL
        // =====================================================

        ps.setBigDecimal(
                7,
                detalle.getSubtotal()
        );


        // =====================================================
        // ID DETALLE
        // =====================================================

        ps.setLong(
                8,
                detalle.getIdDetalle()
        );


        return ps.executeUpdate() > 0;
    }
}


    // =========================================================
    // ELIMINAR DETALLE
    //
    // El detalle de una compra BORRADOR sí puede eliminarse.
    // No significa eliminar una compra histórica confirmada.
    // Esa regla la controla CompraService.
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


    public boolean eliminarDetalle(
            long idDetalle,
            Connection cn)
            throws SQLException {

        String sql =
                "DELETE FROM compra_detalle "
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
    // BUSCAR COMPRA POR ID
    // =========================================================

    public Compra buscarPorId(
            long idCompra)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            Compra compra =
                    buscarPorId(
                            idCompra,
                            cn
                    );

            if (compra != null) {

                compra.setDetalles(
                        listarDetalles(
                                idCompra,
                                cn
                        )
                );
            }

            return compra;
        }
    }


    // =========================================================
    // BUSCAR POR ID - CONEXIÓN EXTERNA
    //
    // No carga detalles automáticamente para poder controlar
    // mejor las consultas dentro de una transacción.
    // =========================================================

    public Compra buscarPorId(
            long idCompra,
            Connection cn)
            throws SQLException {

        String sql =
                SQL_BASE_CABECERA
                + "WHERE c.id_compra = ?";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idCompra
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return mapearCompra(
                            rs,
                            cn
                    );
                }
            }
        }

        return null;
    }


    // =========================================================
    // BUSCAR POR ID PARA ACTUALIZAR
    //
    // Este método será fundamental en confirmarCompra().
    //
    // FOR UPDATE bloquea esa compra mientras dura
    // la transacción.
    // =========================================================

    public Compra buscarPorIdParaActualizar(
            long idCompra,
            Connection cn)
            throws SQLException {

        String sql =
                SQL_BASE_CABECERA
                + "WHERE c.id_compra = ? "
                + "FOR UPDATE";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idCompra
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    Compra compra =
                            mapearCompra(
                                    rs,
                                    cn
                            );

                    compra.setDetalles(
                            listarDetalles(
                                    idCompra,
                                    cn
                            )
                    );

                    return compra;
                }
            }
        }

        return null;
    }


    // =========================================================
    // LISTAR DETALLES
    // =========================================================

    public List<CompraDetalle> listarDetalles(
            long idCompra)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            return listarDetalles(
                    idCompra,
                    cn
            );
        }
    }


   public List<CompraDetalle> listarDetalles(
        long idCompra,
        Connection cn)
        throws SQLException {

    List<CompraDetalle> lista =
            new ArrayList<>();


    String sql =
            "SELECT "
            + "id_detalle, "
            + "id_compra, "
            + "id_producto, "
            + "unidad_compra, "
            + "factor_conversion, "
            + "cantidad, "
            + "cantidad_stock, "
            + "costo_unitario, "
            + "subtotal "
            + "FROM compra_detalle "
            + "WHERE id_compra = ? "
            + "ORDER BY id_detalle ASC";


    try (PreparedStatement ps =
                 cn.prepareStatement(sql)) {

        ps.setLong(
                1,
                idCompra
        );


        try (ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                lista.add(
                        mapearDetalle(
                                rs
                        )
                );
            }
        }
    }


    return lista;
}

    // =========================================================
    // LISTAR TODAS
    // =========================================================

    public List<Compra> listarTodos()
            throws SQLException {

        List<Compra> lista =
                new ArrayList<>();


        String sql =
                SQL_BASE_CABECERA
                + "ORDER BY c.fecha DESC, "
                + "c.id_compra DESC";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {


            while (rs.next()) {

                lista.add(
                        mapearCompra(
                                rs,
                                cn
                        )
                );
            }
        }

        return lista;
    }


    // =========================================================
    // LISTAR POR ESTADO
    // =========================================================

    public List<Compra> listarPorEstado(
            String estado)
            throws SQLException {

        List<Compra> lista =
                new ArrayList<>();


        String sql =
                SQL_BASE_CABECERA
                + "WHERE c.estado = ? "
                + "ORDER BY c.fecha DESC, "
                + "c.id_compra DESC";


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

                    lista.add(
                            mapearCompra(
                                    rs,
                                    cn
                            )
                    );
                }
            }
        }

        return lista;
    }


    // =========================================================
    // LISTAR POR PROVEEDOR
    // =========================================================

    public List<Compra> listarPorProveedor(
            int idProveedor)
            throws SQLException {

        List<Compra> lista =
                new ArrayList<>();


        String sql =
                SQL_BASE_CABECERA
                + "WHERE c.id_proveedor = ? "
                + "ORDER BY c.fecha DESC, "
                + "c.id_compra DESC";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idProveedor
            );


            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            mapearCompra(
                                    rs,
                                    cn
                            )
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
            long idCompra,
            String estado)
            throws SQLException {

        try (Connection cn =
                     conexion.getConexion()) {

            return cambiarEstado(
                    idCompra,
                    estado,
                    cn
            );
        }
    }


    public boolean cambiarEstado(
            long idCompra,
            String estado,
            Connection cn)
            throws SQLException {

        String sql =
                "UPDATE compra "
                + "SET estado = ? "
                + "WHERE id_compra = ?";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    estado
            );

            ps.setLong(
                    2,
                    idCompra
            );


            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // CAMBIAR ESTADO SI COINCIDE
    //
    // Evita confirmar dos veces la misma compra.
    // =========================================================

    public boolean cambiarEstadoSiCoincide(
            long idCompra,
            String estadoActual,
            String estadoNuevo,
            Connection cn)
            throws SQLException {

        String sql =
                "UPDATE compra "
                + "SET estado = ? "
                + "WHERE id_compra = ? "
                + "AND estado = ?";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    estadoNuevo
            );

            ps.setLong(
                    2,
                    idCompra
            );

            ps.setString(
                    3,
                    estadoActual
            );


            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // ACTUALIZAR TOTALES
    // =========================================================

    public boolean actualizarTotales(
            long idCompra,
            java.math.BigDecimal subtotal,
            java.math.BigDecimal descuento,
            java.math.BigDecimal total,
            Connection cn)
            throws SQLException {

        String sql =
                "UPDATE compra SET "
                + "subtotal = ?, "
                + "descuento = ?, "
                + "total = ? "
                + "WHERE id_compra = ?";


        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setBigDecimal(
                    1,
                    subtotal
            );

            ps.setBigDecimal(
                    2,
                    descuento
            );

            ps.setBigDecimal(
                    3,
                    total
            );

            ps.setLong(
                    4,
                    idCompra
            );


            return ps.executeUpdate() > 0;
        }
    }


    // =========================================================
    // EXISTE COMPRA
    // =========================================================

    public boolean existe(
            long idCompra)
            throws SQLException {

        String sql =
                "SELECT 1 "
                + "FROM compra "
                + "WHERE id_compra = ? "
                + "LIMIT 1";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idCompra
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
            long idCompra)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) "
                + "FROM compra_detalle "
                + "WHERE id_compra = ?";


        try (Connection cn =
                     conexion.getConexion();

             PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idCompra
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
    // MAPEAR COMPRA
    // =========================================================

    private Compra mapearCompra(
            ResultSet rs,
            Connection cn)
            throws SQLException {

        Compra compra =
                new Compra();


        compra.setIdCompra(
                rs.getLong(
                        "id_compra"
                )
        );


        // -----------------------------------------------------
        // PROVEEDOR
        // -----------------------------------------------------

        Proveedor proveedor =
                proveedorDao.buscarPorId(
                        rs.getInt(
                                "id_proveedor"
                        ),
                        cn
                );

        compra.setProveedor(
                proveedor
        );


        // -----------------------------------------------------
        // USUARIO
        // -----------------------------------------------------

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

        compra.setUsuario(
                usuario
        );


        // -----------------------------------------------------
        // DEPÓSITO
        // -----------------------------------------------------

        Deposito deposito =
                depositoDao.buscarPorId(
                        rs.getInt(
                                "id_deposito"
                        )
                );

        compra.setDeposito(
                deposito
        );


        // -----------------------------------------------------
        // DATOS DE COMPRA
        // -----------------------------------------------------

        compra.setNumeroComprobante(
                rs.getString(
                        "numero_comprobante"
                )
        );

        compra.setOrigenCarga(
                rs.getString(
                        "origen_carga"
                )
        );


        Timestamp fecha =
                rs.getTimestamp(
                        "fecha"
                );

        if (fecha != null) {

            compra.setFecha(
                    fecha.toLocalDateTime()
            );
        }


        compra.setSubtotal(
                rs.getBigDecimal(
                        "subtotal"
                )
        );

        compra.setDescuento(
                rs.getBigDecimal(
                        "descuento"
                )
        );

        compra.setTotal(
                rs.getBigDecimal(
                        "total"
                )
        );

        compra.setEstado(
                rs.getString(
                        "estado"
                )
        );

        compra.setObservaciones(
                rs.getString(
                        "observaciones"
                )
        );


        return compra;
    }


    // =========================================================
    // MAPEAR DETALLE
    // =========================================================

    private CompraDetalle mapearDetalle(
        ResultSet rs)
        throws SQLException {

    CompraDetalle detalle =
            new CompraDetalle();


    // =========================================================
    // ID DETALLE
    // =========================================================

    detalle.setIdDetalle(
            rs.getLong(
                    "id_detalle"
            )
    );


    // =========================================================
    // ID COMPRA
    // =========================================================

    detalle.setIdCompra(
            rs.getLong(
                    "id_compra"
            )
    );


    // =========================================================
    // PRODUCTO
    // =========================================================

    Producto producto =
            productoDao.buscarPorId(
                    rs.getInt(
                            "id_producto"
                    )
            );

    detalle.setProducto(
            producto
    );


    // =========================================================
    // UNIDAD DE COMPRA UTILIZADA
    // =========================================================

    detalle.setUnidadCompra(
            rs.getString(
                    "unidad_compra"
            )
    );


    // =========================================================
    // FACTOR DE CONVERSIÓN UTILIZADO
    // =========================================================

    detalle.setFactorConversion(
            rs.getBigDecimal(
                    "factor_conversion"
            )
    );


    // =========================================================
    // CANTIDAD COMPRADA
    // =========================================================

    detalle.setCantidad(
            rs.getBigDecimal(
                    "cantidad"
            )
    );


    // =========================================================
    // CANTIDAD QUE INGRESA AL STOCK
    // =========================================================

    detalle.setCantidadStock(
            rs.getBigDecimal(
                    "cantidad_stock"
            )
    );


    // =========================================================
    // COSTO
    // =========================================================

    detalle.setCostoUnitario(
            rs.getBigDecimal(
                    "costo_unitario"
            )
    );


    // =========================================================
    // SUBTOTAL
    // =========================================================

    detalle.setSubtotal(
            rs.getBigDecimal(
                    "subtotal"
            )
    );


    return detalle;
}
}