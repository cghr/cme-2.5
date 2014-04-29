/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.common.Logger;
/*   5:    */ 
/*   6:    */ final class Function
/*   7:    */ {
/*   8: 34 */   private static Logger logger = Logger.getLogger(Function.class);
/*   9:    */   private final int code;
/*  10:    */   private final String name;
/*  11:    */   private final int numArgs;
/*  12: 56 */   private static Function[] functions = new Function[0];
/*  13:    */   
/*  14:    */   private Function(int v, String s, int a)
/*  15:    */   {
/*  16: 68 */     this.code = v;
/*  17: 69 */     this.name = s;
/*  18: 70 */     this.numArgs = a;
/*  19:    */     
/*  20:    */ 
/*  21: 73 */     Function[] newarray = new Function[functions.length + 1];
/*  22: 74 */     System.arraycopy(functions, 0, newarray, 0, functions.length);
/*  23: 75 */     newarray[functions.length] = this;
/*  24: 76 */     functions = newarray;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public int hashCode()
/*  28:    */   {
/*  29: 86 */     return this.code;
/*  30:    */   }
/*  31:    */   
/*  32:    */   int getCode()
/*  33:    */   {
/*  34: 96 */     return this.code;
/*  35:    */   }
/*  36:    */   
/*  37:    */   String getPropertyName()
/*  38:    */   {
/*  39:107 */     return this.name;
/*  40:    */   }
/*  41:    */   
/*  42:    */   String getName(WorkbookSettings ws)
/*  43:    */   {
/*  44:117 */     FunctionNames fn = ws.getFunctionNames();
/*  45:118 */     return fn.getName(this);
/*  46:    */   }
/*  47:    */   
/*  48:    */   int getNumArgs()
/*  49:    */   {
/*  50:128 */     return this.numArgs;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public static Function getFunction(int v)
/*  54:    */   {
/*  55:139 */     Function f = null;
/*  56:141 */     for (int i = 0; i < functions.length; i++) {
/*  57:143 */       if (functions[i].code == v)
/*  58:    */       {
/*  59:145 */         f = functions[i];
/*  60:146 */         break;
/*  61:    */       }
/*  62:    */     }
/*  63:150 */     return f != null ? f : UNKNOWN;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static Function getFunction(String v, WorkbookSettings ws)
/*  67:    */   {
/*  68:162 */     FunctionNames fn = ws.getFunctionNames();
/*  69:163 */     Function f = fn.getFunction(v);
/*  70:164 */     return f != null ? f : UNKNOWN;
/*  71:    */   }
/*  72:    */   
/*  73:    */   static Function[] getFunctions()
/*  74:    */   {
/*  75:175 */     return functions;
/*  76:    */   }
/*  77:    */   
/*  78:181 */   public static final Function COUNT = new Function(0, "count", 255);
/*  79:182 */   public static final Function ATTRIBUTE = new Function(1, "", 255);
/*  80:184 */   public static final Function ISNA = new Function(2, "isna", 1);
/*  81:186 */   public static final Function ISERROR = new Function(3, "iserror", 1);
/*  82:188 */   public static final Function SUM = new Function(4, "sum", 255);
/*  83:190 */   public static final Function AVERAGE = new Function(5, "average", 255);
/*  84:192 */   public static final Function MIN = new Function(6, "min", 255);
/*  85:194 */   public static final Function MAX = new Function(7, "max", 255);
/*  86:196 */   public static final Function ROW = new Function(8, "row", 255);
/*  87:198 */   public static final Function COLUMN = new Function(9, "column", 255);
/*  88:200 */   public static final Function NA = new Function(10, "na", 0);
/*  89:202 */   public static final Function NPV = new Function(11, "npv", 255);
/*  90:204 */   public static final Function STDEV = new Function(12, "stdev", 255);
/*  91:206 */   public static final Function DOLLAR = new Function(13, "dollar", 2);
/*  92:208 */   public static final Function FIXED = new Function(14, "fixed", 255);
/*  93:210 */   public static final Function SIN = new Function(15, "sin", 1);
/*  94:212 */   public static final Function COS = new Function(16, "cos", 1);
/*  95:214 */   public static final Function TAN = new Function(17, "tan", 1);
/*  96:216 */   public static final Function ATAN = new Function(18, "atan", 1);
/*  97:218 */   public static final Function PI = new Function(19, "pi", 0);
/*  98:220 */   public static final Function SQRT = new Function(20, "sqrt", 1);
/*  99:222 */   public static final Function EXP = new Function(21, "exp", 1);
/* 100:224 */   public static final Function LN = new Function(22, "ln", 1);
/* 101:226 */   public static final Function LOG10 = new Function(23, "log10", 1);
/* 102:228 */   public static final Function ABS = new Function(24, "abs", 1);
/* 103:230 */   public static final Function INT = new Function(25, "int", 1);
/* 104:232 */   public static final Function SIGN = new Function(26, "sign", 1);
/* 105:234 */   public static final Function ROUND = new Function(27, "round", 2);
/* 106:236 */   public static final Function LOOKUP = new Function(28, "lookup", 2);
/* 107:238 */   public static final Function INDEX = new Function(29, "index", 3);
/* 108:239 */   public static final Function REPT = new Function(30, "rept", 2);
/* 109:241 */   public static final Function MID = new Function(31, "mid", 3);
/* 110:243 */   public static final Function LEN = new Function(32, "len", 1);
/* 111:245 */   public static final Function VALUE = new Function(33, "value", 1);
/* 112:247 */   public static final Function TRUE = new Function(34, "true", 0);
/* 113:249 */   public static final Function FALSE = new Function(35, "false", 0);
/* 114:251 */   public static final Function AND = new Function(36, "and", 255);
/* 115:253 */   public static final Function OR = new Function(37, "or", 255);
/* 116:255 */   public static final Function NOT = new Function(38, "not", 1);
/* 117:257 */   public static final Function MOD = new Function(39, "mod", 2);
/* 118:259 */   public static final Function DCOUNT = new Function(40, "dcount", 3);
/* 119:261 */   public static final Function DSUM = new Function(41, "dsum", 3);
/* 120:263 */   public static final Function DAVERAGE = new Function(42, "daverage", 3);
/* 121:265 */   public static final Function DMIN = new Function(43, "dmin", 3);
/* 122:267 */   public static final Function DMAX = new Function(44, "dmax", 3);
/* 123:269 */   public static final Function DSTDEV = new Function(45, "dstdev", 3);
/* 124:271 */   public static final Function VAR = new Function(46, "var", 255);
/* 125:273 */   public static final Function DVAR = new Function(47, "dvar", 3);
/* 126:275 */   public static final Function TEXT = new Function(48, "text", 2);
/* 127:277 */   public static final Function LINEST = new Function(49, "linest", 255);
/* 128:279 */   public static final Function TREND = new Function(50, "trend", 255);
/* 129:281 */   public static final Function LOGEST = new Function(51, "logest", 255);
/* 130:283 */   public static final Function GROWTH = new Function(52, "growth", 255);
/* 131:287 */   public static final Function PV = new Function(56, "pv", 255);
/* 132:289 */   public static final Function FV = new Function(57, "fv", 255);
/* 133:291 */   public static final Function NPER = new Function(58, "nper", 255);
/* 134:293 */   public static final Function PMT = new Function(59, "pmt", 255);
/* 135:295 */   public static final Function RATE = new Function(60, "rate", 255);
/* 136:299 */   public static final Function RAND = new Function(63, "rand", 0);
/* 137:301 */   public static final Function MATCH = new Function(64, "match", 3);
/* 138:303 */   public static final Function DATE = new Function(65, "date", 3);
/* 139:305 */   public static final Function TIME = new Function(66, "time", 3);
/* 140:307 */   public static final Function DAY = new Function(67, "day", 1);
/* 141:309 */   public static final Function MONTH = new Function(68, "month", 1);
/* 142:311 */   public static final Function YEAR = new Function(69, "year", 1);
/* 143:313 */   public static final Function WEEKDAY = new Function(70, "weekday", 2);
/* 144:315 */   public static final Function HOUR = new Function(71, "hour", 1);
/* 145:317 */   public static final Function MINUTE = new Function(72, "minute", 1);
/* 146:319 */   public static final Function SECOND = new Function(73, "second", 1);
/* 147:321 */   public static final Function NOW = new Function(74, "now", 0);
/* 148:323 */   public static final Function AREAS = new Function(75, "areas", 255);
/* 149:325 */   public static final Function ROWS = new Function(76, "rows", 1);
/* 150:327 */   public static final Function COLUMNS = new Function(77, "columns", 255);
/* 151:329 */   public static final Function OFFSET = new Function(78, "offset", 255);
/* 152:333 */   public static final Function SEARCH = new Function(82, "search", 255);
/* 153:335 */   public static final Function TRANSPOSE = new Function(83, "transpose", 255);
/* 154:337 */   public static final Function ERROR = new Function(84, "error", 1);
/* 155:340 */   public static final Function TYPE = new Function(86, "type", 1);
/* 156:352 */   public static final Function ATAN2 = new Function(97, "atan2", 1);
/* 157:354 */   public static final Function ASIN = new Function(98, "asin", 1);
/* 158:356 */   public static final Function ACOS = new Function(99, "acos", 1);
/* 159:358 */   public static final Function CHOOSE = new Function(100, "choose", 255);
/* 160:360 */   public static final Function HLOOKUP = new Function(101, "hlookup", 255);
/* 161:362 */   public static final Function VLOOKUP = new Function(102, "vlookup", 255);
/* 162:366 */   public static final Function ISREF = new Function(105, "isref", 1);
/* 163:371 */   public static final Function LOG = new Function(109, "log", 255);
/* 164:374 */   public static final Function CHAR = new Function(111, "char", 1);
/* 165:376 */   public static final Function LOWER = new Function(112, "lower", 1);
/* 166:378 */   public static final Function UPPER = new Function(113, "upper", 1);
/* 167:380 */   public static final Function PROPER = new Function(114, "proper", 1);
/* 168:382 */   public static final Function LEFT = new Function(115, "left", 255);
/* 169:384 */   public static final Function RIGHT = new Function(116, "right", 255);
/* 170:386 */   public static final Function EXACT = new Function(117, "exact", 2);
/* 171:388 */   public static final Function TRIM = new Function(118, "trim", 1);
/* 172:390 */   public static final Function REPLACE = new Function(119, "replace", 4);
/* 173:392 */   public static final Function SUBSTITUTE = new Function(120, "substitute", 255);
/* 174:394 */   public static final Function CODE = new Function(121, "code", 1);
/* 175:398 */   public static final Function FIND = new Function(124, "find", 255);
/* 176:400 */   public static final Function CELL = new Function(125, "cell", 2);
/* 177:402 */   public static final Function ISERR = new Function(126, "iserr", 1);
/* 178:404 */   public static final Function ISTEXT = new Function(127, "istext", 1);
/* 179:406 */   public static final Function ISNUMBER = new Function(128, "isnumber", 1);
/* 180:408 */   public static final Function ISBLANK = new Function(129, "isblank", 1);
/* 181:410 */   public static final Function T = new Function(130, "t", 1);
/* 182:412 */   public static final Function N = new Function(131, "n", 1);
/* 183:422 */   public static final Function DATEVALUE = new Function(140, "datevalue", 1);
/* 184:424 */   public static final Function TIMEVALUE = new Function(141, "timevalue", 1);
/* 185:426 */   public static final Function SLN = new Function(142, "sln", 3);
/* 186:428 */   public static final Function SYD = new Function(143, "syd", 3);
/* 187:430 */   public static final Function DDB = new Function(144, "ddb", 255);
/* 188:435 */   public static final Function INDIRECT = new Function(148, "indirect", 255);
/* 189:457 */   public static final Function CLEAN = new Function(162, "clean", 1);
/* 190:459 */   public static final Function MDETERM = new Function(163, "mdeterm", 255);
/* 191:461 */   public static final Function MINVERSE = new Function(164, "minverse", 255);
/* 192:463 */   public static final Function MMULT = new Function(165, "mmult", 255);
/* 193:467 */   public static final Function IPMT = new Function(167, "ipmt", 255);
/* 194:469 */   public static final Function PPMT = new Function(168, "ppmt", 255);
/* 195:471 */   public static final Function COUNTA = new Function(169, "counta", 255);
/* 196:473 */   public static final Function PRODUCT = new Function(183, "product", 255);
/* 197:475 */   public static final Function FACT = new Function(184, "fact", 1);
/* 198:483 */   public static final Function DPRODUCT = new Function(189, "dproduct", 3);
/* 199:485 */   public static final Function ISNONTEXT = new Function(190, "isnontext", 1);
/* 200:489 */   public static final Function STDEVP = new Function(193, "stdevp", 255);
/* 201:491 */   public static final Function VARP = new Function(194, "varp", 255);
/* 202:493 */   public static final Function DSTDEVP = new Function(195, "dstdevp", 255);
/* 203:495 */   public static final Function DVARP = new Function(196, "dvarp", 255);
/* 204:497 */   public static final Function TRUNC = new Function(197, "trunc", 255);
/* 205:499 */   public static final Function ISLOGICAL = new Function(198, "islogical", 1);
/* 206:501 */   public static final Function DCOUNTA = new Function(199, "dcounta", 255);
/* 207:503 */   public static final Function FINDB = new Function(205, "findb", 255);
/* 208:505 */   public static final Function SEARCHB = new Function(206, "searchb", 3);
/* 209:507 */   public static final Function REPLACEB = new Function(207, "replaceb", 4);
/* 210:509 */   public static final Function LEFTB = new Function(208, "leftb", 255);
/* 211:511 */   public static final Function RIGHTB = new Function(209, "rightb", 255);
/* 212:513 */   public static final Function MIDB = new Function(210, "midb", 3);
/* 213:515 */   public static final Function LENB = new Function(211, "lenb", 1);
/* 214:517 */   public static final Function ROUNDUP = new Function(212, "roundup", 2);
/* 215:519 */   public static final Function ROUNDDOWN = new Function(213, "rounddown", 2);
/* 216:521 */   public static final Function RANK = new Function(216, "rank", 255);
/* 217:523 */   public static final Function ADDRESS = new Function(219, "address", 255);
/* 218:525 */   public static final Function AYS360 = new Function(220, "days360", 255);
/* 219:527 */   public static final Function ODAY = new Function(221, "today", 0);
/* 220:529 */   public static final Function VDB = new Function(222, "vdb", 255);
/* 221:531 */   public static final Function MEDIAN = new Function(227, "median", 255);
/* 222:533 */   public static final Function SUMPRODUCT = new Function(228, "sumproduct", 255);
/* 223:535 */   public static final Function SINH = new Function(229, "sinh", 1);
/* 224:537 */   public static final Function COSH = new Function(230, "cosh", 1);
/* 225:539 */   public static final Function TANH = new Function(231, "tanh", 1);
/* 226:541 */   public static final Function ASINH = new Function(232, "asinh", 1);
/* 227:543 */   public static final Function ACOSH = new Function(233, "acosh", 1);
/* 228:545 */   public static final Function ATANH = new Function(234, "atanh", 1);
/* 229:547 */   public static final Function INFO = new Function(244, "info", 1);
/* 230:549 */   public static final Function AVEDEV = new Function(269, "avedev", 255);
/* 231:551 */   public static final Function BETADIST = new Function(270, "betadist", 255);
/* 232:553 */   public static final Function GAMMALN = new Function(271, "gammaln", 1);
/* 233:555 */   public static final Function BETAINV = new Function(272, "betainv", 255);
/* 234:557 */   public static final Function BINOMDIST = new Function(273, "binomdist", 4);
/* 235:559 */   public static final Function CHIDIST = new Function(274, "chidist", 2);
/* 236:561 */   public static final Function CHIINV = new Function(275, "chiinv", 2);
/* 237:563 */   public static final Function COMBIN = new Function(276, "combin", 2);
/* 238:565 */   public static final Function CONFIDENCE = new Function(277, "confidence", 3);
/* 239:567 */   public static final Function CRITBINOM = new Function(278, "critbinom", 3);
/* 240:569 */   public static final Function EVEN = new Function(279, "even", 1);
/* 241:571 */   public static final Function EXPONDIST = new Function(280, "expondist", 3);
/* 242:573 */   public static final Function FDIST = new Function(281, "fdist", 3);
/* 243:575 */   public static final Function FINV = new Function(282, "finv", 3);
/* 244:577 */   public static final Function FISHER = new Function(283, "fisher", 1);
/* 245:579 */   public static final Function FISHERINV = new Function(284, "fisherinv", 1);
/* 246:581 */   public static final Function FLOOR = new Function(285, "floor", 2);
/* 247:583 */   public static final Function GAMMADIST = new Function(286, "gammadist", 4);
/* 248:585 */   public static final Function GAMMAINV = new Function(287, "gammainv", 3);
/* 249:587 */   public static final Function CEILING = new Function(288, "ceiling", 2);
/* 250:589 */   public static final Function HYPGEOMDIST = new Function(289, "hypgeomdist", 4);
/* 251:591 */   public static final Function LOGNORMDIST = new Function(290, "lognormdist", 3);
/* 252:593 */   public static final Function LOGINV = new Function(291, "loginv", 3);
/* 253:595 */   public static final Function NEGBINOMDIST = new Function(292, "negbinomdist", 3);
/* 254:597 */   public static final Function NORMDIST = new Function(293, "normdist", 4);
/* 255:599 */   public static final Function NORMSDIST = new Function(294, "normsdist", 1);
/* 256:601 */   public static final Function NORMINV = new Function(295, "norminv", 3);
/* 257:603 */   public static final Function NORMSINV = new Function(296, "normsinv", 1);
/* 258:605 */   public static final Function STANDARDIZE = new Function(297, "standardize", 3);
/* 259:607 */   public static final Function ODD = new Function(298, "odd", 1);
/* 260:609 */   public static final Function PERMUT = new Function(299, "permut", 2);
/* 261:611 */   public static final Function POISSON = new Function(300, "poisson", 3);
/* 262:613 */   public static final Function TDIST = new Function(301, "tdist", 3);
/* 263:615 */   public static final Function WEIBULL = new Function(302, "weibull", 4);
/* 264:617 */   public static final Function SUMXMY2 = new Function(303, "sumxmy2", 255);
/* 265:619 */   public static final Function SUMX2MY2 = new Function(304, "sumx2my2", 255);
/* 266:621 */   public static final Function SUMX2PY2 = new Function(305, "sumx2py2", 255);
/* 267:623 */   public static final Function CHITEST = new Function(306, "chitest", 255);
/* 268:625 */   public static final Function CORREL = new Function(307, "correl", 255);
/* 269:627 */   public static final Function COVAR = new Function(308, "covar", 255);
/* 270:629 */   public static final Function FORECAST = new Function(309, "forecast", 255);
/* 271:631 */   public static final Function FTEST = new Function(310, "ftest", 255);
/* 272:633 */   public static final Function INTERCEPT = new Function(311, "intercept", 255);
/* 273:635 */   public static final Function PEARSON = new Function(312, "pearson", 255);
/* 274:637 */   public static final Function RSQ = new Function(313, "rsq", 255);
/* 275:639 */   public static final Function STEYX = new Function(314, "steyx", 255);
/* 276:641 */   public static final Function SLOPE = new Function(315, "slope", 2);
/* 277:643 */   public static final Function TTEST = new Function(316, "ttest", 255);
/* 278:645 */   public static final Function PROB = new Function(317, "prob", 255);
/* 279:647 */   public static final Function DEVSQ = new Function(318, "devsq", 255);
/* 280:649 */   public static final Function GEOMEAN = new Function(319, "geomean", 255);
/* 281:651 */   public static final Function HARMEAN = new Function(320, "harmean", 255);
/* 282:653 */   public static final Function SUMSQ = new Function(321, "sumsq", 255);
/* 283:655 */   public static final Function KURT = new Function(322, "kurt", 255);
/* 284:657 */   public static final Function SKEW = new Function(323, "skew", 255);
/* 285:659 */   public static final Function ZTEST = new Function(324, "ztest", 255);
/* 286:661 */   public static final Function LARGE = new Function(325, "large", 255);
/* 287:663 */   public static final Function SMALL = new Function(326, "small", 255);
/* 288:665 */   public static final Function QUARTILE = new Function(327, "quartile", 255);
/* 289:667 */   public static final Function PERCENTILE = new Function(328, "percentile", 255);
/* 290:669 */   public static final Function PERCENTRANK = new Function(329, "percentrank", 255);
/* 291:671 */   public static final Function MODE = new Function(330, "mode", 255);
/* 292:673 */   public static final Function TRIMMEAN = new Function(331, "trimmean", 255);
/* 293:675 */   public static final Function TINV = new Function(332, "tinv", 2);
/* 294:677 */   public static final Function CONCATENATE = new Function(336, "concatenate", 255);
/* 295:679 */   public static final Function POWER = new Function(337, "power", 2);
/* 296:681 */   public static final Function RADIANS = new Function(342, "radians", 1);
/* 297:683 */   public static final Function DEGREES = new Function(343, "degrees", 1);
/* 298:685 */   public static final Function SUBTOTAL = new Function(344, "subtotal", 255);
/* 299:687 */   public static final Function SUMIF = new Function(345, "sumif", 255);
/* 300:689 */   public static final Function COUNTIF = new Function(346, "countif", 2);
/* 301:691 */   public static final Function COUNTBLANK = new Function(347, "countblank", 1);
/* 302:693 */   public static final Function HYPERLINK = new Function(359, "hyperlink", 2);
/* 303:695 */   public static final Function AVERAGEA = new Function(361, "averagea", 255);
/* 304:697 */   public static final Function MAXA = new Function(362, "maxa", 255);
/* 305:699 */   public static final Function MINA = new Function(363, "mina", 255);
/* 306:701 */   public static final Function STDEVPA = new Function(364, "stdevpa", 255);
/* 307:703 */   public static final Function VARPA = new Function(365, "varpa", 255);
/* 308:705 */   public static final Function STDEVA = new Function(366, "stdeva", 255);
/* 309:707 */   public static final Function VARA = new Function(367, "vara", 255);
/* 310:712 */   public static final Function IF = new Function(65534, "if", 255);
/* 311:715 */   public static final Function UNKNOWN = new Function(65535, "", 0);
/* 312:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.Function
 * JD-Core Version:    0.7.0.1
 */