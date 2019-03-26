package com.dysen.common_library.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @package com.zsdk.exchange.util
 * @email dy.sen@qq.com
 * created by dysen on 2019/3/4 - 18:20
 * @info
 */
public class PwdUitls {
    public static int getPwdLevel(String pwd) {
        int level = 0, index = 1;
        System.out.println(pwd.length()+"====="+pwd);
        if (pwd.length() >= 8 && pwd.length() <= 20) {
            //正则表达式验证符合要求的
            if (isMatches(pwd,"[0-9]")) level++; //数字 20
            if (isMatches(pwd,"[a-z]")) level++; //小写 40
            if (isMatches(pwd,"[A-Z]")) level++; //大写 60
            if (isSpecialChar(pwd)) level++; //特殊字符 80
            System.out.println(
                    isMatches(pwd,"[0-9]")+"==="+
                    isMatches(pwd,"[a-z]")+"==="+
                    isMatches(pwd,"[A-Z]")+"==="+
                    isSpecialChar(pwd)+"==="+
                       (level > 1 && pwd.length() > 12)

            );
            if (level > 1 && pwd.length() > 12) level++;//超过12位并且两种组合以上 100
        }
        return level;
    }


    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
    public static boolean isMatches(String str, String regEx) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

        public static void main(String args[]){
        System.out.println(getPwdLevel("1^abcqwevhrryyA"));
        System.out.println(getPwdLevel("1abcqwevhr/ryyA".replace("/", "")));
    }
}
