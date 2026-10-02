package services;

import Dao.ProveedorDao;
import Dao.ProveedorDao;
import model.Proveedor;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ProveedorService {

    // =========================================================
    // DAO
    // =========================================================

    private final ProveedorDao proveedorDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ProveedorService() {

        this.proveedorDao =
                new ProveedorDao();
    }


    // =========================================================
    // CREAR PROVEEDOR
    // =========================================================

    public ResultadoOperacion crear(
            Proveedor proveedor) {

        if (proveedor == null) {

            return ResultadoOperacion.error(
                    "El proveedor no puede ser nulo."
            );
        }


        // -----------------------------------------------------
        // LIMPIAR DATOS
        // -----------------------------------------------------

        limpiarProveedor(
                proveedor
        );


        // -----------------------------------------------------
        // VALIDAR
        // -----------------------------------------------------

        ResultadoOperacion validacion =
                validarProveedor(
                        proveedor
                );


        if (!validacion.isExitoso()) {

            return validacion;
        }


        try {

            // -------------------------------------------------
            // VALIDAR CUIT DUPLICADO
            // -------------------------------------------------

            if (proveedor.getCuit() != null
                    && proveedorDao.existeCuit(
                            proveedor.getCuit()
                    )) {

                return ResultadoOperacion.error(
                        "Ya existe un proveedor "
                        + "con el CUIT ingresado."
                );
            }


            // -------------------------------------------------
            // NUEVO PROVEEDOR ACTIVO
            // -------------------------------------------------

            proveedor.setActivo(
                    true
            );


            // -------------------------------------------------
            // GUARDAR
            // -------------------------------------------------

            int idProveedor =
                    proveedorDao.guardar(
                            proveedor
                    );


            if (idProveedor <= 0) {

                return ResultadoOperacion.error(
                        "No se pudo registrar "
                        + "el proveedor."
                );
            }


            return ResultadoOperacion.ok(
                    "Proveedor registrado correctamente. "
                    + "ID: "
                    + idProveedor
            );


        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error al registrar el proveedor: "
                    + ex.getMessage()
            );
        }
    }


    // =========================================================
    // ACTUALIZAR PROVEEDOR
    // =========================================================

    public ResultadoOperacion actualizar(
            Proveedor proveedor) {

        if (proveedor == null) {

            return ResultadoOperacion.error(
                    "El proveedor no puede ser nulo."
            );
        }


        if (proveedor.getIdProveedor() <= 0) {

            return ResultadoOperacion.error(
                    "El proveedor indicado no es válido."
            );
        }


        // -----------------------------------------------------
        // LIMPIAR DATOS
        // -----------------------------------------------------

        limpiarProveedor(
                proveedor
        );


        // -----------------------------------------------------
        // VALIDAR
        // -----------------------------------------------------

        ResultadoOperacion validacion =
                validarProveedor(
                        proveedor
                );


        if (!validacion.isExitoso()) {

            return validacion;
        }


        try {

            // -------------------------------------------------
            // COMPROBAR EXISTENCIA
            // -------------------------------------------------

            Proveedor existente =
                    proveedorDao.buscarPorId(
                            proveedor.getIdProveedor()
                    );


            if (existente == null) {

                return ResultadoOperacion.error(
                        "El proveedor no existe."
                );
            }


            // -------------------------------------------------
            // VALIDAR CUIT EN OTRO PROVEEDOR
            // -------------------------------------------------

            if (proveedor.getCuit() != null
                    && proveedorDao
                            .existeCuitEnOtroProveedor(
                                    proveedor.getCuit(),
                                    proveedor.getIdProveedor()
                            )) {

                return ResultadoOperacion.error(
                        "Ya existe otro proveedor "
                        + "con el CUIT ingresado."
                );
            }


            // -------------------------------------------------
            // ACTUALIZAR
            // -------------------------------------------------

            boolean actualizado =
                    proveedorDao.actualizar(
                            proveedor
                    );


            if (!actualizado) {

                return ResultadoOperacion.error(
                        "No se pudo actualizar "
                        + "el proveedor."
                );
            }


            return ResultadoOperacion.ok(
                    "Proveedor actualizado correctamente."
            );


        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error al actualizar el proveedor: "
                    + ex.getMessage()
            );
        }
    }


    // =========================================================
    // CAMBIAR ESTADO
    // =========================================================

    public ResultadoOperacion cambiarEstado(
            int idProveedor,
            boolean activo) {

        if (idProveedor <= 0) {

            return ResultadoOperacion.error(
                    "El proveedor indicado no es válido."
            );
        }


        try {

            Proveedor proveedor =
                    proveedorDao.buscarPorId(
                            idProveedor
                    );


            if (proveedor == null) {

                return ResultadoOperacion.error(
                        "El proveedor no existe."
                );
            }


            // -------------------------------------------------
            // SI YA TIENE ESE ESTADO, NO HACEMOS NADA
            // -------------------------------------------------

            if (proveedor.isActivo()
                    == activo) {

                if (activo) {

                    return ResultadoOperacion.error(
                            "El proveedor ya se encuentra activo."
                    );

                } else {

                    return ResultadoOperacion.error(
                            "El proveedor ya se encuentra inactivo."
                    );
                }
            }


            boolean actualizado =
                    proveedorDao.cambiarEstado(
                            idProveedor,
                            activo
                    );


            if (!actualizado) {

                return ResultadoOperacion.error(
                        "No se pudo cambiar el estado "
                        + "del proveedor."
                );
            }


            if (activo) {

                return ResultadoOperacion.ok(
                        "Proveedor activado correctamente."
                );

            } else {

                return ResultadoOperacion.ok(
                        "Proveedor desactivado correctamente."
                );
            }


        } catch (SQLException ex) {

            return ResultadoOperacion.error(
                    "Error al cambiar el estado "
                    + "del proveedor: "
                    + ex.getMessage()
            );
        }
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public Proveedor buscarPorId(
            int idProveedor) {

        if (idProveedor <= 0) {

            return null;
        }


        try {

            return proveedorDao.buscarPorId(
                    idProveedor
            );

        } catch (SQLException ex) {

            return null;
        }
    }


    // =========================================================
    // BUSCAR POR CUIT
    // =========================================================

    public Proveedor buscarPorCuit(
            String cuit) {

        cuit =
                limpiar(cuit);


        if (cuit == null) {

            return null;
        }


        try {

            return proveedorDao.buscarPorCuit(
                    cuit
            );

        } catch (SQLException ex) {

            return null;
        }
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<Proveedor> listarTodos() {

        try {

            return proveedorDao.listarTodos();

        } catch (SQLException ex) {

            return Collections.emptyList();
        }
    }


    // =========================================================
    // LISTAR ACTIVOS
    // =========================================================

    public List<Proveedor> listarActivos() {

        try {

            return proveedorDao.listarActivos();

        } catch (SQLException ex) {

            return Collections.emptyList();
        }
    }


    // =========================================================
    // BUSCAR
    // =========================================================

    public List<Proveedor> buscar(
            String texto) {

        texto =
                limpiar(texto);


        if (texto == null) {

            return listarTodos();
        }


        try {

            return proveedorDao.buscar(
                    texto
            );

        } catch (SQLException ex) {

            return Collections.emptyList();
        }
    }


    // =========================================================
    // BUSCAR ACTIVOS
    //
    // Especialmente útil para Compra / Entrada de mercadería.
    // =========================================================

    public List<Proveedor> buscarActivos(
            String texto) {

        texto =
                limpiar(texto);


        if (texto == null) {

            return listarActivos();
        }


        try {

            return proveedorDao.buscarActivos(
                    texto
            );

        } catch (SQLException ex) {

            return Collections.emptyList();
        }
    }


    // =========================================================
    // VALIDAR PROVEEDOR
    // =========================================================

    private ResultadoOperacion validarProveedor(
            Proveedor proveedor) {

        // -----------------------------------------------------
        // RAZÓN SOCIAL
        // -----------------------------------------------------

        if (proveedor.getRazonSocial() == null) {

            return ResultadoOperacion.error(
                    "La razón social es obligatoria."
            );
        }


        if (proveedor
                .getRazonSocial()
                .length() > 150) {

            return ResultadoOperacion.error(
                    "La razón social no puede superar "
                    + "los 150 caracteres."
            );
        }


        // -----------------------------------------------------
        // NOMBRE COMERCIAL
        // -----------------------------------------------------

        if (proveedor.getNombreComercial() != null
                && proveedor
                        .getNombreComercial()
                        .length() > 150) {

            return ResultadoOperacion.error(
                    "El nombre comercial no puede superar "
                    + "los 150 caracteres."
            );
        }


        // -----------------------------------------------------
        // CUIT
        //
        // En esta etapa validamos longitud.
        // Después podemos incorporar validación matemática
        // del dígito verificador si queremos.
        // -----------------------------------------------------

        if (proveedor.getCuit() != null
                && proveedor
                        .getCuit()
                        .length() > 20) {

            return ResultadoOperacion.error(
                    "El CUIT no puede superar "
                    + "los 20 caracteres."
            );
        }


        // -----------------------------------------------------
        // CONDICIÓN IVA
        // -----------------------------------------------------

        if (proveedor.getCondicionIva() != null
                && proveedor
                        .getCondicionIva()
                        .length() > 50) {

            return ResultadoOperacion.error(
                    "La condición de IVA no puede superar "
                    + "los 50 caracteres."
            );
        }


        // -----------------------------------------------------
        // DIRECCIÓN
        // -----------------------------------------------------

        if (proveedor.getDireccion() != null
                && proveedor
                        .getDireccion()
                        .length() > 200) {

            return ResultadoOperacion.error(
                    "La dirección no puede superar "
                    + "los 200 caracteres."
            );
        }


        // -----------------------------------------------------
        // LOCALIDAD
        // -----------------------------------------------------

        if (proveedor.getLocalidad() != null
                && proveedor
                        .getLocalidad()
                        .length() > 100) {

            return ResultadoOperacion.error(
                    "La localidad no puede superar "
                    + "los 100 caracteres."
            );
        }


        // -----------------------------------------------------
        // PROVINCIA
        // -----------------------------------------------------

        if (proveedor.getProvincia() != null
                && proveedor
                        .getProvincia()
                        .length() > 100) {

            return ResultadoOperacion.error(
                    "La provincia no puede superar "
                    + "los 100 caracteres."
            );
        }


        // -----------------------------------------------------
        // TELÉFONO
        // -----------------------------------------------------

        if (proveedor.getTelefono() != null
                && proveedor
                        .getTelefono()
                        .length() > 30) {

            return ResultadoOperacion.error(
                    "El teléfono no puede superar "
                    + "los 30 caracteres."
            );
        }


        // -----------------------------------------------------
        // EMAIL
        // -----------------------------------------------------

        if (proveedor.getEmail() != null) {

            if (proveedor
                    .getEmail()
                    .length() > 150) {

                return ResultadoOperacion.error(
                        "El email no puede superar "
                        + "los 150 caracteres."
                );
            }


            if (!emailValido(
                    proveedor.getEmail()
            )) {

                return ResultadoOperacion.error(
                        "El email ingresado no es válido."
                );
            }
        }


        // -----------------------------------------------------
        // PERSONA DE CONTACTO
        // -----------------------------------------------------

        if (proveedor.getPersonaContacto() != null
                && proveedor
                        .getPersonaContacto()
                        .length() > 150) {

            return ResultadoOperacion.error(
                    "La persona de contacto no puede superar "
                    + "los 150 caracteres."
            );
        }


        // -----------------------------------------------------
        // TELÉFONO CONTACTO
        // -----------------------------------------------------

        if (proveedor.getTelefonoContacto() != null
                && proveedor
                        .getTelefonoContacto()
                        .length() > 30) {

            return ResultadoOperacion.error(
                    "El teléfono de contacto no puede superar "
                    + "los 30 caracteres."
            );
        }


        return ResultadoOperacion.ok(
                "Datos válidos."
        );
    }


    // =========================================================
    // LIMPIAR PROVEEDOR
    //
    // Evita guardar:
    //
    // "  Distribuidora Norte  "
    //
    // y lo transforma en:
    //
    // "Distribuidora Norte"
    //
    // Los String vacíos pasan a null.
    // =========================================================

    private void limpiarProveedor(
            Proveedor proveedor) {

        proveedor.setRazonSocial(
                limpiar(
                        proveedor.getRazonSocial()
                )
        );

        proveedor.setNombreComercial(
                limpiar(
                        proveedor.getNombreComercial()
                )
        );

        proveedor.setCuit(
                limpiar(
                        proveedor.getCuit()
                )
        );

        proveedor.setCondicionIva(
                limpiar(
                        proveedor.getCondicionIva()
                )
        );

        proveedor.setDireccion(
                limpiar(
                        proveedor.getDireccion()
                )
        );

        proveedor.setLocalidad(
                limpiar(
                        proveedor.getLocalidad()
                )
        );

        proveedor.setProvincia(
                limpiar(
                        proveedor.getProvincia()
                )
        );

        proveedor.setTelefono(
                limpiar(
                        proveedor.getTelefono()
                )
        );

        proveedor.setEmail(
                limpiar(
                        proveedor.getEmail()
                )
        );

        proveedor.setPersonaContacto(
                limpiar(
                        proveedor.getPersonaContacto()
                )
        );

        proveedor.setTelefonoContacto(
                limpiar(
                        proveedor.getTelefonoContacto()
                )
        );

        proveedor.setObservaciones(
                limpiar(
                        proveedor.getObservaciones()
                )
        );
    }


    // =========================================================
    // VALIDACIÓN BÁSICA DE EMAIL
    // =========================================================

    private boolean emailValido(
            String email) {

        if (email == null) {

            return true;
        }


        int arroba =
                email.indexOf('@');


        int ultimoArroba =
                email.lastIndexOf('@');


        int punto =
                email.lastIndexOf('.');


        return arroba > 0
                && arroba == ultimoArroba
                && punto > arroba + 1
                && punto < email.length() - 1;
    }


    // =========================================================
    // LIMPIAR STRING
    // =========================================================

    private String limpiar(
            String texto) {

        if (texto == null) {

            return null;
        }


        String resultado =
                texto.trim();


        if (resultado.isEmpty()) {

            return null;
        }


        return resultado;
    }
}