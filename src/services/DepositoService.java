package services;

import Dao.DepositoDao;
import model.Deposito;

import java.util.List;

public class DepositoService {

    private final DepositoDao depositoDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public DepositoService() {

        depositoDao =
                new DepositoDao();
    }


    // =========================================================
    // CREAR DEPÓSITO
    // =========================================================

    public ResultadoOperacion crearDeposito(
            String codigo,
            String nombre,
            String descripcion,
            boolean esPrincipal) {

        codigo = limpiar(codigo);
        nombre = limpiar(nombre);
        descripcion = limpiar(descripcion);


        // =====================================================
        // VALIDACIONES
        // =====================================================

        ResultadoOperacion validacion =
                validarDatos(
                        codigo,
                        nombre,
                        descripcion
                );

        if (!validacion.isExitoso()) {
            return validacion;
        }


        // =====================================================
        // VALIDAR CÓDIGO DUPLICADO
        // =====================================================

        if (depositoDao.existeCodigo(codigo)) {

            return ResultadoOperacion.error(
                    "Ya existe un depósito con el código "
                    + codigo + "."
            );
        }


        // =====================================================
        // VALIDAR DEPÓSITO PRINCIPAL
        // =====================================================

        if (esPrincipal) {

            Deposito principalActual =
                    depositoDao.buscarPrincipal();

            if (principalActual != null) {

                return ResultadoOperacion.error(
                        "Ya existe un depósito principal: "
                        + principalActual.getNombre() + "."
                );
            }
        }


        // =====================================================
        // CREAR OBJETO
        // =====================================================

        Deposito deposito =
                new Deposito();

        deposito.setCodigo(codigo);
        deposito.setNombre(nombre);
        deposito.setDescripcion(descripcion);
        deposito.setEsPrincipal(esPrincipal);
        deposito.setActivo(true);


        // =====================================================
        // GUARDAR
        // =====================================================

        if (depositoDao.guardar(deposito)) {

            return ResultadoOperacion.ok(
                    "Depósito creado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo crear el depósito."
        );
    }


    // =========================================================
    // EDITAR DEPÓSITO
    // =========================================================

    public ResultadoOperacion editarDeposito(
            int idDeposito,
            String codigo,
            String nombre,
            String descripcion,
            boolean esPrincipal) {

        if (idDeposito <= 0) {

            return ResultadoOperacion.error(
                    "El ID del depósito no es válido."
            );
        }


        Deposito deposito =
                depositoDao.buscarPorId(
                        idDeposito
                );

        if (deposito == null) {

            return ResultadoOperacion.error(
                    "El depósito no existe."
            );
        }


        codigo = limpiar(codigo);
        nombre = limpiar(nombre);
        descripcion = limpiar(descripcion);


        ResultadoOperacion validacion =
                validarDatos(
                        codigo,
                        nombre,
                        descripcion
                );

        if (!validacion.isExitoso()) {
            return validacion;
        }


        if (depositoDao.existeCodigoEnOtroDeposito(
                codigo,
                idDeposito)) {

            return ResultadoOperacion.error(
                    "Ya existe otro depósito con el código "
                    + codigo + "."
            );
        }


        // =====================================================
        // VALIDAR PRINCIPAL
        // =====================================================

        if (esPrincipal) {

            Deposito principalActual =
                    depositoDao.buscarPrincipal();

            if (principalActual != null
                    && principalActual.getIdDeposito()
                    != idDeposito) {

                return ResultadoOperacion.error(
                        "Ya existe un depósito principal: "
                        + principalActual.getNombre() + "."
                );
            }
        }


        deposito.setCodigo(codigo);
        deposito.setNombre(nombre);
        deposito.setDescripcion(descripcion);
        deposito.setEsPrincipal(esPrincipal);


        if (depositoDao.actualizar(deposito)) {

            return ResultadoOperacion.ok(
                    "Depósito actualizado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo actualizar el depósito."
        );
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public Deposito buscarPorId(
            int idDeposito) {

        if (idDeposito <= 0) {
            return null;
        }

        return depositoDao.buscarPorId(
                idDeposito
        );
    }


    // =========================================================
    // BUSCAR POR CÓDIGO
    // =========================================================

    public Deposito buscarPorCodigo(
            String codigo) {

        codigo = limpiar(codigo);

        if (codigo == null) {
            return null;
        }

        return depositoDao.buscarPorCodigo(
                codigo
        );
    }


    // =========================================================
    // BUSCAR PRINCIPAL
    // =========================================================

    public Deposito buscarPrincipal() {

        return depositoDao.buscarPrincipal();
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<Deposito> listarTodos() {

        return depositoDao.listarTodos();
    }


    // =========================================================
    // LISTAR ACTIVOS
    // =========================================================

    public List<Deposito> listarActivos() {

        return depositoDao.listarActivos();
    }


    // =========================================================
    // DESACTIVAR
    // =========================================================

    public ResultadoOperacion desactivarDeposito(
            int idDeposito) {

        Deposito deposito =
                depositoDao.buscarPorId(
                        idDeposito
                );

        if (deposito == null) {

            return ResultadoOperacion.error(
                    "El depósito no existe."
            );
        }

        if (!deposito.isActivo()) {

            return ResultadoOperacion.error(
                    "El depósito ya está inactivo."
            );
        }

        if (deposito.isEsPrincipal()) {

            return ResultadoOperacion.error(
                    "No se puede desactivar el depósito principal."
            );
        }


        if (depositoDao.cambiarEstado(
                idDeposito,
                false)) {

            return ResultadoOperacion.ok(
                    "Depósito desactivado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo desactivar el depósito."
        );
    }


    // =========================================================
    // ACTIVAR
    // =========================================================

    public ResultadoOperacion activarDeposito(
            int idDeposito) {

        Deposito deposito =
                depositoDao.buscarPorId(
                        idDeposito
                );

        if (deposito == null) {

            return ResultadoOperacion.error(
                    "El depósito no existe."
            );
        }

        if (deposito.isActivo()) {

            return ResultadoOperacion.error(
                    "El depósito ya está activo."
            );
        }


        if (depositoDao.cambiarEstado(
                idDeposito,
                true)) {

            return ResultadoOperacion.ok(
                    "Depósito activado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo activar el depósito."
        );
    }


    // =========================================================
    // VALIDAR DATOS
    // =========================================================

    private ResultadoOperacion validarDatos(
            String codigo,
            String nombre,
            String descripcion) {

        if (codigo == null) {

            return ResultadoOperacion.error(
                    "El código del depósito es obligatorio."
            );
        }

        if (codigo.length() > 20) {

            return ResultadoOperacion.error(
                    "El código no puede superar los 20 caracteres."
            );
        }

        if (nombre == null) {

            return ResultadoOperacion.error(
                    "El nombre del depósito es obligatorio."
            );
        }

        if (nombre.length() > 100) {

            return ResultadoOperacion.error(
                    "El nombre no puede superar los 100 caracteres."
            );
        }

        if (descripcion != null
                && descripcion.length() > 250) {

            return ResultadoOperacion.error(
                    "La descripción no puede superar los 250 caracteres."
            );
        }

        return ResultadoOperacion.ok(
                "Datos válidos."
        );
    }


    // =========================================================
    // LIMPIAR TEXTO
    // =========================================================

    private String limpiar(
            String texto) {

        if (texto == null) {
            return null;
        }

        texto = texto.trim();

        return texto.isEmpty()
                ? null
                : texto;
    }
}