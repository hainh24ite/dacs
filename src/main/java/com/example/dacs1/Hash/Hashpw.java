package com.example.dacs1.Hash;

import org.mindrot.jbcrypt.BCrypt;

public class Hashpw {
    public static String Hash(String password) {
        String hash= BCrypt.hashpw(password,BCrypt.gensalt());
        return hash;
    }

    public static boolean checkPassword(String password, String hash) {
        return BCrypt.checkpw(password,hash);
    }
}
