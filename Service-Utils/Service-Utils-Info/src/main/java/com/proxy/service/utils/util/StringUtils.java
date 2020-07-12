package com.proxy.service.utils.util;

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
    private static Pattern PATTERN_LETTER_LOWERCASE = Pattern.compile(LETTER_LOWERCASE);

    private static final String LETTER_UPPERCASE = "[ABCDEFGHIJKLMNOPQRSTUVWXYZ]";
    private static Pattern PATTERN_LETTER_UPPERCASE = Pattern.compile(LETTER_UPPERCASE);

    private static final String LETTER = "[" + LETTER_LOWERCASE + LETTER_UPPERCASE + "]";
    private static Pattern PATTERN_LETTER = Pattern.compile(LETTER);

    private static final String NUMBER = "[0123456789]";
    private static Pattern PATTERN_NUMBER = Pattern.compile(NUMBER);

    private static final String EMOJI = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
    private static Pattern PATTERN_EMOJI = Pattern.compile(EMOJI, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

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
        //第一重过滤,这个方法能过滤掉大部分表情,但有极个别的过滤不掉,故加上下面的方法进行双重过滤
        Matcher matcher = PATTERN_EMOJI.matcher(source);
        if (matcher.find()) {
            return true;
        }
        //第二重过滤,增强排查力度,如果还有漏网之鱼，则进行第三次过滤(暂不考虑第三次过滤)
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
}
