package services;

import Dao.TipoIvaDao;
import model.TipoIva;

import java.math.BigDecimal;
import java.util.List;

public class TipoIvaService {

    private final TipoIvaDao tipoIvaDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public TipoIvaService() {
        this.tipoIvaDao = new TipoIvaDao();
    }


    // =========================================================
    // CREAR
    // =========================================================

    public ResultadoOperacion crearTipoIva(
            String codigo,
            String nombre,
            BigDecimal porcentaje) {

        codigo = limpiar(codigo);
        nombre = limpiar(nombre);

        if (codigo == null) {

            return ResultadoOperacion.error(
                    "El código del IVA es obligatorio."
            );
        }

        codigo = codigo.toUpperCase();

        if (codigo.length() > 20) {

            return ResultadoOperacion.error(
                    "El código no puede superar los 20 caracteres."
            );
        }

        if (nombre == null) {

            return ResultadoOperacion.error(
                    "El nombre del IVA es obligatorio."
            );
        }

        if (nombre.length() > 80) {

            return ResultadoOperacion.error(
                    "El nombre no puede superar los 80 caracteres."
            );
        }

        if (porcentaje == null) {

            return ResultadoOperacion.error(
                    "El porcentaje es obligatorio."
            );
        }

        if (porcentaje.compareTo(BigDecimal.ZERO) < 0) {

            return ResultadoOperacion.error(
                    "El porcentaje no puede ser negativo."
            );
        }

        if (porcentaje.compareTo(
                new BigDecimal("999.999")) > 0) {

            return ResultadoOperacion.error(
                    "El porcentaje supera el valor permitido."
            );
        }

        if (porcentaje.scale() > 3) {

            return ResultadoOperacion.error(
                    "El porcentaje admite como máximo 3 decimales."
            );
        }

        if (tipoIvaDao.existeCodigo(codigo)) {

            return ResultadoOperacion.error(
                    "Ya existe un tipo de IVA con el código "
                    + codigo + "."
            );
        }

        TipoIva tipoIva = new TipoIva();

        tipoIva.setCodigo(codigo);
        tipoIva.setNombre(nombre);
        tipoIva.setPorcentaje(porcentaje);
        tipoIva.setActivo(true);

        if (tipoIvaDao.guardar(tipoIva)) {

            return ResultadoOperacion.ok(
                    "Tipo de IVA creado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo crear el tipo de IVA."
        );
    }


    // =========================================================
    // EDITAR
    // =========================================================

    public ResultadoOperacion editarTipoIva(
            int idIva,
            String codigo,
            String nombre,
            BigDecimal porcentaje) {

        if (idIva <= 0) {

            return ResultadoOperacion.error(
                    "El ID del tipo de IVA no es válido."
            );
        }

        TipoIva tipoIva =
                tipoIvaDao.buscarPorId(idIva);

        if (tipoIva == null) {

            return ResultadoOperacion.error(
                    "El tipo de IVA no existe."
            );
        }

        codigo = limpiar(codigo);
        nombre = limpiar(nombre);

        if (codigo == null) {

            return ResultadoOperacion.error(
                    "El código del IVA es obligatorio."
            );
        }

        codigo = codigo.toUpperCase();

        if (codigo.length() > 20) {

            return ResultadoOperacion.error(
                    "El código no puede superar los 20 caracteres."
            );
        }

        if (nombre == null) {

            return ResultadoOperacion.error(
                    "El nombre del IVA es obligatorio."
            );
        }

        if (nombre.length() > 80) {

            return ResultadoOperacion.error(
                    "El nombre no puede superar los 80 caracteres."
            );
        }

        if (porcentaje == null) {

            return ResultadoOperacion.error(
                    "El porcentaje es obligatorio."
            );
        }

        if (porcentaje.compareTo(BigDecimal.ZERO) < 0) {

            return ResultadoOperacion.error(
                    "El porcentaje no puede ser negativo."
            );
        }

        if (porcentaje.compareTo(
                new BigDecimal("999.999")) > 0) {

            return ResultadoOperacion.error(
                    "El porcentaje supera el valor permitido."
            );
        }

        if (porcentaje.scale() > 3) {

            return ResultadoOperacion.error(
                    "El porcentaje admite como máximo 3 decimales."
            );
        }

        if (tipoIvaDao.existeCodigoEnOtroTipo(
                codigo,
                idIva)) {

            return ResultadoOperacion.error(
                    "Ya existe otro tipo de IVA con el código "
                    + codigo + "."
            );
        }

        tipoIva.setCodigo(codigo);
        tipoIva.setNombre(nombre);
        tipoIva.setPorcentaje(porcentaje);

        if (tipoIvaDao.actualizar(tipoIva)) {

            return ResultadoOperacion.ok(
                    "Tipo de IVA actualizado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo actualizar el tipo de IVA."
        );
    }


    // =========================================================
    // ACTIVAR
    // =========================================================

    public ResultadoOperacion activarTipoIva(
            int idIva) {

        TipoIva tipoIva =
                tipoIvaDao.buscarPorId(idIva);

        if (tipoIva == null) {

            return ResultadoOperacion.error(
                    "El tipo de IVA no existe."
            );
        }

        if (tipoIva.isActivo()) {

            return ResultadoOperacion.error(
                    "El tipo de IVA ya se encuentra activo."
            );
        }

        if (tipoIvaDao.cambiarEstado(
                idIva,
                true)) {

            return ResultadoOperacion.ok(
                    "Tipo de IVA activado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo activar el tipo de IVA."
        );
    }


    // =========================================================
    // DESACTIVAR
    // =========================================================

    public ResultadoOperacion desactivarTipoIva(
            int idIva) {

        TipoIva tipoIva =
                tipoIvaDao.buscarPorId(idIva);

        if (tipoIva == null) {

            return ResultadoOperacion.error(
                    "El tipo de IVA no existe."
            );
        }

        if (!tipoIva.isActivo()) {

            return ResultadoOperacion.error(
                    "El tipo de IVA ya se encuentra inactivo."
            );
        }

        if (tipoIvaDao.cambiarEstado(
                idIva,
                false)) {

            return ResultadoOperacion.ok(
                    "Tipo de IVA desactivado correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo desactivar el tipo de IVA."
        );
    }


    // =========================================================
    // BÚSQUEDAS
    // =========================================================

    public TipoIva buscarPorId(int idIva) {

        if (idIva <= 0) {
            return null;
        }

        return tipoIvaDao.buscarPorId(idIva);
    }


    public TipoIva buscarPorCodigo(String codigo) {

        codigo = limpiar(codigo);

        if (codigo == null) {
            return null;
        }

        return tipoIvaDao.buscarPorCodigo(
                codigo.toUpperCase()
        );
    }


    // =========================================================
    // LISTADOS
    // =========================================================

    public List<TipoIva> listarTodos() {
        return tipoIvaDao.listarTodos();
    }


    public List<TipoIva> listarActivos() {
        return tipoIvaDao.listarActivos();
    }


    // =========================================================
    // LIMPIAR
    // =========================================================

    private String limpiar(String texto) {

        if (texto == null) {
            return null;
        }

        texto = texto.trim();

        return texto.isEmpty()
                ? null
                : texto;
    }
}