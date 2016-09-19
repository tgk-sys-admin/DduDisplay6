package lecip.co.jp.ddudisplay6;

import android.util.Log;

public class Util {
    private static String TAG = "Util";

    public static byte[] hex2bin(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int index = 0; index < bytes.length; index++) {
            try {
                bytes[index] = (byte) Integer.parseInt(hex.substring(index * 2, (index + 1) * 2), 16);
            } catch (NumberFormatException e) {
                // 何もしない
            }
        }
        return bytes;
    }

    public static String bin2hex(byte[] bin) {
        String str = new String();
        for (int index = 0; index < bin.length; index++) {
            try {
                str = String.format("%s%02x", str, bin[index]);
                if(0x03 == bin[index]){
                    Log.d(TAG, "footer");
                    break;
                }
            } catch (NumberFormatException e) {
                // 何もしない
            }
        }
        return str;
    }

    public static String bin2hex(byte[] bin, int size) {
        String str = new String();
        for (int index = 0; index < size; index++) {
            if (index < bin.length) {
                try {
                    str = String.format("%s%02x", str, bin[index]);
                } catch (NumberFormatException e) {
                    // 何もしない
                }
            } else {
                break;
            }
        }
        return str;
    }
}
