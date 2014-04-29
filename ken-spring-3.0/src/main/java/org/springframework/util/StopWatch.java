/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.text.NumberFormat;
/*   4:    */ import java.util.LinkedList;
/*   5:    */ import java.util.List;
/*   6:    */ 
/*   7:    */ public class StopWatch
/*   8:    */ {
/*   9:    */   private final String id;
/*  10: 50 */   private boolean keepTaskList = true;
/*  11: 52 */   private final List<TaskInfo> taskList = new LinkedList();
/*  12:    */   private long startTimeMillis;
/*  13:    */   private boolean running;
/*  14:    */   private String currentTaskName;
/*  15:    */   private TaskInfo lastTaskInfo;
/*  16:    */   private int taskCount;
/*  17:    */   private long totalTimeMillis;
/*  18:    */   
/*  19:    */   public StopWatch()
/*  20:    */   {
/*  21: 75 */     this.id = "";
/*  22:    */   }
/*  23:    */   
/*  24:    */   public StopWatch(String id)
/*  25:    */   {
/*  26: 86 */     this.id = id;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setKeepTaskList(boolean keepTaskList)
/*  30:    */   {
/*  31: 96 */     this.keepTaskList = keepTaskList;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void start()
/*  35:    */     throws IllegalStateException
/*  36:    */   {
/*  37:106 */     start("");
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void start(String taskName)
/*  41:    */     throws IllegalStateException
/*  42:    */   {
/*  43:116 */     if (this.running) {
/*  44:117 */       throw new IllegalStateException("Can't start StopWatch: it's already running");
/*  45:    */     }
/*  46:119 */     this.startTimeMillis = System.currentTimeMillis();
/*  47:120 */     this.running = true;
/*  48:121 */     this.currentTaskName = taskName;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void stop()
/*  52:    */     throws IllegalStateException
/*  53:    */   {
/*  54:131 */     if (!this.running) {
/*  55:132 */       throw new IllegalStateException("Can't stop StopWatch: it's not running");
/*  56:    */     }
/*  57:134 */     long lastTime = System.currentTimeMillis() - this.startTimeMillis;
/*  58:135 */     this.totalTimeMillis += lastTime;
/*  59:136 */     this.lastTaskInfo = new TaskInfo(this.currentTaskName, lastTime);
/*  60:137 */     if (this.keepTaskList) {
/*  61:138 */       this.taskList.add(this.lastTaskInfo);
/*  62:    */     }
/*  63:140 */     this.taskCount += 1;
/*  64:141 */     this.running = false;
/*  65:142 */     this.currentTaskName = null;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public boolean isRunning()
/*  69:    */   {
/*  70:149 */     return this.running;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public long getLastTaskTimeMillis()
/*  74:    */     throws IllegalStateException
/*  75:    */   {
/*  76:157 */     if (this.lastTaskInfo == null) {
/*  77:158 */       throw new IllegalStateException("No tasks run: can't get last task interval");
/*  78:    */     }
/*  79:160 */     return this.lastTaskInfo.getTimeMillis();
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String getLastTaskName()
/*  83:    */     throws IllegalStateException
/*  84:    */   {
/*  85:167 */     if (this.lastTaskInfo == null) {
/*  86:168 */       throw new IllegalStateException("No tasks run: can't get last task name");
/*  87:    */     }
/*  88:170 */     return this.lastTaskInfo.getTaskName();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public TaskInfo getLastTaskInfo()
/*  92:    */     throws IllegalStateException
/*  93:    */   {
/*  94:177 */     if (this.lastTaskInfo == null) {
/*  95:178 */       throw new IllegalStateException("No tasks run: can't get last task info");
/*  96:    */     }
/*  97:180 */     return this.lastTaskInfo;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public long getTotalTimeMillis()
/* 101:    */   {
/* 102:188 */     return this.totalTimeMillis;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public double getTotalTimeSeconds()
/* 106:    */   {
/* 107:195 */     return this.totalTimeMillis / 1000.0D;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public int getTaskCount()
/* 111:    */   {
/* 112:202 */     return this.taskCount;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public TaskInfo[] getTaskInfo()
/* 116:    */   {
/* 117:209 */     if (!this.keepTaskList) {
/* 118:210 */       throw new UnsupportedOperationException("Task info is not being kept!");
/* 119:    */     }
/* 120:212 */     return (TaskInfo[])this.taskList.toArray(new TaskInfo[this.taskList.size()]);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public String shortSummary()
/* 124:    */   {
/* 125:220 */     return "StopWatch '" + this.id + "': running time (millis) = " + getTotalTimeMillis();
/* 126:    */   }
/* 127:    */   
/* 128:    */   public String prettyPrint()
/* 129:    */   {
/* 130:228 */     StringBuilder sb = new StringBuilder(shortSummary());
/* 131:229 */     sb.append('\n');
/* 132:230 */     if (!this.keepTaskList)
/* 133:    */     {
/* 134:231 */       sb.append("No task info kept");
/* 135:    */     }
/* 136:    */     else
/* 137:    */     {
/* 138:233 */       sb.append("-----------------------------------------\n");
/* 139:234 */       sb.append("ms     %     Task name\n");
/* 140:235 */       sb.append("-----------------------------------------\n");
/* 141:236 */       NumberFormat nf = NumberFormat.getNumberInstance();
/* 142:237 */       nf.setMinimumIntegerDigits(5);
/* 143:238 */       nf.setGroupingUsed(false);
/* 144:239 */       NumberFormat pf = NumberFormat.getPercentInstance();
/* 145:240 */       pf.setMinimumIntegerDigits(3);
/* 146:241 */       pf.setGroupingUsed(false);
/* 147:242 */       for (TaskInfo task : getTaskInfo())
/* 148:    */       {
/* 149:243 */         sb.append(nf.format(task.getTimeMillis())).append("  ");
/* 150:244 */         sb.append(pf.format(task.getTimeSeconds() / getTotalTimeSeconds())).append("  ");
/* 151:245 */         sb.append(task.getTaskName()).append("\n");
/* 152:    */       }
/* 153:    */     }
/* 154:248 */     return sb.toString();
/* 155:    */   }
/* 156:    */   
/* 157:    */   public String toString()
/* 158:    */   {
/* 159:257 */     StringBuilder sb = new StringBuilder(shortSummary());
/* 160:258 */     if (this.keepTaskList) {
/* 161:259 */       for (TaskInfo task : getTaskInfo())
/* 162:    */       {
/* 163:260 */         sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeMillis());
/* 164:261 */         long percent = Math.round(100.0D * task.getTimeSeconds() / getTotalTimeSeconds());
/* 165:262 */         sb.append(" = ").append(percent).append("%");
/* 166:    */       }
/* 167:    */     } else {
/* 168:265 */       sb.append("; no task info kept");
/* 169:    */     }
/* 170:267 */     return sb.toString();
/* 171:    */   }
/* 172:    */   
/* 173:    */   public static final class TaskInfo
/* 174:    */   {
/* 175:    */     private final String taskName;
/* 176:    */     private final long timeMillis;
/* 177:    */     
/* 178:    */     TaskInfo(String taskName, long timeMillis)
/* 179:    */     {
/* 180:281 */       this.taskName = taskName;
/* 181:282 */       this.timeMillis = timeMillis;
/* 182:    */     }
/* 183:    */     
/* 184:    */     public String getTaskName()
/* 185:    */     {
/* 186:289 */       return this.taskName;
/* 187:    */     }
/* 188:    */     
/* 189:    */     public long getTimeMillis()
/* 190:    */     {
/* 191:296 */       return this.timeMillis;
/* 192:    */     }
/* 193:    */     
/* 194:    */     public double getTimeSeconds()
/* 195:    */     {
/* 196:303 */       return this.timeMillis / 1000.0D;
/* 197:    */     }
/* 198:    */   }
/* 199:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.StopWatch
 * JD-Core Version:    0.7.0.1
 */