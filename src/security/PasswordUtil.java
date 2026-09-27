package security;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    private PasswordUtil() {
    }

    public static String generarHash(String password) {

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "La contraseña no puede estar vacía"
            );
        }

        return BCrypt.hashpw(
                password,
                BCrypt.gensalt(12)
        );
    }

    public static boolean verificar(
            String password,
            String hash) {

        if (password == null ||
            hash == null ||
            hash.isBlank()) {

            return false;
        }

        try {
            return BCrypt.checkpw(password, hash);

        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}