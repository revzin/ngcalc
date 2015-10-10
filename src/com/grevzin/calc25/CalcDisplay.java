package com.grevzin.calc25;

import javax.microedition.lcdui.*;
import com.grevzin.util.*;
import com.grevzin.expression.*;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Enumeration;
import java.util.Calendar;

/**
 *
 * @author Kuka
 */
// <editor-fold defaultstate="collapsed" desc="class Button {}">
class Button {

     CFntText cnts;
     String contents = "";
     private boolean isHgltd = false, isPressed = false;
     int opcode = dcnst.OPCODE_INSERT;

     class render_data {

          int[] pixels;
          int scanlng, width, heigth;
     }

     CFntText.RenderData get_rdata() {
          return cnts.get_rdata();
     }

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

     Button(String contents, int opcode, CFont fnt) {
          this.contents = contents;
          cnts = new CFntText(contents, fnt);
          this.opcode = opcode;
     }
}// </editor-fold>

public final class CalcDisplay extends Canvas {

     private int nButtonH,
             nButtonW,
             nHorNumButtons,
             nVerNumButtons,
             nScreenH,
             nTdH,
             nTdW,
             hbX,
             hbY,
             display_text_anchor_x,
             display_text_anchor_y,
             exp_width;
     private boolean responds = false;
     private StringBuffer display_c = new StringBuffer();
     private Button[][][] bts;
     private int current_layer;
     javax.microedition.lcdui.Font nf;
     javax.microedition.lcdui.Font bf;
     javax.microedition.lcdui.Font sf;
     private boolean pointerOnLScreen = false, pointerOnRScreen = false;
     private expression eq = new expression(0);
     private boolean hasans = false;
     private boolean exp_overlaps = false;
     private ButtonScheme3D bs;
     private Stack h = new Stack();
     int panelH = 0;
     Calendar C = Calendar.getInstance();
     Command c_hist, c_vars;

     public boolean Responds() {
          return responds;
     }

     public CalcDisplay(Command c_hist, Command c_vars) {
          this.setFullScreenMode(true);
          this.c_hist = c_hist;
          this.c_vars = c_vars;
          bs = dcnst.bs5;//new ButtonScheme3D(dcnst.bs4x4, dcnst.bs4x4opc);
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
                         bts[i][j][k] = new Button(bs.get(i, j, k), bs.getOpcode(i, j, k), normal_sized_font);
                    }
               }
          }
          hbX = 0;
          hbY = 0;
          bts[current_layer][hbX][hbY].Highlight();
          display_text_anchor_x = nTdW / 2;
          display_text_anchor_y = (nScreenH / 2) - (nf.getBaselinePosition() / 2);

//#ifdef Special
//#           if (C.get(Calendar.DAY_OF_MONTH) == 19) {
//#                display_c = new StringBuffer("Прости меня за всё, любмое создание. Прости, пожалуйста. Так хочу быть уверенным в том, что завтра всё будет в порядке, но не могу. Сохраним все, любимая...");
//#           }
//#           if ((C.get(Calendar.DAY_OF_MONTH) == 9) && (C.get(Calendar.MONTH) == Calendar.DECEMBER)) {
//#                display_c = new StringBuffer("Такой чудный день! Сегодня родилась моя Наташенька... С днем рождения, моё единственное существо, С ДНЕМ РОЖДЕНИЯ, НАТАША, СОКРОВИЩЕ МОЁ!");
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

     private void SetOpen() {
          this.removeCommand(c_hist);
          this.removeCommand(c_vars);
          this.setFullScreenMode(true);
     }

     private void SetMenu() {
          this.addCommand(c_hist);
          this.addCommand(c_vars);
          this.setFullScreenMode(false);
     }

     public void injectNewPanel(int i) {
          if (i < 0) {
               i = 0;
          }
          if (i < nTdH / 2) {
               i = nTdH / 2;
          }
          panelH = i;
          current_layer = 0;

          nButtonH = (nTdH - nScreenH - panelH) / bs.getRow();
          nButtonW = nTdW / bs.getColumn();
          nHorNumButtons = bs.getColumn();
          nVerNumButtons = bs.getRow();
          bts = new Button[bs.getLay()][bs.getRow()][bs.getColumn()];
          for (i = 0; i < bs.getLay(); i++) {
               for (int j = 0; j < bs.getRow(); j++) {
                    for (int k = 0; k < bs.getColumn(); k++) {
                         bts[i][j][k] = new Button(bs.get(i, j, k), bs.getOpcode(i, j, k), normal_sized_font);
                    }
               }
          }
          hbX = 0;
          hbY = 0;
          bts[current_layer][hbX][hbY].Highlight();

          repaint();
     }

     public void injectBS(ButtonScheme3D bs3) {
          current_layer = 0;

          nButtonH = (nTdH - nScreenH - panelH) / bs3.getRow();
          nButtonW = nTdW / bs3.getColumn();
          nHorNumButtons = bs3.getColumn();
          nVerNumButtons = bs3.getRow();
          bts = new Button[bs3.getLay()][bs3.getRow()][bs3.getColumn()];
          for (int i = 0; i < bs3.getLay(); i++) {
               for (int j = 0; j < bs3.getRow(); j++) {
                    for (int k = 0; k < bs3.getColumn(); k++) {
                         bts[i][j][k] = new Button(bs3.get(i, j, k), bs3.getOpcode(i, j, k), normal_sized_font);
                    }
               }
          }
          hbX = 0;
          hbY = 0;
          bts[current_layer][hbX][hbY].Highlight();
          repaint();
     }

     protected void keyPressed(int keyCode) {


          boolean navbutton_pressed = false;
          boolean pos_reset = true;
          if (keyCode == getKeyCode(LEFT)) {
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
          } else if (keyCode == getKeyCode(RIGHT)) {
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
          } else if (keyCode == getKeyCode(DOWN)) {
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
          } else if (keyCode == getKeyCode(UP)) {
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
          } else if (keyCode == getKeyCode(FIRE)) {
               buttonPress(hbX, hbY);
          } else if (keyCode == KEY_NUM1) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               display_c.append(new Integer(keyCode - 48));
          } else if (keyCode == KEY_NUM2) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               display_c.append(new Integer(keyCode - 48));
          } else if (keyCode == KEY_NUM3) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               display_c.append(new Integer(keyCode - 48));
          } else if (keyCode == KEY_NUM4) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               display_c.append(new Integer(keyCode - 48));
          } else if (keyCode == KEY_NUM5) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               display_c.append(new Integer(keyCode - 48));
          } else if (keyCode == KEY_NUM6) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               display_c.append(new Integer(keyCode - 48));
          } else if (keyCode == KEY_NUM7) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               display_c.append(new Integer(keyCode - 48));
          } else if (keyCode == KEY_NUM8) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               display_c.append(new Integer(keyCode - 48));
          } else if (keyCode == KEY_NUM9) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               display_c.append(new Integer(keyCode - 48));
          } else if (keyCode == KEY_NUM0) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               display_c.append(new Integer(keyCode - 48));
          } else if (keyCode == KEY_STAR) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               display_c.append(" * ");
          } else if (keyCode == KEY_POUND) {
               if (hasans) {
                    hasans = false;
                    display_c = new StringBuffer("");
               }
               if (display_c.length() > 0) {
                    display_c.deleteCharAt(display_c.length() - 1);
               }

          } //else if (keyCode == Canvas.)

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

     public Enumeration getEqHist() {
          return h.elements();
     }

     public Stack getEqHS() {
          return h;
     }

     public void redraw() {
          repaint();
     }

     public Hashtable getEqVars() {
          return eq.extract_vars();
     }

     public void setDispCont(String s) {
          if (s == null) {
               return;
          }
          display_c = new StringBuffer(s);
     }

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

     private void buttonPress(int i, int j) {
          bts[current_layer][i][j].Highlight();
          switch (bts[current_layer][i][j].opcode) {
               case dcnst.OPCODE_D_A: {
                    display_c.delete(0, display_c.length()); // MAZAFAKA //
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
                    display_c.append(bts[current_layer][i][j].contents);
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
               case dcnst.OPCODE_SWITCHBS: {
                    if (bs == dcnst.bs5) {
                         injectBS(dcnst.bs4);
                         bs = dcnst.bs4;
                    } else {
                         injectBS(dcnst.bs5);
                         bs = dcnst.bs5;
                    }
                    break;
               }
               case dcnst.OPCODE_SWITCHABC: {
                    injectBS(dcnst.bsA);
                    bs = dcnst.bsA;
                    break;
               }
               case dcnst.OPCODE_SWITCHNUMS: {
                    injectBS(dcnst.bs5);
                    bs = dcnst.bs5;
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
                         sbs.append(" >> ");
                         int v = 0;
                         for (; v < display_c.length(); v++) {
                              if (display_c.charAt(v) == '=') {
                                   display_c.setCharAt(v, ' ');
                                   break;
                              } else {
                                   sbs.append(display_c.charAt(v));
                              }
                         }
                         display_c.delete(0, v + 1);
                    }

                    if (!(eq.err())) {
                         if (eq.getDR() == 0) {
                              h.push(new String("D: " + display_c + " = " + eq.getAnwserP10XORW(15, 7) + sbs.toString()));
                         } else {
                              h.push(new String("R: " + display_c + " = " + eq.getAnwserP10XORW(15, 7) + sbs.toString()));
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
                    if (display_c.length() > 0) {
                         for (;;) {
                              if (display_c.length() == 0) {
                                   break;
                              }
                              display_c.deleteCharAt(display_c.length() - 1);
                              if (display_c.length() == 0) {
                                   break;
                              }
                              if (display_c.charAt(display_c.length() - 1) != ' ') {
                                   break;
                              }
                         }
                    }
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

     public void deletevar(String name) throws error {
          eq.remvar(name);
     }

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
          CFntText.RenderData current_rnd_data;
          for (int i = 0; i < nHorNumButtons; i++) {

               for (int j = 0; j < nVerNumButtons; j++) {

                    current_rnd_data = bts[current_layer][j][i].get_rdata();

                    g.drawRGB(current_rnd_data.rgb_paint_data,
                            0,
                            current_rnd_data.w,
                            curx,
                            cury,
                            current_rnd_data.w,
                            current_rnd_data.h,
                            true);

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