package top.csl.read.vo;

import lombok.Data;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 13:12
 **/
@Data
public class AuthVO {
    private String token;
    private UserVO user;
}
