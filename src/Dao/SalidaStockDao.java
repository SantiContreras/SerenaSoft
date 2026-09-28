package Dao;

import Configuracion.conexion;

import model.Deposito;
import model.Producto;
import model.SalidaStock;
import model.SalidaStockDetalle;
import model.Usuario;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class SalidaStockDao {

    // =========================================================
    // SQL BASE PARA CONSULTAR CABECERA
    // =========================================================
    private static final String SQL_BASE_CABECERA
            = "SELECT "
            + "ss.id_salida, "
            + "ss.fecha, "
            + "ss.motivo, "
            + "ss.destino, "
            + "ss.observaciones, "
            + "ss.estado, "
            + "d.id_deposito, "
            + "d.codigo AS deposito_codigo, "
            + "d.nombre AS deposito_nombre, "
            + "d.descripcion AS deposito_descripcion, "
            + "d.es_principal AS deposito_principal, "
            + "d.activo AS deposito_activo, "
            + "u.id_usuario, "
            + "u.nombre_completo AS usuario_nombre, "
            + "u.username AS usuario_username, "
            + "u.email AS usuario_email, "
            + "u.estado AS usuario_estado "
            + "FROM salida_stock ss "
            + "INNER JOIN deposito d "
            + "ON ss.id_deposito = d.id_deposito "
            + "INNER JOIN usuario u "
            + "ON ss.id_usuario = u.id_usuario ";

    // =========================================================
    // GUARDAR CABECERA
    // =========================================================
    /**
     * Guarda una salida utilizando una conexión propia.
     *
     * Este método puede utilizarse para crear normalmente una salida en estado
     * BORRADOR.
     */
    public boolean guardar(SalidaStock salida)
            throws SQLException {

        try (Connection cn = conexion.getConexion()) {

            return guardar(salida, cn);
        }
    }

    /**
     * Guarda la cabecera utilizando una Connection existente.
     *
     * Este método será importante cuando el Service necesite manejar una única
     * transacción.
     */
    public boolean guardar(
            SalidaStock salida,
            Connection cn)
            throws SQLException {

        String sql
                = "INSERT INTO salida_stock "
                + "(id_deposito, "
                + "id_usuario, "
                + "motivo, "
                + "destino, "
                + "observaciones, "
                + "estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps
                = cn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(
                    1,
                    salida.getDeposito().getIdDeposito()
            );

            ps.setInt(
                    2,
                    salida.getUsuario().getIdUsuario()
            );

            ps.setString(
                    3,
                    salida.getMotivo()
            );

            ps.setString(
                    4,
                    salida.getDestino()
            );

            ps.setString(
                    5,
                    salida.getObservaciones()
            );

            ps.setString(
                    6,
                    salida.getEstado()
            );

            int filas = ps.executeUpdate();

            if (filas == 0) {
                return false;
            }

            try (ResultSet rs
                    = ps.getGeneratedKeys()) {

                if (rs.next()) {

                    salida.setIdSalida(
                            rs.getLong(1)
                    );
                }
            }

            return true;
        }
    }

    // =========================================================
    // ACTUALIZAR CABECERA
    // =========================================================
    /**
     * Actualiza los datos editables de una salida.
     *
     * La regla de negocio de permitir editar solamente BORRADORES estará en el
     * Service.
     */
    public boolean actualizar(
            SalidaStock salida)
            throws SQLException {

        try (Connection cn = conexion.getConexion()) {

            return actualizar(salida, cn);
        }
    }

    public boolean actualizar(
            SalidaStock salida,
            Connection cn)
            throws SQLException {

        String sql
                = "UPDATE salida_stock "
                + "SET id_deposito = ?, "
                + "motivo = ?, "
                + "destino = ?, "
                + "observaciones = ? "
                + "WHERE id_salida = ?";

        try (PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    salida.getDeposito().getIdDeposito()
            );

            ps.setString(
                    2,
                    salida.getMotivo()
            );

            ps.setString(
                    3,
                    salida.getDestino()
            );

            ps.setString(
                    4,
                    salida.getObservaciones()
            );

            ps.setLong(
                    5,
                    salida.getIdSalida()
            );

            return ps.executeUpdate() > 0;
        }
    }

    // =========================================================
    // GUARDAR DETALLE
    // =========================================================
    public boolean guardarDetalle(
            SalidaStockDetalle detalle)
            throws SQLException {

        try (Connection cn = conexion.getConexion()) {

            return guardarDetalle(
                    detalle,
                    cn
            );
        }
    }

    /**
     * Inserta un detalle utilizando una Connection existente.
     */
    public boolean guardarDetalle(
            SalidaStockDetalle detalle,
            Connection cn)
            throws SQLException {

        String sql
                = "INSERT INTO salida_stock_detalle "
                + "(id_salida, "
                + "id_producto, "
                + "cantidad) "
                + "VALUES (?, ?, ?)";

        try (PreparedStatement ps
                = cn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(
                    1,
                    detalle.getIdSalida()
            );

            ps.setInt(
                    2,
                    detalle.getProducto().getIdProducto()
            );

            ps.setBigDecimal(
                    3,
                    detalle.getCantidad()
            );

            int filas = ps.executeUpdate();

            if (filas == 0) {
                return false;
            }

            try (ResultSet rs
                    = ps.getGeneratedKeys()) {

                if (rs.next()) {

                    detalle.setIdDetalle(
                            rs.getLong(1)
                    );
                }
            }

            return true;
        }
    }

    // =========================================================
    // GUARDAR TODOS LOS DETALLES
    // =========================================================
    /**
     * Guarda todos los detalles usando la misma Connection.
     *
     * Si uno falla, lanza SQLException para que el Service pueda ejecutar
     * rollback.
     */
    public void guardarDetalles(
            long idSalida,
            List<SalidaStockDetalle> detalles,
            Connection cn)
            throws SQLException {

        if (detalles == null
                || detalles.isEmpty()) {

            return;
        }

        for (SalidaStockDetalle detalle
                : detalles) {

            detalle.setIdSalida(idSalida);

            boolean guardado
                    = guardarDetalle(
                            detalle,
                            cn
                    );

            if (!guardado) {

                throw new SQLException(
                        "No se pudo guardar "
                        + "un detalle de la salida."
                );
            }
        }
    }

    // =========================================================
    // BUSCAR POR ID
    // =========================================================
    /**
     * Recupera cabecera + detalles.
     */
    public SalidaStock buscarPorId(
            long idSalida)
            throws SQLException {

        try (Connection cn = conexion.getConexion()) {

            return buscarPorId(
                    idSalida,
                    cn
            );
        }
    }

    /**
     * Versión preparada para utilizar una Connection existente.
     */
    public SalidaStock buscarPorId(
            long idSalida,
            Connection cn)
            throws SQLException {

        String sql
                = SQL_BASE_CABECERA
                + "WHERE ss.id_salida = ?";

        SalidaStock salida = null;

        try (PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idSalida
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                if (rs.next()) {

                    salida
                            = mapearSalida(rs);
                }
            }
        }

        if (salida != null) {

            salida.setDetalles(
                    listarDetalles(
                            idSalida,
                            cn
                    )
            );
        }

        return salida;
    }

    // =========================================================
    // BUSCAR POR ID PARA ACTUALIZACIÓN
    // =========================================================
    /**
     * Lee la cabecera aplicando SELECT ... FOR UPDATE.
     *
     * Este método se utilizará durante la confirmación para evitar que dos
     * operaciones confirmen simultáneamente la misma salida.
     *
     * Debe ejecutarse dentro de una transacción.
     */
    public SalidaStock buscarPorIdParaActualizar(
            long idSalida,
            Connection cn)
            throws SQLException {

        String sql
                = SQL_BASE_CABECERA
                + "WHERE ss.id_salida = ? "
                + "FOR UPDATE";

        SalidaStock salida = null;

        try (PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idSalida
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                if (rs.next()) {

                    salida
                            = mapearSalida(rs);
                }
            }
        }

        if (salida != null) {

            salida.setDetalles(
                    listarDetalles(
                            idSalida,
                            cn
                    )
            );
        }

        return salida;
    }

    // =========================================================
    // LISTAR DETALLES
    // =========================================================
    public List<SalidaStockDetalle> listarDetalles(
            long idSalida)
            throws SQLException {

        try (Connection cn = conexion.getConexion()) {

            return listarDetalles(
                    idSalida,
                    cn
            );
        }
    }

    public List<SalidaStockDetalle> listarDetalles(
            long idSalida,
            Connection cn)
            throws SQLException {

        String sql
                = "SELECT "
                + "ssd.id_detalle, "
                + "ssd.id_salida, "
                + "ssd.cantidad, "
                + "p.id_producto, "
                + "p.codigo, "
                + "p.codigo_interno, "
                + "p.codigo_barra, "
                + "p.nombre, "
                + "p.descripcion, "
                + "p.ubicacion, "
                + "p.precio_compra, "
                + "p.precio_venta, "
                + "p.margen_porcentaje, "
                + "p.stock_minimo, "
                + "p.stock_maximo, "
                + "p.controla_stock, "
                + "p.permite_venta_sin_stock, "
                + "p.es_pesable, "
                + "p.permite_descuento, "
                + "p.activo, "
                + "p.observaciones "
                + "FROM salida_stock_detalle ssd "
                + "INNER JOIN producto p "
                + "ON ssd.id_producto = p.id_producto "
                + "WHERE ssd.id_salida = ? "
                + "ORDER BY ssd.id_detalle";

        List<SalidaStockDetalle> lista
                = new ArrayList<>();

        try (PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idSalida
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            mapearDetalle(rs)
                    );
                }
            }
        }

        return lista;
    }

    // =========================================================
    // LISTAR SALIDAS
    // =========================================================
    public List<SalidaStock> listarTodas()
            throws SQLException {

        String sql
                = SQL_BASE_CABECERA
                + "ORDER BY ss.fecha DESC, "
                + "ss.id_salida DESC";

        List<SalidaStock> lista
                = new ArrayList<>();

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql); ResultSet rs
                = ps.executeQuery()) {

            while (rs.next()) {

                SalidaStock salida
                        = mapearSalida(rs);

                salida.setDetalles(
                        listarDetalles(
                                salida.getIdSalida(),
                                cn
                        )
                );

                lista.add(salida);
            }
        }

        return lista;
    }

    // =========================================================
    // LISTAR POR ESTADO
    // =========================================================
    public List<SalidaStock> listarPorEstado(
            String estado)
            throws SQLException {

        String sql
                = SQL_BASE_CABECERA
                + "WHERE ss.estado = ? "
                + "ORDER BY ss.fecha DESC, "
                + "ss.id_salida DESC";

        List<SalidaStock> lista
                = new ArrayList<>();

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    estado
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                while (rs.next()) {

                    SalidaStock salida
                            = mapearSalida(rs);

                    salida.setDetalles(
                            listarDetalles(
                                    salida.getIdSalida(),
                                    cn
                            )
                    );

                    lista.add(salida);
                }
            }
        }

        return lista;
    }

    // =========================================================
    // LISTAR POR DEPÓSITO
    // =========================================================
    public List<SalidaStock> listarPorDeposito(
            int idDeposito)
            throws SQLException {

        String sql
                = SQL_BASE_CABECERA
                + "WHERE ss.id_deposito = ? "
                + "ORDER BY ss.fecha DESC, "
                + "ss.id_salida DESC";

        List<SalidaStock> lista
                = new ArrayList<>();

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idDeposito
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                while (rs.next()) {

                    SalidaStock salida
                            = mapearSalida(rs);

                    salida.setDetalles(
                            listarDetalles(
                                    salida.getIdSalida(),
                                    cn
                            )
                    );

                    lista.add(salida);
                }
            }
        }

        return lista;
    }

    // =========================================================
    // ACTUALIZAR CANTIDAD DE UN DETALLE
    // =========================================================
    public boolean actualizarCantidadDetalle(
            long idDetalle,
            BigDecimal cantidad)
            throws SQLException {

        String sql
                = "UPDATE salida_stock_detalle "
                + "SET cantidad = ? "
                + "WHERE id_detalle = ?";

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setBigDecimal(
                    1,
                    cantidad
            );

            ps.setLong(
                    2,
                    idDetalle
            );

            return ps.executeUpdate() > 0;
        }
    }

    // =========================================================
    // ELIMINAR DETALLE
    // =========================================================
    /**
     * Eliminar físicamente un detalle es válido mientras la salida siga siendo
     * BORRADOR.
     *
     * Esa regla se controlará en el Service.
     */
    public boolean eliminarDetalle(
            long idDetalle)
            throws SQLException {

        String sql
                = "DELETE FROM salida_stock_detalle "
                + "WHERE id_detalle = ?";

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idDetalle
            );

            return ps.executeUpdate() > 0;
        }
    }

    // =========================================================
    // CAMBIAR ESTADO
    // =========================================================
    public boolean cambiarEstado(
            long idSalida,
            String estado)
            throws SQLException {

        try (Connection cn
                = conexion.getConexion()) {

            return cambiarEstado(
                    idSalida,
                    estado,
                    cn
            );
        }
    }

    /**
     * Versión transaccional.
     *
     * No abre una conexión nueva.
     */
    public boolean cambiarEstado(
            long idSalida,
            String estado,
            Connection cn)
            throws SQLException {

        String sql
                = "UPDATE salida_stock "
                + "SET estado = ? "
                + "WHERE id_salida = ?";

        try (PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    estado
            );

            ps.setLong(
                    2,
                    idSalida
            );

            return ps.executeUpdate() > 0;
        }
    }

    // =========================================================
    // CAMBIAR ESTADO SOLO SI COINCIDE EL ESTADO ACTUAL
    // =========================================================
    /**
     * Método especialmente útil para confirmación.
     *
     * Ejemplo:
     *
     * BORRADOR -> CONFIRMADA
     *
     * Evita confirmar accidentalmente una salida que ya cambió de estado.
     */
    public boolean cambiarEstadoSiCoincide(
            long idSalida,
            String estadoActual,
            String nuevoEstado,
            Connection cn)
            throws SQLException {

        String sql
                = "UPDATE salida_stock "
                + "SET estado = ? "
                + "WHERE id_salida = ? "
                + "AND estado = ?";

        try (PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    nuevoEstado
            );

            ps.setLong(
                    2,
                    idSalida
            );

            ps.setString(
                    3,
                    estadoActual
            );

            return ps.executeUpdate() > 0;
        }
    }

    // =========================================================
    // VERIFICAR SI EXISTE
    // =========================================================
    public boolean existe(
            long idSalida)
            throws SQLException {

        String sql
                = "SELECT 1 "
                + "FROM salida_stock "
                + "WHERE id_salida = ?";

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idSalida
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

                return rs.next();
            }
        }
    }

    // =========================================================
    // CONTAR DETALLES
    // =========================================================
    public int contarDetalles(
            long idSalida)
            throws SQLException {

        String sql
                = "SELECT COUNT(*) "
                + "FROM salida_stock_detalle "
                + "WHERE id_salida = ?";

        try (Connection cn
                = conexion.getConexion(); PreparedStatement ps
                = cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    idSalida
            );

            try (ResultSet rs
                    = ps.executeQuery()) {

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
    private SalidaStock mapearSalida(
            ResultSet rs)
            throws SQLException {

        // -----------------------------------------------------
        // DEPÓSITO
        // -----------------------------------------------------
        Deposito deposito
                = new Deposito();

        deposito.setIdDeposito(
                rs.getInt("id_deposito")
        );

        deposito.setCodigo(
                rs.getString("deposito_codigo")
        );

        deposito.setNombre(
                rs.getString("deposito_nombre")
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

        // -----------------------------------------------------
        // USUARIO
        // -----------------------------------------------------
        Usuario usuario
                = new Usuario();

        usuario.setIdUsuario(
                rs.getInt("id_usuario")
        );

        usuario.setNombreCompleto(
                rs.getString("usuario_nombre")
        );

        usuario.setUsername(
                rs.getString("usuario_username")
        );

        usuario.setEmail(
                rs.getString("usuario_email")
        );

        usuario.setEstado(
                rs.getString("usuario_estado")
        );

        // -----------------------------------------------------
        // SALIDA
        // -----------------------------------------------------
        SalidaStock salida
                = new SalidaStock();

        salida.setIdSalida(
                rs.getLong("id_salida")
        );

        salida.setDeposito(
                deposito
        );

        salida.setUsuario(
                usuario
        );

        Timestamp fecha
                = rs.getTimestamp("fecha");

        if (fecha != null) {

            salida.setFecha(
                    fecha.toLocalDateTime()
            );
        }

        salida.setMotivo(
                rs.getString("motivo")
        );

        salida.setDestino(
                rs.getString("destino")
        );

        salida.setObservaciones(
                rs.getString("observaciones")
        );

        salida.setEstado(
                rs.getString("estado")
        );

        return salida;
    }

    // =========================================================
    // MAPEAR DETALLE
    // =========================================================
    private SalidaStockDetalle mapearDetalle(
            ResultSet rs)
            throws SQLException {

        Producto producto
                = new Producto();

        producto.setIdProducto(
                rs.getInt("id_producto")
        );

        producto.setCodigo(
                rs.getString("codigo")
        );

        producto.setCodigoInterno(
                rs.getString("codigo_interno")
        );

        producto.setCodigoBarra(
                rs.getString("codigo_barra")
        );

        producto.setNombre(
                rs.getString("nombre")
        );

        producto.setDescripcion(
                rs.getString("descripcion")
        );

        producto.setUbicacion(
                rs.getString("ubicacion")
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

        producto.setMargenPorcentaje(
                rs.getBigDecimal(
                        "margen_porcentaje"
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
                rs.getBoolean("activo")
        );

        producto.setObservaciones(
                rs.getString("observaciones")
        );

        SalidaStockDetalle detalle
                = new SalidaStockDetalle();

        detalle.setIdDetalle(
                rs.getLong("id_detalle")
        );

        detalle.setIdSalida(
                rs.getLong("id_salida")
        );

        detalle.setProducto(
                producto
        );

        detalle.setCantidad(
                rs.getBigDecimal("cantidad")
        );

        return detalle;
    }
}
