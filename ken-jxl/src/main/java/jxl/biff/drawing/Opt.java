/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.biff.StringHelper;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ 
/*   9:    */ class Opt
/*  10:    */   extends EscherAtom
/*  11:    */ {
/*  12: 38 */   private static Logger logger = Logger.getLogger(Opt.class);
/*  13:    */   private byte[] data;
/*  14:    */   private int numProperties;
/*  15:    */   private ArrayList properties;
/*  16:    */   
/*  17:    */   static final class Property
/*  18:    */   {
/*  19:    */     int id;
/*  20:    */     boolean blipId;
/*  21:    */     boolean complex;
/*  22:    */     int value;
/*  23:    */     String stringValue;
/*  24:    */     
/*  25:    */     public Property(int i, boolean bl, boolean co, int v)
/*  26:    */     {
/*  27: 76 */       this.id = i;
/*  28: 77 */       this.blipId = bl;
/*  29: 78 */       this.complex = co;
/*  30: 79 */       this.value = v;
/*  31:    */     }
/*  32:    */     
/*  33:    */     public Property(int i, boolean bl, boolean co, int v, String s)
/*  34:    */     {
/*  35: 93 */       this.id = i;
/*  36: 94 */       this.blipId = bl;
/*  37: 95 */       this.complex = co;
/*  38: 96 */       this.value = v;
/*  39: 97 */       this.stringValue = s;
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Opt(EscherRecordData erd)
/*  44:    */   {
/*  45:108 */     super(erd);
/*  46:109 */     this.numProperties = getInstance();
/*  47:110 */     readProperties();
/*  48:    */   }
/*  49:    */   
/*  50:    */   private void readProperties()
/*  51:    */   {
/*  52:118 */     this.properties = new ArrayList();
/*  53:119 */     int pos = 0;
/*  54:120 */     byte[] bytes = getBytes();
/*  55:122 */     for (int i = 0; i < this.numProperties; i++)
/*  56:    */     {
/*  57:124 */       int val = IntegerHelper.getInt(bytes[pos], bytes[(pos + 1)]);
/*  58:125 */       int id = val & 0x3FFF;
/*  59:126 */       int value = IntegerHelper.getInt(bytes[(pos + 2)], bytes[(pos + 3)], 
/*  60:127 */         bytes[(pos + 4)], bytes[(pos + 5)]);
/*  61:128 */       Property p = new Property(id, 
/*  62:129 */         (val & 0x4000) != 0, 
/*  63:130 */         (val & 0x8000) != 0, 
/*  64:131 */         value);
/*  65:132 */       pos += 6;
/*  66:133 */       this.properties.add(p);
/*  67:    */     }
/*  68:136 */     for (Iterator i = this.properties.iterator(); i.hasNext();)
/*  69:    */     {
/*  70:138 */       Property p = (Property)i.next();
/*  71:139 */       if (p.complex)
/*  72:    */       {
/*  73:141 */         p.stringValue = StringHelper.getUnicodeString(bytes, p.value / 2, 
/*  74:142 */           pos);
/*  75:143 */         pos += p.value;
/*  76:    */       }
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Opt()
/*  81:    */   {
/*  82:153 */     super(EscherRecordType.OPT);
/*  83:154 */     this.properties = new ArrayList();
/*  84:155 */     setVersion(3);
/*  85:    */   }
/*  86:    */   
/*  87:    */   byte[] getData()
/*  88:    */   {
/*  89:165 */     this.numProperties = this.properties.size();
/*  90:166 */     setInstance(this.numProperties);
/*  91:    */     
/*  92:168 */     this.data = new byte[this.numProperties * 6];
/*  93:169 */     int pos = 0;
/*  94:172 */     for (Iterator i = this.properties.iterator(); i.hasNext();)
/*  95:    */     {
/*  96:174 */       Property p = (Property)i.next();
/*  97:175 */       int val = p.id & 0x3FFF;
/*  98:177 */       if (p.blipId) {
/*  99:179 */         val |= 0x4000;
/* 100:    */       }
/* 101:182 */       if (p.complex) {
/* 102:184 */         val |= 0x8000;
/* 103:    */       }
/* 104:187 */       IntegerHelper.getTwoBytes(val, this.data, pos);
/* 105:188 */       IntegerHelper.getFourBytes(p.value, this.data, pos + 2);
/* 106:189 */       pos += 6;
/* 107:    */     }
/* 108:193 */     for (Iterator i = this.properties.iterator(); i.hasNext();)
/* 109:    */     {
/* 110:195 */       Property p = (Property)i.next();
/* 111:197 */       if ((p.complex) && (p.stringValue != null))
/* 112:    */       {
/* 113:199 */         byte[] newData = 
/* 114:200 */           new byte[this.data.length + p.stringValue.length() * 2];
/* 115:201 */         System.arraycopy(this.data, 0, newData, 0, this.data.length);
/* 116:202 */         StringHelper.getUnicodeBytes(p.stringValue, newData, this.data.length);
/* 117:203 */         this.data = newData;
/* 118:    */       }
/* 119:    */     }
/* 120:207 */     return setHeaderData(this.data);
/* 121:    */   }
/* 122:    */   
/* 123:    */   void addProperty(int id, boolean blip, boolean complex, int val)
/* 124:    */   {
/* 125:220 */     Property p = new Property(id, blip, complex, val);
/* 126:221 */     this.properties.add(p);
/* 127:    */   }
/* 128:    */   
/* 129:    */   void addProperty(int id, boolean blip, boolean complex, int val, String s)
/* 130:    */   {
/* 131:235 */     Property p = new Property(id, blip, complex, val, s);
/* 132:236 */     this.properties.add(p);
/* 133:    */   }
/* 134:    */   
/* 135:    */   Property getProperty(int id)
/* 136:    */   {
/* 137:247 */     boolean found = false;
/* 138:248 */     Property p = null;
/* 139:249 */     for (Iterator i = this.properties.iterator(); (i.hasNext()) && (!found);)
/* 140:    */     {
/* 141:251 */       p = (Property)i.next();
/* 142:252 */       if (p.id == id) {
/* 143:254 */         found = true;
/* 144:    */       }
/* 145:    */     }
/* 146:257 */     return found ? p : null;
/* 147:    */   }
/* 148:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.Opt
 * JD-Core Version:    0.7.0.1
 */