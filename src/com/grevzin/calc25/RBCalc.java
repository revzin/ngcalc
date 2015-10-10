package com.grevzin.calc25;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.*;
import com.grevzin.expression.Real;
import com.grevzin.expression.chartools;
import com.grevzin.util.error;

/**
 * @author Kuka
 */
public class RBCalc extends MIDlet implements CommandListener {

     private boolean midletPaused = false;
     private CalcDisplay D_MAIN;
     private Image image2 = null;
     //private RecordStore rs;
     //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">
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
     //</editor-fold>

     /**
      * The RBCalc constructor.
      */
     public RBCalc() {
          D_MAIN = new CalcDisplay(getC_history(), getC_tovars());
          D_MAIN.setCommandListener(this);

          //#ifdef GeneralDevice 
//#           getImageItem().setImage(getImage2());
          //#endif


     }

     //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">
     //</editor-fold>
     //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">
     /**
      * Initilizes the application.
      * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
      */
     private void initialize() {
          /*try {
          rs = RecordStore.openRecordStore("RBCalc-SH", true);
          rs.
          ByteArrayInputStream bis = new ByteArrayInputStream("asd");
          } catch (RecordStoreException e) {
          }*/
          // write post-initialize user code here
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">
     /**
      * Performs an action assigned to the Mobile Device - MIDlet Started point.
      */
     public void startMIDlet() {
          // write pre-action user code here
          switchDisplayable(null, getF_welcome());
          //switchDisplayable(null, D_MAIN);
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">
     /**
      * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
      */
     public void resumeMIDlet() {
          // write pre-action user code here
          // write post-action user code here
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">
     /**
      * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
      * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
      * @param nextDisplayable the Displayable to be set
      */
     public void switchDisplayable(Alert alert, Displayable nextDisplayable) {
          // write pre-switch user code here
          Display display = getDisplay();
          if (alert == null) {
               display.setCurrent(nextDisplayable);
          } else {
               display.setCurrent(alert, nextDisplayable);
          }
          // write post-switch user code here
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: f_welcome ">
     /**
      * Returns an initiliazed instance of f_welcome component.
      * @return the initialized component instance
      */
     public Form getF_welcome() {
          if (f_welcome == null) {
               // write pre-init user code here
               f_welcome = new Form("RBCalc v2.0.0 \u00A9 G. G. Revzin", new Item[]{getImageItem()});
               f_welcome.addCommand(getC_canvas());
               f_welcome.setCommandListener(this);
               // write post-init user code here
          }
          return f_welcome;
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">
     /**
      * Called by a system to indicated that a command has been invoked on a particular displayable.
      * @param command the Command that was invoked
      * @param displayable the Displayable where the command was invoked
      */
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

                    // write post-action user code here
               }
          } else if (displayable == form) {
               if (command == c_history) {
                    // write pre-action user code here
                    // write post-action user code here
               } else if (command == c_tovars) {
                    // write pre-action user code here
                    // write post-action user code here
               }
          } else if (displayable == l_history) {
               if (command == List.SELECT_COMMAND) {
                    // write pre-action user code here
                    l_historyAction();

               } else if (command == c_canvas) {
                    switchDisplayable(null, D_MAIN);

                    // write post-action user code here
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
                    D_MAIN.redraw();
               }
          } else if (displayable == l_vars) {
               if (command == List.SELECT_COMMAND) {
                    // write pre-action user code here
                    l_varsAction();
                    // write post-action user code here
               } else if (command == c_canvas) {
                    switchDisplayable(null, D_MAIN);

                    // write post-action user code here
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

                    // write post-action user code here
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

                    D_MAIN.redraw();                   // write post-action user code here
               }
          }
          // write post-action user code here
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: l_history ">
     /**
      * Returns an initiliazed instance of l_history component.
      * @return the initialized component instance
      */
     public List getL_history() {
          if (l_history == null) {
               // write pre-init user code here
               l_history = new List("\u0418\u0441\u0442\u043E\u0440\u0438\u044F \u0432\u044B\u0447\u0438\u0441\u043B\u0435\u043D\u0438\u0439", Choice.IMPLICIT);
               l_history.addCommand(getC_canvas());
               l_history.addCommand(getC_insert());
               l_history.setCommandListener(this);
               // write post-init user code here
          }
          return l_history;
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Method: l_historyAction ">
     /**
      * Performs an action assigned to the selected list element in the l_history component.
      */
     public void l_historyAction() {
          // enter pre-action user code here
          String __selectedString = getL_history().getString(getL_history().getSelectedIndex());
          // enter post-action user code here
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: l_vars ">
     /**
      * Returns an initiliazed instance of l_vars component.
      * @return the initialized component instance
      */
     public List getL_vars() {
          if (l_vars == null) {
               // write pre-init user code here
               l_vars = new List("\u041F\u0435\u0440\u0435\u043C\u0435\u043D\u043D\u044B\u0435", Choice.IMPLICIT);
               l_vars.addCommand(getC_canvas());
               l_vars.addCommand(getC_insert());
               l_vars.addCommand(getC_delvar());
               l_vars.setCommandListener(this);
               // write post-init user code here
          }
          return l_vars;
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Method: l_varsAction ">
     /**
      * Performs an action assigned to the selected list element in the l_vars component.
      */
     public void l_varsAction() {
          // enter pre-action user code here
          String __selectedString = getL_vars().getString(getL_vars().getSelectedIndex());
          // enter post-action user code here
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: c_canvas ">
     /**
      * Returns an initiliazed instance of c_canvas component.
      * @return the initialized component instance
      */
     public Command getC_canvas() {
          if (c_canvas == null) {
               // write pre-init user code here
               c_canvas = new Command("\u041A\u043B\u0430\u0432\u0438\u0430\u0442.", "\u041A \u043A\u043B\u0430\u0432\u0438\u0430\u0442\u0443\u0440\u0435", Command.BACK, 0);
               // write post-init user code here
          }
          return c_canvas;
     }
     //</editor-fold>

     /*    //</editor-fold>
     //</editor-fold>
     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: c_tovars ">
     /**
      * Returns an initiliazed instance of c_tovars component.
      * @return the initialized component instance
      */
     public Command getC_tovars() {
          if (c_tovars == null) {
               // write pre-init user code here
               c_tovars = new Command("\u041F\u0435\u0440\u0435\u043C\u0435\u043D\u043D\u044B\u0435", Command.OK, 0);

          }
          return c_tovars;
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: c_history ">
     /**
      * Returns an initiliazed instance of c_history component.
      * @return the initialized component instance
      */
     public Command getC_history() {
          if (c_history == null) {
               // write pre-init user code here
               c_history = new Command("\u0418\u0441\u0442\u043E\u0440\u0438\u044F", Command.OK, 0);
               // write post-init user code here
          }
          return c_history;
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: c_exit ">
     /**
      * Returns an initiliazed instance of c_exit component.
      * @return the initialized component instance
      */
     public Command getC_exit() {
          if (c_exit == null) {
               // write pre-init user code here
               c_exit = new Command("\u0412\u044B\u0439\u0442\u0438", Command.EXIT, 0);
               // write post-init user code here
          }
          return c_exit;
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: image1 ">
     /**
      * Returns an initiliazed instance of image1 component.
      * @return the initialized component instance
      */
     public Image getImage1() {
          if (image1 == null) {
               // write pre-init user code here
               try {
                    image1 = Image.createImage("/logo-2.0.0-ffffffff.jpg");
               } catch (java.io.IOException e) {
                    e.printStackTrace();
               }
               // write post-init user code here
          }
          return image1;
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: imageItem ">
     /**
      * Returns an initiliazed instance of imageItem component.
      * @return the initialized component instance
      */
     public ImageItem getImageItem() {
          if (imageItem == null) {
               // write pre-init user code here
               imageItem = new ImageItem("", getImage1(), ImageItem.LAYOUT_DEFAULT, "<Missing Image>");
               // write post-init user code here
          }
          return imageItem;
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: c_insert ">
     /**
      * Returns an initiliazed instance of c_insert component.
      * @return the initialized component instance
      */
     public Command getC_insert() {
          if (c_insert == null) {
               // write pre-init user code here
               c_insert = new Command("\u0412\u0441\u0442\u0430\u0432\u0438\u0442\u044C", Command.BACK, 0);
               // write post-init user code here
          }
          return c_insert;
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: c_insert2 ">
     /**
      * Returns an initiliazed instance of c_insert2 component.
      * @return the initialized component instance
      */
     public Command getC_insert2() {
          if (c_insert2 == null) {
               // write pre-init user code here
               c_insert2 = new Command("Back", Command.BACK, 0);
               // write post-init user code here
          }
          return c_insert2;
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: c_delvar ">
     /**
      * Returns an initiliazed instance of c_delvar component.
      * @return the initialized component instance
      */
     public Command getC_delvar() {
          if (c_delvar == null) {
               // write pre-init user code here
               c_delvar = new Command("\u0423\u0434\u0430\u043B\u0438\u0442\u044C", Command.OK, 0);
               // write post-init user code here
          }
          return c_delvar;
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: a_error ">
     /**
      * Returns an initiliazed instance of a_error component.
      * @return the initialized component instance
      */
     public Alert getA_error() {
          if (a_error == null) {
               // write pre-init user code here
               a_error = new Alert("\u041E\u0448\u0438\u0431\u043A\u0430!");
               a_error.setTimeout(Alert.FOREVER);
               // write post-init user code here
          }
          return a_error;
     }
     //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc=" Generated Getter: form ">
     /**
      * Returns an initiliazed instance of form component.
      * @return the initialized component instance
      */
     public Form getForm() {
          if (form == null) {
               // write pre-init user code here
               form = new Form("form");
               form.addCommand(getC_tovars());
               form.addCommand(getC_history());
               form.setCommandListener(this);
               // write post-init user code here
          }
          return form;
     }
     //</editor-fold>

     //</editor-fold>
     /**
      * Returns a display instance.
      * @return the display instance.
      */
     public Display getDisplay() {
          return Display.getDisplay(this);
     }

     /**
      * Exits MIDlet.
      */
     public void exitMIDlet() {
          switchDisplayable(null, null);
          destroyApp(true);
          notifyDestroyed();
     }

     /**
      * Called when MIDlet is started.
      * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
      */
     public void startApp() {
          if (midletPaused) {
               resumeMIDlet();
          } else {
               initialize();
               startMIDlet();
          }
          midletPaused = false;
     }

     /**
      * Called when MIDlet is paused.
      */
     public void pauseApp() {
          midletPaused = true;
     }

     /**
      * Called to signal the MIDlet to terminate.
      * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
      */
     public void destroyApp(boolean unconditional) {
     }

     public Image getImage2() {
          if (image2 == null) {
               // write pre-init user code here
               try {
                    image2 = Image.createImage("/logo-inst-2.jpg");
               } catch (java.io.IOException e) {
                    e.printStackTrace();
               }
               // write post-init user code here
          }
          return image2;
     }

     public Command getC_dummy() {
          if (c_tovars == null) {
               // write pre-init user code here
               c_tovars = new Command("Enter", Command.CANCEL, 0);

          }
          return c_tovars;
     }
}
