package duan.utils;

import cn.dev33.satoken.secure.SaSecureUtil;

public class HashUtils {
    public static String GetHash(String password) {
        return SaSecureUtil.sha256(password);
    }
    public static boolean MatchHash(String password,String hash){
        System.out.println("1xxx"+SaSecureUtil.sha256(password));
        System.out.println("2xxx"+hash);
        System.out.println(SaSecureUtil.sha256(password).equals(hash));
        if(SaSecureUtil.sha256(password).equals(hash)){
            System.out.println(SaSecureUtil.sha256(password));
            return true;
        }
        System.out.println(SaSecureUtil.sha256(password));
        return false;
    }

    public static void main(String[] args) {
        System.out.println(SaSecureUtil.sha256("1"));
    }
}
