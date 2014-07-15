package im.aktive.aktive.manager;

/**
 * Created by hoangtran on 14/7/14.
 */

import java.security.MessageDigest;

import android.util.Base64;

public class ATDeviceInfoManager {
    private static String SALT = "jkSD2.f.FQK*@LFF.ASMF1,S;F";
    public static String getDeviceId(){
        {
            String deviceId = android.os.Build.SERIAL;
            String plainText = SALT + deviceId + SALT;
            try {
                byte[] bytesOfMessage = plainText.getBytes("UTF-8");
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] encodedBytes = Base64.encode(md.digest(bytesOfMessage), Base64.DEFAULT);
                String result = new String(encodedBytes);
                System.out.println("encodedBytes " + new String(encodedBytes));
                return result;
            } catch (Exception e) {
                return String.valueOf(plainText.hashCode());
            }
        }
    }
}

