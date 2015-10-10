/*
 * NGCalc.java. Весрия 2.4.3.
 * Стандартный основной класс Java-приложения. Осуществляет запуск приложения, демонстрацию заставки и переключение
 * на экран калькулятора.
 */


package com.grevzin.calc2;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.*;
import com.grevzin.expression.Real;
import com.grevzin.expression.chartools;
import com.grevzin.util.error;

/**
 * @author Kuka
 */
public class NGCalc extends MIDlet implements CommandListener {

     private boolean midletPaused = false;
     private CalcDisplay D_MAIN;
     private Image image2 = null;
     
     private Alert a_error;
     private Form form;
     private Form f_welcome;
     private ImageItem imageItem;
     private List l_history;
     private List l_vars;
     private Command c_delvar;
     private Command c_insert2;
     private Command c_canvas;
     private Command c_tovars;
     private Command c_history;
     private Command c_exit;
     private Command c_insert;
     private Image image1;
     
     public NGCalc() {
          D_MAIN = new CalcDisplay(getC_history(), getC_tovars(), getC_exit());
          D_MAIN.setCommandListener(this);

          //#ifdef Special
//#           getImageItem().setImage(getImage2());
          //#endif


     }

     private void initialize() {
          
     }
     
     
     public void startMIDlet() {
          
          switchDisplayable(null, getF_welcome());
         
     }
     
     public void resumeMIDlet() {
          
     }
   
     
     public void switchDisplayable(Alert alert, Displayable nextDisplayable) {
          
          Display display = getDisplay();
          if (alert == null) {
               display.setCurrent(nextDisplayable);
          } else {
               display.setCurrent(alert, nextDisplayable);
          }
         
     }
     
    
     public Form getF_welcome() {
          if (f_welcome == null) {
               f_welcome = new Form("NGCalc 2.4.3 \u00A9 Revzin", new Item[]{getImageItem()});
               f_welcome.addCommand(getC_canvas());
               f_welcome.setCommandListener(this);
              
          }
          return f_welcome;
     }
     
     public void commandAction(Command command, Displayable displayable) {
          if ((displayable == D_MAIN) && D_MAIN.Responds()) {
               if (command == c_exit) {
                    exitMIDlet();
               }
               if (command == c_history) {
                    switchDisplayable(null, getL_history());
                    Stack st = D_MAIN.getEqHS();
                    Enumeration en = st.elements();
                    l_history.deleteAll();
                    while (en.hasMoreElements()) {
                         l_history.append((String) en.nextElement(), null);
                    }

               }
               if (command == c_tovars) {
                    getL_vars().deleteAll();
                    StringBuffer sb;
                    Enumeration values = D_MAIN.getEqVars().elements();
                    Enumeration names = D_MAIN.getEqVars().keys();
                    String name;
                    Real value;
                    while ((values.hasMoreElements()) && (names.hasMoreElements())) {
                         name = String.valueOf(names.nextElement());
                         value = new Real(String.valueOf((values.nextElement())));
                         Real.NumberFormat n = new Real.NumberFormat();
                         n.maxwidth = 17;
                         n.fse = Real.NumberFormat.FSE_NONE;
                         sb = new StringBuffer(value.toString(n));
                         if (chartools.contains(sb, 'e')) {
                              StringBuffer ev = new StringBuffer();
                              for (int i = sb.length() - 1; i > -1; --i) {
                                   if (sb.charAt(i) == 'e') {
                                        sb.deleteCharAt(i);
                                        break;
                                   }
                                   ev.append(sb.charAt(i));
                                   sb.deleteCharAt(i);
                              }
                              ev.reverse();
                              sb.append(" * 10^(" + ev + ")");
                              if ((!(sb.equals(new StringBuffer("")))) && (!(name.equals(""))) && (!(sb.equals(new StringBuffer("null")))) && (!(name.equals("null")))) {
                                   l_vars.append(name + " = " + sb.toString(), null);
                              }
                         } else {
                              if ((!(sb.equals(new StringBuffer("")))) && (!(name.equals(""))) && (!(sb.equals(new StringBuffer("null")))) && (!(name.equals("null")))) {
                                   getL_vars().append(name + " = " + sb.toString(), null);
                              }
                         }
                    }
                    switchDisplayable(null, getL_vars());
               }
          }
          if (displayable == f_welcome) {
               if (command == c_canvas) {
                    switchDisplayable(null, D_MAIN);

                   
               }
          } else if (displayable == form) {
               if (command == c_history) {
                    
               } else if (command == c_tovars) {
                    
               }
          } else if (displayable == l_history) {
               if (command == List.SELECT_COMMAND) {
                    
                    l_historyAction();

               } else if (command == c_canvas) {
                    switchDisplayable(null, D_MAIN);

                   
               } else if (command == c_insert) {

                    if (l_history.size() != 0) {
                         StringBuffer sb = new StringBuffer();
                         String his = l_history.getString(l_history.getSelectedIndex());
                         boolean p_end = false;
                         for (int i = 0; i < his.length(); i++) {
                              if (his.charAt(i) == '=') {
                                   break;
                              }
                              if (p_end) {
                                   sb.append(his.charAt(i));
                              }
                              if (his.charAt(i) == ':') {
                                   p_end = true;
                              }
                         }
                         sb.deleteCharAt(sb.length() - 1);
                         D_MAIN.appendDispCont(" " + sb.toString());

                    }
                    switchDisplayable(null, D_MAIN);
                    D_MAIN.repaint();
               }
          } else if (displayable == l_vars) {
               if (command == List.SELECT_COMMAND) {
                    
                    l_varsAction();
                    
               } else if (command == c_canvas) {
                    switchDisplayable(null, D_MAIN);

                 
               } else if (command == c_delvar) {
                    try {
                         StringBuffer sb = new StringBuffer();
                         String his = l_vars.getString(l_vars.getSelectedIndex());
                         for (int i = 0; i < his.length(); i++) {
                              if (his.charAt(i) == '=') {
                                   break;
                              }
                              sb.append(his.charAt(i));
                         }
                         D_MAIN.deletevar(sb.toString().trim());
                         l_vars.delete(l_vars.getSelectedIndex());
                    } catch (error er) {
                         switchDisplayable(null, getA_error());
                         a_error.setString(er.getMessage());
                    }

                  
               } else if (command == c_insert) {
                    StringBuffer sb = new StringBuffer();
                    String his = l_vars.getString(l_vars.getSelectedIndex());
                    for (int i = 0; i < his.length(); i++) {
                         if (his.charAt(i) == '=') {
                              break;
                         }
                         sb.append(his.charAt(i));
                    }
                    D_MAIN.appendDispCont(sb.toString());
                    switchDisplayable(null, D_MAIN);

                    D_MAIN.repaint();                   
               }
          }
          
     }

     public List getL_history() {
          if (l_history == null) {
               
               l_history = new List("\u0418\u0441\u0442\u043E\u0440\u0438\u044F \u0432\u044B\u0447\u0438\u0441\u043B\u0435\u043D\u0438\u0439", Choice.IMPLICIT);
               l_history.addCommand(getC_canvas());
               l_history.addCommand(getC_insert());
               l_history.setCommandListener(this);
              
          }
          return l_history;
     }
   

     public void l_historyAction() {
         
          String __selectedString = getL_history().getString(getL_history().getSelectedIndex());
          
     }
    
     public List getL_vars() {
          if (l_vars == null) {
              
               l_vars = new List("\u041F\u0435\u0440\u0435\u043C\u0435\u043D\u043D\u044B\u0435", Choice.IMPLICIT);
               l_vars.addCommand(getC_canvas());
               l_vars.addCommand(getC_insert());
               l_vars.addCommand(getC_delvar());
               l_vars.setCommandListener(this);
             
          }
          return l_vars;
     }
     
     public void l_varsAction() {
        
          String __selectedString = getL_vars().getString(getL_vars().getSelectedIndex());
        
     }
     
     public Command getC_canvas() {
          if (c_canvas == null) {
               
               c_canvas = new Command("\u041A\u043B\u0430\u0432\u0438\u0430\u0442.", "\u041A \u043A\u043B\u0430\u0432\u0438\u0430\u0442\u0443\u0440\u0435", Command.BACK, 0);
               
          }
          return c_canvas;
     }
     
     public Command getC_tovars() {
          if (c_tovars == null) {
               
               c_tovars = new Command("\u041F\u0435\u0440\u0435\u043C\u0435\u043D\u043D\u044B\u0435", Command.OK, 0);

          }
          return c_tovars;
     }

     
     public Command getC_history() {
          if (c_history == null) {
               
               c_history = new Command("\u0418\u0441\u0442\u043E\u0440\u0438\u044F", Command.OK, 0);
              
          }
          return c_history;
     }
     
     public Command getC_exit() {
          if (c_exit == null) {
              
               c_exit = new Command("\u0412\u044B\u0439\u0442\u0438", Command.EXIT, 0);
               
          }
          return c_exit;
     }
     
     public Image getImage1() {
          if (image1 == null) {
              
               try {
                    image1 = Image.createImage("/com/grevzin/pct/logo-gd.png");
               } catch (java.io.IOException e) {
                    e.printStackTrace();
               }
            
          }
          return image1;
     }
     
     public ImageItem getImageItem() {
          if (imageItem == null) {
               
               imageItem = new ImageItem("", getImage1(), ImageItem.LAYOUT_CENTER, "/com/grevzin/pct/logo-gd.png");
               
          }
          return imageItem;
     }
    
     public Command getC_insert() {
          if (c_insert == null) {
              
               c_insert = new Command("\u0412\u0441\u0442\u0430\u0432\u0438\u0442\u044C", Command.BACK, 0);
         
          }
          return c_insert;
     }
     
     public Command getC_insert2() {
          if (c_insert2 == null) {
               
               c_insert2 = new Command("Back", Command.BACK, 0);
               
          }
          return c_insert2;
     }
     
     public Command getC_delvar() {
          if (c_delvar == null) {
               
               c_delvar = new Command("\u0423\u0434\u0430\u043B\u0438\u0442\u044C", Command.OK, 0);
           
          }
          return c_delvar;
     }
     
     public Alert getA_error() {
          if (a_error == null) {
               
               a_error = new Alert("\u041E\u0448\u0438\u0431\u043A\u0430!");
               a_error.setTimeout(Alert.FOREVER);
              
          }
          return a_error;
     }
     
     public Form getForm() {
          if (form == null) {
               
               form = new Form("form");
               form.addCommand(getC_tovars());
               form.addCommand(getC_history());
               form.setCommandListener(this);
               
          }
          return form;
     }
     
     public Display getDisplay() {
          return Display.getDisplay(this);
     }

     
     public void exitMIDlet() {
          switchDisplayable(null, null);
          destroyApp(true);
          notifyDestroyed();
     }

    
     public void startApp() {
          if (midletPaused) {
               resumeMIDlet();
          } else {
               initialize();
               startMIDlet();
          }
          midletPaused = false;
     }

     
     public void pauseApp() {
          midletPaused = true;
     }

    
     public void destroyApp(boolean unconditional) {
     }

     public Image getImage2() {
          if (image2 == null) {
               
               try {
                    image2 = Image.createImage("/com/grevzin/pct/logo-sp.png");
               } catch (java.io.IOException e) {
                    e.printStackTrace();
               }
               
          }
          return image2;
     }

     public Command getC_dummy() {
          if (c_tovars == null) {
             
               c_tovars = new Command("Enter", Command.CANCEL, 0);

          }
          return c_tovars;
     }
}