package com.grevzin.expression;

/*
 * cnst.java
 * 2.4.3
 * Настройки алогритмов разбора и подсчета выражений.
 */

public class cnst {

    // <editor-fold defaultstate="collapsed" desc="КОП Управление 1-9">
    public static final int NULL = 1; /*Оператор перехода.*/

    public static final int ERR = 2; /* Оператор ошибки. */// </editor-fold>
    static final int OPCODE_CONT_DATATYPE = 10;
    // <editor-fold defaultstate="collapsed" desc="КОП Типы данных 10-100">
    static final int NUMBER = 11;
    static final int OPERATOR = 12;
    static final int FUNCTION = 13;
    static final int VARIABLE = 14;
    static final int UNDEF_ABC = 15; // </editor-fold>
    static final int OPCODE_DATATYPE_ARITHM = 100, OPCODE_OPER_MIN = 100;
    // <editor-fold defaultstate="collapsed" desc="КОП Арифметика. 101-199">
/*Аримфетические функциии.*/
    static final int PLUS = 101;
    static final int MINUS = 102;
    static final int MULTIPLY = 103;
    static final int DIVIDE = 104;
    static final int ROOT = 105;
    static final int POWER = 106;
    static final int SQRT = 107;
    static final int TDRT = 108;
    static final int FTRT = 109;// </editor-fold>
    static final int OPCODE_ARITHM_COMMONF = 200, OPCODE_FUNC_MIN = 200;
    // <editor-fold defaultstate="collapsed" desc="КОП Неклассифицированные функции 201-499">
    static final int ABS = 201;
    static final int Z = 202; /* Целая часть числа. */

    static final int FRA = 203; /*Дробная часть числа. */

    static final int FLOOR = 204;
    static final int CEIL = 205;
    static final int FACTOR = 206;
    static final int GCD = 207;
    static final int LN = 208;
    static final int LOG = 209;
    static final int GAMMA = 210;
    static final int REMAINED = 211;
    static final int ZDIV = 212;
    static final int TODEG = 213;
    static final int TORAD = 214;
    // </editor-fold>
    static final int OPCODE_COMMONF_TRIGF = 500;
    // <editor-fold defaultstate="collapsed" desc="КОП Тригонометрия 501-699">
    static final int COS = 501;
    static final int SIN = 502;
    static final int TAN = 503;
    static final int CTN = 504;
    static final int ACOS = 505;
    static final int ASIN = 506;
    static final int ATAN = 507;
    static final int ACTN = 508;
    static final int SC = 509;
    static final int CSC = 510;
    static final int ASC = 511;
    static final int ACSC = 512;
    // </editor-fold>
    static final int OPCODE_MAX = 700, OPCODE_FUNC_MAX = 700;
    /* Оффсеты для опкодной промышленности. */
    static final int OPCODE_ARITHM_OFFSET = 101;
    static final int OPCODE_COMMON_OFFSET = 201;
    static final int OPCODE_TRIG_OFFSET = 501;

    /* Типы символов. */
    static final int SY_ABC = 0; 
    static final int SY_NUM = 1; 
    static final int SY_DEL = 2;
    static final int SY_OPE = 3; 
    static final int SY_SPC = 4; 
    static final int SY_UNK = 5; 

    /* Константы базового приоритета операторов. */
    static final int PR_PLUS_MINUS = 5;
    static final int PR_MULT_DIV = 20;
    static final int PR_POWER = 60;
    static final int PR_FUNCTION = 90;
    static final int PR_VAR = 95;
    static final String[] RESTRICTED_VARS = {"pi", "пи", "e", /*Русская*/ "е", "ans", "по"};

    /* Имена функций. */
    static final String[] COMMON_FUNCTION_NAMES = {"abs", "&&", "&&", "&&&", "&&&", "fac", "&&&&", "ln", "log", "&&&", "&&&", "&&&","tdeg","trad"};
    static final String[] TRIGONOMETRIC_FUNCTION_NAMES = {"cos", "sin", "tg", "ctg", "arccos", "arcsin", "arctg", "arcctg"};

    /* Маскимальное созможное количество переменных. */
    static final int MAX_VAR_NUMBER = 30;
}
