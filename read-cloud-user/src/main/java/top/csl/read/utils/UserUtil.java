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
    public static String getSalt(String loginName) {
        String[] salts = {"sun","moon","star","sky","cloud","fog","rain","wind","rainbow"};
        int hashCode = loginName.hashCode() + 159;
        int mod = Math.abs( hashCode % 9 );
        return salts[mod];
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
        System.out.println(getSalt("csl"));
        String admin = getUserEncryptPassword("csl", "csl");
        System.out.println(admin);
    }
}

