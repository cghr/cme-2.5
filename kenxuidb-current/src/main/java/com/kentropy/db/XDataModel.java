/*   1:    */ package com.kentropy.db;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.util.StringTokenizer;
/*   5:    */ import net.xoetrope.xui.data.XBaseModel;
/*   6:    */ import net.xoetrope.xui.data.XModel;
/*   7:    */ 
/*   8:    */ public class XDataModel
/*   9:    */   extends XBaseModel
/*  10:    */ {
/*  11:    */   public String table;
/*  12:    */   public String where;
/*  13: 12 */   public String task = "";
/*  14: 13 */   public String assignedTo = "";
/*  15: 14 */   public String area = "";
/*  16: 15 */   public String house = "";
/*  17: 16 */   public String household = "";
/*  18: 17 */   public String member = "";
/*  19: 18 */   public boolean fetched = false;
/*  20:    */   
/*  21:    */   public XTaskModel createProto()
/*  22:    */   {
/*  23: 22 */     return null;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void save()
/*  27:    */     throws Exception
/*  28:    */   {
/*  29: 28 */     TestXUIDB.getInstance().saveInterview1(this);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public Object get()
/*  33:    */   {
/*  34: 33 */     System.out.println("Debug Get called");
/*  35:    */     
/*  36: 35 */     return super.get();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public XModel get(int arg0)
/*  40:    */   {
/*  41: 40 */     return super.get(arg0);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Object getWithCreate(String arg0)
/*  45:    */   {
/*  46: 45 */     System.out.println("Debug Get1 called " + arg0);
/*  47: 46 */     if ((arg0 != null) && (!arg0.startsWith("@")))
/*  48:    */     {
/*  49: 48 */       StringTokenizer st = new StringTokenizer(arg0, "/");
/*  50: 49 */       String task = "";
/*  51: 50 */       if (st.hasMoreTokens()) {
/*  52: 51 */         task = st.nextToken();
/*  53:    */       } else {
/*  54: 55 */         task = arg0;
/*  55:    */       }
/*  56:    */     }
/*  57: 60 */     return super.get(arg0);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public Object append(String arg0)
/*  61:    */   {
/*  62: 65 */     return super.append(arg0);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void append(XModel arg0)
/*  66:    */   {
/*  67: 70 */     System.out.println("Append1 Called" + arg0.getId());
/*  68: 71 */     super.append(arg0);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public Object get(String arg0)
/*  72:    */   {
/*  73: 76 */     System.out.println("Debug Get1 called " + arg0);
/*  74: 78 */     if ((arg0 != null) && (!arg0.startsWith("@")))
/*  75:    */     {
/*  76: 80 */       StringTokenizer st = new StringTokenizer(arg0, "/");
/*  77: 81 */       String currentId = arg0;
/*  78: 82 */       if (st.hasMoreTokens()) {
/*  79: 84 */         currentId = st.nextToken();
/*  80:    */       }
/*  81: 86 */       XDataModel child = TestXUIDB.getInstance().getDataM("data", currentId, (String)get("@area"), (String)get("@house"), (String)get("@household"), (String)get("@member"));
/*  82: 87 */       if (child != null)
/*  83:    */       {
/*  84: 89 */         child.setId(currentId);
/*  85: 90 */         append(child);
/*  86:    */       }
/*  87:    */     }
/*  88: 95 */     return super.get(arg0);
/*  89:    */   }
/*  90:    */   
/*  91:    */   public String getId()
/*  92:    */   {
/*  93:100 */     return super.getId();
/*  94:    */   }
/*  95:    */   
/*  96:    */   public int getNumChildren()
/*  97:    */   {
/*  98:105 */     System.out.println(" Get Num called " + this.task);
/*  99:    */     
/* 100:107 */     return super.getNumChildren();
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void set(Object arg0)
/* 104:    */   {
/* 105:112 */     super.set(arg0);
/* 106:    */     try
/* 107:    */     {
/* 108:114 */       if (arg0 != null) {
/* 109:115 */         save();
/* 110:    */       }
/* 111:    */     }
/* 112:    */     catch (Exception e)
/* 113:    */     {
/* 114:119 */       e.printStackTrace();
/* 115:    */     }
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void set(String arg0, Object arg1)
/* 119:    */   {
/* 120:125 */     super.set(arg0, arg1);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public void setId(String arg0)
/* 124:    */   {
/* 125:130 */     super.setId(arg0);
/* 126:    */   }
/* 127:    */   
/* 128:    */   protected Object clone()
/* 129:    */     throws CloneNotSupportedException
/* 130:    */   {
/* 131:136 */     return super.clone();
/* 132:    */   }
/* 133:    */   
/* 134:    */   public static XDataModel getRoot(String name)
/* 135:    */   {
/* 136:141 */     XDataModel dataM = new XDataModel();
/* 137:    */     
/* 138:143 */     dataM.setId("tasks");
/* 139:    */     
/* 140:145 */     return dataM;
/* 141:    */   }
/* 142:    */   
/* 143:    */   public static void main(String[] args)
/* 144:    */   {
/* 145:149 */     XBaseModel xtm = new XBaseModel();
/* 146:    */     
/* 147:151 */     XDataModel tM = getRoot("12");
/* 148:152 */     System.out.println("-----------------------------------");
/* 149:153 */     System.out.println(tM.get("test"));
/* 150:154 */     System.out.println("-----------------------------------");
/* 151:155 */     System.out.println(tM.get("test/test"));
/* 152:    */   }
/* 153:    */   
/* 154:    */   public String getArea()
/* 155:    */   {
/* 156:160 */     return this.area;
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void setArea(String area)
/* 160:    */   {
/* 161:165 */     this.area = area;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public String getAssignedTo()
/* 165:    */   {
/* 166:170 */     return this.assignedTo;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void setAssignedTo(String assignedTo)
/* 170:    */   {
/* 171:175 */     this.assignedTo = assignedTo;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public String getHouse()
/* 175:    */   {
/* 176:180 */     return this.house;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void setHouse(String house)
/* 180:    */   {
/* 181:185 */     this.house = house;
/* 182:    */   }
/* 183:    */   
/* 184:    */   public String getHousehold()
/* 185:    */   {
/* 186:190 */     return this.household;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public void setHousehold(String household)
/* 190:    */   {
/* 191:195 */     this.household = household;
/* 192:    */   }
/* 193:    */   
/* 194:    */   public String getMember()
/* 195:    */   {
/* 196:200 */     return this.member;
/* 197:    */   }
/* 198:    */   
/* 199:    */   public void setMember(String member)
/* 200:    */   {
/* 201:205 */     this.member = member;
/* 202:    */   }
/* 203:    */   
/* 204:    */   public String getTask()
/* 205:    */   {
/* 206:210 */     return this.task;
/* 207:    */   }
/* 208:    */   
/* 209:    */   public void setTask(String task)
/* 210:    */   {
/* 211:215 */     this.task = task;
/* 212:    */   }
/* 213:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.db.XDataModel
 * JD-Core Version:    0.7.0.1
 */