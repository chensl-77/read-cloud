package top.csl.read.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;
import java.util.UUID;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 12:53
 **/
public class UserUtil {

    /**
     * 获取用户盐值，用于加密用户密码
     *
     * @param
     * @return
     */
    public static String getSalt(String loginName){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=,./<>?".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            stringBuilder.append(aChar);
        }
        stringBuilder.append(loginName);
        return stringBuilder.toString();
    }

    /**
     * 获取用户加密密码
     *
     * @param loginName
     * @param password
     * @return
     */
    public static String getUserEncryptPassword(String loginName, String password) {
        String pwdAndSalt = password + getSalt(loginName);
        return DigestUtils.md5Hex(pwdAndSalt);
    }

    public static void main(String[] args) {
        System.out.println(getSalt("admin"));
        String admin = getUserEncryptPassword("admin", "111");
        System.out.println(admin);
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
    }
}

