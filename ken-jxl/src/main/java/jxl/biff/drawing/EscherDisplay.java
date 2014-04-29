/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import java.io.BufferedWriter;
/*   4:    */ import java.io.IOException;
/*   5:    */ 
/*   6:    */ public class EscherDisplay
/*   7:    */ {
/*   8:    */   private EscherStream stream;
/*   9:    */   private BufferedWriter writer;
/*  10:    */   
/*  11:    */   public EscherDisplay(EscherStream s, BufferedWriter bw)
/*  12:    */   {
/*  13: 51 */     this.stream = s;
/*  14: 52 */     this.writer = bw;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public void display()
/*  18:    */     throws IOException
/*  19:    */   {
/*  20: 62 */     EscherRecordData er = new EscherRecordData(this.stream, 0);
/*  21: 63 */     EscherContainer ec = new EscherContainer(er);
/*  22: 64 */     displayContainer(ec, 0);
/*  23:    */   }
/*  24:    */   
/*  25:    */   private void displayContainer(EscherContainer ec, int level)
/*  26:    */     throws IOException
/*  27:    */   {
/*  28: 77 */     displayRecord(ec, level);
/*  29:    */     
/*  30:    */ 
/*  31: 80 */     level++;
/*  32:    */     
/*  33: 82 */     EscherRecord[] children = ec.getChildren();
/*  34: 84 */     for (int i = 0; i < children.length; i++)
/*  35:    */     {
/*  36: 86 */       EscherRecord er = children[i];
/*  37: 87 */       if (er.getEscherData().isContainer()) {
/*  38: 89 */         displayContainer((EscherContainer)er, level);
/*  39:    */       } else {
/*  40: 93 */         displayRecord(er, level);
/*  41:    */       }
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   private void displayRecord(EscherRecord er, int level)
/*  46:    */     throws IOException
/*  47:    */   {
/*  48:108 */     indent(level);
/*  49:    */     
/*  50:110 */     EscherRecordType type = er.getType();
/*  51:    */     
/*  52:    */ 
/*  53:113 */     this.writer.write(Integer.toString(type.getValue(), 16));
/*  54:114 */     this.writer.write(" - ");
/*  55:117 */     if (type == EscherRecordType.DGG_CONTAINER)
/*  56:    */     {
/*  57:119 */       this.writer.write("Dgg Container");
/*  58:120 */       this.writer.newLine();
/*  59:    */     }
/*  60:122 */     else if (type == EscherRecordType.BSTORE_CONTAINER)
/*  61:    */     {
/*  62:124 */       this.writer.write("BStore Container");
/*  63:125 */       this.writer.newLine();
/*  64:    */     }
/*  65:127 */     else if (type == EscherRecordType.DG_CONTAINER)
/*  66:    */     {
/*  67:129 */       this.writer.write("Dg Container");
/*  68:130 */       this.writer.newLine();
/*  69:    */     }
/*  70:132 */     else if (type == EscherRecordType.SPGR_CONTAINER)
/*  71:    */     {
/*  72:134 */       this.writer.write("Spgr Container");
/*  73:135 */       this.writer.newLine();
/*  74:    */     }
/*  75:137 */     else if (type == EscherRecordType.SP_CONTAINER)
/*  76:    */     {
/*  77:139 */       this.writer.write("Sp Container");
/*  78:140 */       this.writer.newLine();
/*  79:    */     }
/*  80:142 */     else if (type == EscherRecordType.DGG)
/*  81:    */     {
/*  82:144 */       this.writer.write("Dgg");
/*  83:145 */       this.writer.newLine();
/*  84:    */     }
/*  85:147 */     else if (type == EscherRecordType.BSE)
/*  86:    */     {
/*  87:149 */       this.writer.write("Bse");
/*  88:150 */       this.writer.newLine();
/*  89:    */     }
/*  90:152 */     else if (type == EscherRecordType.DG)
/*  91:    */     {
/*  92:154 */       Dg dg = new Dg(er.getEscherData());
/*  93:155 */       this.writer.write("Dg:  drawing id " + dg.getDrawingId() + 
/*  94:156 */         " shape count " + dg.getShapeCount());
/*  95:157 */       this.writer.newLine();
/*  96:    */     }
/*  97:159 */     else if (type == EscherRecordType.SPGR)
/*  98:    */     {
/*  99:161 */       this.writer.write("Spgr");
/* 100:162 */       this.writer.newLine();
/* 101:    */     }
/* 102:164 */     else if (type == EscherRecordType.SP)
/* 103:    */     {
/* 104:166 */       Sp sp = new Sp(er.getEscherData());
/* 105:167 */       this.writer.write("Sp:  shape id " + sp.getShapeId() + 
/* 106:168 */         " shape type " + sp.getShapeType());
/* 107:169 */       this.writer.newLine();
/* 108:    */     }
/* 109:171 */     else if (type == EscherRecordType.OPT)
/* 110:    */     {
/* 111:173 */       Opt opt = new Opt(er.getEscherData());
/* 112:174 */       Opt.Property p260 = opt.getProperty(260);
/* 113:175 */       Opt.Property p261 = opt.getProperty(261);
/* 114:176 */       this.writer.write("Opt (value, stringValue): ");
/* 115:177 */       if (p260 != null) {
/* 116:179 */         this.writer.write("260: " + 
/* 117:180 */           p260.value + ", " + 
/* 118:181 */           p260.stringValue + 
/* 119:182 */           ";");
/* 120:    */       }
/* 121:184 */       if (p261 != null) {
/* 122:186 */         this.writer.write("261: " + 
/* 123:187 */           p261.value + ", " + 
/* 124:188 */           p261.stringValue + 
/* 125:189 */           ";");
/* 126:    */       }
/* 127:191 */       this.writer.newLine();
/* 128:    */     }
/* 129:193 */     else if (type == EscherRecordType.CLIENT_ANCHOR)
/* 130:    */     {
/* 131:195 */       this.writer.write("Client Anchor");
/* 132:196 */       this.writer.newLine();
/* 133:    */     }
/* 134:198 */     else if (type == EscherRecordType.CLIENT_DATA)
/* 135:    */     {
/* 136:200 */       this.writer.write("Client Data");
/* 137:201 */       this.writer.newLine();
/* 138:    */     }
/* 139:203 */     else if (type == EscherRecordType.CLIENT_TEXT_BOX)
/* 140:    */     {
/* 141:205 */       this.writer.write("Client Text Box");
/* 142:206 */       this.writer.newLine();
/* 143:    */     }
/* 144:208 */     else if (type == EscherRecordType.SPLIT_MENU_COLORS)
/* 145:    */     {
/* 146:210 */       this.writer.write("Split Menu Colors");
/* 147:211 */       this.writer.newLine();
/* 148:    */     }
/* 149:    */     else
/* 150:    */     {
/* 151:215 */       this.writer.write("???");
/* 152:216 */       this.writer.newLine();
/* 153:    */     }
/* 154:    */   }
/* 155:    */   
/* 156:    */   private void indent(int level)
/* 157:    */     throws IOException
/* 158:    */   {
/* 159:228 */     for (int i = 0; i < level * 2; i++) {
/* 160:230 */       this.writer.write(32);
/* 161:    */     }
/* 162:    */   }
/* 163:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.EscherDisplay
 * JD-Core Version:    0.7.0.1
 */