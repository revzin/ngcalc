/*
 * dcnst.java - файл, содержащий основные настройки виртуального дисплея.
 */
package com.grevzin.calc2;

/**
 *
 * @author Kuka
 */

/*
 Класс содержит константы кодов клавиш телефона, константы кодов клавиш дисплея.
 */
class dcnst {
     /*
      *  Константы кодов клавиш четырёхпозиционного переключателя и софт-клавиш.
      */
     class HrdwButtonCodes {

          public static final int JOY_UP = -1;
          public static final int JOY_DOWN = -2;
          public static final int JOY_FIRE = -5;
          public static final int JOY_LEFT = -3;
          public static final int JOY_RIGHT = -4;
          public static final int SE_CNCL = -8;
          public static final int LSK = -6;
          public static final int RSK = -7;
     }

     /*
      * Константы операционных кодов виртуальных клавиш.
      */
     public static final int OPCODE_SWITCHLAYER = 0;
     public static final int OPCODE_INSERT = 1;
     public static final int OPCODE_MENU = 2;
     public static final int OPCODE_EXEC = 3;
     public static final int OPCODE_BACKSPACE = 4;
     public static final int OPCODE_BACKSWITCHLAYER = 5;
     public static final int OPCODE_ADDLAST = 6;
     public static final int OPCODE_SWITCHABC = 7;
     public static final int OPCODE_SWITCHNUMS = 8;
     public static final int DEBUG_BRD = 0;
     public static final int OPCODE_SPACE = 9;
     public static final int OPCODE_SWITCHBS = 10;
     public static final int OPCODE_D_R = 11;
     public static final int OPCODE_D_A = 12;

     /*
      *  Константы наименований виртуальной клавиатуры.
      */

     public static final String[][][] bs4x5 = {
          {
               {"1", "2", "3", "+"},
               {"4", "5", "6", "-"},
               {"7", "8", "9", "*"},
               {"(", "0", ")", "/"},
               {".", "^", "BS", "NL"}
          }, {
               {"cos", "sin", "tg", "ctg"},
               {"arccos", "arcsin", "arctg", "arcctg"},
               {"pi", "log", "ln", "\u221A"},
               {"fac", "^(1/3)", "^(1/4)", "SPC"},
               {"DALL", "e", "BS", "NL"}
          }, {
               {"EXE", "^3", "D/R", "MNU"},
               {":", "%", "tdeg", "trad"},
               {"X", "Y", "Z", "ans"},
               {"$X=", "$Y=", "$Z=", "^2"},
               {"*10^(", ": 10", "BS", "NL"}
          }
     };

     /*
      *  Константы операционных кодов клавиш виртуальной клавиатуры (см. выше для расшифровки).
      */
     public static final int[][][] bs4x5opc = {
          {
               {1, 1, 1, 1},
               {1, 1, 1, 1},
               {1, 1, 1, 1},
               {1, 1, 1, 1},
               {1, 1, 4, 0}
          }, {
               {1, 1, 1, 1},
               {1, 1, 1, 1},
               {1, 1, 1, 1},
               {1, 1, 1, 9},
               {12, 1, 4, 0}
          }, {
               {3, 1, 11, 2},
               {1, 1, 1, 1},
               {1, 1, 1, 1},
               {1, 1, 1, 1},
               {1, 1, 4, 0}
          }
     };
    


     /*
      * Константые вспомогательные объекты, содержащие полуню информацию о клавиатуре.
      */
     public static final ButtonScheme3D bs5 = new ButtonScheme3D(bs4x5, bs4x5opc);
    
}


