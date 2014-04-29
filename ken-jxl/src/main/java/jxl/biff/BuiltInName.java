/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ public class BuiltInName
/*   4:    */ {
/*   5:    */   private String name;
/*   6:    */   private int value;
/*   7: 40 */   private static BuiltInName[] builtInNames = new BuiltInName[0];
/*   8:    */   
/*   9:    */   private BuiltInName(String n, int v)
/*  10:    */   {
/*  11: 46 */     this.name = n;
/*  12: 47 */     this.value = v;
/*  13:    */     
/*  14: 49 */     BuiltInName[] oldnames = builtInNames;
/*  15: 50 */     builtInNames = new BuiltInName[oldnames.length + 1];
/*  16: 51 */     System.arraycopy(oldnames, 0, builtInNames, 0, oldnames.length);
/*  17: 52 */     builtInNames[oldnames.length] = this;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public String getName()
/*  21:    */   {
/*  22: 62 */     return this.name;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public int getValue()
/*  26:    */   {
/*  27: 72 */     return this.value;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static BuiltInName getBuiltInName(int val)
/*  31:    */   {
/*  32: 80 */     BuiltInName ret = FILTER_DATABASE;
/*  33: 81 */     for (int i = 0; i < builtInNames.length; i++) {
/*  34: 83 */       if (builtInNames[i].getValue() == val) {
/*  35: 85 */         ret = builtInNames[i];
/*  36:    */       }
/*  37:    */     }
/*  38: 88 */     return ret;
/*  39:    */   }
/*  40:    */   
/*  41: 93 */   public static final BuiltInName CONSOLIDATE_AREA = new BuiltInName("Consolidate_Area", 0);
/*  42: 95 */   public static final BuiltInName AUTO_OPEN = new BuiltInName("Auto_Open", 1);
/*  43: 97 */   public static final BuiltInName AUTO_CLOSE = new BuiltInName("Auto_Open", 2);
/*  44: 99 */   public static final BuiltInName EXTRACT = new BuiltInName("Extract", 3);
/*  45:101 */   public static final BuiltInName DATABASE = new BuiltInName("Database", 4);
/*  46:103 */   public static final BuiltInName CRITERIA = new BuiltInName("Criteria", 5);
/*  47:105 */   public static final BuiltInName PRINT_AREA = new BuiltInName("Print_Area", 6);
/*  48:107 */   public static final BuiltInName PRINT_TITLES = new BuiltInName("Print_Titles", 7);
/*  49:109 */   public static final BuiltInName RECORDER = new BuiltInName("Recorder", 8);
/*  50:111 */   public static final BuiltInName DATA_FORM = new BuiltInName("Data_Form", 9);
/*  51:113 */   public static final BuiltInName AUTO_ACTIVATE = new BuiltInName("Auto_Activate", 10);
/*  52:115 */   public static final BuiltInName AUTO_DEACTIVATE = new BuiltInName("Auto_Deactivate", 11);
/*  53:117 */   public static final BuiltInName SHEET_TITLE = new BuiltInName("Sheet_Title", 11);
/*  54:119 */   public static final BuiltInName FILTER_DATABASE = new BuiltInName("_FilterDatabase", 13);
/*  55:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.BuiltInName
 * JD-Core Version:    0.7.0.1
 */