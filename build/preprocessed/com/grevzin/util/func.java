/*
 * func.java
 * 2.4.4
 * Вспомогательный класс, сожержащий некоторые нужные в NGCalc функции.
 */
package com.grevzin.util;

/**
 *
 * @author Kuka
 */
public class func {
    /*
     * Значение целочилсенного деления a на b.
     */
    public static int zd(long a, long b) {
          int r = 0;
          for (; r * b < a; r++);
          return (r - 1);
    }
    /*
     * Проверяет, находится ли b между a и c.
     */
    public static boolean fits(int a, int b, int c) {
        if ((a < b) && (b < c)) {
            return true;
        } else {
            return false;
        }
    }
}
