package com.it.quanxianguanli.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class IdCardUtil {
    
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$");
    
    public static boolean isValid(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return false;
        }
        return ID_CARD_PATTERN.matcher(idCard).matches();
    }
    
    public static String extractGender(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return null;
        }
        int genderCode = Integer.parseInt(idCard.substring(16, 17));
        return genderCode % 2 == 0 ? "女" : "男";
    }
    
    public static LocalDate extractBirthDate(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return null;
        }
        String birthStr = idCard.substring(6, 14);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(birthStr, formatter);
    }
}

