package services;

import Dao.UnidadMedidaDao;
import model.UnidadMedida;

import java.util.List;

public class UnidadMedidaService {

    private final UnidadMedidaDao unidadDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public UnidadMedidaService() {
        this.unidadDao = new UnidadMedidaDao();
    }


    // =========================================================
    // CREAR
    // =========================================================

    public ResultadoOperacion crearUnidad(String codigo,String nombre,boolean permiteDecimales) {

        codigo = limpiar(codigo);
        nombre = limpiar(nombre);

        if (codigo == null) {

            return ResultadoOperacion.error(
                    "El código es obligatorio."
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
                    "El nombre es obligatorio."
            );
        }

        if (nombre.length() > 60) {

            return ResultadoOperacion.error(
                    "El nombre no puede superar los 60 caracteres."
            );
        }

        if (unidadDao.existeCodigo(codigo)) {

            return ResultadoOperacion.error(
                    "Ya existe una unidad con el código "
                    + codigo + "."
            );
        }

        UnidadMedida unidad = new UnidadMedida();

        unidad.setCodigo(codigo);
        unidad.setNombre(nombre);
        unidad.setPermiteDecimales(permiteDecimales);
        unidad.setActivo(true);

        if (unidadDao.guardar(unidad)) {

            return ResultadoOperacion.ok(
                    "Unidad de medida creada correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo crear la unidad de medida."
        );
    }


    // =========================================================
    // EDITAR
    // =========================================================

    public ResultadoOperacion editarUnidad(
            int idUnidad,
            String codigo,
            String nombre,
            boolean permiteDecimales) {

        if (idUnidad <= 0) {

            return ResultadoOperacion.error(
                    "El ID de la unidad no es válido."
            );
        }

        UnidadMedida unidad =
                unidadDao.buscarPorId(idUnidad);

        if (unidad == null) {

            return ResultadoOperacion.error(
                    "La unidad de medida no existe."
            );
        }

        codigo = limpiar(codigo);
        nombre = limpiar(nombre);

        if (codigo == null) {

            return ResultadoOperacion.error(
                    "El código es obligatorio."
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
                    "El nombre es obligatorio."
            );
        }

        if (nombre.length() > 60) {

            return ResultadoOperacion.error(
                    "El nombre no puede superar los 60 caracteres."
            );
        }

        if (unidadDao.existeCodigoEnOtraUnidad(
                codigo,
                idUnidad)) {

            return ResultadoOperacion.error(
                    "Ya existe otra unidad con el código "
                    + codigo + "."
            );
        }

        unidad.setCodigo(codigo);
        unidad.setNombre(nombre);
        unidad.setPermiteDecimales(permiteDecimales);

        if (unidadDao.actualizar(unidad)) {

            return ResultadoOperacion.ok(
                    "Unidad de medida actualizada correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo actualizar la unidad de medida."
        );
    }


    // =========================================================
    // ACTIVAR
    // =========================================================

    public ResultadoOperacion activarUnidad(
            int idUnidad) {

        UnidadMedida unidad =
                unidadDao.buscarPorId(idUnidad);

        if (unidad == null) {

            return ResultadoOperacion.error(
                    "La unidad de medida no existe."
            );
        }

        if (unidad.isActivo()) {

            return ResultadoOperacion.error(
                    "La unidad ya se encuentra activa."
            );
        }

        if (unidadDao.cambiarEstado(
                idUnidad,
                true)) {

            return ResultadoOperacion.ok(
                    "Unidad activada correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo activar la unidad."
        );
    }


    // =========================================================
    // DESACTIVAR
    // =========================================================

    public ResultadoOperacion desactivarUnidad(
            int idUnidad) {

        UnidadMedida unidad =
                unidadDao.buscarPorId(idUnidad);

        if (unidad == null) {

            return ResultadoOperacion.error(
                    "La unidad de medida no existe."
            );
        }

        if (!unidad.isActivo()) {

            return ResultadoOperacion.error(
                    "La unidad ya se encuentra inactiva."
            );
        }

        if (unidadDao.cambiarEstado(
                idUnidad,
                false)) {

            return ResultadoOperacion.ok(
                    "Unidad desactivada correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo desactivar la unidad."
        );
    }


    // =========================================================
    // BÚSQUEDAS
    // =========================================================

    public UnidadMedida buscarPorId(int idUnidad) {

        if (idUnidad <= 0) {
            return null;
        }

        return unidadDao.buscarPorId(idUnidad);
    }


    public UnidadMedida buscarPorCodigo(String codigo) {

        codigo = limpiar(codigo);

        if (codigo == null) {
            return null;
        }

        return unidadDao.buscarPorCodigo(
                codigo.toUpperCase()
        );
    }


    // =========================================================
    // LISTADOS
    // =========================================================

    public List<UnidadMedida> listarTodas() {
        return unidadDao.listarTodas();
    }


    public List<UnidadMedida> listarActivas() {
        return unidadDao.listarActivas();
    }


    // =========================================================
    // LIMPIAR TEXTO
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