/*
 * expart.java
 * 2.4.3
 * Класс описывает единицу математического выражения - матобъект.
 */
package com.grevzin.expression;

import com.grevzin.util.*;


class expart {

     private Real value = new Real(); // Cсылка на численное значение матобъекта.
     private int opcode; // Операционный код матобъекта.
     private int priority; // Приоритет выполнения матобъекта.
     private String sval = new String();     // Строковое предствление значения матобъекта.

     /*
      * Функция вернет true, если данный матобъект - оператор.
      */
     public boolean isOperator() {
          if (func.fits(cnst.OPCODE_OPER_MIN, opcode, cnst.OPCODE_MAX)) {
               return true;
          } else {
               return false;
          }
     }

     /*
      * Функция создает новый матобъект, попутно определяя его тип.
      * Выставляет КОП матобъекта, его приоритет и значение.
      */
     public expart(int type, String string, int priority) throws error {
          switch (type) {
               case cnst.NUMBER: {
                    if (!(chartools.check_str_cons(string, cnst.SY_NUM))) {
                         throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                    }
                    value = new Real(string);
                    opcode = cnst.NUMBER;
                    priority = -1;
                    break;
               }
               case cnst.OPERATOR: {
                    if ((string.length() == 1) && (chartools.gettype(string.charAt(0)) == cnst.SY_OPE)) {
                         switch (string.charAt(0)) {
                              case '+': {
                                   opcode = cnst.PLUS;
                                   this.priority = priority + cnst.PR_PLUS_MINUS;
                                   break;
                              }
                              case '-': {
                                   opcode = cnst.MINUS;
                                   this.priority = priority + cnst.PR_PLUS_MINUS;
                                   break;
                              }
                              case '*': {
                                   opcode = cnst.MULTIPLY;
                                   this.priority = priority + cnst.PR_MULT_DIV;
                                   break;
                              }
                              case '/': {
                                   opcode = cnst.DIVIDE;
                                   this.priority = priority + cnst.PR_MULT_DIV;
                                   break;
                              }
                              case '^': {
                                   opcode = cnst.POWER;
                                   this.priority = priority + cnst.PR_POWER;
                                   break;
                              }
                              case '%': {
                                   opcode = cnst.REMAINED;
                                   this.priority = priority + cnst.PR_FUNCTION;
                                   break;
                              }
                              case ':': {
                                   opcode = cnst.ZDIV;
                                   this.priority = priority + cnst.PR_FUNCTION;
                                   break;
                              }
                              case '\u221a': {
                                   opcode = cnst.SQRT;
                                   this.priority = priority + cnst.PR_FUNCTION;
                                   break;
                              }
                              case '\u221b': {
                                   opcode = cnst.TDRT;
                                   this.priority = priority + cnst.PR_FUNCTION;
                                   break;
                              }
                              case '\u221c': {
                                   opcode = cnst.FTRT;
                                   this.priority = priority + cnst.PR_FUNCTION;
                                   break;
                              }

                         }
                    }
                    break;
               }
               case cnst.UNDEF_ABC: {
                    opcode = cnst.VARIABLE;
                    for (int i = 0; i < cnst.TRIGONOMETRIC_FUNCTION_NAMES.length; i++) {
                         if (cnst.TRIGONOMETRIC_FUNCTION_NAMES[i].equalsIgnoreCase(string)) {
                              opcode = i + cnst.OPCODE_TRIG_OFFSET;
                              this.priority = priority + cnst.PR_FUNCTION;
                              break;
                         }
                    }

                    for (int i = 0; i < cnst.COMMON_FUNCTION_NAMES.length; i++) {
                         if (cnst.COMMON_FUNCTION_NAMES[i].equalsIgnoreCase(string)) {
                              opcode = i + cnst.OPCODE_COMMON_OFFSET;
                              this.priority = priority + cnst.PR_FUNCTION;
                              break;
                         }
                    }
                    if (opcode == cnst.VARIABLE) {
                         this.priority = priority + cnst.PR_VAR;
                         sval = string;
                    }
                    break;
               }

          }
     }

     /*
      * Возвращает приоритет матобъекта.
      */
     public int Priority() {
          return priority;
     }
     /*
      * Возвращает строковое представление матобъекта.
      */
     public String SValue() {
          return sval;
     }

     /*
      * Возвращает КОП матобъекта.
      */
     public int Opcode() {
          return opcode;
     }

     /*
      * Создает новый матобъект, представляющий число num.
      */
     public expart(Real num) {
          this.value = new Real();
          value.assign(num);
          opcode = cnst.NUMBER;
     }

     /*
      * Cоздаёт матобъект-"заглушку".
      */
     public expart() {
          opcode = cnst.NULL;
          priority = -1000;
     }

     /*
      * Возвращает ссылку на численное значение матобъекта.
      */
     public Real Value() {
          return value;
     }
     /*
      * Возвращает true, если маотобъект - оперетатор, функция  или переменная.
      */
     public boolean IsOperable() {
          if ((func.fits(cnst.OPCODE_OPER_MIN, opcode, cnst.OPCODE_MAX)) || (opcode == cnst.VARIABLE)) {
               return true;
          } else {
               return false;
          }
     }
}
