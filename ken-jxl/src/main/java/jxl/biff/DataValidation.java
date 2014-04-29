/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import jxl.WorkbookSettings;
/*   7:    */ import jxl.biff.formula.ExternalSheet;
/*   8:    */ import jxl.common.Assert;
/*   9:    */ import jxl.common.Logger;
/*  10:    */ import jxl.write.biff.File;
/*  11:    */ 
/*  12:    */ public class DataValidation
/*  13:    */ {
/*  14: 44 */   private static Logger logger = Logger.getLogger(DataValidation.class);
/*  15:    */   private DataValidityListRecord validityList;
/*  16:    */   private ArrayList validitySettings;
/*  17:    */   private WorkbookMethods workbook;
/*  18:    */   private ExternalSheet externalSheet;
/*  19:    */   private WorkbookSettings workbookSettings;
/*  20:    */   private int comboBoxObjectId;
/*  21:    */   private boolean copied;
/*  22:    */   public static final int DEFAULT_OBJECT_ID = -1;
/*  23:    */   private static final int MAX_NO_OF_VALIDITY_SETTINGS = 65533;
/*  24:    */   
/*  25:    */   public DataValidation(DataValidityListRecord dvlr)
/*  26:    */   {
/*  27: 90 */     this.validityList = dvlr;
/*  28: 91 */     this.validitySettings = new ArrayList(this.validityList.getNumberOfSettings());
/*  29: 92 */     this.copied = false;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public DataValidation(int objId, ExternalSheet es, WorkbookMethods wm, WorkbookSettings ws)
/*  33:    */   {
/*  34:103 */     this.workbook = wm;
/*  35:104 */     this.externalSheet = es;
/*  36:105 */     this.workbookSettings = ws;
/*  37:106 */     this.validitySettings = new ArrayList();
/*  38:107 */     this.comboBoxObjectId = objId;
/*  39:108 */     this.copied = false;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public DataValidation(DataValidation dv, ExternalSheet es, WorkbookMethods wm, WorkbookSettings ws)
/*  43:    */   {
/*  44:119 */     this.workbook = wm;
/*  45:120 */     this.externalSheet = es;
/*  46:121 */     this.workbookSettings = ws;
/*  47:122 */     this.copied = true;
/*  48:123 */     this.validityList = new DataValidityListRecord(dv.getDataValidityList());
/*  49:    */     
/*  50:125 */     this.validitySettings = new ArrayList();
/*  51:126 */     DataValiditySettingsRecord[] settings = dv.getDataValiditySettings();
/*  52:128 */     for (int i = 0; i < settings.length; i++) {
/*  53:130 */       this.validitySettings.add(new DataValiditySettingsRecord(settings[i], 
/*  54:131 */         this.externalSheet, 
/*  55:132 */         this.workbook, 
/*  56:133 */         this.workbookSettings));
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void add(DataValiditySettingsRecord dvsr)
/*  61:    */   {
/*  62:142 */     this.validitySettings.add(dvsr);
/*  63:143 */     dvsr.setDataValidation(this);
/*  64:145 */     if (this.copied)
/*  65:    */     {
/*  66:148 */       Assert.verify(this.validityList != null);
/*  67:149 */       this.validityList.dvAdded();
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   public DataValidityListRecord getDataValidityList()
/*  72:    */   {
/*  73:158 */     return this.validityList;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public DataValiditySettingsRecord[] getDataValiditySettings()
/*  77:    */   {
/*  78:166 */     DataValiditySettingsRecord[] dvlr = new DataValiditySettingsRecord[0];
/*  79:167 */     return (DataValiditySettingsRecord[])this.validitySettings.toArray(dvlr);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void write(File outputFile)
/*  83:    */     throws IOException
/*  84:    */   {
/*  85:178 */     if (this.validitySettings.size() > 65533)
/*  86:    */     {
/*  87:180 */       logger.warn("Maximum number of data validations exceeded - truncating...");
/*  88:    */       
/*  89:182 */       this.validitySettings = new ArrayList(
/*  90:183 */         this.validitySettings.subList(0, 65532));
/*  91:184 */       Assert.verify(this.validitySettings.size() <= 65533);
/*  92:    */     }
/*  93:187 */     if (this.validityList == null)
/*  94:    */     {
/*  95:189 */       DValParser dvp = new DValParser(this.comboBoxObjectId, 
/*  96:190 */         this.validitySettings.size());
/*  97:191 */       this.validityList = new DataValidityListRecord(dvp);
/*  98:    */     }
/*  99:194 */     if (!this.validityList.hasDVRecords()) {
/* 100:196 */       return;
/* 101:    */     }
/* 102:199 */     outputFile.write(this.validityList);
/* 103:201 */     for (Iterator i = this.validitySettings.iterator(); i.hasNext();)
/* 104:    */     {
/* 105:203 */       DataValiditySettingsRecord dvsr = (DataValiditySettingsRecord)i.next();
/* 106:204 */       outputFile.write(dvsr);
/* 107:    */     }
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void insertRow(int row)
/* 111:    */   {
/* 112:215 */     for (Iterator i = this.validitySettings.iterator(); i.hasNext();)
/* 113:    */     {
/* 114:217 */       DataValiditySettingsRecord dv = (DataValiditySettingsRecord)i.next();
/* 115:218 */       dv.insertRow(row);
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void removeRow(int row)
/* 120:    */   {
/* 121:229 */     for (Iterator i = this.validitySettings.iterator(); i.hasNext();)
/* 122:    */     {
/* 123:231 */       DataValiditySettingsRecord dv = (DataValiditySettingsRecord)i.next();
/* 124:233 */       if ((dv.getFirstRow() == row) && (dv.getLastRow() == row))
/* 125:    */       {
/* 126:235 */         i.remove();
/* 127:236 */         this.validityList.dvRemoved();
/* 128:    */       }
/* 129:    */       else
/* 130:    */       {
/* 131:240 */         dv.removeRow(row);
/* 132:    */       }
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void insertColumn(int col)
/* 137:    */   {
/* 138:252 */     for (Iterator i = this.validitySettings.iterator(); i.hasNext();)
/* 139:    */     {
/* 140:254 */       DataValiditySettingsRecord dv = (DataValiditySettingsRecord)i.next();
/* 141:255 */       dv.insertColumn(col);
/* 142:    */     }
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void removeColumn(int col)
/* 146:    */   {
/* 147:266 */     for (Iterator i = this.validitySettings.iterator(); i.hasNext();)
/* 148:    */     {
/* 149:268 */       DataValiditySettingsRecord dv = (DataValiditySettingsRecord)i.next();
/* 150:270 */       if ((dv.getFirstColumn() == col) && (dv.getLastColumn() == col))
/* 151:    */       {
/* 152:272 */         i.remove();
/* 153:273 */         this.validityList.dvRemoved();
/* 154:    */       }
/* 155:    */       else
/* 156:    */       {
/* 157:277 */         dv.removeColumn(col);
/* 158:    */       }
/* 159:    */     }
/* 160:    */   }
/* 161:    */   
/* 162:    */   public void removeDataValidation(int col, int row)
/* 163:    */   {
/* 164:290 */     for (Iterator i = this.validitySettings.iterator(); i.hasNext();)
/* 165:    */     {
/* 166:292 */       DataValiditySettingsRecord dv = (DataValiditySettingsRecord)i.next();
/* 167:294 */       if ((dv.getFirstColumn() == col) && (dv.getLastColumn() == col) && 
/* 168:295 */         (dv.getFirstRow() == row) && (dv.getLastRow() == row))
/* 169:    */       {
/* 170:297 */         i.remove();
/* 171:298 */         this.validityList.dvRemoved();
/* 172:299 */         break;
/* 173:    */       }
/* 174:    */     }
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void removeSharedDataValidation(int col1, int row1, int col2, int row2)
/* 178:    */   {
/* 179:313 */     for (Iterator i = this.validitySettings.iterator(); i.hasNext();)
/* 180:    */     {
/* 181:315 */       DataValiditySettingsRecord dv = (DataValiditySettingsRecord)i.next();
/* 182:317 */       if ((dv.getFirstColumn() == col1) && (dv.getLastColumn() == col2) && 
/* 183:318 */         (dv.getFirstRow() == row1) && (dv.getLastRow() == row2))
/* 184:    */       {
/* 185:320 */         i.remove();
/* 186:321 */         this.validityList.dvRemoved();
/* 187:322 */         break;
/* 188:    */       }
/* 189:    */     }
/* 190:    */   }
/* 191:    */   
/* 192:    */   public DataValiditySettingsRecord getDataValiditySettings(int col, int row)
/* 193:    */   {
/* 194:333 */     boolean found = false;
/* 195:334 */     DataValiditySettingsRecord foundRecord = null;
/* 196:335 */     for (Iterator i = this.validitySettings.iterator(); (i.hasNext()) && (!found);)
/* 197:    */     {
/* 198:337 */       DataValiditySettingsRecord dvsr = (DataValiditySettingsRecord)i.next();
/* 199:338 */       if ((dvsr.getFirstColumn() == col) && (dvsr.getFirstRow() == row))
/* 200:    */       {
/* 201:340 */         found = true;
/* 202:341 */         foundRecord = dvsr;
/* 203:    */       }
/* 204:    */     }
/* 205:345 */     return foundRecord;
/* 206:    */   }
/* 207:    */   
/* 208:    */   public int getComboBoxObjectId()
/* 209:    */   {
/* 210:353 */     return this.comboBoxObjectId;
/* 211:    */   }
/* 212:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.DataValidation
 * JD-Core Version:    0.7.0.1
 */