package top.csl.read.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.csl.read.common.pojo.account.User;
import top.csl.read.common.result.ResultUtil;
import top.csl.read.config.SystemPropertiesConfig;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static top.csl.read.common.constant.JwtConstant.SECRET_KEY;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 17:03
 **/
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private SystemPropertiesConfig systemPropertiesConfig;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 白名单Path
        Set<String> whiteList = this.getWhiteList();
        String path = exchange.getRequest().getPath().toString();

        // 主页接口、图书接口正则匹配
        boolean indexMatch = Pattern.matches("/index[^\\s]*", path);
        boolean bookMatch = Pattern.matches("/book/[^\\s]*", path);

        // 白名单接口、开放接口放行
        if (bookMatch || indexMatch || whiteList.contains(path)) {
            return chain.filter(exchange);
        }
        String[] segments = path.split("/");
        if (!whiteList.contains(segments[1])) {
            // 认证
            String token = exchange.getRequest().getHeaders().getFirst("token");
            User user = validationToken(token);
            if (user != null) {
                // 追加请求头用户信息
                Consumer<HttpHeaders> httpHeaders = httpHeader -> {
                    httpHeader.set("userId", user.getId().toString());
                    httpHeader.set("nickName", user.getNickName());
                };
                ServerHttpRequest serverHttpRequest = exchange.getRequest()
                        .mutate()
                        .headers(httpHeaders)
                        .build();
                exchange.mutate().request(serverHttpRequest).build();
                return chain.filter(exchange);
            }

            // 认证过期、失败，均返回401
            ServerHttpResponse response = exchange.getResponse();
            byte[] bits = JSONObject.toJSONString(ResultUtil.custom(201, "失效的token")).getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bits);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 指定编码，否则在浏览器中会中文乱码
            response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }


    /**
     * 请求白名单
     *
     * @return
     */
    private Set<String> getWhiteList() {
        String whitelists = this.systemPropertiesConfig.getWhitelist();
        Set<String> whiteList = new HashSet<>();
        if (StringUtils.isEmpty(whitelists)) {
            return whiteList;
        }
        String[] whiteArray = whitelists.split(",");
        for (int i = 0; i < whiteArray.length; i++) {
            whiteList.add(whiteArray[i]);
        }
        return whiteList;
    }


    public User validationToken(String token) {
        if (token == null) {
            return null;
        }
        //解析JWT字符串中的数据，并进行最基础的验证
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        if (claims == null) {
            return null;
        }
        String redisRes = redisTemplate.opsForValue().get(token);
        if (org.apache.commons.lang3.StringUtils.isBlank(redisRes)) {
            return null;
        }
        User user = JSON.parseObject(redisRes, User.class);
        return user;
    }
}
