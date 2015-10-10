/*
 * expression.java.
 * v 2.4.3
 * Класс, содержищий воможности по вычислению математических выржаений. Ввод выражения
 * в строковой форме осуществляется функцией set, подсчет - eval, получение результата -
 * getAnwserP10XORW.
 */
package com.grevzin.expression;

import java.util.Hashtable;

import com.grevzin.util.*;

/**
 *
 * @author Kuka
 */
public final class expression {

     private boolean errored;                               // Булева переменная, содежит 1, если последнее выражения вычилсить не удалось.
     private int DRG;                                       // Переключатаель режима градусы/радианы.
     private int iterations;                                // Счетчик количетства матобъектов, сожержащихся в последнем выражении.
     private Real result = new Real();                      // Ссылку на библиотеченое представление последнего ответа.
     private StringBuffer input = new StringBuffer(300);    // Входная строка.
     private StringBuffer buffer = new StringBuffer(10);    // Строка-буфер (используется при разборе выражения).
     private Hashtable vars = new Hashtable(40);            // Ссылка на словарь переменных и их значений.
     private expart main_array[] = new expart[100];         // Главный массив матобъектов.
     private String msg = new String();                     // Строка, содержащая сообщение об ошибке.
     private boolean update_var = false;                    // Булева переменная, сожержащая 1, если требуется обновить
     // некоторую переменную после вычисления.
     private String vartoupd = "";                          // Строка, содержащая имя кандидата из предыдущего комментария.

     /*
      *    Функция возвращает ссылку на библиотечное предстваление значения выражения.
      */
     public Real extract_real_res() {
          return result;
     }

     /*
      *   Служебная функция, возвращающая ссылку на таблицу соответствия переменных их значениям.
      */
     public Hashtable extract_vars() {
          return vars;
     }

     /*
      *   Служебная внутренняя функция, задействованная в системе сохранения переменных кальулятора.
      *   Не документируется. Вызов извне класса невозможен.
      */
     private boolean isvrestricted(String name) {
          for (int i = 0; i < cnst.RESTRICTED_VARS.length; i++) {
               if (cnst.RESTRICTED_VARS[i].equals(name.toLowerCase())) {
                    return true;
               }
          }
          return false;
     }

     /*
      *   Служебная функция, возвращающая true, если выражение не было посчитано из-за ошибок.
      */
     public boolean err() {
          return errored;
     }

     /*
      *   Служебная функция, подготавливающая данный экземпляр класса к подсчёту нового выражения.
      */
     public void reset() {
          update_var = false;
          vartoupd = "";
          errored = false;
     }


     /*
      *   Функция, возвращающая цифробуквенное значение выражения. Значение будет
      *   предствлено в стандартном виде с rad числами после запятой, если
      *   количество символов в записи этого результата больше, чем width.
      */
     public String getAnwserP10XORW(int width, int rad) {
          if (errored) {
               return msg;
          }
          Real.NumberFormat fmt = new Real.NumberFormat();
          fmt.maxwidth = width;
          fmt.precision = rad;
          fmt.fse = Real.NumberFormat.FSE_NONE;
          StringBuffer s = new StringBuffer(result.toString(fmt));
          StringBuffer e = new StringBuffer(2);
          if ((chartools.contains(s, 'E')) || (chartools.contains(s, 'e'))) {
               for (int i = s.length() - 1; i > -1; i--) {
                    if (s.charAt(i) == 'E') {
                         e.reverse();
                         s.deleteCharAt(i);
                         break;
                    }
                    if (s.charAt(i) == 'e') {
                         e.reverse();
                         s.deleteCharAt(i);
                         break;
                    }
                    e.append(s.charAt(i));
                    s.deleteCharAt(i);
               }
               s.append(" * 10^(");
               s.append(e);
               s.append(")");
               return s.toString();
          }
          return s.toString();

     }

     /*
      *   Служебная внутренняя математическая функция.
      *   (Перевод числа из радианов в градусы.)
      */
     private Real tdeg(Real n) {
          n.mul(180);
          n.div(Real.PI);
          return n;
     }

     /*
      *   Служебная внутренняя математическая функция.
      *   (Перевод числа из градусов в радианы.)
      *   Вызов извне класса невозможен.
      */
     private Real trad(Real n) {
          n.mul(Real.PI);
          n.div(180);
          return n;
     }

     /*
      *   Функция, устанавливающая тип тригонометрических аргументов в градусы.
      */
     public void deg() {
          DRG = 0;
     }

     /*
      *   Функция, устанавливающая тип тригонометрических аргументов в градусы.
      */
     public void rad() {
          DRG = 1;
     }

     /*
      *   Функция, возвращающая установленный тип тригонометрических аргуметнов.
      */
     public int getDR() {
          return DRG;
     }

     /*
      *   Конструктор класса. Принимает в качестве параметра начальную установку
      *   типа тригонометрических аргументов.
      */
     public expression(int DRG) {
          this.DRG = DRG;

          vars.put("pi", Real.PI);

          vars.put("e", Real.E);
          vars.put("ans", Real.ZERO);

     }

     /*
      *   Функция, готовящая экземпляр класса к вычислению выражения, записанного в s.
      */
     public void set(String s) {
          input = new StringBuffer(s);
     }

     /*
      *   Внутренняя служебная функция. Разбирает выражение и делает приготовления для его вычисления,
      *   заполняя массив матобъектов.
      *   Вызов извне класса невозможен.
      */
     private void parse() throws error {
          int operator_priority = 0, l_priority = 0;
          iterations = 0;
          boolean last_real = false, first_char = true;

          // <editor-fold defaultstate="collapsed" desc="Обработка новых переменных.">
          if (input.charAt(0) == '$') {

               input.deleteCharAt(0);
               for (;;) {
                   
                    if (chartools.gettype(input.charAt(0)) != cnst.SY_ABC) {
                         if (buffer.length() == 0) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         } else if (input.charAt(0) == '=') {
                              input.deleteCharAt(0);
                              
                              update_var = true;
                              vartoupd = buffer.toString();
                              break;
                         }
                    }
                    buffer.append(input.charAt(0));
                    input.deleteCharAt(0);
               }
          }
          buffer.delete(0, buffer.length());// </editor-fold>
          for (;;) {
               if (operator_priority < 0) {
                    throw new error("ОШИБКА СКОБОК");
               }

               if (input.length() == 0) {
                    System.err.println("Inside. Buffer " + buffer.toString() + "; Input " + input.toString() + "; Iterations " + String.valueOf(iterations));
                    break;
               }
               buffer.append(input.charAt(0));

               switch (chartools.gettype(input.charAt(0))) {
                    case cnst.SY_SPC: {
                         input.deleteCharAt(0);
                         break;
                    }
                    case cnst.SY_DEL: {
                         // <editor-fold defaultstate="collapsed" desc="Обработка скобок.">
                         if (input.charAt(0) == '(') {
                              l_priority = operator_priority;
                              operator_priority += 100;
                         }
                         if (input.charAt(0) == ')') {

                              operator_priority -= 100;
                              l_priority = operator_priority;
                         }
                         input.deleteCharAt(0);// </editor-fold>
                         break;
                    }
                    case cnst.SY_OPE: {
                         // <editor-fold defaultstate="collapsed" desc="Обработка операторов.">
                         last_real = false;
                         if ((input.charAt(0) == '\u221A') && !first_char && iterations > 0) {
                              if (!main_array[iterations - 1].isOperator()) {
                                   main_array[iterations] = new expart(cnst.OPERATOR, "*", operator_priority);
                                   iterations++;
                                   main_array[iterations] = new expart(cnst.OPERATOR, "\u221A", operator_priority);
                                   iterations++;
                                   input.deleteCharAt(0);
                              } else if (iterations != 0 && input.charAt(0) == '\u221A') {
                                   if (main_array[iterations - 1].Opcode() == cnst.SQRT) {
                                        main_array[iterations] = new expart(cnst.OPERATOR, "\u221A", operator_priority + 1);
                                        iterations++;
                                        input.deleteCharAt(0);
                                   } else {
                                        main_array[iterations] = new expart(cnst.OPERATOR, buffer.toString(), operator_priority);
                                        input.deleteCharAt(0);
                                        iterations++;
                                   }
                              }
                         } else {
                              main_array[iterations] = new expart(cnst.OPERATOR, buffer.toString(), operator_priority);
                              input.deleteCharAt(0);
                              iterations++;
                         }
                         // </editor-fold>
                         break;
                    }
                    case cnst.SY_NUM: {
                         // <editor-fold defaultstate="collapsed" desc="Обрабокта числовых.">
                         if (last_real) {
                              main_array[iterations] = new expart(cnst.OPERATOR, "*", l_priority);
                              iterations++;
                              last_real = false;
                         }
                         if (buffer.charAt(0) == '.') {
                              buffer.insert(0, '0');
                         }

                         for (;;) {
                              if (input.length() > 1) {
                                   if (chartools.gettype(input.charAt(1)) != cnst.SY_NUM) {
                                        main_array[iterations] = new expart(cnst.NUMBER, buffer.toString(), operator_priority);
                                        iterations++;
                                        input.deleteCharAt(0);
                                        last_real = true;
                                        break;
                                   } else {
                                        input.deleteCharAt(0);
                                        buffer.append(input.charAt(0));
                                   }
                              } else {
                                   System.err.println("Inside. Buffer " + buffer.toString() + "; Input " + input.toString() + "; Iterations " + String.valueOf(iterations));
                                   main_array[iterations] = new expart(cnst.NUMBER, buffer.toString(), operator_priority);
                                   iterations++;
                                   input.deleteCharAt(0);
                                   last_real = true;
                                   break;
                              }
                              System.err.println("Inside. Buffer " + buffer.toString() + "; Input " + input.toString() + "; Iterations " + String.valueOf(iterations));
                         }// </editor-fold>
                         break;
                    }
                    case cnst.SY_ABC: {

                         // <editor-fold defaultstate="collapsed" desc="Обработка буквенных.">
                         if ((last_real)) {
                              main_array[iterations] = new expart(cnst.OPERATOR, "*", l_priority);
                              iterations++;
                              last_real = false;
                         }
                         for (;;) {
                              if (input.length() > 1) {
                                   if (chartools.gettype(input.charAt(1)) != cnst.SY_ABC) {
                                        main_array[iterations] = new expart(cnst.UNDEF_ABC, buffer.toString(), operator_priority);
                                        if (main_array[iterations].Opcode() == cnst.VARIABLE) {
                                             last_real = true;
                                        } else {
                                             last_real = false;
                                        }
                                        iterations++;
                                        input.deleteCharAt(0);

                                        break;
                                   } else {
                                        input.deleteCharAt(0);
                                        buffer.append(input.charAt(0));
                                   }
                              } else {
                                   System.err.println("Inside. Buffer " + buffer.toString() + "; Input " + input.toString() + "; Iterations " + String.valueOf(iterations));
                                   main_array[iterations] = new expart(cnst.UNDEF_ABC, buffer.toString(), operator_priority);
                                   if (main_array[iterations].Opcode() == cnst.VARIABLE) {
                                        last_real = true;
                                   } else {
                                        last_real = false;
                                   }
                                   iterations++;
                                   input.deleteCharAt(0);

                                   break;
                              }
                              System.err.println("Inside. Buffer " + buffer.toString() + "; Input " + input.toString() + "; Iterations " + String.valueOf(iterations));
                         }// </editor-fold>
                         break;
                    }
                    case cnst.SY_UNK: {
                         throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                    }
               }
               buffer.delete(0, buffer.length());
               first_char = false;

          }
     }

     /*
      *   Внутренняя служебная функция. Подсчитывает разобранное выражение.
      *   Вызов извне класса невозможен.
      */
     private void calc() throws error {
          int op_pos = -1,
                  rop_pos = -1,
                  lop_pos = -1,
                  operable_counter = 0,
                  eq_parts = 0,
                  max_priority = 0,
                  r_pos = -1;
          for (;;) {

               // <editor-fold defaultstate="collapsed" desc="Вычисления позиций.">
               for (int i = 0; i < iterations; i++) {
                    if ((main_array[i].Value().isNan()) || (main_array[i].Value().isInfinity())) {
                         throw new error("ОШИБКА ВЫЧИСЛЕНИЙ");
                    }
                    if (main_array[i].Opcode() == cnst.ERR) {
                         throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                    }
                    if (!(main_array[i].Opcode() == cnst.NULL)) {
                         r_pos = i;
                         eq_parts++;
                    }
                    if (main_array[i].IsOperable()) {
                         operable_counter++;
                    }
               }
               if (operable_counter == 0) {
                    if (eq_parts == 1) {
                         result.assign(main_array[r_pos].Value());
                         if (update_var) {
                              overwritevar(vartoupd, new Real(result));
                         }
                         break;
                    } else {
                         throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                    }
               }
               if (operable_counter == 1) {
                    for (int i = 0; i < iterations; i++) {
                         if (main_array[i].IsOperable()) {
                              op_pos = i;
                              break;
                         }
                    }
               } else {
                    for (int i = 0; i < iterations; i++) {
                         if ((main_array[i].IsOperable()) && (max_priority < main_array[i].Priority())) {
                              max_priority = main_array[i].Priority();
                              op_pos = i;
                         }

                    }
               }
               max_priority = 0;
               lop_pos = rop_pos = -1;
               for (int i = op_pos - 1; i > -1; i--) {
                    if (main_array[i].IsOperable()) {
                         break;
                    }
                    if ((i == 0) && (main_array[i].Opcode() != cnst.NUMBER)) {
                         break;
                    }
                    if (main_array[i].Opcode() == cnst.NUMBER) {
                         lop_pos = i;
                    }
               }
               for (int i = op_pos + 1; i < iterations; i++) {
                    if (main_array[i].IsOperable()) {
                         break;
                    }
                    if (main_array[i].Opcode() == cnst.NUMBER) {
                         rop_pos = i;
                    }
               }// </editor-fold>

               // <editor-fold defaultstate="collapsed" desc="Вычисления результатов.">
               switch (main_array[op_pos].Opcode()) {

                    case cnst.VARIABLE: {
                         try {
                              main_array[op_pos] = new expart(getcvar(main_array[op_pos].SValue()));
                         } catch (error er) {
                              throw new error(er.getMessage());
                         }
                         break;
                    }
                    case cnst.PLUS: {
                         if ((lop_pos == -1) || (rop_pos == -1)) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }

                         Real calcbuffer = main_array[lop_pos].Value();
                         calcbuffer.add(main_array[rop_pos].Value());
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[lop_pos] = main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.MINUS: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         if (lop_pos == -1) {
                              Real calcbuffer = main_array[rop_pos].Value();
                              calcbuffer.neg();
                              main_array[op_pos] = new expart(calcbuffer);
                              main_array[rop_pos] = new expart();
                              break;
                         } else {
                              Real calcbuffer = main_array[lop_pos].Value();
                              calcbuffer.sub(main_array[rop_pos].Value());
                              main_array[op_pos] = new expart(calcbuffer);
                              main_array[lop_pos] = main_array[rop_pos] = new expart();
                              break;
                         }
                    }
                    case cnst.MULTIPLY: {
                         if ((lop_pos == -1) || (rop_pos == -1)) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[lop_pos].Value();
                         calcbuffer.mul(main_array[rop_pos].Value());
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[lop_pos] = main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.DIVIDE: {
                         if ((lop_pos == -1) || (rop_pos == -1)) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[lop_pos].Value();
                         calcbuffer.div(main_array[rop_pos].Value());
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[lop_pos] = main_array[rop_pos] = new expart();
                         break;
                    }

                    case cnst.POWER: {
                         if ((lop_pos == -1) || (rop_pos == -1)) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[lop_pos].Value();
                         calcbuffer.pow(main_array[rop_pos].Value());
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[lop_pos] = main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.COS: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         if (DRG == 0) {
                              calcbuffer = trad(calcbuffer);
                         }
                         calcbuffer.cos();
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;

                    }
                    case cnst.SIN: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         if (DRG == 0) {
                              calcbuffer = trad(calcbuffer);
                         }
                         calcbuffer.sin();
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;

                    }
                    case cnst.TAN: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         if (DRG == 0) {
                              calcbuffer = trad(calcbuffer);
                         }
                         calcbuffer.tan();
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.CTN: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         if (DRG == 0) {
                              calcbuffer = trad(calcbuffer);
                         }
                         calcbuffer.tan();
                         calcbuffer.recip();
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.ACOS: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();

                         calcbuffer.acos();
                         if (DRG == 0) {
                              calcbuffer = tdeg(calcbuffer);
                         }
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.ASIN: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer.asin();
                         if (DRG == 0) {
                              calcbuffer = tdeg(calcbuffer);
                         }
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.ATAN: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer.atan();
                         if (DRG == 0) {
                              calcbuffer = tdeg(calcbuffer);
                         }
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }

                    case cnst.ACTN: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer.atan();
                         calcbuffer.neg();
                         calcbuffer.add(Real.PI_2);
                         if (DRG == 0) {
                              calcbuffer = tdeg(calcbuffer);
                         }
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.LN: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         if (main_array[rop_pos].Value().isNegative()) {
                              throw new error("ОШИБКА ВЫЧИСЛЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer.ln();
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.LOG: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         if (main_array[rop_pos].Value().isNegative()) {
                              throw new error("ОШИБКА ВЫЧИСЛЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer.log10();
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.GAMMA: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer.gamma();
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;

                    }
                    case cnst.REMAINED: {
                         if ((lop_pos == -1) || (rop_pos == -1)) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         if (!(main_array[lop_pos].Value().isIntegral() && (main_array[rop_pos].Value().isIntegral()))) {
                              throw new error("ОШИБКА ВЫЧИСЛЕНИЯ");
                         }
                         Real calcbuffer = main_array[lop_pos].Value();
                         calcbuffer.mod(main_array[rop_pos].Value());
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[lop_pos] = main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.FACTOR: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer.fact();
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }

                    case cnst.ZDIV: {
                         if ((lop_pos == -1) || (rop_pos == -1)) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         if (!(main_array[lop_pos].Value().isIntegral() && (main_array[rop_pos].Value().isIntegral()))) {
                              throw new error("ОШИБКА ВЫЧИСЛЕНИЯ");
                         }
                         main_array[lop_pos].Value().abs();
                         main_array[rop_pos].Value().abs();
                         long divisable = main_array[lop_pos].Value().toLong(), divisor = main_array[rop_pos].Value().toLong(), res, rem = 0;
                         Real calcbuffer = main_array[lop_pos].Value();
                         calcbuffer.mod(main_array[lop_pos].Value());

                         rem = calcbuffer.toLong();
                         for (res = 1; res * divisor <= (divisable - calcbuffer.toLong()); res++);
                         res -= 1;
                         main_array[op_pos] = new expart(new Real(res));
                         main_array[lop_pos] = main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.ABS: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer.abs();
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;

                    }
                    case cnst.FLOOR: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer.floor();
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.TODEG: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer = tdeg(calcbuffer);
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.TORAD: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer = trad(calcbuffer);
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.CEIL: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer.ceil();
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.SQRT: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         if (calcbuffer.isNegative()) {
                              throw new error("ОШИБКА ВЫЧИСЛЕНИЯ");
                         }
                         calcbuffer.sqrt();
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.TDRT: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer.nroot(new Real(3));
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    case cnst.FTRT: {
                         if (rop_pos == -1) {
                              throw new error("ОШИБКА ВЫРАЖЕНИЯ");
                         }
                         Real calcbuffer = main_array[rop_pos].Value();
                         calcbuffer.nroot(new Real(4));
                         main_array[op_pos] = new expart(calcbuffer);
                         main_array[rop_pos] = new expart();
                         break;
                    }
                    // </editor-fold>
               }
               operable_counter = eq_parts = 0;
               lop_pos = rop_pos = op_pos = -1;

          }
     }

     /*
      *  Cлужебная функция из системы хранения переменных.
      *   Удаляет переменную name.
      *  
      */
     public void remvar(String name) throws error {
          if (isvrestricted(name)) {
               throw new error("Запрещается изменение переменной \"" + name + "\"!");
          }
          name = name.toLowerCase();
          vars.remove(name);
     }

     /*
      *   Cлужебная функция из системы хранения переменных. Вызов извне класса невозможен.
      *   Выровращает численное значение переменной name.
      */
     private Real getcvar(String name) throws error {
          name = name.toLowerCase();
          if (vars.containsKey(name)) {
               return (Real) vars.get(name);
          } else {
               throw new error(name + ": нет такой переменной!");
          }
     }

     /*
      *   Служебная функция из системы хранения переменных, возвращающая строковое значение переменной
      *   имени name. Генерирует исключение в случае несуществования такой переменной.
      */
     public String getvar(String name) throws error {
          name = name.toLowerCase();
          if (vars.containsKey(name)) {
               return String.valueOf(vars.get(name));
          } else {
               throw new error(name + ": нет такой переменной!");
          }
     }

     /*
      *   Внутренняя служебная функция из системы хранения переменных.
      *   Перезаписывает значение переменной s на v.
      *    Вызов извне класса невозможен.
      */
     private void overwritevar(String s, Real v) throws error {
          if (!(chartools.check_str_cons(s, cnst.SY_ABC))) {
               throw new error("Недопустимое имя переменной.");
          }
          if (isvrestricted(s)) {
               throw new error("Запрещается изменение переменной \"" + s + "\"!");
          }
          s = s.toLowerCase();

          vars.remove(s);
          vars.put(s, v);
     }

     /*
      *   Функция, отдающая команду на разбор и подсчет выражения.
      *   В случае неуспеха в msg запишется либо "ОШИБКА ВЫЧИСЛЕНИЯ"
      *   в случае вычислительной ошибки, ибо "ОШИБКА ВЫРАЖЕНИЯ"
      *   в случае неверно составленного выражения.
      */
     public void eval() {
          try {
               this.parse();
               this.calc();
               vars.put("ans", result);
          } catch (error e) {
               errored = true;
               msg = e.getMessage();
          }
     }
}
