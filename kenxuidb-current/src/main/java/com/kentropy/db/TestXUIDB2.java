/*    1:     */ package com.kentropy.db;
/*    2:     */ 
/*    3:     */ import com.kentropy.data.DataHandler;
/*    4:     */ import com.kentropy.data.TaskHandler;
/*    5:     */ import com.kentropy.model.KenList;
/*    6:     */ import com.kentropy.process.Process;
/*    7:     */ import com.kentropy.security.client.UserAuthentication;
/*    8:     */ import com.kentropy.sync.ChangeLog;
/*    9:     */ import com.kentropy.transfer.Client;
/*   10:     */ import java.io.DataInputStream;
/*   11:     */ import java.io.File;
/*   12:     */ import java.io.FileInputStream;
/*   13:     */ import java.io.FileWriter;
/*   14:     */ import java.io.InputStream;
/*   15:     */ import java.io.InputStreamReader;
/*   16:     */ import java.io.OutputStream;
/*   17:     */ import java.io.PrintStream;
/*   18:     */ import java.io.StringReader;
/*   19:     */ import java.io.StringWriter;
/*   20:     */ import java.io.UnsupportedEncodingException;
/*   21:     */ import java.sql.PreparedStatement;
/*   22:     */ import java.sql.ResultSet;
/*   23:     */ import java.sql.ResultSetMetaData;
/*   24:     */ import java.text.DateFormat;
/*   25:     */ import java.text.SimpleDateFormat;
/*   26:     */ import java.util.Calendar;
/*   27:     */ import java.util.Date;
/*   28:     */ import java.util.Enumeration;
/*   29:     */ import java.util.Hashtable;
/*   30:     */ import java.util.Locale;
/*   31:     */ import java.util.Properties;
/*   32:     */ import java.util.StringTokenizer;
/*   33:     */ import java.util.Vector;
/*   34:     */ import net.xoetrope.data.XDataSource;
/*   35:     */ import net.xoetrope.optional.data.sql.CachedDatabaseTable;
/*   36:     */ import net.xoetrope.optional.data.sql.ConnectionObject;
/*   37:     */ import net.xoetrope.optional.data.sql.DatabaseTableModel;
/*   38:     */ import net.xoetrope.optional.data.sql.NamedConnectionManager;
/*   39:     */ import net.xoetrope.xml.XmlElement;
/*   40:     */ import net.xoetrope.xml.XmlSource;
/*   41:     */ import net.xoetrope.xui.XProject;
/*   42:     */ import net.xoetrope.xui.XProjectManager;
/*   43:     */ import net.xoetrope.xui.data.XBaseModel;
/*   44:     */ import net.xoetrope.xui.data.XModel;
/*   45:     */ import org.apache.log4j.Logger;
/*   46:     */ import org.springframework.context.ApplicationContext;
/*   47:     */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*   48:     */ 
/*   49:     */ public class TestXUIDB2
/*   50:     */   implements DataHandler, TaskHandler
/*   51:     */ {
/*   52:  53 */   static String workload = null;
/*   53:  55 */   public static String driver = "com.mysql.jdbc.Driver";
/*   54:  57 */   public static String user = "root";
/*   55:  58 */   public static String passwd = "";
/*   56:  59 */   public static String confStatus = "error";
/*   57:  60 */   public static String imagePath = "";
/*   58:  61 */   public static String dwdb = "";
/*   59:  62 */   public static boolean logImport = false;
/*   60:     */   
/*   61:     */   static
/*   62:     */   {
/*   63:  65 */     InputStream is = null;
/*   64:     */     try
/*   65:     */     {
/*   66:  68 */       String url = "jdbc:mysql://localhost:3306/vatest";
/*   67:     */       try
/*   68:     */       {
/*   69:  71 */         ApplicationContext appContext = new ClassPathXmlApplicationContext("appContext.xml");
/*   70:     */         
/*   71:  73 */         Properties p = (Properties)appContext.getBean("db");
/*   72:     */         
/*   73:  75 */         driver = p.getProperty("driver", driver);
/*   74:  76 */         url = p.getProperty("dburl");
/*   75:  77 */         user = p.getProperty("user");
/*   76:  78 */         passwd = p.getProperty("passwd");
/*   77:  79 */         imagePath = p.getProperty("imagePath");
/*   78:  80 */         dwdb = p.getProperty("dwdb");
/*   79:  81 */         confStatus = "ok";
/*   80:     */         
/*   81:  83 */         TestXUIDB.logImport = p.getProperty("logImport") == null ? false : Boolean.parseBoolean(p.getProperty("logImport"));
/*   82:  84 */         TestXUIDB.workload = p.getProperty("workload");
/*   83:     */       }
/*   84:     */       catch (Exception e)
/*   85:     */       {
/*   86:  88 */         e.printStackTrace();
/*   87:     */       }
/*   88:  91 */       NamedConnectionManager nc = (NamedConnectionManager)NamedConnectionManager.getInstance();
/*   89:     */       
/*   90:  93 */       System.out.println(" >> Driver" + driver);
/*   91:  94 */       Class.forName(driver);
/*   92:  95 */       nc.addConnection("test", driver, url, user, passwd);
/*   93:     */       
/*   94:  97 */       ConnectionObject co = nc.getConnection("test");
/*   95:     */       
/*   96:  99 */       System.out.println("Workload::" + workload);
/*   97:     */     }
/*   98:     */     catch (Exception e)
/*   99:     */     {
/*  100: 103 */       e.printStackTrace();
/*  101:     */     }
/*  102:     */   }
/*  103:     */   
/*  104:     */   public String getProperty(String property)
/*  105:     */   {
/*  106: 109 */     String value = null;
/*  107: 110 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  108: 111 */     String sql = "select value from configuration where property='" + property + "'";
/*  109: 112 */     System.out.println("sql::" + sql);
/*  110:     */     
/*  111: 114 */     dt.setSqlStatement(sql, "test", false);
/*  112: 115 */     dt.retrieve();
/*  113: 116 */     int n = dt.getNumChildren();
/*  114: 117 */     System.out.println("No of Rows:" + n);
/*  115: 118 */     XModel xm = new XBaseModel();
/*  116: 119 */     if (n > 0)
/*  117:     */     {
/*  118: 120 */       XModel row = (XModel)xm.get("0");
/*  119: 121 */       for (int i = 0; i < dt.getNumAttributes(); i++) {
/*  120: 122 */         value = dt.get(0).getAttribValueAsString(0);
/*  121:     */       }
/*  122:     */     }
/*  123: 126 */     return value;
/*  124:     */   }
/*  125:     */   
/*  126:     */   public void updateCODReport(String phys1, String phys2, String adjudicator, String report)
/*  127:     */     throws Exception
/*  128:     */   {
/*  129: 132 */     String finalIcd = "";
/*  130: 133 */     XModel xm = new XBaseModel();
/*  131: 134 */     String path = "/cme/" + report;
/*  132:     */     
/*  133: 136 */     String codPath1 = path + "/Coding/" + phys1 + "/icd";
/*  134: 137 */     String reconPath1 = path + "/Reconciliation/" + phys1 + "/icd";
/*  135: 138 */     String codPath2 = path + "/Coding/" + phys2 + "/icd";
/*  136: 139 */     String reconPath2 = path + "/Reconciliation/" + phys2 + "/icd";
/*  137: 140 */     String adjPath = path + "/Adjudication/" + adjudicator + "/icd";
/*  138:     */     
/*  139: 142 */     String keywordPath1 = path + "/Coding/Comments/" + phys1;
/*  140: 143 */     String keywordPath2 = path + "/Coding/Comments/" + phys2;
/*  141: 144 */     String keywordPath3 = path + "/Coding/Comments/" + adjudicator;
/*  142:     */     
/*  143: 146 */     String cod1 = TestXUIDB.getInstance().getValue("keyvalue", codPath1);
/*  144: 147 */     String recon1 = TestXUIDB.getInstance().getValue("keyvalue", reconPath1);
/*  145: 148 */     String cod2 = TestXUIDB.getInstance().getValue("keyvalue", codPath2);
/*  146: 149 */     String recon2 = TestXUIDB.getInstance().getValue("keyvalue", reconPath2);
/*  147: 150 */     String adj = TestXUIDB.getInstance().getValue("keyvalue", adjPath);
/*  148: 151 */     StringBuffer codingKeyword1 = new StringBuffer();
/*  149: 152 */     StringBuffer codingKeyword2 = new StringBuffer();
/*  150: 153 */     StringBuffer reconKeyword1 = new StringBuffer();
/*  151: 154 */     StringBuffer reconKeyword2 = new StringBuffer();
/*  152: 155 */     StringBuffer adjudicationKeyword = new StringBuffer();
/*  153:     */     
/*  154: 157 */     XModel dataM = new XBaseModel();
/*  155: 158 */     dataM.setId(report);
/*  156: 159 */     ((XModel)dataM.get("physician1")).set(phys1);
/*  157: 160 */     ((XModel)dataM.get("physician2")).set(phys2);
/*  158: 161 */     if ((adjudicator != null) && (!adjudicator.trim().equals(""))) {
/*  159: 162 */       ((XModel)dataM.get("adjudicator")).set(adjudicator);
/*  160:     */     }
/*  161: 164 */     ((XModel)dataM.get("coding_icd1")).set(cod1);
/*  162: 165 */     ((XModel)dataM.get("coding_icd2")).set(cod2);
/*  163: 166 */     finalIcd = cod2;
/*  164: 167 */     if ((recon1 != null) && (!recon1.trim().equals(""))) {
/*  165: 168 */       ((XModel)dataM.get("reconciliation_icd1")).set(recon1);
/*  166:     */     }
/*  167: 170 */     if ((recon2 != null) && (!recon2.trim().equals("")))
/*  168:     */     {
/*  169: 171 */       ((XModel)dataM.get("reconciliation_icd2")).set(recon2);
/*  170: 172 */       finalIcd = recon2;
/*  171:     */     }
/*  172: 174 */     if ((adj != null) && (!adj.trim().equals("")))
/*  173:     */     {
/*  174: 175 */       ((XModel)dataM.get("adjudication_icd")).set(adj);
/*  175: 176 */       finalIcd = adj;
/*  176:     */     }
/*  177: 178 */     ((XModel)dataM.get("uniqno")).set(report);
/*  178:     */     
/*  179: 180 */     ((XModel)dataM.get("coding_keyword1")).set(codingKeyword1);
/*  180: 181 */     ((XModel)dataM.get("coding_keyword2")).set(codingKeyword2);
/*  181: 182 */     ((XModel)dataM.get("reconciliation_keyword1")).set(reconKeyword1);
/*  182: 183 */     ((XModel)dataM.get("reconciliation_keyword2")).set(reconKeyword2);
/*  183: 184 */     ((XModel)dataM.get("adjudication_keyword")).set(adjudicationKeyword);
/*  184:     */     
/*  185: 186 */     TestXUIDB.getInstance().saveData("cme_report", "uniqno='" + report + "' and physician1='" + phys1 + "' and physician2='" + phys2 + "'", dataM);
/*  186:     */   }
/*  187:     */   
/*  188:     */   public String getICDDesc(String icd)
/*  189:     */   {
/*  190: 191 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  191: 192 */     dt.setupTable("icd", "description", "icd='" + icd + "'", "test", false);
/*  192: 193 */     dt.retrieve();
/*  193:     */     
/*  194: 195 */     System.out.println(dt.getNumChildren());
/*  195: 196 */     int i = 0;
/*  196: 196 */     if (i < dt.getNumChildren())
/*  197:     */     {
/*  198: 198 */       String desc = dt.get(i).get("description").toString();
/*  199: 199 */       return desc;
/*  200:     */     }
/*  201: 202 */     return null;
/*  202:     */   }
/*  203:     */   
/*  204:     */   public boolean isValidIcdAge(String age, String icd)
/*  205:     */   {
/*  206: 207 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  207: 208 */     dt.setupTable("icd_exclusions_age", "*", "icd='" + icd + "'", "test", false);
/*  208: 209 */     dt.retrieve();
/*  209:     */     
/*  210: 211 */     System.out.println(dt.getNumChildren());
/*  211: 212 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  212:     */     {
/*  213: 214 */       String ageRange1 = dt.get(i).get("age").toString();
/*  214: 215 */       String[] ageRange = ageRange1.split("-");
/*  215: 216 */       double min = Double.parseDouble(ageRange[0]);
/*  216: 217 */       double max = Double.parseDouble(ageRange[1]);
/*  217: 218 */       double ageInYears = Double.parseDouble(age);
/*  218: 219 */       if ((ageInYears >= min) && (ageInYears <= max)) {
/*  219: 220 */         return false;
/*  220:     */       }
/*  221:     */     }
/*  222: 225 */     return true;
/*  223:     */   }
/*  224:     */   
/*  225:     */   public boolean isValidIcdSex(String age, String icd)
/*  226:     */   {
/*  227: 230 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  228: 231 */     dt.setupTable("icd_exclusions_sex", "*", "icd='" + icd + "'", "test", false);
/*  229: 232 */     dt.retrieve();
/*  230:     */     
/*  231: 234 */     System.out.println(dt.getNumChildren());
/*  232: 236 */     if (dt.getNumChildren() > 0) {
/*  233: 237 */       return false;
/*  234:     */     }
/*  235: 240 */     return true;
/*  236:     */   }
/*  237:     */   
/*  238:     */   public boolean checkEquivalence(String icd1, String icd2)
/*  239:     */   {
/*  240: 245 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  241: 246 */     dt.setupTable("icd_equivalent_codes", "*", "icdEquivalence LIKE '%" + icd1 + "%' AND icdEquivalence LIKE '%" + icd2 + "%'", "test", false);
/*  242: 247 */     dt.retrieve();
/*  243: 249 */     if (dt.getNumChildren() > 0) {
/*  244: 250 */       return true;
/*  245:     */     }
/*  246: 253 */     return false;
/*  247:     */   }
/*  248:     */   
/*  249:     */   public static XUIDB getInstance()
/*  250:     */   {
/*  251: 258 */     XUIDB tt = new TestXUIDB();
/*  252: 259 */     tt.init();
/*  253:     */     
/*  254: 261 */     return tt;
/*  255:     */   }
/*  256:     */   
/*  257:     */   public String getImagePath()
/*  258:     */   {
/*  259: 266 */     return imagePath;
/*  260:     */   }
/*  261:     */   
/*  262:     */   public void getReportData(String path, XModel dataM, int noOfColumns)
/*  263:     */   {
/*  264: 280 */     throw new Error("Unresolved compilation problem: \n\tThe method setId(String) in the type XBaseModel is not applicable for the arguments (int)\n");
/*  265:     */   }
/*  266:     */   
/*  267:     */   public void getDiffDiagnosis(String icdCode, XModel dataM)
/*  268:     */   {
/*  269: 315 */     throw new Error("Unresolved compilation problem: \n\tThe method setId(String) in the type XBaseModel is not applicable for the arguments (int)\n");
/*  270:     */   }
/*  271:     */   
/*  272:     */   public void getSearch(String searchKey, String icdKey, XModel dataM)
/*  273:     */   {
/*  274: 339 */     throw new Error("Unresolved compilation problem: \n\tThe method setId(String) in the type XBaseModel is not applicable for the arguments (int)\n");
/*  275:     */   }
/*  276:     */   
/*  277:     */   public void getSearchIcd(String searchKey, XModel dataM)
/*  278:     */   {
/*  279: 364 */     throw new Error("Unresolved compilation problem: \n\tThe method setId(String) in the type XBaseModel is not applicable for the arguments (int)\n");
/*  280:     */   }
/*  281:     */   
/*  282:     */   public void getSearch(String searchKey, XModel dataM)
/*  283:     */   {
/*  284: 389 */     throw new Error("Unresolved compilation problem: \n\tThe method setId(String) in the type XBaseModel is not applicable for the arguments (int)\n");
/*  285:     */   }
/*  286:     */   
/*  287:     */   public Vector findPhysicians(String vaId)
/*  288:     */   {
/*  289: 406 */     String language = getValue("keyvalue", "/va/" + vaId + "/gi/Language");
/*  290: 407 */     System.out.println("language " + language);
/*  291:     */     
/*  292: 409 */     String sql = "SELECT DISTINCT a.physician physician from  physician_language a  LEFT JOIN physician_workload b ON a.physician= b.physician WHERE b.workload < 11 ";
/*  293: 410 */     DatabaseTableModel dtm = new DatabaseTableModel();
/*  294: 411 */     dtm.setSqlStatement(sql, "test", false);
/*  295: 412 */     dtm.retrieve();
/*  296: 413 */     Vector ind = new Vector();
/*  297: 414 */     for (int i = 0; i < dtm.getNumChildren(); i++)
/*  298:     */     {
/*  299: 416 */       String phy = dtm.get(i).get("physician").toString();
/*  300: 417 */       System.out.println(phy);
/*  301: 418 */       ind.add(dtm.get(i).get("physician").toString());
/*  302:     */     }
/*  303: 421 */     return ind;
/*  304:     */   }
/*  305:     */   
/*  306:     */   public void saveProcess(Process p)
/*  307:     */   {
/*  308:     */     try
/*  309:     */     {
/*  310: 428 */       String where = " pid ='" + p.pid + "'";
/*  311: 429 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  312: 430 */       dt.setupTable("process", "*", where, "test", false);
/*  313:     */       
/*  314: 432 */       dt.retrieve();
/*  315: 433 */       String s = new String("");
/*  316: 434 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  317: 435 */       String st = "'" + sdf.format(p.startTime) + "'";
/*  318: 436 */       String et = "'" + sdf.format(p.endTime) + "'";
/*  319: 437 */       System.out.println(dt.getNumChildren());
/*  320: 439 */       if (dt.getNumChildren() > 0) {
/*  321: 441 */         s = "update process set status='" + p.status + "',startTime=" + st + ",endTime=" + et + ", stateMachine='" + p.states + "' where" + where;
/*  322:     */       } else {
/*  323: 444 */         s = "insert into process (pid,status,startTime,endTime,stateMachine) Values('" + p.pid + "','" + p.status + "'," + st + "," + st + ",'" + p.states + "')";
/*  324:     */       }
/*  325: 445 */       System.out.println(">>>>" + s);
/*  326: 446 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/*  327: 447 */       ps.execute();
/*  328:     */     }
/*  329:     */     catch (Exception e)
/*  330:     */     {
/*  331: 451 */       e.printStackTrace();
/*  332:     */     }
/*  333:     */   }
/*  334:     */   
/*  335:     */   public void saveProcess(Process p, String stateMachineClassName)
/*  336:     */   {
/*  337:     */     try
/*  338:     */     {
/*  339: 459 */       String where = " pid ='" + p.pid + "'";
/*  340: 460 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  341: 461 */       dt.setupTable("process", "*", where, "test", false);
/*  342:     */       
/*  343: 463 */       dt.retrieve();
/*  344: 464 */       String s = new String("");
/*  345: 465 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  346: 466 */       String st = "'" + sdf.format(p.startTime) + "'";
/*  347: 467 */       String et = "'" + sdf.format(p.endTime) + "'";
/*  348: 468 */       System.out.println(dt.getNumChildren());
/*  349: 470 */       if (dt.getNumChildren() > 0) {
/*  350: 472 */         s = "update process set status='" + p.status + "',startTime=" + st + ",endTime=" + et + ", stateMachine='" + p.states + "',stateMachineClass='" + stateMachineClassName + "' where" + where;
/*  351:     */       } else {
/*  352: 475 */         s = "insert into process (pid,status,startTime,endTime,stateMachine,stateMachineClass) Values('" + p.pid + "','" + p.status + "'," + st + "," + st + ",'" + p.states + "','" + p.stateMachineClass + "')";
/*  353:     */       }
/*  354: 476 */       System.out.println(">>>>" + s);
/*  355: 477 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/*  356: 478 */       ps.execute();
/*  357:     */     }
/*  358:     */     catch (Exception e)
/*  359:     */     {
/*  360: 482 */       e.printStackTrace();
/*  361:     */     }
/*  362:     */   }
/*  363:     */   
/*  364:     */   public void execute(String table, String where, String sql)
/*  365:     */     throws Exception
/*  366:     */   {
/*  367: 489 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  368:     */     
/*  369: 491 */     dt.setupTable(table);
/*  370:     */     
/*  371: 493 */     PreparedStatement ps = dt.getTable().getPreparedStatement(sql);
/*  372:     */     
/*  373: 495 */     ps.execute();
/*  374: 496 */     ps.close();
/*  375:     */   }
/*  376:     */   
/*  377:     */   public void saveTransition(String id, String pid, int status)
/*  378:     */   {
/*  379:     */     try
/*  380:     */     {
/*  381: 503 */       String where = " id ='" + id + "'";
/*  382: 504 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  383: 505 */       dt.setupTable("transitions", "*", where, "test", false);
/*  384:     */       
/*  385: 507 */       dt.retrieve();
/*  386: 508 */       String s = new String("");
/*  387: 509 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  388:     */       
/*  389: 511 */       System.out.println(dt.getNumChildren());
/*  390: 513 */       if (dt.getNumChildren() > 0) {
/*  391: 515 */         s = "update transitions set pid='" + pid + "',status=" + status + " where" + where;
/*  392:     */       } else {
/*  393: 518 */         s = "insert into transitions (pid,status) Values('" + pid + "','" + status + "')";
/*  394:     */       }
/*  395: 519 */       System.out.println(">>>>" + s);
/*  396: 520 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/*  397: 521 */       ps.execute();
/*  398:     */     }
/*  399:     */     catch (Exception e)
/*  400:     */     {
/*  401: 525 */       e.printStackTrace();
/*  402:     */     }
/*  403:     */   }
/*  404:     */   
/*  405:     */   public String[] getNextTransition()
/*  406:     */     throws Exception
/*  407:     */   {
/*  408: 532 */     String where = " status =0";
/*  409: 533 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  410:     */     
/*  411: 535 */     dt.setupTable("transitions", "*", where, "test", false);
/*  412: 536 */     dt.retrieve();
/*  413: 537 */     Process p = null;
/*  414: 538 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/*  415: 539 */     System.out.println(" NO of transitions" + dt.getNumChildren());
/*  416: 540 */     if (dt.getNumChildren() > 0)
/*  417:     */     {
/*  418: 542 */       String[] ret = new String[2];
/*  419: 543 */       ret[0] = dt.get(0).get("id").toString();
/*  420: 544 */       ret[1] = dt.get(0).get("pid").toString();
/*  421: 545 */       return ret;
/*  422:     */     }
/*  423: 548 */     return null;
/*  424:     */   }
/*  425:     */   
/*  426:     */   public void logAgent(String pid, String agent, String currentState, String message)
/*  427:     */   {
/*  428:     */     try
/*  429:     */     {
/*  430: 555 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  431: 556 */       dt.setupTable("transitions", "*", "", "test", false);
/*  432:     */       
/*  433: 558 */       dt.retrieve();
/*  434: 559 */       String s = new String("");
/*  435: 560 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  436:     */       
/*  437: 562 */       System.out.println(dt.getNumChildren());
/*  438: 563 */       s = "insert into agentlog (pid,agent,state,message) Values('" + pid + "','" + agent + "','" + currentState + "','" + message + "')";
/*  439: 564 */       System.out.println(">>>>" + s);
/*  440: 565 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/*  441: 566 */       ps.execute();
/*  442:     */     }
/*  443:     */     catch (Exception e)
/*  444:     */     {
/*  445: 570 */       e.printStackTrace();
/*  446:     */     }
/*  447:     */   }
/*  448:     */   
/*  449:     */   public Process getProcess(String pid)
/*  450:     */     throws Exception
/*  451:     */   {
/*  452: 604 */     throw new Error("Unresolved compilation problems: \n\tThe method parse(String) in the type DateFormat is not applicable for the arguments (Object)\n\tThe method parse(String) in the type DateFormat is not applicable for the arguments (Object)\n\tThe method deserialize(String) in the type StateMachine is not applicable for the arguments (Object)\n");
/*  453:     */   }
/*  454:     */   
/*  455:     */   public XModel getTasks(String surveyType, String team, String taskPath, KenList dataPath)
/*  456:     */   {
/*  457: 624 */     KenList kl = new KenList("area/house/household/member");
/*  458:     */     
/*  459: 626 */     KenList kl1 = null;
/*  460: 627 */     if (kl.size() > dataPath.size()) {
/*  461: 629 */       kl1 = kl.subset(0, dataPath.size() - 1);
/*  462:     */     } else {
/*  463: 633 */       kl1 = kl;
/*  464:     */     }
/*  465: 636 */     String context = kl1.add1("=").add1(dataPath).toString(" and ");
/*  466: 637 */     String where = "task like '%definition/" + taskPath + "' and assignedto='" + team + "' " + (context.length() > 0 ? "and " + context : "") + " and survey_type='" + surveyType + "' and (duedate is null or duedate >= Now())";
/*  467: 638 */     System.out.println(" Where " + where);
/*  468: 639 */     XModel dataM = new XBaseModel();
/*  469: 640 */     getTaskData(where, dataM);
/*  470: 641 */     return dataM;
/*  471:     */   }
/*  472:     */   
/*  473:     */   public XModel getTasks1(String surveyType, String team, String taskPath, KenList dataPath)
/*  474:     */   {
/*  475: 646 */     KenList kl = new KenList("area/house/household/member");
/*  476:     */     
/*  477: 648 */     KenList kl1 = null;
/*  478: 649 */     if (kl.size() > dataPath.size()) {
/*  479: 651 */       kl1 = kl.subset(0, dataPath.size() - 1);
/*  480:     */     } else {
/*  481: 655 */       kl1 = kl;
/*  482:     */     }
/*  483: 658 */     String context = kl1.add1("=").add1(dataPath).toString(" and ", "'");
/*  484: 659 */     String defPath = "taskdefinitions/" + ((taskPath != null) && (!taskPath.equals("")) ? surveyType + "_taskdefinition/" + taskPath : new StringBuilder().append(surveyType).append("_taskdefinition/%").toString());
/*  485:     */     
/*  486: 661 */     String where = "task like '" + defPath + "/%' and task not like '" + defPath + "/%/%' and assignedto='" + team + "' " + (context.length() > 0 ? "and " + context : "") + " and (status  ='0' or status is null)";
/*  487: 662 */     System.out.println(" Where " + where);
/*  488: 663 */     Logger logger = Logger.getLogger(getClass());
/*  489: 664 */     logger.info(" get Tasks1 " + where);
/*  490: 665 */     XModel dataM = new XBaseModel();
/*  491: 666 */     getTaskData(where, dataM);
/*  492: 667 */     return dataM;
/*  493:     */   }
/*  494:     */   
/*  495:     */   public void getTasks(XModel ioM, String team, String participant)
/*  496:     */     throws Exception
/*  497:     */   {
/*  498: 673 */     String parent = "taskdefinitions/healthcheckup_taskdefinition";
/*  499:     */     
/*  500: 675 */     testP(parent, team, ioM);
/*  501:     */   }
/*  502:     */   
/*  503:     */   public void checkDBServer()
/*  504:     */     throws Exception
/*  505:     */   {
/*  506: 681 */     NamedConnectionManager nc = (NamedConnectionManager)NamedConnectionManager.getInstance();
/*  507: 682 */     ConnectionObject co = nc.getConnection("test");
/*  508:     */   }
/*  509:     */   
/*  510:     */   public String getTaskStatusPath(String task, String surveyType, String area, String house, String household, String individual)
/*  511:     */   {
/*  512: 687 */     StringTokenizer tkzr = new StringTokenizer(task, "/");
/*  513: 688 */     int tokenIndex = 0;
/*  514: 689 */     String outputPath = "";
/*  515: 690 */     String tkn = "";
/*  516: 691 */     while (tkzr.hasMoreTokens())
/*  517:     */     {
/*  518: 693 */       tkn = tkzr.nextToken();
/*  519: 694 */       switch (tokenIndex)
/*  520:     */       {
/*  521:     */       case 0: 
/*  522: 697 */         outputPath = outputPath + "survey";
/*  523: 698 */         break;
/*  524:     */       case 1: 
/*  525: 700 */         outputPath = outputPath + surveyType + "/tasks";
/*  526: 701 */         break;
/*  527:     */       case 3: 
/*  528: 703 */         outputPath = outputPath + tkn + "-" + area;
/*  529: 704 */         break;
/*  530:     */       case 4: 
/*  531: 706 */         outputPath = outputPath + tkn + "-" + house;
/*  532: 707 */         break;
/*  533:     */       case 5: 
/*  534: 709 */         outputPath = outputPath + tkn + "-" + household;
/*  535: 710 */         break;
/*  536:     */       case 6: 
/*  537: 712 */         if ((individual == null) || (individual.equals(""))) {
/*  538: 713 */           outputPath = outputPath + tkn;
/*  539:     */         } else {
/*  540: 715 */           outputPath = outputPath + tkn + "-" + individual;
/*  541:     */         }
/*  542: 716 */         break;
/*  543:     */       case 2: 
/*  544:     */       default: 
/*  545: 719 */         outputPath = outputPath + tkn;
/*  546:     */       }
/*  547: 721 */       outputPath = outputPath + "/";
/*  548: 722 */       tokenIndex++;
/*  549:     */     }
/*  550: 724 */     outputPath = outputPath.substring(0, outputPath.length() - 1);
/*  551: 725 */     System.out.println("Output path for task '=" + task + "'=" + outputPath);
/*  552: 726 */     return outputPath;
/*  553:     */   }
/*  554:     */   
/*  555:     */   public int getTaskChildren(String parentPath, String team)
/*  556:     */   {
/*  557: 731 */     String taskStr = parentPath;
/*  558:     */     
/*  559: 733 */     System.out.println(taskStr);
/*  560:     */     try
/*  561:     */     {
/*  562: 736 */       String where = "assignedto=" + team + " and  survey_type='2'  and task LIKE '" + taskStr + "/%' and  task NOT LIKE '" + taskStr + "/%/%' ";
/*  563: 737 */       System.out.println(where);
/*  564: 738 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  565: 739 */       dt.setupTable("tasks", "count(*) count1", where, "test", false);
/*  566:     */       
/*  567: 741 */       dt.retrieve();
/*  568: 742 */       XModel row = dt.get(0);
/*  569: 743 */       return Integer.parseInt(row.get("count1").toString());
/*  570:     */     }
/*  571:     */     catch (Exception e)
/*  572:     */     {
/*  573: 747 */       e.printStackTrace();
/*  574:     */     }
/*  575: 750 */     return 0;
/*  576:     */   }
/*  577:     */   
/*  578:     */   public XLogisticsModel getLogisticsM(String table, String name, String path)
/*  579:     */   {
/*  580: 755 */     String context = "and path='" + path + "'";
/*  581:     */     try
/*  582:     */     {
/*  583: 758 */       String where = "name='" + name + "' " + context;
/*  584: 759 */       System.out.println(where);
/*  585: 760 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  586: 761 */       dt.setupTable(table, "*", where, "test", false);
/*  587:     */       
/*  588: 763 */       dt.retrieve();
/*  589:     */       
/*  590: 765 */       XLogisticsModel dataM = new XLogisticsModel();
/*  591: 766 */       if (dt.getNumChildren() == 0)
/*  592:     */       {
/*  593: 768 */         dataM.setId(name);
/*  594: 769 */         dataM.set("@path", path);
/*  595:     */         
/*  596: 771 */         return dataM;
/*  597:     */       }
/*  598: 774 */       XModel row = dt.get(0);
/*  599: 776 */       for (int j = 0; j < dt.getNumAttributes(); j++) {
/*  600: 778 */         if (!dt.getAttribName(j).equals("id")) {
/*  601: 780 */           dataM.set("@" + dt.getAttribName(j), row.get(j).get());
/*  602:     */         }
/*  603:     */       }
/*  604: 783 */       System.out.println(" >>> " + dataM.get("@name"));
/*  605:     */       
/*  606: 785 */       dataM.setId(name);
/*  607: 786 */       return dataM;
/*  608:     */     }
/*  609:     */     catch (Exception e)
/*  610:     */     {
/*  611: 790 */       e.printStackTrace();
/*  612:     */     }
/*  613: 792 */     return null;
/*  614:     */   }
/*  615:     */   
/*  616:     */   public XDataModel getDataM1(String table, String where)
/*  617:     */   {
/*  618:     */     try
/*  619:     */     {
/*  620: 799 */       System.out.println(where);
/*  621: 800 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  622: 801 */       dt.setupTable(table, "*", where, "test", false);
/*  623:     */       
/*  624: 803 */       dt.retrieve();
/*  625:     */       
/*  626: 805 */       XDataModel dataM = new XDataModel();
/*  627: 807 */       if (dt.getNumChildren() > 0)
/*  628:     */       {
/*  629: 809 */         XModel row = dt.get(0);
/*  630: 811 */         for (int j = 0; j < dt.getNumAttributes(); j++) {
/*  631: 813 */           dataM.set("@" + dt.getAttribName(j), row.get(j).get());
/*  632:     */         }
/*  633: 815 */         System.out.println(" >>> " + dataM.get("@name"));
/*  634:     */       }
/*  635: 818 */       return dataM;
/*  636:     */     }
/*  637:     */     catch (Exception e)
/*  638:     */     {
/*  639: 822 */       e.printStackTrace();
/*  640:     */     }
/*  641: 824 */     return null;
/*  642:     */   }
/*  643:     */   
/*  644:     */   public XDataModel getDataM2(String table, String where)
/*  645:     */   {
/*  646: 843 */     throw new Error("Unresolved compilation problem: \n\tThe method setId(String) in the type XModel is not applicable for the arguments (int)\n");
/*  647:     */   }
/*  648:     */   
/*  649:     */   public XDataModel getDataM(String table, String name, String area, String house, String household, String individual)
/*  650:     */   {
/*  651: 864 */     String context = ((area != null) && (!area.equals("")) && (!area.equals("-1")) ? "and area=" + area : "") + ((house != null) && (!house.equals("")) ? " and house=" + house : "") + ((household != null) && (!household.equals("")) ? " and household=" + household : "") + ((individual != null) && (!individual.equals("")) ? " and member=" + individual : "");
/*  652:     */     try
/*  653:     */     {
/*  654: 867 */       String where = "name='" + name + "' " + context;
/*  655: 868 */       System.out.println(where);
/*  656: 869 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  657: 870 */       dt.setupTable(table, "*", where, "test", false);
/*  658:     */       
/*  659: 872 */       dt.retrieve();
/*  660:     */       
/*  661: 874 */       XDataModel dataM = new XDataModel();
/*  662: 875 */       if (dt.getNumChildren() == 0)
/*  663:     */       {
/*  664: 877 */         dataM.setId(name);
/*  665: 878 */         dataM.set("@name", name);
/*  666: 879 */         dataM.set("@area", area);
/*  667: 880 */         dataM.set("@house", house);
/*  668: 881 */         dataM.set("@household", household);
/*  669: 882 */         dataM.set("@member", individual);
/*  670: 883 */         return dataM;
/*  671:     */       }
/*  672: 886 */       XModel row = dt.get(0);
/*  673: 888 */       for (int j = 0; j < dt.getNumAttributes(); j++) {
/*  674: 890 */         if (!dt.getAttribName(j).equals("id")) {
/*  675: 892 */           dataM.set("@" + dt.getAttribName(j), row.get(j).get());
/*  676:     */         }
/*  677:     */       }
/*  678: 895 */       System.out.println(" >>> " + dataM.get("@name"));
/*  679:     */       
/*  680: 897 */       dataM.setId(name);
/*  681: 898 */       return dataM;
/*  682:     */     }
/*  683:     */     catch (Exception e)
/*  684:     */     {
/*  685: 902 */       e.printStackTrace();
/*  686:     */     }
/*  687: 904 */     return null;
/*  688:     */   }
/*  689:     */   
/*  690:     */   public void createMessage()
/*  691:     */     throws Exception
/*  692:     */   {
/*  693: 910 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  694:     */     
/*  695: 912 */     dt.setupTable("changelogs", "value", "", "test", true);
/*  696: 913 */     dt.retrieve();
/*  697:     */     
/*  698: 915 */     FileWriter fw = new FileWriter("c:\\message.xml");
/*  699: 916 */     fw.write("<message>\n");
/*  700: 917 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  701:     */     {
/*  702: 919 */       XModel row = dt.get(i);
/*  703:     */       
/*  704: 921 */       String log = (String)row.get(0).get();
/*  705: 922 */       fw.write(log);
/*  706:     */     }
/*  707: 925 */     fw.write("</message>\n");
/*  708: 926 */     fw.close();
/*  709:     */   }
/*  710:     */   
/*  711:     */   public String getLastChangeLog()
/*  712:     */     throws Exception
/*  713:     */   {
/*  714: 932 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  715:     */     
/*  716: 934 */     dt.setupTable("changelogs", "max(id) bookmark", "", "test", false);
/*  717: 935 */     dt.retrieve();
/*  718:     */     
/*  719: 937 */     int i = 0;
/*  720: 937 */     if (i < dt.getNumChildren())
/*  721:     */     {
/*  722: 939 */       XModel row = dt.get(i);
/*  723:     */       
/*  724: 941 */       String time = (String)row.get(0).get();
/*  725: 942 */       return time;
/*  726:     */     }
/*  727: 945 */     return null;
/*  728:     */   }
/*  729:     */   
/*  730:     */   public String getPendingChanges()
/*  731:     */     throws Exception
/*  732:     */   {
/*  733: 951 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  734:     */     
/*  735: 953 */     dt.setupTable("changelogs", "SUM(IF(VALUE LIKE \"%table='keyvalue'%\",1,0)) datachanges,SUM(IF(VALUE LIKE \"%table='tasks'%\",1,0)) taskchanges", "status is null", "test", false);
/*  736: 954 */     dt.retrieve();
/*  737: 955 */     String taskchanges = dt.get(0).get("taskchanges").toString();
/*  738: 956 */     taskchanges = (taskchanges == null) || (taskchanges.equals("null")) ? "0" : taskchanges;
/*  739: 957 */     String datachanges = dt.get(0).get("datachanges").toString();
/*  740: 958 */     datachanges = (datachanges == null) || (datachanges.equals("null")) ? "0" : datachanges;
/*  741: 959 */     String msg = taskchanges + " Task and " + datachanges + " Data changes";
/*  742: 960 */     return msg;
/*  743:     */   }
/*  744:     */   
/*  745:     */   public void sendServerLogs(String participant, String recepients, String frombookmark, String tobookmark)
/*  746:     */     throws Exception
/*  747:     */   {
/*  748: 966 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  749: 967 */     System.out.println("bookmark " + frombookmark);
/*  750: 968 */     dt.setupTable("changelogs", "value", " status is null and id >'" + frombookmark + "' and id <'" + tobookmark + "'", "test", false);
/*  751: 969 */     dt.retrieve();
/*  752:     */     
/*  753: 971 */     Vector logs = new Vector();
/*  754: 972 */     Client cl = new Client();
/*  755: 973 */     cl.participant = participant;
/*  756: 974 */     cl.operation = null;
/*  757: 976 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  758:     */     {
/*  759: 978 */       XModel row = dt.get(i);
/*  760:     */       
/*  761: 980 */       String log = (String)row.get(0).get();
/*  762: 981 */       logs.add(log);
/*  763:     */     }
/*  764: 984 */     cl.run(logs, recepients);
/*  765: 985 */     PreparedStatement ps = dt.getTable().getPreparedStatement("update changelogs set status='1'");
/*  766: 986 */     ps.execute();
/*  767: 987 */     ps.close();
/*  768:     */   }
/*  769:     */   
/*  770:     */   public synchronized void sendOutBoundResources()
/*  771:     */     throws Exception
/*  772:     */   {
/*  773: 993 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  774:     */     
/*  775: 995 */     String where = " status is null";
/*  776:     */     
/*  777: 997 */     dt.setupTable("resource_outbound_queue", "*", where, "test", false);
/*  778: 998 */     dt.retrieve();
/*  779: 999 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  780:     */     {
/*  781:1001 */       XModel row = dt.get(i);
/*  782:1002 */       String rec = (String)((XModel)row.get("recepient")).get();
/*  783:1003 */       String fromPath = ((XModel)row.get("resource")).get().toString();
/*  784:1004 */       String toPath = ((XModel)row.get("resource1")).get().toString();
/*  785:     */       
/*  786:1006 */       Client cl = new Client();
/*  787:1007 */       cl.operation = "deliver";
/*  788:1008 */       cl.run(fromPath, toPath, rec);
/*  789:     */     }
/*  790:1011 */     PreparedStatement ps = dt.getTable().getPreparedStatement("update resource_outbound_queue set status='1'");
/*  791:1012 */     ps.execute();
/*  792:1013 */     ps.close();
/*  793:     */   }
/*  794:     */   
/*  795:     */   public synchronized void sendOutboundLogs(String participant)
/*  796:     */     throws Exception
/*  797:     */   {
/*  798:1017 */     sendOutboundLogs(participant, "default");
/*  799:     */   }
/*  800:     */   
/*  801:     */   public synchronized void sendOutboundLogs(String participant, String dest)
/*  802:     */     throws Exception
/*  803:     */   {
/*  804:1022 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  805:     */     
/*  806:1024 */     String where = " status is null";
/*  807:     */     
/*  808:1026 */     dt.setupTable("changelog_outbound_queue", "*", where, "test", false);
/*  809:1027 */     dt.retrieve();
/*  810:1028 */     Hashtable ht = new Hashtable();
/*  811:1030 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  812:     */     {
/*  813:1032 */       XModel row = dt.get(i);
/*  814:1033 */       String rec = (String)((XModel)row.get("recepient")).get();
/*  815:1034 */       Vector[] bks = (Vector[])ht.get(rec);
/*  816:1035 */       if (bks == null)
/*  817:     */       {
/*  818:1037 */         bks = new Vector[2];
/*  819:1038 */         bks[0] = new Vector();
/*  820:1039 */         bks[1] = new Vector();
/*  821:     */       }
/*  822:1042 */       bks[0].add(((XModel)row.get("frombookmark")).get());
/*  823:1043 */       bks[1].add(((XModel)row.get("tobookmark")).get());
/*  824:1044 */       ht.put(rec, bks);
/*  825:     */     }
/*  826:1047 */     Enumeration keys = ht.keys();
/*  827:1048 */     while (keys.hasMoreElements())
/*  828:     */     {
/*  829:1050 */       String id = (String)keys.nextElement();
/*  830:1051 */       Vector[] bks = (Vector[])ht.get(id);
/*  831:1052 */       sendServerLogs(participant, id, bks[0], bks[1], dest);
/*  832:1053 */       PreparedStatement ps = dt.getTable().getPreparedStatement("update changelog_outbound_queue set status='1' where recepient='" + id + "'");
/*  833:1054 */       ps.execute();
/*  834:1055 */       ps.close();
/*  835:     */     }
/*  836:     */   }
/*  837:     */   
/*  838:     */   public void createNotification(String subject, String summary, String summaryquery, String query, String template, String status, String sent, String type)
/*  839:     */     throws Exception
/*  840:     */   {
/*  841:1062 */     XModel xm = new XBaseModel();
/*  842:1063 */     ((XModel)xm.get("subject")).set(subject);
/*  843:1064 */     ((XModel)xm.get("summary")).set(summary);
/*  844:1065 */     ((XModel)xm.get("summaryquery")).set(summaryquery);
/*  845:1066 */     ((XModel)xm.get("query")).set(query);
/*  846:1067 */     ((XModel)xm.get("template")).set(template);
/*  847:     */     
/*  848:1069 */     TestXUIDB.getInstance().saveData("notifications", "false", xm);
/*  849:     */   }
/*  850:     */   
/*  851:     */   public void sendServerLogs(String participant, String recepients, Vector frombookmark, Vector tobookmark, String dest)
/*  852:     */     throws Exception
/*  853:     */   {
/*  854:1074 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  855:1075 */     System.out.println("bookmark " + frombookmark);
/*  856:1076 */     String where = "";
/*  857:1077 */     for (int i = 0; i < frombookmark.size(); i++)
/*  858:     */     {
/*  859:1079 */       String cond = " id >'" + frombookmark.get(i) + "' and id <='" + tobookmark.get(i) + "'";
/*  860:1081 */       if (i == 0) {
/*  861:1082 */         where = where + cond;
/*  862:     */       } else {
/*  863:1084 */         where = where + " or " + cond;
/*  864:     */       }
/*  865:     */     }
/*  866:1086 */     System.out.println(" COndition " + where);
/*  867:1087 */     dt.setupTable("changelogs", "value", where, "test", false);
/*  868:1088 */     dt.retrieve();
/*  869:     */     
/*  870:1090 */     Vector logs = new Vector();
/*  871:1091 */     Client cl = new Client();
/*  872:1092 */     cl.init(dest);
/*  873:1093 */     cl.participant = participant;
/*  874:1094 */     cl.operation = null;
/*  875:1096 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  876:     */     {
/*  877:1098 */       XModel row = dt.get(i);
/*  878:     */       
/*  879:1100 */       String log = (String)row.get(0).get();
/*  880:1101 */       logs.add(log);
/*  881:     */     }
/*  882:1104 */     cl.run(logs, recepients);
/*  883:1105 */     PreparedStatement ps = dt.getTable().getPreparedStatement("update changelogs set status='1'");
/*  884:1106 */     ps.execute();
/*  885:1107 */     ps.close();
/*  886:     */   }
/*  887:     */   
/*  888:     */   public void sendServerLogs2(String participant, String recepients, String frombookmark, String tobookmark)
/*  889:     */     throws Exception
/*  890:     */   {
/*  891:1113 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  892:1114 */     System.out.println("bookmark " + frombookmark);
/*  893:1115 */     dt.setupTable("changelogs", "value", " status is null and id >'" + frombookmark + "' and id <'" + tobookmark + "'", "test", true);
/*  894:1116 */     dt.retrieve();
/*  895:     */     
/*  896:1118 */     Client cl = new Client();
/*  897:1119 */     cl.participant = participant;
/*  898:1120 */     cl.operation = null;
/*  899:1121 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS");
/*  900:1122 */     StringBuffer logs = new StringBuffer();
/*  901:     */     
/*  902:1124 */     logs.append("<logs>");
/*  903:1126 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  904:     */     {
/*  905:1128 */       XModel row = dt.get(i);
/*  906:     */       
/*  907:1130 */       String log = (String)row.get(0).get();
/*  908:1131 */       logs.append(log + "\r\n");
/*  909:     */     }
/*  910:1134 */     logs.append("</logs>");
/*  911:1135 */     String fname = "./" + participant + "-" + sdf.format(new Date()) + "-received.xml";
/*  912:     */     
/*  913:1137 */     FileWriter fw = new FileWriter(fname);
/*  914:     */     
/*  915:1139 */     fw.write(logs.toString());
/*  916:     */     
/*  917:1141 */     fw.close();
/*  918:     */     
/*  919:1143 */     StringTokenizer st = new StringTokenizer(recepients, ",");
/*  920:1144 */     if (st.countTokens() > 1) {
/*  921:1146 */       while (st.hasMoreTokens()) {
/*  922:1148 */         deliverMessage(fname, st.nextToken());
/*  923:     */       }
/*  924:     */     } else {
/*  925:1152 */       deliverMessage(fname, recepients);
/*  926:     */     }
/*  927:1153 */     System.out.println("dELIVERED");
/*  928:1154 */     PreparedStatement ps = dt.getTable().getPreparedStatement("update changelogs set status='1'");
/*  929:1155 */     ps.execute();
/*  930:1156 */     ps.close();
/*  931:     */   }
/*  932:     */   
/*  933:     */   public void sendServerLogs1(String participant, String recepients)
/*  934:     */     throws Exception
/*  935:     */   {
/*  936:1162 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  937:     */     
/*  938:1164 */     dt.setupTable("changelogs", "value", " status is null or status !=1 ", "test", false);
/*  939:1165 */     dt.retrieve();
/*  940:     */     
/*  941:1167 */     Vector logs = new Vector();
/*  942:1168 */     Client cl = new Client();
/*  943:1169 */     cl.participant = participant;
/*  944:1170 */     cl.operation = null;
/*  945:1172 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  946:     */     {
/*  947:1174 */       XModel row = dt.get(i);
/*  948:     */       
/*  949:1176 */       String log = (String)row.get(0).get();
/*  950:1177 */       logs.add(log);
/*  951:     */     }
/*  952:1180 */     cl.run(logs, recepients);
/*  953:1181 */     PreparedStatement ps = dt.getTable().getPreparedStatement("update changelogs set status='1'");
/*  954:1182 */     ps.execute();
/*  955:1183 */     ps.close();
/*  956:     */   }
/*  957:     */   
/*  958:     */   public int sendLogs(String participant, String recepients)
/*  959:     */     throws Exception
/*  960:     */   {
/*  961:1189 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  962:     */     
/*  963:1191 */     dt.setupTable("changelogs", "value,id", " status is null ", "test", true);
/*  964:1192 */     dt.retrieve();
/*  965:     */     
/*  966:1194 */     Vector logs = new Vector();
/*  967:1195 */     Client cl = new Client();
/*  968:1196 */     cl.participant = participant;
/*  969:     */     
/*  970:1198 */     int count = dt.getNumChildren();
/*  971:1199 */     String lastId = null;
/*  972:1201 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  973:     */     {
/*  974:1203 */       XModel row = dt.get(i);
/*  975:     */       
/*  976:1205 */       String log = (String)row.get(0).get();
/*  977:1206 */       logs.add(log);
/*  978:1207 */       lastId = (String)row.get(1).get();
/*  979:     */     }
/*  980:1210 */     if (lastId != null)
/*  981:     */     {
/*  982:1212 */       cl.run(logs, recepients);
/*  983:     */       
/*  984:1214 */       PreparedStatement ps = dt.getTable().getPreparedStatement("update changelogs set status='1' where id <= ?");
/*  985:1215 */       ps.setString(1, lastId);
/*  986:1216 */       ps.execute();
/*  987:     */     }
/*  988:1219 */     return count;
/*  989:     */   }
/*  990:     */   
/*  991:     */   public int sendLogs(String participant, String recepients, String dest)
/*  992:     */     throws Exception
/*  993:     */   {
/*  994:1224 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  995:     */     
/*  996:1226 */     dt.setupTable("changelogs", "value,id", " status is null ", "test", true);
/*  997:1227 */     dt.retrieve();
/*  998:     */     
/*  999:1229 */     Vector logs = new Vector();
/* 1000:1230 */     Client cl = new Client();
/* 1001:1231 */     cl.init(dest);
/* 1002:1232 */     cl.participant = participant;
/* 1003:     */     
/* 1004:1234 */     int count = dt.getNumChildren();
/* 1005:1235 */     String lastId = null;
/* 1006:1237 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1007:     */     {
/* 1008:1239 */       XModel row = dt.get(i);
/* 1009:     */       
/* 1010:1241 */       String log = (String)row.get(0).get();
/* 1011:1242 */       logs.add(log);
/* 1012:1243 */       lastId = (String)row.get(1).get();
/* 1013:     */     }
/* 1014:1246 */     if (lastId != null)
/* 1015:     */     {
/* 1016:1248 */       cl.run(logs, recepients);
/* 1017:     */       
/* 1018:1250 */       PreparedStatement ps = dt.getTable().getPreparedStatement("update changelogs set status='1' where id <= ?");
/* 1019:1251 */       ps.setString(1, lastId);
/* 1020:1252 */       ps.execute();
/* 1021:     */     }
/* 1022:1255 */     return count;
/* 1023:     */   }
/* 1024:     */   
/* 1025:     */   public int sendLogs(String participant, String recepients, String frombookmark, String tobookmark, String dest)
/* 1026:     */     throws Exception
/* 1027:     */   {
/* 1028:1261 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1029:     */     
/* 1030:1263 */     dt.setupTable("changelogs", "value,id", " status is null and id >" + frombookmark + " and id <=" + tobookmark, "test", true);
/* 1031:1264 */     dt.retrieve();
/* 1032:     */     
/* 1033:1266 */     Vector logs = new Vector();
/* 1034:1267 */     Client cl = new Client();
/* 1035:1268 */     cl.init(dest);
/* 1036:1269 */     cl.participant = participant;
/* 1037:     */     
/* 1038:1271 */     int count = dt.getNumChildren();
/* 1039:1272 */     String lastId = null;
/* 1040:1274 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1041:     */     {
/* 1042:1276 */       XModel row = dt.get(i);
/* 1043:     */       
/* 1044:1278 */       String log = (String)row.get(0).get();
/* 1045:1279 */       logs.add(log);
/* 1046:1280 */       lastId = (String)row.get(1).get();
/* 1047:     */     }
/* 1048:1283 */     if (lastId != null)
/* 1049:     */     {
/* 1050:1285 */       cl.run(logs, recepients);
/* 1051:     */       
/* 1052:1287 */       PreparedStatement ps = dt.getTable().getPreparedStatement("update changelogs set status='1' where  id >" + frombookmark + " and id <=" + tobookmark);
/* 1053:     */       
/* 1054:1289 */       ps.execute();
/* 1055:     */     }
/* 1056:1292 */     return count;
/* 1057:     */   }
/* 1058:     */   
/* 1059:     */   public void sendLogsLocal(String path, String participant, String recepients)
/* 1060:     */     throws Exception
/* 1061:     */   {
/* 1062:1298 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1063:1299 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS");
/* 1064:1300 */     dt.setupTable("changelogs", "value", " status is null ", "test", true);
/* 1065:1301 */     dt.retrieve();
/* 1066:     */     
/* 1067:1303 */     StringBuffer logs = new StringBuffer();
/* 1068:1304 */     Client cl = new Client();
/* 1069:1305 */     cl.participant = participant;
/* 1070:1306 */     logs.append("<logs>");
/* 1071:1308 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1072:     */     {
/* 1073:1310 */       XModel row = dt.get(i);
/* 1074:     */       
/* 1075:1312 */       String log = (String)row.get(0).get();
/* 1076:1313 */       logs.append(log + "\r\n");
/* 1077:     */     }
/* 1078:1316 */     logs.append("</logs>");
/* 1079:1317 */     String fname = path + "/" + participant + "-" + sdf.format(new Date()) + "-received.xml";
/* 1080:     */     
/* 1081:1319 */     FileWriter fw = new FileWriter(fname);
/* 1082:     */     
/* 1083:1321 */     fw.write(logs.toString());
/* 1084:     */     
/* 1085:1323 */     fw.close();
/* 1086:     */     
/* 1087:1325 */     PreparedStatement ps = dt.getTable().getPreparedStatement("update changelogs set status='1'");
/* 1088:1326 */     ps.execute();
/* 1089:1327 */     ps.close();
/* 1090:     */   }
/* 1091:     */   
/* 1092:     */   public void getTask(String path, String team, XTaskModel ioM, String surveyType, String area, String house, String household, String individual)
/* 1093:     */   {
/* 1094:1332 */     String taskStr = path;
/* 1095:1333 */     String context = ((area != null) && (!area.equals("")) && (!area.equals("-1")) ? "and area=" + area : "") + ((house != null) && (!house.equals("")) ? " and house=" + house : "") + ((household != null) && (!household.equals("")) ? " and household=" + household : "") + ((individual != null) && (!individual.equals("")) ? " and member=" + individual : "");
/* 1096:1334 */     System.out.println(taskStr);
/* 1097:     */     try
/* 1098:     */     {
/* 1099:1337 */       String where = "assignedto=" + team + " and  survey_type='" + surveyType + "'  and task ='" + taskStr + "/%' ";
/* 1100:1338 */       System.out.println(where);
/* 1101:1339 */       DatabaseTableModel dt = new DatabaseTableModel();
/* 1102:1340 */       dt.setupTable("tasks", "*", where, "test", false);
/* 1103:     */       
/* 1104:1342 */       dt.retrieve();
/* 1105:1344 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 1106:     */       {
/* 1107:1346 */         XModel row = dt.get(i);
/* 1108:1347 */         XTaskModel taskM = new XTaskModel();
/* 1109:1348 */         ioM.append(taskM);
/* 1110:1349 */         taskM.surveyType = surveyType;
/* 1111:1350 */         taskM.task = row.get("task").toString();
/* 1112:1351 */         taskM.area = row.get("area").toString();
/* 1113:1352 */         taskM.house = row.get("house").toString();
/* 1114:1353 */         taskM.household = row.get("household").toString();
/* 1115:1354 */         taskM.member = row.get("member").toString();
/* 1116:1355 */         taskM.assignedTo = row.get("assignedto").toString();
/* 1117:1356 */         String status = row.get("status").toString();
/* 1118:     */         
/* 1119:1358 */         String taskPath = getTaskStatusPath(taskM.task, "healthcheckup", taskM.area, taskM.house, taskM.household, taskM.member);
/* 1120:1359 */         StringTokenizer st = new StringTokenizer(taskPath, "/");
/* 1121:1360 */         String taskId = "";
/* 1122:1361 */         while (st.hasMoreTokens()) {
/* 1123:1363 */           taskId = st.nextToken();
/* 1124:     */         }
/* 1125:1366 */         System.out.println(" TAsk Path " + taskPath);
/* 1126:     */         
/* 1127:1368 */         taskM.setId(taskId);
/* 1128:     */         
/* 1129:1370 */         taskM.set(status);
/* 1130:1371 */         for (int j = 0; j < dt.getNumAttributes(); j++) {
/* 1131:1373 */           if ((!dt.getAttribName(j).equals("task")) && (!dt.getAttribName(j).equals("survey_type")) && (!dt.getAttribName(j).equals("id"))) {
/* 1132:1375 */             taskM.set("@" + dt.getAttribName(j), row.get(j).get());
/* 1133:     */           }
/* 1134:     */         }
/* 1135:1378 */         System.out.println(" >>> " + taskM.get("@task"));
/* 1136:     */       }
/* 1137:     */     }
/* 1138:     */     catch (Exception e)
/* 1139:     */     {
/* 1140:1384 */       e.printStackTrace();
/* 1141:     */     }
/* 1142:     */   }
/* 1143:     */   
/* 1144:     */   public int getTaskChildCount(String parentPath, String team, XTaskModel ioM, String area, String house, String household, String individual)
/* 1145:     */   {
/* 1146:1390 */     String taskStr = parentPath;
/* 1147:1391 */     String context = ((area != null) && (!area.equals("")) && (!area.equals("-1")) ? "and area=" + area : "") + ((house != null) && (!house.equals("")) ? " and house=" + house : "") + ((household != null) && (!household.equals("")) ? " and household=" + household : "") + ((individual != null) && (!individual.equals("")) ? " and member=" + individual : "");
/* 1148:1392 */     System.out.println(taskStr);
/* 1149:     */     try
/* 1150:     */     {
/* 1151:1395 */       String where = "assignedto=" + team + " and  survey_type='2'  and task LIKE '" + taskStr + "/%' and  task NOT LIKE '" + taskStr + "/%/%' " + context;
/* 1152:1396 */       System.out.println(where);
/* 1153:1397 */       DatabaseTableModel dt = new DatabaseTableModel();
/* 1154:1398 */       dt.setupTable("tasks", "count(*) count1", where, "test", false);
/* 1155:     */       
/* 1156:1400 */       dt.retrieve();
/* 1157:     */       
/* 1158:1402 */       return Integer.parseInt(dt.get(0).get("count1").toString());
/* 1159:     */     }
/* 1160:     */     catch (Exception e)
/* 1161:     */     {
/* 1162:1406 */       e.printStackTrace();
/* 1163:     */     }
/* 1164:1408 */     return 0;
/* 1165:     */   }
/* 1166:     */   
/* 1167:     */   public void getLogisticsData(String parentPath, XLogisticsModel ioM)
/* 1168:     */   {
/* 1169:1413 */     String taskStr = parentPath;
/* 1170:1414 */     String context = "path='" + parentPath + "'";
/* 1171:1415 */     System.out.println(taskStr);
/* 1172:     */     try
/* 1173:     */     {
/* 1174:1418 */       String where = context;
/* 1175:1419 */       System.out.println(where);
/* 1176:1420 */       DatabaseTableModel dt = new DatabaseTableModel();
/* 1177:1421 */       dt.setupTable("logistics", "*", where, "test", false);
/* 1178:     */       
/* 1179:1423 */       dt.retrieve();
/* 1180:1425 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 1181:     */       {
/* 1182:1427 */         XModel row = dt.get(i);
/* 1183:1428 */         XLogisticsModel lM = new XLogisticsModel();
/* 1184:1429 */         ioM.append(lM);
/* 1185:1431 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1186:     */         {
/* 1187:1433 */           System.out.println("Attributes called " + dt.getAttribName(j));
/* 1188:1434 */           if (dt.getAttribName(j).equals("name"))
/* 1189:     */           {
/* 1190:1436 */             lM.setId((String)row.get(j).get());
/* 1191:1437 */             System.out.println(row.get(j).get());
/* 1192:     */           }
/* 1193:     */           else
/* 1194:     */           {
/* 1195:1440 */             lM.set("@" + dt.getAttribName(j), row.get(j).get());
/* 1196:     */           }
/* 1197:     */         }
/* 1198:     */       }
/* 1199:     */     }
/* 1200:     */     catch (Exception e)
/* 1201:     */     {
/* 1202:1450 */       e.printStackTrace();
/* 1203:     */     }
/* 1204:     */   }
/* 1205:     */   
/* 1206:     */   public void getTaskData(String where, XModel dataM)
/* 1207:     */   {
/* 1208:     */     try
/* 1209:     */     {
/* 1210:1458 */       DatabaseTableModel dt = new DatabaseTableModel();
/* 1211:1459 */       dt.setupTable("tasks", "*", where, "test", false);
/* 1212:1460 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 1213:     */       {
/* 1214:1462 */         XModel row = dt.get(i);
/* 1215:1463 */         XTaskModel taskM = new XTaskModel();
/* 1216:1464 */         dataM.append(taskM);
/* 1217:1465 */         String status = "";
/* 1218:1467 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1219:     */         {
/* 1220:1469 */           String attrib = dt.getAttribName(j);
/* 1221:1470 */           String attrib1 = attrib.toLowerCase();
/* 1222:1472 */           if (attrib1.equals("survey_type")) {
/* 1223:1473 */             taskM.surveyType = row.get(attrib).toString();
/* 1224:     */           }
/* 1225:1474 */           if (attrib1.equals("task")) {
/* 1226:1475 */             taskM.task = row.get(attrib).toString();
/* 1227:     */           }
/* 1228:1476 */           if (attrib1.equals("area")) {
/* 1229:1477 */             taskM.area = row.get(attrib).toString();
/* 1230:     */           }
/* 1231:1478 */           if (attrib1.equals("house")) {
/* 1232:1479 */             taskM.house = row.get(attrib).toString();
/* 1233:     */           }
/* 1234:1480 */           if (attrib1.equals("household")) {
/* 1235:1481 */             taskM.household = row.get(attrib).toString();
/* 1236:     */           }
/* 1237:1482 */           if (attrib1.equals("member")) {
/* 1238:1483 */             taskM.member = row.get(attrib).toString();
/* 1239:     */           }
/* 1240:1484 */           if (attrib1.equals("assignedto")) {
/* 1241:1485 */             taskM.assignedTo = row.get(attrib).toString();
/* 1242:     */           }
/* 1243:1486 */           if (attrib1.equals("survey_type")) {
/* 1244:1487 */             status = row.get(attrib).toString();
/* 1245:     */           }
/* 1246:     */         }
/* 1247:1490 */         String taskPath = getTaskStatusPath(taskM.task, "healthcheckup", taskM.area, taskM.house, taskM.household, taskM.member);
/* 1248:1491 */         dataM.get(taskPath);
/* 1249:1492 */         StringTokenizer st = new StringTokenizer(taskPath, "/");
/* 1250:1493 */         String taskId = "";
/* 1251:1494 */         while (st.hasMoreTokens()) {
/* 1252:1496 */           taskId = st.nextToken();
/* 1253:     */         }
/* 1254:1499 */         System.out.println(" TAsk Path " + taskPath);
/* 1255:     */         
/* 1256:1501 */         taskM.setId(taskId);
/* 1257:     */         
/* 1258:1503 */         taskM.set(status);
/* 1259:1505 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1260:     */         {
/* 1261:1507 */           String attrib = dt.getAttribName(j);
/* 1262:1508 */           String attrib1 = attrib.toLowerCase();
/* 1263:1510 */           if ((!attrib1.equals("task")) && (!attrib1.equals("survey_type")) && (!attrib1.equals("id"))) {
/* 1264:1512 */             taskM.set("@" + attrib1, row.get(j).get());
/* 1265:     */           }
/* 1266:     */         }
/* 1267:1515 */         System.out.println(" >>> " + taskM.get("@task"));
/* 1268:     */       }
/* 1269:     */     }
/* 1270:     */     catch (Exception e)
/* 1271:     */     {
/* 1272:1521 */       e.printStackTrace();
/* 1273:     */     }
/* 1274:     */   }
/* 1275:     */   
/* 1276:     */   public void getTasks(String parentPath, String team, XTaskModel ioM, String surveyType, String area, String house, String household, String individual)
/* 1277:     */   {
/* 1278:1527 */     init();
/* 1279:1528 */     String taskStr = parentPath;
/* 1280:1529 */     String context = ((area != null) && (!area.equals("")) && (!area.equals("-1")) && (!area.equals("0")) ? "and area='" + area + "'" : "") + ((house != null) && (!house.equals("")) ? " and house='" + house + "'" : "'") + ((household != null) && (!household.equals("")) ? " and household='" + household + "'" : "'") + ((individual != null) && (!individual.equals("")) ? " and member='" + individual + "'" : "");
/* 1281:1530 */     System.out.println(taskStr);
/* 1282:1531 */     String constraint = " and " + ioM.constraint;
/* 1283:     */     try
/* 1284:     */     {
/* 1285:1534 */       String where = "assignedto='" + team + "' and  survey_type='" + surveyType + "'  and task LIKE '" + taskStr + "/%' and  task NOT LIKE '" + taskStr + "/%/%' " + context + constraint;
/* 1286:1535 */       System.out.println(where);
/* 1287:1536 */       DatabaseTableModel dt = new DatabaseTableModel();
/* 1288:1537 */       dt.setupTable("tasks", "*", where, "test", false);
/* 1289:     */       
/* 1290:1539 */       dt.retrieve();
/* 1291:1541 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 1292:     */       {
/* 1293:1543 */         XModel row = dt.get(i);
/* 1294:1544 */         XTaskModel taskM = new XTaskModel();
/* 1295:1545 */         ioM.append(taskM);
/* 1296:     */         
/* 1297:1547 */         String status = "";
/* 1298:1548 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1299:     */         {
/* 1300:1550 */           String attrib = dt.getAttribName(j);
/* 1301:1551 */           String attrib1 = attrib.toLowerCase();
/* 1302:1553 */           if (attrib1.equals("survey_type")) {
/* 1303:1554 */             taskM.surveyType = row.get(attrib).toString();
/* 1304:     */           }
/* 1305:1555 */           if (attrib1.equals("task")) {
/* 1306:1556 */             taskM.task = row.get(attrib).toString();
/* 1307:     */           }
/* 1308:1557 */           if (attrib1.equals("area")) {
/* 1309:1558 */             taskM.area = row.get(attrib).toString();
/* 1310:     */           }
/* 1311:1559 */           if (attrib1.equals("house")) {
/* 1312:1560 */             taskM.house = row.get(attrib).toString();
/* 1313:     */           }
/* 1314:1561 */           if (attrib1.equals("household")) {
/* 1315:1562 */             taskM.household = row.get(attrib).toString();
/* 1316:     */           }
/* 1317:1563 */           if (attrib1.equals("member")) {
/* 1318:1564 */             taskM.member = row.get(attrib).toString();
/* 1319:     */           }
/* 1320:1565 */           if (attrib1.equals("assignedto")) {
/* 1321:1566 */             taskM.assignedTo = row.get(attrib).toString();
/* 1322:     */           }
/* 1323:1567 */           if (attrib1.equals("survey_type")) {
/* 1324:1568 */             status = row.get(attrib).toString();
/* 1325:     */           }
/* 1326:     */         }
/* 1327:1571 */         String taskPath = getTaskStatusPath(taskM.task, "healthcheckup", taskM.area, taskM.house, taskM.household, taskM.member);
/* 1328:1572 */         StringTokenizer st = new StringTokenizer(taskPath, "/");
/* 1329:1573 */         String taskId = "";
/* 1330:1574 */         while (st.hasMoreTokens()) {
/* 1331:1576 */           taskId = st.nextToken();
/* 1332:     */         }
/* 1333:1579 */         System.out.println(" TAsk Path " + taskPath);
/* 1334:     */         
/* 1335:1581 */         taskM.setId(taskId);
/* 1336:     */         
/* 1337:1583 */         taskM.set(status);
/* 1338:1585 */         for (int j = 0; j < dt.getNumAttributes(); j++) {
/* 1339:1587 */           if ((!dt.getAttribName(j).equals("task")) && (!dt.getAttribName(j).equals("survey_type")) && (!dt.getAttribName(j).equals("id"))) {
/* 1340:1589 */             taskM.set("@" + dt.getAttribName(j), row.get(j).get());
/* 1341:     */           }
/* 1342:     */         }
/* 1343:1592 */         System.out.println(" >>> " + taskM.get("@task"));
/* 1344:     */       }
/* 1345:     */     }
/* 1346:     */     catch (Exception e)
/* 1347:     */     {
/* 1348:1598 */       e.printStackTrace();
/* 1349:     */     }
/* 1350:     */   }
/* 1351:     */   
/* 1352:     */   public void testP(String parentPath, String team, XModel ioM)
/* 1353:     */   {
/* 1354:1604 */     String taskStr = parentPath;
/* 1355:     */     
/* 1356:1606 */     System.out.println(taskStr);
/* 1357:     */     try
/* 1358:     */     {
/* 1359:1609 */       String where = "assignedto=" + team + " and  survey_type='2'  ";
/* 1360:1610 */       DatabaseTableModel dt = new DatabaseTableModel();
/* 1361:1611 */       dt.setupTable("tasks", "*", where, "test", false);
/* 1362:     */       
/* 1363:1613 */       dt.retrieve();
/* 1364:1615 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 1365:     */       {
/* 1366:1617 */         XModel row = dt.get(i);
/* 1367:     */         
/* 1368:1619 */         String task = row.get("task").toString();
/* 1369:1620 */         String area = row.get("area").toString();
/* 1370:1621 */         String house = row.get("house").toString();
/* 1371:1622 */         String household = row.get("household").toString();
/* 1372:1623 */         String member = row.get("member").toString();
/* 1373:1624 */         String status = row.get("status").toString();
/* 1374:     */         
/* 1375:1626 */         String taskPath = getTaskStatusPath(task, "healthcheckup", area, house, household, member);
/* 1376:     */         
/* 1377:1628 */         XModel taskM = (XModel)ioM.get(taskPath);
/* 1378:1629 */         taskM.set(status);
/* 1379:1630 */         for (int j = 0; j < dt.getNumAttributes(); j++) {
/* 1380:1632 */           if ((!dt.getAttribName(j).equals("task")) && (!dt.getAttribName(j).equals("survey_type")) && (!dt.getAttribName(j).equals("id"))) {
/* 1381:1634 */             taskM.set("@" + dt.getAttribName(j), row.get(j).get());
/* 1382:     */           }
/* 1383:     */         }
/* 1384:1637 */         System.out.println(" >>> " + taskM.get("@task"));
/* 1385:     */       }
/* 1386:     */     }
/* 1387:     */     catch (Exception e)
/* 1388:     */     {
/* 1389:1643 */       e.printStackTrace();
/* 1390:     */     }
/* 1391:     */   }
/* 1392:     */   
/* 1393:     */   public String getPath(XModel taskM, XModel rel)
/* 1394:     */   {
/* 1395:1649 */     XModel parent = taskM.getParent();
/* 1396:1650 */     String id = taskM.getId();
/* 1397:1651 */     while ((parent != null) && (parent != rel))
/* 1398:     */     {
/* 1399:1653 */       id = parent.getId() + "/" + id;
/* 1400:1654 */       parent = parent.getParent();
/* 1401:     */     }
/* 1402:1656 */     return id;
/* 1403:     */   }
/* 1404:     */   
/* 1405:     */   public Vector split(String path, String sep, int index)
/* 1406:     */   {
/* 1407:1661 */     StringTokenizer st = new StringTokenizer(path, "/");
/* 1408:1662 */     Vector path1 = new Vector();
/* 1409:1663 */     while (st.hasMoreTokens())
/* 1410:     */     {
/* 1411:1665 */       String ele = st.nextToken();
/* 1412:1666 */       if (ele.indexOf(sep) != -1)
/* 1413:     */       {
/* 1414:1668 */         StringTokenizer st1 = new StringTokenizer(ele, sep);
/* 1415:1669 */         int count = 0;
/* 1416:1671 */         for (count = 0; (count <= index) && (st1.hasMoreTokens()); count++)
/* 1417:     */         {
/* 1418:1673 */           String tmp = st1.nextToken();
/* 1419:1675 */           if ((count == index) && (tmp != null) && (!tmp.equals("null")) && (!tmp.equals(""))) {
/* 1420:1677 */             path1.add(tmp);
/* 1421:     */           }
/* 1422:     */         }
/* 1423:     */       }
/* 1424:     */     }
/* 1425:1686 */     return path1;
/* 1426:     */   }
/* 1427:     */   
/* 1428:     */   
/* 1429:     */    @Deprecated
/* 1430:     */    
/* 1431:     */   public void dataPath(String taskPath) {}
/* 1432:     */   
/* 1433:     */   public DateFormat getMysqlDateFormat()
/* 1434:     */   {
/* 1435:1696 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 1436:1697 */     return df;
/* 1437:     */   }
/* 1438:     */   
/* 1439:     */   public int toInt(String obj, int defaultVal)
/* 1440:     */   {
/* 1441:     */     try
/* 1442:     */     {
/* 1443:1704 */       return Integer.parseInt(obj);
/* 1444:     */     }
/* 1445:     */     catch (Exception localException) {}
/* 1446:1710 */     return defaultVal;
/* 1447:     */   }
/* 1448:     */   
/* 1449:     */   public Date toDatetime(String obj, Date defaultVal)
/* 1450:     */   {
/* 1451:     */     try
/* 1452:     */     {
/* 1453:1717 */       return getDateFormat().parse(obj);
/* 1454:     */     }
/* 1455:     */     catch (Exception localException) {}
/* 1456:1723 */     return defaultVal;
/* 1457:     */   }
/* 1458:     */   
/* 1459:     */   public String toMySQlDatetime(Date obj, String defaultVal)
/* 1460:     */   {
/* 1461:     */     try
/* 1462:     */     {
/* 1463:1730 */       return getMysqlDateFormat().format(obj);
/* 1464:     */     }
/* 1465:     */     catch (Exception localException) {}
/* 1466:1736 */     return defaultVal;
/* 1467:     */   }
/* 1468:     */   
/* 1469:     */   
/* 1470:     */    @Deprecated
/* 1471:     */    
/* 1472:     */   public boolean checkIfTaskCanBeSaved(String dataPath, String taskPath)
/* 1473:     */   {
/* 1474:1742 */     return true;
/* 1475:     */   }
/* 1476:     */   
/* 1477:     */   public int saveTaskToDb(XModel taskM, String parentTaskPath, String parentDataPath)
/* 1478:     */   {
/* 1479:1747 */     String taskId = taskM.getId();
/* 1480:     */     
/* 1481:1749 */     int hiphenIndex = taskId.indexOf("-");
/* 1482:     */     
/* 1483:1751 */     String taskPath = "";
/* 1484:1752 */     String dataPath = "";
/* 1485:1753 */     if (hiphenIndex != -1)
/* 1486:     */     {
/* 1487:1755 */       taskPath = parentTaskPath + "/" + taskId.substring(0, hiphenIndex);
/* 1488:1756 */       String dataStr = taskId.substring(hiphenIndex + 1);
/* 1489:1758 */       if (!dataStr.equals("null")) {
/* 1490:1760 */         dataPath = parentDataPath + "/" + taskId.substring(taskId.indexOf("-") + 1);
/* 1491:     */       } else {
/* 1492:1764 */         return 0;
/* 1493:     */       }
/* 1494:     */     }
/* 1495:     */     else
/* 1496:     */     {
/* 1497:1769 */       taskPath = parentTaskPath + "/" + taskId;
/* 1498:     */     }
/* 1499:1772 */     System.out.println(dataPath + " " + taskPath);
/* 1500:1773 */     StringTokenizer tknzr = new StringTokenizer(dataPath, "/");
/* 1501:1774 */     String area = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1502:1775 */     String house = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1503:1776 */     String household = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1504:1777 */     String individual = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1505:1778 */     System.out.println(" Individual =" + individual);
/* 1506:     */     
/* 1507:1780 */     int assignedTo = toInt((String)taskM.get("@assignedto"), -1);
/* 1508:1781 */     int areaId = toInt(area, -1);
/* 1509:1782 */     int teamId = assignedTo;
/* 1510:1783 */     Date dtAssigned = toDatetime((String)taskM.get("@dateassigned"), null);
/* 1511:1784 */     int status = toInt((String)taskM.get(), -1);
/* 1512:1785 */     Date stTime = toDatetime((String)taskM.get("@starttime"), null);
/* 1513:1786 */     Date eTime = toDatetime((String)taskM.get("@endtime"), null);
/* 1514:1787 */     System.out.println("stTime=" + stTime);
/* 1515:1788 */     System.out.println("eTime=" + eTime);
/* 1516:1789 */     String stTimeStr = toMySQlDatetime(stTime, null);
/* 1517:1790 */     String eTimeStr = toMySQlDatetime(eTime, null);
/* 1518:1791 */     String dtAssignedStr = toMySQlDatetime(dtAssigned, null);
/* 1519:     */     
/* 1520:1793 */     int updatedRecords = 0;
/* 1521:1795 */     if (checkIfTaskCanBeSaved(dataPath, taskPath)) {
/* 1522:     */       try
/* 1523:     */       {
/* 1524:1799 */         String table = "tasks";
/* 1525:     */         
/* 1526:1801 */         String where = "task='" + taskPath + "' and area=" + areaId + " and house='" + house + "' and household='" + household + "' and member='" + individual + "' and survey_type=" + 2 + " and status != 1";
/* 1527:     */         
/* 1528:1803 */         XModel dataM = taskM;
/* 1529:     */         
/* 1530:1805 */         saveData(table, where, dataM);
/* 1531:     */       }
/* 1532:     */       catch (Exception exc)
/* 1533:     */       {
/* 1534:1809 */         exc.printStackTrace();
/* 1535:1810 */         throw new IllegalStateException("SAVE TASKS TO DB " + exc.toString());
/* 1536:     */       }
/* 1537:     */     }
/* 1538:1815 */     for (int index = 0; index < taskM.getNumChildren(); index++) {
/* 1539:1817 */       updatedRecords += saveTaskToDb(taskM.get(index), taskPath, dataPath);
/* 1540:     */     }
/* 1541:1820 */     return updatedRecords;
/* 1542:     */   }
/* 1543:     */   
/* 1544:     */   public int saveTaskToSingle(XModel taskM, String parentTaskPath)
/* 1545:     */   {
/* 1546:1825 */     String taskId = taskM.getId();
/* 1547:     */     
/* 1548:1827 */     int hiphenIndex = taskId.indexOf("-");
/* 1549:     */     
/* 1550:1829 */     String taskPath = "";
/* 1551:1830 */     String dataPath = "";
/* 1552:     */     String dataStr;
/* 1553:1832 */     if (hiphenIndex != -1)
/* 1554:     */     {
/* 1555:1834 */       taskPath = parentTaskPath + "/" + taskId.substring(0, hiphenIndex);
/* 1556:1835 */       dataStr = taskId.substring(hiphenIndex + 1);
/* 1557:     */     }
/* 1558:     */     else
/* 1559:     */     {
/* 1560:1839 */       taskPath = parentTaskPath + "/" + taskId;
/* 1561:     */     }
/* 1562:1842 */     System.out.println(dataPath + " " + taskPath);
/* 1563:1843 */     StringTokenizer tknzr = new StringTokenizer(dataPath, "/");
/* 1564:1844 */     String area = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1565:1845 */     String house = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1566:1846 */     String household = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1567:1847 */     String individual = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1568:1848 */     System.out.println(" Individual =" + individual);
/* 1569:     */     
/* 1570:1850 */     int assignedTo = toInt((String)taskM.get("@assignedto"), -1);
/* 1571:1851 */     int areaId = toInt(area, -1);
/* 1572:1852 */     int teamId = assignedTo;
/* 1573:1853 */     Date dtAssigned = toDatetime((String)taskM.get("@dateassigned"), null);
/* 1574:1854 */     int status = toInt((String)taskM.get(), -1);
/* 1575:1855 */     Date stTime = toDatetime((String)taskM.get("@starttime"), null);
/* 1576:1856 */     Date eTime = toDatetime((String)taskM.get("@endtime"), null);
/* 1577:1857 */     System.out.println("stTime=" + stTime);
/* 1578:1858 */     System.out.println("eTime=" + eTime);
/* 1579:1859 */     String stTimeStr = toMySQlDatetime(stTime, null);
/* 1580:1860 */     String eTimeStr = toMySQlDatetime(eTime, null);
/* 1581:1861 */     String dtAssignedStr = toMySQlDatetime(dtAssigned, null);
/* 1582:     */     
/* 1583:1863 */     int updatedRecords = 0;
/* 1584:1865 */     if (checkIfTaskCanBeSaved(dataPath, taskPath)) {
/* 1585:     */       try
/* 1586:     */       {
/* 1587:1869 */         String table = "tasks";
/* 1588:     */         
/* 1589:1871 */         String where = "task='" + taskPath + "' and area=" + areaId + " and house='" + house + "' and household='" + household + "' and member='" + individual + "' and survey_type=" + 2 + " and status != 1";
/* 1590:     */         
/* 1591:1873 */         XModel dataM = taskM;
/* 1592:     */         
/* 1593:1875 */         saveData(table, where, dataM);
/* 1594:     */       }
/* 1595:     */       catch (Exception exc)
/* 1596:     */       {
/* 1597:1879 */         exc.printStackTrace();
/* 1598:1880 */         throw new IllegalStateException("SAVE TASKS TO DB " + exc.toString());
/* 1599:     */       }
/* 1600:     */     }
/* 1601:1885 */     for (int index = 0; index < taskM.getNumChildren(); index++) {
/* 1602:1887 */       updatedRecords += saveTaskToDb(taskM.get(index), taskPath, dataPath);
/* 1603:     */     }
/* 1604:1890 */     return updatedRecords;
/* 1605:     */   }
/* 1606:     */   
/* 1607:     */   public DateFormat getDateFormat()
/* 1608:     */   {
/* 1609:1895 */     SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
/* 1610:1896 */     return df;
/* 1611:     */   }
/* 1612:     */   
/* 1613:     */   public XModel getAreas(String assignedTo, XModel dataM, String surveyType)
/* 1614:     */     throws Exception
/* 1615:     */   {
/* 1616:1902 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1617:1903 */     dt.setupTable("tasks", "area", "assignedto='" + assignedTo + "' and area != -1 and survey_type='" + surveyType + "'", "test", false);
/* 1618:     */     
/* 1619:1905 */     dt.setDistinct(true);
/* 1620:1906 */     dt.retrieve();
/* 1621:     */     
/* 1622:1908 */     System.out.println(" debug getareas " + dt.getNumChildren());
/* 1623:1909 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1624:     */     {
/* 1625:1911 */       String id = dt.get(i).get("area").toString();
/* 1626:1912 */       XModel tt = (XModel)dataM.get(id);
/* 1627:     */       
/* 1628:1914 */       XModel xm = getAreadetails(id);
/* 1629:     */       
/* 1630:1916 */       tt.append(xm);
/* 1631:     */     }
/* 1632:1919 */     return dataM;
/* 1633:     */   }
/* 1634:     */   
/* 1635:     */   public XModel getEnumData1(String table, String where, XModel dataM, String fields, String idField)
/* 1636:     */     throws Exception
/* 1637:     */   {
/* 1638:1924 */     System.out.println(" table " + table + " where = " + where + " fields=" + fields + " " + idField);
/* 1639:1925 */     String[] fields2 = fields.split(",");
/* 1640:1926 */     boolean removeId = !fields.equals("*");
/* 1641:1928 */     for (int i = 0; i < fields2.length; i++) {
/* 1642:1930 */       if (fields2[i].equals(idField)) {
/* 1643:1932 */         removeId = false;
/* 1644:     */       }
/* 1645:     */     }
/* 1646:1935 */     String fields1 = removeId ? fields + "," + idField : fields;
/* 1647:1936 */     System.out.println(" table " + table + " where = " + where + " fields=" + fields1 + " " + idField);
/* 1648:1937 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1649:1938 */     dt.setupTable(table, fields1, where, "test", false);
/* 1650:     */     
/* 1651:1940 */     dt.retrieve();
/* 1652:1941 */     String idField1 = idField;
/* 1653:1942 */     if (idField.contains("."))
/* 1654:     */     {
/* 1655:1944 */       StringTokenizer st = new StringTokenizer(idField, ".");
/* 1656:     */       
/* 1657:1946 */       String tt = "";
/* 1658:1947 */       while (st.hasMoreTokens()) {
/* 1659:1949 */         tt = st.nextToken();
/* 1660:     */       }
/* 1661:1952 */       System.out.println("TT  " + tt);
/* 1662:1953 */       idField1 = tt;
/* 1663:     */     }
/* 1664:1956 */     System.out.println(" debug getareas " + dt.getNumChildren() + " " + idField1);
/* 1665:1957 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1666:     */     {
/* 1667:1959 */       String id = ((XModel)dt.get(i).get(idField1)).get().toString();
/* 1668:     */       
/* 1669:1961 */       XModel rowM = (XModel)dataM.get(id);
/* 1670:1962 */       rowM.setId(id);
/* 1671:1964 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1672:     */       {
/* 1673:1966 */         String id1 = dt.getAttribName(j);
/* 1674:     */         
/* 1675:1968 */         XModel tt = (XModel)rowM.get(id1);
/* 1676:     */         
/* 1677:1970 */         Object val = dt.get(i).get(j).get();
/* 1678:1971 */         val = val == null ? "" : val;
/* 1679:1972 */         tt.set(val);
/* 1680:     */         
/* 1681:1974 */         rowM.append(tt);
/* 1682:     */       }
/* 1683:     */     }
/* 1684:1979 */     return dataM;
/* 1685:     */   }
/* 1686:     */   
/* 1687:     */   public XModel getAreas1(XModel dataM)
/* 1688:     */     throws Exception
/* 1689:     */   {
/* 1690:1984 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1691:1985 */     dt.setupTable("hc_area", "id ,name,description,landmark,pincode", "", "test", false);
/* 1692:     */     
/* 1693:1987 */     dt.setDistinct(true);
/* 1694:1988 */     dt.retrieve();
/* 1695:     */     
/* 1696:1990 */     System.out.println(" debug getareas " + dt.getNumChildren());
/* 1697:1991 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1698:     */     {
/* 1699:1993 */       String id = ((XModel)dt.get(i).get("id")).get().toString();
/* 1700:     */       
/* 1701:1995 */       XModel rowM = (XModel)dataM.get("id");
/* 1702:1996 */       rowM.setId(id);
/* 1703:1998 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1704:     */       {
/* 1705:2000 */         String id1 = dt.getAttribName(j);
/* 1706:2001 */         XModel tt = (XModel)rowM.get(id1);
/* 1707:     */         
/* 1708:2003 */         Object val = dt.get(i).get(j).get();
/* 1709:2004 */         val = val == null ? "" : val;
/* 1710:2005 */         tt.set(val);
/* 1711:     */         
/* 1712:2007 */         rowM.append(tt);
/* 1713:     */       }
/* 1714:     */     }
/* 1715:2012 */     return dataM;
/* 1716:     */   }
/* 1717:     */   
/* 1718:     */   public XModel getAreadetails(String area)
/* 1719:     */   {
/* 1720:2017 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1721:     */     
/* 1722:2019 */     String sql = "select ea.id areacode,ea.landmark landmark,ea.pincode pin, ea.name areaname, ea.description, ea.healthcheckup hcid, ea.target target, su.code sampleunitcode, su.name sampleunitname, si.name state, di.name district, su.vort vort from hc_area ea left join healthcheckup s on ea.healthcheckup=s.id left join sampleunit su on s.sunit=su.code left join states_india si on su.state=si.id  left join districts_india di on su.district=di.id ";
/* 1723:2020 */     String where = "ea.id=" + area;
/* 1724:2021 */     String fields = "ea.id as areacode,ea.landmark landmark,ea.pincode pin, ea.name areaname, ea.description, ea.healthcheckup hcid, ea.target target, su.code sampleunitcode, su.vort vort";
/* 1725:2022 */     String table = "hc_area ea left join healthcheckup s on ea.healthcheckup=s.id left join sampleunit su on s.sunit=su.code left join states_india si on su.state=si.id  left join districts_india di on su.district=di.id ";
/* 1726:     */     
/* 1727:2024 */     dt.setupTable(table, fields, where, "test", false);
/* 1728:     */     
/* 1729:2026 */     dt.retrieve();
/* 1730:2027 */     System.out.println(" sql  " + sql + "where " + where);
/* 1731:2028 */     System.out.println(" Children " + dt.getNumChildren());
/* 1732:2029 */     XBaseModel xm = new XBaseModel();
/* 1733:2030 */     xm.setId("taskinfo");
/* 1734:2032 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 1735:2034 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1736:     */       {
/* 1737:2036 */         System.out.println(" attr ---" + dt.getAttribName(j));
/* 1738:2037 */         System.out.println(" attr ---" + dt.get(i).getAttribName(j));
/* 1739:     */         
/* 1740:2039 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 1741:2040 */         tt.set(dt.get(i).get(j).get());
/* 1742:     */       }
/* 1743:     */     }
/* 1744:2045 */     return xm;
/* 1745:     */   }
/* 1746:     */   
/* 1747:     */   public void getUsersForTeam(String team, KenList list)
/* 1748:     */   {
/* 1749:2050 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1750:2051 */     dt.setupTable("team_user", "user", "team='" + team + "'", "test", false);
/* 1751:2052 */     dt.retrieve();
/* 1752:2053 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1753:     */     {
/* 1754:2055 */       String id = dt.get(i).get("user").toString();
/* 1755:     */       
/* 1756:2057 */       list.add(id);
/* 1757:     */     }
/* 1758:     */   }
/* 1759:     */   
/* 1760:     */   public XModel getHouses(String area, XModel areaM)
/* 1761:     */     throws Exception
/* 1762:     */   {
/* 1763:2064 */     System.out.println(" Debug area is " + area);
/* 1764:2065 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1765:2066 */     dt.setupTable("houses", "houseno", "enum_area=" + area, "test", false);
/* 1766:2067 */     dt.retrieve();
/* 1767:     */     
/* 1768:2069 */     System.out.println(dt.getNumChildren());
/* 1769:2070 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1770:     */     {
/* 1771:2072 */       String id = dt.get(i).get("houseno").toString();
/* 1772:2073 */       XModel tt = (XModel)areaM.get(id);
/* 1773:     */       
/* 1774:2075 */       XModel xm = getHousedetails(id, area);
/* 1775:     */       
/* 1776:2077 */       tt.append(xm);
/* 1777:     */       
/* 1778:2079 */       xm.append(getDataM("data", "gpscheck", area, id, null, null));
/* 1779:     */     }
/* 1780:2082 */     return areaM;
/* 1781:     */   }
/* 1782:     */   
/* 1783:     */   public XModel getHouses1(String area, XModel dataM)
/* 1784:     */     throws Exception
/* 1785:     */   {
/* 1786:2087 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1787:2088 */     dt.setupTable("houses", "houseno ,address1,pin", "enum_area='" + area + "'", "test", false);
/* 1788:     */     
/* 1789:2090 */     dt.setDistinct(true);
/* 1790:2091 */     dt.retrieve();
/* 1791:     */     
/* 1792:2093 */     System.out.println(" debug getareas " + dt.getNumChildren());
/* 1793:2094 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1794:     */     {
/* 1795:2096 */       String id = ((XModel)dt.get(i).get("houseno")).get().toString();
/* 1796:     */       
/* 1797:2098 */       XModel rowM = (XModel)dataM.get(id);
/* 1798:2099 */       rowM.setId(id);
/* 1799:2101 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1800:     */       {
/* 1801:2103 */         String id1 = dt.getAttribName(j);
/* 1802:2104 */         XModel tt = (XModel)rowM.get(id1);
/* 1803:     */         
/* 1804:2106 */         Object val = dt.get(i).get(j).get();
/* 1805:2107 */         val = val == null ? "" : val;
/* 1806:2108 */         tt.set(val);
/* 1807:     */         
/* 1808:2110 */         rowM.append(tt);
/* 1809:     */       }
/* 1810:     */     }
/* 1811:2113 */     return dataM;
/* 1812:     */   }
/* 1813:     */   
/* 1814:     */   public XModel getHousedetails(String house, String area)
/* 1815:     */   {
/* 1816:2118 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1817:2119 */     dt.setupTable("houses", "*", "enum_area=" + area + " and houseno=" + house, "test", true);
/* 1818:2120 */     dt.setName("updatestatus");
/* 1819:2121 */     dt.setId("updatestatus");
/* 1820:2122 */     dt.setTagName("data");
/* 1821:2123 */     dt.retrieve();
/* 1822:2124 */     XBaseModel xm = new XBaseModel();
/* 1823:2125 */     xm.setId("updatestatus");
/* 1824:2127 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 1825:2129 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1826:     */       {
/* 1827:2131 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 1828:2132 */         tt.set(dt.get(i).get(j).get());
/* 1829:     */       }
/* 1830:     */     }
/* 1831:2137 */     return xm;
/* 1832:     */   }
/* 1833:     */   
/* 1834:     */   public XModel getHousedetails(String house, String area, XModel xm)
/* 1835:     */   {
/* 1836:2142 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1837:2143 */     dt.setupTable("houses", "*", "enum_area=" + area + " and houseno=" + house, "test", true);
/* 1838:2144 */     dt.setName("updatestatus");
/* 1839:2145 */     dt.setId("updatestatus");
/* 1840:2146 */     dt.setTagName("data");
/* 1841:2147 */     dt.retrieve();
/* 1842:2149 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 1843:2151 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1844:     */       {
/* 1845:2153 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 1846:2154 */         tt.set(dt.get(i).get(j).get());
/* 1847:     */       }
/* 1848:     */     }
/* 1849:2159 */     return xm;
/* 1850:     */   }
/* 1851:     */   
/* 1852:     */   public String getCurrentUser()
/* 1853:     */   {
/* 1854:2164 */     XModel rootModel = XProjectManager.getCurrentProject().getModel();
/* 1855:2165 */     String currentUser = (String)((XModel)rootModel.get("temp/currentuser")).get();
/* 1856:2166 */     return currentUser;
/* 1857:     */   }
/* 1858:     */   
/* 1859:     */   public void createChangeLog(String table, String where, Vector keyFields)
/* 1860:     */     throws Exception
/* 1861:     */   {
/* 1862:2172 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1863:     */     
/* 1864:2174 */     dt.setupTable(table, "*", where, "test", true);
/* 1865:     */     
/* 1866:2176 */     dt.retrieve();
/* 1867:2177 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1868:     */     {
/* 1869:2179 */       String where1 = "";
/* 1870:2180 */       KenList kl = new KenList();
/* 1871:2181 */       for (int k = 0; k < keyFields.size(); k++)
/* 1872:     */       {
/* 1873:2183 */         String value = dt.get(i).get(keyFields.get(k).toString()).toString();
/* 1874:2184 */         kl.add(keyFields.get(k).toString() + "='" + value + "'");
/* 1875:     */       }
/* 1876:2186 */       where1 = kl.toString(" and ");
/* 1877:2187 */       ChangeLog.startLog(table, where1, getCurrentUser());
/* 1878:2188 */       String qry = "update " + table + " set ";
/* 1879:2189 */       int count = 0;
/* 1880:2190 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1881:     */       {
/* 1882:2192 */         String fld = dt.getAttribName(j);
/* 1883:     */         
/* 1884:2194 */         int colType = dt.getMetaData().getColumnType(j + 1);
/* 1885:     */         
/* 1886:2196 */         String value = dt.get(i).get(fld).toString();
/* 1887:2197 */         ChangeLog.logField(fld, value);
/* 1888:2198 */         count++;
/* 1889:     */       }
/* 1890:2200 */       ChangeLog.endLog();
/* 1891:     */     }
/* 1892:     */   }
/* 1893:     */   
/* 1894:     */   public void saveDataM(String table, String where, XModel dataM)
/* 1895:     */     throws Exception
/* 1896:     */   {
/* 1897:2207 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1898:     */     
/* 1899:2209 */     ChangeLog.startLog(table, where, getCurrentUser());
/* 1900:     */     
/* 1901:2211 */     dt.setupTable(table, "*", where, "test", true);
/* 1902:     */     
/* 1903:2213 */     dt.retrieve();
/* 1904:2214 */     if (dt.getNumChildren() > 0)
/* 1905:     */     {
/* 1906:2216 */       System.out.println(" total " + dt.getNumChildren());
/* 1907:     */       
/* 1908:2218 */       String qry = "update " + table + " set ";
/* 1909:2219 */       int count = 0;
/* 1910:2220 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1911:     */       {
/* 1912:2222 */         String fld = dt.getAttribName(j);
/* 1913:2223 */         if (!fld.equals("id"))
/* 1914:     */         {
/* 1915:2225 */           int colType = dt.getMetaData().getColumnType(j + 1);
/* 1916:2226 */           String def = colType == 4 ? "0" : null;
/* 1917:2227 */           Object value = dataM.get("@" + fld);
/* 1918:2228 */           value = "'" + value + "'";
/* 1919:2229 */           qry = qry + (count == 0 ? "" : ",") + fld + "=" + value;
/* 1920:2230 */           ChangeLog.logField(fld, (String)value);
/* 1921:2231 */           count++;
/* 1922:     */         }
/* 1923:     */       }
/* 1924:2234 */       int updates = dt.executeUpdate(qry + " where " + where);
/* 1925:2235 */       System.out.println("updates " + updates);
/* 1926:     */     }
/* 1927:     */     else
/* 1928:     */     {
/* 1929:2239 */       String s = "insert into " + table;
/* 1930:2240 */       String flds = "";
/* 1931:2241 */       String values = "";
/* 1932:2242 */       int count = 0;
/* 1933:2243 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1934:     */       {
/* 1935:2245 */         String fld = dt.getAttribName(j);
/* 1936:2246 */         if (!fld.equals("id"))
/* 1937:     */         {
/* 1938:2248 */           int colType = dt.getMetaData().getColumnType(j + 1);
/* 1939:2249 */           String def = colType == 4 ? "0" : null;
/* 1940:     */           
/* 1941:2251 */           Object value = dataM.get("@" + fld);
/* 1942:2252 */           value = "'" + value + "'";
/* 1943:2253 */           flds = flds + (count != 0 ? "," : "") + fld;
/* 1944:2254 */           values = values + (count != 0 ? "," : "") + value;
/* 1945:2255 */           ChangeLog.logField(fld, (String)value);
/* 1946:2256 */           count++;
/* 1947:     */         }
/* 1948:     */       }
/* 1949:2259 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 1950:     */       
/* 1951:2261 */       System.out.println(" Debug sql " + s);
/* 1952:     */       
/* 1953:2263 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 1954:2264 */       ps.execute();
/* 1955:2265 */       dt.getTable().releasePreparedStatement(ps);
/* 1956:2266 */       ChangeLog.endLog();
/* 1957:     */     }
/* 1958:     */   }
/* 1959:     */   
/* 1960:     */   public void saveDataM1(String table, String where, XModel dataM)
/* 1961:     */     throws Exception
/* 1962:     */   {
/* 1963:2271 */     System.out.println("----Here----");
/* 1964:2272 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1965:     */     
/* 1966:2274 */     ChangeLog.startLog(table, where, getCurrentUser());
/* 1967:     */     
/* 1968:2276 */     dt.setupTable(table, "*", where, "test", false);
/* 1969:     */     
/* 1970:2278 */     dt.retrieve();
/* 1971:2279 */     if (dt.getNumChildren() > 0)
/* 1972:     */     {
/* 1973:2281 */       System.out.println(" total " + dt.getNumChildren());
/* 1974:     */       
/* 1975:2283 */       String qry = "update " + table + " set ";
/* 1976:2284 */       int count = 0;
/* 1977:2285 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 1978:     */       {
/* 1979:2287 */         String fld = dataM.get(j).getId();
/* 1980:2288 */         int col = dt.getAttribute(fld);
/* 1981:2289 */         if (!fld.equals("id"))
/* 1982:     */         {
/* 1983:2291 */           Object value = ((XModel)dataM.get(fld)).get();
/* 1984:     */           
/* 1985:2293 */           qry = qry + (count == 0 ? "" : ",") + fld + "='" + value + "'";
/* 1986:2294 */           ChangeLog.logField(fld, (String)value);
/* 1987:2295 */           count++;
/* 1988:     */         }
/* 1989:     */       }
/* 1990:2298 */       System.out.println("Update query " + qry);
/* 1991:2299 */       int updates = dt.executeUpdate(qry + " where " + where);
/* 1992:2300 */       System.out.println("updates " + updates);
/* 1993:2301 */       ChangeLog.endLog();
/* 1994:     */     }
/* 1995:     */     else
/* 1996:     */     {
/* 1997:2305 */       String s = "insert into " + table;
/* 1998:2306 */       String flds = "";
/* 1999:2307 */       String values = "";
/* 2000:2308 */       int count = 0;
/* 2001:2310 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 2002:     */       {
/* 2003:2312 */         String fld = dataM.get(j).getId();
/* 2004:2313 */         int col = dt.getAttribute(fld);
/* 2005:     */         
/* 2006:2315 */         Object value = ((XModel)dataM.get(fld)).get();
/* 2007:     */         
/* 2008:2317 */         flds = flds + (count != 0 ? "," : "") + fld;
/* 2009:2318 */         values = values + (count != 0 ? "," : "") + "'" + value + "'";
/* 2010:2319 */         ChangeLog.logField(fld, (String)value);
/* 2011:2320 */         count++;
/* 2012:     */       }
/* 2013:2322 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 2014:     */       
/* 2015:2324 */       System.out.println(" Debug sql " + s);
/* 2016:     */       
/* 2017:2326 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2018:2327 */       ps.execute();
/* 2019:2328 */       ChangeLog.endLog();
/* 2020:2329 */       dt.getTable().releasePreparedStatement(ps);
/* 2021:     */     }
/* 2022:     */   }
/* 2023:     */   
/* 2024:     */   public void saveDataM2(String table, String where, XModel dataM)
/* 2025:     */     throws Exception
/* 2026:     */   {
/* 2027:2335 */     System.out.println("----Here----");
/* 2028:2336 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2029:     */     
/* 2030:2338 */     dt.setupTable(table, "*", where, "test", false);
/* 2031:     */     
/* 2032:2340 */     dt.retrieve();
/* 2033:2341 */     if (dt.getNumChildren() > 0)
/* 2034:     */     {
/* 2035:2343 */       System.out.println(" total " + dt.getNumChildren());
/* 2036:     */       
/* 2037:2345 */       String qry = "update " + table + " set ";
/* 2038:2346 */       int count = 0;
/* 2039:2347 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 2040:     */       {
/* 2041:2349 */         String fld = dataM.get(j).getId();
/* 2042:2350 */         int col = dt.getAttribute(fld);
/* 2043:2351 */         if (!fld.equals("id"))
/* 2044:     */         {
/* 2045:2353 */           Object value = ((XModel)dataM.get(fld)).get();
/* 2046:     */           
/* 2047:2355 */           qry = qry + (count == 0 ? "" : ",") + fld + "='" + value + "'";
/* 2048:     */           
/* 2049:2357 */           count++;
/* 2050:     */         }
/* 2051:     */       }
/* 2052:2360 */       System.out.println("Update query " + qry);
/* 2053:2361 */       int updates = dt.executeUpdate(qry + " where " + where);
/* 2054:2362 */       System.out.println("updates " + updates);
/* 2055:     */     }
/* 2056:     */     else
/* 2057:     */     {
/* 2058:2366 */       String s = "insert into " + table;
/* 2059:2367 */       String flds = "";
/* 2060:2368 */       String values = "";
/* 2061:2369 */       int count = 0;
/* 2062:2371 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 2063:     */       {
/* 2064:2373 */         String fld = dataM.get(j).getId();
/* 2065:2374 */         int col = dt.getAttribute(fld);
/* 2066:     */         
/* 2067:2376 */         Object value = ((XModel)dataM.get(fld)).get();
/* 2068:     */         
/* 2069:2378 */         flds = flds + (count != 0 ? "," : "") + fld;
/* 2070:2379 */         values = values + (count != 0 ? "," : "") + "'" + value + "'";
/* 2071:     */         
/* 2072:2381 */         count++;
/* 2073:     */       }
/* 2074:2383 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 2075:     */       
/* 2076:2385 */       System.out.println(" Debug sql " + s);
/* 2077:     */       
/* 2078:2387 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2079:2388 */       ps.execute();
/* 2080:     */       
/* 2081:2390 */       dt.getTable().releasePreparedStatement(ps);
/* 2082:     */     }
/* 2083:     */   }
/* 2084:     */   
/* 2085:     */   public void updateTaskStatus(XTaskModel taskM, String status)
/* 2086:     */     throws Exception
/* 2087:     */   {
/* 2088:2397 */     updateTaskStatus(taskM.task, taskM.surveyType, taskM.area, taskM.house, taskM.household, taskM.member, taskM.get("@assignedto").toString(), status);
/* 2089:     */   }
/* 2090:     */   
/* 2091:     */   public void updateTaskStatus(String task, String surveyType, String area, String house, String hh, String idc, String assignedTo, String status)
/* 2092:     */     throws Exception
/* 2093:     */   {
/* 2094:2432 */     throw new Error("Unresolved compilation problems: \n\tendtime cannot be resolved to a variable\n\tendtime cannot be resolved to a variable\n\tendtime cannot be resolved to a variable\n\tType mismatch: cannot convert from int to String\n");
/* 2095:     */   }
/* 2096:     */   
/* 2097:     */   public void saveDataMUTF(String table, String where, XModel dataM)
/* 2098:     */     throws Exception
/* 2099:     */   {
/* 2100:2447 */     System.out.println("----Here----");
/* 2101:2448 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2102:     */     
/* 2103:2450 */     ChangeLog.startLog(table, where, getCurrentUser());
/* 2104:     */     
/* 2105:2452 */     dt.setupTable(table, "*", where, "test", false);
/* 2106:     */     
/* 2107:2454 */     dt.retrieve();
/* 2108:2455 */     if (dt.getNumChildren() > 0)
/* 2109:     */     {
/* 2110:2457 */       System.out.println(" total " + dt.getNumChildren());
/* 2111:     */       
/* 2112:2459 */       String qry = "update " + table + " set ";
/* 2113:2460 */       int count = 0;
/* 2114:2461 */       Vector values1 = new Vector();
/* 2115:2462 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 2116:     */       {
/* 2117:2464 */         String fld = dataM.get(j).getId();
/* 2118:2465 */         int col = dt.getAttribute(fld);
/* 2119:     */         
/* 2120:2467 */         Object value = ((XModel)dataM.get(fld)).get();
/* 2121:     */         
/* 2122:2469 */         values1.add(value);
/* 2123:2470 */         qry = qry + (count == 0 ? "" : ",") + fld + "=?";
/* 2124:2471 */         ChangeLog.logField(fld, (String)value);
/* 2125:2472 */         count++;
/* 2126:     */       }
/* 2127:2475 */       String s = qry + " where " + where;
/* 2128:2476 */       System.out.println("Update query " + s);
/* 2129:2477 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2130:2479 */       for (int i = 0; i < values1.size(); i++)
/* 2131:     */       {
/* 2132:2481 */         String val = (String)values1.get(i);
/* 2133:2482 */         if (val != null) {
/* 2134:2483 */           ps.setBytes(i + 1, val.getBytes("utf-8"));
/* 2135:     */         } else {
/* 2136:2485 */           ps.setString(i + 1, null);
/* 2137:     */         }
/* 2138:     */       }
/* 2139:2487 */       System.out.println(" No of updates " + ps.executeUpdate());
/* 2140:2488 */       ChangeLog.endLog();
/* 2141:     */     }
/* 2142:     */     else
/* 2143:     */     {
/* 2144:2492 */       String s = "insert into " + table;
/* 2145:2493 */       String flds = "";
/* 2146:2494 */       String values = "";
/* 2147:2495 */       int count = 0;
/* 2148:2496 */       Vector values1 = new Vector();
/* 2149:2498 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 2150:     */       {
/* 2151:2500 */         String fld = dataM.get(j).getId();
/* 2152:2501 */         int col = dt.getAttribute(fld);
/* 2153:     */         
/* 2154:2503 */         Object value = ((XModel)dataM.get(fld)).get();
/* 2155:     */         
/* 2156:2505 */         flds = flds + (count != 0 ? "," : "") + fld;
/* 2157:     */         
/* 2158:2507 */         values = values + (count != 0 ? "," : "") + "?";
/* 2159:2508 */         values1.add(value);
/* 2160:2509 */         ChangeLog.logField(fld, (String)value);
/* 2161:2510 */         count++;
/* 2162:     */       }
/* 2163:2512 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 2164:     */       
/* 2165:2514 */       System.out.println(" Debug sql " + s);
/* 2166:     */       
/* 2167:2516 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2168:2518 */       for (int i = 0; i < values1.size(); i++)
/* 2169:     */       {
/* 2170:2520 */         String val = (String)values1.get(i);
/* 2171:2521 */         if (val != null) {
/* 2172:2522 */           ps.setBytes(i + 1, val.getBytes("utf-8"));
/* 2173:     */         } else {
/* 2174:2524 */           ps.setString(i + 1, null);
/* 2175:     */         }
/* 2176:     */       }
/* 2177:2526 */       ps.execute();
/* 2178:2527 */       ChangeLog.endLog();
/* 2179:2528 */       dt.getTable().releasePreparedStatement(ps);
/* 2180:     */     }
/* 2181:     */   }
/* 2182:     */   
/* 2183:     */   public void importChangeLog(String log)
/* 2184:     */     throws Exception
/* 2185:     */   {
/* 2186:2535 */     StringReader sr = new StringReader(log);
/* 2187:2536 */     XmlElement xe = XmlSource.read(sr);
/* 2188:2537 */     String logId = xe.getAttribute("id");
/* 2189:2538 */     XmlElement dt1 = xe.elementAt(0);
/* 2190:2539 */     String table = dt1.getAttribute("table");
/* 2191:2540 */     String where = dt1.getAttribute("key");
/* 2192:2541 */     String user1 = dt1.getAttribute("user");
/* 2193:2542 */     String op = dt1.getAttribute("op");
/* 2194:2543 */     XmlElement data = dt1.elementAt(0);
/* 2195:2544 */     Enumeration e = data.enumerateAttributeNames();
/* 2196:2545 */     if ((op != null) && (op.equals("deleteAll")))
/* 2197:     */     {
/* 2198:2547 */       deleteResources("./images");
/* 2199:2548 */       deleteAllData();
/* 2200:     */     }
/* 2201:2550 */     else if ((op != null) && (op.equals("deleteResource")))
/* 2202:     */     {
/* 2203:2552 */       deleteResource(table, user);
/* 2204:     */     }
/* 2205:2554 */     else if ((op != null) && (op.equals("delete")))
/* 2206:     */     {
/* 2207:2556 */       deleteData1(table, where, data, user);
/* 2208:     */     }
/* 2209:     */     else
/* 2210:     */     {
/* 2211:2559 */       saveData1(table, where, data, user1);
/* 2212:     */     }
/* 2213:     */   }
/* 2214:     */   
/* 2215:     */   public void logImport(String fname, String status, String reason)
/* 2216:     */     throws Exception
/* 2217:     */   {
/* 2218:     */     try
/* 2219:     */     {
/* 2220:2568 */       System.out.println(" Inside log import");
/* 2221:2569 */       XModel dataM = new XBaseModel();
/* 2222:2570 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 2223:2571 */       ((XModel)dataM.get("time")).set(sdf.format(new Date()));
/* 2224:2572 */       ((XModel)dataM.get("filename")).set(fname);
/* 2225:2573 */       ((XModel)dataM.get("status")).set(status);
/* 2226:2574 */       ((XModel)dataM.get("reason")).set(reason.replaceAll("'", "").replaceAll("\"", ""));
/* 2227:2575 */       ((XModel)dataM.get("user")).set(getCurrentUser());
/* 2228:2576 */       saveDataM2("import", "filename='" + fname + "'", dataM);
/* 2229:     */     }
/* 2230:     */     catch (Exception e)
/* 2231:     */     {
/* 2232:2580 */       e.printStackTrace();
/* 2233:     */     }
/* 2234:     */   }
/* 2235:     */   
/* 2236:     */   public void processIncompleteImports()
/* 2237:     */     throws Exception
/* 2238:     */   {
/* 2239:2587 */     String table = "import";
/* 2240:2588 */     String where = " status='processing'";
/* 2241:2589 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2242:2590 */     dt.setupTable(table, "*", where, "test", true);
/* 2243:2591 */     if (dt.getNumChildren() > 0) {
/* 2244:2593 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 2245:     */       {
/* 2246:2595 */         XModel row = dt.get(i);
/* 2247:2596 */         String fname = (String)((XModel)row.get("filename")).get();
/* 2248:2597 */         System.out.println(fname);
/* 2249:     */         
/* 2250:2599 */         new Client().reRun(fname);
/* 2251:     */       }
/* 2252:     */     }
/* 2253:     */   }
/* 2254:     */   
/* 2255:     */   public void processIncompleteImports1()
/* 2256:     */     throws Exception
/* 2257:     */   {
/* 2258:2607 */     String table = "import";
/* 2259:2608 */     String where = " status='processing'";
/* 2260:2609 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2261:2610 */     dt.setupTable(table, "*", where, "test", true);
/* 2262:2611 */     if (dt.getNumChildren() > 0) {
/* 2263:2613 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 2264:     */       {
/* 2265:2615 */         XModel row = dt.get(i);
/* 2266:2616 */         String fname = (String)((XModel)row.get("filename")).get();
/* 2267:2617 */         System.out.println(fname);
/* 2268:2618 */         importChangeLogs1(fname);
/* 2269:     */       }
/* 2270:     */     }
/* 2271:     */   }
/* 2272:     */   
/* 2273:     */   public void importChangeLogs1(String fname)
/* 2274:     */     throws Exception
/* 2275:     */   {
/* 2276:2626 */     System.out.println(" Inside import1");
/* 2277:     */     try
/* 2278:     */     {
/* 2279:2629 */       InputStreamReader r = new InputStreamReader(new FileInputStream(fname));
/* 2280:2630 */       System.out.println(" Reader got" + r);
/* 2281:2631 */       XmlElement xe0 = XmlSource.read(r);
/* 2282:2632 */       r.close();
/* 2283:2633 */       System.out.println(" Read " + fname);
/* 2284:2634 */       Vector children = xe0.getChildren();
/* 2285:2635 */       System.out.println(" Children " + xe0.getChildren());
/* 2286:2636 */       for (int i = 0; i < children.size(); i++)
/* 2287:     */       {
/* 2288:2638 */         XmlElement xe = (XmlElement)children.get(i);
/* 2289:2639 */         String logId = xe.getAttribute("id");
/* 2290:2640 */         XmlElement dt1 = xe.elementAt(0);
/* 2291:2641 */         String table = dt1.getAttribute("table");
/* 2292:2642 */         String where = dt1.getAttribute("key");
/* 2293:2643 */         String user = dt1.getAttribute("user");
/* 2294:2644 */         String op = dt1.getAttribute("op");
/* 2295:2645 */         XmlElement data = dt1.elementAt(0);
/* 2296:2646 */         Enumeration e = data.enumerateAttributeNames();
/* 2297:2647 */         while (e.hasMoreElements()) {
/* 2298:2649 */           System.out.println(data.getAttribute(e.nextElement().toString()));
/* 2299:     */         }
/* 2300:     */         try
/* 2301:     */         {
/* 2302:2654 */           if ((op != null) && (op.equals("deleteAll")))
/* 2303:     */           {
/* 2304:2656 */             deleteResources("./images");
/* 2305:2657 */             deleteAllData();
/* 2306:     */           }
/* 2307:2659 */           else if ((op != null) && (op.equals("deleteResource")))
/* 2308:     */           {
/* 2309:2661 */             deleteResource(table, user);
/* 2310:     */           }
/* 2311:2663 */           else if ((op != null) && (op.equals("delete")))
/* 2312:     */           {
/* 2313:2665 */             deleteData1(table, where, data, user);
/* 2314:     */           }
/* 2315:     */           else
/* 2316:     */           {
/* 2317:2669 */             saveData1(table, where, data, user);
/* 2318:     */           }
/* 2319:     */         }
/* 2320:     */         catch (Exception e1)
/* 2321:     */         {
/* 2322:2675 */           e1.printStackTrace();
/* 2323:     */         }
/* 2324:     */       }
/* 2325:     */     }
/* 2326:     */     catch (Exception e2)
/* 2327:     */     {
/* 2328:2683 */       e2.printStackTrace();
/* 2329:     */     }
/* 2330:     */   }
/* 2331:     */   
/* 2332:     */   public void importChangeLogs(String logs)
/* 2333:     */     throws Exception
/* 2334:     */   {
/* 2335:2690 */     System.out.println(" Inside import");
/* 2336:     */     
/* 2337:2692 */     StringReader sr = new StringReader(logs);
/* 2338:2693 */     XmlElement xe0 = XmlSource.read(sr);
/* 2339:2694 */     Vector children = xe0.getChildren();
/* 2340:2695 */     for (int i = 0; i < children.size(); i++)
/* 2341:     */     {
/* 2342:2697 */       XmlElement xe = (XmlElement)children.get(i);
/* 2343:2698 */       String logId = xe.getAttribute("id");
/* 2344:2699 */       XmlElement dt1 = xe.elementAt(0);
/* 2345:2700 */       String table = dt1.getAttribute("table");
/* 2346:2701 */       String where = dt1.getAttribute("key");
/* 2347:2702 */       String user = dt1.getAttribute("user");
/* 2348:2703 */       String op = dt1.getAttribute("op");
/* 2349:2704 */       XmlElement data = dt1.elementAt(0);
/* 2350:2705 */       Enumeration e = data.enumerateAttributeNames();
/* 2351:     */       try
/* 2352:     */       {
/* 2353:2707 */         if ((op != null) && (op.equals("deleteAll")))
/* 2354:     */         {
/* 2355:2709 */           deleteResources("./images");
/* 2356:2710 */           deleteAllData();
/* 2357:     */         }
/* 2358:2712 */         else if ((op != null) && (op.equals("deleteResource")))
/* 2359:     */         {
/* 2360:2714 */           deleteResource(table, user);
/* 2361:     */         }
/* 2362:2716 */         else if ((op != null) && (op.equals("delete")))
/* 2363:     */         {
/* 2364:2718 */           deleteData1(table, where, data, user);
/* 2365:     */         }
/* 2366:     */         else
/* 2367:     */         {
/* 2368:2722 */           saveData1(table, where, data, user);
/* 2369:     */         }
/* 2370:     */       }
/* 2371:     */       catch (Exception e1)
/* 2372:     */       {
/* 2373:2727 */         e1.printStackTrace();
/* 2374:     */       }
/* 2375:     */     }
/* 2376:     */   }
/* 2377:     */   
/* 2378:     */   public void distributeMessage(String msg, String to)
/* 2379:     */     throws Exception
/* 2380:     */   {
/* 2381:2735 */     String from = msg.split("-")[0];
/* 2382:2736 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2383:     */     
/* 2384:2738 */     dt.setupTable("msg_distribution", "*", "from1='" + from + "' and to1='" + to + "'", "test", false);
/* 2385:2739 */     dt.retrieve();
/* 2386:2740 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 2387:     */     {
/* 2388:2742 */       XModel row = dt.get(i);
/* 2389:2743 */       deliverMessage(msg, row.get("cc").toString());
/* 2390:     */     }
/* 2391:     */   }
/* 2392:     */   
/* 2393:     */   public void deliverMessage(String msg, String recepient)
/* 2394:     */     throws Exception
/* 2395:     */   {
/* 2396:2750 */     System.out.println("Inside 2");
/* 2397:2751 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2398:     */     
/* 2399:2753 */     dt.setupTable("mqueue", "*", "", "test", false);
/* 2400:2754 */     String sql = "insert into mqueue (message,recepient) VALUES('" + msg + "','" + recepient + "')";
/* 2401:2755 */     System.out.println(sql);
/* 2402:     */     
/* 2403:2757 */     PreparedStatement ps = dt.getTable().getPreparedStatement(sql);
/* 2404:2758 */     ps.execute();
/* 2405:     */   }
/* 2406:     */   
/* 2407:     */   public void updateMessageStatus(String message, String recepient)
/* 2408:     */     throws Exception
/* 2409:     */   {
/* 2410:2764 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2411:2765 */     dt.setupTable("mqueue", "*", "", "test", true);
/* 2412:     */     
/* 2413:2767 */     PreparedStatement ps = dt.getTable().getPreparedStatement("update mqueue set status='1' where message='" + message + "' and recepient='" + recepient + "'");
/* 2414:2768 */     ps.execute();
/* 2415:2769 */     ps.close();
/* 2416:     */   }
/* 2417:     */   
/* 2418:     */   public Vector getMessages(String recepient)
/* 2419:     */     throws Exception
/* 2420:     */   {
/* 2421:2773 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2422:2774 */     Vector v = new Vector();
/* 2423:2775 */     String where = " recepient='" + recepient + "' and status is null";
/* 2424:2776 */     dt.setupTable("mqueue", "message", where, "test", true);
/* 2425:     */     
/* 2426:2778 */     dt.retrieve();
/* 2427:2779 */     if (dt.getNumChildren() > 0) {
/* 2428:2781 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 2429:     */       {
/* 2430:2783 */         XModel row = dt.get(i);
/* 2431:     */         
/* 2432:2785 */         System.out.println(" total " + dt.getNumChildren());
/* 2433:2786 */         String message = (String)row.get(0).get();
/* 2434:2787 */         v.add(message);
/* 2435:     */       }
/* 2436:     */     }
/* 2437:2790 */     return v;
/* 2438:     */   }
/* 2439:     */   
/* 2440:     */   public void saveChangeLog(String log)
/* 2441:     */     throws Exception
/* 2442:     */   {
/* 2443:2796 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2444:     */     
/* 2445:2798 */     dt.setupTable("changelogs", "*", "", "test", true);
/* 2446:     */     
/* 2447:2800 */     String s = "insert into changelogs";
/* 2448:2801 */     String flds = "";
/* 2449:2802 */     String values = "";
/* 2450:2803 */     s = s + " (value) VALUES(" + log + ")";
/* 2451:     */     
/* 2452:2805 */     System.out.println(" Debug sql " + s);
/* 2453:     */     
/* 2454:2807 */     PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2455:2808 */     ps.execute();
/* 2456:2809 */     dt.getTable().releasePreparedStatement(ps);
/* 2457:     */   }
/* 2458:     */   
/* 2459:     */   public void addToResourceOutboundQueue(String recepient, String resource, String resource1, String participant)
/* 2460:     */     throws Exception
/* 2461:     */   {
/* 2462:2813 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2463:     */     
/* 2464:2815 */     dt.setupTable("resource_outbound_queue", "*", "", "test", true);
/* 2465:     */     
/* 2466:2817 */     String s = "insert into resource_outbound_queue";
/* 2467:2818 */     String flds = "";
/* 2468:2819 */     String values = "";
/* 2469:2820 */     s = s + " (recepient,resource,resource1,participant) VALUES('" + recepient + "','" + resource + "','" + resource1 + "','" + participant + "')";
/* 2470:     */     
/* 2471:2822 */     System.out.println(" Debug sql " + s);
/* 2472:     */     
/* 2473:2824 */     PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2474:2825 */     ps.execute();
/* 2475:     */   }
/* 2476:     */   
/* 2477:     */   public void addToChangeLogOutboundQueue(String recepient, String frombookmark, String tobookmark)
/* 2478:     */     throws Exception
/* 2479:     */   {
/* 2480:2830 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2481:2831 */     frombookmark = frombookmark == null ? "0" : frombookmark;
/* 2482:2832 */     dt.setupTable("changelog_outbound_queue", "*", "", "test", true);
/* 2483:     */     
/* 2484:2834 */     String s = "insert into changelog_outbound_queue";
/* 2485:2835 */     String flds = "";
/* 2486:2836 */     String values = "";
/* 2487:2837 */     s = s + " (recepient,frombookmark,tobookmark) VALUES('" + recepient + "','" + frombookmark + "','" + tobookmark + "')";
/* 2488:     */     
/* 2489:2839 */     System.out.println(" Debug sql " + s);
/* 2490:     */     
/* 2491:2841 */     PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2492:2842 */     ps.execute();
/* 2493:     */   }
/* 2494:     */   
/* 2495:     */   public void saveData(String table, String where, XModel dataM)
/* 2496:     */     throws Exception
/* 2497:     */   {
/* 2498:2848 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2499:     */     
/* 2500:2850 */     dt.setupTable(table, "*", where, "test", true);
/* 2501:     */     
/* 2502:2852 */     dt.retrieve();
/* 2503:2853 */     if (dt.getNumChildren() > 0)
/* 2504:     */     {
/* 2505:2855 */       System.out.println(" total " + dt.getNumChildren());
/* 2506:     */       
/* 2507:2857 */       String qry = "update " + table + " set ";
/* 2508:2858 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2509:     */       {
/* 2510:2860 */         String fld = dt.getAttribName(j);
/* 2511:2861 */         XModel valM = (XModel)dataM.get(fld);
/* 2512:2862 */         int colType = dt.getMetaData().getColumnType(j + 1);
/* 2513:2863 */         String def = colType == 4 ? "0" : null;
/* 2514:     */         
/* 2515:2865 */         Object value = valM == null ? null : valM.get();
/* 2516:2866 */         value = "'" + value + "'";
/* 2517:2867 */         qry = qry + (j == 0 ? "" : ",") + fld + "=" + value;
/* 2518:     */       }
/* 2519:2870 */       int updates = dt.executeUpdate(qry + " where " + where);
/* 2520:2871 */       System.out.println("updates " + updates);
/* 2521:     */     }
/* 2522:     */     else
/* 2523:     */     {
/* 2524:2875 */       String s = "insert into " + table;
/* 2525:2876 */       String flds = "";
/* 2526:2877 */       String values = "";
/* 2527:2878 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2528:     */       {
/* 2529:2880 */         String fld = dt.getAttribName(j);
/* 2530:2881 */         XModel valM = (XModel)dataM.get(fld);
/* 2531:2882 */         int colType = dt.getMetaData().getColumnType(j + 1);
/* 2532:2883 */         String def = colType == 4 ? "0" : null;
/* 2533:     */         
/* 2534:2885 */         Object value = valM == null ? null : valM.get();
/* 2535:2886 */         value = "'" + value + "'";
/* 2536:2887 */         flds = flds + (j != 0 ? "," : "") + fld;
/* 2537:2888 */         values = values + (j != 0 ? "," : "") + value;
/* 2538:     */       }
/* 2539:2890 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 2540:     */       
/* 2541:2892 */       System.out.println(" Debug sql " + s);
/* 2542:     */       
/* 2543:2894 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2544:2895 */       ps.execute();
/* 2545:2896 */       dt.getTable().releasePreparedStatement(ps);
/* 2546:     */     }
/* 2547:     */   }
/* 2548:     */   
/* 2549:     */   public void saveConflictData(String table, String where, XModel dataM)
/* 2550:     */     throws Exception
/* 2551:     */   {
/* 2552:2903 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2553:     */     
/* 2554:2905 */     dt.setupTable(table, "*", where, "test", false);
/* 2555:     */     
/* 2556:2907 */     dt.retrieve();
/* 2557:     */     
/* 2558:2909 */     String s = "insert into " + table;
/* 2559:2910 */     String flds = "";
/* 2560:2911 */     String values = "";
/* 2561:2912 */     for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2562:     */     {
/* 2563:2914 */       String fld = dt.getAttribName(j);
/* 2564:2915 */       XModel valM = (XModel)dataM.get(fld);
/* 2565:2916 */       int colType = dt.getMetaData().getColumnType(j + 1);
/* 2566:2917 */       String def = colType == 4 ? "0" : null;
/* 2567:     */       
/* 2568:2919 */       Object value = valM == null ? null : valM.get();
/* 2569:     */       
/* 2570:2921 */       flds = flds + (j != 0 ? "," : "") + fld;
/* 2571:2922 */       values = values + (j != 0 ? "," : "") + value;
/* 2572:     */     }
/* 2573:2924 */     s = s + "(" + flds + ") VALUES(" + values + ")";
/* 2574:     */     
/* 2575:2926 */     System.out.println(" Debug sql " + s);
/* 2576:     */     
/* 2577:2928 */     PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2578:2929 */     ps.execute();
/* 2579:2930 */     dt.getTable().releasePreparedStatement(ps);
/* 2580:     */   }
/* 2581:     */   
/* 2582:     */   public void saveData1(String table, String where, XmlElement dataM, String user1)
/* 2583:     */     throws Exception
/* 2584:     */   {
/* 2585:2936 */     XModel conflictData = new XBaseModel();
/* 2586:2937 */     XModel oldData = new XBaseModel();
/* 2587:2938 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 2588:2939 */     conflictData.set("date", "'" + sdf.format(new Date()) + "'");
/* 2589:2940 */     conflictData.set("surveyor", "'" + user1 + "'");
/* 2590:2941 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2591:2942 */     System.out.println(" Where is " + where);
/* 2592:2943 */     dt.setupTable(table, "*", where, "test", true);
/* 2593:2944 */     Vector values1 = new Vector();
/* 2594:     */     
/* 2595:2946 */     dt.retrieve();
/* 2596:2947 */     if (table.equals("data"))
/* 2597:     */     {
/* 2598:2949 */       String value1 = dataM.getAttribute("value");
/* 2599:2950 */       if ((value1 == null) || (value1.equals(" Choose Any One"))) {
/* 2600:2951 */         return;
/* 2601:     */       }
/* 2602:     */     }
/* 2603:2953 */     System.out.println(" Debug children " + dt.getNumChildren());
/* 2604:2954 */     if (dt.getNumChildren() > 0)
/* 2605:     */     {
/* 2606:2956 */       System.out.println(" total " + dt.getNumChildren());
/* 2607:     */       
/* 2608:2958 */       String qry = "update " + table + " set ";
/* 2609:2959 */       int count = 0;
/* 2610:2960 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2611:     */       {
/* 2612:2962 */         String fld = dt.getAttribName(j).toLowerCase();
/* 2613:2963 */         String fld1 = fld;
/* 2614:2964 */         if (fld.equals("id")) {
/* 2615:2965 */           fld = "_id";
/* 2616:     */         }
/* 2617:2966 */         String value = dataM.getAttribute(fld);
/* 2618:2967 */         String oldValue = "";
/* 2619:     */         try
/* 2620:     */         {
/* 2621:2969 */           oldValue = (String)((XModel)dt.get(0).get(fld1)).get();
/* 2622:2970 */           System.out.println(" Value " + fld1 + " " + oldValue);
/* 2623:     */         }
/* 2624:     */         catch (Exception e)
/* 2625:     */         {
/* 2626:2974 */           e.printStackTrace();
/* 2627:     */         }
/* 2628:2976 */         int colType = dt.getMetaData().getColumnType(j + 1);
/* 2629:2977 */         String def = colType == 4 ? "0" : null;
/* 2630:     */         
/* 2631:2979 */         conflictData.set(fld, value);
/* 2632:2980 */         oldData.set(fld1, oldValue);
/* 2633:2982 */         if ((value != null) && (!fld.equals("id")) && ((!table.equals("keyvalue")) || (!fld.equals("key1"))))
/* 2634:     */         {
/* 2635:2984 */           qry = qry + (count == 0 ? "" : ",") + fld + "=?";
/* 2636:2985 */           values1.add(value);
/* 2637:2986 */           count++;
/* 2638:     */         }
/* 2639:     */       }
/* 2640:2990 */       if (!table.equals("data")) {
/* 2641:2992 */         if (table.equals("tasks"))
/* 2642:     */         {
/* 2643:2994 */           String status = dataM.getAttribute("status");
/* 2644:2995 */           String endtime = dataM.getAttribute("endtime");
/* 2645:2996 */           if (((status != null) && ((status.equals("2")) || (status.equals("5")))) || ((endtime != null) && (endtime.length() > 0)))
/* 2646:     */           {
/* 2647:2998 */             System.out.println(qry + " where " + where);
/* 2648:     */             try
/* 2649:     */             {
/* 2650:3000 */               PreparedStatement ps = dt.getTable().getPreparedStatement(qry + " where " + where);
/* 2651:3002 */               for (int i = 0; i < values1.size(); i++) {
/* 2652:3004 */                 ps.setBytes(i + 1, values1.get(i).toString().getBytes("utf-8"));
/* 2653:     */               }
/* 2654:3006 */               int updates = ps.executeUpdate();
/* 2655:     */               
/* 2656:3008 */               System.out.println("updates " + updates);
/* 2657:     */             }
/* 2658:     */             catch (Exception e)
/* 2659:     */             {
/* 2660:3012 */               e.printStackTrace();
/* 2661:     */             }
/* 2662:     */           }
/* 2663:     */         }
/* 2664:3018 */         else if (table.equals("keyvalue"))
/* 2665:     */         {
/* 2666:3020 */           System.out.println(" query " + qry);
/* 2667:3021 */           PreparedStatement ps = dt.getTable().getPreparedStatement(qry + " where " + where);
/* 2668:3023 */           for (int i = 0; i < values1.size(); i++) {
/* 2669:3025 */             if (values1.get(i) != null) {
/* 2670:3027 */               ps.setBytes(i + 1, values1.get(i).toString().getBytes("utf-8"));
/* 2671:     */             } else {
/* 2672:3031 */               ps.setString(i + 1, null);
/* 2673:     */             }
/* 2674:     */           }
/* 2675:3036 */           int updates = ps.executeUpdate();
/* 2676:     */           
/* 2677:3038 */           System.out.println("updates " + updates);
/* 2678:     */         }
/* 2679:     */         else
/* 2680:     */         {
/* 2681:3042 */           System.out.println(" query " + qry);
/* 2682:     */           
/* 2683:3044 */           PreparedStatement ps = dt.getTable().getPreparedStatement(qry + " where " + where);
/* 2684:3046 */           for (int i = 0; i < values1.size(); i++)
/* 2685:     */           {
/* 2686:3048 */             System.out.println("Value of " + i + " is " + values1.get(i));
/* 2687:3049 */             if (values1.get(i) != null) {
/* 2688:3050 */               ps.setBytes(i + 1, values1.get(i).toString().getBytes());
/* 2689:     */             } else {
/* 2690:3052 */               ps.setString(i + 1, null);
/* 2691:     */             }
/* 2692:     */           }
/* 2693:3054 */           int updates = ps.executeUpdate();
/* 2694:     */           
/* 2695:3056 */           System.out.println("updates " + updates);
/* 2696:     */         }
/* 2697:     */       }
/* 2698:     */     }
/* 2699:     */     else
/* 2700:     */     {
/* 2701:3063 */       String s = "insert into " + table;
/* 2702:3064 */       String flds = "";
/* 2703:3065 */       String values = "";
/* 2704:3066 */       int count = 0;
/* 2705:3067 */       values1 = new Vector();
/* 2706:3068 */       Vector colTypes = new Vector();
/* 2707:3070 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2708:     */       {
/* 2709:3072 */         String fld = dt.getAttribName(j);
/* 2710:3073 */         String fld1 = fld;
/* 2711:3074 */         if (fld.equals("id")) {
/* 2712:3075 */           fld1 = "_id";
/* 2713:     */         }
/* 2714:3076 */         String value = dataM.getAttribute(fld1.toLowerCase());
/* 2715:3077 */         int colType = dt.getMetaData().getColumnType(j + 1);
/* 2716:     */         
/* 2717:3079 */         String def = colType == 4 ? "0" : null;
/* 2718:3080 */         colTypes.add(dt.getMetaData().getColumnTypeName(j + 1));
/* 2719:3081 */         if ((!table.equals("tasks")) || (!fld.toLowerCase().equals("id")))
/* 2720:     */         {
/* 2721:3083 */           System.out.println("Value is " + value);
/* 2722:     */           
/* 2723:3085 */           value = (value == null) || (value.equals("")) ? def : value;
/* 2724:3086 */           values1.add(value);
/* 2725:3087 */           flds = flds + (count != 0 ? "," : "") + fld;
/* 2726:3088 */           values = values + (count != 0 ? "," : "") + "?";
/* 2727:3089 */           count++;
/* 2728:     */         }
/* 2729:     */       }
/* 2730:3092 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 2731:     */       
/* 2732:3094 */       System.out.println(" Debug sql " + s);
/* 2733:3095 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2734:3097 */       for (int i = 0; i < values1.size(); i++) {
/* 2735:3099 */         if (values1.get(i) != null)
/* 2736:     */         {
/* 2737:3101 */           System.out.println(" Coltypes " + i + " " + colTypes.get(i));
/* 2738:3102 */           ps.setBytes(i + 1, values1.get(i).toString().getBytes());
/* 2739:     */         }
/* 2740:     */         else
/* 2741:     */         {
/* 2742:3105 */           ps.setString(i + 1, null);
/* 2743:     */         }
/* 2744:     */       }
/* 2745:3108 */       ps.execute();
/* 2746:     */     }
/* 2747:3111 */     if (table.equals("tasks"))
/* 2748:     */     {
/* 2749:3113 */       String status = dataM.getAttribute("status");
/* 2750:     */       
/* 2751:3115 */       String task = dataM.getAttribute("task");
/* 2752:3116 */       if (task == null)
/* 2753:     */       {
/* 2754:3118 */         task = (String)((XModel)oldData.get("task")).get();
/* 2755:3119 */         System.out.println("Old task " + task);
/* 2756:     */       }
/* 2757:3121 */       String pid = dataM.getAttribute("member");
/* 2758:3122 */       if (pid == null)
/* 2759:     */       {
/* 2760:3124 */         pid = (String)((XModel)oldData.get("member")).get();
/* 2761:3125 */         System.out.println("Old pid " + pid);
/* 2762:     */       }
/* 2763:3127 */       String type = dataM.getAttribute("survey_type");
/* 2764:3128 */       if (type == null)
/* 2765:     */       {
/* 2766:3130 */         type = (String)((XModel)oldData.get("survey_type")).get();
/* 2767:3131 */         System.out.println("Old type " + type);
/* 2768:     */       }
/* 2769:3133 */       String endtime = dataM.getAttribute("endtime");
/* 2770:3134 */       if (((status != null) && ((status.equals("2")) || (status.equals("5")))) || ((endtime != null) && (endtime.length() > 0))) {
/* 2771:3136 */         if ((type != null) && (type.equals("6"))) {
/* 2772:3138 */           Process.taskStatusUpdate(pid, task, status);
/* 2773:     */         }
/* 2774:     */       }
/* 2775:     */     }
/* 2776:     */   }
/* 2777:     */   
/* 2778:     */   public void deleteResources(String path)
/* 2779:     */     throws Exception
/* 2780:     */   {
/* 2781:3147 */     File dir = new File(path);
/* 2782:3148 */     if (dir.isDirectory())
/* 2783:     */     {
/* 2784:3150 */       String[] files = dir.list();
/* 2785:3151 */       for (int i = 0; i < files.length; i++) {
/* 2786:3153 */         deleteResource(files[i], "");
/* 2787:     */       }
/* 2788:     */     }
/* 2789:     */   }
/* 2790:     */   
/* 2791:     */   public void deleteAllData()
/* 2792:     */     throws Exception
/* 2793:     */   {
/* 2794:3160 */     deleteData1("tasks", "", null, null);
/* 2795:3161 */     deleteData1("keyvalue", "", null, null);
/* 2796:3162 */     deleteData1("changelogs", "", null, null);
/* 2797:     */   }
/* 2798:     */   
/* 2799:     */   public void deleteResource(String resource, String user1)
/* 2800:     */     throws Exception
/* 2801:     */   {
/* 2802:     */     try
/* 2803:     */     {
/* 2804:3169 */       File file1 = new File(resource);
/* 2805:3170 */       Logger logger = Logger.getLogger(getClass());
/* 2806:3171 */       logger.info("Deleting resource" + resource);
/* 2807:3172 */       if (file1.exists())
/* 2808:     */       {
/* 2809:3174 */         logger.info("Resource exists " + resource);
/* 2810:3175 */         if (file1.delete()) {
/* 2811:3176 */           logger.info("Deleted");
/* 2812:     */         } else {
/* 2813:3178 */           logger.info("Not Deleted");
/* 2814:     */         }
/* 2815:     */       }
/* 2816:     */     }
/* 2817:     */     catch (Exception e)
/* 2818:     */     {
/* 2819:3184 */       e.printStackTrace();
/* 2820:     */     }
/* 2821:     */   }
/* 2822:     */   
/* 2823:     */   public void deleteData(String table, String where, XmlElement dataM, String user1)
/* 2824:     */     throws Exception
/* 2825:     */   {
/* 2826:3191 */     XModel conflictData = new XBaseModel();
/* 2827:3192 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 2828:3193 */     conflictData.set("date", "'" + sdf.format(new Date()) + "'");
/* 2829:3194 */     conflictData.set("surveyor", "'" + user1 + "'");
/* 2830:3195 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2831:3196 */     System.out.println(" Where is " + where);
/* 2832:3197 */     dt.setupTable(table, "*", where, "test", true);
/* 2833:3198 */     Vector values1 = new Vector();
/* 2834:     */     
/* 2835:3200 */     String qry = "delete " + table + " where  " + where;
/* 2836:3201 */     PreparedStatement ps = dt.getTable().getPreparedStatement(qry);
/* 2837:     */     try
/* 2838:     */     {
/* 2839:3203 */       ChangeLog.startLog(table, "delete", where, getCurrentUser());
/* 2840:     */       
/* 2841:3205 */       int updates = ps.executeUpdate();
/* 2842:     */       
/* 2843:3207 */       System.out.println("updates " + updates);
/* 2844:     */     }
/* 2845:     */     catch (Exception e)
/* 2846:     */     {
/* 2847:3211 */       e.printStackTrace();
/* 2848:     */     }
/* 2849:3213 */     ChangeLog.endLog();
/* 2850:     */   }
/* 2851:     */   
/* 2852:     */   public void deleteData1(String table, String where, XmlElement dataM, String user1)
/* 2853:     */     throws Exception
/* 2854:     */   {
/* 2855:3219 */     XModel conflictData = new XBaseModel();
/* 2856:3220 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 2857:3221 */     conflictData.set("date", "'" + sdf.format(new Date()) + "'");
/* 2858:3222 */     conflictData.set("surveyor", "'" + user1 + "'");
/* 2859:3223 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2860:3224 */     System.out.println(" Where is " + where);
/* 2861:3225 */     dt.setupTable(table, "*", where, "test", true);
/* 2862:3226 */     Vector values1 = new Vector();
/* 2863:     */     
/* 2864:3228 */     String qry = "delete from " + table + " where  " + where;
/* 2865:3229 */     PreparedStatement ps = dt.getTable().getPreparedStatement(qry);
/* 2866:     */     try
/* 2867:     */     {
/* 2868:3232 */       int updates = ps.executeUpdate();
/* 2869:     */       
/* 2870:3234 */       System.out.println("updates " + updates);
/* 2871:     */     }
/* 2872:     */     catch (Exception e)
/* 2873:     */     {
/* 2874:3238 */       e.printStackTrace();
/* 2875:     */     }
/* 2876:     */   }
/* 2877:     */   
/* 2878:     */   public String getTaskContext(String task, String area, String house, String hh, String idc)
/* 2879:     */   {
/* 2880:3244 */     String context = "";
/* 2881:3245 */     if (task != null)
/* 2882:     */     {
/* 2883:3247 */       context = context + " task = '" + task + "'";
/* 2884:3248 */       if (area != null)
/* 2885:     */       {
/* 2886:3250 */         context = context + "and area = '" + area + "'";
/* 2887:3252 */         if (house != null)
/* 2888:     */         {
/* 2889:3254 */           context = context + "and house = '" + house + "'";
/* 2890:3256 */           if (hh != null)
/* 2891:     */           {
/* 2892:3258 */             context = context + "and household = '" + hh + "'";
/* 2893:3260 */             if (idc != null) {
/* 2894:3262 */               context = context + "and member = '" + idc + "'";
/* 2895:     */             }
/* 2896:     */           }
/* 2897:     */         }
/* 2898:     */       }
/* 2899:     */     }
/* 2900:3271 */     return context;
/* 2901:     */   }
/* 2902:     */   
/* 2903:     */   public String getTaskContext(String task, String area, String house, String hh, String idc, String assignedTo)
/* 2904:     */   {
/* 2905:3275 */     String context = "";
/* 2906:3277 */     if (task != null)
/* 2907:     */     {
/* 2908:3279 */       context = context + " task = '" + task + "' ";
/* 2909:3280 */       if (area != null)
/* 2910:     */       {
/* 2911:3282 */         context = context + "and area = '" + area + "'";
/* 2912:3284 */         if (house != null)
/* 2913:     */         {
/* 2914:3286 */           context = context + "and house = '" + house + "'";
/* 2915:3288 */           if (hh != null)
/* 2916:     */           {
/* 2917:3290 */             context = context + "and household = '" + hh + "'";
/* 2918:3292 */             if (idc != null) {
/* 2919:3294 */               context = context + "and member = '" + idc + "'";
/* 2920:     */             }
/* 2921:     */           }
/* 2922:     */         }
/* 2923:     */       }
/* 2924:3303 */       if (assignedTo != null) {
/* 2925:3305 */         context = context + "and assignedTo = '" + assignedTo + "'";
/* 2926:     */       }
/* 2927:     */     }
/* 2928:3310 */     return context;
/* 2929:     */   }
/* 2930:     */   
/* 2931:     */   public void saveTask(XTaskModel taskM)
/* 2932:     */     throws Exception
/* 2933:     */   {
/* 2934:3315 */     saveTask(taskM.task, taskM.surveyType, taskM.area, taskM.house, taskM.household, taskM.member, taskM);
/* 2935:     */   }
/* 2936:     */   
/* 2937:     */   public void saveTask(String task, String surveyType, String area, String house, String hh, String idc, XModel dataM)
/* 2938:     */     throws Exception
/* 2939:     */   {
/* 2940:3320 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2941:3321 */     String table = "tasks";
/* 2942:3322 */     String assignedTo = (String)dataM.get("@assignedto");
/* 2943:3324 */     if (task.equals("taskdefinitions/healthcheckup_taskdefinition/task0")) {
/* 2944:3325 */       return;
/* 2945:     */     }
/* 2946:3326 */     String where = getTaskContext(task, area, house, hh, idc, assignedTo);
/* 2947:3327 */     ChangeLog.startLog(table, where, getCurrentUser());
/* 2948:3328 */     dt.setupTable(table, "*", where, "test", true);
/* 2949:3329 */     dt.retrieve();
/* 2950:3330 */     dataM.set("@task", task);
/* 2951:3331 */     dataM.set("@area", area);
/* 2952:3332 */     dataM.set("@house", house);
/* 2953:3333 */     dataM.set("@household", hh);
/* 2954:3334 */     dataM.set("@member", idc);
/* 2955:3335 */     dataM.set("@survey_type", surveyType);
/* 2956:3336 */     dataM.set("@status", dataM.get());
/* 2957:     */     
/* 2958:3338 */     System.out.println(" TASK " + dataM.get("@task"));
/* 2959:3340 */     if (dt.getNumChildren() > 0)
/* 2960:     */     {
/* 2961:3342 */       System.out.println(" total " + dt.getNumChildren());
/* 2962:     */       
/* 2963:3344 */       String qry = "update " + table + " set ";
/* 2964:3345 */       int count = 0;
/* 2965:3346 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2966:     */       {
/* 2967:3348 */         String fld = dt.getAttribName(j);
/* 2968:3349 */         System.out.println(" DEBUG column " + dt.getMetaData().getColumnType(j + 1) + " " + fld);
/* 2969:3350 */         if (!fld.equals("id"))
/* 2970:     */         {
/* 2971:3352 */           int colType = dt.getMetaData().getColumnType(j + 1);
/* 2972:3353 */           String def = colType == 4 ? "0" : null;
/* 2973:3354 */           String value = (String)dataM.get("@" + fld);
/* 2974:     */           
/* 2975:3356 */           value = "'" + value + "'";
/* 2976:3357 */           qry = qry + (count == 0 ? "" : ",") + fld + "=" + value;
/* 2977:3358 */           ChangeLog.logField(fld, value);
/* 2978:3359 */           count++;
/* 2979:     */         }
/* 2980:     */       }
/* 2981:3362 */       int updates = dt.executeUpdate(qry + " where " + where);
/* 2982:3363 */       System.out.println("updates " + updates);
/* 2983:     */       
/* 2984:3365 */       System.out.println(" Debug update sql " + qry);
/* 2985:     */     }
/* 2986:     */     else
/* 2987:     */     {
/* 2988:3369 */       String s = "insert into " + table;
/* 2989:3370 */       String flds = "";
/* 2990:3371 */       String values = "";
/* 2991:3372 */       int count = 0;
/* 2992:3373 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2993:     */       {
/* 2994:3375 */         String fld = dt.getAttribName(j);
/* 2995:3376 */         if (!fld.equals("id"))
/* 2996:     */         {
/* 2997:3378 */           System.out.println(" DEBUG column " + dt.getMetaData().getColumnType(j + 1) + " " + fld);
/* 2998:3379 */           int colType = dt.getMetaData().getColumnType(j + 1);
/* 2999:3380 */           String def = colType == 4 ? "0" : null;
/* 3000:3381 */           String value = (String)dataM.get("@" + fld);
/* 3001:3382 */           flds = flds + (count != 0 ? "," : "") + fld;
/* 3002:3383 */           value = "'" + value + "'";
/* 3003:3384 */           values = values + (count != 0 ? "," : "") + value;
/* 3004:3385 */           ChangeLog.logField(fld, value);
/* 3005:3386 */           count++;
/* 3006:     */         }
/* 3007:     */       }
/* 3008:3389 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 3009:     */       
/* 3010:3391 */       System.out.println(" Debug sql " + s);
/* 3011:     */       
/* 3012:3393 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 3013:3394 */       ps.execute();
/* 3014:3395 */       ps.close();
/* 3015:3396 */       dt.getTable().releasePreparedStatement(ps);
/* 3016:     */     }
/* 3017:3399 */     ChangeLog.endLog();
/* 3018:     */   }
/* 3019:     */   
/* 3020:     */   public XModel getHouseholds(String area, String house, XModel houseM)
/* 3021:     */     throws Exception
/* 3022:     */   {
/* 3023:3405 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3024:3406 */     dt.setupTable("households", "household", "enum_area=" + area + " and house=" + house, "test", false);
/* 3025:3407 */     dt.retrieve();
/* 3026:     */     
/* 3027:3409 */     System.out.println(dt.getNumChildren());
/* 3028:3410 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 3029:     */     {
/* 3030:3412 */       String id = dt.get(i).get("household").toString();
/* 3031:3413 */       XModel tt = (XModel)houseM.get(id);
/* 3032:     */       
/* 3033:3415 */       tt.setId(dt.get(i).get("household").toString());
/* 3034:3416 */       XModel xm = getHouseholddetails(id, house, area);
/* 3035:3417 */       xm.append(getDataM("data", "visitHistory", area, house, id, null));
/* 3036:     */       
/* 3037:3419 */       tt = getIndividuals(area, house, tt.getId(), tt);
/* 3038:3420 */       tt.append(xm);
/* 3039:     */       
/* 3040:3422 */       tt.append(getDataM("data", "characteristics", area, house, id, null));
/* 3041:     */     }
/* 3042:3425 */     return houseM;
/* 3043:     */   }
/* 3044:     */   
/* 3045:     */   public XModel getHouseholddetails(String household, String house, String area, XModel xm)
/* 3046:     */   {
/* 3047:3430 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3048:3431 */     String sql = "select a.*,a.head headcode,b.name  headname from households a left join members b on a.enum_area=b.enum_area and a.house=b.house and a.household=b.household and a.head=b.idc";
/* 3049:     */     
/* 3050:3433 */     dt.setSqlStatement(sql + " where a.enum_area='" + area + "' and a.house='" + house + "' and a.household='" + household + "'", "test", true);
/* 3051:3434 */     dt.setName("updatestatus");
/* 3052:3435 */     dt.setId("updatestatus");
/* 3053:3436 */     dt.setTagName("data");
/* 3054:3437 */     dt.retrieve();
/* 3055:3439 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 3056:3441 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 3057:     */       {
/* 3058:3443 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 3059:3444 */         tt.set(dt.get(i).get(j).get());
/* 3060:     */       }
/* 3061:     */     }
/* 3062:3449 */     return xm;
/* 3063:     */   }
/* 3064:     */   
/* 3065:     */   public XModel getHouseholddetails(String household, String house, String area)
/* 3066:     */   {
/* 3067:3454 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3068:3455 */     String sql = "select a.*,a.head headcode,b.name  headname from households a left join members b on a.enum_area=b.enum_area and a.house=b.house and a.household=b.household and a.head=b.idc";
/* 3069:     */     
/* 3070:3457 */     dt.setSqlStatement(sql + " where a.enum_area='" + area + "' and a.house='" + house + "' and a.household='" + household + "'", "test", true);
/* 3071:3458 */     dt.setName("updatestatus");
/* 3072:3459 */     dt.setId("updatestatus");
/* 3073:3460 */     dt.setTagName("data");
/* 3074:3461 */     dt.retrieve();
/* 3075:3462 */     XBaseModel xm = new XBaseModel();
/* 3076:3463 */     xm.setId("updatestatus");
/* 3077:3465 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 3078:3467 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 3079:     */       {
/* 3080:3469 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 3081:3470 */         tt.set(dt.get(i).get(j).get());
/* 3082:     */       }
/* 3083:     */     }
/* 3084:3475 */     return xm;
/* 3085:     */   }
/* 3086:     */   
/* 3087:     */   public String getNextContextType(XModel context)
/* 3088:     */     throws Exception
/* 3089:     */   {
/* 3090:3480 */     XModel areaM = (XModel)context.get("area");
/* 3091:3481 */     XModel houseM = (XModel)context.get("house");
/* 3092:3482 */     XModel householdM = (XModel)context.get("household");
/* 3093:3483 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3094:3484 */     String id = "";
/* 3095:3486 */     if ((householdM != null) && (householdM.get() != null) && (!householdM.get().equals(""))) {
/* 3096:3488 */       id = "individual";
/* 3097:3490 */     } else if ((houseM != null) && (houseM.get() != null) && (!houseM.equals(""))) {
/* 3098:3492 */       id = "household";
/* 3099:3494 */     } else if ((areaM != null) && (areaM.get() != null) && (!areaM.get().equals(""))) {
/* 3100:3496 */       id = "house";
/* 3101:     */     } else {
/* 3102:3500 */       id = "area";
/* 3103:     */     }
/* 3104:3501 */     return id;
/* 3105:     */   }
/* 3106:     */   
/* 3107:     */   public void applyAutoUpdate(XModel context, String contextType)
/* 3108:     */     throws Exception
/* 3109:     */   {
/* 3110:3507 */     XModel areaM = (XModel)context.get("area");
/* 3111:3508 */     XModel houseM = (XModel)context.get("house");
/* 3112:3509 */     XModel householdM = (XModel)context.get("household");
/* 3113:3510 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3114:3511 */     XModel details = new XBaseModel();
/* 3115:     */     
/* 3116:3513 */     XModel indivM = (XModel)context.get("individual");
/* 3117:3514 */     XModel visitM = (XModel)context.get("visit");
/* 3118:3515 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
/* 3119:3516 */     if (contextType.equals("visit"))
/* 3120:     */     {
/* 3121:3518 */       String fields = "max(visitid)";
/* 3122:3519 */       XModel dataM = new XBaseModel();
/* 3123:3520 */       getData("visit", fields, "household='" + householdM.get() + "' and teamid=" + surveyorM.get(), dataM);
/* 3124:3521 */       if (dataM.getNumChildren() > 0)
/* 3125:     */       {
/* 3126:3523 */         System.out.println("Max " + dataM.get(0).get(0).getId() + " ");
/* 3127:3524 */         XModel maxTime = dataM.get(0).get(0);
/* 3128:3525 */         System.out.println("Max " + maxTime.get() + " ");
/* 3129:3527 */         if (maxTime.get() == null)
/* 3130:     */         {
/* 3131:3529 */           String visitTime = sdf.format(new Date());
/* 3132:3530 */           XModel xm = new XBaseModel();
/* 3133:3531 */           ((XModel)xm.get("visittime")).set(visitTime);
/* 3134:3532 */           ((XModel)xm.get("teamid")).set(surveyorM.get());
/* 3135:3533 */           ((XModel)context.get("visit")).set(visitTime);
/* 3136:3534 */           saveEnumData(context, contextType, xm);
/* 3137:     */         }
/* 3138:     */         else
/* 3139:     */         {
/* 3140:3538 */           String lastVisitTime = maxTime.get().toString();
/* 3141:3539 */           Date l = sdf.parse(lastVisitTime);
/* 3142:3540 */           Calendar cal = Calendar.getInstance();
/* 3143:3541 */           cal.setTime(l);
/* 3144:3542 */           cal.add(10, 4);
/* 3145:3543 */           System.out.println(" Next visit time" + sdf.format(cal.getTime()));
/* 3146:3544 */           System.out.println(" Current time " + sdf.format(new Date()));
/* 3147:3545 */           System.out.println(" Check  " + cal.getTime().after(new Date()) + " " + cal.getTime().equals(new Date()));
/* 3148:3546 */           Calendar cal2 = Calendar.getInstance();
/* 3149:3547 */           System.out.println(" Check2  " + cal.compareTo(cal2));
/* 3150:3548 */           if (!cal.getTime().after(new Date()))
/* 3151:     */           {
/* 3152:3550 */             String visitTime = sdf.format(new Date());
/* 3153:3551 */             XModel xm = new XBaseModel();
/* 3154:3552 */             ((XModel)xm.get("visittime")).set(visitTime);
/* 3155:3553 */             ((XModel)xm.get("teamid")).set(surveyorM.get());
/* 3156:3554 */             ((XModel)context.get("visit")).set(visitTime);
/* 3157:3555 */             saveEnumData(context, contextType, xm);
/* 3158:     */           }
/* 3159:     */         }
/* 3160:     */       }
/* 3161:     */       else
/* 3162:     */       {
/* 3163:3561 */         String visitTime = sdf.format(new Date());
/* 3164:3562 */         XModel xm = new XBaseModel();
/* 3165:3563 */         ((XModel)xm.get("visittime")).set(visitTime);
/* 3166:3564 */         ((XModel)xm.get("visitid")).set(visitTime);
/* 3167:3565 */         ((XModel)xm.get("teamid")).set(surveyorM.get());
/* 3168:3566 */         ((XModel)context.get("visit")).set(visitTime);
/* 3169:3567 */         System.out.println(" New visit");
/* 3170:3568 */         saveEnumData(context, contextType, xm);
/* 3171:     */       }
/* 3172:     */     }
/* 3173:     */   }
/* 3174:     */   
/* 3175:     */   public XModel getCMEData(XModel context, String contextType, String fields)
/* 3176:     */     throws Exception
/* 3177:     */   {
/* 3178:3576 */     XModel areaM = (XModel)context.get("area");
/* 3179:3577 */     XModel houseM = (XModel)context.get("house");
/* 3180:3578 */     XModel householdM = (XModel)context.get("household");
/* 3181:3579 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3182:3580 */     XModel details = new XBaseModel();
/* 3183:     */     
/* 3184:3582 */     XModel indivM = (XModel)context.get("cme");
/* 3185:3583 */     XModel visitM = (XModel)context.get("visit");
/* 3186:3584 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3187:     */     
/* 3188:3586 */     String where = "";
/* 3189:3587 */     String table = "";
/* 3190:3588 */     String idField = "";
/* 3191:3589 */     String id = "";
/* 3192:3590 */     String path = "/cme/" + indivM.get() + "/";
/* 3193:3591 */     StringTokenizer st = new StringTokenizer(fields, ",");
/* 3194:3592 */     XModel dataM = new XBaseModel();
/* 3195:3593 */     XModel dataM1 = (XModel)dataM.get("test");
/* 3196:3594 */     while (st.hasMoreTokens())
/* 3197:     */     {
/* 3198:3596 */       String key = st.nextToken();
/* 3199:3597 */       XModel xm = (XModel)dataM1.get(key);
/* 3200:3598 */       String value = TestXUIDB.getInstance().getValue("keyvalue", path + key);
/* 3201:3599 */       xm.set(value);
/* 3202:     */     }
/* 3203:3601 */     return dataM;
/* 3204:     */   }
/* 3205:     */   
/* 3206:     */   public XModel getVAData(XModel context, String contextType, String fields)
/* 3207:     */     throws Exception
/* 3208:     */   {
/* 3209:3607 */     XModel areaM = (XModel)context.get("area");
/* 3210:3608 */     XModel houseM = (XModel)context.get("house");
/* 3211:3609 */     XModel householdM = (XModel)context.get("household");
/* 3212:3610 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3213:3611 */     XModel details = new XBaseModel();
/* 3214:     */     
/* 3215:3613 */     XModel indivM = (XModel)context.get("va");
/* 3216:3614 */     XModel visitM = (XModel)context.get("visit");
/* 3217:3615 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3218:     */     
/* 3219:3617 */     String where = "";
/* 3220:3618 */     String table = "";
/* 3221:3619 */     String idField = "";
/* 3222:3620 */     String id = "";
/* 3223:3621 */     String path = "/va/" + areaM.get() + indivM.get() + "/" + surveyorM.get() + "/";
/* 3224:3622 */     StringTokenizer st = new StringTokenizer(fields, ",");
/* 3225:3623 */     XModel dataM = new XBaseModel();
/* 3226:3624 */     XModel dataM1 = (XModel)dataM.get("test");
/* 3227:3625 */     while (st.hasMoreTokens())
/* 3228:     */     {
/* 3229:3627 */       String key = st.nextToken();
/* 3230:3628 */       XModel xm = (XModel)dataM1.get(key);
/* 3231:3629 */       String value = TestXUIDB.getInstance().getValue("keyvalue", path + key);
/* 3232:3630 */       xm.set(value);
/* 3233:     */     }
/* 3234:3632 */     return dataM;
/* 3235:     */   }
/* 3236:     */   
/* 3237:     */   public void saveFlow(String id, String flow, String context, String surveyor, String status, String startTime, String endTime)
/* 3238:     */     throws Exception
/* 3239:     */   {
/* 3240:3638 */     System.out.println(" Surveyor " + surveyor);
/* 3241:3639 */     DatabaseTableModel dtm = new DatabaseTableModel();
/* 3242:3640 */     dtm.setupTable("flow", "*", "", "test", false);
/* 3243:     */     
/* 3244:3642 */     ChangeLog.startLog("flow", "id1='" + id + "'", getCurrentUser());
/* 3245:3643 */     PreparedStatement ps = dtm.getTable().getPreparedStatement(" insert into flow  values(?,?,?,?,?,?,?)");
/* 3246:3644 */     ChangeLog.logField("id1", id);
/* 3247:3645 */     ChangeLog.logField("flow", flow);
/* 3248:     */     
/* 3249:3647 */     ps.setString(1, id);
/* 3250:3648 */     ps.setString(2, flow);
/* 3251:3649 */     ps.setString(3, context);
/* 3252:3650 */     ChangeLog.logField("context", context);
/* 3253:3651 */     ps.setString(4, status);
/* 3254:3652 */     ChangeLog.logField("status", status);
/* 3255:3653 */     ps.setString(5, startTime);
/* 3256:3654 */     ChangeLog.logField("starttime", startTime);
/* 3257:3655 */     ps.setString(6, endTime);
/* 3258:3656 */     ChangeLog.logField("endtime", endTime);
/* 3259:3657 */     ps.setString(7, surveyor);
/* 3260:3658 */     ChangeLog.logField("user", surveyor);
/* 3261:     */     
/* 3262:3660 */     System.out.println(" Executing ..");
/* 3263:3661 */     ps.execute();
/* 3264:3662 */     ChangeLog.endLog();
/* 3265:     */   }
/* 3266:     */   
/* 3267:     */   public void saveFlowQuestion(String flowid, String flow, String surveyor, String qno, String startTime, String endTime)
/* 3268:     */     throws Exception
/* 3269:     */   {
/* 3270:3668 */     System.out.println(" Surveyor " + surveyor);
/* 3271:3669 */     DatabaseTableModel dtm = new DatabaseTableModel();
/* 3272:3670 */     dtm.setupTable("flow_question", "*", "", "test", false);
/* 3273:3671 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 3274:     */     
/* 3275:3673 */     ChangeLog.startLog("flow_question", "flow_id='" + flowid + "'  and qno='" + qno + "'", getCurrentUser());
/* 3276:3674 */     PreparedStatement ps = dtm.getTable().getPreparedStatement(" insert into flow_question  values(?,?,?,?,?,?)");
/* 3277:3675 */     ChangeLog.logField("flow_id", flowid);
/* 3278:3676 */     ChangeLog.logField("flow", flow);
/* 3279:3677 */     ChangeLog.logField("qno", qno);
/* 3280:     */     
/* 3281:3679 */     ps.setString(1, flowid);
/* 3282:3680 */     ps.setString(2, flow);
/* 3283:3681 */     ps.setString(3, qno);
/* 3284:     */     
/* 3285:3683 */     ps.setString(4, startTime);
/* 3286:3684 */     ChangeLog.logField("starttime", startTime);
/* 3287:3685 */     ps.setString(5, endTime);
/* 3288:3686 */     ChangeLog.logField("endtime", endTime);
/* 3289:3687 */     ps.setString(6, surveyor);
/* 3290:3688 */     ChangeLog.logField("user", surveyor);
/* 3291:     */     
/* 3292:3690 */     System.out.println(" Executing fq ..");
/* 3293:3691 */     ps.execute();
/* 3294:3692 */     ChangeLog.endLog();
/* 3295:     */   }
/* 3296:     */   
/* 3297:     */   public XModel getFlowParameters(XModel context, String contextType)
/* 3298:     */     throws Exception
/* 3299:     */   {
/* 3300:3698 */     System.out.println("Flow context type " + contextType);
/* 3301:3699 */     XModel surveyM = (XModel)context.get("survey");
/* 3302:3700 */     XModel areaM = (XModel)context.get("area");
/* 3303:3701 */     XModel houseM = (XModel)context.get("house");
/* 3304:3702 */     XModel householdM = (XModel)context.get("household");
/* 3305:3703 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3306:3704 */     XModel details = new XBaseModel();
/* 3307:     */     
/* 3308:3706 */     XModel indivM = (XModel)context.get("member");
/* 3309:3707 */     XModel visitM = (XModel)context.get("visit");
/* 3310:3708 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3311:3709 */     XModel params = new XBaseModel();
/* 3312:3710 */     XModel newContext = new XBaseModel();
/* 3313:3711 */     for (int i = 0; i < context.getNumChildren(); i++) {
/* 3314:3713 */       if (!context.get(i).getId().equals(contextType)) {
/* 3315:3715 */         newContext.append(context.get(i));
/* 3316:     */       }
/* 3317:     */     }
/* 3318:3718 */     if (contextType.equals("va"))
/* 3319:     */     {
/* 3320:3720 */       ((XModel)newContext.get("member")).set(((XModel)context.get("va")).get());
/* 3321:3721 */       XModel values = getEnumData(newContext, "member", "sex,deathage,deathageunit");
/* 3322:3722 */       ((XModel)params.get("deceased_age")).set("@type", "age");
/* 3323:3723 */       System.out.println(" Flow has " + values.get(0).getNumChildren());
/* 3324:3724 */       String sex = ((XModel)values.get(0).get("sex")).get().toString();
/* 3325:3725 */       String age = ((XModel)values.get(0).get("deathage")).get().toString();
/* 3326:3726 */       String ageUnit = ((XModel)values.get(0).get("deathageunit")).get().toString();
/* 3327:3727 */       ((XModel)params.get("deceased_age")).set(age + "," + ageUnit);
/* 3328:3728 */       ((XModel)params.get("deceased_sex")).set(sex);
/* 3329:3729 */       ((XModel)params.get("deceased_sex")).set("@type", "text");
/* 3330:     */     }
/* 3331:3732 */     return params;
/* 3332:     */   }
/* 3333:     */   
/* 3334:     */   public XModel getEnumData(XModel context, String contextType, String fields)
/* 3335:     */     throws Exception
/* 3336:     */   {
/* 3337:3737 */     XModel surveyM = (XModel)context.get("survey");
/* 3338:3738 */     XModel areaM = (XModel)context.get("area");
/* 3339:3739 */     XModel houseM = (XModel)context.get("house");
/* 3340:3740 */     XModel householdM = (XModel)context.get("household");
/* 3341:3741 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3342:3742 */     XModel details = new XBaseModel();
/* 3343:     */     
/* 3344:3744 */     XModel indivM = (XModel)context.get("member");
/* 3345:3745 */     XModel visitM = (XModel)context.get("visit");
/* 3346:3746 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3347:     */     
/* 3348:3748 */     String where = "";
/* 3349:3749 */     String table = "";
/* 3350:3750 */     String idField = "";
/* 3351:3751 */     String id = "";
/* 3352:3752 */     if (surveyM.get().equals("istp"))
/* 3353:     */     {
/* 3354:3754 */       XModel clinicM = (XModel)context.get("clinic");
/* 3355:     */       
/* 3356:3756 */       where = "ictc_code='" + clinicM.get() + "'";
/* 3357:3757 */       table = "clinic";
/* 3358:3758 */       idField = "ictc_code";
/* 3359:3759 */       XModel stdM = (XModel)context.get("std");
/* 3360:3760 */       if ((contextType.equals("std")) && (stdM != null) && (stdM.get() != null) && (!stdM.equals("")))
/* 3361:     */       {
/* 3362:3762 */         where = "uniqno='" + stdM.get() + "'";
/* 3363:3763 */         table = "patients";
/* 3364:3764 */         idField = "uniqno";
/* 3365:     */       }
/* 3366:     */     }
/* 3367:     */     else
/* 3368:     */     {
/* 3369:3769 */       if (contextType.equals("va")) {
/* 3370:3771 */         return getVAData(context, contextType, fields);
/* 3371:     */       }
/* 3372:3774 */       if ((contextType.equals("member")) && (indivM != null) && (indivM.get() != null) && (!indivM.equals("")))
/* 3373:     */       {
/* 3374:3776 */         where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "' and idc='" + indivM.get() + "'";
/* 3375:3777 */         table = "members";
/* 3376:3778 */         idField = "idc";
/* 3377:     */       }
/* 3378:3780 */       else if (((contextType.equals("births")) || (contextType.equals("marriages"))) && (contextTypeM != null))
/* 3379:     */       {
/* 3380:3782 */         where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "' and idc='" + contextTypeM.get() + "'";
/* 3381:3783 */         table = "births";
/* 3382:3784 */         if (contextType.equals("marriages")) {
/* 3383:3785 */           table = "marriages";
/* 3384:     */         }
/* 3385:3787 */         idField = "idc";
/* 3386:     */       }
/* 3387:3789 */       else if ((contextType.equals("marriages")) && (indivM != null) && (indivM.get() != null) && (!indivM.equals("")))
/* 3388:     */       {
/* 3389:3791 */         where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "' and idc='" + indivM.get() + "'";
/* 3390:3792 */         table = "marriages";
/* 3391:3793 */         idField = "idc";
/* 3392:     */       }
/* 3393:3795 */       else if ((contextType.equals("visit")) && (visitM != null) && (visitM.get() != null) && (!visitM.equals("")))
/* 3394:     */       {
/* 3395:3797 */         where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "'";
/* 3396:3798 */         table = "visit";
/* 3397:3799 */         idField = "visitid";
/* 3398:     */       }
/* 3399:3801 */       else if ((householdM != null) && (householdM.get() != null) && (!householdM.get().equals("")))
/* 3400:     */       {
/* 3401:3803 */         where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "'";
/* 3402:3804 */         table = "households";
/* 3403:3805 */         idField = "household";
/* 3404:3806 */         id = householdM.get().toString();
/* 3405:     */       }
/* 3406:3808 */       else if ((houseM != null) && (houseM.get() != null) && (!houseM.equals("")))
/* 3407:     */       {
/* 3408:3810 */         where = "enum_area='" + areaM.get() + "' and houseno='" + houseM.get() + "'";
/* 3409:     */         
/* 3410:3812 */         table = "houses";
/* 3411:     */         
/* 3412:3814 */         idField = "houseno";
/* 3413:3815 */         id = houseM.get().toString();
/* 3414:     */       }
/* 3415:3817 */       else if ((areaM != null) && (areaM.get() != null) && (!areaM.equals("")))
/* 3416:     */       {
/* 3417:3819 */         where = "id='" + areaM.get() + "' ";
/* 3418:3820 */         table = "hc_area";
/* 3419:3821 */         idField = "id";
/* 3420:     */       }
/* 3421:     */     }
/* 3422:3826 */     return getEnumData1(table, where, details, fields, idField);
/* 3423:     */   }
/* 3424:     */   
/* 3425:     */   private XModel getISTPData(XModel context, String contextType, String fields)
/* 3426:     */   {
/* 3427:3831 */     XModel areaM = (XModel)context.get("area");
/* 3428:3832 */     XModel houseM = (XModel)context.get("clinic");
/* 3429:     */     
/* 3430:3834 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3431:3835 */     XModel details = new XBaseModel();
/* 3432:     */     
/* 3433:3837 */     XModel indivM = (XModel)context.get("std");
/* 3434:3838 */     XModel visitM = (XModel)context.get("visit");
/* 3435:3839 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3436:     */     
/* 3437:3841 */     String where = "";
/* 3438:3842 */     String table = "";
/* 3439:3843 */     String idField = "";
/* 3440:3844 */     String id = "";
/* 3441:3845 */     String path = "/std/" + areaM.get() + indivM.get() + "/" + surveyorM.get() + "/";
/* 3442:3846 */     StringTokenizer st = new StringTokenizer(fields, ",");
/* 3443:3847 */     XModel dataM = new XBaseModel();
/* 3444:3848 */     XModel dataM1 = (XModel)dataM.get("test");
/* 3445:3849 */     while (st.hasMoreTokens())
/* 3446:     */     {
/* 3447:3851 */       String key = st.nextToken();
/* 3448:3852 */       XModel xm = (XModel)dataM1.get(key);
/* 3449:3853 */       String value = TestXUIDB.getInstance().getValue("keyvalue", path + key);
/* 3450:3854 */       xm.set(value);
/* 3451:     */     }
/* 3452:3856 */     return dataM;
/* 3453:     */   }
/* 3454:     */   
/* 3455:     */   public XModel getKeyValChildren(XModel context, String fields, String constraints, String contextType)
/* 3456:     */     throws Exception
/* 3457:     */   {
/* 3458:3862 */     String dom = "/" + contextType + "/";
/* 3459:3863 */     String key1 = "REPLACE(LEFT(key1,LOCATE ('/',key1," + (dom.length() + 1) + ")-1),'" + dom + "','')";
/* 3460:     */     
/* 3461:3865 */     String fld1 = "RIGHT(key1,INSTR(REVERSE(key1),'/')-1)";
/* 3462:     */     
/* 3463:3867 */     String where = constraints;
/* 3464:3868 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3465:     */     
/* 3466:3870 */     dt.setupTable("keyvalue", key1 + "," + fld1 + ",value1", where, "test", false);
/* 3467:     */     
/* 3468:3872 */     dt.retrieve();
/* 3469:3873 */     String keyFld = "key1";
/* 3470:3874 */     String valFld = "value1";
/* 3471:3875 */     System.out.println("---" + dt.getNumChildren());
/* 3472:3876 */     XModel xm = new XBaseModel();
/* 3473:3877 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 3474:     */     {
/* 3475:3879 */       XModel rowM = dt.get(i);
/* 3476:3880 */       String id = rowM.get(0).toString();
/* 3477:3881 */       String field = rowM.get(1).toString();
/* 3478:3882 */       String value = rowM.get(2).toString();
/* 3479:3883 */       XModel tt = (XModel)xm.get(id);
/* 3480:3884 */       XModel col = (XModel)tt.get(field);
/* 3481:3885 */       col.set(value);
/* 3482:     */     }
/* 3483:3888 */     return xm;
/* 3484:     */   }
/* 3485:     */   
/* 3486:     */   public XModel getISTPChildren(XModel context, String fields, String constraints, String contextType)
/* 3487:     */     throws Exception
/* 3488:     */   {
/* 3489:3892 */     return getKeyValChildren(context, fields, constraints, contextType);
/* 3490:     */   }
/* 3491:     */   
/* 3492:     */   public XModel getEnumDataChildren(XModel context, String fields, String constraints, String contextType)
/* 3493:     */     throws Exception
/* 3494:     */   {
/* 3495:3897 */     XModel surveyM = (XModel)context.get("survey");
/* 3496:3898 */     XModel areaM = (XModel)context.get("area");
/* 3497:3899 */     XModel houseM = (XModel)context.get("house");
/* 3498:     */     
/* 3499:3901 */     XModel householdM = (XModel)context.get("household");
/* 3500:3902 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3501:     */     
/* 3502:3904 */     XModel details = new XBaseModel();
/* 3503:     */     
/* 3504:3906 */     XModel indivM = (XModel)context.get("individual");
/* 3505:3907 */     XModel contextTypeM = (XModel)context.get("individual");
/* 3506:     */     
/* 3507:3909 */     String where = "";
/* 3508:3910 */     String table = "";
/* 3509:3911 */     String idField = "";
/* 3510:3912 */     String id = "";
/* 3511:3914 */     if (surveyM.get().equals("istp"))
/* 3512:     */     {
/* 3513:3916 */       XModel clinicM = (XModel)context.get("clinic");
/* 3514:3917 */       where = "";
/* 3515:3918 */       table = "clinic";
/* 3516:3919 */       idField = "ictc_code";
/* 3517:3920 */       if (contextType.equals("std"))
/* 3518:     */       {
/* 3519:3922 */         where = "ictc_code='" + clinicM.get() + "'";
/* 3520:3923 */         table = "patients";
/* 3521:3924 */         idField = "uniqno";
/* 3522:     */       }
/* 3523:     */     }
/* 3524:3928 */     else if ((householdM != null) && (householdM.get() != null) && (!householdM.equals("")))
/* 3525:     */     {
/* 3526:3930 */       where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "'";
/* 3527:3931 */       if (contextType.equals("va"))
/* 3528:     */       {
/* 3529:3933 */         table = "members";
/* 3530:3934 */         idField = "idc";
/* 3531:     */       }
/* 3532:3936 */       if (contextType.equals("member"))
/* 3533:     */       {
/* 3534:3938 */         table = "members";
/* 3535:3939 */         idField = "idc";
/* 3536:     */       }
/* 3537:3942 */       if (contextType.equals("livemember"))
/* 3538:     */       {
/* 3539:3944 */         table = "members";
/* 3540:3945 */         idField = "idc";
/* 3541:3946 */         constraints = "deathStatus is null";
/* 3542:     */       }
/* 3543:3949 */       if (contextType.equals("death"))
/* 3544:     */       {
/* 3545:3951 */         table = "members";
/* 3546:3952 */         idField = "idc";
/* 3547:3953 */         constraints = "deathStatus is not null";
/* 3548:     */       }
/* 3549:3956 */       if (contextType.equals("visit"))
/* 3550:     */       {
/* 3551:3958 */         table = "visit";
/* 3552:3959 */         idField = "visitid";
/* 3553:     */       }
/* 3554:3961 */       if (contextType.equals("births"))
/* 3555:     */       {
/* 3556:3963 */         table = "births";
/* 3557:3964 */         idField = "idc";
/* 3558:     */       }
/* 3559:3966 */       if (contextType.equals("marriages"))
/* 3560:     */       {
/* 3561:3968 */         table = "marriages";
/* 3562:3969 */         idField = "idc";
/* 3563:     */       }
/* 3564:     */     }
/* 3565:3973 */     else if ((houseM != null) && (houseM.get() != null) && (!houseM.equals("")))
/* 3566:     */     {
/* 3567:3975 */       where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "'";
/* 3568:3976 */       table = "households";
/* 3569:3977 */       idField = "households.household";
/* 3570:3978 */       if (surveyM.get().equals("VA")) {
/* 3571:3980 */         where = "households.enum_area='" + areaM.get() + "' and households.house='" + houseM.get() + "' and  num_of_deaths is not null and num_of_deaths > 0 ";
/* 3572:     */       }
/* 3573:     */     }
/* 3574:3984 */     else if ((areaM != null) && (areaM.get() != null) && (!areaM.equals("")))
/* 3575:     */     {
/* 3576:3986 */       where = "enum_area='" + areaM.get() + "' ";
/* 3577:3987 */       table = "houses";
/* 3578:3988 */       idField = "houseno";
/* 3579:3989 */       if (surveyM.get().equals("VA"))
/* 3580:     */       {
/* 3581:3991 */         fields = "houses.houseno id," + fields;
/* 3582:3992 */         where = "houses.enum_area='" + areaM.get() + "' and households.num_of_deaths is not null and households.num_of_deaths > 0  ";
/* 3583:     */         
/* 3584:3994 */         table = "houses left join households on houses.enum_area=households.enum_area and houses.houseno=households.house where " + where + " group by houses.enum_area,houses.houseno";
/* 3585:3995 */         where = "";
/* 3586:     */       }
/* 3587:     */     }
/* 3588:     */     else
/* 3589:     */     {
/* 3590:4001 */       where = "";
/* 3591:4002 */       table = "hc_area";
/* 3592:4003 */       idField = "id";
/* 3593:4004 */       surveyM.get().equals("VA");
/* 3594:     */     }
/* 3595:4007 */     System.out.println(">>" + fields + " " + where + " " + table + " " + contextType);
/* 3596:4008 */     return getEnumData1(table, where + (constraints != null ? " and " + constraints : ""), details, fields, idField);
/* 3597:     */   }
/* 3598:     */   
/* 3599:     */   public void saveVAData(XModel context, String contextType, XModel dataM)
/* 3600:     */     throws Exception
/* 3601:     */   {
/* 3602:4014 */     XModel areaM = (XModel)context.get("area");
/* 3603:4015 */     XModel houseM = (XModel)context.get("house");
/* 3604:4016 */     XModel householdM = (XModel)context.get("household");
/* 3605:4017 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3606:4018 */     XModel indivM = (XModel)context.get("va");
/* 3607:4019 */     XModel visitM = (XModel)context.get("visit");
/* 3608:4020 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3609:4021 */     XModel details = new XBaseModel();
/* 3610:4022 */     String where = "";
/* 3611:4023 */     String table = "";
/* 3612:4024 */     String idField = "";
/* 3613:4025 */     String id = "";
/* 3614:4026 */     String path = "/va/" + areaM.get() + indivM.get() + "/" + surveyorM.get() + "/";
/* 3615:4027 */     System.out.println("Save VA called " + dataM.getNumChildren());
/* 3616:4028 */     for (int i = 0; i < dataM.getNumChildren(); i++) {
/* 3617:4030 */       TestXUIDB.getInstance().saveKeyValue("keyvalue", path + dataM.get(i).getId().replace("-", "/"), dataM.get(i).get().toString());
/* 3618:     */     }
/* 3619:     */   }
/* 3620:     */   
/* 3621:     */   public void saveCMEData(XModel context, String contextType, XModel dataM)
/* 3622:     */     throws Exception
/* 3623:     */   {
/* 3624:4037 */     XModel areaM = (XModel)context.get("area");
/* 3625:4038 */     XModel houseM = (XModel)context.get("house");
/* 3626:4039 */     XModel householdM = (XModel)context.get("household");
/* 3627:4040 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3628:4041 */     XModel indivM = (XModel)context.get("va");
/* 3629:4042 */     XModel visitM = (XModel)context.get("visit");
/* 3630:4043 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3631:4044 */     XModel details = new XBaseModel();
/* 3632:4045 */     String where = "";
/* 3633:4046 */     String table = "";
/* 3634:4047 */     String idField = "";
/* 3635:4048 */     String id = "";
/* 3636:4049 */     String path = "/cme/" + indivM.get() + "/";
/* 3637:4050 */     System.out.println("Save VA called " + dataM.getNumChildren());
/* 3638:4051 */     for (int i = 0; i < dataM.getNumChildren(); i++) {
/* 3639:4053 */       TestXUIDB.getInstance().saveKeyValue("keyvalue", path + dataM.get(i).getId().replace("-", "/"), dataM.get(i).get().toString());
/* 3640:     */     }
/* 3641:     */   }
/* 3642:     */   
/* 3643:     */   public void saveEnumData(XModel context, String contextType, XModel dataM)
/* 3644:     */     throws Exception
/* 3645:     */   {
/* 3646:4060 */     XModel areaM = (XModel)context.get("area");
/* 3647:4061 */     XModel houseM = (XModel)context.get("house");
/* 3648:4062 */     XModel householdM = (XModel)context.get("household");
/* 3649:4063 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3650:4064 */     XModel indivM = (XModel)context.get("member");
/* 3651:4065 */     XModel visitM = (XModel)context.get("visit");
/* 3652:4066 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3653:4067 */     XModel details = new XBaseModel();
/* 3654:4068 */     String where = "";
/* 3655:4069 */     String table = "";
/* 3656:4070 */     String idField = "";
/* 3657:4071 */     String id = "";
/* 3658:4073 */     if (contextType.equals("std"))
/* 3659:     */     {
/* 3660:4075 */       XModel clinicM = (XModel)context.get("clinic");
/* 3661:4076 */       XModel stdM = (XModel)context.get("std");
/* 3662:4077 */       where = "uniqno='" + stdM.get() + "'";
/* 3663:4078 */       table = "patients";
/* 3664:4079 */       idField = "uniqno";
/* 3665:4080 */       ((XModel)dataM.get("ictc_code")).set(clinicM.get());
/* 3666:4081 */       ((XModel)dataM.get("uniqno")).set(stdM.get());
/* 3667:     */     }
/* 3668:4084 */     if (contextType.equals("va"))
/* 3669:     */     {
/* 3670:4086 */       saveVAData(context, contextType, dataM);
/* 3671:4087 */       return;
/* 3672:     */     }
/* 3673:4090 */     if ((contextType.equals("member")) && (indivM != null) && (indivM.get() != null) && (!indivM.equals("")))
/* 3674:     */     {
/* 3675:4092 */       where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "' and idc='" + indivM.get() + "'";
/* 3676:4093 */       table = "members";
/* 3677:4094 */       idField = "idc";
/* 3678:4095 */       ((XModel)dataM.get("enum_area")).set(areaM.get());
/* 3679:4096 */       ((XModel)dataM.get("house")).set(houseM.get());
/* 3680:4097 */       ((XModel)dataM.get("household")).set(householdM.get());
/* 3681:4098 */       ((XModel)dataM.get("idc")).set(indivM.get());
/* 3682:     */     }
/* 3683:4100 */     else if (((contextType.equals("births")) || (contextType.equals("marriages"))) && (contextTypeM != null))
/* 3684:     */     {
/* 3685:4102 */       where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "' and idc='" + contextTypeM.get() + "'";
/* 3686:4103 */       table = contextType;
/* 3687:4104 */       idField = "idc";
/* 3688:4105 */       ((XModel)dataM.get("enum_area")).set(areaM.get());
/* 3689:4106 */       ((XModel)dataM.get("house")).set(houseM.get());
/* 3690:4107 */       ((XModel)dataM.get("household")).set(householdM.get());
/* 3691:4108 */       ((XModel)dataM.get("idc")).set(contextTypeM.get());
/* 3692:     */     }
/* 3693:4110 */     else if ((contextType.equals("visit")) && (visitM != null) && (visitM.get() != null) && (!visitM.equals("")))
/* 3694:     */     {
/* 3695:4112 */       where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "' and visitid='" + visitM.get() + "' and teamid='" + surveyorM.get() + "'";
/* 3696:4113 */       table = "visit";
/* 3697:4114 */       idField = "visitid";
/* 3698:4115 */       ((XModel)dataM.get("enum_area")).set(areaM.get());
/* 3699:4116 */       ((XModel)dataM.get("house")).set(houseM.get());
/* 3700:4117 */       ((XModel)dataM.get("household")).set(householdM.get());
/* 3701:4118 */       ((XModel)dataM.get("visitid")).set(visitM.get());
/* 3702:4119 */       ((XModel)dataM.get("visittime")).set(visitM.get());
/* 3703:4120 */       ((XModel)dataM.get("teamid")).set(surveyorM.get());
/* 3704:     */     }
/* 3705:4122 */     else if ((householdM != null) && (householdM.get() != null) && (!householdM.equals("")))
/* 3706:     */     {
/* 3707:4124 */       where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "'";
/* 3708:4125 */       table = "households";
/* 3709:4126 */       idField = "household";
/* 3710:4127 */       id = householdM.get().toString();
/* 3711:4128 */       ((XModel)dataM.get("enum_area")).set(areaM.get());
/* 3712:4129 */       ((XModel)dataM.get("house")).set(houseM.get());
/* 3713:4130 */       ((XModel)dataM.get("household")).set(householdM.get());
/* 3714:     */     }
/* 3715:4132 */     else if ((houseM != null) && (houseM.get() != null) && (!houseM.equals("")))
/* 3716:     */     {
/* 3717:4134 */       where = "enum_area='" + areaM.get() + "' and houseno='" + houseM.get() + "'";
/* 3718:4135 */       table = "houses";
/* 3719:4136 */       idField = "houseno";
/* 3720:4137 */       id = houseM.get().toString();
/* 3721:4138 */       ((XModel)dataM.get("enum_area")).set(areaM.get());
/* 3722:4139 */       ((XModel)dataM.get("houseno")).set(houseM.get());
/* 3723:     */     }
/* 3724:4142 */     saveDataM1(table, where, dataM);
/* 3725:     */   }
/* 3726:     */   
/* 3727:     */   private void saveISTPData(XModel context, String contextType, XModel dataM)
/* 3728:     */     throws Exception
/* 3729:     */   {
/* 3730:4148 */     XModel areaM = (XModel)context.get("area");
/* 3731:4149 */     XModel houseM = (XModel)context.get("clinic");
/* 3732:     */     
/* 3733:4151 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3734:4152 */     XModel indivM = (XModel)context.get("std");
/* 3735:4153 */     XModel visitM = (XModel)context.get("visit");
/* 3736:4154 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3737:4155 */     XModel details = new XBaseModel();
/* 3738:4156 */     String where = "";
/* 3739:4157 */     String table = "";
/* 3740:4158 */     String idField = "";
/* 3741:4159 */     String id = "";
/* 3742:4160 */     String path = "/std/" + areaM.get() + indivM.get() + "/" + surveyorM.get() + "/";
/* 3743:4161 */     System.out.println("Save VA called " + dataM.getNumChildren());
/* 3744:4162 */     for (int i = 0; i < dataM.getNumChildren(); i++) {
/* 3745:4164 */       TestXUIDB.getInstance().saveKeyValue("keyvalue", path + dataM.get(i).getId().replace("-", "/"), dataM.get(i).get().toString());
/* 3746:     */     }
/* 3747:     */   }
/* 3748:     */   
/* 3749:     */   public String getCount(String table, String where)
/* 3750:     */   {
/* 3751:4170 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3752:4171 */     dt.setupTable(table, "count(*) idc", where, "test", false);
/* 3753:4172 */     dt.retrieve();
/* 3754:4173 */     System.out.println(" Count is " + ((XModel)dt.get(0).get("idc")).get().toString());
/* 3755:4174 */     return ((XModel)dt.get(0).get("idc")).get().toString();
/* 3756:     */   }
/* 3757:     */   
/* 3758:     */   public String getMaxId(XModel context, String contextType)
/* 3759:     */     throws Exception
/* 3760:     */   {
/* 3761:4195 */     throw new Error("Unresolved compilation problems: \n\tType mismatch: cannot convert from int to String\n\tType mismatch: cannot convert from int to String\n\tType mismatch: cannot convert from int to String\n\tType mismatch: cannot convert from int to String\n");
/* 3762:     */   }
/* 3763:     */   
/* 3764:     */   public String getMaxIndivId(String area, String house, String household)
/* 3765:     */     throws Exception
/* 3766:     */   {
/* 3767:4221 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3768:4222 */     dt.setupTable("members", "max(idc) idc", "enum_area=" + area + " and house=" + house + " and household=" + household, "test", false);
/* 3769:4223 */     dt.retrieve();
/* 3770:     */     
/* 3771:4225 */     System.out.println(" Individuals " + dt.getNumChildren() + " " + household);
/* 3772:     */     
/* 3773:4227 */     System.out.println(dt.get(0).get(0));
/* 3774:4228 */     String id = dt.get(0).get("idc").toString();
/* 3775:4229 */     if (id == null) {
/* 3776:4230 */       return household + "00";
/* 3777:     */     }
/* 3778:     */     try
/* 3779:     */     {
/* 3780:4233 */       Integer.parseInt(id);
/* 3781:     */     }
/* 3782:     */     catch (NumberFormatException e)
/* 3783:     */     {
/* 3784:4237 */       e.printStackTrace();
/* 3785:4238 */       return household + "00";
/* 3786:     */     }
/* 3787:4240 */     return id;
/* 3788:     */   }
/* 3789:     */   
/* 3790:     */   public String getMaxStdId(String clinic)
/* 3791:     */     throws Exception
/* 3792:     */   {
/* 3793:4246 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
/* 3794:4247 */     return clinic + "-" + sdf.format(new Date());
/* 3795:     */   }
/* 3796:     */   
/* 3797:     */   public String getMaxHouseId(String area, String surveyorId)
/* 3798:     */     throws Exception
/* 3799:     */   {
/* 3800:4252 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3801:4253 */     dt.setupTable("houses", "max(houseno) houseno", "enum_area=" + area + " and houseno like '" + surveyorId + "%'", "test", false);
/* 3802:4254 */     dt.retrieve();
/* 3803:     */     
/* 3804:4256 */     int i = 0;
/* 3805:4256 */     if (i < dt.getNumChildren())
/* 3806:     */     {
/* 3807:4258 */       System.out.println(dt.get(i).get(0));
/* 3808:4259 */       String id = dt.get(i).get("houseno").toString();
/* 3809:4260 */       if (id == null) {
/* 3810:4261 */         return surveyorId + "000";
/* 3811:     */       }
/* 3812:4263 */       return id;
/* 3813:     */     }
/* 3814:4266 */     return surveyorId + "000";
/* 3815:     */   }
/* 3816:     */   
/* 3817:     */   public String getMaxHouseholdId(String area, String house)
/* 3818:     */     throws Exception
/* 3819:     */   {
/* 3820:4271 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3821:4272 */     dt.setupTable("households", "max(household) household", "enum_area=" + area + " and house = '" + house + "'", "test", false);
/* 3822:4273 */     dt.retrieve();
/* 3823:     */     
/* 3824:4275 */     int i = 0;
/* 3825:4275 */     if (i < dt.getNumChildren())
/* 3826:     */     {
/* 3827:4277 */       System.out.println(dt.get(i).get(0));
/* 3828:4278 */       String id = dt.get(i).get("household").toString();
/* 3829:4279 */       if (id == null) {
/* 3830:4280 */         return house + "00";
/* 3831:     */       }
/* 3832:4281 */       return id;
/* 3833:     */     }
/* 3834:4284 */     return null;
/* 3835:     */   }
/* 3836:     */   
/* 3837:     */   public XModel getIndividuals(String area, String house, String household, XModel hhM)
/* 3838:     */     throws Exception
/* 3839:     */   {
/* 3840:4290 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3841:4291 */     dt.setupTable("members", "idc", "enum_area=" + area + " and house=" + house + " and household=" + household, "test", false);
/* 3842:4292 */     dt.retrieve();
/* 3843:     */     
/* 3844:4294 */     System.out.println(" Individuals " + dt.getNumChildren() + " " + household);
/* 3845:4295 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 3846:     */     {
/* 3847:4297 */       String id = dt.get(i).get("idc").toString();
/* 3848:4298 */       XModel tt = (XModel)hhM.get(id);
/* 3849:     */       
/* 3850:4300 */       tt.setId(dt.get(i).get("idc").toString());
/* 3851:4301 */       XModel xm = getIndividualdetails(id, household, house, area);
/* 3852:     */       
/* 3853:4303 */       tt.append(xm);
/* 3854:4304 */       tt.append(getDataM("data", "interview", area, house, household, id));
/* 3855:     */     }
/* 3856:4307 */     return hhM;
/* 3857:     */   }
/* 3858:     */   
/* 3859:     */   public XModel getIndividualdetails(String individual, String household, String house, String area)
/* 3860:     */   {
/* 3861:4312 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3862:4313 */     dt.setupTable("members", "*", "enum_area=" + area + " and house=" + house + " and household=" + household + " and idc=" + individual, "test", true);
/* 3863:4314 */     dt.setName("updatestatus");
/* 3864:4315 */     dt.setId("updatestatus");
/* 3865:4316 */     dt.setTagName("data");
/* 3866:4317 */     dt.retrieve();
/* 3867:4318 */     XBaseModel xm = new XBaseModel();
/* 3868:4319 */     xm.setId("updatestatus");
/* 3869:4321 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 3870:4323 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 3871:     */       {
/* 3872:4325 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 3873:4326 */         tt.set(dt.get(i).get(j).get());
/* 3874:     */       }
/* 3875:     */     }
/* 3876:4331 */     return xm;
/* 3877:     */   }
/* 3878:     */   
/* 3879:     */   public XModel getInterview(String individual, String household, String house, String area, XModel xm)
/* 3880:     */   {
/* 3881:4336 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3882:4337 */     dt.setupTable("barshi_interview", "*", "enum_area=" + area + " and house=" + house + " and household=" + household + " and idc=" + individual, "test", true);
/* 3883:     */     
/* 3884:4339 */     dt.retrieve();
/* 3885:     */     
/* 3886:4341 */     xm.setId("interview");
/* 3887:4342 */     if (dt.getNumChildren() == 0)
/* 3888:     */     {
/* 3889:4344 */       getProto("barshi_interview", xm);
/* 3890:4345 */       xm.set("enum_area", area);
/* 3891:4346 */       xm.set("house", house);
/* 3892:4347 */       xm.set("household", household);
/* 3893:4348 */       xm.set("idc", individual);
/* 3894:4349 */       return xm;
/* 3895:     */     }
/* 3896:4351 */     getDetails(dt, xm);
/* 3897:     */     
/* 3898:4353 */     return xm;
/* 3899:     */   }
/* 3900:     */   
/* 3901:     */   public XModel getCC(String household, String house, String area, XModel xm)
/* 3902:     */   {
/* 3903:4358 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3904:4359 */     dt.setupTable("barshi_cc", "*", "enum_area=" + area + " and house=" + house + " and household=" + household, "test", true);
/* 3905:     */     
/* 3906:4361 */     dt.retrieve();
/* 3907:     */     
/* 3908:4363 */     xm.setId("characterstics");
/* 3909:4364 */     if (dt.getNumChildren() == 0)
/* 3910:     */     {
/* 3911:4366 */       getProto("barshi_cc", xm);
/* 3912:4367 */       xm.set("enum_area", area);
/* 3913:4368 */       xm.set("house", house);
/* 3914:4369 */       xm.set("household", household);
/* 3915:     */       
/* 3916:4371 */       return xm;
/* 3917:     */     }
/* 3918:4373 */     getDetails(dt, xm);
/* 3919:     */     
/* 3920:4375 */     return xm;
/* 3921:     */   }
/* 3922:     */   
/* 3923:     */   public void getDetails(DatabaseTableModel dt, XModel xm)
/* 3924:     */   {
/* 3925:4380 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 3926:4382 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 3927:     */       {
/* 3928:4384 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 3929:4385 */         tt.set(dt.get(i).get(j).get());
/* 3930:     */       }
/* 3931:     */     }
/* 3932:     */   }
/* 3933:     */   
/* 3934:     */   public void getProto(String table, XModel dataM)
/* 3935:     */   {
/* 3936:4392 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3937:4393 */     dt.setupTable(table, "*", "test", true);
/* 3938:4394 */     for (int i = 0; i < dt.getNumAttributes(); i++) {
/* 3939:4396 */       dataM.get(dt.getAttribName(i));
/* 3940:     */     }
/* 3941:     */   }
/* 3942:     */   
/* 3943:     */   public void init() {}
/* 3944:     */   
/* 3945:     */   public void test1(String[] args)
/* 3946:     */     throws Exception
/* 3947:     */   {
/* 3948:4407 */     NamedConnectionManager nc = (NamedConnectionManager)NamedConnectionManager.getInstance();
/* 3949:     */     
/* 3950:4409 */     nc.addConnection("test", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/vatest", "root", "password");
/* 3951:4410 */     ConnectionObject co = nc.getConnection("test");
/* 3952:     */     
/* 3953:4412 */     FileWriter fout = new FileWriter("c:\\testdbfetch.xml");
/* 3954:4413 */     XModel out = new XBaseModel();
/* 3955:4414 */     XModel taskM = out;
/* 3956:4415 */     getTasks(taskM, "12", "");
/* 3957:4416 */     XModel dataM = (XModel)out.get("survey/surveydata");
/* 3958:     */     
/* 3959:4418 */     dataM = getAreas("12", dataM, "2");
/* 3960:     */     
/* 3961:4420 */     XModel areaM = dataM.get(0);
/* 3962:4421 */     areaM = getHouses("13", areaM);
/* 3963:     */     
/* 3964:4423 */     String house = areaM.get(1).getId();
/* 3965:4424 */     System.out.println(house);
/* 3966:4425 */     XModel houseM = areaM.get(1);
/* 3967:4426 */     houseM = getHouseholds("13", house, houseM);
/* 3968:     */     
/* 3969:4428 */     XDataSource.outputModel(fout, out);
/* 3970:     */     
/* 3971:4430 */     System.out.println("Complete");
/* 3972:     */   }
/* 3973:     */   
/* 3974:     */   public String getTaskPath(String path)
/* 3975:     */   {
/* 3976:4435 */     return join(split("task0-1/task1-2/task3-3", "-", 0));
/* 3977:     */   }
/* 3978:     */   
/* 3979:     */   public String join(Vector v)
/* 3980:     */   {
/* 3981:4439 */     String t = "";
/* 3982:4440 */     for (int i = 0; i < v.size(); i++) {
/* 3983:4442 */       t = t + (i == 0 ? "" : "/") + v.get(i).toString();
/* 3984:     */     }
/* 3985:4445 */     return t;
/* 3986:     */   }
/* 3987:     */   
/* 3988:     */   public void testImport(String file)
/* 3989:     */     throws Exception
/* 3990:     */   {
/* 3991:4450 */     FileInputStream fin = new FileInputStream(file);
/* 3992:4451 */     DataInputStream din = new DataInputStream(fin);
/* 3993:4452 */     String line = "";
/* 3994:4453 */     StringBuffer logs = new StringBuffer();
/* 3995:4454 */     while (line != null)
/* 3996:     */     {
/* 3997:4456 */       line = din.readLine();
/* 3998:4457 */       logs.append(line + "\r\n");
/* 3999:     */     }
/* 4000:4460 */     System.out.println(logs);
/* 4001:4461 */     importChangeLogs(logs.toString());
/* 4002:     */   }
/* 4003:     */   
/* 4004:     */   public void rollbackOutkeyvalue(String path, String table)
/* 4005:     */     throws Exception
/* 4006:     */   {
/* 4007:4466 */     XModel xm1 = new XBaseModel();
/* 4008:4467 */     TestXUIDB.getInstance().getData("outkeyvalue", "value1", "key1 like '" + path + "'", xm1);
/* 4009:4468 */     String value = xm1.get(0).get(0).get().toString();
/* 4010:4469 */     XModel testM = new XBaseModel();
/* 4011:4470 */     StringReader sr = new StringReader(value);
/* 4012:4471 */     XmlElement xe = XmlSource.read(sr);
/* 4013:4472 */     XDataSource xd = new XDataSource();
/* 4014:4473 */     xd.loadTable(xe, testM);
/* 4015:     */     
/* 4016:4475 */     System.out.println(" Value " + value + " " + testM.get(0).getId());
/* 4017:4476 */     TestXUIDB.getInstance().saveKeyValue1(table, path, testM);
/* 4018:     */   }
/* 4019:     */   
/* 4020:     */   public static void main(String[] args)
/* 4021:     */     throws Exception
/* 4022:     */   {
/* 4023:4482 */     XModel xm1 = new XBaseModel();
/* 4024:     */     
/* 4025:4484 */     String[] keys1 = {
/* 4026:4485 */       "/cme/06300059_01_01", 
/* 4027:4486 */       "/cme/06300060_01_09", 
/* 4028:4487 */       "/cme/06300060_01_10", 
/* 4029:4488 */       "/cme/06300061_01_06", 
/* 4030:4489 */       "/cme/06300061_01_08", 
/* 4031:4490 */       "/cme/06300063_01_04", 
/* 4032:4491 */       "/cme/06300065_01_03" };
/* 4033:4493 */     for (int i = 0; i < keys1.length; i++) {
/* 4034:4495 */       TestXUIDB.getInstance().rollbackOutkeyvalue(keys1[i], "keyvalue_copy");
/* 4035:     */     }
/* 4036:4497 */     System.in.read();
/* 4037:4498 */     TestXUIDB.getInstance().createNotification("test", "test", "test\\'1\\'", "test", "test", null, null, "email");
/* 4038:4499 */     System.in.read();
/* 4039:4500 */     UserAuthentication ua = new UserAuthentication();
/* 4040:4501 */     ua.authenticate1("admin", "password");
/* 4041:     */     
/* 4042:4503 */     TestXUIDB.getInstance().sendOutboundLogs("admin");
/* 4043:     */     
/* 4044:4505 */     System.in.read();
/* 4045:     */     
/* 4046:4507 */     System.out.println("Starting");
/* 4047:     */     
/* 4048:4509 */     XModel xm = TestXUIDB.getInstance().getKeyValChildren(null, "", "", "va");
/* 4049:4510 */     System.out.println(xm.getNumChildren());
/* 4050:4511 */     System.in.read();
/* 4051:4512 */     String tt = TestXUIDB.getInstance().getTranslation("Yes", "mr");
/* 4052:4513 */     System.out.println(tt);
/* 4053:4514 */     System.out.println(TestXUIDB.getInstance().getTranslation("Had a doctor EVER stated that the deceased had the following diseases?", "Marathi"));
/* 4054:     */     
/* 4055:4516 */     System.in.read();
/* 4056:4517 */     ChangeLog.startLog("test", "test", "test");
/* 4057:     */     
/* 4058:4519 */     ChangeLog.endLog();
/* 4059:4520 */     System.in.read();
/* 4060:4521 */     System.out.println("Starting");
/* 4061:     */     
/* 4062:4523 */     System.out.println(TestXUIDB.getInstance().getTranslation("Date of birth of deceased", "Marathi"));
/* 4063:4524 */     System.out.println(TestXUIDB.getInstance().getTranslation1(tt, "Marathi"));
/* 4064:     */     
/* 4065:4526 */     System.in.read();
/* 4066:     */     
/* 4067:4528 */     System.in.read();
/* 4068:     */     
/* 4069:4530 */     System.out.println(TestXUIDB.getInstance().isPhysicianAway("Sagar", "2011-1-21"));
/* 4070:4531 */     System.out.println(TestXUIDB.getInstance().updateAwayDate("Sagar", new Date("01/22/2011")));
/* 4071:4532 */     System.in.read();
/* 4072:     */     
/* 4073:4534 */     System.out.println(TestXUIDB.getInstance().isValidIcdAge("0.769", "C50"));
/* 4074:4535 */     System.out.println(TestXUIDB.getInstance().isValidIcdSex("Male", "O34"));
/* 4075:4536 */     System.out.println(TestXUIDB.getInstance().checkEquivalence("A40", "A41"));
/* 4076:     */     
/* 4077:4538 */     Vector keys = new Vector();
/* 4078:4539 */     keys.add("household");
/* 4079:     */     
/* 4080:4541 */     System.in.read();
/* 4081:     */     
/* 4082:4543 */     System.out.println(xm.getNumChildren() + " " + xm.get(0).getId());
/* 4083:     */     
/* 4084:4545 */     String str = "<l id='Tue Apr 13 17:26:00 IST 2010'>\t\t<dt id='dt' table='data' key=\"name='cookingPlace' and area = '22' and house = '16048' and household = '1604801' and member is null\">\t\t<d id='data' area='22' house='16048' household='1604801'  name='cookingPlace' value=' Choose Any One' /></dt></l>";
/* 4085:     */   }
/* 4086:     */   
/* 4087:     */   public void saveLogistics(XModel xm)
/* 4088:     */     throws Exception
/* 4089:     */   {
/* 4090:4551 */     xm.set("@name", xm.getId());
/* 4091:4552 */     Object path = xm.get("@path");
/* 4092:4553 */     String where = "name='" + xm.getId() + "'";
/* 4093:4554 */     where = where + " and path='" + path + "'";
/* 4094:4555 */     saveDataM("logistics", where, xm);
/* 4095:     */   }
/* 4096:     */   
/* 4097:     */   public void saveTree(XModel root, String table, String parentPath)
/* 4098:     */     throws Exception
/* 4099:     */   {
/* 4100:4561 */     String key = parentPath + "/" + root.getId();
/* 4101:4562 */     String value = (String)root.get();
/* 4102:4563 */     if (value != null) {
/* 4103:4564 */       saveKeyValue(table, key, value);
/* 4104:     */     }
/* 4105:4566 */     for (int i = 0; i < root.getNumChildren(); i++) {
/* 4106:4568 */       saveTree(root.get(i), table, key);
/* 4107:     */     }
/* 4108:     */   }
/* 4109:     */   
/* 4110:     */   public void deleteKeyValue(String table, String key)
/* 4111:     */     throws Exception
/* 4112:     */   {
/* 4113:4575 */     String where = " key1 like '" + key + "'";
/* 4114:4576 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4115:4577 */     dt.setupTable(table, "*", where, "test", true);
/* 4116:     */     
/* 4117:4579 */     ChangeLog.startLog(table, "delete", where, getCurrentUser());
/* 4118:     */     
/* 4119:4581 */     String qry = "delete from " + table + " where " + where;
/* 4120:4582 */     PreparedStatement ps = dt.getTable().getPreparedStatement(qry);
/* 4121:4583 */     ps.execute();
/* 4122:     */     
/* 4123:4585 */     ChangeLog.endLog();
/* 4124:     */   }
/* 4125:     */   
/* 4126:     */   public void createDeleteLog(String table, String where)
/* 4127:     */     throws Exception
/* 4128:     */   {
/* 4129:4591 */     ChangeLog.startLog(table, "delete", where, getCurrentUser());
/* 4130:4592 */     ChangeLog.endLog();
/* 4131:     */   }
/* 4132:     */   
/* 4133:     */   public void saveKeyValue(String table, String key, String value)
/* 4134:     */     throws Exception
/* 4135:     */   {
/* 4136:4598 */     if ((key == null) || (key.equals("")))
/* 4137:     */     {
/* 4138:4600 */       System.out.println("Error :Key is null");
/* 4139:4601 */       return;
/* 4140:     */     }
/* 4141:4604 */     String where = " key1='" + key + "'";
/* 4142:4605 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4143:     */     
/* 4144:4607 */     ChangeLog.startLog(table, where, getCurrentUser());
/* 4145:     */     
/* 4146:4609 */     dt.setupTable(table, "*", where, "test", true);
/* 4147:     */     
/* 4148:4611 */     dt.retrieve();
/* 4149:     */     int updates;
/* 4150:4613 */     if (dt.getNumChildren() > 0)
/* 4151:     */     {
/* 4152:4615 */       System.out.println(" total " + dt.getNumChildren());
/* 4153:     */       
/* 4154:4617 */       String qry = "update " + table + " set value1=?";
/* 4155:4618 */       int count = 0;
/* 4156:4619 */       System.out.println("Query is " + qry);
/* 4157:     */       
/* 4158:4621 */       PreparedStatement ps = dt.getTable().getPreparedStatement(qry + " where " + where);
/* 4159:4622 */       ps.setBytes(1, value.getBytes("utf-8"));
/* 4160:4623 */       updates = ps.executeUpdate();
/* 4161:     */     }
/* 4162:     */     else
/* 4163:     */     {
/* 4164:4627 */       String s = "insert into " + table;
/* 4165:4628 */       String flds = "key1,value1";
/* 4166:     */       
/* 4167:4630 */       String values = "'" + key + "',?";
/* 4168:4631 */       int count = 0;
/* 4169:     */       
/* 4170:4633 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 4171:     */       
/* 4172:4635 */       System.out.println(" Debug sql " + s);
/* 4173:     */       
/* 4174:4637 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 4175:4638 */       ps.setBytes(1, value.getBytes("utf-8"));
/* 4176:4639 */       ps.execute();
/* 4177:     */     }
/* 4178:4642 */     ChangeLog.logField("key1", key);
/* 4179:4643 */     ChangeLog.logField("value1", value);
/* 4180:4644 */     ChangeLog.endLog();
/* 4181:     */   }
/* 4182:     */   
/* 4183:     */   public void saveKeyValue1(String table, String key, XModel xm)
/* 4184:     */     throws Exception
/* 4185:     */   {
/* 4186:4649 */     if ((key == null) || (key.equals("")))
/* 4187:     */     {
/* 4188:4651 */       System.out.println("Error :Key is null");
/* 4189:4652 */       return;
/* 4190:     */     }
/* 4191:4654 */     if (xm.get() != null)
/* 4192:     */     {
/* 4193:4656 */       System.out.println(" Saving " + key + " Value= " + (String)xm.get());
/* 4194:     */       
/* 4195:4658 */       saveKeyValue(table, key, xm.get().toString());
/* 4196:     */     }
/* 4197:4660 */     for (int i = 0; i < xm.getNumChildren(); i++) {
/* 4198:4662 */       saveKeyValue1(table, key + "/" + xm.get(i).getId(), xm.get(i));
/* 4199:     */     }
/* 4200:     */   }
/* 4201:     */   
/* 4202:     */   public void readTree(XModel root, String table, String path)
/* 4203:     */   {
/* 4204:4668 */     getKeyValues(root, "keyvalue", path);
/* 4205:     */   }
/* 4206:     */   
/* 4207:     */   public String getKeyValuesSerialized(String table, String parentPath)
/* 4208:     */   {
/* 4209:4673 */     XModel xm = new XBaseModel();
/* 4210:4674 */     getKeyValues(xm, table, parentPath);
/* 4211:4675 */     StringWriter sw = new StringWriter();
/* 4212:     */     
/* 4213:4677 */     XDataSource.outputModel(sw, xm);
/* 4214:4678 */     return sw.getBuffer().toString();
/* 4215:     */   }
/* 4216:     */   
/* 4217:     */   public String getKeyValues(XModel xm, String table, String parentPath)
/* 4218:     */   {
/* 4219:4683 */     String where = " key1 like '" + parentPath + "/%' ";
/* 4220:4684 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4221:     */     
/* 4222:4686 */     dt.setupTable(table, "*", where, "test", false);
/* 4223:     */     
/* 4224:4688 */     dt.retrieve();
/* 4225:4689 */     String keyFld = "key1";
/* 4226:4690 */     String valFld = "value1";
/* 4227:4691 */     System.out.println("---" + table + "   " + where + " " + dt.getNumChildren());
/* 4228:4692 */     if (dt.getNumChildren() > 0) {
/* 4229:4694 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 4230:     */       {
/* 4231:4696 */         String key = dt.get(i).get(keyFld).toString();
/* 4232:4697 */         String value = dt.get(i).get(valFld).toString();
/* 4233:     */         
/* 4234:4699 */         KenList kl = new KenList(key);
/* 4235:4700 */         KenList k2 = new KenList(parentPath);
/* 4236:4701 */         String path1 = kl.subset(k2.size(), kl.size() - 1).toString();
/* 4237:4702 */         System.out.println("keyy====::" + key);
/* 4238:4703 */         System.out.println("valuepath====::" + path1);
/* 4239:4704 */         System.out.println("Value of key ====::" + value);
/* 4240:4705 */         System.out.println(key + " " + parentPath + " " + path1);
/* 4241:4706 */         xm.set(path1, value);
/* 4242:     */       }
/* 4243:     */     }
/* 4244:4711 */     return null;
/* 4245:     */   }
/* 4246:     */   
/* 4247:     */   public String getValue(String table, String key)
/* 4248:     */   {
/* 4249:4716 */     String where = " key1='" + key + "'";
/* 4250:4717 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4251:     */     
/* 4252:4719 */     dt.setupTable(table, "*", where, "test", true);
/* 4253:     */     
/* 4254:4721 */     dt.retrieve();
/* 4255:4722 */     if (dt.getNumChildren() > 0)
/* 4256:     */     {
/* 4257:4724 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 4258:     */       {
/* 4259:4726 */         String attrib = dt.getAttribName(j);
/* 4260:4727 */         String attrib1 = attrib.toLowerCase();
/* 4261:4729 */         if (attrib1.equals("value1")) {
/* 4262:4730 */           return dt.get(0).get(attrib).toString();
/* 4263:     */         }
/* 4264:     */       }
/* 4265:4734 */       return dt.get(0).get("value1").toString();
/* 4266:     */     }
/* 4267:4736 */     return null;
/* 4268:     */   }
/* 4269:     */   
/* 4270:     */   public void saveInterview1(XModel xm)
/* 4271:     */     throws Exception
/* 4272:     */   {
/* 4273:4740 */     xm.set("@name", xm.getId());
/* 4274:4741 */     Object area = xm.get("@area");
/* 4275:4742 */     Object house = xm.get("@house");
/* 4276:4743 */     Object household = xm.get("@household");
/* 4277:4744 */     Object member = xm.get("@member");
/* 4278:4745 */     String where = "name='" + xm.getId() + "'";
/* 4279:4746 */     where = where + " and " + (area == null ? "area is null" : new StringBuilder("area = '").append(area).append("'").toString());
/* 4280:4747 */     where = where + " and " + (house == null ? "house is null" : new StringBuilder("house = '").append(house).append("'").toString());
/* 4281:4748 */     where = where + " and " + (household == null ? "household is null" : new StringBuilder("household = '").append(household).append("'").toString());
/* 4282:4749 */     where = where + " and " + (member == null ? "member is null" : new StringBuilder("member = '").append(member).append("'").toString());
/* 4283:4750 */     saveDataM("data", where, xm);
/* 4284:     */   }
/* 4285:     */   
/* 4286:     */   public void saveHouse(String area, String id, XModel houseM)
/* 4287:     */     throws Exception
/* 4288:     */   {
/* 4289:4753 */     saveData("houses", "enum_area='" + area + "' and houseno='" + id + "'", houseM);
/* 4290:     */   }
/* 4291:     */   
/* 4292:     */   public void saveHouseHold(String area, String house, String id, XModel hhM)
/* 4293:     */     throws Exception
/* 4294:     */   {
/* 4295:4758 */     saveData("households", "enum_area='" + area + "' and  house='" + house + "' and household='" + id + "'", hhM);
/* 4296:     */   }
/* 4297:     */   
/* 4298:     */   public void saveMember(String area, String house, String hh, String id, XModel indvM)
/* 4299:     */     throws Exception
/* 4300:     */   {
/* 4301:4763 */     saveData("members", "enum_area='" + area + "' and house='" + house + "' and household='" + hh + "' and idc='" + id + "'", indvM);
/* 4302:     */   }
/* 4303:     */   
/* 4304:     */   public void saveVisitInfo(String area, String house, String hh, String idc, String team, String doneBy, XModel visitM)
/* 4305:     */     throws Exception
/* 4306:     */   {
/* 4307:4769 */     saveData("members", "area='" + area + "' and houseno='" + house + "' and  householdno='" + hh + "' and idc='" + idc + "' and team=" + team + " and doneby=" + doneBy, visitM);
/* 4308:     */   }
/* 4309:     */   
/* 4310:     */   public void saveInterview(String area, String house, String hh, String idc, XModel interviewM)
/* 4311:     */     throws Exception
/* 4312:     */   {
/* 4313:4773 */     saveData("barshi_interview", "enum_area='" + area + "' and house='" + house + "' and household='" + hh + "' and idc='" + idc + "'", interviewM);
/* 4314:     */   }
/* 4315:     */   
/* 4316:     */   public void saveResponse(String area, String house, String hh, String idc, XModel interviewM)
/* 4317:     */     throws Exception
/* 4318:     */   {
/* 4319:4778 */     saveData("responsedetails", "area='" + area + "' and house='" + house + "' householdno='" + hh + "' idc='" + idc + "'", interviewM);
/* 4320:     */   }
/* 4321:     */   
/* 4322:     */   public void saveCommon(String area, String house, String hh, XModel ccM)
/* 4323:     */     throws Exception
/* 4324:     */   {
/* 4325:4782 */     saveData("barshi_cc", "enum_area='" + area + "' and house='" + house + "' and household='" + hh + "' ", ccM);
/* 4326:     */   }
/* 4327:     */   
/* 4328:     */   public void saveTask2(String taskPath, String surveyType, String area, String house, String hh, String individual, XModel taskM)
/* 4329:     */     throws Exception
/* 4330:     */   {
/* 4331:4787 */     String table = "tasks";
/* 4332:     */     
/* 4333:4789 */     String where = "task='" + taskPath + "' and area=" + area + " and house='" + house + "' and household='" + hh + "' and member='" + individual + "' and survey_type='" + surveyType + "' and status != 1";
/* 4334:     */     
/* 4335:4791 */     XModel dataM = taskM;
/* 4336:     */   }
/* 4337:     */   
/* 4338:     */   public void save(XModel xM, String area, String house, String hh, String idc)
/* 4339:     */     throws Exception
/* 4340:     */   {
/* 4341:4797 */     System.out.println("/*********** Save Called " + xM.getId() + "***/");
/* 4342:4798 */     if (xM.getId().equals("characterstics")) {
/* 4343:4800 */       saveCommon(area, house, hh, xM);
/* 4344:     */     }
/* 4345:4802 */     if (xM.getId().equals("interview")) {
/* 4346:4804 */       saveInterview(area, house, hh, idc, xM);
/* 4347:     */     }
/* 4348:4806 */     xM.getId().equals("responsedetails");
/* 4349:     */   }
/* 4350:     */   
/* 4351:     */   public void get(XModel xM, String area, String house, String hh, String idc)
/* 4352:     */     throws Exception
/* 4353:     */   {
/* 4354:4812 */     System.out.println("/*********** Save Called " + xM.getId() + "***/");
/* 4355:4813 */     if (xM.getId().equals("characterstics")) {
/* 4356:4815 */       xM = getCC(hh, house, area, xM);
/* 4357:     */     }
/* 4358:4817 */     if (xM.getId().equals("interview")) {
/* 4359:4819 */       xM = getInterview(idc, hh, house, area, xM);
/* 4360:     */     }
/* 4361:4821 */     xM.getId().equals("responsedetails");
/* 4362:     */   }
/* 4363:     */   
/* 4364:     */   public XModel get1(String path, String area, String house, String hh, String idc)
/* 4365:     */     throws Exception
/* 4366:     */   {
/* 4367:4827 */     StringTokenizer st = new StringTokenizer(path, "/");
/* 4368:4828 */     String name = path;
/* 4369:4830 */     while (st.hasMoreTokens()) {
/* 4370:4832 */       name = st.nextToken();
/* 4371:     */     }
/* 4372:4834 */     System.out.println("/*********** Get Called " + name + "***/");
/* 4373:4835 */     XDataModel xM = getDataM("data", name, area, house, hh, idc);
/* 4374:4836 */     return xM;
/* 4375:     */   }
/* 4376:     */   
/* 4377:     */   public void authenticateUser(String user, String passwd)
/* 4378:     */   {
/* 4379:4841 */     DatabaseTableModel dtm = DatabaseTableModel.getTable("team");
/* 4380:     */     
/* 4381:4843 */     dtm.first();
/* 4382:     */   }
/* 4383:     */   
/* 4384:     */   public void authoriseUser(String roles)
/* 4385:     */   {
/* 4386:4848 */     DatabaseTableModel dtm = DatabaseTableModel.getTable("team");
/* 4387:     */     
/* 4388:4850 */     dtm.first();
/* 4389:     */   }
/* 4390:     */   
/* 4391:     */   public boolean isPhysicianAway(String id, String date)
/* 4392:     */   {
/* 4393:4855 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4394:     */     
/* 4395:4857 */     dt.setupTable("physician_away", "*", "physician='" + id + "' and away_date='" + date + "'", "test", false);
/* 4396:4858 */     dt.retrieve();
/* 4397:4859 */     System.out.println("No of Rows:" + dt.getNumChildren());
/* 4398:4860 */     if (dt.getNumChildren() > 0) {
/* 4399:4861 */       return true;
/* 4400:     */     }
/* 4401:4862 */     return false;
/* 4402:     */   }
/* 4403:     */   
/* 4404:     */   public boolean isPhysician(String username, String password)
/* 4405:     */   {
/* 4406:4867 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4407:     */     
/* 4408:4869 */     dt.setupTable("accounts", "*", "username='" + username + "' and password='" + password + "'", "test", false);
/* 4409:4870 */     dt.retrieve();
/* 4410:4871 */     System.out.println("No of Rows:" + dt.getNumChildren());
/* 4411:4872 */     if (dt.getNumChildren() > 0) {
/* 4412:4873 */       return true;
/* 4413:     */     }
/* 4414:4874 */     return false;
/* 4415:     */   }
/* 4416:     */   
/* 4417:     */   public boolean updateAwayDate(String id, Date date)
/* 4418:     */     throws Exception
/* 4419:     */   {
/* 4420:4880 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 4421:4881 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4422:     */     
/* 4423:4883 */     dt.setupTable("physician_away", "*", "physician='" + id + "'", "test", false);
/* 4424:4884 */     String sql = "insert into physician_away (physician, away_date) values ('" + id + "','" + sdf.format(date) + "')";
/* 4425:4885 */     PreparedStatement ps = dt.getTable().getPreparedStatement(sql);
/* 4426:4886 */     ps.execute();
/* 4427:     */     
/* 4428:4888 */     return true;
/* 4429:     */   }
/* 4430:     */   
/* 4431:     */   public void createPhysician(String username, String name, String languages, String coder, String adjudicator, String id, String status)
/* 4432:     */     throws Exception
/* 4433:     */   {
/* 4434:4894 */     XModel xm = new XBaseModel();
/* 4435:     */     
/* 4436:4896 */     XModel dataM = new XBaseModel();
/* 4437:4897 */     dataM.setId(username);
/* 4438:4898 */     ((XModel)dataM.get("username")).set(username);
/* 4439:4899 */     ((XModel)dataM.get("name")).set(name);
/* 4440:4900 */     ((XModel)dataM.get("languages")).set(languages);
/* 4441:4901 */     ((XModel)dataM.get("coder")).set(coder);
/* 4442:4902 */     ((XModel)dataM.get("adjudicator")).set(adjudicator);
/* 4443:4903 */     ((XModel)dataM.get("status")).set(status);
/* 4444:4904 */     if (!id.equals("0")) {
/* 4445:4905 */       ((XModel)dataM.get("id")).set(id);
/* 4446:     */     }
/* 4447:4907 */     TestXUIDB.getInstance().saveData("physician", "username='" + username + "'", dataM);
/* 4448:     */   }
/* 4449:     */   
/* 4450:     */   public void createAccount(String username, String password, String roles)
/* 4451:     */     throws Exception
/* 4452:     */   {
/* 4453:4913 */     XModel xm = new XBaseModel();
/* 4454:     */     
/* 4455:4915 */     XModel dataM = new XBaseModel();
/* 4456:4916 */     dataM.setId(username);
/* 4457:4917 */     ((XModel)dataM.get("username")).set(username);
/* 4458:4918 */     ((XModel)dataM.get("password")).set(password);
/* 4459:4919 */     ((XModel)dataM.get("roles")).set(roles);
/* 4460:4920 */     TestXUIDB.getInstance().saveData("accounts", "username='" + username + "'", dataM);
/* 4461:     */   }
/* 4462:     */   
/* 4463:     */   public boolean physicianExists(String username)
/* 4464:     */   {
/* 4465:4925 */     XModel xm = new XBaseModel();
/* 4466:4926 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4467:4927 */     dt.setupTable("physician", "*", "username='" + username + "'", "test", false);
/* 4468:4928 */     dt.retrieve();
/* 4469:4929 */     if (dt.getNumChildren() > 0) {
/* 4470:4930 */       return true;
/* 4471:     */     }
/* 4472:4933 */     return false;
/* 4473:     */   }
/* 4474:     */   
/* 4475:     */   public boolean userExists(String username)
/* 4476:     */   {
/* 4477:4938 */     XModel xm = new XBaseModel();
/* 4478:4939 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4479:4940 */     dt.setupTable("accounts", "*", "username='" + username + "'", "test", false);
/* 4480:4941 */     dt.retrieve();
/* 4481:4942 */     if (dt.getNumChildren() > 0) {
/* 4482:4943 */       return true;
/* 4483:     */     }
/* 4484:4946 */     return false;
/* 4485:     */   }
/* 4486:     */   
/* 4487:     */   public boolean getWorkLoad(String physician, XModel xm)
/* 4488:     */   {
/* 4489:4951 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4490:4952 */     workload = TestXUIDB.getInstance().getProperty("workload");
/* 4491:     */     
/* 4492:4954 */     String sql = "SELECT a.id,SUM(IF(task LIKE '%task0/%',1,0)) number ,IF(b.STATUS='1','Complete','In Process') STATUS,IF(task LIKE '%task0','Coding',IF(task LIKE '%task1','Reconciliation',IF(task LIKE '%task2','Adjudication',NULL))) stage, a.username FROM physician  a LEFT JOIN tasks b ON a.id=b.assignedTo AND (task LIKE '%task0/%' OR task IS NULL)  WHERE task LIKE '%task0/%' AND task IS NOT NULL AND a.id LIKE '" + physician + "' GROUP BY a.id,IF(b.STATUS='1','Complete','In Process'),IF(task LIKE '%task0','Coding',IF(task LIKE '%task1','Reconciliation',IF(task LIKE '%task2','Adjudication',NULL)))";
/* 4493:4955 */     System.out.println("sql::" + sql);
/* 4494:     */     
/* 4495:4957 */     dt.setSqlStatement(sql, "test", false);
/* 4496:     */     
/* 4497:4959 */     dt.retrieve();
/* 4498:4960 */     System.out.println("No of Rows1:" + dt.getNumChildren());
/* 4499:4961 */     if (dt.getNumChildren() > 0) {
/* 4500:4963 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 4501:     */       {
/* 4502:4965 */         XModel row = xm.get(i);
/* 4503:4966 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 4504:     */         {
/* 4505:4968 */           ((XModel)row.get(dt.getAttribName(j))).set(dt.get(i).get(j).get());
/* 4506:4969 */           System.out.println(dt.getAttribName(j) + "=" + dt.get(i).get(j).get());
/* 4507:     */         }
/* 4508:     */       }
/* 4509:     */     }
/* 4510:4975 */     return false;
/* 4511:     */   }
/* 4512:     */   
/* 4513:     */   public boolean getPhysiciansWithLessWorkload(String language, String status, XModel xm)
/* 4514:     */   {
/* 4515:4980 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4516:     */     
/* 4517:4982 */     workload = TestXUIDB.getInstance().getProperty("workload");
/* 4518:     */     
/* 4519:4984 */     String sql = "SELECT a.id,SUM(IF((task LIKE '%task0/%' ) AND (b.STATUS IS NULL OR b.STATUS =0),1,0)) number, IF(task LIKE '%task0','Coding',IF(task LIKE '%task1','Reconciliation',IF(task LIKE '%task2','Adjudication',NULL))) stage, a.username  FROM  physician  a LEFT JOIN tasks   b ON a.id=b.assignedTo WHERE  " + status + " AND a.languages LIKE '%" + language + "%'GROUP BY a.id  HAVING number < " + workload + " ORDER BY number,RAND() ASC";
/* 4520:4985 */     System.out.println("sql::" + sql);
/* 4521:     */     
/* 4522:4987 */     dt.setSqlStatement(sql, "test", false);
/* 4523:     */     
/* 4524:4989 */     dt.retrieve();
/* 4525:4990 */     System.out.println("No of Rows1:" + dt.getNumChildren());
/* 4526:4991 */     if (dt.getNumChildren() > 0) {
/* 4527:4993 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 4528:     */       {
/* 4529:4995 */         XModel row = xm.get(i);
/* 4530:4996 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 4531:     */         {
/* 4532:4998 */           ((XModel)row.get(dt.getAttribName(j))).set(dt.get(i).get(j).get());
/* 4533:4999 */           System.out.println(dt.getAttribName(j) + "=" + dt.get(i).get(j).get());
/* 4534:     */         }
/* 4535:     */       }
/* 4536:     */     }
/* 4537:5005 */     return false;
/* 4538:     */   }
/* 4539:     */   
/* 4540:     */   public boolean getData(String table, String fields, String where, XModel xm)
/* 4541:     */   {
/* 4542:5010 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4543:     */     
/* 4544:5012 */     dt.setupTable(table, fields, where, "test", false);
/* 4545:5013 */     dt.retrieve();
/* 4546:5014 */     System.out.println("No of Rows1:" + dt.getNumChildren());
/* 4547:5015 */     if (dt.getNumChildren() > 0) {
/* 4548:5017 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 4549:     */       {
/* 4550:5019 */         XModel row = xm.get(i);
/* 4551:5020 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 4552:     */         {
/* 4553:5022 */           ((XModel)row.get(dt.getAttribName(j))).set(dt.get(i).get(j).get());
/* 4554:5023 */           System.out.println(dt.getAttribName(j) + "=" + dt.get(i).get(j).get());
/* 4555:     */         }
/* 4556:5026 */         xm.append(row);
/* 4557:     */       }
/* 4558:     */     }
/* 4559:5030 */     return false;
/* 4560:     */   }
/* 4561:     */   
/* 4562:     */   public boolean getPhysicianDetails(String where, XModel xm)
/* 4563:     */   {
/* 4564:5035 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4565:     */     
/* 4566:5037 */     dt.setupTable("physician", "*", where, "test", false);
/* 4567:5038 */     dt.retrieve();
/* 4568:5039 */     System.out.println("No of Rows:" + dt.getNumChildren());
/* 4569:5040 */     if (dt.getNumChildren() > 0) {
/* 4570:5042 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 4571:     */       {
/* 4572:5044 */         XModel row = xm.get(i);
/* 4573:5045 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 4574:     */         {
/* 4575:5047 */           ((XModel)row.get(dt.getAttribName(j))).set(dt.get(i).get(j).get());
/* 4576:5048 */           System.out.println(dt.getAttribName(j) + "=" + dt.get(i).get(j).get());
/* 4577:     */         }
/* 4578:     */       }
/* 4579:     */     }
/* 4580:5054 */     return false;
/* 4581:     */   }
/* 4582:     */   
/* 4583:     */   public boolean getAccountDetails(String where, XModel xm)
/* 4584:     */   {
/* 4585:5059 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4586:     */     
/* 4587:5061 */     dt.setupTable("accounts", "*", where, "test", false);
/* 4588:5062 */     dt.retrieve();
/* 4589:5063 */     System.out.println("No of Rows:" + dt.getNumChildren());
/* 4590:5064 */     if (dt.getNumChildren() > 0) {
/* 4591:5066 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 4592:     */       {
/* 4593:5068 */         XModel row = xm.get(i);
/* 4594:5069 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 4595:     */         {
/* 4596:5071 */           ((XModel)row.get(dt.getAttribName(j))).set(dt.get(i).get(j).get());
/* 4597:5072 */           System.out.println(dt.getAttribName(j) + "=" + dt.get(i).get(j).get());
/* 4598:     */         }
/* 4599:     */       }
/* 4600:     */     }
/* 4601:5078 */     return false;
/* 4602:     */   }
/* 4603:     */   
/* 4604:     */   public void getAllPhysicians(XModel xm)
/* 4605:     */   {
/* 4606:5083 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4607:5084 */     dt.setupTable("physician", "name,id", null, "test", false);
/* 4608:5085 */     dt.retrieve();
/* 4609:5086 */     if (dt.getNumChildren() > 0) {
/* 4610:5087 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 4611:     */       {
/* 4612:5088 */         XModel row = xm.get(i);
/* 4613:5089 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 4614:     */         {
/* 4615:5090 */           ((XModel)row.get(dt.getAttribName(j))).set(dt.get(i)
/* 4616:5091 */             .get(j).get());
/* 4617:5092 */           System.out.println(dt.getAttribName(j) + "=" + 
/* 4618:5093 */             dt.get(i).get(j).get());
/* 4619:     */         }
/* 4620:     */       }
/* 4621:     */     }
/* 4622:     */   }
/* 4623:     */   
/* 4624:     */   public boolean removePhysician(int physicianId)
/* 4625:     */   {
/* 4626:5100 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4627:5101 */     dt.setupTable("physician", "status", "id=" + physicianId, "test", true);
/* 4628:5102 */     dt.retrieve();
/* 4629:5103 */     int i = 0;
/* 4630:5104 */     String sql = "update physician set status='inactive' where id=" + physicianId;
/* 4631:5105 */     System.out.println("Remove phy::" + sql);
/* 4632:     */     try
/* 4633:     */     {
/* 4634:5107 */       i = dt.executeUpdate(sql);
/* 4635:5108 */       if (i > 0) {
/* 4636:5109 */         return true;
/* 4637:     */       }
/* 4638:     */     }
/* 4639:     */     catch (Exception e)
/* 4640:     */     {
/* 4641:5113 */       e.printStackTrace();
/* 4642:     */     }
/* 4643:5116 */     return false;
/* 4644:     */   }
/* 4645:     */   
/* 4646:     */   public XModel getChildren(String type, String subtype, XModel context, String fields, String constraints)
/* 4647:     */     throws Exception
/* 4648:     */   {
/* 4649:5121 */     if (type.equals("Enumeration")) {
/* 4650:5123 */       return getEnumDataChildren(context, fields, constraints, subtype);
/* 4651:     */     }
/* 4652:5125 */     if (type.equals("va")) {
/* 4653:5127 */       return getEnumDataChildren(context, fields, constraints, subtype);
/* 4654:     */     }
/* 4655:5129 */     return null;
/* 4656:     */   }
/* 4657:     */   
/* 4658:     */   public XModel getData(String type, String subtype, XModel context, String fields)
/* 4659:     */     throws Exception
/* 4660:     */   {
/* 4661:5135 */     if (type.equals("Enumeration")) {
/* 4662:5137 */       return getEnumData(context, subtype, fields);
/* 4663:     */     }
/* 4664:5140 */     if (type.equals("va")) {
/* 4665:5142 */       return getVAData(context, subtype, fields);
/* 4666:     */     }
/* 4667:5145 */     if (type.equals("CME")) {
/* 4668:5147 */       return getCMEData(context, subtype, fields);
/* 4669:     */     }
/* 4670:5150 */     return null;
/* 4671:     */   }
/* 4672:     */   
/* 4673:     */   public String getTranslation(String text, String language)
/* 4674:     */   {
/* 4675:5153 */     System.out.println(language);
/* 4676:5154 */     if (language.equals("en")) {
/* 4677:5155 */       return text;
/* 4678:     */     }
/* 4679:5156 */     DatabaseTableModel dtm = new DatabaseTableModel();
/* 4680:5157 */     dtm.setupTable("dictionary", "localtext", "text='" + text + "' and lang='" + language + "'", "test", false);
/* 4681:5158 */     dtm.retrieve();
/* 4682:5160 */     if (dtm.getNumChildren() > 0) {
/* 4683:5161 */       return dtm.get(0).get(0).toString();
/* 4684:     */     }
/* 4685:5163 */     return text;
/* 4686:     */   }
/* 4687:     */   
/* 4688:     */   public String getTranslation1(String text, String language)
/* 4689:     */     throws Exception
/* 4690:     */   {
/* 4691:5169 */     if (language.equals("en")) {
/* 4692:5170 */       return text;
/* 4693:     */     }
/* 4694:5171 */     DatabaseTableModel dtm = new DatabaseTableModel();
/* 4695:     */     
/* 4696:5173 */     String where = "localtext='" + text + "'";
/* 4697:     */     try
/* 4698:     */     {
/* 4699:5175 */       System.out.println(new String(where.getBytes("utf-8"), "utf-8"));
/* 4700:     */     }
/* 4701:     */     catch (UnsupportedEncodingException e)
/* 4702:     */     {
/* 4703:5179 */       e.printStackTrace();
/* 4704:     */     }
/* 4705:5181 */     System.out.println(where);
/* 4706:5182 */     dtm.setupTable("vadictionary.dictionary where " + where, "*", "", "test", false);
/* 4707:5183 */     PreparedStatement ps = dtm.getTable().getPreparedStatement(" select text from dictionary where localtext=?");
/* 4708:     */     
/* 4709:5185 */     ps.setBytes(1, text.getBytes("utf-8"));
/* 4710:5186 */     ResultSet rs = ps.executeQuery();
/* 4711:5188 */     if (rs.next()) {
/* 4712:5189 */       return rs.getString("text");
/* 4713:     */     }
/* 4714:5191 */     return text;
/* 4715:     */   }
/* 4716:     */   
/* 4717:     */   public void saveData(String type, String subtype, XModel context, XModel dataM)
/* 4718:     */     throws Exception
/* 4719:     */   {
/* 4720:5196 */     if (type.equals("Enumeration")) {
/* 4721:5198 */       saveEnumData(context, subtype, dataM);
/* 4722:5200 */     } else if (type.equals("VA")) {
/* 4723:5201 */       saveVAData(context, subtype, dataM);
/* 4724:5203 */     } else if (type.equals("CME")) {
/* 4725:5204 */       saveCMEData(context, subtype, dataM);
/* 4726:     */     }
/* 4727:     */   }
/* 4728:     */   
/* 4729:     */   public class Out1
/* 4730:     */     extends PrintStream
/* 4731:     */   {
/* 4732:     */     public void flush()
/* 4733:     */     {
/* 4734:5211 */       super.flush();
/* 4735:     */     }
/* 4736:     */     
/* 4737:     */     public void close()
/* 4738:     */     {
/* 4739:5216 */       super.close();
/* 4740:     */     }
/* 4741:     */     
/* 4742:     */     public boolean checkError()
/* 4743:     */     {
/* 4744:5221 */       return super.checkError();
/* 4745:     */     }
/* 4746:     */     
/* 4747:     */     protected void setError()
/* 4748:     */     {
/* 4749:5226 */       super.setError();
/* 4750:     */     }
/* 4751:     */     
/* 4752:     */     public void write(int b)
/* 4753:     */     {
/* 4754:5231 */       super.write(b);
/* 4755:     */     }
/* 4756:     */     
/* 4757:     */     public void write(byte[] buf, int off, int len)
/* 4758:     */     {
/* 4759:5236 */       super.write(buf, off, len);
/* 4760:     */     }
/* 4761:     */     
/* 4762:     */     public void print(boolean b)
/* 4763:     */     {
/* 4764:5241 */       super.print(b);
/* 4765:     */     }
/* 4766:     */     
/* 4767:     */     public void print(char c)
/* 4768:     */     {
/* 4769:5246 */       super.print(c);
/* 4770:     */     }
/* 4771:     */     
/* 4772:     */     public void print(int i)
/* 4773:     */     {
/* 4774:5251 */       super.print(i);
/* 4775:     */     }
/* 4776:     */     
/* 4777:     */     public void print(long l)
/* 4778:     */     {
/* 4779:5256 */       super.print(l);
/* 4780:     */     }
/* 4781:     */     
/* 4782:     */     public void print(float f)
/* 4783:     */     {
/* 4784:5261 */       super.print(f);
/* 4785:     */     }
/* 4786:     */     
/* 4787:     */     public void print(double d)
/* 4788:     */     {
/* 4789:5266 */       super.print(d);
/* 4790:     */     }
/* 4791:     */     
/* 4792:     */     public void print(char[] s)
/* 4793:     */     {
/* 4794:5271 */       super.print(s);
/* 4795:     */     }
/* 4796:     */     
/* 4797:     */     public void print(String s)
/* 4798:     */     {
/* 4799:5276 */       super.print(s);
/* 4800:     */     }
/* 4801:     */     
/* 4802:     */     public void print(Object obj)
/* 4803:     */     {
/* 4804:5281 */       super.print(obj);
/* 4805:     */     }
/* 4806:     */     
/* 4807:     */     public void println()
/* 4808:     */     {
/* 4809:5286 */       super.println();
/* 4810:     */     }
/* 4811:     */     
/* 4812:     */     public void println(boolean x)
/* 4813:     */     {
/* 4814:5291 */       super.println(x);
/* 4815:     */     }
/* 4816:     */     
/* 4817:     */     public void println(char x)
/* 4818:     */     {
/* 4819:5296 */       super.println(x);
/* 4820:     */     }
/* 4821:     */     
/* 4822:     */     public void println(int x)
/* 4823:     */     {
/* 4824:5301 */       super.println(x);
/* 4825:     */     }
/* 4826:     */     
/* 4827:     */     public void println(long x)
/* 4828:     */     {
/* 4829:5306 */       super.println(x);
/* 4830:     */     }
/* 4831:     */     
/* 4832:     */     public void println(float x)
/* 4833:     */     {
/* 4834:5311 */       super.println(x);
/* 4835:     */     }
/* 4836:     */     
/* 4837:     */     public void println(double x)
/* 4838:     */     {
/* 4839:5316 */       super.println(x);
/* 4840:     */     }
/* 4841:     */     
/* 4842:     */     public void println(char[] x)
/* 4843:     */     {
/* 4844:5321 */       super.println(x);
/* 4845:     */     }
/* 4846:     */     
/* 4847:     */     public void println(String x)
/* 4848:     */     {
/* 4849:5326 */       super.println(x);
/* 4850:     */     }
/* 4851:     */     
/* 4852:     */     public void println(Object x)
/* 4853:     */     {
/* 4854:5331 */       super.println(x);
/* 4855:     */     }
/* 4856:     */     
/* 4857:     */     public PrintStream printf(String format, Object[] args)
/* 4858:     */     {
/* 4859:5336 */       return super.printf(format, args);
/* 4860:     */     }
/* 4861:     */     
/* 4862:     */     public PrintStream printf(Locale l, String format, Object[] args)
/* 4863:     */     {
/* 4864:5341 */       return super.printf(l, format, args);
/* 4865:     */     }
/* 4866:     */     
/* 4867:     */     public PrintStream format(String format, Object[] args)
/* 4868:     */     {
/* 4869:5346 */       return super.format(format, args);
/* 4870:     */     }
/* 4871:     */     
/* 4872:     */     public PrintStream format(Locale l, String format, Object[] args)
/* 4873:     */     {
/* 4874:5351 */       return super.format(l, format, args);
/* 4875:     */     }
/* 4876:     */     
/* 4877:     */     public PrintStream append(CharSequence csq)
/* 4878:     */     {
/* 4879:5356 */       return super.append(csq);
/* 4880:     */     }
/* 4881:     */     
/* 4882:     */     public PrintStream append(CharSequence csq, int start, int end)
/* 4883:     */     {
/* 4884:5361 */       return super.append(csq, start, end);
/* 4885:     */     }
/* 4886:     */     
/* 4887:     */     public PrintStream append(char c)
/* 4888:     */     {
/* 4889:5366 */       return super.append(c);
/* 4890:     */     }
/* 4891:     */     
/* 4892:     */     public Out1(OutputStream arg2) {}
/* 4893:     */   }
/* 4894:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.db.TestXUIDB2
 * JD-Core Version:    0.7.0.1
 */