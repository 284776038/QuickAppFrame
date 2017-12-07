package cn.richinfo.frame.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * author : Pan
 * time   : 2017/5/3
 * desc   : xxxx描述
 * version: 1.0
 * <p>
 * Copyright: Copyright (c) 2017
 * Company:XXXXXXXXXXXXXXXXXXXX
 */

public class TextUtil {
    private static final String PHONE_PATTERN =
            "^((13[4-9])|(147)|(15[0-2,7-9])|(178)|(18[2-4,7-8]))\\d{8}$";
    private static final String PASSWORD_NUMBER_PATTERN =
            "^\\d{6,14}$";
    private static final String PASSWORD_LETTER_PATTERN =
            "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,14}$";
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$]).{6,14}$";
    private static final String VERIFY_CODE_PATTERN = "((?=.*\\d).{6})";
    private static final String INVITE_CODE_PATTERN = "^[A-Z]{2}[0-9]{6}$";

    private static final String VERIFY_USER_NAME = "^[\\u4e00-\\u9fa5a-zA-Z][\\u4e00-\\u9fa5_a-zA-Z0-9]{0,12}[\\u4e00-\\u9fa5a-zA-Z]$";

    public static boolean isPhoneValid(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return Pattern.compile(PHONE_PATTERN).matcher(phone).matches();
        }
    }

    public static boolean isPasswordValid(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        } else {
            return Pattern.compile(PASSWORD_NUMBER_PATTERN).matcher(password).matches()
                    || Pattern.compile(PASSWORD_LETTER_PATTERN).matcher(password).matches()
                    || Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
        }
    }

    public static boolean isVerifyCodeValid(String verifyCode) {
        if (TextUtils.isEmpty(verifyCode)) {
            return false;
        } else {
            return Pattern.compile(VERIFY_CODE_PATTERN).matcher(verifyCode).matches();
        }
    }

    public static boolean isInviteyCodeValid(String inviteCode) {
        return Pattern.compile(INVITE_CODE_PATTERN).matcher(inviteCode).matches();
    }

    public static boolean verifyName(String name) {
        return Pattern.compile(VERIFY_USER_NAME).matcher(name).matches();
    }

    public static int getStringBytesLength(String str) {
        if (str == null || str.length() == 0){
            return 0;
        }
        char[] dest = new char[str.length()];
        str.getChars(0, str.length(), dest, 0);
        int number = 0;
        for (char c : dest) {
            if (c > 128) {
                number += 2;
            } else {
                number += 1;
            }
        }
        return number;
    }
}
