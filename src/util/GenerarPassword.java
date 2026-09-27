package util;

import security.PasswordUtil;

public class GenerarPassword {

    public static void main(String[] args) {

        String password = "Admin1234";

        String hash = PasswordUtil.generarHash(password);

        System.out.println("==============================");
        System.out.println("PASSWORD ADMIN SERENA SOFT");
        System.out.println("==============================");

        System.out.println("Password: " + password);
        System.out.println("Hash:");
        System.out.println(hash);
    }
}