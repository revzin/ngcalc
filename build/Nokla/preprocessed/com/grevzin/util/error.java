/*
 * error.java
 * 2.4.3
 * Вспомогателльный класс, обеспечивающий генерацию исключений и передачу сообщений в NGCalc.
 */
package com.grevzin.util;

/**
 *
 * @author Kuka
 */
public class error extends Throwable {

    private String msg; // Сообщение об ошибке.
    /*
     * Конструктор класса.
     */
    public error(String text) {
        msg = text;
    }
    /*
     * Возвращает msg.
     */
    public String getMessage() {
        return msg;
    }
}
