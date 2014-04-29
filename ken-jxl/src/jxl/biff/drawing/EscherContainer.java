/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ 
/*   7:    */ class EscherContainer
/*   8:    */   extends EscherRecord
/*   9:    */ {
/*  10: 36 */   private static Logger logger = Logger.getLogger(EscherContainer.class);
/*  11:    */   private boolean initialized;
/*  12:    */   private ArrayList children;
/*  13:    */   
/*  14:    */   public EscherContainer(EscherRecordData erd)
/*  15:    */   {
/*  16: 56 */     super(erd);
/*  17: 57 */     this.initialized = false;
/*  18: 58 */     this.children = new ArrayList();
/*  19:    */   }
/*  20:    */   
/*  21:    */   protected EscherContainer(EscherRecordType type)
/*  22:    */   {
/*  23: 68 */     super(type);
/*  24: 69 */     setContainer(true);
/*  25: 70 */     this.children = new ArrayList();
/*  26:    */   }
/*  27:    */   
/*  28:    */   public EscherRecord[] getChildren()
/*  29:    */   {
/*  30: 80 */     if (!this.initialized) {
/*  31: 82 */       initialize();
/*  32:    */     }
/*  33: 85 */     Object[] ca = this.children.toArray(new EscherRecord[this.children.size()]);
/*  34:    */     
/*  35: 87 */     return (EscherRecord[])ca;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void add(EscherRecord child)
/*  39:    */   {
/*  40: 97 */     this.children.add(child);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void remove(EscherRecord child)
/*  44:    */   {
/*  45:107 */     boolean result = this.children.remove(child);
/*  46:    */   }
/*  47:    */   
/*  48:    */   private void initialize()
/*  49:    */   {
/*  50:115 */     int curpos = getPos() + 8;
/*  51:116 */     int endpos = Math.min(getPos() + getLength(), getStreamLength());
/*  52:    */     
/*  53:118 */     EscherRecord newRecord = null;
/*  54:120 */     while (curpos < endpos)
/*  55:    */     {
/*  56:122 */       EscherRecordData erd = new EscherRecordData(getEscherStream(), curpos);
/*  57:    */       
/*  58:124 */       EscherRecordType type = erd.getType();
/*  59:125 */       if (type == EscherRecordType.DGG) {
/*  60:127 */         newRecord = new Dgg(erd);
/*  61:129 */       } else if (type == EscherRecordType.DG) {
/*  62:131 */         newRecord = new Dg(erd);
/*  63:133 */       } else if (type == EscherRecordType.BSTORE_CONTAINER) {
/*  64:135 */         newRecord = new BStoreContainer(erd);
/*  65:137 */       } else if (type == EscherRecordType.SPGR_CONTAINER) {
/*  66:139 */         newRecord = new SpgrContainer(erd);
/*  67:141 */       } else if (type == EscherRecordType.SP_CONTAINER) {
/*  68:143 */         newRecord = new SpContainer(erd);
/*  69:145 */       } else if (type == EscherRecordType.SPGR) {
/*  70:147 */         newRecord = new Spgr(erd);
/*  71:149 */       } else if (type == EscherRecordType.SP) {
/*  72:151 */         newRecord = new Sp(erd);
/*  73:153 */       } else if (type == EscherRecordType.CLIENT_ANCHOR) {
/*  74:155 */         newRecord = new ClientAnchor(erd);
/*  75:157 */       } else if (type == EscherRecordType.CLIENT_DATA) {
/*  76:159 */         newRecord = new ClientData(erd);
/*  77:161 */       } else if (type == EscherRecordType.BSE) {
/*  78:163 */         newRecord = new BlipStoreEntry(erd);
/*  79:165 */       } else if (type == EscherRecordType.OPT) {
/*  80:167 */         newRecord = new Opt(erd);
/*  81:169 */       } else if (type == EscherRecordType.SPLIT_MENU_COLORS) {
/*  82:171 */         newRecord = new SplitMenuColors(erd);
/*  83:173 */       } else if (type == EscherRecordType.CLIENT_TEXT_BOX) {
/*  84:175 */         newRecord = new ClientTextBox(erd);
/*  85:    */       } else {
/*  86:179 */         newRecord = new EscherAtom(erd);
/*  87:    */       }
/*  88:182 */       this.children.add(newRecord);
/*  89:183 */       curpos += newRecord.getLength();
/*  90:    */     }
/*  91:186 */     this.initialized = true;
/*  92:    */   }
/*  93:    */   
/*  94:    */   byte[] getData()
/*  95:    */   {
/*  96:196 */     if (!this.initialized) {
/*  97:198 */       initialize();
/*  98:    */     }
/*  99:201 */     byte[] data = new byte[0];
/* 100:202 */     for (Iterator i = this.children.iterator(); i.hasNext();)
/* 101:    */     {
/* 102:204 */       EscherRecord er = (EscherRecord)i.next();
/* 103:205 */       byte[] childData = er.getData();
/* 104:207 */       if (childData != null)
/* 105:    */       {
/* 106:209 */         byte[] newData = new byte[data.length + childData.length];
/* 107:210 */         System.arraycopy(data, 0, newData, 0, data.length);
/* 108:211 */         System.arraycopy(childData, 0, newData, data.length, childData.length);
/* 109:212 */         data = newData;
/* 110:    */       }
/* 111:    */     }
/* 112:216 */     return setHeaderData(data);
/* 113:    */   }
/* 114:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.EscherContainer
 * JD-Core Version:    0.7.0.1
 */