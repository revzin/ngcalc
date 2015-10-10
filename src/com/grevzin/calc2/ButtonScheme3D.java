
package com.grevzin.calc2;

/*
 *  Вспомогательный класс, описывающий конкретную раскалдку виртуальной клавиатуры.
 */

class ButtonScheme3D {

     private String[][][] contents;
     private int[][][] opcodes;
     
     ButtonScheme3D(String[][][] s, int[][][] o) {
          contents = s;
          opcodes = o;
     }

     String get(int l, int r, int c) {
          return contents[l][r][c];
     }

     int getOpcode(int l, int r, int c){
          return opcodes[l][r][c];
     }
     int getColumn() {
          return contents[0][0].length;
     }

     int getRow() {
          return contents[0].length;
     }

     int getLay() {
          return contents.length;
     }
}
