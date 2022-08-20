package top.csl.read.common.utils;

import cn.hutool.core.util.BooleanUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author: csl
 * @DateTime: 2022/8/12 9:58
 **/
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 当前时间距离第二天凌晨的毫秒数
     *
     * @return 返回值单位为[ms:毫秒]
     */
    public  Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis());
    }

    public  boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    public  void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    //起始时间戳
    private static Long startStamp = 1656460800L;


    public Long createRedisID(String key){
        long nowStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long stamp = nowStamp - startStamp;
        DateFormat dateFormat = new SimpleDateFormat("yy:MM:dd");
        Date date = new Date();
        String dateStr = dateFormat.format(date);
        long increment = stringRedisTemplate.opsForValue().increment(":increment:" + key + ":" + dateStr);
        return stamp << 32 | increment;
    }

    /**
     * 开始时间戳
     */
    private static final long BEGIN_TIMESTAMP = 1640995200L;
    /**
     * 序列号的位数
     */
    private static final int COUNT_BITS = 32;

    /**
     * 生成唯一ID
     * @param keyPrefix
     * @return
     */
    public long nextId(String keyPrefix) {
        // 1.生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

        // 2.生成序列号
        // 2.1.获取当前日期，精确到天
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        // 2.2.自增长
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);

        // 3.拼接并返回
        return timestamp << COUNT_BITS | count;
    }

    /**
     * 判断是否存在该key
     * @param key
     * @return
     */
    public boolean isExistKey(String key) {
        String res = stringRedisTemplate.opsForValue().get(key);
        if (res!=null){
            return true;
        }
        return false;
    }

    /**
     * 设置缓存
     * @param key
     * @param value
     */
    public void setKey(String key,String value,Long ex) {
        stringRedisTemplate.opsForValue().set(key,value, Duration.ofMillis(ex));
    }

    /**
     * 自增长
     * @param key
     * @param i
     */
    public void incr(String key, int i) {
        stringRedisTemplate.opsForValue().increment(key,i);
    }
}
