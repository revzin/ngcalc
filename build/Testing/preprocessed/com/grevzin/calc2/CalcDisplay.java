/*
 *   CalcDipslay.java
 *   Класс CalcDisplay, содержащий процедуры ввода-вывода.
 *   Версия 2.4.3 - релиз.
 */
package com.grevzin.calc2;

import javax.microedition.lcdui.*;
import com.grevzin.util.*;
import com.grevzin.expression.*;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Enumeration;
import java.util.Calendar;

class Button {
     /*
      * Класс, описывающий виртуальную клавишу:
      */

     String contents = "";                                  // её содержимое,
     private boolean isHgltd = false, isPressed = false;    // её состяние (выделена, нажата),
     int opcode = dcnst.OPCODE_INSERT;                      // опкод.

     boolean isHG() {
          return isHgltd;
     }

     void Highlight() {
          isHgltd = true;
     }

     void Extinguish() {
          isHgltd = false;
     }

     void Press() {
          isPressed = (!(isPressed));
     }

     Button() {
          this.contents = "NULL";
          this.opcode = -1;
     }

     Button(String contents, int opcode) {
          this.contents = contents;
          this.opcode = opcode;
     }
}

public final class CalcDisplay extends Canvas {

     /*
      *  Этот внутренний класс отсчитывает время в децисекундах, прошедшее с запуска. (Таймер).
      *  Получение прощедщего времени осуществляется функциями get_seconds() и get_decisec().
      */
     private class dsec_stopwatch implements Runnable {

          Thread t = new Thread(this, "Display_decitimer");
          private long deciseconds = 0;

          dsec_stopwatch() {
               t.start();
          }

          public long get_seconds() {
               return this.deciseconds / 10;
          }

          public long get_decisec() {
               return this.deciseconds;
          }

          public void run() {
               for (;;) {
                    try {
                         Thread.sleep(100);
                    } catch (Throwable tr) {
                         tr.printStackTrace();
                    }
                    ++deciseconds;
               }
          }
     }

     /* Переменные, хранязие параметры дисплея, необходимые для отрисовки виртуальной клавиатуры. */
     /*
      * Телефонный экран разбивается на одинаковые кнопки и виртуальный экран калькулятора согласно определениям, данным в dcnst.java.
      * Все кнопки подразделяются на несколько слоёв согласно тем жэе определяниям. Виден и активен в один момент один слой.
      * Переключение осуществляется нажатием виртуальной кнопки "NL" или нажатием кнопки направления у края экарана.
      */
     private int nButtonH, // Высота одной кнопки.
             nButtonW, // Ширина одной кнопки.
             nHorNumButtons, // Количество кнопок по высоте.
             nVerNumButtons, // Количество кнопок по ширине.
             nScreenH, // Высота виртуального дисплея.
             nTdH, // Высота экрана телефона.
             nTdW, // Ширина экрана телефона.
             hbX, // Х-координата активной кнопки.
             hbY, // Y-координата активной кнопки.
             display_text_anchor_x, // X-координата точки отрисовки содержимого виртуального экрана.
             display_text_anchor_y, // Y-координата точки отрисовки содержимого виртуального экрана.
             exp_width;
     private StringBuffer display_c = new StringBuffer();                  // Строка, содержащая содерживмое вирутального дисплея.
     private boolean responds = false;                                     // Булева переменная, определющая способ реакции телефона
     // на нажатую кнопку.
     private Button[][][] bts;                                             // Массив виртуальных клавиш.
     private int current_layer;                                            // Номер текущего активного слоя.
     private dsec_stopwatch timer;
     javax.microedition.lcdui.Font nf;                                     // Шрифты трёх размеров для отрисовки текста.
     javax.microedition.lcdui.Font bf;
     javax.microedition.lcdui.Font sf;
     private boolean pointerOnLScreen = false, pointerOnRScreen = false;   // Булевы переменные, оперделяющие, на
     // какой половине экрана находится касание сенсорного экрана.
     // Если выражение выходит за край экрана, пользователь нажимает
     // в верхнем левом или правом углу для перемещенния по выражению.
     private expression eq = new expression(0);                            // Объект, содержащий вычислительные мощности.
     boolean hasans = false;                                               // Булева переменная, истинна в случае, если сейчас на экране
     // находится занчение выражения.
     private boolean exp_overlaps = false;                                 // Булева переменная, истинная, если содержимое
     // виртуального экрана по ширине больше ширины реального экрана.
     private ButtonScheme3D bs;                                            // Сслыка на текущую клавиатурную раскалдку.
     private Stack h = new Stack();                                        // Стек для ведения истории вычислений.
     int panelH = 0;                                                       // Высота панели контроля (для некоторых телефонов).
     private Command c_hist, c_vars, c_exit;                               // Сслыки на команды экрана, упревляемые J2ME.
     private int last_length = display_c.length();                         // Длина строки, содержащей содержимое вирутального дисплея.
     private long dsec_passed = 0, dsec_passing = 0;                       // Время, прощедшее с момента последнего опроса таймера (для
     // клавиатурного ввода).
     private static final long SWITCH_DUR_DECISEC = 7;                     // Предельный интервал между нажатиями, после которого
     // символ перестаёт заменяться на новый.
     String[] operator_switching_sequence = {"*", "-", "/", "^", "\u221A", "(", ")"};                   // Последовательность символов, вводимых клавишей "*".
     byte current_op_pnter = 0;

     /*
      * Возвращает состояние responds.
      */
     public boolean Responds() {
          return responds;
     }

     /*
      * Конструктор класса.
      */
     public CalcDisplay(Command c_hist, Command c_vars, Command c_exit) {

          this.timer = new dsec_stopwatch();


          this.setFullScreenMode(true);
          this.c_hist = c_hist;
          this.c_vars = c_vars;
          this.c_exit = c_exit;
          //#ifdef Special
//#           Calendar current_time = Calendar.getInstance();
          //#endif
          bs = dcnst.bs5;

          nf = javax.microedition.lcdui.Font.getFont(
                  0,
                  javax.microedition.lcdui.Font.STYLE_BOLD,
                  javax.microedition.lcdui.Font.SIZE_MEDIUM);
          bf = javax.microedition.lcdui.Font.getFont(
                  javax.microedition.lcdui.Font.FACE_MONOSPACE,
                  javax.microedition.lcdui.Font.STYLE_BOLD,
                  javax.microedition.lcdui.Font.SIZE_LARGE);
          sf = javax.microedition.lcdui.Font.getFont(
                  javax.microedition.lcdui.Font.FACE_MONOSPACE,
                  javax.microedition.lcdui.Font.STYLE_BOLD,
                  javax.microedition.lcdui.Font.SIZE_SMALL);
          short sh = 40;

          String platform = System.getProperty("microedition.platform");
          platform = platform.toLowerCase();

          current_layer = 0;
          nTdH = getHeight();
          nTdW = getWidth();
          nScreenH = sh;


          nButtonH = (nTdH - sh - panelH) / bs.getRow();
          nButtonW = (nTdW) / bs.getColumn();
          nHorNumButtons = bs.getColumn();
          nVerNumButtons = bs.getRow();
          bts = new Button[bs.getLay()][bs.getRow()][bs.getColumn()];
          for (int i = 0; i < bs.getLay(); i++) {
               for (int j = 0; j < bs.getRow(); j++) {
                    for (int k = 0; k < bs.getColumn(); k++) {
                         bts[i][j][k] = new Button(bs.get(i, j, k), bs.getOpcode(i, j, k));
                    }
               }
          }
          hbX = 0;
          hbY = 0;
          bts[current_layer][hbX][hbY].Highlight();
          display_text_anchor_x = nTdW / 2;
          display_text_anchor_y = (nScreenH / 2) - (nf.getBaselinePosition() / 2);

//#ifdef Special
//#           if (current_time.get(Calendar.DAY_OF_MONTH) == 19) {
//#                display_c = new StringBuffer("current_time 19ым, дружище моё!! Кто б думал, что мы вот так найдем друг друга... У-ра! Очень люблю тебя, дружище, создание моё чудное!");
//#           }
//#           if ((current_time.get(Calendar.DAY_OF_MONTH) == 9) && (current_time.get(Calendar.MONTH) == Calendar.DECEMBER)) {
//#                display_c = new StringBuffer("Такой чудный день! Сегодня родилось моя Наташенька... С днем рождения, моё единственное существо, С ДНЕМ РОЖДЕНИЯ, НАТАША, СОКРОВИЩЕ МОЁ!");
//#           }
//#endif
          exp_width = bf.stringWidth(display_c.toString());
          if (exp_width > nTdW - 2 * (bf.stringWidth("5") + 10)) {
               exp_overlaps = true;
          } else {
               exp_overlaps = false;
               display_text_anchor_x = nTdW / 2;
          }
          if (exp_overlaps) {
               display_text_anchor_x = bf.stringWidth(display_c.toString()) / 2;
          }
          SetOpen();
          repaint();
     }

     /*
      * Внутренние служебные функции, обрабатывающие нажатия на виртуальную кнопку 'MNU'.
      */
     private void SetOpen() {
          this.removeCommand(c_hist);
          this.removeCommand(c_vars);
          this.removeCommand(c_exit);
          this.setFullScreenMode(true);
     }

     private void SetMenu() {
          this.addCommand(c_hist);
          this.addCommand(c_vars);
          this.addCommand(c_exit);
          this.setFullScreenMode(false);
     }

     /*
      * Функция, дописывающая в виртуальный экран добавочное содержимое, соблюдая правила расстановки пробелов для удобночитаемости.
      */
     private void append_display(String s) {
          if (display_c.length() != 0) {
               char first = s.charAt(s.length() - 1),
                       last = display_c.charAt(display_c.length() - 1);
               if (last == '.' && !chartools.isNumber(first)) {
                    return;
               }
               if (first == '.' && !chartools.isNumber(last)) {
                    return;
               }
               if (first == '^' || last == '^') {
                    display_c.append(s);
                    return;
               }
               if (first == '\u221A' || last == '\u221A') {
                    display_c.append(s);
                    return;
               }
               if (chartools.isAlphabetical(last)) {
                    if (chartools.isAlphabetical(first) || chartools.isNumber(first)) {
                         display_c.append(" " + s);
                    } else {
                         display_c.append(s);
                    }
               } else if (chartools.isDelimeter(last)) {
                    if (last == '(' && first == ')') {
                         return;
                    } else if (chartools.isOperator(first)) {
                         display_c.append(" " + s);
                    } else {
                         display_c.append(s);
                    }
               } else if (chartools.isOperator(last)) {
                    if (last == '\u221A') {
                         display_c.append(s);
                    } else if (chartools.isDelimeter(first)) {
                         display_c.append(" " + s);
                    } else {
                         display_c.append(" " + s);
                    }
               } else if (chartools.isNumber(last)) {
                    if (first == '.' && last == '.') {
                         return;
                    } else if (chartools.isNumber(first)) {
                         display_c.append(s);
                    } else if (chartools.isDelimeter(first)) {
                         display_c.append(s);
                    } else if (first == '^') {
                         display_c.append(s);
                    } else {
                         display_c.append(" " + s);
                    }
               } else {
                    display_c.append(s);
               }
          } else {
               display_c.append(s);
          }

     }

     /*
      * Системная функция, обрабатывыающая собитие "зажатия" клавиши телефона.
      */
     protected void keyRepeated(int kc) {
          if (kc == dcnst.HrdwButtonCodes.RSK || kc == dcnst.HrdwButtonCodes.SE_CNCL) {
               if (hasans) {
                    hasans = false;
                    return;
               }
               this.display_bspace();
               repaint();
          }
     }

     /*
      * Системная функция, обрабатывающая событие нажатия на клавишу телефона.
      */
     protected void keyPressed(int keyCode) {
          boolean navbutton_pressed = false;
          boolean pos_reset = true;


          if (keyCode == dcnst.HrdwButtonCodes.RSK) {
               if (hasans) {
                    hasans = false;
                    return;
               }
               this.display_bspace();
          } else if (keyCode == dcnst.HrdwButtonCodes.JOY_LEFT) {
               pos_reset = false;
               bts[current_layer][hbX][hbY].Extinguish();
               if (exp_width > nTdW - 2 * (bf.stringWidth("5") + 10)) {
                    exp_overlaps = true;
               }
               hbY--;
               if (hbY == -1) {
                    hbY = 0;
                    pointerOnLScreen = true;
                    handleEvents();
                    navbutton_pressed = true;
               }
               if (responds) {
                    SetOpen();
                    setFullScreenMode(true);
                    responds = false;
               }
          } else if (keyCode == dcnst.HrdwButtonCodes.JOY_RIGHT) {
               pos_reset = false;
               if (exp_width > nTdW - 2 * (bf.stringWidth("5") + 10)) {
                    exp_overlaps = true;
               }
               bts[current_layer][hbX][hbY].Extinguish();
               hbY++;
               if (hbY == bs.getColumn()) {
                    hbY = bs.getColumn() - 1;
                    pointerOnRScreen = true;
                    handleEvents();
                    navbutton_pressed = true;
               }
               if (responds) {
                    SetOpen();
                    setFullScreenMode(true);
                    responds = false;
               }
          } else if (keyCode == dcnst.HrdwButtonCodes.JOY_DOWN) {
               pos_reset = false;
               bts[current_layer][hbX][hbY].Extinguish();
               hbX++;
               if (responds) {
                    SetOpen();
                    setFullScreenMode(true);
                    responds = false;
               }
               if (hbX == bs.getRow()) {
                    current_layer++;
                    if (current_layer == bts.length) {
                         current_layer = 0;
                    }

                    hbX = 0;
               }
          } else if (keyCode == dcnst.HrdwButtonCodes.JOY_UP) {
               pos_reset = false;
               bts[current_layer][hbX][hbY].Extinguish();
               hbX--;
               if (responds) {
                    SetOpen();
                    responds = false;

               }
               if (hbX == -1) {
                    current_layer--;
                    if (current_layer == -1) {
                         current_layer = bts.length - 1;
                    }

                    repaint();
                    hbX = bs.getRow() - 1;
               }
          } else if (keyCode == dcnst.HrdwButtonCodes.JOY_FIRE) {
               buttonPress(hbX, hbY);

          } else if (keyCode == dcnst.HrdwButtonCodes.SE_CNCL) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               this.display_bspace();
          } else if (keyCode == KEY_STAR) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               dsec_passing = timer.get_decisec();
               if (dsec_passing - dsec_passed < SWITCH_DUR_DECISEC && this.last_length == display_c.length()) {
                    if (this.current_op_pnter < this.operator_switching_sequence.length - 1) {
                         this.current_op_pnter++;
                    } else {
                         this.current_op_pnter = 0;
                    }
                    if (display_c.length() != 0) {
                         this.display_bspace();
                    }

               } else {
                    this.current_op_pnter = 0;
               }
               append_display(this.operator_switching_sequence[this.current_op_pnter]);
               dsec_passed = timer.get_decisec();
               this.last_length = display_c.length();
          } else if (keyCode == KEY_POUND) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               append_display(".");
          } else if (48 <= keyCode && keyCode <= 57) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               append_display(String.valueOf(new Integer(keyCode - 48)));
          } else if (keyCode == dcnst.HrdwButtonCodes.RSK) {
               if (hasans) {
                    hasans = false;
                    return;
               }
               this.display_bspace();
          } else if (keyCode == dcnst.HrdwButtonCodes.LSK) {
               if (hasans) {
                    hasans = false;
                    display_c.delete(0, display_c.length());
                    return;
               }
               if (display_c.toString().equals("")) {
                    return;
               }

               eq.set(display_c.toString());
               eq.eval();

               StringBuffer sbs = new StringBuffer(5);
               if (display_c.charAt(0) == '$') {
                    display_c.deleteCharAt(0);
                    int v = 0;
                    for (; v < display_c.length(); v++) {
                         if (display_c.charAt(v) == '=') {
                              display_c.setCharAt(v, ' ');
                              break;
                         } else {
                              sbs.append(display_c.charAt(v));
                         }
                    }
                    sbs.append(" = ");
                    display_c.delete(0, v + 1);
               }

               if (!(eq.err())) {
                    if (eq.getDR() == 0) {
                         h.push(new String("D: " + sbs.toString() + display_c + " == " + eq.getAnwserP10XORW(15, 7)));
                    } else {
                         h.push(new String("R: " + sbs.toString() + display_c + " == " + eq.getAnwserP10XORW(15, 7)));
                    }
               }
               display_c = new StringBuffer(" == " + eq.getAnwserP10XORW(15, 7));
               hasans = true;
               display_text_anchor_x = bf.stringWidth(display_c.toString()) / 2;


               repaint();
               eq.reset();

          }

          exp_width = bf.stringWidth(display_c.toString());
          if (exp_width > nTdW - 2 * (bf.stringWidth("5") + 10)) {
               exp_overlaps = true;
          } else {
               exp_overlaps = false;
               display_text_anchor_x = nTdW / 2;
          }
          if ((exp_overlaps) && (!(navbutton_pressed)) && (pos_reset)) {
               display_text_anchor_x = -(bf.stringWidth(display_c.toString()) / 2 - nTdW + bf.stringWidth("5") + 10);
          }
          bts[current_layer][hbX][hbY].Highlight();
          repaint();

     }

     /*
      * Системная функция, стирающая данные с вирутального дисплея по заданным правилам.
      * Например, названия функция стираются целиком, а цифры - по одной.
      */
     private void display_bspace() {
          if (display_c.length() == 0) {
               return;
          }
          if (display_c.length() == 1) {
               display_c.deleteCharAt(0);
               return;
          }
          int last_type;
          for (int i = display_c.length() - 1; i != 0; i--) {

               last_type = chartools.gettype(display_c.charAt(i));
               if (last_type != chartools.gettype(display_c.charAt(i - 1))) {

                    if (display_c.charAt(i - 1) == ' ') {
                         display_c.deleteCharAt(i);
                         continue;
                    } else {
                         display_c.deleteCharAt(i);
                         break;
                    }
               }
               if (display_c.charAt(i - 1) == '\u221A') {
                    display_c.deleteCharAt(i);
                    return;
               }
               if (display_c.charAt(i - 1) == '(' || display_c.charAt(i - 1) == ')') {
                    display_c.deleteCharAt(i);
                    return;
               }
               display_c.deleteCharAt(i);
               if (chartools.isNumber(display_c.charAt(i - 1)) || chartools.isOperator(display_c.charAt(i - 1))) {
                    break;
               }
          }
          if (display_c.length() == 1 && !chartools.isNumber(display_c.charAt(0))) {
               if (!chartools.isDelimeter(display_c.charAt(0)) && !chartools.isOperator(display_c.charAt(0))) {
                    display_c.deleteCharAt(0);
               }
          }
     }


     /*
      * Возвращает ссылку на стек строк с вычисленными выражениями.
      */
     public Stack getEqHS() {
          return h;
     }

     /*
      * Возвращает ссылку на словарь переменных, содержащихся в привязанном к дисплею объекте expression.
      */
     public Hashtable getEqVars() {
          return eq.extract_vars();
     }

     /*
      * Устанавливает содержимое виртуального экрана.
      */
     public void setDispCont(String s) {
          if (s == null) {
               return;
          }
          display_c = new StringBuffer(s);
     }

     /*
      * Функция дописывает в конец виртуального экрана строку без соблюдения правил расстановки пробелов.
      */
     public void appendDispCont(String s) {
          if (s == null) {
               return;
          }
          if (hasans) {
               hasans = false;
               display_c = new StringBuffer("");
          }
          display_c.append(s);
     }

     /*
      * Функция обрататывает нажатие на виртуальную экранную кнопку.
      */
     private void buttonPress(int i, int j) {
          bts[current_layer][i][j].Highlight();
          switch (bts[current_layer][i][j].opcode) {
               case dcnst.OPCODE_D_A: {
                    display_c.delete(0, display_c.length());
                    repaint();
                    return;
               }
               case dcnst.OPCODE_SWITCHLAYER: {
                    bts[current_layer][hbX][hbY].Extinguish();
                    current_layer++;
                    if (current_layer == bts.length) {
                         current_layer = 0;
                    }
                    if (responds) {
                         setFullScreenMode(true);
                         responds = false;
                         break;
                    }
                    bts[current_layer][hbX][hbY].Highlight();
                    repaint();
                    break;
               }

               case dcnst.OPCODE_INSERT: {
                    if (hasans) {
                         hasans = false;
                         display_c = new StringBuffer("");
                    }
                    append_display(bts[current_layer][i][j].contents);
                    if (exp_overlaps) {
                         display_text_anchor_x = -(bf.stringWidth(display_c.toString()) / 2 - nTdW + bf.stringWidth("5") + 10);
                    }
                    break;
               }
               case dcnst.OPCODE_D_R: {
                    if (eq.getDR() == 0) {
                         eq.rad();
                    } else {
                         eq.deg();
                    }
                    repaint();
                    break;
               }
               case dcnst.OPCODE_MENU: {
                    if (responds) {
                         SetOpen();
                         repaint();
                         serviceRepaints();
                         responds = false;
                    } else {
                         SetMenu();
                         repaint();
                         serviceRepaints();
                         responds = true;

                    }
                    break;
               }
               case dcnst.OPCODE_SPACE: {
                    if (hasans) {
                         hasans = false;
                         display_c = new StringBuffer("");
                    }
                    display_c.append(' ');
                    break;
               }
               case dcnst.OPCODE_EXEC: {
                    if (hasans) {
                         display_c.delete(0, display_c.length());
                         hasans = false;
                         break;
                    }
                    if (display_c.toString().equals("")) {
                         break;
                    }
                    eq.set(display_c.toString());
                    eq.eval();
                    StringBuffer sbs = new StringBuffer(5);
                    if (display_c.charAt(0) == '$') {
                         display_c.deleteCharAt(0);
                         int v = 0;
                         for (; v < display_c.length(); v++) {
                              if (display_c.charAt(v) == '=') {
                                   display_c.setCharAt(v, ' ');
                                   break;
                              } else {
                                   sbs.append(display_c.charAt(v));
                              }
                         }
                         sbs.append(" = ");
                         display_c.delete(0, v + 1);
                    }

                    if (!(eq.err())) {
                         if (eq.getDR() == 0) {
                              h.push(new String("D: " + sbs.toString() + display_c + " == " + eq.getAnwserP10XORW(15, 7)));
                         } else {
                              h.push(new String("R: " + sbs.toString() + display_c + " == " + eq.getAnwserP10XORW(15, 7)));
                         }
                    }
                    display_c = new StringBuffer(" == " + eq.getAnwserP10XORW(15, 7));
                    hasans = true;
                    display_text_anchor_x = bf.stringWidth(display_c.toString()) / 2;


                    repaint();
                    eq.reset();
                    break;
               }
               case dcnst.OPCODE_BACKSPACE: {
                    if (hasans) {
                         hasans = false;
                         display_c = new StringBuffer("");
                    }
                    this.display_bspace();
                    if (exp_overlaps) {
                         display_text_anchor_x = -(bf.stringWidth(display_c.toString()) / 2 - nTdW + bf.stringWidth("5") + 10);
                    }
                    break;
               }


          }
          exp_width = bf.stringWidth(display_c.toString());
          if (exp_width > nTdW - 2 * (bf.stringWidth("5") + 10)) {
               exp_overlaps = true;
          } else {
               exp_overlaps = false;
               display_text_anchor_x = nTdW / 2;
          }
          repaint();

     }

     /*
      * Функция удаляет переменную с именем name из объекта выражения.
      */
     public void deletevar(String name) throws error {
          eq.remvar(name);
     }

     /*
      * Фукнция вызывается после подготовки к реакции на действия пользователя.
      */
     private void handleEvents() {
          if (pointerOnLScreen) {
               if ((exp_overlaps) && (display_text_anchor_x < bf.stringWidth(display_c.toString()) / 2)) {
                    display_text_anchor_x += 20;
               }
               pointerOnRScreen = pointerOnLScreen = false;
               repaint();
               return;
          }
          if (pointerOnRScreen) {
               if ((exp_overlaps) && (display_text_anchor_x > (-(15 + bf.stringWidth(display_c.toString()) / 2 - nTdW)))) {
                    display_text_anchor_x -= 20;
               }
               pointerOnRScreen = pointerOnLScreen = false;
               repaint();
               return;
          }
          buttonPress(hbX, hbY);
          pointerOnRScreen = pointerOnLScreen = false;
     }

     /*
      * Функция вызывается, когда заканчивается нажатие на сенсорный экран.
      */
     protected void pointerReleased(int x, int y) {
          if ((x < 3) || (y < 0)) {
               return;
          }
          if (x > nTdW - 3) {
               return;
          }

          if (y > (nTdH - panelH - 3)) {
               return;
          }
          if (y < nScreenH + 3) {
               if (x > nTdW / 2) {
                    pointerOnRScreen = true;
               } else {
                    pointerOnLScreen = true;
               }
          } else {
               for (int i = 0; i < bts.length; i++) {
                    bts[i][hbX][hbY].Extinguish();
               }
               hbY = func.zd(x, nButtonW);
               hbX = func.zd(-nScreenH + y, nButtonH);
          }

          handleEvents();
     }

     /*
      * Функция осуществояет отрисовку данных на экране.
      */
     protected void paint(Graphics g) {
          int cury = 0 + nScreenH;
          int curx = 0;
          g.fillRect(0, 0, nTdW, nTdH);
          g.setColor(150, 150, 255);
          for (int i = 0; i < nHorNumButtons; i++) {
               for (int j = 0; j < nVerNumButtons; j++) {
                    if ((bts[current_layer][j][i].opcode == dcnst.OPCODE_MENU) && (Responds())) {
                         g.setColor(0, 255, 0);
                         g.fillRect(curx + 2, cury + 2, nButtonW - 2, nButtonH - 2);
                         g.setColor(150, 150, 255);
                    } else if (bts[current_layer][j][i].isHG()) {
                         g.setColor(255, 255, 0);
                         g.fillRect(curx + 2, cury + 2, nButtonW - 2, nButtonH - 2);
                         g.setColor(150, 150, 255);
                    } else if (bts[current_layer][j][i].opcode != dcnst.OPCODE_INSERT) {
                         g.setColor(0x00FF9999);
                         g.fillRect(curx + 2, cury + 2, nButtonW - 2, nButtonH - 2);
                         g.setColor(150, 150, 255);
                    } else {
                         g.fillRect(curx + 2, cury + 2, nButtonW - 2, nButtonH - 2);
                    }

                    cury += nButtonH;
               }
               curx += nButtonW;
               cury = 0 + nScreenH;
          }
          g.setColor(255, 200, 200);
          g.setFont(nf);
          for (int d = 0; d > - 3; d--) {
               cury = 0 + nScreenH;
               curx = 0;
               for (int i = 0; i < nHorNumButtons; i++) {
                    for (int j = 0; j < nVerNumButtons; j++) {
                         g.drawRect(curx + d, cury + d, nButtonW - d, nButtonH - d);
                         cury += nButtonH;
                    }
                    curx += nButtonW;
                    cury = 0 + nScreenH;
               }
          }

          cury = 0 + nScreenH;
          curx = 0;
          g.setColor(0x00000000);
          g.setFont(sf);

          for (int i = 0; i < nHorNumButtons; i++) {

               for (int j = 0; j < nVerNumButtons; j++) {
                    if (bf.stringWidth(bts[current_layer][j][i].contents) > nButtonW) {
                         g.setFont(nf);
                         g.drawString(bts[current_layer][j][i].contents,
                                 curx + nButtonW / 2,
                                 cury + (nButtonH - nf.getBaselinePosition()) / 2,
                                 Graphics.HCENTER | Graphics.TOP);
                    } else {
                         g.setFont(bf);
                         g.drawString(bts[current_layer][j][i].contents,
                                 curx + nButtonW / 2,
                                 cury + (nButtonH - bf.getBaselinePosition()) / 2,
                                 Graphics.HCENTER | Graphics.TOP);
                    }

                    cury += nButtonH;
               }
               curx += nButtonW;
               cury = 0 + nScreenH;
          }
          g.setColor(0xffFFFFFF);
          g.setFont(bf);
          g.drawString(display_c.toString(),
                  display_text_anchor_x,
                  display_text_anchor_y,
                  Graphics.HCENTER | Graphics.TOP);

          g.setFont(sf);
          g.setColor(0xFFFFFFFF);
          g.drawString(new Integer(current_layer + 1).toString(),
                  sf.stringWidth("5"),
                  nScreenH / 2 - sf.getBaselinePosition() / 4,
                  Graphics.BOTTOM | Graphics.HCENTER);
          if (eq.getDR() == 0) {
               g.drawString("DEG", nTdW - sf.stringWidth("DEG"), nScreenH / 2 - sf.getBaselinePosition() / 4, Graphics.BOTTOM | Graphics.HCENTER);
          } else {
               g.drawString("RAD", nTdW - sf.stringWidth("RAD"), nScreenH / 2 - sf.getBaselinePosition() / 4, Graphics.BOTTOM | Graphics.HCENTER);
          }
     }
}
