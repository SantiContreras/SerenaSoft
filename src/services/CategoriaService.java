package services;

import Dao.CategoriaDao;
import Dao.RubroDao;

import model.Categoria;
import model.Rubro;

import java.util.List;

public class CategoriaService {

    // =========================================================
    // DAO
    // =========================================================

    private final CategoriaDao categoriaDao;
    private final RubroDao rubroDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public CategoriaService() {

        this.categoriaDao
                = new CategoriaDao();

        this.rubroDao
                = new RubroDao();
    }


    // =========================================================
    // CREAR CATEGORÍA
    // =========================================================

    public ResultadoOperacion crearCategoria(
            Integer idRubro,
            String nombre,
            String descripcion
    ) {

        nombre = limpiar(nombre);
        descripcion = limpiar(descripcion);


        // -----------------------------------------------------
        // VALIDAR NOMBRE
        // -----------------------------------------------------

        if (nombre.isBlank()) {

            return ResultadoOperacion.error(
                    "El nombre de la categoría es obligatorio."
            );
        }


        if (nombre.length() > 100) {

            return ResultadoOperacion.error(
                    "El nombre de la categoría no puede "
                    + "superar los 100 caracteres."
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
        // DUPLICADOS
        // -----------------------------------------------------

        if (categoriaDao.existeNombre(nombre)) {

            return ResultadoOperacion.error(
                    "Ya existe una categoría con el nombre "
                    + nombre + "."
            );
        }


        // -----------------------------------------------------
        // BUSCAR RUBRO
        // -----------------------------------------------------

        Rubro rubro = null;


        if (idRubro != null) {

            if (idRubro <= 0) {

                return ResultadoOperacion.error(
                        "El rubro seleccionado no es válido."
                );
            }


            rubro = rubroDao.buscarPorId(
                    idRubro
            );


            if (rubro == null) {

                return ResultadoOperacion.error(
                        "El rubro seleccionado no existe."
                );
            }


            if (!rubro.isActivo()) {

                return ResultadoOperacion.error(
                        "El rubro seleccionado está inactivo."
                );
            }
        }


        // -----------------------------------------------------
        // CREAR OBJETO
        // -----------------------------------------------------

        Categoria categoria
                = new Categoria();


        categoria.setRubro(
                rubro
        );

        categoria.setNombre(
                nombre
        );

        categoria.setDescripcion(
                descripcion.isBlank()
                        ? null
                        : descripcion
        );

        categoria.setActivo(
                true
        );


        // -----------------------------------------------------
        // GUARDAR
        // -----------------------------------------------------

        boolean guardado
                = categoriaDao.guardar(
                        categoria
                );


        if (!guardado) {

            return ResultadoOperacion.error(
                    "No se pudo guardar la categoría."
            );
        }


        return ResultadoOperacion.ok(
                "Categoría creada correctamente."
        );
    }


    // =========================================================
    // EDITAR CATEGORÍA
    // =========================================================

    public ResultadoOperacion editarCategoria(
            int idCategoria,
            Integer idRubro,
            String nombre,
            String descripcion
    ) {

        if (idCategoria <= 0) {

            return ResultadoOperacion.error(
                    "La categoría seleccionada no es válida."
            );
        }


        Categoria categoria
                = categoriaDao.buscarPorId(
                        idCategoria
                );


        if (categoria == null) {

            return ResultadoOperacion.error(
                    "La categoría no existe."
            );
        }


        nombre = limpiar(nombre);
        descripcion = limpiar(descripcion);


        // -----------------------------------------------------
        // VALIDAR NOMBRE
        // -----------------------------------------------------

        if (nombre.isBlank()) {

            return ResultadoOperacion.error(
                    "El nombre de la categoría es obligatorio."
            );
        }


        if (nombre.length() > 100) {

            return ResultadoOperacion.error(
                    "El nombre de la categoría no puede "
                    + "superar los 100 caracteres."
            );
        }


        if (descripcion.length() > 250) {

            return ResultadoOperacion.error(
                    "La descripción no puede superar "
                    + "los 250 caracteres."
            );
        }


        // -----------------------------------------------------
        // VERIFICAR DUPLICADO
        // -----------------------------------------------------

        if (categoriaDao.existeNombreEnOtraCategoria(
                nombre,
                idCategoria
        )) {

            return ResultadoOperacion.error(
                    "Ya existe otra categoría con el nombre "
                    + nombre + "."
            );
        }


        // -----------------------------------------------------
        // RUBRO
        // -----------------------------------------------------

        Rubro rubro = null;


        if (idRubro != null) {

            if (idRubro <= 0) {

                return ResultadoOperacion.error(
                        "El rubro seleccionado no es válido."
                );
            }


            rubro = rubroDao.buscarPorId(
                    idRubro
            );


            if (rubro == null) {

                return ResultadoOperacion.error(
                        "El rubro seleccionado no existe."
                );
            }


            if (!rubro.isActivo()) {

                return ResultadoOperacion.error(
                        "El rubro seleccionado está inactivo."
                );
            }
        }


        // -----------------------------------------------------
        // MODIFICAR OBJETO
        // -----------------------------------------------------

        categoria.setRubro(
                rubro
        );

        categoria.setNombre(
                nombre
        );

        categoria.setDescripcion(
                descripcion.isBlank()
                        ? null
                        : descripcion
        );


        // -----------------------------------------------------
        // ACTUALIZAR
        // -----------------------------------------------------

        boolean actualizado
                = categoriaDao.actualizar(
                        categoria
                );


        if (!actualizado) {

            return ResultadoOperacion.error(
                    "No se pudo actualizar la categoría."
            );
        }


        return ResultadoOperacion.ok(
                "Categoría actualizada correctamente."
        );
    }


    // =========================================================
    // ACTIVAR
    // =========================================================

    public ResultadoOperacion activarCategoria(
            int idCategoria
    ) {

        Categoria categoria
                = categoriaDao.buscarPorId(
                        idCategoria
                );


        if (categoria == null) {

            return ResultadoOperacion.error(
                    "La categoría no existe."
            );
        }


        if (categoria.isActivo()) {

            return ResultadoOperacion.error(
                    "La categoría ya se encuentra activa."
            );
        }


        if (categoria.getRubro() != null
                && !categoria.getRubro().isActivo()) {

            return ResultadoOperacion.error(
                    "No se puede activar la categoría "
                    + "porque su rubro está inactivo."
            );
        }


        boolean actualizado
                = categoriaDao.cambiarEstado(
                        idCategoria,
                        true
                );


        if (!actualizado) {

            return ResultadoOperacion.error(
                    "No se pudo activar la categoría."
            );
        }


        return ResultadoOperacion.ok(
                "Categoría activada correctamente."
        );
    }


    // =========================================================
    // DESACTIVAR
    // =========================================================

    public ResultadoOperacion desactivarCategoria(
            int idCategoria
    ) {

        Categoria categoria
                = categoriaDao.buscarPorId(
                        idCategoria
                );


        if (categoria == null) {

            return ResultadoOperacion.error(
                    "La categoría no existe."
            );
        }


        if (!categoria.isActivo()) {

            return ResultadoOperacion.error(
                    "La categoría ya se encuentra inactiva."
            );
        }


        boolean actualizado
                = categoriaDao.cambiarEstado(
                        idCategoria,
                        false
                );


        if (!actualizado) {

            return ResultadoOperacion.error(
                    "No se pudo desactivar la categoría."
            );
        }


        return ResultadoOperacion.ok(
                "Categoría desactivada correctamente."
        );
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public Categoria buscarPorId(
            int idCategoria
    ) {

        if (idCategoria <= 0) {

            return null;
        }


        return categoriaDao.buscarPorId(
                idCategoria
        );
    }


    // =========================================================
    // BUSCAR POR NOMBRE
    // =========================================================

    public Categoria buscarPorNombre(
            String nombre
    ) {

        nombre = limpiar(nombre);


        if (nombre.isBlank()) {

            return null;
        }


        return categoriaDao.buscarPorNombre(
                nombre
        );
    }


    // =========================================================
    // LISTAR TODAS
    // =========================================================

    public List<Categoria> listarTodas() {

        return categoriaDao.listarTodos();
    }


    // =========================================================
    // LISTAR ACTIVAS
    // =========================================================

    public List<Categoria> listarActivas() {

        return categoriaDao.listarActivas();
    }


    // =========================================================
    // LISTAR POR RUBRO
    // =========================================================

    public List<Categoria> listarPorRubro(
            int idRubro
    ) {

        if (idRubro <= 0) {

            return List.of();
        }


        return categoriaDao.listarPorRubro(
                idRubro
        );
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