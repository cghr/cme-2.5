/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.common.Assert;
/*   4:    */ import jxl.common.Logger;
/*   5:    */ 
/*   6:    */ public abstract class BaseCompoundFile
/*   7:    */ {
/*   8: 33 */   private static Logger logger = Logger.getLogger(BaseCompoundFile.class);
/*   9: 39 */   protected static final byte[] IDENTIFIER = { -48, 
/*  10: 40 */     -49, 
/*  11: 41 */     17, 
/*  12: 42 */     -32, 
/*  13: 43 */     -95, 
/*  14: 44 */     -79, 
/*  15: 45 */     26, 
/*  16: 46 */     -31 };
/*  17:    */   protected static final int NUM_BIG_BLOCK_DEPOT_BLOCKS_POS = 44;
/*  18:    */   protected static final int SMALL_BLOCK_DEPOT_BLOCK_POS = 60;
/*  19:    */   protected static final int NUM_SMALL_BLOCK_DEPOT_BLOCKS_POS = 64;
/*  20:    */   protected static final int ROOT_START_BLOCK_POS = 48;
/*  21:    */   protected static final int BIG_BLOCK_SIZE = 512;
/*  22:    */   protected static final int SMALL_BLOCK_SIZE = 64;
/*  23:    */   protected static final int EXTENSION_BLOCK_POS = 68;
/*  24:    */   protected static final int NUM_EXTENSION_BLOCK_POS = 72;
/*  25:    */   protected static final int PROPERTY_STORAGE_BLOCK_SIZE = 128;
/*  26:    */   protected static final int BIG_BLOCK_DEPOT_BLOCKS_POS = 76;
/*  27:    */   protected static final int SMALL_BLOCK_THRESHOLD = 4096;
/*  28:    */   private static final int SIZE_OF_NAME_POS = 64;
/*  29:    */   private static final int TYPE_POS = 66;
/*  30:    */   private static final int COLOUR_POS = 67;
/*  31:    */   private static final int PREVIOUS_POS = 68;
/*  32:    */   private static final int NEXT_POS = 72;
/*  33:    */   private static final int CHILD_POS = 76;
/*  34:    */   private static final int START_BLOCK_POS = 116;
/*  35:    */   private static final int SIZE_POS = 120;
/*  36:    */   public static final String ROOT_ENTRY_NAME = "Root Entry";
/*  37:    */   public static final String WORKBOOK_NAME = "Workbook";
/*  38:    */   public static final String SUMMARY_INFORMATION_NAME = "\005SummaryInformation";
/*  39:    */   public static final String DOCUMENT_SUMMARY_INFORMATION_NAME = "\005DocumentSummaryInformation";
/*  40:    */   public static final String COMP_OBJ_NAME = "\001CompObj";
/*  41:119 */   public static final String[] STANDARD_PROPERTY_SETS = { "Root Entry", "Workbook", 
/*  42:120 */     "\005SummaryInformation", 
/*  43:121 */     "\005DocumentSummaryInformation" };
/*  44:    */   public static final int NONE_PS_TYPE = 0;
/*  45:    */   public static final int DIRECTORY_PS_TYPE = 1;
/*  46:    */   public static final int FILE_PS_TYPE = 2;
/*  47:    */   public static final int ROOT_ENTRY_PS_TYPE = 5;
/*  48:    */   
/*  49:    */   public class PropertyStorage
/*  50:    */   {
/*  51:    */     public String name;
/*  52:    */     public int type;
/*  53:    */     public int colour;
/*  54:    */     public int startBlock;
/*  55:    */     public int size;
/*  56:    */     public int previous;
/*  57:    */     public int next;
/*  58:    */     public int child;
/*  59:    */     public byte[] data;
/*  60:    */     
/*  61:    */     public PropertyStorage(byte[] d)
/*  62:    */     {
/*  63:183 */       this.data = d;
/*  64:184 */       int nameSize = IntegerHelper.getInt(this.data[64], 
/*  65:185 */         this.data[65]);
/*  66:187 */       if (nameSize > 64)
/*  67:    */       {
/*  68:189 */         BaseCompoundFile.logger.warn("property set name exceeds max length - truncating");
/*  69:190 */         nameSize = 64;
/*  70:    */       }
/*  71:193 */       this.type = this.data[66];
/*  72:194 */       this.colour = this.data[67];
/*  73:    */       
/*  74:196 */       this.startBlock = IntegerHelper.getInt(
/*  75:197 */         this.data[116], 
/*  76:198 */         this.data[117], 
/*  77:199 */         this.data[118], 
/*  78:200 */         this.data[119]);
/*  79:201 */       this.size = IntegerHelper.getInt(
/*  80:202 */         this.data[120], 
/*  81:203 */         this.data[121], 
/*  82:204 */         this.data[122], 
/*  83:205 */         this.data[123]);
/*  84:206 */       this.previous = IntegerHelper.getInt(
/*  85:207 */         this.data[68], 
/*  86:208 */         this.data[69], 
/*  87:209 */         this.data[70], 
/*  88:210 */         this.data[71]);
/*  89:211 */       this.next = IntegerHelper.getInt(
/*  90:212 */         this.data[72], 
/*  91:213 */         this.data[73], 
/*  92:214 */         this.data[74], 
/*  93:215 */         this.data[75]);
/*  94:216 */       this.child = IntegerHelper.getInt(
/*  95:217 */         this.data[76], 
/*  96:218 */         this.data[77], 
/*  97:219 */         this.data[78], 
/*  98:220 */         this.data[79]);
/*  99:    */       
/* 100:222 */       int chars = 0;
/* 101:223 */       if (nameSize > 2) {
/* 102:225 */         chars = (nameSize - 1) / 2;
/* 103:    */       }
/* 104:228 */       StringBuffer n = new StringBuffer("");
/* 105:229 */       for (int i = 0; i < chars; i++) {
/* 106:231 */         n.append((char)this.data[(i * 2)]);
/* 107:    */       }
/* 108:234 */       this.name = n.toString();
/* 109:    */     }
/* 110:    */     
/* 111:    */     public PropertyStorage(String name)
/* 112:    */     {
/* 113:244 */       this.data = new byte[''];
/* 114:    */       
/* 115:246 */       Assert.verify(name.length() < 32);
/* 116:    */       
/* 117:248 */       IntegerHelper.getTwoBytes((name.length() + 1) * 2, 
/* 118:249 */         this.data, 
/* 119:250 */         64);
/* 120:253 */       for (int i = 0; i < name.length(); i++) {
/* 121:255 */         this.data[(i * 2)] = ((byte)name.charAt(i));
/* 122:    */       }
/* 123:    */     }
/* 124:    */     
/* 125:    */     public void setType(int t)
/* 126:    */     {
/* 127:266 */       this.type = t;
/* 128:267 */       this.data[66] = ((byte)t);
/* 129:    */     }
/* 130:    */     
/* 131:    */     public void setStartBlock(int sb)
/* 132:    */     {
/* 133:277 */       this.startBlock = sb;
/* 134:278 */       IntegerHelper.getFourBytes(sb, this.data, 116);
/* 135:    */     }
/* 136:    */     
/* 137:    */     public void setSize(int s)
/* 138:    */     {
/* 139:288 */       this.size = s;
/* 140:289 */       IntegerHelper.getFourBytes(s, this.data, 120);
/* 141:    */     }
/* 142:    */     
/* 143:    */     public void setPrevious(int prev)
/* 144:    */     {
/* 145:299 */       this.previous = prev;
/* 146:300 */       IntegerHelper.getFourBytes(prev, this.data, 68);
/* 147:    */     }
/* 148:    */     
/* 149:    */     public void setNext(int nxt)
/* 150:    */     {
/* 151:310 */       this.next = nxt;
/* 152:311 */       IntegerHelper.getFourBytes(this.next, this.data, 72);
/* 153:    */     }
/* 154:    */     
/* 155:    */     public void setChild(int dir)
/* 156:    */     {
/* 157:321 */       this.child = dir;
/* 158:322 */       IntegerHelper.getFourBytes(this.child, this.data, 76);
/* 159:    */     }
/* 160:    */     
/* 161:    */     public void setColour(int col)
/* 162:    */     {
/* 163:332 */       this.colour = (col == 0 ? 0 : 1);
/* 164:333 */       this.data[67] = ((byte)this.colour);
/* 165:    */     }
/* 166:    */   }
/* 167:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.BaseCompoundFile
 * JD-Core Version:    0.7.0.1
 */