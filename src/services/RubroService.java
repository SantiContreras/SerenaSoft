package services;

import Dao.RubroDao;
import model.Rubro;

import java.util.List;


/**
 * Service encargado de las reglas de negocio
 * relacionadas con los rubros.
 */
public class RubroService {

    // =========================================================
    // DAO
    // =========================================================

    private final RubroDao rubroDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public RubroService() {

        this.rubroDao
                = new RubroDao();
    }


    // =========================================================
    // CREAR RUBRO
    // =========================================================

    public ResultadoOperacion crearRubro(
            String nombre,
            String descripcion
    ) {

        // -----------------------------------------------------
        // LIMPIAR DATOS
        // -----------------------------------------------------

        nombre = limpiar(nombre);
        descripcion = limpiar(descripcion);


        // -----------------------------------------------------
        // VALIDAR NOMBRE
        // -----------------------------------------------------

        if (nombre.isBlank()) {

            return ResultadoOperacion.error(
                    "El nombre del rubro es obligatorio."
            );
        }


        if (nombre.length() > 100) {

            return ResultadoOperacion.error(
                    "El nombre del rubro no puede superar "
                    + "los 100 caracteres."
            );
        }


        // -----------------------------------------------------
        // VALIDAR DESCRIPCIÓN
        // -----------------------------------------------------

        if (descripcion.length() > 250) {

            return ResultadoOperacion.error(
                    "La descripción no puede superar "
                    + "los 250 caracteres."
            );
        }


        // -----------------------------------------------------
        // VERIFICAR DUPLICADO
        // -----------------------------------------------------

        if (rubroDao.existeNombre(nombre)) {

            return ResultadoOperacion.error(
                    "Ya existe un rubro con el nombre "
                    + nombre + "."
            );
        }


        // -----------------------------------------------------
        // CREAR OBJETO
        // -----------------------------------------------------

        Rubro rubro
                = new Rubro();

        rubro.setNombre(nombre);

        rubro.setDescripcion(
                descripcion.isBlank()
                        ? null
                        : descripcion
        );

        rubro.setActivo(true);


        // -----------------------------------------------------
        // GUARDAR
        // -----------------------------------------------------

        boolean guardado
                = rubroDao.guardar(rubro);


        if (!guardado) {

            return ResultadoOperacion.error(
                    "No se pudo guardar el rubro."
            );
        }


        return ResultadoOperacion.ok(
                "Rubro creado correctamente."
        );
    }


    // =========================================================
    // EDITAR RUBRO
    // =========================================================

    public ResultadoOperacion editarRubro(
            int idRubro,
            String nombre,
            String descripcion
    ) {

        // -----------------------------------------------------
        // VALIDAR ID
        // -----------------------------------------------------

        if (idRubro <= 0) {

            return ResultadoOperacion.error(
                    "El rubro seleccionado no es válido."
            );
        }


        // -----------------------------------------------------
        // BUSCAR RUBRO
        // -----------------------------------------------------

        Rubro rubro
                = rubroDao.buscarPorId(idRubro);


        if (rubro == null) {

            return ResultadoOperacion.error(
                    "El rubro no existe."
            );
        }


        // -----------------------------------------------------
        // LIMPIAR DATOS
        // -----------------------------------------------------

        nombre = limpiar(nombre);
        descripcion = limpiar(descripcion);


        // -----------------------------------------------------
        // VALIDAR NOMBRE
        // -----------------------------------------------------

        if (nombre.isBlank()) {

            return ResultadoOperacion.error(
                    "El nombre del rubro es obligatorio."
            );
        }


        if (nombre.length() > 100) {

            return ResultadoOperacion.error(
                    "El nombre del rubro no puede superar "
                    + "los 100 caracteres."
            );
        }


        // -----------------------------------------------------
        // VALIDAR DESCRIPCIÓN
        // -----------------------------------------------------

        if (descripcion.length() > 250) {

            return ResultadoOperacion.error(
                    "La descripción no puede superar "
                    + "los 250 caracteres."
            );
        }


        // -----------------------------------------------------
        // VERIFICAR DUPLICADO
        // -----------------------------------------------------

        if (rubroDao.existeNombreEnOtroRubro(
                nombre,
                idRubro
        )) {

            return ResultadoOperacion.error(
                    "Ya existe otro rubro con el nombre "
                    + nombre + "."
            );
        }


        // -----------------------------------------------------
        // ACTUALIZAR OBJETO
        // -----------------------------------------------------

        rubro.setNombre(nombre);

        rubro.setDescripcion(
                descripcion.isBlank()
                        ? null
                        : descripcion
        );


        // -----------------------------------------------------
        // ACTUALIZAR BASE DE DATOS
        // -----------------------------------------------------

        boolean actualizado
                = rubroDao.actualizar(rubro);


        if (!actualizado) {

            return ResultadoOperacion.error(
                    "No se pudo actualizar el rubro."
            );
        }


        return ResultadoOperacion.ok(
                "Rubro actualizado correctamente."
        );
    }


    // =========================================================
    // ACTIVAR RUBRO
    // =========================================================

    public ResultadoOperacion activarRubro(
            int idRubro
    ) {

        Rubro rubro
                = rubroDao.buscarPorId(idRubro);


        if (rubro == null) {

            return ResultadoOperacion.error(
                    "El rubro no existe."
            );
        }


        if (rubro.isActivo()) {

            return ResultadoOperacion.error(
                    "El rubro ya se encuentra activo."
            );
        }


        boolean actualizado
                = rubroDao.cambiarEstado(
                        idRubro,
                        true
                );


        if (!actualizado) {

            return ResultadoOperacion.error(
                    "No se pudo activar el rubro."
            );
        }


        return ResultadoOperacion.ok(
                "Rubro activado correctamente."
        );
    }


    // =========================================================
    // DESACTIVAR RUBRO
    // =========================================================

    public ResultadoOperacion desactivarRubro(
            int idRubro
    ) {

        Rubro rubro
                = rubroDao.buscarPorId(idRubro);


        if (rubro == null) {

            return ResultadoOperacion.error(
                    "El rubro no existe."
            );
        }


        if (!rubro.isActivo()) {

            return ResultadoOperacion.error(
                    "El rubro ya se encuentra inactivo."
            );
        }


        boolean actualizado
                = rubroDao.cambiarEstado(
                        idRubro,
                        false
                );


        if (!actualizado) {

            return ResultadoOperacion.error(
                    "No se pudo desactivar el rubro."
            );
        }


        return ResultadoOperacion.ok(
                "Rubro desactivado correctamente."
        );
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public Rubro buscarPorId(
            int idRubro
    ) {

        if (idRubro <= 0) {

            return null;
        }

        return rubroDao.buscarPorId(
                idRubro
        );
    }


    // =========================================================
    // BUSCAR POR NOMBRE
    // =========================================================

    public Rubro buscarPorNombre(
            String nombre
    ) {

        nombre = limpiar(nombre);


        if (nombre.isBlank()) {

            return null;
        }


        return rubroDao.buscarPorNombre(
                nombre
        );
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<Rubro> listarTodos() {

        return rubroDao.listarTodos();
    }


    // =========================================================
    // LISTAR ACTIVOS
    // =========================================================

    public List<Rubro> listarActivos() {

        return rubroDao.listarActivos();
    }


    // =========================================================
    // LIMPIAR TEXTO
    // =========================================================

    private String limpiar(
            String texto
    ) {

        if (texto == null) {

            return "";
        }


        return texto.trim();
    }
}