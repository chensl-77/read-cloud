package top.csl.read.common.utils;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式化工具
 * @Author: csl
 * @DateTime: 2022/8/12 12:26
 **/
@Component
public class DateUtil {


    public String dateFormatA(){
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        Date date = new Date();
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    public String dateFormatB(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        Date date = new Date();
        String dateStr = dateFormat.format(date);
        return dateStr;
    }
}
