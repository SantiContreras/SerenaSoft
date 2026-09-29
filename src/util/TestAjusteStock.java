package util;

import Dao.AjusteStockDao;
import Dao.StockProductoDao;
import Dao.UsuarioDao;

import model.AjusteStock;
import model.AjusteStockDetalle;
import model.StockProducto;
import model.Usuario;

import services.AjusteStockService;
import services.ResultadoOperacion;

import sesion.SesionUsuario;

import java.math.BigDecimal;

public class TestAjusteStock {

    // =========================================================
    // DATOS DE PRUEBA
    // =========================================================

    private static final int ID_PRODUCTO = 1;
    private static final int ID_DEPOSITO = 1;

    private static final BigDecimal STOCK_FISICO =
            new BigDecimal("67");


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "TEST AJUSTE DE STOCK - SERENA SOFT"
        );

        System.out.println(
                "=============================================="
        );


        try {

            // =================================================
            // DAO Y SERVICE
            // =================================================

            UsuarioDao usuarioDao =
                    new UsuarioDao();

            StockProductoDao stockProductoDao =
                    new StockProductoDao();

            AjusteStockDao ajusteStockDao =
                    new AjusteStockDao();

            AjusteStockService ajusteStockService =
                    new AjusteStockService();


            // =================================================
            // 1. INICIAR SESIÓN
            // =================================================

            System.out.println();
            System.out.println(
                    "1) INICIANDO SESIÓN..."
            );


            Usuario usuario =
                    usuarioDao.buscarPorUsername(
                            "admin"
                    );


            if (usuario == null) {

                System.out.println(
                        "ERROR: No se encontró "
                        + "el usuario admin."
                );

                return;
            }


            SesionUsuario.iniciarSesion(
                    usuario
            );


            System.out.println(
                    "Usuario: "
                    + usuario.getUsername()
            );


            // =================================================
            // 2. CONSULTAR STOCK INICIAL
            // =================================================

            System.out.println();
            System.out.println(
                    "2) CONSULTANDO STOCK INICIAL..."
            );


            StockProducto stockInicial =
                    stockProductoDao
                            .buscarPorProductoDeposito(
                                    ID_PRODUCTO,
                                    ID_DEPOSITO
                            );


            if (stockInicial == null) {

                System.out.println(
                        "ERROR: No existe stock "
                        + "para el producto."
                );

                return;
            }


            BigDecimal cantidadInicial =
                    stockInicial.getCantidad();


            System.out.println(
                    "Stock inicial: "
                    + cantidadInicial
                    + " UN"
            );


            // =================================================
            // 3. CREAR AJUSTE
            // =================================================

            System.out.println();
            System.out.println(
                    "3) CREANDO AJUSTE..."
            );


            ResultadoOperacion resultadoCrear =
                    ajusteStockService.crearAjuste(
                            ID_DEPOSITO,
                            "PRUEBA AJUSTE STOCK",
                            "TEST SERENA SOFT"
                    );


            System.out.println(
                    resultadoCrear.getMensaje()
            );


            if (!resultadoCrear.isExitoso()) {

                return;
            }


            // =================================================
            // OBTENER ÚLTIMO BORRADOR
            //
            // Para el test buscamos los borradores y tomamos
            // el más reciente, ya que listarPorEstado()
            // devuelve fecha/id descendente.
            // =================================================

            AjusteStock ajuste =
                    null;


            for (AjusteStock candidato
                    : ajusteStockService
                            .listarPorEstado(
                                    "BORRADOR"
                            )) {

                if ("PRUEBA AJUSTE STOCK".equals(
                        candidato.getMotivo()
                )) {

                    ajuste =
                            candidato;

                    break;
                }
            }


            if (ajuste == null) {

                System.out.println(
                        "ERROR: No se pudo recuperar "
                        + "el ajuste creado."
                );

                return;
            }


            long idAjuste =
                    ajuste.getIdAjuste();


            System.out.println(
                    "Ajuste creado. ID: "
                    + idAjuste
            );


            System.out.println(
                    "Estado: "
                    + ajuste.getEstado()
            );


            // =================================================
            // 4. AGREGAR PRODUCTO
            // =================================================

            System.out.println();
            System.out.println(
                    "4) AGREGANDO PRODUCTO..."
            );


            ResultadoOperacion resultadoAgregar =
                    ajusteStockService.agregarProducto(
                            idAjuste,
                            ID_PRODUCTO,
                            STOCK_FISICO
                    );


            System.out.println(
                    resultadoAgregar.getMensaje()
            );


            if (!resultadoAgregar.isExitoso()) {

                return;
            }


            // =================================================
            // 5. MOSTRAR DETALLE
            // =================================================

            ajuste =
                    ajusteStockService.buscarPorId(
                            idAjuste
                    );


            if (ajuste == null
                    || ajuste.getDetalles() == null
                    || ajuste.getDetalles().isEmpty()) {

                System.out.println(
                        "ERROR: No se encontró "
                        + "el detalle del ajuste."
                );

                return;
            }


            AjusteStockDetalle detalle =
                    ajuste
                            .getDetalles()
                            .get(0);


            System.out.println();
            System.out.println(
                    "5) DETALLE DEL AJUSTE"
            );


            System.out.println(
                    "Producto: "
                    + detalle
                            .getProducto()
                            .getNombre()
            );


            System.out.println(
                    "Stock sistema: "
                    + detalle.getStockSistema()
                    + " UN"
            );


            System.out.println(
                    "Stock físico: "
                    + detalle.getStockFisico()
                    + " UN"
            );


            System.out.println(
                    "Diferencia: "
                    + detalle.getDiferencia()
                    + " UN"
            );


            // =================================================
            // 6. COMPROBAR QUE BORRADOR NO MODIFICÓ STOCK
            // =================================================

            System.out.println();
            System.out.println(
                    "6) COMPROBANDO STOCK EN BORRADOR..."
            );


            StockProducto stockBorrador =
                    stockProductoDao
                            .buscarPorProductoDeposito(
                                    ID_PRODUCTO,
                                    ID_DEPOSITO
                            );


            BigDecimal cantidadBorrador =
                    stockBorrador.getCantidad();


            System.out.println(
                    "Stock mientras está BORRADOR: "
                    + cantidadBorrador
                    + " UN"
            );


            if (cantidadBorrador.compareTo(
                    cantidadInicial
            ) != 0) {

                System.out.println(
                        "ERROR: El BORRADOR modificó "
                        + "el stock."
                );

                return;
            }


            System.out.println(
                    "OK: el borrador NO modificó el stock."
            );


            // =================================================
            // 7. CONFIRMAR AJUSTE
            // =================================================

            System.out.println();
            System.out.println(
                    "7) CONFIRMANDO AJUSTE..."
            );


            ResultadoOperacion resultadoConfirmar =
                    ajusteStockService
                            .confirmarAjuste(
                                    idAjuste
                            );


            System.out.println(
                    resultadoConfirmar.getMensaje()
            );


            if (!resultadoConfirmar.isExitoso()) {

                return;
            }


            // =================================================
            // 8. VERIFICAR STOCK FINAL
            // =================================================

            System.out.println();
            System.out.println(
                    "8) VERIFICANDO STOCK FINAL..."
            );


            StockProducto stockFinal =
                    stockProductoDao
                            .buscarPorProductoDeposito(
                                    ID_PRODUCTO,
                                    ID_DEPOSITO
                            );


            BigDecimal cantidadFinal =
                    stockFinal.getCantidad();


            System.out.println(
                    "Stock esperado: "
                    + STOCK_FISICO
                    + " UN"
            );


            System.out.println(
                    "Stock obtenido: "
                    + cantidadFinal
                    + " UN"
            );


            if (cantidadFinal.compareTo(
                    STOCK_FISICO
            ) != 0) {

                System.out.println(
                        "ERROR: El stock final "
                        + "no es correcto."
                );

                return;
            }


            System.out.println(
                    "OK: stock actualizado correctamente."
            );


            // =================================================
            // 9. VERIFICAR ESTADO
            // =================================================

            ajuste =
                    ajusteStockService.buscarPorId(
                            idAjuste
                    );


            System.out.println();
            System.out.println(
                    "9) VERIFICANDO ESTADO..."
            );


            System.out.println(
                    "Estado: "
                    + ajuste.getEstado()
            );


            if (!"CONFIRMADO".equals(
                    ajuste.getEstado()
            )) {

                System.out.println(
                        "ERROR: El ajuste no quedó "
                        + "CONFIRMADO."
                );

                return;
            }


            System.out.println(
                    "OK: ajuste CONFIRMADO."
            );


            // =================================================
            // 10. PROBAR DOBLE CONFIRMACIÓN
            // =================================================

            System.out.println();
            System.out.println(
                    "10) PROBANDO DOBLE CONFIRMACIÓN..."
            );


            ResultadoOperacion segundaConfirmacion =
                    ajusteStockService
                            .confirmarAjuste(
                                    idAjuste
                            );


            System.out.println(
                    segundaConfirmacion.getMensaje()
            );


            if (segundaConfirmacion.isExitoso()) {

                System.out.println(
                        "ERROR: Se permitió confirmar "
                        + "el ajuste dos veces."
                );

                return;
            }


            System.out.println(
                    "OK: segunda confirmación rechazada."
            );


            // =================================================
            // 11. VERIFICAR QUE STOCK SIGUE EN 67
            // =================================================

            StockProducto stockDespues =
                    stockProductoDao
                            .buscarPorProductoDeposito(
                                    ID_PRODUCTO,
                                    ID_DEPOSITO
                            );


            System.out.println();
            System.out.println(
                    "11) STOCK DESPUÉS DE INTENTAR "
                    + "CONFIRMAR OTRA VEZ:"
            );


            System.out.println(
                    stockDespues.getCantidad()
                    + " UN"
            );


            if (stockDespues
                    .getCantidad()
                    .compareTo(
                            STOCK_FISICO
                    ) != 0) {

                System.out.println(
                        "ERROR: La segunda confirmación "
                        + "alteró el stock."
                );

                return;
            }


            System.out.println(
                    "OK: el stock permanece correcto."
            );


            // =================================================
            // TEST FINALIZADO
            // =================================================

            System.out.println();
            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "TEST AJUSTE STOCK FINALIZADO CORRECTAMENTE"
            );

            System.out.println(
                    "=============================================="
            );


        } catch (Exception ex) {

            System.out.println();
            System.out.println(
                    "ERROR GENERAL:"
            );

            ex.printStackTrace();

        } finally {

            // =================================================
            // CERRAR SESIÓN
            // =================================================

            SesionUsuario.cerrarSesion();
        }
    }
}