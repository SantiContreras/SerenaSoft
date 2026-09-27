package services;

import Dao.MarcaDao;
import model.Marca;

import java.util.List;

public class MarcaService {

    private final MarcaDao marcaDao;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public MarcaService() {
        this.marcaDao = new MarcaDao();
    }

    // =========================================================
    // CREAR MARCA
    // =========================================================
    public ResultadoOperacion crearMarca(String nombre, String descripcion) {

        nombre = limpiar(nombre);
        descripcion = limpiar(descripcion);
        // -----------------------------------------------------
        // VALIDAR NOMBRE
        // -----------------------------------------------------
        if (nombre == null || nombre.isBlank()) {

            return ResultadoOperacion.error(
                    "El nombre de la marca es obligatorio."
            );
        }

        if (nombre.length() > 100) {

            return ResultadoOperacion.error(
                    "El nombre de la marca no puede superar los 100 caracteres."
            );
        }

        // -----------------------------------------------------
        // VALIDAR DESCRIPCIÓN
        // -----------------------------------------------------
        if (descripcion != null
                && descripcion.length() > 250) {

            return ResultadoOperacion.error(
                    "La descripción no puede superar los 250 caracteres."
            );
        }

        // -----------------------------------------------------
        // VALIDAR DUPLICADO
        // -----------------------------------------------------
        if (marcaDao.existeNombre(nombre)) {

            return ResultadoOperacion.error(
                    "Ya existe una marca con el nombre "
                    + nombre + "."
            );
        }

        // -----------------------------------------------------
        // CREAR OBJETO
        // -----------------------------------------------------
        Marca marca = new Marca();

        marca.setNombre(nombre);
        marca.setDescripcion(descripcion);
        marca.setActivo(true);

        // -----------------------------------------------------
        // GUARDAR
        // -----------------------------------------------------
        if (marcaDao.guardar(marca)) {

            return ResultadoOperacion.ok(
                    "Marca creada correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo crear la marca."
        );
    }

    // =========================================================
    // EDITAR MARCA
    // =========================================================
    public ResultadoOperacion editarMarca(int idMarca, String nombre, String descripcion) {
        nombre = limpiar(nombre);
        descripcion = limpiar(descripcion);
        // -----------------------------------------------------
        // VALIDAR ID
        // -----------------------------------------------------
        if (idMarca <= 0) {

            return ResultadoOperacion.error(
                    "El ID de la marca no es válido."
            );
        }

        // -----------------------------------------------------
        // BUSCAR MARCA
        // -----------------------------------------------------
        Marca marca
                = marcaDao.buscarPorId(idMarca);

        if (marca == null) {

            return ResultadoOperacion.error(
                    "La marca no existe."
            );
        }

        // -----------------------------------------------------
        // VALIDAR NOMBRE
        // -----------------------------------------------------
        if (nombre == null || nombre.isBlank()) {

            return ResultadoOperacion.error(
                    "El nombre de la marca es obligatorio."
            );
        }

        if (nombre.length() > 100) {

            return ResultadoOperacion.error(
                    "El nombre de la marca no puede superar los 100 caracteres."
            );
        }

        // -----------------------------------------------------
        // VALIDAR DESCRIPCIÓN
        // -----------------------------------------------------
        if (descripcion != null
                && descripcion.length() > 250) {

            return ResultadoOperacion.error(
                    "La descripción no puede superar los 250 caracteres."
            );
        }

        // -----------------------------------------------------
        // VALIDAR DUPLICADO
        // -----------------------------------------------------
        if (marcaDao.existeNombreEnOtraMarca(
                nombre,
                idMarca)) {

            return ResultadoOperacion.error(
                    "Ya existe otra marca con el nombre "
                    + nombre + "."
            );
        }

        // -----------------------------------------------------
        // MODIFICAR OBJETO
        // -----------------------------------------------------
        marca.setNombre(nombre);
        marca.setDescripcion(descripcion);

        // -----------------------------------------------------
        // ACTUALIZAR
        // -----------------------------------------------------
        if (marcaDao.actualizar(marca)) {

            return ResultadoOperacion.ok(
                    "Marca actualizada correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo actualizar la marca."
        );
    }

    // =========================================================
    // ACTIVAR MARCA
    // =========================================================
    public ResultadoOperacion activarMarca(int idMarca) {

        Marca marca
                = marcaDao.buscarPorId(idMarca);

        if (marca == null) {

            return ResultadoOperacion.error(
                    "La marca no existe."
            );
        }

        if (marca.isActivo()) {

            return ResultadoOperacion.error(
                    "La marca ya se encuentra activa."
            );
        }

        if (marcaDao.cambiarEstado(
                idMarca,
                true)) {

            return ResultadoOperacion.ok(
                    "Marca activada correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo activar la marca."
        );
    }

    // =========================================================
    // DESACTIVAR MARCA
    // =========================================================
    public ResultadoOperacion desactivarMarca(
            int idMarca) {

        Marca marca
                = marcaDao.buscarPorId(idMarca);

        if (marca == null) {

            return ResultadoOperacion.error(
                    "La marca no existe."
            );
        }

        if (!marca.isActivo()) {

            return ResultadoOperacion.error(
                    "La marca ya se encuentra inactiva."
            );
        }

        if (marcaDao.cambiarEstado(
                idMarca,
                false)) {

            return ResultadoOperacion.ok(
                    "Marca desactivada correctamente."
            );
        }

        return ResultadoOperacion.error(
                "No se pudo desactivar la marca."
        );
    }

    // =========================================================
    // BUSCAR POR ID
    // =========================================================
    public Marca buscarPorId(int idMarca) {

        if (idMarca <= 0) {
            return null;
        }

        return marcaDao.buscarPorId(idMarca);
    }

    // =========================================================
    // BUSCAR POR NOMBRE
    // =========================================================
    public Marca buscarPorNombre(String nombre) {

        nombre = limpiar(nombre);

        if (nombre == null || nombre.isBlank()) {
            return null;
        }

        return marcaDao.buscarPorNombre(nombre);
    }

    // =========================================================
    // LISTAR TODAS
    // =========================================================
    public List<Marca> listarTodas() {

        return marcaDao.listarTodas();
    }

    // =========================================================
    // LISTAR ACTIVAS
    // =========================================================
    public List<Marca> listarActivas() {

        return marcaDao.listarActivas();
    }

    // =========================================================
    // LIMPIAR TEXTO
    // =========================================================
    private String limpiar(String texto) {

        if (texto == null) {
            return null;
        }

        texto = texto.trim();

        if (texto.isEmpty()) {
            return null;
        }

        return texto;
    }
}
