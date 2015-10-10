
package com.grevzin.calc25;

/**
 *
 * @author Kuka
 */
class dcnst {

     public static final int OPCODE_SWITCHLAYER = 0;
     public static final int OPCODE_INSERT = 1;
     public static final int OPCODE_MENU = 2;
     public static final int OPCODE_EXEC = 3;
     public static final int OPCODE_BACKSPACE = 4;
     public static final int OPCODE_BACKSWITCHLAYER = 5;
     public static final int OPCODE_ADDLAST = 6;
     public static final int OPCODE_SWITCHABC = 7;
     public static final int OPCODE_SWITCHNUMS = 8;
     public static final int SAMSUNG_XYNTA_HEIGHT = 0;
     public static final int OPCODE_SPACE = 9;
     public static final int OPCODE_SWITCHBS = 10;
     public static final int OPCODE_D_R = 11;
     public static final int OPCODE_D_A = 12;
     public static final String[][][] bs4x5 = {
          {
               {"ABC DE FGHASFLASDKHKDFHSADF", " ", "3", " + "},
               {"4", "5", "6", " - "},
               {"7", "8", "9", " * "},
               {"(", "0", ")", " / "},
               {".", " ^ ", "BS", "NL"}
          }, {
               {"cos ", "sin ", "tg ", "ctg "},
               {"arccos ", "arcsin ", "arctg ", "arcctg "},
               {"pi ", "(pi/2) ", "(3 pi/2)", "\u221A"},
               {"fac ", "\u221B", "\u221C", "SPC"},
               {"DALL", "e ", "BS", "NL"}
          }, {
               {"EXE", "log ", "D/R", "MNU"},
               {" : ", " % ", "tdeg ", "trad "},
               {"X", "Y", "Z", "ans"},
               {"$X=", "$Y=", "$Z=", "^2 "},
               {" *10^(", "SABC", "BS", "NL"}
          }
     };
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
               {1, 7, 4, 0}
          }
     };
     public static final String[][][] bsAbc4x5 = {
          {
               {"A", "B", "C", "D"},
               {"E", "F", "G", "H"},
               {"I", "J", "K", "L"},
               {"M", "N", "P", "Q"},
               {"DALL", "SPC", "BS", "NL"}
          }, {
               {"R", "S", "T", "U"},
               {"V", "W", "X", "Y"},
               {"Z", "v_a", "$", "v_c"},
               {"v_b", "v_e", "=", "v_d"},
               {"NUMS", "SPC", "BS", "NL"}
          }
     };
     public static final int[][][] boAbc4x5 = {
          {
               {1, 1, 1, 1},
               {1, 1, 1, 1},
               {1, 1, 1, 1},
               {1, 1, 1, 1},
               {12, 9, 4, 0}
          }, {
               {1, 1, 1, 1},
               {1, 1, 1, 1},
               {1, 1, 1, 1},
               {1, 1, 1, 1},
               {8, 9, 4, 0}
          }
     };
     public static final int[][][] bs4x4opc = {
          {
               {1, 1, 1, 1},
               {1, 1, 1, 4},
               {1, 1, 1, 0},
               {1, 1, 1, 5}
          }, {
               {1, 1, 1, 1},
               {1, 1, 1, 4},
               {1, 1, 1, 0},
               {1, 1, 1, 5}
          }, {
               {1, 9, 1, 1},
               {1, 1, 1, 4},
               {1, 1, 1, 0},
               {1, 1, 1, 5}
          }, {
               {1, 1, 1, 1},
               {1, 1, 1, 4},
               {1, 1, 1, 0},
               {1, 1, 1, 5}
          }, {
               {3, 10, 7, 2},
               {11, 1, 1, 4},
               {1, 1, 1, 0},
               {1, 1, 1, 5}
          },};
     public static final String[][][] bs4x4 = {
          {
               {"ABCD", "2", "3", "."},
               {"4", "5", "6", "BS"},
               {"7", "8", "9", "NL"},
               {"(", "0", ")", "PL"}
          }, {
               {" + ", " * ", " ^ ", "."},
               {" - ", " / ", " ! ", "BS"},
               {" *10^(", " ^ 2", " ^ 3", "NL"},
               {"(", "0", ")", "PL"}
          }, {
               {" cos ", "SPC", " pi", " pi/2 "},
               {" sin ", "0", " 3*pi/2 ", "BS"},
               {" tg ", "(", " tdeg ", "NL"},
               {" ctg ", ")", " trad ", "PL"}
          }, {
               {" arcsin ", " log ", "F ", "."},
               {" arccos ", " zd ", "!3 ", "BS"},
               {" arctg ", " rem ", "!2 ", "NL"},
               {" arcctg ", " gma ", " ns", "PL"}
          }, {
               {"EXE", "SWBS", "SWABC", "MNU"},
               {"D/R", "$A=", " A", "BS"},
               {"$X= ", "$Y= ", "$Z= ", "NL"},
               {"X", "Y", "Z", "PL"}
          }
     };
     public static final ButtonScheme3D bs5 = new ButtonScheme3D(bs4x5, bs4x5opc);
     public static final ButtonScheme3D bs4 = new ButtonScheme3D(bs4x4, bs4x4opc);
     public static final ButtonScheme3D bsA = new ButtonScheme3D(bsAbc4x5, boAbc4x5);
}

class DefaultFntProps {

     FntProps small_f = new FntProps();
     FntProps big_f = new FntProps();
     FntProps mid_f = new FntProps();

     DefaultFntProps() {
          mid_f.ltr_h = 17;
          mid_f.ltr_picture_distance = 1;
          mid_f.ltr_spacing = 2;
          mid_f.ltr_w = 16;
     }
}