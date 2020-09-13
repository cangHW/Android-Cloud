package com.proxy.service.utils.util;

import android.text.TextUtils;

import com.proxy.service.api.error.CloudApiError;
import com.proxy.service.api.utils.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: cangHX
 * on 2020/07/12  12:11
 */
public class StringUtils {

    private static final String INPUT_SYMBOL = "`~!@#$%^&*()_+-=[]{}|;':,./\\，。、；’【】、";

    private static final String LETTER_LOWERCASE = "[abcdefghijklmnopqrstuvwxyz]";
    private static final Pattern PATTERN_LETTER_LOWERCASE = Pattern.compile(LETTER_LOWERCASE);

    private static final String LETTER_UPPERCASE = "[ABCDEFGHIJKLMNOPQRSTUVWXYZ]";
    private static final Pattern PATTERN_LETTER_UPPERCASE = Pattern.compile(LETTER_UPPERCASE);

    private static final String LETTER = "[" + LETTER_LOWERCASE + LETTER_UPPERCASE + "]";
    private static final Pattern PATTERN_LETTER = Pattern.compile(LETTER);

    private static final String NUMBER = "[0123456789]";
    private static final Pattern PATTERN_NUMBER = Pattern.compile(NUMBER);

    private static final String EMOJI = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
    private static final Pattern PATTERN_EMOJI = Pattern.compile(EMOJI, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    /**
     * 自定义正则检测
     *
     * @param pattern : 正则模式
     * @param source  : 待检测对象
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-12 11:56
     */
    public static boolean checkMatcher(Pattern pattern, CharSequence source) {
        try {
            Matcher matcher = pattern.matcher(source);
            return matcher.find();
        } catch (Throwable throwable) {
            Logger.Error(CloudApiError.DATA_ERROR.setMsg("Regular expression format error. matcher : " + pattern.pattern()).build(), throwable);
        }
        return false;
    }

    /**
     * 检测是否包含大写英文
     *
     * @param source : 待检测对象
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 18:49
     */
    public static boolean checkLetterUpperCase(CharSequence source) {
        Matcher matcher = PATTERN_LETTER_UPPERCASE.matcher(source);
        return matcher.find();
    }

    /**
     * 检测是否包含小写英文
     *
     * @param source : 待检测对象
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 18:49
     */
    public static boolean checkLetterLowerCase(CharSequence source) {
        Matcher matcher = PATTERN_LETTER_LOWERCASE.matcher(source);
        return matcher.find();
    }

    /**
     * 检测是否包含英文
     *
     * @param source : 待检测对象
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 18:49
     */
    public static boolean checkLetter(CharSequence source) {
        Matcher matcher = PATTERN_LETTER.matcher(source);
        return matcher.find();
    }

    /**
     * 检测是否包含数字
     *
     * @param source : 待检测对象
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 18:49
     */
    public static boolean checkNumber(CharSequence source) {
        Matcher matcher = PATTERN_NUMBER.matcher(source);
        return matcher.find();
    }

    /**
     * 检测是否包含表情
     *
     * @param source : 待检测对象
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 18:46
     */
    public static boolean checkEmoji(CharSequence source) {
        //第一重检测,这个方法能检测大部分表情,但有极个别的检测不到,故加上下面的方法进行双重检测
        Matcher matcher = PATTERN_EMOJI.matcher(source);
        if (matcher.find()) {
            return true;
        }
        //第二重检测,增强检测力度,如果还有漏网之鱼，则进行第三次检测(暂不考虑第三次检测)
        return containsEmoji(source.toString());
    }

    /**
     * 检测 string 字符串中是否包含表情
     *
     * @param source : 待检测的字符串
     * @return true 包含，false 不包含
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 14:09
     */
    private static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测字符是否为表情
     *
     * @param codePoint : 待检测的字符
     * @return true 是表情，false 不是表情
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-07-10 14:10
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return codePoint == 0x0 || codePoint == 0x9 || codePoint == 0xA || codePoint == 0xD || codePoint >= 0x20 && codePoint <= 0xD7FF || codePoint >= 0xE000 && codePoint <= 0xFFFD;
    }

    /**
     * 16 进制字符串转为 10 进制 byte 数组
     *
     * @param hexStr : 待转换的字符串
     * @return 转换后的 10 进制 byte 数组
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:24 PM
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (TextUtils.isEmpty(hexStr)) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * byte 数组转为 16 进制字符串
     *
     * @param buffer : 待转换的 byte 数组
     * @return 转换后的字符串
     * @version: 1.0
     * @author: cangHX
     * @date: 2020/9/11 11:27 PM
     */
    public static String parseByte2HexStr(byte[] buffer) {
        if (buffer == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : buffer) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}
