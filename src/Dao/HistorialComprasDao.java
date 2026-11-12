package Dao;

import Configuracion.conexion;
import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

public class HistorialComprasDao {
    public static class CompraFila {
        public long id;
        public Timestamp fecha;
        public String proveedor, comprobante, estado, letra;
        public int idProveedor;
        public BigDecimal total;
        public CompraFila(ResultSet r) throws SQLException {
            id=r.getLong("id_compra"); idProveedor=r.getInt("id_proveedor"); fecha=r.getTimestamp("fecha");
            proveedor=r.getString("proveedor"); comprobante=r.getString("comprobante");
            estado=r.getString("estado"); letra=r.getString("letra"); total=r.getBigDecimal("total");
        }
    }
    public List<CompraFila> listar(String filtro, boolean soloConfirmadas) throws SQLException {
        List<CompraFila> lista=new ArrayList<>();
        String sql="SELECT c.id_compra,c.id_proveedor,c.fecha,p.razon_social proveedor,c.numero_comprobante comprobante,c.estado,c.total,COALESCE(cf.letra,'-') letra "
          +"FROM compra c JOIN proveedor p ON p.id_proveedor=c.id_proveedor "
          +"LEFT JOIN compra_fiscal cf ON cf.id_compra=c.id_compra "
          +"WHERE (?=0 OR c.estado='CONFIRMADA') AND (CAST(c.id_compra AS CHAR) LIKE ? OR p.razon_social LIKE ? OR COALESCE(c.numero_comprobante,'') LIKE ?) "
          +"ORDER BY c.fecha DESC,c.id_compra DESC LIMIT 500";
        String q="%"+(filtro==null?"":filtro.trim())+"%";
        try(Connection c=conexion.getConexion();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,soloConfirmadas?1:0); for(int i=2;i<=4;i++)ps.setString(i,q);
            try(ResultSet rs=ps.executeQuery()){while(rs.next())lista.add(new CompraFila(rs));}
        }
        return lista;
    }

    /** Consulta filtrada sin LIMIT para reportes; las fechas incluyen ambos extremos. */
    public List<CompraFila> listarReporte(String filtro, boolean soloConfirmadas, int proveedorId,
            java.time.LocalDate desde, java.time.LocalDate hasta) throws SQLException {
        StringBuilder sql=new StringBuilder("SELECT c.id_compra,c.id_proveedor,c.fecha,p.razon_social proveedor,"
          +"c.numero_comprobante comprobante,c.estado,c.total,COALESCE(cf.letra,'-') letra "
          +"FROM compra c JOIN proveedor p ON p.id_proveedor=c.id_proveedor "
          +"LEFT JOIN compra_fiscal cf ON cf.id_compra=c.id_compra WHERE 1=1 ");
        if(soloConfirmadas)sql.append("AND c.estado='CONFIRMADA' ");
        if(proveedorId>0)sql.append("AND c.id_proveedor=? ");
        if(desde!=null)sql.append("AND c.fecha>=? ");
        if(hasta!=null)sql.append("AND c.fecha<? ");
        sql.append("AND (CAST(c.id_compra AS CHAR) LIKE ? OR p.razon_social LIKE ? OR COALESCE(c.numero_comprobante,'') LIKE ?) ");
        sql.append("ORDER BY c.fecha DESC,c.id_compra DESC");
        List<CompraFila> lista=new ArrayList<>();
        try(Connection cn=conexion.getConexion();PreparedStatement ps=cn.prepareStatement(sql.toString())){
            int i=1;
            if(proveedorId>0)ps.setInt(i++,proveedorId);
            if(desde!=null)ps.setTimestamp(i++,Timestamp.valueOf(desde.atStartOfDay()));
            if(hasta!=null)ps.setTimestamp(i++,Timestamp.valueOf(hasta.plusDays(1).atStartOfDay()));
            String q="%"+(filtro==null?"":filtro.trim())+"%";
            for(int j=0;j<3;j++)ps.setString(i++,q);
            try(ResultSet rs=ps.executeQuery()){while(rs.next())lista.add(new CompraFila(rs));}
        }
        return lista;
    }

    public List<Object[]> detalles(long id) throws SQLException {
        List<Object[]> lista=new ArrayList<>();
        String sql="SELECT p.codigo,p.nombre,cd.unidad_compra,cd.cantidad,cd.factor_conversion,cd.cantidad_stock,cd.costo_unitario,cd.subtotal "
          +"FROM compra_detalle cd JOIN producto p ON p.id_producto=cd.id_producto WHERE cd.id_compra=? ORDER BY cd.id_detalle";
        try(Connection c=conexion.getConexion();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setLong(1,id);try(ResultSet rs=ps.executeQuery()){while(rs.next())lista.add(new Object[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getBigDecimal(4),rs.getBigDecimal(5),rs.getBigDecimal(6),rs.getBigDecimal(7),rs.getBigDecimal(8)});}
        }return lista;
    }
    public List<Object[]> impuestos(long id) throws SQLException {
        List<Object[]> lista=new ArrayList<>();
        String sql="SELECT alicuota,base_imponible,importe_iva,credito_computable FROM compra_fiscal_iva WHERE id_compra=? ORDER BY alicuota";
        try(Connection c=conexion.getConexion();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setLong(1,id);try(ResultSet rs=ps.executeQuery()){while(rs.next())lista.add(new Object[]{rs.getBigDecimal(1),rs.getBigDecimal(2),rs.getBigDecimal(3),rs.getBigDecimal(4)});}
        }return lista;
    }
    public void guardarAdjunto(long id,String nombre,String mime,byte[] datos) throws SQLException {
        String sql="INSERT INTO compra_adjunto (id_compra,nombre_archivo,tipo_mime,contenido) VALUES (?,?,?,?)";
        try(Connection c=conexion.getConexion();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setLong(1,id);ps.setString(2,nombre);ps.setString(3,mime);ps.setBytes(4,datos);ps.executeUpdate();
        }
    }
    public List<Object[]> listarAdjuntos(long id) throws SQLException {
        List<Object[]> lista=new ArrayList<>();
        try(Connection c=conexion.getConexion();PreparedStatement ps=c.prepareStatement("SELECT id_adjunto,nombre_archivo,fecha_carga,OCTET_LENGTH(contenido) FROM compra_adjunto WHERE id_compra=? ORDER BY id_adjunto DESC")){
            ps.setLong(1,id);try(ResultSet rs=ps.executeQuery()){while(rs.next())lista.add(new Object[]{rs.getLong(1),rs.getString(2),rs.getTimestamp(3),rs.getLong(4)});}
        }return lista;
    }
    public byte[] leerAdjunto(long id,long compra) throws SQLException {
        try(Connection c=conexion.getConexion();PreparedStatement ps=c.prepareStatement("SELECT contenido FROM compra_adjunto WHERE id_adjunto=? AND id_compra=?")){
            ps.setLong(1,id);ps.setLong(2,compra);try(ResultSet rs=ps.executeQuery()){if(rs.next())return rs.getBytes(1);}
        }throw new SQLException("Adjunto inexistente");
    }
}
