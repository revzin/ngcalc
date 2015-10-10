/*
 * chartools.java
 * Класс, содержащий статические методы для оппределения типа символов:
 * цифровые, включая десятичную точку, математические операторы, буквенные,
 * включая нижнее подчеркивание, а также скобки.
 */
package com.grevzin.expression;

public class chartools {
     /*
      * Массив алфавитных символов.
      */

     static final private char alphabeticals[] = {
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
          'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
          'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
          'А', 'а', 'Б', 'б', 'В', 'в', 'Г', 'г', 'Д', 'д', 'Е', 'е', 'Ё',
          'ё', 'Ж', 'ж', 'З', 'з', 'И', 'и', 'Й', 'й', 'К', 'к', 'Л', 'л',
          'М', 'м', 'Н', 'н', 'О', 'о', 'П', 'п', 'Р', 'р', 'С', 'с', 'Т',
          'т', 'У', 'у', 'Ф', 'ф', 'Х', 'х', 'Ц', 'ц', 'Ч', 'ч', 'Ш', 'ш',
          'Щ', 'щ', 'Ъ', 'ъ', 'Ы', 'ы', 'Ь', 'ь', 'Э', 'э', 'Ю', 'ю', 'Я',
          'я', '_'
     };
     /*
      * Массив сиволов математических операторов.
      */
     static final private char operators[] = {
          '+', '-', '*', '/', '^', '%', ':', '\u221A', // Квадратный корень.
     };
     /*
      * Массив символов, служащих для записи чисел.
      */
     static final private char numbers[] = {
          '0', '1', '2', '3', '4', '6', '5', '7', '8', '9', '.'
     };

     /*
      * Возвращает true, если с - буква.
      */
     static public boolean isAlphabetical(char c) {
          for (int i = 0; i < alphabeticals.length; i++) {
               if (c == alphabeticals[i]) {
                    return true;
               }
          }
          return false;
     }

     /*
      * Возвращает true, если с - буква.
      */
     static public boolean isOperator(char c) {
          for (int i = 0; i < operators.length; i++) {
               if (c == operators[i]) {
                    return true;
               }
          }
          return false;
     }

     /*
      * Возвращает true, если с - буква.
      */
     static public boolean isNumber(char c) {
          for (int i = 0; i < numbers.length; i++) {
               if (c == numbers[i]) {
                    return true;
               }
          }
          return false;
     }

     /*
      * Возвращает true, если с - буква.
      */
     static public boolean isDelimeter(char c) {
          switch (c) {
               case '(':
               case ')': {
                    return true;
               }
               default: {
                    return false;
               }
          }
     }

     /*
      * Возвращает true, если с - буква.
      */
     public static boolean isSpace(char c) {
          if (c == ' ') {
               return true;
          }
          return false;
     }

     /*
      * Возвращает true, если с - буква.
      */
     public static boolean contains(StringBuffer s, char c) {
          if (s.length() == 0) {
               return false;
          }
          for (int i = 0; i < s.length(); i++) {
               if (s.charAt(i) == c) {
                    return true;
               }
          }
          return false;
     }

     /*
      * Возвращает тип символа.
      */
     static public int gettype(char c) {
          if (isAlphabetical(c)) {
               return cnst.SY_ABC;
          }
          if (isNumber(c)) {
               return cnst.SY_NUM;
          }
          if ((isDelimeter(c))) {
               return cnst.SY_DEL;
          }
          if (isSpace(c)) {
               return cnst.SY_SPC;
          }
          if (isOperator(c)) {
               return cnst.SY_OPE;
          }
          return cnst.SY_UNK;
     }
     /*
      * Проверяет, принадлежат ли все символы в строке к типу type.
      */
     public static boolean check_str_cons(String string, int type) {
          for (int i = 0; i < string.length(); i++) {
               if (gettype(string.charAt(i)) != type) {
                    if (!((type == cnst.SY_NUM) && (string.charAt(i) == 'E'))) {
                         return false;
                    }
               }
          }
          return true;
     }
}
