package Dialogos;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/** Conversión estricta de importes para caja (máximo dos decimales). */
public final class ImporteCaja {
    private ImporteCaja() {}

    public static BigDecimal parsear(String valor) {
        if (valor == null) return null;
        String s = valor.trim().replace("$", "").replace("\u00a0", "").replace(" ", "");
        if (s.isEmpty()) return null;
        // 1.234,56 / 1,234.56: separadores de miles y decimales explícitos.
        if (s.matches("\\d{1,3}(\\.\\d{3})+,\\d{1,2}")) s = s.replace(".", "").replace(',', '.');
        else if (s.matches("\\d{1,3}(,\\d{3})+\\.\\d{1,2}")) s = s.replace(",", "");
        else if (s.matches("\\d+[,.]\\d{1,2}")) s = s.replace(',', '.');
        else if (s.matches("\\d{1,3}(\\.\\d{3})+")) s = s.replace(".", "");
        else if (!s.matches("\\d+")) return null;
        try {
            BigDecimal n = new BigDecimal(s);
            return n.scale() <= 2 && n.signum() >= 0 ? n.setScale(2) : null;
        } catch (NumberFormatException ex) { return null; }
    }
}
