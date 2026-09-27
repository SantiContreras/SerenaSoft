package util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import model.UnidadMedida;

public final class FormateadorCantidad {

    // =========================================================
    // CONSTRUCTOR PRIVADO
    // =========================================================

    private FormateadorCantidad() {
    }


    // =========================================================
    // FORMATEAR CANTIDAD
    // =========================================================

    public static String formatear(
            BigDecimal cantidad,
            UnidadMedida unidad) {

        if (cantidad == null) {
            return "0";
        }

        // Si no conocemos la unidad,
        // eliminamos ceros innecesarios.
        if (unidad == null) {
            return formatearDecimal(cantidad);
        }

        // -----------------------------------------------------
        // UNIDADES QUE NO PERMITEN DECIMALES
        // -----------------------------------------------------

        if (!unidad.isPermiteDecimales()) {

            DecimalFormat formato =
                    new DecimalFormat(
                            "#,##0",
                            simbolosArgentinos()
                    );

            return formato.format(cantidad);
        }

        // -----------------------------------------------------
        // UNIDADES QUE PERMITEN DECIMALES
        // -----------------------------------------------------

        return formatearDecimal(cantidad);
    }


    // =========================================================
    // FORMATEAR CON UNIDAD
    // =========================================================

    public static String formatearConUnidad(
            BigDecimal cantidad,
            UnidadMedida unidad) {

        String valor =
                formatear(
                        cantidad,
                        unidad
                );

        if (unidad == null
                || unidad.getCodigo() == null
                || unidad.getCodigo().isBlank()) {

            return valor;
        }

        return valor
                + " "
                + unidad.getCodigo();
    }


    // =========================================================
    // FORMATO DECIMAL
    // =========================================================

    private static String formatearDecimal(
            BigDecimal cantidad) {

        DecimalFormat formato =
                new DecimalFormat(
                        "#,##0.###",
                        simbolosArgentinos()
                );

        return formato.format(cantidad);
    }


    // =========================================================
    // SÍMBOLOS
    // =========================================================

    private static DecimalFormatSymbols simbolosArgentinos() {

        return DecimalFormatSymbols.getInstance(
                new Locale("es", "AR")
        );
    }
}