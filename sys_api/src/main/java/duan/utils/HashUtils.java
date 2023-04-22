package duan.utils;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;

public class HashUtils {
    public static String GetHash(String password) {
        return SaSecureUtil.sha256(password);
    }
    public static boolean MatchHash(String password,String hash){
        if(SaSecureUtil.sha256(password).equals(hash)){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(SaSecureUtil.sha256("123456"));
    }
}
