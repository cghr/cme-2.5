/*   1:    */ package org.springframework.scheduling.support;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.BitSet;
/*   6:    */ import java.util.Calendar;
/*   7:    */ import java.util.Collections;
/*   8:    */ import java.util.Date;
/*   9:    */ import java.util.GregorianCalendar;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import java.util.List;
/*  12:    */ import java.util.TimeZone;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ 
/*  15:    */ public class CronSequenceGenerator
/*  16:    */ {
/*  17: 56 */   private final BitSet seconds = new BitSet(60);
/*  18: 58 */   private final BitSet minutes = new BitSet(60);
/*  19: 60 */   private final BitSet hours = new BitSet(24);
/*  20: 62 */   private final BitSet daysOfWeek = new BitSet(7);
/*  21: 64 */   private final BitSet daysOfMonth = new BitSet(31);
/*  22: 66 */   private final BitSet months = new BitSet(12);
/*  23:    */   private final String expression;
/*  24:    */   private final TimeZone timeZone;
/*  25:    */   
/*  26:    */   public CronSequenceGenerator(String expression, TimeZone timeZone)
/*  27:    */   {
/*  28: 79 */     this.expression = expression;
/*  29: 80 */     this.timeZone = timeZone;
/*  30: 81 */     parse(expression);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public Date next(Date date)
/*  34:    */   {
/*  35:111 */     Calendar calendar = new GregorianCalendar();
/*  36:112 */     calendar.setTimeZone(this.timeZone);
/*  37:113 */     calendar.setTime(date);
/*  38:    */     
/*  39:    */ 
/*  40:116 */     calendar.add(13, 1);
/*  41:117 */     calendar.set(14, 0);
/*  42:    */     
/*  43:119 */     doNext(calendar, calendar.get(1));
/*  44:    */     
/*  45:121 */     return calendar.getTime();
/*  46:    */   }
/*  47:    */   
/*  48:    */   private void doNext(Calendar calendar, int dot)
/*  49:    */   {
/*  50:125 */     List<Integer> resets = new ArrayList();
/*  51:    */     
/*  52:127 */     int second = calendar.get(13);
/*  53:128 */     List<Integer> emptyList = Collections.emptyList();
/*  54:129 */     int updateSecond = findNext(this.seconds, second, calendar, 13, 12, emptyList);
/*  55:130 */     if (second == updateSecond) {
/*  56:131 */       resets.add(Integer.valueOf(13));
/*  57:    */     }
/*  58:134 */     int minute = calendar.get(12);
/*  59:135 */     int updateMinute = findNext(this.minutes, minute, calendar, 12, 11, resets);
/*  60:136 */     if (minute == updateMinute) {
/*  61:137 */       resets.add(Integer.valueOf(12));
/*  62:    */     } else {
/*  63:139 */       doNext(calendar, dot);
/*  64:    */     }
/*  65:142 */     int hour = calendar.get(11);
/*  66:143 */     int updateHour = findNext(this.hours, hour, calendar, 11, 7, resets);
/*  67:144 */     if (hour == updateHour) {
/*  68:145 */       resets.add(Integer.valueOf(11));
/*  69:    */     } else {
/*  70:147 */       doNext(calendar, dot);
/*  71:    */     }
/*  72:150 */     int dayOfWeek = calendar.get(7);
/*  73:151 */     int dayOfMonth = calendar.get(5);
/*  74:152 */     int updateDayOfMonth = findNextDay(calendar, this.daysOfMonth, dayOfMonth, this.daysOfWeek, dayOfWeek, resets);
/*  75:153 */     if (dayOfMonth == updateDayOfMonth) {
/*  76:154 */       resets.add(Integer.valueOf(5));
/*  77:    */     } else {
/*  78:156 */       doNext(calendar, dot);
/*  79:    */     }
/*  80:159 */     int month = calendar.get(2);
/*  81:160 */     int updateMonth = findNext(this.months, month, calendar, 2, 1, resets);
/*  82:161 */     if (month != updateMonth)
/*  83:    */     {
/*  84:162 */       if (calendar.get(1) - dot > 4) {
/*  85:163 */         throw new IllegalStateException("Invalid cron expression led to runaway search for next trigger");
/*  86:    */       }
/*  87:165 */       doNext(calendar, dot);
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   private int findNextDay(Calendar calendar, BitSet daysOfMonth, int dayOfMonth, BitSet daysOfWeek, int dayOfWeek, List<Integer> resets)
/*  92:    */   {
/*  93:173 */     int count = 0;
/*  94:174 */     int max = 366;
/*  95:177 */     while (((!daysOfMonth.get(dayOfMonth)) || (!daysOfWeek.get(dayOfWeek - 1))) && (count++ < max))
/*  96:    */     {
/*  97:178 */       calendar.add(5, 1);
/*  98:179 */       dayOfMonth = calendar.get(5);
/*  99:180 */       dayOfWeek = calendar.get(7);
/* 100:181 */       reset(calendar, resets);
/* 101:    */     }
/* 102:183 */     if (count >= max) {
/* 103:184 */       throw new IllegalStateException("Overflow in day for expression=" + this.expression);
/* 104:    */     }
/* 105:186 */     return dayOfMonth;
/* 106:    */   }
/* 107:    */   
/* 108:    */   private int findNext(BitSet bits, int value, Calendar calendar, int field, int nextField, List<Integer> lowerOrders)
/* 109:    */   {
/* 110:202 */     int nextValue = bits.nextSetBit(value);
/* 111:204 */     if (nextValue == -1)
/* 112:    */     {
/* 113:205 */       calendar.add(nextField, 1);
/* 114:206 */       reset(calendar, Arrays.asList(new Integer[] { Integer.valueOf(field) }));
/* 115:207 */       nextValue = bits.nextSetBit(0);
/* 116:    */     }
/* 117:209 */     if (nextValue != value)
/* 118:    */     {
/* 119:210 */       calendar.set(field, nextValue);
/* 120:211 */       reset(calendar, lowerOrders);
/* 121:    */     }
/* 122:213 */     return nextValue;
/* 123:    */   }
/* 124:    */   
/* 125:    */   private void reset(Calendar calendar, List<Integer> fields)
/* 126:    */   {
/* 127:220 */     for (Iterator localIterator = fields.iterator(); localIterator.hasNext();)
/* 128:    */     {
/* 129:220 */       int field = ((Integer)localIterator.next()).intValue();
/* 130:221 */       calendar.set(field, field == 5 ? 1 : 0);
/* 131:    */     }
/* 132:    */   }
/* 133:    */   
/* 134:    */   private void parse(String expression)
/* 135:    */     throws IllegalArgumentException
/* 136:    */   {
/* 137:231 */     String[] fields = StringUtils.tokenizeToStringArray(expression, " ");
/* 138:232 */     if (fields.length != 6) {
/* 139:233 */       throw new IllegalArgumentException(
/* 140:234 */         String.format("cron expression must consist of 6 fields (found %d in %s)", new Object[] {Integer.valueOf(fields.length), expression }));
/* 141:    */     }
/* 142:236 */     setNumberHits(this.seconds, fields[0], 0, 60);
/* 143:237 */     setNumberHits(this.minutes, fields[1], 0, 60);
/* 144:238 */     setNumberHits(this.hours, fields[2], 0, 24);
/* 145:239 */     setDaysOfMonth(this.daysOfMonth, fields[3]);
/* 146:240 */     setMonths(this.months, fields[4]);
/* 147:241 */     setDays(this.daysOfWeek, replaceOrdinals(fields[5], "SUN,MON,TUE,WED,THU,FRI,SAT"), 8);
/* 148:242 */     if (this.daysOfWeek.get(7))
/* 149:    */     {
/* 150:244 */       this.daysOfWeek.set(0);
/* 151:245 */       this.daysOfWeek.clear(7);
/* 152:    */     }
/* 153:    */   }
/* 154:    */   
/* 155:    */   private String replaceOrdinals(String value, String commaSeparatedList)
/* 156:    */   {
/* 157:255 */     String[] list = StringUtils.commaDelimitedListToStringArray(commaSeparatedList);
/* 158:256 */     for (int i = 0; i < list.length; i++)
/* 159:    */     {
/* 160:257 */       String item = list[i].toUpperCase();
/* 161:258 */       value = StringUtils.replace(value.toUpperCase(), item, i);
/* 162:    */     }
/* 163:260 */     return value;
/* 164:    */   }
/* 165:    */   
/* 166:    */   private void setDaysOfMonth(BitSet bits, String field)
/* 167:    */   {
/* 168:264 */     int max = 31;
/* 169:    */     
/* 170:266 */     setDays(bits, field, max + 1);
/* 171:    */     
/* 172:268 */     bits.clear(0);
/* 173:    */   }
/* 174:    */   
/* 175:    */   private void setDays(BitSet bits, String field, int max)
/* 176:    */   {
/* 177:272 */     if (field.contains("?")) {
/* 178:273 */       field = "*";
/* 179:    */     }
/* 180:275 */     setNumberHits(bits, field, 0, max);
/* 181:    */   }
/* 182:    */   
/* 183:    */   private void setMonths(BitSet bits, String value)
/* 184:    */   {
/* 185:279 */     int max = 12;
/* 186:280 */     value = replaceOrdinals(value, "FOO,JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC");
/* 187:281 */     BitSet months = new BitSet(13);
/* 188:    */     
/* 189:283 */     setNumberHits(months, value, 1, max + 1);
/* 190:285 */     for (int i = 1; i <= max; i++) {
/* 191:286 */       if (months.get(i)) {
/* 192:287 */         bits.set(i - 1);
/* 193:    */       }
/* 194:    */     }
/* 195:    */   }
/* 196:    */   
/* 197:    */   private void setNumberHits(BitSet bits, String value, int min, int max)
/* 198:    */   {
/* 199:293 */     String[] fields = StringUtils.delimitedListToStringArray(value, ",");
/* 200:294 */     for (String field : fields) {
/* 201:295 */       if (!field.contains("/"))
/* 202:    */       {
/* 203:297 */         int[] range = getRange(field, min, max);
/* 204:298 */         bits.set(range[0], range[1] + 1);
/* 205:    */       }
/* 206:    */       else
/* 207:    */       {
/* 208:300 */         String[] split = StringUtils.delimitedListToStringArray(field, "/");
/* 209:301 */         if (split.length > 2) {
/* 210:302 */           throw new IllegalArgumentException("Incrementer has more than two fields: " + field);
/* 211:    */         }
/* 212:304 */         int[] range = getRange(split[0], min, max);
/* 213:305 */         if (!split[0].contains("-")) {
/* 214:306 */           range[1] = (max - 1);
/* 215:    */         }
/* 216:308 */         int delta = Integer.valueOf(split[1]).intValue();
/* 217:309 */         for (int i = range[0]; i <= range[1]; i += delta) {
/* 218:310 */           bits.set(i);
/* 219:    */         }
/* 220:    */       }
/* 221:    */     }
/* 222:    */   }
/* 223:    */   
/* 224:    */   private int[] getRange(String field, int min, int max)
/* 225:    */   {
/* 226:317 */     int[] result = new int[2];
/* 227:318 */     if (field.contains("*"))
/* 228:    */     {
/* 229:319 */       result[0] = min;
/* 230:320 */       result[1] = (max - 1);
/* 231:321 */       return result;
/* 232:    */     }
/* 233:323 */     if (!field.contains("-"))
/* 234:    */     {
/* 235:324 */       int tmp53_50 = Integer.valueOf(field).intValue();result[1] = tmp53_50;result[0] = tmp53_50;
/* 236:    */     }
/* 237:    */     else
/* 238:    */     {
/* 239:326 */       String[] split = StringUtils.delimitedListToStringArray(field, "-");
/* 240:327 */       if (split.length > 2) {
/* 241:328 */         throw new IllegalArgumentException("Range has more than two fields: " + field);
/* 242:    */       }
/* 243:330 */       result[0] = Integer.valueOf(split[0]).intValue();
/* 244:331 */       result[1] = Integer.valueOf(split[1]).intValue();
/* 245:    */     }
/* 246:333 */     if ((result[0] >= max) || (result[1] >= max)) {
/* 247:334 */       throw new IllegalArgumentException("Range exceeds maximum (" + max + "): " + field);
/* 248:    */     }
/* 249:336 */     if ((result[0] < min) || (result[1] < min)) {
/* 250:337 */       throw new IllegalArgumentException("Range less than minimum (" + min + "): " + field);
/* 251:    */     }
/* 252:339 */     return result;
/* 253:    */   }
/* 254:    */   
/* 255:    */   public boolean equals(Object obj)
/* 256:    */   {
/* 257:344 */     if (!(obj instanceof CronSequenceGenerator)) {
/* 258:345 */       return false;
/* 259:    */     }
/* 260:347 */     CronSequenceGenerator cron = (CronSequenceGenerator)obj;
/* 261:    */     
/* 262:    */ 
/* 263:350 */     return (cron.months.equals(this.months)) && (cron.daysOfMonth.equals(this.daysOfMonth)) && (cron.daysOfWeek.equals(this.daysOfWeek)) && (cron.hours.equals(this.hours)) && (cron.minutes.equals(this.minutes)) && (cron.seconds.equals(this.seconds));
/* 264:    */   }
/* 265:    */   
/* 266:    */   public int hashCode()
/* 267:    */   {
/* 268:355 */     return 37 + 17 * this.months.hashCode() + 29 * this.daysOfMonth.hashCode() + 37 * this.daysOfWeek.hashCode() + 
/* 269:356 */       41 * this.hours.hashCode() + 53 * this.minutes.hashCode() + 61 * this.seconds.hashCode();
/* 270:    */   }
/* 271:    */   
/* 272:    */   public String toString()
/* 273:    */   {
/* 274:361 */     return getClass().getSimpleName() + ": " + this.expression;
/* 275:    */   }
/* 276:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.support.CronSequenceGenerator
 * JD-Core Version:    0.7.0.1
 */