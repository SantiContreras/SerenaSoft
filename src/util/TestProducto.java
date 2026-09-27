package util;

import Dao.CategoriaDao;
import Dao.MarcaDao;
import Dao.RubroDao;
import Dao.TipoIvaDao;
import Dao.UnidadMedidaDao;

import model.Categoria;
import model.Marca;
import model.Producto;
import model.Rubro;
import model.TipoIva;
import model.UnidadMedida;

import services.ProductoService;
import services.ResultadoOperacion;

import java.math.BigDecimal;

public class TestProducto {

    public static void main(String[] args) {

        // =====================================================
        // SERVICES Y DAO
        // =====================================================
        ProductoService productoService
                = new ProductoService();

        RubroDao rubroDao
                = new RubroDao();

        CategoriaDao categoriaDao
                = new CategoriaDao();

        MarcaDao marcaDao
                = new MarcaDao();

        UnidadMedidaDao unidadDao
                = new UnidadMedidaDao();

        TipoIvaDao ivaDao
                = new TipoIvaDao();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "          PRUEBA MODULO PRODUCTO"
        );

        System.out.println(
                "========================================"
        );

        // =====================================================
        // 1. BUSCAR DATOS MAESTROS
        // =====================================================
        Rubro rubro
                = rubroDao.buscarPorNombre(
                        "BEBIDAS"
                );

        Categoria categoria
                = categoriaDao.buscarPorNombre(
                        "GASEOSAS"
                );

        Marca marca
                = marcaDao.buscarPorNombre(
                        "COCA-COLA"
                );

        UnidadMedida unidad
                = unidadDao.buscarPorCodigo(
                        "UN"
                );

        TipoIva iva
                = ivaDao.buscarPorCodigo(
                        "IVA21"
                );

        // =====================================================
        // 2. VERIFICAR DATOS MAESTROS
        // =====================================================
        System.out.println();
        System.out.println(
                "1. VERIFICANDO DATOS MAESTROS"
        );

        System.out.println(
                "----------------------------------------"
        );

        System.out.println(
                "Rubro BEBIDAS: "
                + (rubro != null
                        ? "OK"
                        : "NO EXISTE")
        );

        System.out.println(
                "Categoria GASEOSAS: "
                + (categoria != null
                        ? "OK"
                        : "NO EXISTE")
        );

        System.out.println(
                "Marca COCA-COLA: "
                + (marca != null
                        ? "OK"
                        : "NO EXISTE")
        );

        System.out.println(
                "Unidad UN: "
                + (unidad != null
                        ? "OK"
                        : "NO EXISTE")
        );

        System.out.println(
                "IVA IVA21: "
                + (iva != null
                        ? "OK"
                        : "NO EXISTE")
        );

        // =====================================================
        // 3. DETENER SI FALTA ALGÚN MAESTRO
        // =====================================================
        if (rubro == null
                || categoria == null
                || marca == null
                || unidad == null
                || iva == null) {

            System.out.println();

            System.out.println(
                    "ERROR: faltan datos maestros."
            );

            System.out.println(
                    "No se puede crear el producto."
            );

            return;
        }

        // =====================================================
        // 4. MOSTRAR DATOS MAESTROS ENCONTRADOS
        // =====================================================
        System.out.println();
        System.out.println(
                "2. DATOS MAESTROS ENCONTRADOS"
        );

        System.out.println(
                "----------------------------------------"
        );

        System.out.println(
                "Rubro: "
                + rubro.getNombre()
                + " | ID: "
                + rubro.getIdRubro()
        );

        System.out.println(
                "Categoria: "
                + categoria.getNombre()
                + " | ID: "
                + categoria.getIdCategoria()
        );

        System.out.println(
                "Marca: "
                + marca.getNombre()
                + " | ID: "
                + marca.getIdMarca()
        );

        System.out.println(
                "Unidad: "
                + unidad.getNombre()
                + " | Codigo: "
                + unidad.getCodigo()
                + " | ID: "
                + unidad.getIdUnidad()
        );

        System.out.println(
                "IVA: "
                + iva.getNombre()
                + " | "
                + iva.getPorcentaje()
                + "%"
                + " | ID: "
                + iva.getIdIva()
        );

        // =====================================================
        // 5. CREAR PRODUCTO
        // =====================================================
        System.out.println();
        System.out.println(
                "3. CREANDO PRODUCTO..."
        );

        System.out.println(
                "----------------------------------------"
        );

        ResultadoOperacion resultado
                = productoService.crearProducto(
                        // Código
                        "BEB-0001",
                        // Código interno
                        "INT-0001",
                        // Código de barras
                        "779000000001",
                        // Nombre
                        "Coca-Cola 500 ml",
                        // Descripción
                        "Gaseosa Coca-Cola botella 500 ml",
                        // Ubicación
                        "Estante bebidas",
                        // Rubro
                        rubro.getIdRubro(),
                        // Categoría
                        categoria.getIdCategoria(),
                        // Marca
                        marca.getIdMarca(),
                        // Unidad de compra
                        unidad.getIdUnidad(),
                        // Unidad de venta
                        unidad.getIdUnidad(),
                        // Factor conversión
                        new BigDecimal("1.0000"),
                        // Precio compra
                        new BigDecimal("800.00"),
                        // Precio venta
                        new BigDecimal("1200.00"),
                        // Margen porcentaje
                        new BigDecimal("50.00"),
                        // IVA
                        iva.getIdIva(),
                        // Stock mínimo
                        new BigDecimal("10.000"),
                        // Stock máximo
                        new BigDecimal("100.000"),
                        // Controla stock
                        true,
                        // Permite venta sin stock
                        false,
                        // Es pesable
                        false,
                        // Permite descuento
                        true,
                        // Observaciones
                        "Producto de prueba Serena Soft"
                );

        System.out.println(
                resultado.getMensaje()
        );

        // =====================================================
        // 6. BUSCAR PRODUCTO CREADO
        // =====================================================
        System.out.println();
        System.out.println(
                "4. BUSCANDO PRODUCTO..."
        );

        System.out.println(
                "----------------------------------------"
        );

        Producto producto
                = productoService.buscarPorCodigo(
                        "BEB-0001"
                );

        if (producto == null) {

            System.out.println(
                    "ERROR: No se encontro el producto."
            );

            return;
        }

        // =====================================================
        // 7. MOSTRAR PRODUCTO
        // =====================================================
        System.out.println(
                "ID: "
                + producto.getIdProducto()
        );

        System.out.println(
                "Codigo: "
                + producto.getCodigo()
        );

        System.out.println(
                "Codigo interno: "
                + producto.getCodigoInterno()
        );

        System.out.println(
                "Codigo de barras: "
                + producto.getCodigoBarra()
        );

        System.out.println(
                "Nombre: "
                + producto.getNombre()
        );

        System.out.println(
                "Descripcion: "
                + producto.getDescripcion()
        );

        System.out.println(
                "Ubicacion: "
                + producto.getUbicacion()
        );

        // =====================================================
// 5. EDITAR PRODUCTO
// =====================================================
        System.out.println();
        System.out.println(
                "5. EDITANDO PRODUCTO..."
        );

        System.out.println(
                "----------------------------------------"
        );

        ResultadoOperacion resultadoEdicion
                = productoService.editarProducto(
                        producto.getIdProducto(),
                        "BEB-0001",
                        "INT-0001",
                        "779000000001",
                        "Coca-Cola 500 ml Retornable",
                        "Gaseosa Coca-Cola botella retornable 500 ml",
                        "Heladera bebidas",
                        rubro.getIdRubro(),
                        categoria.getIdCategoria(),
                        marca.getIdMarca(),
                        unidad.getIdUnidad(),
                        unidad.getIdUnidad(),
                        new BigDecimal("1.0000"),
                        new BigDecimal("850.00"),
                        new BigDecimal("1350.00"),
                        new BigDecimal("58.82"),
                        iva.getIdIva(),
                        new BigDecimal("15.000"),
                        new BigDecimal("120.000"),
                        true,
                        false,
                        false,
                        true,
                        "Producto actualizado desde TestProducto"
                );

        System.out.println(
                resultadoEdicion.getMensaje()
        );

// =====================================================
// 6. VOLVER A CONSULTAR DESDE MYSQL
// =====================================================
        System.out.println();
        System.out.println(
                "6. VERIFICANDO PRODUCTO ACTUALIZADO..."
        );

        System.out.println(
                "----------------------------------------"
        );

        Producto productoActualizado
                = productoService.buscarPorId(
                        producto.getIdProducto()
                );

        if (productoActualizado == null) {

            System.out.println(
                    "ERROR: No se pudo recuperar el producto actualizado."
            );

            return;
        }

        System.out.println(
                "ID: "
                + productoActualizado.getIdProducto()
        );

        System.out.println(
                "Nombre: "
                + productoActualizado.getNombre()
        );

        System.out.println(
                "Descripcion: "
                + productoActualizado.getDescripcion()
        );

        System.out.println(
                "Ubicacion: "
                + productoActualizado.getUbicacion()
        );

        System.out.println(
                "Precio compra: $"
                + productoActualizado.getPrecioCompra()
        );

        System.out.println(
                "Precio venta: $"
                + productoActualizado.getPrecioVenta()
        );

        System.out.println(
                "Margen: "
                + productoActualizado.getMargenPorcentaje()
                + "%"
        );

        System.out.println(
                "Stock minimo: "
                + productoActualizado.getStockMinimo()
        );

        System.out.println(
                "Stock maximo: "
                + productoActualizado.getStockMaximo()
        );

        System.out.println(
                "Rubro: "
                + productoActualizado.getRubro()
        );

        System.out.println(
                "Categoria: "
                + productoActualizado.getCategoria()
        );

        System.out.println(
                "Marca: "
                + productoActualizado.getMarca()
        );

        System.out.println(
                "IVA: "
                + productoActualizado.getTipoIva()
        );

        System.out.println(
                "Fecha modificacion: "
                + productoActualizado.getFechaModificacion()
        );

        // =====================================================
        // RELACIONES
        // =====================================================
        System.out.println();
        System.out.println(
                "RELACIONES"
        );

        System.out.println(
                "----------------------------------------"
        );

        System.out.println(
                "Rubro: "
                + producto.getRubro()
        );

        System.out.println(
                "Categoria: "
                + producto.getCategoria()
        );

        System.out.println(
                "Marca: "
                + producto.getMarca()
        );

        System.out.println(
                "Unidad compra: "
                + producto.getUnidadCompra()
        );

        System.out.println(
                "Unidad venta: "
                + producto.getUnidadVenta()
        );

        System.out.println(
                "IVA: "
                + producto.getTipoIva()
        );

        // =====================================================
        // PRECIOS
        // =====================================================
        System.out.println();
        System.out.println(
                "PRECIOS"
        );

        System.out.println(
                "----------------------------------------"
        );

        System.out.println(
                "Factor conversion: "
                + producto.getFactorConversion()
        );

        System.out.println(
                "Precio compra: $"
                + producto.getPrecioCompra()
        );

        System.out.println(
                "Precio venta: $"
                + producto.getPrecioVenta()
        );

        System.out.println(
                "Margen: "
                + producto.getMargenPorcentaje()
                + "%"
        );

        // =====================================================
        // STOCK
        // =====================================================
        System.out.println();
        System.out.println(
                "CONFIGURACION DE STOCK"
        );

        System.out.println(
                "----------------------------------------"
        );

        System.out.println(
                "Stock minimo: "
                + producto.getStockMinimo()
        );

        System.out.println(
                "Stock maximo: "
                + producto.getStockMaximo()
        );

        System.out.println(
                "Controla stock: "
                + producto.isControlaStock()
        );

        System.out.println(
                "Permite venta sin stock: "
                + producto.isPermiteVentaSinStock()
        );

        // =====================================================
        // OTRAS CONFIGURACIONES
        // =====================================================
        System.out.println();
        System.out.println(
                "OTRAS CONFIGURACIONES"
        );

        System.out.println(
                "----------------------------------------"
        );

        System.out.println(
                "Es pesable: "
                + producto.isPesable()
        );

        System.out.println(
                "Permite descuento: "
                + producto.isPermiteDescuento()
        );

        System.out.println(
                "Activo: "
                + producto.isActivo()
        );

        System.out.println(
                "Observaciones: "
                + producto.getObservaciones()
        );

        // =====================================================
        // FECHAS
        // =====================================================
        System.out.println();
        System.out.println(
                "FECHAS"
        );

        System.out.println(
                "----------------------------------------"
        );

        System.out.println(
                "Fecha creacion: "
                + producto.getFechaCreacion()
        );

        System.out.println(
                "Fecha modificacion: "
                + producto.getFechaModificacion()
        );

        // =====================================================
        // FIN
        // =====================================================
        System.out.println();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "       FIN PRUEBA MODULO PRODUCTO"
        );

        System.out.println(
                "========================================"
        );
    }

}
