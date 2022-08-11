package top.csl.read.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import top.csl.read.vo.UserVO;

import java.util.Calendar;
import java.util.Date;

import static top.csl.read.common.constant.JwtConstant.EXPIRE_DAY;
import static top.csl.read.common.constant.JwtConstant.SECRET_KEY;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 12:24
 **/
public class JWTUtil {

    /**
     * 构建JWT对象
     * @param expire
     * @param user
     * @return
     */
    public static String buildJwt(Date expire, UserVO user) {
        String jwt = Jwts.builder()
                // 使用HS256加密算法
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                // 过期时间
                .setExpiration(expire)
                .claim("loginName",user.getLoginName())
                .claim("nickName",user.getNickName())
                .claim("phoneNumber",user.getPhoneNumber())
                .claim("headImgUrl",user.getHeadImgUrl())
                .claim("uuid",user.getUuid())
                .claim("id",user.getId())
                .compact();
        return jwt;
    }


    public static void main(String[] args) {
        UserVO userVO = new UserVO();
        userVO.setHeadImgUrl("1");
        userVO.setId(1);
        userVO.setLoginName("csl");
        userVO.setNickName("csl");
        userVO.setPhoneNumber("123");
        userVO.setUuid("456");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, EXPIRE_DAY);
        String s = buildJwt(calendar.getTime(), userVO);
        System.out.println(s);
    }
}
