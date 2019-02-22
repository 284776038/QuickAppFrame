package cn.bfy.frame.util;

/**
 * Description: 描述 </br>
 * Copyright: Copyright (c) 2017 </br>
 * Company:XXXXXXXXXXXXXXXXXXXX </br>
 * Email:284425176@qq.com </br>
 *
 * @author suma on 2017/5/9
 * @version 1.0
 */

public class StringUtils {

    public static boolean isNullOrEmpty(String str){
        if (str == null || str.length() == 0)
            return true;
        return false;
    }
}
