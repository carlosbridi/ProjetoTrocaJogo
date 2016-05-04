package com.ce2apk.projetotrocajogo.Password;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by carlosbridi on 29/10/15.
 */
public class CryptPass {

    public static String Crype(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest cript = MessageDigest.getInstance("SHA-1");
        cript.reset();
        cript.update(password.getBytes("utf8"));
        return new String(byteToHex(cript.digest()));
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash){
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}
