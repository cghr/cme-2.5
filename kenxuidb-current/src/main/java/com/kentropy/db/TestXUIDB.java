/*    1:     */ package com.kentropy.db;
/*    2:     */ 
/*    3:     */ import com.kentropy.data.DataHandler;
/*    4:     */ import com.kentropy.data.TaskHandler;
/*    5:     */ import com.kentropy.model.KenList;
/*    6:     */ import com.kentropy.process.Process;
/*    7:     */ import com.kentropy.process.StateMachine;
/*    8:     */ import com.kentropy.security.client.UserAuthentication;
/*    9:     */ import com.kentropy.sync.ChangeLog;
/*   10:     */ import com.kentropy.transfer.Client;
/*   11:     */ import java.io.DataInputStream;
/*   12:     */ import java.io.File;
/*   13:     */ import java.io.FileInputStream;
/*   14:     */ import java.io.FileReader;
/*   15:     */ import java.io.FileWriter;
/*   16:     */ import java.io.InputStream;
/*   17:     */ import java.io.InputStreamReader;
/*   18:     */ import java.io.PrintStream;
/*   19:     */ import java.io.StringReader;
/*   20:     */ import java.io.StringWriter;
/*   21:     */ import java.io.UnsupportedEncodingException;
/*   22:     */ import java.sql.PreparedStatement;
/*   23:     */ import java.sql.ResultSet;
/*   24:     */ import java.sql.ResultSetMetaData;
/*   25:     */ import java.sql.SQLException;
/*   26:     */ import java.text.DateFormat;
/*   27:     */ import java.text.SimpleDateFormat;
/*   28:     */ import java.util.Calendar;
/*   29:     */ import java.util.Date;
/*   30:     */ import java.util.Enumeration;
/*   31:     */ import java.util.Hashtable;
/*   32:     */ import java.util.Properties;
/*   33:     */ import java.util.StringTokenizer;
/*   34:     */ import java.util.Vector;
/*   35:     */ import net.xoetrope.data.XDataSource;
/*   36:     */ import net.xoetrope.optional.data.sql.CachedDatabaseTable;
/*   37:     */ import net.xoetrope.optional.data.sql.ConnectionManager;
/*   38:     */ import net.xoetrope.optional.data.sql.ConnectionObject;
/*   39:     */ import net.xoetrope.optional.data.sql.DatabaseTableModel;
/*   40:     */ import net.xoetrope.optional.data.sql.NamedConnectionManager;
/*   41:     */ import net.xoetrope.xml.XmlElement;
/*   42:     */ import net.xoetrope.xml.XmlSource;
/*   43:     */ import net.xoetrope.xui.XProject;
/*   44:     */ import net.xoetrope.xui.XProjectManager;
/*   45:     */ import net.xoetrope.xui.data.XBaseModel;
/*   46:     */ import net.xoetrope.xui.data.XModel;
/*   47:     */ import org.apache.log4j.Logger;
/*   48:     */ import org.springframework.context.ApplicationContext;
/*   49:     */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*   50:     */ 
/*   51:     */ public class TestXUIDB
/*   52:     */   implements DataHandler, TaskHandler, XUIDB
/*   53:     */ {
/*   54:  54 */   static String workload = null;
/*   55:  56 */   Logger logger = Logger.getLogger(getClass().getName());
/*   56:  58 */   public static String driver = "com.mysql.jdbc.Driver";
/*   57:  60 */   public static String user = "root";
/*   58:  61 */   public static String passwd = "";
/*   59:  62 */   public static String confStatus = "error";
/*   60:  63 */   public static String imagePath = "";
/*   61:  64 */   public static String dwdb = "";
/*   62:  65 */   public static boolean logImport = false;
/*   63:  66 */   public static ApplicationContext appContext = null;
/*   64:  67 */   public static String url = "jdbc:mysql://localhost:3306/vatest";
/*   65:     */   
/*   66:     */   static
/*   67:     */   {
/*   68:  70 */     InputStream is = null;
/*   69:     */     try
/*   70:     */     {
/*   71:     */       try
/*   72:     */       {
/*   73:  75 */         appContext = new ClassPathXmlApplicationContext("appContext.xml");
/*   74:     */         
/*   75:  77 */         Properties p = (Properties)appContext.getBean("db");
/*   76:     */         
/*   77:  79 */         driver = p.getProperty("driver", driver);
/*   78:  80 */         url = p.getProperty("dburl");
/*   79:  81 */         user = p.getProperty("user");
/*   80:  82 */         passwd = p.getProperty("passwd");
/*   81:  83 */         imagePath = p.getProperty("imagePath");
/*   82:  84 */         dwdb = p.getProperty("dwdb");
/*   83:  85 */         confStatus = "ok";
/*   84:     */         
/*   85:  87 */         logImport = p.getProperty("logImport") == null ? false : Boolean.parseBoolean(p.getProperty("logImport"));
/*   86:  88 */         workload = p.getProperty("workload");
/*   87:     */       }
/*   88:     */       catch (Exception e)
/*   89:     */       {
/*   90:  92 */         e.printStackTrace();
/*   91:     */       }
/*   92:  95 */       Class.forName(driver);
/*   93:  96 */       NamedConnectionManager nc = (NamedConnectionManager)NamedConnectionManager.getInstance();
/*   94:     */       
/*   95:  98 */       nc.reset("test", driver, url, user, passwd);
/*   96:     */     }
/*   97:     */     catch (Exception e)
/*   98:     */     {
/*   99: 102 */       e.printStackTrace();
/*  100:     */     }
/*  101:     */   }
/*  102:     */   
/*  103:     */   public String getProperty(String property)
/*  104:     */   {
/*  105: 108 */     String value = null;
/*  106: 109 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  107: 110 */     String sql = "select value from configuration where property='" + property + "'";
/*  108: 111 */     this.logger.info("sql::" + sql);
/*  109:     */     
/*  110: 113 */     dt.setSqlStatement(sql, "test", false);
/*  111: 114 */     dt.retrieve();
/*  112: 115 */     int n = dt.getNumChildren();
/*  113: 116 */     this.logger.info("No of Rows:" + n);
/*  114: 117 */     XModel xm = new XBaseModel();
/*  115: 118 */     if (n > 0)
/*  116:     */     {
/*  117: 119 */       XModel row = (XModel)xm.get("0");
/*  118: 120 */       for (int i = 0; i < dt.getNumAttributes(); i++) {
/*  119: 121 */         value = dt.get(0).getAttribValueAsString(0);
/*  120:     */       }
/*  121:     */     }
/*  122: 125 */     return value;
/*  123:     */   }
/*  124:     */   
/*  125:     */   public void updateCODReport(String phys1, String phys2, String adjudicator, String report)
/*  126:     */     throws Exception
/*  127:     */   {
/*  128: 131 */     String finalIcd = "";
/*  129: 132 */     XModel xm = new XBaseModel();
/*  130: 133 */     String path = "/cme/" + report;
/*  131:     */     
/*  132: 135 */     String codPath1 = path + "/Coding/" + phys1 + "/icd";
/*  133: 136 */     String reconPath1 = path + "/Reconciliation/" + phys1 + "/icd";
/*  134: 137 */     String codPath2 = path + "/Coding/" + phys2 + "/icd";
/*  135: 138 */     String reconPath2 = path + "/Reconciliation/" + phys2 + "/icd";
/*  136: 139 */     String adjPath = path + "/Adjudication/" + adjudicator + "/icd";
/*  137:     */     
/*  138: 141 */     String keywordPath1 = path + "/Coding/Comments/" + phys1;
/*  139: 142 */     String keywordPath2 = path + "/Coding/Comments/" + phys2;
/*  140: 143 */     String keywordPath3 = path + "/Coding/Comments/" + adjudicator;
/*  141:     */     
/*  142: 145 */     String cod1 = getInstance().getValue("keyvalue", codPath1);
/*  143: 146 */     String recon1 = getInstance().getValue("keyvalue", reconPath1);
/*  144: 147 */     String cod2 = getInstance().getValue("keyvalue", codPath2);
/*  145: 148 */     String recon2 = getInstance().getValue("keyvalue", reconPath2);
/*  146: 149 */     String adj = getInstance().getValue("keyvalue", adjPath);
/*  147: 150 */     StringBuffer codingKeyword1 = new StringBuffer();
/*  148: 151 */     StringBuffer codingKeyword2 = new StringBuffer();
/*  149: 152 */     StringBuffer reconKeyword1 = new StringBuffer();
/*  150: 153 */     StringBuffer reconKeyword2 = new StringBuffer();
/*  151: 154 */     StringBuffer adjudicationKeyword = new StringBuffer();
/*  152:     */     
/*  153: 156 */     XModel dataM = new XBaseModel();
/*  154: 157 */     dataM.setId(report);
/*  155: 158 */     ((XModel)dataM.get("physician1")).set(phys1);
/*  156: 159 */     ((XModel)dataM.get("physician2")).set(phys2);
/*  157: 160 */     if ((adjudicator != null) && (!adjudicator.trim().equals(""))) {
/*  158: 161 */       ((XModel)dataM.get("adjudicator")).set(adjudicator);
/*  159:     */     }
/*  160: 163 */     ((XModel)dataM.get("coding_icd1")).set(cod1);
/*  161: 164 */     ((XModel)dataM.get("coding_icd2")).set(cod2);
/*  162: 165 */     finalIcd = cod2;
/*  163: 166 */     if ((recon1 != null) && (!recon1.trim().equals(""))) {
/*  164: 167 */       ((XModel)dataM.get("reconciliation_icd1")).set(recon1);
/*  165:     */     }
/*  166: 169 */     if ((recon2 != null) && (!recon2.trim().equals("")))
/*  167:     */     {
/*  168: 170 */       ((XModel)dataM.get("reconciliation_icd2")).set(recon2);
/*  169: 171 */       finalIcd = recon2;
/*  170:     */     }
/*  171: 173 */     if ((adj != null) && (!adj.trim().equals("")))
/*  172:     */     {
/*  173: 174 */       ((XModel)dataM.get("adjudication_icd")).set(adj);
/*  174: 175 */       finalIcd = adj;
/*  175:     */     }
/*  176: 177 */     ((XModel)dataM.get("uniqno")).set(report);
/*  177:     */     
/*  178: 179 */     ((XModel)dataM.get("coding_keyword1")).set(codingKeyword1);
/*  179: 180 */     ((XModel)dataM.get("coding_keyword2")).set(codingKeyword2);
/*  180: 181 */     ((XModel)dataM.get("reconciliation_keyword1")).set(reconKeyword1);
/*  181: 182 */     ((XModel)dataM.get("reconciliation_keyword2")).set(reconKeyword2);
/*  182: 183 */     ((XModel)dataM.get("adjudication_keyword")).set(adjudicationKeyword);
/*  183:     */     
/*  184: 185 */     getInstance().saveData("cme_report", "uniqno='" + report + "' and physician1='" + phys1 + "' and physician2='" + phys2 + "'", dataM);
/*  185:     */   }
/*  186:     */   
/*  187:     */   public String getICDDesc(String icd)
/*  188:     */   {
/*  189: 190 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  190: 191 */     dt.setupTable("icd", "description", "icd='" + icd + "'", "test", false);
/*  191: 192 */     dt.retrieve();
/*  192:     */     
/*  193: 194 */     this.logger.info(Integer.valueOf(dt.getNumChildren()));
/*  194: 195 */     int i = 0;
/*  195: 195 */     if (i < dt.getNumChildren())
/*  196:     */     {
/*  197: 197 */       String desc = dt.get(i).get("description").toString();
/*  198: 198 */       return desc;
/*  199:     */     }
/*  200: 201 */     return null;
/*  201:     */   }
/*  202:     */   
/*  203:     */   public boolean isValidIcdAge(String age, String icd)
/*  204:     */   {
/*  205: 206 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  206: 207 */     dt.setupTable("icd_exclusions_age", "*", "icd='" + icd + "'", "test", false);
/*  207: 208 */     dt.retrieve();
/*  208:     */     
/*  209: 210 */     this.logger.info(Integer.valueOf(dt.getNumChildren()));
/*  210: 211 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  211:     */     {
/*  212: 213 */       String ageRange1 = dt.get(i).get("age").toString();
/*  213: 214 */       String[] ageRange = ageRange1.split("-");
/*  214: 215 */       double min = Double.parseDouble(ageRange[0]);
/*  215: 216 */       double max = Double.parseDouble(ageRange[1]);
/*  216: 217 */       double ageInYears = Double.parseDouble(age);
/*  217: 218 */       if ((ageInYears >= min) && (ageInYears <= max)) {
/*  218: 219 */         return false;
/*  219:     */       }
/*  220:     */     }
/*  221: 224 */     return true;
/*  222:     */   }
/*  223:     */   
/*  224:     */   public boolean isValidIcdSex(String age, String icd)
/*  225:     */   {
/*  226: 229 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  227: 230 */     dt.setupTable("icd_exclusions_sex", "*", "icd='" + icd + "'", "test", false);
/*  228: 231 */     dt.retrieve();
/*  229:     */     
/*  230: 233 */     this.logger.info(Integer.valueOf(dt.getNumChildren()));
/*  231: 235 */     if (dt.getNumChildren() > 0) {
/*  232: 236 */       return false;
/*  233:     */     }
/*  234: 239 */     return true;
/*  235:     */   }
/*  236:     */   
/*  237:     */   public boolean checkEquivalence(String icd1, String icd2)
/*  238:     */   {
/*  239: 244 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  240: 245 */     dt.setupTable("icd_equivalent_codes", "*", "icdEquivalence LIKE '%" + icd1 + "%' AND icdEquivalence LIKE '%" + icd2 + "%'", "test", false);
/*  241: 246 */     dt.retrieve();
/*  242: 248 */     if (dt.getNumChildren() > 0) {
/*  243: 249 */       return true;
/*  244:     */     }
/*  245: 252 */     return false;
/*  246:     */   }
/*  247:     */   
/*  248:     */   public static TestXUIDB getInstance()
/*  249:     */   {
/*  250: 257 */     TestXUIDB tt = new TestXUIDB();
/*  251: 258 */     tt.init();
/*  252:     */     
/*  253: 260 */     return tt;
/*  254:     */   }
/*  255:     */   
/*  256:     */   public String getImagePath()
/*  257:     */   {
/*  258: 265 */     return imagePath;
/*  259:     */   }
/*  260:     */   
/*  261:     */   public void getReportData(String path, XModel dataM, int noOfColumns)
/*  262:     */   {
/*  263: 270 */     XModel dtm = new XBaseModel();
/*  264: 271 */     readTree(dtm, "keyvalue", path);
/*  265: 272 */     int count = 0;
/*  266: 273 */     for (int i = 0; i < dtm.getNumChildren(); i += noOfColumns)
/*  267:     */     {
/*  268: 275 */       XBaseModel xm = new XBaseModel();
/*  269: 276 */       xm.setTagName("tr");
/*  270: 277 */       for (int j = 0; j < noOfColumns; j++)
/*  271:     */       {
/*  272: 279 */         xm.setId(i + j);
/*  273: 280 */         XBaseModel desc = new XBaseModel();
/*  274: 281 */         desc.setId("label" + j);
/*  275: 282 */         desc.set(dtm.get(i + j).getId());
/*  276: 283 */         XBaseModel icd = new XBaseModel();
/*  277: 284 */         icd.setId("value" + j);
/*  278: 285 */         this.logger.info(dtm.get(i + j).get());
/*  279: 286 */         icd.set(dtm.get(i + j).get());
/*  280: 287 */         xm.append(desc);
/*  281: 288 */         xm.append(icd);
/*  282: 289 */         count++;
/*  283: 290 */         if (count >= dtm.getNumChildren()) {
/*  284:     */           break;
/*  285:     */         }
/*  286:     */       }
/*  287: 296 */       dataM.append(xm);
/*  288:     */     }
/*  289:     */   }
/*  290:     */   
/*  291:     */   public void getDiffDiagnosis(String icdCode, XModel dataM)
/*  292:     */   {
/*  293: 302 */     String sql = "SELECT  description,diffdiagnosis FROM icd_diff where icd='" + icdCode + "'";
/*  294: 303 */     this.logger.info(sql);
/*  295:     */     
/*  296: 305 */     DatabaseTableModel dtm = new DatabaseTableModel();
/*  297:     */     
/*  298: 307 */     dtm.setSqlStatement(sql, "test", false);
/*  299: 308 */     dtm.retrieve();
/*  300: 310 */     for (int i = 0; i < dtm.getNumChildren(); i++)
/*  301:     */     {
/*  302: 312 */       XBaseModel xm = new XBaseModel();
/*  303: 313 */       xm.setTagName("tr");
/*  304: 314 */       xm.setId(i);
/*  305: 315 */       XBaseModel desc = new XBaseModel();
/*  306: 316 */       desc.setId("description");
/*  307:     */       
/*  308: 318 */       this.logger.info(dtm.get(i).get("diffdiagnosis").toString());
/*  309:     */       
/*  310: 320 */       desc.set(dtm.get(i).get("diffdiagnosis").toString() + ":  " + dtm.get(i).get("description").toString());
/*  311:     */       
/*  312: 322 */       xm.append(desc);
/*  313:     */       
/*  314: 324 */       dataM.append(xm);
/*  315:     */     }
/*  316:     */   }
/*  317:     */   
/*  318:     */   public void getSearch(String searchKey, String icdKey, XModel dataM)
/*  319:     */   {
/*  320: 330 */     String sql = "SELECT DISTINCT a.ICD,DESCRIPTION FROM icd_synonyms1 a LEFT JOIN icd_family b ON a.icd=b.icd WHERE synonyms like '%" + searchKey + "%' OR a.icd LIKE '" + icdKey + "%'";
/*  321: 331 */     DatabaseTableModel dtm = new DatabaseTableModel();
/*  322: 332 */     dtm.setSqlStatement(sql, "test", false);
/*  323: 333 */     dtm.retrieve();
/*  324: 334 */     for (int i = 0; i < dtm.getNumChildren(); i++)
/*  325:     */     {
/*  326: 336 */       XBaseModel xm = new XBaseModel();
/*  327: 337 */       xm.setTagName("tr");
/*  328: 338 */       xm.setId(i);
/*  329: 339 */       XBaseModel icd = new XBaseModel();
/*  330: 340 */       icd.setId("icd");
/*  331: 341 */       this.logger.info(dtm.get(i).get("ICD").toString());
/*  332: 342 */       icd.set(dtm.get(i).get("ICD").toString());
/*  333: 343 */       XBaseModel desc = new XBaseModel();
/*  334: 344 */       desc.setId("description");
/*  335: 345 */       desc.set(dtm.get(i).get("DESCRIPTION").toString());
/*  336:     */       
/*  337: 347 */       xm.append(icd);
/*  338: 348 */       xm.append(desc);
/*  339: 349 */       dataM.append(xm);
/*  340:     */     }
/*  341:     */   }
/*  342:     */   
/*  343:     */   public void getSearchIcd(String searchKey, XModel dataM)
/*  344:     */   {
/*  345: 355 */     String sql = "SELECT  distinct a.icd,a.description FROM icd a LEFT JOIN icd_synony ms1 b ON a.icd=b.icd WHERE IF('" + searchKey + "' REGEXP '^[A-Z||a-z][0-9]{0,2}$' , a.icd LIKE CONCAT('" + searchKey + "','%'),IF(LENGTH('" + searchKey + "') >2 and '" + searchKey + "'!='and' , description LIKE CONCAT('%','" + searchKey + "','%') OR synonyms LIKE CONCAT('%','" + searchKey + "','%'),FALSE))";
/*  346: 356 */     DatabaseTableModel dtm = new DatabaseTableModel();
/*  347: 357 */     dtm.setSqlStatement(sql, "test", false);
/*  348: 358 */     dtm.retrieve();
/*  349: 359 */     for (int i = 0; i < dtm.getNumChildren(); i++)
/*  350:     */     {
/*  351: 361 */       XBaseModel xm = new XBaseModel();
/*  352: 362 */       xm.setTagName("tr");
/*  353: 363 */       xm.setId(i);
/*  354: 364 */       XBaseModel icd = new XBaseModel();
/*  355: 365 */       icd.setId("icd");
/*  356: 366 */       this.logger.info(dtm.get(i).get("icd").toString());
/*  357: 367 */       icd.set(dtm.get(i).get("icd").toString());
/*  358: 368 */       XBaseModel desc = new XBaseModel();
/*  359: 369 */       desc.setId("description");
/*  360: 370 */       desc.set(dtm.get(i).get("description").toString());
/*  361:     */       
/*  362: 372 */       xm.append(icd);
/*  363: 373 */       xm.append(desc);
/*  364: 374 */       dataM.append(xm);
/*  365:     */     }
/*  366:     */   }
/*  367:     */   
/*  368:     */   public void getSearch(String searchKey, XModel dataM)
/*  369:     */   {
/*  370: 380 */     String sql = "SELECT DISTINCT a.ICD,DESCRIPTION FROM icd_synonyms1 a LEFT JOIN icd_family b ON a.icd=b.icd WHERE synonyms like '%" + searchKey + "%'";
/*  371: 381 */     DatabaseTableModel dtm = new DatabaseTableModel();
/*  372: 382 */     dtm.setSqlStatement(sql, "test", false);
/*  373: 383 */     dtm.retrieve();
/*  374: 384 */     for (int i = 0; i < dtm.getNumChildren(); i++)
/*  375:     */     {
/*  376: 386 */       XBaseModel xm = new XBaseModel();
/*  377: 387 */       xm.setTagName("tr");
/*  378: 388 */       xm.setId(i);
/*  379: 389 */       XBaseModel icd = new XBaseModel();
/*  380: 390 */       icd.setId("icd");
/*  381: 391 */       this.logger.info(dtm.get(i).get("ICD").toString());
/*  382: 392 */       icd.set(dtm.get(i).get("ICD").toString());
/*  383: 393 */       XBaseModel desc = new XBaseModel();
/*  384: 394 */       desc.setId("description");
/*  385: 395 */       desc.set(dtm.get(i).get("DESCRIPTION").toString());
/*  386:     */       
/*  387: 397 */       xm.append(icd);
/*  388: 398 */       xm.append(desc);
/*  389: 399 */       dataM.append(xm);
/*  390:     */     }
/*  391:     */   }
/*  392:     */   
/*  393:     */   public Vector findPhysicians(String vaId)
/*  394:     */   {
/*  395: 405 */     String language = getValue("keyvalue", "/va/" + vaId + "/gi/Language");
/*  396: 406 */     this.logger.info("language " + language);
/*  397:     */     
/*  398: 408 */     String sql = "SELECT DISTINCT a.physician physician from  physician_language a  LEFT JOIN physician_workload b ON a.physician= b.physician WHERE b.workload < 11 ";
/*  399: 409 */     DatabaseTableModel dtm = new DatabaseTableModel();
/*  400: 410 */     dtm.setSqlStatement(sql, "test", false);
/*  401: 411 */     dtm.retrieve();
/*  402: 412 */     Vector ind = new Vector();
/*  403: 413 */     for (int i = 0; i < dtm.getNumChildren(); i++)
/*  404:     */     {
/*  405: 415 */       String phy = dtm.get(i).get("physician").toString();
/*  406: 416 */       this.logger.info(phy);
/*  407: 417 */       ind.add(dtm.get(i).get("physician").toString());
/*  408:     */     }
/*  409: 420 */     return ind;
/*  410:     */   }
/*  411:     */   
/*  412:     */   public void saveProcess(Process p)
/*  413:     */   {
/*  414:     */     try
/*  415:     */     {
/*  416: 427 */       String where = " pid ='" + p.pid + "'";
/*  417: 428 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  418: 429 */       dt.setupTable("process", "*", where, "test", false);
/*  419:     */       
/*  420: 431 */       dt.retrieve();
/*  421: 432 */       String s = new String("");
/*  422: 433 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  423: 434 */       String st = "'" + sdf.format(p.startTime) + "'";
/*  424: 435 */       String et = "'" + sdf.format(p.endTime) + "'";
/*  425: 436 */       this.logger.info(Integer.valueOf(dt.getNumChildren()));
/*  426: 438 */       if (dt.getNumChildren() > 0) {
/*  427: 440 */         s = "update process set status='" + p.status + "',startTime=" + st + ",endTime=" + et + ", stateMachine='" + p.states + "' where" + where;
/*  428:     */       } else {
/*  429: 443 */         s = "insert into process (pid,status,startTime,endTime,stateMachine) Values('" + p.pid + "','" + p.status + "'," + st + "," + st + ",'" + p.states + "')";
/*  430:     */       }
/*  431: 444 */       this.logger.info(">>>>" + s);
/*  432: 445 */       dt.executeUpdate(s);
/*  433:     */     }
/*  434:     */     catch (Exception e)
/*  435:     */     {
/*  436: 449 */       e.printStackTrace();
/*  437:     */     }
/*  438:     */   }
/*  439:     */   
/*  440:     */   public void saveProcess(Process p, String stateMachineClassName)
/*  441:     */   {
/*  442:     */     try
/*  443:     */     {
/*  444: 457 */       String where = " pid ='" + p.pid + "'";
/*  445: 458 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  446: 459 */       dt.setupTable("process", "*", where, "test", false);
/*  447:     */       
/*  448: 461 */       dt.retrieve();
/*  449: 462 */       String s = new String("");
/*  450: 463 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  451: 464 */       String st = "'" + sdf.format(p.startTime) + "'";
/*  452: 465 */       String et = "'" + sdf.format(p.endTime) + "'";
/*  453: 466 */       this.logger.info(Integer.valueOf(dt.getNumChildren()));
/*  454: 468 */       if (dt.getNumChildren() > 0) {
/*  455: 470 */         s = "update process set status='" + p.status + "',startTime=" + st + ",endTime=" + et + ", stateMachine='" + p.states + "',stateMachineClass='" + stateMachineClassName + "' where" + where;
/*  456:     */       } else {
/*  457: 473 */         s = "insert into process (pid,status,startTime,endTime,stateMachine,stateMachineClass) Values('" + p.pid + "','" + p.status + "'," + st + "," + st + ",'" + p.states + "','" + p.stateMachineClass + "')";
/*  458:     */       }
/*  459: 474 */       this.logger.info(">>>>" + s);
/*  460:     */       
/*  461: 476 */       dt.executeUpdate(s);
/*  462:     */     }
/*  463:     */     catch (Exception e)
/*  464:     */     {
/*  465: 480 */       e.printStackTrace();
/*  466:     */     }
/*  467:     */   }
/*  468:     */   
/*  469:     */   public void execute(String table, String where, String sql)
/*  470:     */     throws Exception
/*  471:     */   {
/*  472: 487 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  473:     */     
/*  474: 489 */     dt.setupTable(table);
/*  475: 490 */     dt.executeUpdate(sql);
/*  476:     */   }
/*  477:     */   
/*  478:     */   public void saveTransition(String id, String pid, int status)
/*  479:     */   {
/*  480:     */     try
/*  481:     */     {
/*  482: 497 */       String where = " id ='" + id + "'";
/*  483: 498 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  484: 499 */       dt.setupTable("transitions", "*", where, "test", false);
/*  485:     */       
/*  486: 501 */       dt.retrieve();
/*  487: 502 */       String s = new String("");
/*  488: 503 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  489:     */       
/*  490: 505 */       this.logger.info(Integer.valueOf(dt.getNumChildren()));
/*  491: 507 */       if (dt.getNumChildren() > 0) {
/*  492: 509 */         s = "update transitions set pid='" + pid + "',status=" + status + " where" + where;
/*  493:     */       } else {
/*  494: 512 */         s = "insert into transitions (pid,status) Values('" + pid + "','" + status + "')";
/*  495:     */       }
/*  496: 513 */       this.logger.info(">>>>" + s);
/*  497:     */       
/*  498: 515 */       dt.executeUpdate(s);
/*  499:     */     }
/*  500:     */     catch (Exception e)
/*  501:     */     {
/*  502: 519 */       e.printStackTrace();
/*  503:     */     }
/*  504:     */   }
/*  505:     */   
/*  506:     */   public String[] getNextTransition()
/*  507:     */     throws Exception
/*  508:     */   {
/*  509: 526 */     String where = " status =0";
/*  510: 527 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  511:     */     
/*  512: 529 */     dt.setupTable("transitions", "*", where, "test", false);
/*  513: 530 */     dt.retrieve();
/*  514: 531 */     Process p = null;
/*  515: 532 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/*  516: 533 */     this.logger.info(" NO of transitions" + dt.getNumChildren());
/*  517: 534 */     if (dt.getNumChildren() > 0)
/*  518:     */     {
/*  519: 536 */       String[] ret = new String[2];
/*  520: 537 */       ret[0] = dt.get(0).get("id").toString();
/*  521: 538 */       ret[1] = dt.get(0).get("pid").toString();
/*  522: 539 */       return ret;
/*  523:     */     }
/*  524: 542 */     return null;
/*  525:     */   }
/*  526:     */   
/*  527:     */   public void logAgent(String pid, String agent, String currentState, String message)
/*  528:     */   {
/*  529:     */     try
/*  530:     */     {
/*  531: 549 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  532:     */       
/*  533: 551 */       dt.setupTable("agentlog", "*", "", "test", false);
/*  534:     */       
/*  535: 553 */       String s = new String("");
/*  536: 554 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  537:     */       
/*  538: 556 */       s = "insert into agentlog (pid,agent,state,message) Values('" + pid + "','" + agent + "','" + currentState + "','" + message + "')";
/*  539: 557 */       this.logger.info(">>>>" + s);
/*  540: 558 */       dt.executeUpdate(s);
/*  541:     */     }
/*  542:     */     catch (Exception e)
/*  543:     */     {
/*  544: 562 */       e.printStackTrace();
/*  545:     */     }
/*  546:     */   }
/*  547:     */   
/*  548:     */   public Process getProcess(String pid)
/*  549:     */     throws Exception
/*  550:     */   {
/*  551: 569 */     String where = " pid ='" + pid + "'";
/*  552: 570 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  553:     */     
/*  554: 572 */     dt.setupTable("process", "*", where, "test", false);
/*  555: 573 */     dt.retrieve();
/*  556: 574 */     Process p = null;
/*  557: 575 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/*  558: 576 */     if (dt.getNumChildren() > 0)
/*  559:     */     {
/*  560: 578 */       p = new Process();
/*  561: 579 */       p.pid = pid;
/*  562: 580 */       p.status = dt.get(0).get("status").toString();
/*  563: 581 */       Object startTime = dt.get(0).get("startTime");
/*  564: 582 */       Object endTime = dt.get(0).get("startTime");
/*  565: 583 */       Object stateMachineClass = null;
/*  566:     */       try
/*  567:     */       {
/*  568: 585 */         this.logger.info(dt.get(0).get("stateMachineClass"));
/*  569: 586 */         stateMachineClass = dt.get(0).get("stateMachineClass");
/*  570:     */       }
/*  571:     */       catch (Exception e)
/*  572:     */       {
/*  573: 590 */         e.printStackTrace();
/*  574:     */       }
/*  575: 592 */       this.logger.info(dt.get(0).get("startTime"));
/*  576: 593 */       this.logger.info(dt.get(0).get("endTime"));
/*  577: 595 */       if (startTime != null) {
/*  578: 596 */         p.startTime = sdf.parse(startTime.toString());
/*  579:     */       }
/*  580: 597 */       if (endTime != null) {
/*  581: 598 */         p.endTime = sdf.parse(endTime.toString());
/*  582:     */       }
/*  583: 599 */       if (stateMachineClass != null) {
/*  584: 600 */         p.stateMachineClass = stateMachineClass.toString();
/*  585:     */       }
/*  586: 602 */       this.logger.info(p.status);
/*  587: 603 */       this.logger.info(p.startTime);
/*  588: 604 */       this.logger.info(p.stateMachineClass);
/*  589: 605 */       p.states = Process.createStateMachine(p.stateMachineClass);
/*  590: 606 */       p.states.deserialize(((XModel)dt.get(0).get("stateMachine")).get().toString());
/*  591: 607 */       this.logger.info(p.states.getCurrentState());
/*  592: 608 */       this.logger.info(p.states);
/*  593:     */     }
/*  594: 611 */     return p;
/*  595:     */   }
/*  596:     */   
/*  597:     */   public XModel getTasks(String surveyType, String team, String taskPath, KenList dataPath)
/*  598:     */   {
/*  599: 616 */     KenList kl = new KenList("area/house/household/member");
/*  600:     */     
/*  601: 618 */     KenList kl1 = null;
/*  602: 619 */     if (kl.size() > dataPath.size()) {
/*  603: 621 */       kl1 = kl.subset(0, dataPath.size() - 1);
/*  604:     */     } else {
/*  605: 625 */       kl1 = kl;
/*  606:     */     }
/*  607: 628 */     String context = kl1.add1("=").add1(dataPath).toString(" and ");
/*  608: 629 */     String where = "task like '%definition/" + taskPath + "' and assignedto='" + team + "' " + (context.length() > 0 ? "and " + context : "") + " and survey_type='" + surveyType + "' and (duedate is null or duedate >= Now())";
/*  609: 630 */     this.logger.info(" Where " + where);
/*  610: 631 */     XModel dataM = new XBaseModel();
/*  611: 632 */     getTaskData(where, dataM);
/*  612: 633 */     return dataM;
/*  613:     */   }
/*  614:     */   
/*  615:     */   public XModel getTasks1(String surveyType, String team, String taskPath, KenList dataPath)
/*  616:     */   {
/*  617: 638 */     KenList kl = new KenList("area/house/household/member");
/*  618:     */     
/*  619: 640 */     KenList kl1 = null;
/*  620: 641 */     if (kl.size() > dataPath.size()) {
/*  621: 643 */       kl1 = kl.subset(0, dataPath.size() - 1);
/*  622:     */     } else {
/*  623: 647 */       kl1 = kl;
/*  624:     */     }
/*  625: 650 */     String context = kl1.add1("=").add1(dataPath).toString(" and ", "'");
/*  626: 651 */     String defPath = "taskdefinitions/" + ((taskPath != null) && (!taskPath.equals("")) ? surveyType + "_taskdefinition/" + taskPath : new StringBuilder().append(surveyType).append("_taskdefinition/%").toString());
/*  627:     */     
/*  628: 653 */     String where = "task like '" + defPath + "/%' and task not like '" + defPath + "/%/%' and assignedto='" + team + "' " + (context.length() > 0 ? "and " + context : "") + " and (status  ='0' or status is null)";
/*  629: 654 */     this.logger.info(" Where " + where);
/*  630: 655 */     Logger logger = Logger.getLogger(getClass());
/*  631: 656 */     logger.info(" get Tasks1 " + where);
/*  632: 657 */     XModel dataM = new XBaseModel();
/*  633: 658 */     getTaskData(where, dataM);
/*  634: 659 */     return dataM;
/*  635:     */   }
/*  636:     */   
/*  637:     */   public void getTasks(XModel ioM, String team, String participant)
/*  638:     */     throws Exception
/*  639:     */   {
/*  640: 665 */     String parent = "taskdefinitions/healthcheckup_taskdefinition";
/*  641:     */     
/*  642: 667 */     testP(parent, team, ioM);
/*  643:     */   }
/*  644:     */   
/*  645:     */   public void checkDBServer()
/*  646:     */     throws Exception
/*  647:     */   {
/*  648: 673 */     NamedConnectionManager nc = (NamedConnectionManager)NamedConnectionManager.getInstance();
/*  649: 674 */     ConnectionObject co = nc.getConnection("test");
/*  650:     */   }
/*  651:     */   
/*  652:     */   public String getTaskStatusPath(String task, String surveyType, String area, String house, String household, String individual)
/*  653:     */   {
/*  654: 679 */     StringTokenizer tkzr = new StringTokenizer(task, "/");
/*  655: 680 */     int tokenIndex = 0;
/*  656: 681 */     String outputPath = "";
/*  657: 682 */     String tkn = "";
/*  658: 683 */     while (tkzr.hasMoreTokens())
/*  659:     */     {
/*  660: 685 */       tkn = tkzr.nextToken();
/*  661: 686 */       switch (tokenIndex)
/*  662:     */       {
/*  663:     */       case 0: 
/*  664: 689 */         outputPath = outputPath + "survey";
/*  665: 690 */         break;
/*  666:     */       case 1: 
/*  667: 692 */         outputPath = outputPath + surveyType + "/tasks";
/*  668: 693 */         break;
/*  669:     */       case 3: 
/*  670: 695 */         outputPath = outputPath + tkn + "-" + area;
/*  671: 696 */         break;
/*  672:     */       case 4: 
/*  673: 698 */         outputPath = outputPath + tkn + "-" + house;
/*  674: 699 */         break;
/*  675:     */       case 5: 
/*  676: 701 */         outputPath = outputPath + tkn + "-" + household;
/*  677: 702 */         break;
/*  678:     */       case 6: 
/*  679: 704 */         if ((individual == null) || (individual.equals(""))) {
/*  680: 705 */           outputPath = outputPath + tkn;
/*  681:     */         } else {
/*  682: 707 */           outputPath = outputPath + tkn + "-" + individual;
/*  683:     */         }
/*  684: 708 */         break;
/*  685:     */       case 2: 
/*  686:     */       default: 
/*  687: 711 */         outputPath = outputPath + tkn;
/*  688:     */       }
/*  689: 713 */       outputPath = outputPath + "/";
/*  690: 714 */       tokenIndex++;
/*  691:     */     }
/*  692: 716 */     outputPath = outputPath.substring(0, outputPath.length() - 1);
/*  693: 717 */     this.logger.info("Output path for task '=" + task + "'=" + outputPath);
/*  694: 718 */     return outputPath;
/*  695:     */   }
/*  696:     */   
/*  697:     */   public int getTaskChildren(String parentPath, String team)
/*  698:     */   {
/*  699: 723 */     String taskStr = parentPath;
/*  700:     */     
/*  701: 725 */     this.logger.info(taskStr);
/*  702:     */     try
/*  703:     */     {
/*  704: 728 */       String where = "assignedto=" + team + " and  survey_type='2'  and task LIKE '" + taskStr + "/%' and  task NOT LIKE '" + taskStr + "/%/%' ";
/*  705: 729 */       this.logger.info(where);
/*  706: 730 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  707: 731 */       dt.setupTable("tasks", "count(*) count1", where, "test", false);
/*  708:     */       
/*  709: 733 */       dt.retrieve();
/*  710: 734 */       XModel row = dt.get(0);
/*  711: 735 */       return Integer.parseInt(row.get("count1").toString());
/*  712:     */     }
/*  713:     */     catch (Exception e)
/*  714:     */     {
/*  715: 739 */       e.printStackTrace();
/*  716:     */     }
/*  717: 742 */     return 0;
/*  718:     */   }
/*  719:     */   
/*  720:     */   public XLogisticsModel getLogisticsM(String table, String name, String path)
/*  721:     */   {
/*  722: 747 */     String context = "and path='" + path + "'";
/*  723:     */     try
/*  724:     */     {
/*  725: 750 */       String where = "name='" + name + "' " + context;
/*  726: 751 */       this.logger.info(where);
/*  727: 752 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  728: 753 */       dt.setupTable(table, "*", where, "test", false);
/*  729:     */       
/*  730: 755 */       dt.retrieve();
/*  731:     */       
/*  732: 757 */       XLogisticsModel dataM = new XLogisticsModel();
/*  733: 758 */       if (dt.getNumChildren() == 0)
/*  734:     */       {
/*  735: 760 */         dataM.setId(name);
/*  736: 761 */         dataM.set("@path", path);
/*  737:     */         
/*  738: 763 */         return dataM;
/*  739:     */       }
/*  740: 766 */       XModel row = dt.get(0);
/*  741: 768 */       for (int j = 0; j < dt.getNumAttributes(); j++) {
/*  742: 770 */         if (!dt.getAttribName(j).equals("id")) {
/*  743: 772 */           dataM.set("@" + dt.getAttribName(j), row.get(j).get());
/*  744:     */         }
/*  745:     */       }
/*  746: 775 */       this.logger.info(" >>> " + dataM.get("@name"));
/*  747:     */       
/*  748: 777 */       dataM.setId(name);
/*  749: 778 */       return dataM;
/*  750:     */     }
/*  751:     */     catch (Exception e)
/*  752:     */     {
/*  753: 782 */       e.printStackTrace();
/*  754:     */     }
/*  755: 784 */     return null;
/*  756:     */   }
/*  757:     */   
/*  758:     */   public XDataModel getDataM1(String table, String where)
/*  759:     */   {
/*  760:     */     try
/*  761:     */     {
/*  762: 791 */       this.logger.info(where);
/*  763: 792 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  764: 793 */       dt.setupTable(table, "*", where, "test", false);
/*  765:     */       
/*  766: 795 */       dt.retrieve();
/*  767:     */       
/*  768: 797 */       XDataModel dataM = new XDataModel();
/*  769: 799 */       if (dt.getNumChildren() > 0)
/*  770:     */       {
/*  771: 801 */         XModel row = dt.get(0);
/*  772: 803 */         for (int j = 0; j < dt.getNumAttributes(); j++) {
/*  773: 805 */           dataM.set("@" + dt.getAttribName(j), row.get(j).get());
/*  774:     */         }
/*  775: 807 */         this.logger.info(" >>> " + dataM.get("@name"));
/*  776:     */       }
/*  777: 810 */       return dataM;
/*  778:     */     }
/*  779:     */     catch (Exception e)
/*  780:     */     {
/*  781: 814 */       e.printStackTrace();
/*  782:     */     }
/*  783: 816 */     return null;
/*  784:     */   }
/*  785:     */   
/*  786:     */   public XDataModel getDataM2(String table, String where)
/*  787:     */   {
/*  788:     */     try
/*  789:     */     {
/*  790: 823 */       this.logger.info(where);
/*  791: 824 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  792: 825 */       dt.setupTable(table, "*", where, "test", false);
/*  793:     */       
/*  794: 827 */       dt.retrieve();
/*  795:     */       
/*  796: 829 */       XDataModel dataM = new XDataModel();
/*  797: 831 */       for (int i = 0; i < dt.getNumChildren(); i++)
/*  798:     */       {
/*  799: 833 */         XModel row = dt.get(i);
/*  800: 834 */         XModel xm = new XBaseModel();
/*  801: 835 */         xm.setId(i);
/*  802: 837 */         for (int j = 0; j < dt.getNumAttributes(); j++) {
/*  803: 839 */           ((XModel)xm.get(dt.getAttribName(j))).set(row.get(j).get());
/*  804:     */         }
/*  805: 842 */         dataM.append(xm);
/*  806:     */       }
/*  807: 845 */       return dataM;
/*  808:     */     }
/*  809:     */     catch (Exception e)
/*  810:     */     {
/*  811: 849 */       e.printStackTrace();
/*  812:     */     }
/*  813: 851 */     return null;
/*  814:     */   }
/*  815:     */   
/*  816:     */   public XDataModel getDataM(String table, String name, String area, String house, String household, String individual)
/*  817:     */   {
/*  818: 856 */     String context = ((area != null) && (!area.equals("")) && (!area.equals("-1")) ? "and area=" + area : "") + ((house != null) && (!house.equals("")) ? " and house=" + house : "") + ((household != null) && (!household.equals("")) ? " and household=" + household : "") + ((individual != null) && (!individual.equals("")) ? " and member=" + individual : "");
/*  819:     */     try
/*  820:     */     {
/*  821: 859 */       String where = "name='" + name + "' " + context;
/*  822: 860 */       this.logger.info(where);
/*  823: 861 */       DatabaseTableModel dt = new DatabaseTableModel();
/*  824: 862 */       dt.setupTable(table, "*", where, "test", false);
/*  825:     */       
/*  826: 864 */       dt.retrieve();
/*  827:     */       
/*  828: 866 */       XDataModel dataM = new XDataModel();
/*  829: 867 */       if (dt.getNumChildren() == 0)
/*  830:     */       {
/*  831: 869 */         dataM.setId(name);
/*  832: 870 */         dataM.set("@name", name);
/*  833: 871 */         dataM.set("@area", area);
/*  834: 872 */         dataM.set("@house", house);
/*  835: 873 */         dataM.set("@household", household);
/*  836: 874 */         dataM.set("@member", individual);
/*  837: 875 */         return dataM;
/*  838:     */       }
/*  839: 878 */       XModel row = dt.get(0);
/*  840: 880 */       for (int j = 0; j < dt.getNumAttributes(); j++) {
/*  841: 882 */         if (!dt.getAttribName(j).equals("id")) {
/*  842: 884 */           dataM.set("@" + dt.getAttribName(j), row.get(j).get());
/*  843:     */         }
/*  844:     */       }
/*  845: 887 */       this.logger.info(" >>> " + dataM.get("@name"));
/*  846:     */       
/*  847: 889 */       dataM.setId(name);
/*  848: 890 */       return dataM;
/*  849:     */     }
/*  850:     */     catch (Exception e)
/*  851:     */     {
/*  852: 894 */       e.printStackTrace();
/*  853:     */     }
/*  854: 896 */     return null;
/*  855:     */   }
/*  856:     */   
/*  857:     */   public void createMessage()
/*  858:     */     throws Exception
/*  859:     */   {
/*  860: 902 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  861:     */     
/*  862: 904 */     dt.setupTable("changelogs", "value", "", "test", true);
/*  863: 905 */     dt.retrieve();
/*  864:     */     
/*  865: 907 */     FileWriter fw = new FileWriter("c:\\message.xml");
/*  866: 908 */     fw.write("<message>\n");
/*  867: 909 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  868:     */     {
/*  869: 911 */       XModel row = dt.get(i);
/*  870:     */       
/*  871: 913 */       String log = (String)row.get(0).get();
/*  872: 914 */       fw.write(log);
/*  873:     */     }
/*  874: 917 */     fw.write("</message>\n");
/*  875: 918 */     fw.close();
/*  876:     */   }
/*  877:     */   
/*  878:     */   public synchronized String getLastChangeLog()
/*  879:     */     throws Exception
/*  880:     */   {
/*  881: 924 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  882:     */     
/*  883: 926 */     dt.setupTable("changelogs", "max(id) bookmark", "", "test", false);
/*  884: 927 */     dt.retrieve();
/*  885:     */     
/*  886: 929 */     int i = 0;
/*  887: 929 */     if (i < dt.getNumChildren())
/*  888:     */     {
/*  889: 931 */       XModel row = dt.get(i);
/*  890:     */       
/*  891: 933 */       String time = (String)row.get(0).get();
/*  892: 934 */       return time;
/*  893:     */     }
/*  894: 937 */     return null;
/*  895:     */   }
/*  896:     */   
/*  897:     */   public String getPendingChanges()
/*  898:     */     throws Exception
/*  899:     */   {
/*  900: 943 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  901:     */     
/*  902: 945 */     dt.setupTable("changelogs", "SUM(IF(VALUE LIKE \"%table='keyvalue'%\",1,0)) datachanges,SUM(IF(VALUE LIKE \"%table='tasks'%\",1,0)) taskchanges", "status is null", "test", false);
/*  903: 946 */     dt.retrieve();
/*  904: 947 */     String taskchanges = dt.get(0).get("taskchanges").toString();
/*  905: 948 */     taskchanges = (taskchanges == null) || (taskchanges.equals("null")) ? "0" : taskchanges;
/*  906: 949 */     String datachanges = dt.get(0).get("datachanges").toString();
/*  907: 950 */     datachanges = (datachanges == null) || (datachanges.equals("null")) ? "0" : datachanges;
/*  908: 951 */     String msg = taskchanges + " Task and " + datachanges + " Data changes";
/*  909: 952 */     return msg;
/*  910:     */   }
/*  911:     */   
/*  912:     */   public void sendServerLogs(String participant, String recepients, String frombookmark, String tobookmark)
/*  913:     */     throws Exception
/*  914:     */   {
/*  915: 958 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  916: 959 */     this.logger.info("bookmark " + frombookmark);
/*  917: 960 */     dt.setupTable("changelogs", "value", " status is null and id >'" + frombookmark + "' and id <'" + tobookmark + "'", "test", false);
/*  918: 961 */     dt.retrieve();
/*  919:     */     
/*  920: 963 */     Vector logs = new Vector();
/*  921: 964 */     Client cl = new Client();
/*  922: 965 */     cl.participant = participant;
/*  923: 966 */     cl.operation = null;
/*  924: 968 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  925:     */     {
/*  926: 970 */       XModel row = dt.get(i);
/*  927:     */       
/*  928: 972 */       String log = (String)row.get(0).get();
/*  929: 973 */       logs.add(log);
/*  930:     */     }
/*  931: 976 */     cl.run(logs, recepients);
/*  932: 977 */     dt.executeUpdate("update changelogs set status='1'");
/*  933:     */   }
/*  934:     */   
/*  935:     */   public synchronized void sendOutBoundResources()
/*  936:     */     throws Exception
/*  937:     */   {
/*  938: 983 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  939:     */     
/*  940: 985 */     String where = " status is null";
/*  941:     */     
/*  942: 987 */     dt.setupTable("resource_outbound_queue", "*", where, "test", false);
/*  943: 988 */     dt.retrieve();
/*  944: 989 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  945:     */     {
/*  946: 991 */       XModel row = dt.get(i);
/*  947: 992 */       String rec = (String)((XModel)row.get("recepient")).get();
/*  948: 993 */       String fromPath = ((XModel)row.get("resource")).get().toString();
/*  949: 994 */       String toPath = ((XModel)row.get("resource1")).get().toString();
/*  950:     */       
/*  951: 996 */       Client cl = new Client();
/*  952: 997 */       cl.operation = "deliver";
/*  953: 998 */       cl.run(fromPath, toPath, rec);
/*  954:     */     }
/*  955:1001 */     dt.executeUpdate("update resource_outbound_queue set status='1'");
/*  956:     */   }
/*  957:     */   
/*  958:     */   public synchronized void sendOutBoundResources(String recepient)
/*  959:     */     throws Exception
/*  960:     */   {
/*  961:1007 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  962:     */     
/*  963:1009 */     String where = " status is null and recepient='" + recepient + "'";
/*  964:     */     
/*  965:1011 */     dt.setupTable("resource_outbound_queue", "*", where, "test", false);
/*  966:1012 */     dt.retrieve();
/*  967:1013 */     String lastId = null;
/*  968:1014 */     for (int i = 0; i < dt.getNumChildren(); i++)
/*  969:     */     {
/*  970:1016 */       XModel row = dt.get(i);
/*  971:1017 */       String rec = (String)((XModel)row.get("recepient")).get();
/*  972:1018 */       String fromPath = ((XModel)row.get("resource")).get().toString();
/*  973:1019 */       String toPath = ((XModel)row.get("resource1")).get().toString();
/*  974:     */       
/*  975:1021 */       Client cl = new Client();
/*  976:1022 */       cl.operation = "deliver";
/*  977:1023 */       cl.run(fromPath, toPath, rec);
/*  978:1024 */       lastId = (String)((XModel)row.get("id")).get();
/*  979:     */     }
/*  980:1027 */     if (lastId != null) {
/*  981:1029 */       dt.executeUpdate("update resource_outbound_queue set status='1' where  recepient='" + recepient + "' and id <='" + lastId + "'");
/*  982:     */     }
/*  983:     */   }
/*  984:     */   
/*  985:     */   public synchronized void sendOutboundLogs(String participant)
/*  986:     */     throws Exception
/*  987:     */   {
/*  988:1036 */     sendOutboundLogs(participant, "default");
/*  989:     */   }
/*  990:     */   
/*  991:     */   public synchronized void sendOutboundLogs(String participant, String dest)
/*  992:     */     throws Exception
/*  993:     */   {
/*  994:1042 */     DatabaseTableModel dt = new DatabaseTableModel();
/*  995:     */     
/*  996:1044 */     String where = " status is null";
/*  997:     */     
/*  998:1046 */     dt.setupTable("changelog_outbound_queue", "*", where, "test", false);
/*  999:1047 */     dt.retrieve();
/* 1000:1048 */     Hashtable ht = new Hashtable();
/* 1001:1050 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1002:     */     {
/* 1003:1052 */       XModel row = dt.get(i);
/* 1004:1053 */       String rec = (String)((XModel)row.get("recepient")).get();
/* 1005:1054 */       Vector[] bks = (Vector[])ht.get(rec);
/* 1006:1055 */       if (bks == null)
/* 1007:     */       {
/* 1008:1057 */         bks = new Vector[2];
/* 1009:1058 */         bks[0] = new Vector();
/* 1010:1059 */         bks[1] = new Vector();
/* 1011:     */       }
/* 1012:1062 */       bks[0].add(((XModel)row.get("frombookmark")).get());
/* 1013:1063 */       bks[1].add(((XModel)row.get("tobookmark")).get());
/* 1014:1064 */       ht.put(rec, bks);
/* 1015:     */     }
/* 1016:1067 */     Enumeration keys = ht.keys();
/* 1017:1068 */     while (keys.hasMoreElements())
/* 1018:     */     {
/* 1019:1070 */       String id = (String)keys.nextElement();
/* 1020:1071 */       Vector[] bks = (Vector[])ht.get(id);
/* 1021:1072 */       sendServerLogs(participant, id, bks[0], bks[1], dest);
/* 1022:1073 */       dt.executeUpdate("update changelog_outbound_queue set status='1' where recepient='" + id + "'");
/* 1023:     */     }
/* 1024:     */   }
/* 1025:     */   
/* 1026:     */   public synchronized void sendOutboundDataPhysicians(String dest)
/* 1027:     */     throws Exception
/* 1028:     */   {
/* 1029:1080 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1030:     */     
/* 1031:1082 */     String where = " status !='stopped' ";
/* 1032:1083 */     this.logger.info(" Inside SenDP");
/* 1033:1084 */     dt.setupTable("physician", "distinct id", where, "test", false);
/* 1034:1085 */     dt.retrieve();
/* 1035:1086 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1036:     */     {
/* 1037:1088 */       XModel row = dt.get(i);
/* 1038:1089 */       String recepient = row.get(0).get().toString();
/* 1039:1090 */       this.logger.info(" Processing phy " + recepient);
/* 1040:1091 */       sendOutboundLogs1("admin", recepient, dest);
/* 1041:1092 */       this.logger.info(" After logs ");
/* 1042:1093 */       sendOutBoundResources(recepient);
/* 1043:1094 */       this.logger.info(" After recources ");
/* 1044:     */     }
/* 1045:     */   }
/* 1046:     */   
/* 1047:     */   public synchronized void sendOutboundLogs1(String from, String to, String dest)
/* 1048:     */     throws Exception
/* 1049:     */   {
/* 1050:1101 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1051:     */     
/* 1052:1103 */     String where = " status is null and recepient='" + to + "'";
/* 1053:     */     
/* 1054:1105 */     dt.setupTable("changelog_outbound_queue", "*", where, "test", false);
/* 1055:1106 */     dt.retrieve();
/* 1056:1107 */     Hashtable ht = new Hashtable();
/* 1057:     */     
/* 1058:1109 */     Vector[] bks = { new Vector(), new Vector() };
/* 1059:1110 */     String lastId = null;
/* 1060:1112 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1061:     */     {
/* 1062:1114 */       XModel row = dt.get(i);
/* 1063:     */       
/* 1064:1116 */       String frombookmark = ((XModel)row.get("frombookmark")).get().toString();
/* 1065:1117 */       String tobookmark = ((XModel)row.get("tobookmark")).get().toString();
/* 1066:1118 */       bks[0].add(frombookmark);
/* 1067:1119 */       bks[1].add(tobookmark);
/* 1068:1120 */       lastId = tobookmark;
/* 1069:     */     }
/* 1070:1123 */     if (lastId != null)
/* 1071:     */     {
/* 1072:1125 */       sendServerLogs(from, to, bks[0], bks[1], dest);
/* 1073:1126 */       dt.executeUpdate("update changelog_outbound_queue set status='1' where recepient='" + to + "' and tobookmark<='" + lastId + "'");
/* 1074:     */     }
/* 1075:     */   }
/* 1076:     */   
/* 1077:     */   public void createNotification(String subject, String summary, String summaryquery, String query, String template, String status, String sent, String type)
/* 1078:     */     throws Exception
/* 1079:     */   {
/* 1080:1133 */     XModel xm = new XBaseModel();
/* 1081:1134 */     ((XModel)xm.get("subject")).set(subject);
/* 1082:1135 */     ((XModel)xm.get("summary")).set(summary);
/* 1083:1136 */     ((XModel)xm.get("summaryquery")).set(summaryquery);
/* 1084:1137 */     ((XModel)xm.get("query")).set(query);
/* 1085:1138 */     ((XModel)xm.get("template")).set(template);
/* 1086:     */     
/* 1087:1140 */     getInstance().saveData("notifications", "false", xm);
/* 1088:     */   }
/* 1089:     */   
/* 1090:     */   public void sendServerLogs(String participant, String recepients, Vector frombookmark, Vector tobookmark, String dest)
/* 1091:     */     throws Exception
/* 1092:     */   {
/* 1093:1146 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1094:1147 */     this.logger.info("bookmark " + frombookmark);
/* 1095:1148 */     String where = "";
/* 1096:1149 */     for (int i = 0; i < frombookmark.size(); i++)
/* 1097:     */     {
/* 1098:1151 */       String cond = " id >'" + frombookmark.get(i) + "' and id <='" + tobookmark.get(i) + "'";
/* 1099:1153 */       if (i == 0) {
/* 1100:1154 */         where = where + cond;
/* 1101:     */       } else {
/* 1102:1156 */         where = where + " or " + cond;
/* 1103:     */       }
/* 1104:     */     }
/* 1105:1158 */     this.logger.info(" COndition " + where);
/* 1106:1159 */     dt.setupTable("changelogs", "value", where, "test", false);
/* 1107:1160 */     dt.retrieve();
/* 1108:     */     
/* 1109:1162 */     Vector logs = new Vector();
/* 1110:1163 */     Client cl = new Client();
/* 1111:1164 */     cl.init(dest);
/* 1112:1165 */     cl.participant = participant;
/* 1113:1166 */     cl.operation = null;
/* 1114:1168 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1115:     */     {
/* 1116:1170 */       XModel row = dt.get(i);
/* 1117:     */       
/* 1118:1172 */       String log = (String)row.get(0).get();
/* 1119:1173 */       logs.add(log);
/* 1120:     */     }
/* 1121:1176 */     this.logger.info("before ");
/* 1122:1177 */     cl.run(logs, recepients);
/* 1123:1178 */     dt.executeUpdate("update changelogs set status='1' where " + where);
/* 1124:     */     
/* 1125:1180 */     this.logger.info("after ");
/* 1126:     */   }
/* 1127:     */   
/* 1128:     */   public void sendServerLogs2(String participant, String recepients, String frombookmark, String tobookmark)
/* 1129:     */     throws Exception
/* 1130:     */   {
/* 1131:1186 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1132:1187 */     this.logger.info("bookmark " + frombookmark);
/* 1133:1188 */     dt.setupTable("changelogs", "value", " status is null and id >'" + frombookmark + "' and id <'" + tobookmark + "'", "test", true);
/* 1134:1189 */     dt.retrieve();
/* 1135:     */     
/* 1136:1191 */     Client cl = new Client();
/* 1137:1192 */     cl.participant = participant;
/* 1138:1193 */     cl.operation = null;
/* 1139:1194 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS");
/* 1140:1195 */     StringBuffer logs = new StringBuffer();
/* 1141:     */     
/* 1142:1197 */     logs.append("<logs>");
/* 1143:1199 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1144:     */     {
/* 1145:1201 */       XModel row = dt.get(i);
/* 1146:     */       
/* 1147:1203 */       String log = (String)row.get(0).get();
/* 1148:1204 */       logs.append(log + "\r\n");
/* 1149:     */     }
/* 1150:1207 */     logs.append("</logs>");
/* 1151:1208 */     String fname = "./" + participant + "-" + sdf.format(new Date()) + "-received.xml";
/* 1152:     */     
/* 1153:1210 */     FileWriter fw = new FileWriter(fname);
/* 1154:     */     
/* 1155:1212 */     fw.write(logs.toString());
/* 1156:     */     
/* 1157:1214 */     fw.close();
/* 1158:     */     
/* 1159:1216 */     StringTokenizer st = new StringTokenizer(recepients, ",");
/* 1160:1217 */     if (st.countTokens() > 1) {
/* 1161:1219 */       while (st.hasMoreTokens()) {
/* 1162:1221 */         deliverMessage(fname, st.nextToken());
/* 1163:     */       }
/* 1164:     */     } else {
/* 1165:1225 */       deliverMessage(fname, recepients);
/* 1166:     */     }
/* 1167:1226 */     this.logger.info("dELIVERED");
/* 1168:1227 */     dt.executeUpdate("update changelogs set status='1'");
/* 1169:     */   }
/* 1170:     */   
/* 1171:     */   public void sendServerLogs1(String participant, String recepients)
/* 1172:     */     throws Exception
/* 1173:     */   {
/* 1174:1233 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1175:     */     
/* 1176:1235 */     dt.setupTable("changelogs", "value", " status is null or status !=1 ", "test", false);
/* 1177:1236 */     dt.retrieve();
/* 1178:     */     
/* 1179:1238 */     Vector logs = new Vector();
/* 1180:1239 */     Client cl = new Client();
/* 1181:1240 */     cl.participant = participant;
/* 1182:1241 */     cl.operation = null;
/* 1183:1243 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1184:     */     {
/* 1185:1245 */       XModel row = dt.get(i);
/* 1186:     */       
/* 1187:1247 */       String log = (String)row.get(0).get();
/* 1188:1248 */       logs.add(log);
/* 1189:     */     }
/* 1190:1251 */     cl.run(logs, recepients);
/* 1191:1252 */     dt.executeUpdate("update changelogs set status='1'");
/* 1192:     */   }
/* 1193:     */   
/* 1194:     */   public int sendLogs(String participant, String recepients)
/* 1195:     */     throws Exception
/* 1196:     */   {
/* 1197:1258 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1198:     */     
/* 1199:1260 */     dt.setupTable("changelogs", "value,id", " status is null ", "test", true);
/* 1200:1261 */     dt.retrieve();
/* 1201:     */     
/* 1202:1263 */     Vector logs = new Vector();
/* 1203:1264 */     Client cl = new Client();
/* 1204:1265 */     cl.participant = participant;
/* 1205:     */     
/* 1206:1267 */     int count = dt.getNumChildren();
/* 1207:1268 */     String lastId = null;
/* 1208:1270 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1209:     */     {
/* 1210:1272 */       XModel row = dt.get(i);
/* 1211:     */       
/* 1212:1274 */       String log = (String)row.get(0).get();
/* 1213:1275 */       logs.add(log);
/* 1214:1276 */       lastId = (String)row.get(1).get();
/* 1215:     */     }
/* 1216:1279 */     if (lastId != null)
/* 1217:     */     {
/* 1218:1281 */       cl.run(logs, recepients);
/* 1219:     */       
/* 1220:1283 */       PreparedStatement ps = dt.getTable().getPreparedStatement("update changelogs set status='1' where id <= ?");
/* 1221:1284 */       ps.setString(1, lastId);
/* 1222:1285 */       ps.execute();
/* 1223:1286 */       closePs(dt, ps);
/* 1224:     */     }
/* 1225:1289 */     return count;
/* 1226:     */   }
/* 1227:     */   
/* 1228:     */   public int sendLogs(String participant, String recepients, String dest)
/* 1229:     */     throws Exception
/* 1230:     */   {
/* 1231:1295 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1232:     */     
/* 1233:1297 */     dt.setupTable("changelogs", "value,id", " status is null ", "test", true);
/* 1234:1298 */     dt.retrieve();
/* 1235:     */     
/* 1236:1300 */     Vector logs = new Vector();
/* 1237:1301 */     Client cl = new Client();
/* 1238:1302 */     cl.init(dest);
/* 1239:1303 */     cl.participant = participant;
/* 1240:     */     
/* 1241:1305 */     int count = dt.getNumChildren();
/* 1242:1306 */     String lastId = null;
/* 1243:1308 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1244:     */     {
/* 1245:1310 */       XModel row = dt.get(i);
/* 1246:     */       
/* 1247:1312 */       String log = (String)row.get(0).get();
/* 1248:1313 */       logs.add(log);
/* 1249:1314 */       lastId = (String)row.get(1).get();
/* 1250:     */     }
/* 1251:1317 */     if (lastId != null)
/* 1252:     */     {
/* 1253:1319 */       cl.run(logs, recepients);
/* 1254:     */       
/* 1255:1321 */       PreparedStatement ps = dt.getTable().getPreparedStatement("update changelogs set status='1' where id <= ?");
/* 1256:1322 */       ps.setString(1, lastId);
/* 1257:1323 */       ps.execute();
/* 1258:1324 */       closePs(dt, ps);
/* 1259:     */     }
/* 1260:1327 */     return count;
/* 1261:     */   }
/* 1262:     */   
/* 1263:     */   public int sendLogs(String participant, String recepients, String frombookmark, String tobookmark, String dest)
/* 1264:     */     throws Exception
/* 1265:     */   {
/* 1266:1333 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1267:     */     
/* 1268:1335 */     dt.setupTable("changelogs", "value,id", " status is null and id >" + frombookmark + " and id <=" + tobookmark, "test", true);
/* 1269:1336 */     dt.retrieve();
/* 1270:     */     
/* 1271:1338 */     Vector logs = new Vector();
/* 1272:1339 */     Client cl = new Client();
/* 1273:1340 */     cl.init(dest);
/* 1274:1341 */     cl.participant = participant;
/* 1275:     */     
/* 1276:1343 */     int count = dt.getNumChildren();
/* 1277:1344 */     String lastId = null;
/* 1278:1346 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1279:     */     {
/* 1280:1348 */       XModel row = dt.get(i);
/* 1281:     */       
/* 1282:1350 */       String log = (String)row.get(0).get();
/* 1283:1351 */       logs.add(log);
/* 1284:1352 */       lastId = (String)row.get(1).get();
/* 1285:     */     }
/* 1286:1355 */     if (lastId != null)
/* 1287:     */     {
/* 1288:1357 */       cl.run(logs, recepients);
/* 1289:1358 */       dt.executeUpdate("update changelogs set status='1' where  id >" + frombookmark + " and id <=" + tobookmark);
/* 1290:     */     }
/* 1291:1361 */     return count;
/* 1292:     */   }
/* 1293:     */   
/* 1294:     */   public void sendLogsLocal(String path, String participant, String recepients)
/* 1295:     */     throws Exception
/* 1296:     */   {
/* 1297:1367 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1298:1368 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS");
/* 1299:1369 */     dt.setupTable("changelogs", "value", " status is null ", "test", true);
/* 1300:1370 */     dt.retrieve();
/* 1301:     */     
/* 1302:1372 */     StringBuffer logs = new StringBuffer();
/* 1303:1373 */     Client cl = new Client();
/* 1304:1374 */     cl.participant = participant;
/* 1305:1375 */     logs.append("<logs>");
/* 1306:1377 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1307:     */     {
/* 1308:1379 */       XModel row = dt.get(i);
/* 1309:     */       
/* 1310:1381 */       String log = (String)row.get(0).get();
/* 1311:1382 */       logs.append(log + "\r\n");
/* 1312:     */     }
/* 1313:1385 */     logs.append("</logs>");
/* 1314:1386 */     String fname = path + "/" + participant + "-" + sdf.format(new Date()) + "-received.xml";
/* 1315:     */     
/* 1316:1388 */     FileWriter fw = new FileWriter(fname);
/* 1317:     */     
/* 1318:1390 */     fw.write(logs.toString());
/* 1319:     */     
/* 1320:1392 */     fw.close();
/* 1321:     */     
/* 1322:1394 */     dt.executeUpdate("update changelogs set status='1'");
/* 1323:     */   }
/* 1324:     */   
/* 1325:     */   public void getTask(String path, String team, XTaskModel ioM, String surveyType, String area, String house, String household, String individual)
/* 1326:     */   {
/* 1327:1399 */     String taskStr = path;
/* 1328:1400 */     String context = ((area != null) && (!area.equals("")) && (!area.equals("-1")) ? "and area=" + area : "") + ((house != null) && (!house.equals("")) ? " and house=" + house : "") + ((household != null) && (!household.equals("")) ? " and household=" + household : "") + ((individual != null) && (!individual.equals("")) ? " and member=" + individual : "");
/* 1329:1401 */     this.logger.info(taskStr);
/* 1330:     */     try
/* 1331:     */     {
/* 1332:1404 */       String where = "assignedto=" + team + " and  survey_type='" + surveyType + "'  and task ='" + taskStr + "/%' ";
/* 1333:1405 */       this.logger.info(where);
/* 1334:1406 */       DatabaseTableModel dt = new DatabaseTableModel();
/* 1335:1407 */       dt.setupTable("tasks", "*", where, "test", false);
/* 1336:     */       
/* 1337:1409 */       dt.retrieve();
/* 1338:1411 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 1339:     */       {
/* 1340:1413 */         XModel row = dt.get(i);
/* 1341:1414 */         XTaskModel taskM = new XTaskModel();
/* 1342:1415 */         ioM.append(taskM);
/* 1343:1416 */         taskM.surveyType = surveyType;
/* 1344:1417 */         taskM.task = row.get("task").toString();
/* 1345:1418 */         taskM.area = row.get("area").toString();
/* 1346:1419 */         taskM.house = row.get("house").toString();
/* 1347:1420 */         taskM.household = row.get("household").toString();
/* 1348:1421 */         taskM.member = row.get("member").toString();
/* 1349:1422 */         taskM.assignedTo = row.get("assignedto").toString();
/* 1350:1423 */         String status = row.get("status").toString();
/* 1351:     */         
/* 1352:1425 */         String taskPath = getTaskStatusPath(taskM.task, "healthcheckup", taskM.area, taskM.house, taskM.household, taskM.member);
/* 1353:1426 */         StringTokenizer st = new StringTokenizer(taskPath, "/");
/* 1354:1427 */         String taskId = "";
/* 1355:1428 */         while (st.hasMoreTokens()) {
/* 1356:1430 */           taskId = st.nextToken();
/* 1357:     */         }
/* 1358:1433 */         this.logger.info(" TAsk Path " + taskPath);
/* 1359:     */         
/* 1360:1435 */         taskM.setId(taskId);
/* 1361:     */         
/* 1362:1437 */         taskM.set(status);
/* 1363:1438 */         for (int j = 0; j < dt.getNumAttributes(); j++) {
/* 1364:1440 */           if ((!dt.getAttribName(j).equals("task")) && (!dt.getAttribName(j).equals("survey_type")) && (!dt.getAttribName(j).equals("id"))) {
/* 1365:1442 */             taskM.set("@" + dt.getAttribName(j), row.get(j).get());
/* 1366:     */           }
/* 1367:     */         }
/* 1368:1445 */         this.logger.info(" >>> " + taskM.get("@task"));
/* 1369:     */       }
/* 1370:     */     }
/* 1371:     */     catch (Exception e)
/* 1372:     */     {
/* 1373:1451 */       e.printStackTrace();
/* 1374:     */     }
/* 1375:     */   }
/* 1376:     */   
/* 1377:     */   public int getTaskChildCount(String parentPath, String team, XTaskModel ioM, String area, String house, String household, String individual)
/* 1378:     */   {
/* 1379:1457 */     String taskStr = parentPath;
/* 1380:1458 */     String context = ((area != null) && (!area.equals("")) && (!area.equals("-1")) ? "and area=" + area : "") + ((house != null) && (!house.equals("")) ? " and house=" + house : "") + ((household != null) && (!household.equals("")) ? " and household=" + household : "") + ((individual != null) && (!individual.equals("")) ? " and member=" + individual : "");
/* 1381:1459 */     this.logger.info(taskStr);
/* 1382:     */     try
/* 1383:     */     {
/* 1384:1462 */       String where = "assignedto=" + team + " and  survey_type='2'  and task LIKE '" + taskStr + "/%' and  task NOT LIKE '" + taskStr + "/%/%' " + context;
/* 1385:1463 */       this.logger.info(where);
/* 1386:1464 */       DatabaseTableModel dt = new DatabaseTableModel();
/* 1387:1465 */       dt.setupTable("tasks", "count(*) count1", where, "test", false);
/* 1388:     */       
/* 1389:1467 */       dt.retrieve();
/* 1390:     */       
/* 1391:1469 */       return Integer.parseInt(dt.get(0).get("count1").toString());
/* 1392:     */     }
/* 1393:     */     catch (Exception e)
/* 1394:     */     {
/* 1395:1473 */       e.printStackTrace();
/* 1396:     */     }
/* 1397:1475 */     return 0;
/* 1398:     */   }
/* 1399:     */   
/* 1400:     */   public void getLogisticsData(String parentPath, XLogisticsModel ioM)
/* 1401:     */   {
/* 1402:1480 */     String taskStr = parentPath;
/* 1403:1481 */     String context = "path='" + parentPath + "'";
/* 1404:1482 */     this.logger.info(taskStr);
/* 1405:     */     try
/* 1406:     */     {
/* 1407:1485 */       String where = context;
/* 1408:1486 */       this.logger.info(where);
/* 1409:1487 */       DatabaseTableModel dt = new DatabaseTableModel();
/* 1410:1488 */       dt.setupTable("logistics", "*", where, "test", false);
/* 1411:     */       
/* 1412:1490 */       dt.retrieve();
/* 1413:1492 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 1414:     */       {
/* 1415:1494 */         XModel row = dt.get(i);
/* 1416:1495 */         XLogisticsModel lM = new XLogisticsModel();
/* 1417:1496 */         ioM.append(lM);
/* 1418:1498 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1419:     */         {
/* 1420:1500 */           this.logger.info("Attributes called " + dt.getAttribName(j));
/* 1421:1501 */           if (dt.getAttribName(j).equals("name"))
/* 1422:     */           {
/* 1423:1503 */             lM.setId((String)row.get(j).get());
/* 1424:1504 */             this.logger.info(row.get(j).get());
/* 1425:     */           }
/* 1426:     */           else
/* 1427:     */           {
/* 1428:1507 */             lM.set("@" + dt.getAttribName(j), row.get(j).get());
/* 1429:     */           }
/* 1430:     */         }
/* 1431:     */       }
/* 1432:     */     }
/* 1433:     */     catch (Exception e)
/* 1434:     */     {
/* 1435:1517 */       e.printStackTrace();
/* 1436:     */     }
/* 1437:     */   }
/* 1438:     */   
/* 1439:     */   public void getTaskData(String where, XModel dataM)
/* 1440:     */   {
/* 1441:     */     try
/* 1442:     */     {
/* 1443:1525 */       DatabaseTableModel dt = new DatabaseTableModel();
/* 1444:1526 */       dt.setupTable("tasks", "*", where, "test", false);
/* 1445:1527 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 1446:     */       {
/* 1447:1529 */         XModel row = dt.get(i);
/* 1448:1530 */         XTaskModel taskM = new XTaskModel();
/* 1449:1531 */         dataM.append(taskM);
/* 1450:1532 */         String status = "";
/* 1451:1534 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1452:     */         {
/* 1453:1536 */           String attrib = dt.getAttribName(j);
/* 1454:1537 */           String attrib1 = attrib.toLowerCase();
/* 1455:1539 */           if (attrib1.equals("survey_type")) {
/* 1456:1540 */             taskM.surveyType = row.get(attrib).toString();
/* 1457:     */           }
/* 1458:1541 */           if (attrib1.equals("task")) {
/* 1459:1542 */             taskM.task = row.get(attrib).toString();
/* 1460:     */           }
/* 1461:1543 */           if (attrib1.equals("area")) {
/* 1462:1544 */             taskM.area = row.get(attrib).toString();
/* 1463:     */           }
/* 1464:1545 */           if (attrib1.equals("house")) {
/* 1465:1546 */             taskM.house = row.get(attrib).toString();
/* 1466:     */           }
/* 1467:1547 */           if (attrib1.equals("household")) {
/* 1468:1548 */             taskM.household = row.get(attrib).toString();
/* 1469:     */           }
/* 1470:1549 */           if (attrib1.equals("member")) {
/* 1471:1550 */             taskM.member = row.get(attrib).toString();
/* 1472:     */           }
/* 1473:1551 */           if (attrib1.equals("assignedto")) {
/* 1474:1552 */             taskM.assignedTo = row.get(attrib).toString();
/* 1475:     */           }
/* 1476:1553 */           if (attrib1.equals("survey_type")) {
/* 1477:1554 */             status = row.get(attrib).toString();
/* 1478:     */           }
/* 1479:     */         }
/* 1480:1557 */         String taskPath = getTaskStatusPath(taskM.task, "healthcheckup", taskM.area, taskM.house, taskM.household, taskM.member);
/* 1481:1558 */         dataM.get(taskPath);
/* 1482:1559 */         StringTokenizer st = new StringTokenizer(taskPath, "/");
/* 1483:1560 */         String taskId = "";
/* 1484:1561 */         while (st.hasMoreTokens()) {
/* 1485:1563 */           taskId = st.nextToken();
/* 1486:     */         }
/* 1487:1566 */         this.logger.info(" TAsk Path " + taskPath);
/* 1488:     */         
/* 1489:1568 */         taskM.setId(taskId);
/* 1490:     */         
/* 1491:1570 */         taskM.set(status);
/* 1492:1572 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1493:     */         {
/* 1494:1574 */           String attrib = dt.getAttribName(j);
/* 1495:1575 */           String attrib1 = attrib.toLowerCase();
/* 1496:1577 */           if ((!attrib1.equals("task")) && (!attrib1.equals("survey_type")) && (!attrib1.equals("id"))) {
/* 1497:1579 */             taskM.set("@" + attrib1, row.get(j).get());
/* 1498:     */           }
/* 1499:     */         }
/* 1500:1582 */         this.logger.info(" >>> " + taskM.get("@task"));
/* 1501:     */       }
/* 1502:     */     }
/* 1503:     */     catch (Exception e)
/* 1504:     */     {
/* 1505:1588 */       e.printStackTrace();
/* 1506:     */     }
/* 1507:     */   }
/* 1508:     */   
/* 1509:     */   public void getTasks(String parentPath, String team, XTaskModel ioM, String surveyType, String area, String house, String household, String individual)
/* 1510:     */   {
/* 1511:1593 */     init();
/* 1512:1594 */     String taskStr = parentPath;
/* 1513:1595 */     String context = ((area != null) && (!area.equals("")) && (!area.equals("-1")) && (!area.equals("0")) ? "and area='" + area + "'" : "") + ((house != null) && (!house.equals("")) ? " and house='" + house + "'" : "'") + ((household != null) && (!household.equals("")) ? " and household='" + household + "'" : "'") + ((individual != null) && (!individual.equals("")) ? " and member='" + individual + "'" : "");
/* 1514:1596 */     System.out.println(taskStr);
/* 1515:1597 */     String constraint = " and " + ioM.constraint;
/* 1516:     */     try
/* 1517:     */     {
/* 1518:1601 */       String where = "assignedto='" + team + "' and  survey_type='" + surveyType + "'  and task LIKE '" + taskStr + "/%' and  task NOT LIKE '" + taskStr + "/%/%' " + context + constraint;
/* 1519:1602 */       System.out.println(where);
/* 1520:1603 */       DatabaseTableModel dt = new DatabaseTableModel();
/* 1521:1604 */       dt.setupTable("tasks", "*", where, "test", false);
/* 1522:     */       
/* 1523:1606 */       dt.retrieve();
/* 1524:1612 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 1525:     */       {
/* 1526:1614 */         XModel row = dt.get(i);
/* 1527:1615 */         XTaskModel taskM = new XTaskModel();
/* 1528:1616 */         ioM.append(taskM);
/* 1529:     */         
/* 1530:1618 */         String status = "";
/* 1531:1619 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1532:     */         {
/* 1533:1621 */           String attrib = dt.getAttribName(j);
/* 1534:1622 */           String attrib1 = attrib.toLowerCase();
/* 1535:1624 */           if (attrib1.equals("survey_type")) {
/* 1536:1625 */             taskM.surveyType = row.get(attrib).toString();
/* 1537:     */           }
/* 1538:1626 */           if (attrib1.equals("task")) {
/* 1539:1627 */             taskM.task = row.get(attrib).toString();
/* 1540:     */           }
/* 1541:1628 */           if (attrib1.equals("area")) {
/* 1542:1629 */             taskM.area = row.get(attrib).toString();
/* 1543:     */           }
/* 1544:1630 */           if (attrib1.equals("house")) {
/* 1545:1631 */             taskM.house = row.get(attrib).toString();
/* 1546:     */           }
/* 1547:1632 */           if (attrib1.equals("household")) {
/* 1548:1633 */             taskM.household = row.get(attrib).toString();
/* 1549:     */           }
/* 1550:1634 */           if (attrib1.equals("member")) {
/* 1551:1635 */             taskM.member = row.get(attrib).toString();
/* 1552:     */           }
/* 1553:1636 */           if (attrib1.equals("assignedto")) {
/* 1554:1637 */             taskM.assignedTo = row.get(attrib).toString();
/* 1555:     */           }
/* 1556:1638 */           if (attrib1.equals("survey_type")) {
/* 1557:1639 */             status = row.get(attrib).toString();
/* 1558:     */           }
/* 1559:     */         }
/* 1560:1642 */         String taskPath = getTaskStatusPath(taskM.task, "healthcheckup", taskM.area, taskM.house, taskM.household, taskM.member);
/* 1561:1643 */         StringTokenizer st = new StringTokenizer(taskPath, "/");
/* 1562:1644 */         String taskId = "";
/* 1563:1645 */         while (st.hasMoreTokens()) {
/* 1564:1647 */           taskId = st.nextToken();
/* 1565:     */         }
/* 1566:1651 */         System.out.println(" TAsk Path " + taskPath);
/* 1567:     */         
/* 1568:1653 */         taskM.setId(taskId);
/* 1569:     */         
/* 1570:1655 */         taskM.set(status);
/* 1571:1657 */         for (int j = 0; j < dt.getNumAttributes(); j++) {
/* 1572:1659 */           if ((!dt.getAttribName(j).equals("task")) && (!dt.getAttribName(j).equals("survey_type")) && (!dt.getAttribName(j).equals("id"))) {
/* 1573:1663 */             taskM.set("@" + dt.getAttribName(j), row.get(j).get());
/* 1574:     */           }
/* 1575:     */         }
/* 1576:1666 */         System.out.println(" >>> " + taskM.get("@task"));
/* 1577:     */       }
/* 1578:     */     }
/* 1579:     */     catch (Exception e)
/* 1580:     */     {
/* 1581:1680 */       e.printStackTrace();
/* 1582:     */     }
/* 1583:     */   }
/* 1584:     */   
/* 1585:     */   public void testP(String parentPath, String team, XModel ioM)
/* 1586:     */   {
/* 1587:1685 */     String taskStr = parentPath;
/* 1588:     */     
/* 1589:1687 */     this.logger.info(taskStr);
/* 1590:     */     try
/* 1591:     */     {
/* 1592:1690 */       String where = "assignedto=" + team + " and  survey_type='2'  ";
/* 1593:1691 */       DatabaseTableModel dt = new DatabaseTableModel();
/* 1594:1692 */       dt.setupTable("tasks", "*", where, "test", false);
/* 1595:     */       
/* 1596:1694 */       dt.retrieve();
/* 1597:1696 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 1598:     */       {
/* 1599:1698 */         XModel row = dt.get(i);
/* 1600:     */         
/* 1601:1700 */         String task = row.get("task").toString();
/* 1602:1701 */         String area = row.get("area").toString();
/* 1603:1702 */         String house = row.get("house").toString();
/* 1604:1703 */         String household = row.get("household").toString();
/* 1605:1704 */         String member = row.get("member").toString();
/* 1606:1705 */         String status = row.get("status").toString();
/* 1607:     */         
/* 1608:1707 */         String taskPath = getTaskStatusPath(task, "healthcheckup", area, house, household, member);
/* 1609:     */         
/* 1610:1709 */         XModel taskM = (XModel)ioM.get(taskPath);
/* 1611:1710 */         taskM.set(status);
/* 1612:1711 */         for (int j = 0; j < dt.getNumAttributes(); j++) {
/* 1613:1713 */           if ((!dt.getAttribName(j).equals("task")) && (!dt.getAttribName(j).equals("survey_type")) && (!dt.getAttribName(j).equals("id"))) {
/* 1614:1715 */             taskM.set("@" + dt.getAttribName(j), row.get(j).get());
/* 1615:     */           }
/* 1616:     */         }
/* 1617:1718 */         this.logger.info(" >>> " + taskM.get("@task"));
/* 1618:     */       }
/* 1619:     */     }
/* 1620:     */     catch (Exception e)
/* 1621:     */     {
/* 1622:1724 */       e.printStackTrace();
/* 1623:     */     }
/* 1624:     */   }
/* 1625:     */   
/* 1626:     */   public String getPath(XModel taskM, XModel rel)
/* 1627:     */   {
/* 1628:1730 */     XModel parent = taskM.getParent();
/* 1629:1731 */     String id = taskM.getId();
/* 1630:1732 */     while ((parent != null) && (parent != rel))
/* 1631:     */     {
/* 1632:1734 */       id = parent.getId() + "/" + id;
/* 1633:1735 */       parent = parent.getParent();
/* 1634:     */     }
/* 1635:1737 */     return id;
/* 1636:     */   }
/* 1637:     */   
/* 1638:     */   public Vector split(String path, String sep, int index)
/* 1639:     */   {
/* 1640:1742 */     StringTokenizer st = new StringTokenizer(path, "/");
/* 1641:1743 */     Vector path1 = new Vector();
/* 1642:1744 */     while (st.hasMoreTokens())
/* 1643:     */     {
/* 1644:1746 */       String ele = st.nextToken();
/* 1645:1747 */       if (ele.indexOf(sep) != -1)
/* 1646:     */       {
/* 1647:1749 */         StringTokenizer st1 = new StringTokenizer(ele, sep);
/* 1648:1750 */         int count = 0;
/* 1649:1752 */         for (count = 0; (count <= index) && (st1.hasMoreTokens()); count++)
/* 1650:     */         {
/* 1651:1754 */           String tmp = st1.nextToken();
/* 1652:1756 */           if ((count == index) && (tmp != null) && (!tmp.equals("null")) && (!tmp.equals(""))) {
/* 1653:1758 */             path1.add(tmp);
/* 1654:     */           }
/* 1655:     */         }
/* 1656:     */       }
/* 1657:     */     }
/* 1658:1767 */     return path1;
/* 1659:     */   }
/* 1660:     */   
/* 1661:     */   public void dataPath(String taskPath) {}
/* 1662:     */   
/* 1663:     */   public DateFormat getMysqlDateFormat()
/* 1664:     */   {
/* 1665:1776 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 1666:1777 */     return df;
/* 1667:     */   }
/* 1668:     */   
/* 1669:     */   public int toInt(String obj, int defaultVal)
/* 1670:     */   {
/* 1671:     */     try
/* 1672:     */     {
/* 1673:1784 */       return Integer.parseInt(obj);
/* 1674:     */     }
/* 1675:     */     catch (Exception localException) {}
/* 1676:1790 */     return defaultVal;
/* 1677:     */   }
/* 1678:     */   
/* 1679:     */   public Date toDatetime(String obj, Date defaultVal)
/* 1680:     */   {
/* 1681:     */     try
/* 1682:     */     {
/* 1683:1797 */       return getDateFormat().parse(obj);
/* 1684:     */     }
/* 1685:     */     catch (Exception localException) {}
/* 1686:1803 */     return defaultVal;
/* 1687:     */   }
/* 1688:     */   
/* 1689:     */   public String toMySQlDatetime(Date obj, String defaultVal)
/* 1690:     */   {
/* 1691:     */     try
/* 1692:     */     {
/* 1693:1810 */       return getMysqlDateFormat().format(obj);
/* 1694:     */     }
/* 1695:     */     catch (Exception localException) {}
/* 1696:1816 */     return defaultVal;
/* 1697:     */   }
/* 1698:     */   
/* 1699:     */   public boolean checkIfTaskCanBeSaved(String dataPath, String taskPath)
/* 1700:     */   {
/* 1701:1821 */     return true;
/* 1702:     */   }
/* 1703:     */   
/* 1704:     */   public int saveTaskToDb(XModel taskM, String parentTaskPath, String parentDataPath)
/* 1705:     */   {
/* 1706:1826 */     String taskId = taskM.getId();
/* 1707:     */     
/* 1708:1828 */     int hiphenIndex = taskId.indexOf("-");
/* 1709:     */     
/* 1710:1830 */     String taskPath = "";
/* 1711:1831 */     String dataPath = "";
/* 1712:1832 */     if (hiphenIndex != -1)
/* 1713:     */     {
/* 1714:1834 */       taskPath = parentTaskPath + "/" + taskId.substring(0, hiphenIndex);
/* 1715:1835 */       String dataStr = taskId.substring(hiphenIndex + 1);
/* 1716:1837 */       if (!dataStr.equals("null")) {
/* 1717:1839 */         dataPath = parentDataPath + "/" + taskId.substring(taskId.indexOf("-") + 1);
/* 1718:     */       } else {
/* 1719:1843 */         return 0;
/* 1720:     */       }
/* 1721:     */     }
/* 1722:     */     else
/* 1723:     */     {
/* 1724:1848 */       taskPath = parentTaskPath + "/" + taskId;
/* 1725:     */     }
/* 1726:1851 */     this.logger.info(dataPath + " " + taskPath);
/* 1727:1852 */     StringTokenizer tknzr = new StringTokenizer(dataPath, "/");
/* 1728:1853 */     String area = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1729:1854 */     String house = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1730:1855 */     String household = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1731:1856 */     String individual = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1732:1857 */     this.logger.info(" Individual =" + individual);
/* 1733:     */     
/* 1734:1859 */     int assignedTo = toInt((String)taskM.get("@assignedto"), -1);
/* 1735:1860 */     int areaId = toInt(area, -1);
/* 1736:1861 */     int teamId = assignedTo;
/* 1737:1862 */     Date dtAssigned = toDatetime((String)taskM.get("@dateassigned"), null);
/* 1738:1863 */     int status = toInt((String)taskM.get(), -1);
/* 1739:1864 */     Date stTime = toDatetime((String)taskM.get("@starttime"), null);
/* 1740:1865 */     Date eTime = toDatetime((String)taskM.get("@endtime"), null);
/* 1741:1866 */     this.logger.info("stTime=" + stTime);
/* 1742:1867 */     this.logger.info("eTime=" + eTime);
/* 1743:1868 */     String stTimeStr = toMySQlDatetime(stTime, null);
/* 1744:1869 */     String eTimeStr = toMySQlDatetime(eTime, null);
/* 1745:1870 */     String dtAssignedStr = toMySQlDatetime(dtAssigned, null);
/* 1746:     */     
/* 1747:1872 */     int updatedRecords = 0;
/* 1748:1874 */     if (checkIfTaskCanBeSaved(dataPath, taskPath)) {
/* 1749:     */       try
/* 1750:     */       {
/* 1751:1878 */         String table = "tasks";
/* 1752:     */         
/* 1753:1880 */         String where = "task='" + taskPath + "' and area=" + areaId + " and house='" + house + "' and household='" + household + "' and member='" + individual + "' and survey_type=" + 2 + " and status != 1";
/* 1754:     */         
/* 1755:1882 */         XModel dataM = taskM;
/* 1756:     */         
/* 1757:1884 */         saveData(table, where, dataM);
/* 1758:     */       }
/* 1759:     */       catch (Exception exc)
/* 1760:     */       {
/* 1761:1888 */         exc.printStackTrace();
/* 1762:1889 */         throw new IllegalStateException("SAVE TASKS TO DB " + exc.toString());
/* 1763:     */       }
/* 1764:     */     }
/* 1765:1894 */     for (int index = 0; index < taskM.getNumChildren(); index++) {
/* 1766:1896 */       updatedRecords += saveTaskToDb(taskM.get(index), taskPath, dataPath);
/* 1767:     */     }
/* 1768:1899 */     return updatedRecords;
/* 1769:     */   }
/* 1770:     */   
/* 1771:     */   public int saveTaskToSingle(XModel taskM, String parentTaskPath)
/* 1772:     */   {
/* 1773:1904 */     String taskId = taskM.getId();
/* 1774:     */     
/* 1775:1906 */     int hiphenIndex = taskId.indexOf("-");
/* 1776:     */     
/* 1777:1908 */     String taskPath = "";
/* 1778:1909 */     String dataPath = "";
/* 1779:     */     String dataStr;
/* 1780:1911 */     if (hiphenIndex != -1)
/* 1781:     */     {
/* 1782:1913 */       taskPath = parentTaskPath + "/" + taskId.substring(0, hiphenIndex);
/* 1783:1914 */       dataStr = taskId.substring(hiphenIndex + 1);
/* 1784:     */     }
/* 1785:     */     else
/* 1786:     */     {
/* 1787:1918 */       taskPath = parentTaskPath + "/" + taskId;
/* 1788:     */     }
/* 1789:1921 */     this.logger.info(dataPath + " " + taskPath);
/* 1790:1922 */     StringTokenizer tknzr = new StringTokenizer(dataPath, "/");
/* 1791:1923 */     String area = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1792:1924 */     String house = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1793:1925 */     String household = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1794:1926 */     String individual = tknzr.hasMoreElements() ? tknzr.nextToken() : "";
/* 1795:1927 */     this.logger.info(" Individual =" + individual);
/* 1796:     */     
/* 1797:1929 */     int assignedTo = toInt((String)taskM.get("@assignedto"), -1);
/* 1798:1930 */     int areaId = toInt(area, -1);
/* 1799:1931 */     int teamId = assignedTo;
/* 1800:1932 */     Date dtAssigned = toDatetime((String)taskM.get("@dateassigned"), null);
/* 1801:1933 */     int status = toInt((String)taskM.get(), -1);
/* 1802:1934 */     Date stTime = toDatetime((String)taskM.get("@starttime"), null);
/* 1803:1935 */     Date eTime = toDatetime((String)taskM.get("@endtime"), null);
/* 1804:1936 */     this.logger.info("stTime=" + stTime);
/* 1805:1937 */     this.logger.info("eTime=" + eTime);
/* 1806:1938 */     String stTimeStr = toMySQlDatetime(stTime, null);
/* 1807:1939 */     String eTimeStr = toMySQlDatetime(eTime, null);
/* 1808:1940 */     String dtAssignedStr = toMySQlDatetime(dtAssigned, null);
/* 1809:     */     
/* 1810:1942 */     int updatedRecords = 0;
/* 1811:1944 */     if (checkIfTaskCanBeSaved(dataPath, taskPath)) {
/* 1812:     */       try
/* 1813:     */       {
/* 1814:1948 */         String table = "tasks";
/* 1815:     */         
/* 1816:1950 */         String where = "task='" + taskPath + "' and area=" + areaId + " and house='" + house + "' and household='" + household + "' and member='" + individual + "' and survey_type=" + 2 + " and status != 1";
/* 1817:     */         
/* 1818:1952 */         XModel dataM = taskM;
/* 1819:     */         
/* 1820:1954 */         saveData(table, where, dataM);
/* 1821:     */       }
/* 1822:     */       catch (Exception exc)
/* 1823:     */       {
/* 1824:1958 */         exc.printStackTrace();
/* 1825:1959 */         throw new IllegalStateException("SAVE TASKS TO DB " + exc.toString());
/* 1826:     */       }
/* 1827:     */     }
/* 1828:1964 */     for (int index = 0; index < taskM.getNumChildren(); index++) {
/* 1829:1966 */       updatedRecords += saveTaskToDb(taskM.get(index), taskPath, dataPath);
/* 1830:     */     }
/* 1831:1969 */     return updatedRecords;
/* 1832:     */   }
/* 1833:     */   
/* 1834:     */   public DateFormat getDateFormat()
/* 1835:     */   {
/* 1836:1974 */     SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
/* 1837:1975 */     return df;
/* 1838:     */   }
/* 1839:     */   
/* 1840:     */   public XModel getAreas(String assignedTo, XModel dataM, String surveyType)
/* 1841:     */     throws Exception
/* 1842:     */   {
/* 1843:1981 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1844:1982 */     dt.setupTable("tasks", "area", "assignedto='" + assignedTo + "' and area != -1 and survey_type='" + surveyType + "'", "test", false);
/* 1845:     */     
/* 1846:1984 */     dt.setDistinct(true);
/* 1847:1985 */     dt.retrieve();
/* 1848:     */     
/* 1849:1987 */     this.logger.info(" debug getareas " + dt.getNumChildren());
/* 1850:1988 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1851:     */     {
/* 1852:1990 */       String id = dt.get(i).get("area").toString();
/* 1853:1991 */       XModel tt = (XModel)dataM.get(id);
/* 1854:     */       
/* 1855:1993 */       XModel xm = getAreadetails(id);
/* 1856:     */       
/* 1857:1995 */       tt.append(xm);
/* 1858:     */     }
/* 1859:1998 */     return dataM;
/* 1860:     */   }
/* 1861:     */   
/* 1862:     */   public XModel getEnumData1(String table, String where, XModel dataM, String fields, String idField)
/* 1863:     */     throws Exception
/* 1864:     */   {
/* 1865:2004 */     this.logger.info(" table " + table + " where = " + where + " fields=" + fields + " " + idField);
/* 1866:2005 */     String[] fields2 = fields.split(",");
/* 1867:2006 */     boolean removeId = !fields.equals("*");
/* 1868:2008 */     for (int i = 0; i < fields2.length; i++) {
/* 1869:2010 */       if (fields2[i].equals(idField)) {
/* 1870:2012 */         removeId = false;
/* 1871:     */       }
/* 1872:     */     }
/* 1873:2015 */     String fields1 = removeId ? fields + "," + idField : fields;
/* 1874:2016 */     this.logger.info(" table " + table + " where = " + where + " fields=" + fields1 + " " + idField);
/* 1875:2017 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1876:2018 */     dt.setupTable(table, fields1, where, "test", false);
/* 1877:     */     
/* 1878:2020 */     dt.retrieve();
/* 1879:2021 */     String idField1 = idField;
/* 1880:2022 */     if (idField.contains("."))
/* 1881:     */     {
/* 1882:2024 */       StringTokenizer st = new StringTokenizer(idField, ".");
/* 1883:     */       
/* 1884:2026 */       String tt = "";
/* 1885:2027 */       while (st.hasMoreTokens()) {
/* 1886:2029 */         tt = st.nextToken();
/* 1887:     */       }
/* 1888:2032 */       this.logger.info("TT  " + tt);
/* 1889:2033 */       idField1 = tt;
/* 1890:     */     }
/* 1891:2036 */     this.logger.info(" debug getareas " + dt.getNumChildren() + " " + idField1);
/* 1892:2037 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1893:     */     {
/* 1894:2039 */       String id = ((XModel)dt.get(i).get(idField1)).get().toString();
/* 1895:     */       
/* 1896:2041 */       XModel rowM = (XModel)dataM.get(id);
/* 1897:2042 */       rowM.setId(id);
/* 1898:2044 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1899:     */       {
/* 1900:2046 */         String id1 = dt.getAttribName(j);
/* 1901:     */         
/* 1902:2048 */         XModel tt = (XModel)rowM.get(id1);
/* 1903:     */         
/* 1904:2050 */         Object val = dt.get(i).get(j).get();
/* 1905:2051 */         val = val == null ? "" : val;
/* 1906:2052 */         tt.set(val);
/* 1907:     */         
/* 1908:2054 */         rowM.append(tt);
/* 1909:     */       }
/* 1910:     */     }
/* 1911:2059 */     return dataM;
/* 1912:     */   }
/* 1913:     */   
/* 1914:     */   public XModel getAreas1(XModel dataM)
/* 1915:     */     throws Exception
/* 1916:     */   {
/* 1917:2065 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1918:2066 */     dt.setupTable("hc_area", "id ,name,description,landmark,pincode", "", "test", false);
/* 1919:     */     
/* 1920:2068 */     dt.setDistinct(true);
/* 1921:2069 */     dt.retrieve();
/* 1922:     */     
/* 1923:2071 */     this.logger.info(" debug getareas " + dt.getNumChildren());
/* 1924:2072 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1925:     */     {
/* 1926:2074 */       String id = ((XModel)dt.get(i).get("id")).get().toString();
/* 1927:     */       
/* 1928:2076 */       XModel rowM = (XModel)dataM.get("id");
/* 1929:2077 */       rowM.setId(id);
/* 1930:2079 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1931:     */       {
/* 1932:2081 */         String id1 = dt.getAttribName(j);
/* 1933:2082 */         XModel tt = (XModel)rowM.get(id1);
/* 1934:     */         
/* 1935:2084 */         Object val = dt.get(i).get(j).get();
/* 1936:2085 */         val = val == null ? "" : val;
/* 1937:2086 */         tt.set(val);
/* 1938:     */         
/* 1939:2088 */         rowM.append(tt);
/* 1940:     */       }
/* 1941:     */     }
/* 1942:2093 */     return dataM;
/* 1943:     */   }
/* 1944:     */   
/* 1945:     */   public XModel getAreadetails(String area)
/* 1946:     */   {
/* 1947:2098 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1948:     */     
/* 1949:2100 */     String sql = "select ea.id areacode,ea.landmark landmark,ea.pincode pin, ea.name areaname, ea.description, ea.healthcheckup hcid, ea.target target, su.code sampleunitcode, su.name sampleunitname, si.name state, di.name district, su.vort vort from hc_area ea left join healthcheckup s on ea.healthcheckup=s.id left join sampleunit su on s.sunit=su.code left join states_india si on su.state=si.id  left join districts_india di on su.district=di.id ";
/* 1950:2101 */     String where = "ea.id=" + area;
/* 1951:2102 */     String fields = "ea.id as areacode,ea.landmark landmark,ea.pincode pin, ea.name areaname, ea.description, ea.healthcheckup hcid, ea.target target, su.code sampleunitcode, su.vort vort";
/* 1952:2103 */     String table = "hc_area ea left join healthcheckup s on ea.healthcheckup=s.id left join sampleunit su on s.sunit=su.code left join states_india si on su.state=si.id  left join districts_india di on su.district=di.id ";
/* 1953:     */     
/* 1954:2105 */     dt.setupTable(table, fields, where, "test", false);
/* 1955:     */     
/* 1956:2107 */     dt.retrieve();
/* 1957:2108 */     this.logger.info(" sql  " + sql + "where " + where);
/* 1958:2109 */     this.logger.info(" Children " + dt.getNumChildren());
/* 1959:2110 */     XBaseModel xm = new XBaseModel();
/* 1960:2111 */     xm.setId("taskinfo");
/* 1961:2113 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 1962:2115 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 1963:     */       {
/* 1964:2117 */         this.logger.info(" attr ---" + dt.getAttribName(j));
/* 1965:2118 */         this.logger.info(" attr ---" + dt.get(i).getAttribName(j));
/* 1966:     */         
/* 1967:2120 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 1968:2121 */         tt.set(dt.get(i).get(j).get());
/* 1969:     */       }
/* 1970:     */     }
/* 1971:2126 */     return xm;
/* 1972:     */   }
/* 1973:     */   
/* 1974:     */   public void getUsersForTeam(String team, KenList list)
/* 1975:     */   {
/* 1976:2131 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1977:2132 */     dt.setupTable("team_user", "user", "team='" + team + "'", "test", false);
/* 1978:2133 */     dt.retrieve();
/* 1979:2134 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1980:     */     {
/* 1981:2136 */       String id = dt.get(i).get("user").toString();
/* 1982:     */       
/* 1983:2138 */       list.add(id);
/* 1984:     */     }
/* 1985:     */   }
/* 1986:     */   
/* 1987:     */   public XModel getHouses(String area, XModel areaM)
/* 1988:     */     throws Exception
/* 1989:     */   {
/* 1990:2145 */     this.logger.info(" Debug area is " + area);
/* 1991:2146 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 1992:2147 */     dt.setupTable("houses", "houseno", "enum_area=" + area, "test", false);
/* 1993:2148 */     dt.retrieve();
/* 1994:     */     
/* 1995:2150 */     this.logger.info(Integer.valueOf(dt.getNumChildren()));
/* 1996:2151 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 1997:     */     {
/* 1998:2153 */       String id = dt.get(i).get("houseno").toString();
/* 1999:2154 */       XModel tt = (XModel)areaM.get(id);
/* 2000:     */       
/* 2001:2156 */       XModel xm = getHousedetails(id, area);
/* 2002:     */       
/* 2003:2158 */       tt.append(xm);
/* 2004:     */       
/* 2005:2160 */       xm.append(getDataM("data", "gpscheck", area, id, null, null));
/* 2006:     */     }
/* 2007:2163 */     return areaM;
/* 2008:     */   }
/* 2009:     */   
/* 2010:     */   public XModel getHouses1(String area, XModel dataM)
/* 2011:     */     throws Exception
/* 2012:     */   {
/* 2013:2169 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2014:2170 */     dt.setupTable("houses", "houseno ,address1,pin", "enum_area='" + area + "'", "test", false);
/* 2015:     */     
/* 2016:2172 */     dt.setDistinct(true);
/* 2017:2173 */     dt.retrieve();
/* 2018:     */     
/* 2019:2175 */     this.logger.info(" debug getareas " + dt.getNumChildren());
/* 2020:2176 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 2021:     */     {
/* 2022:2178 */       String id = ((XModel)dt.get(i).get("houseno")).get().toString();
/* 2023:     */       
/* 2024:2180 */       XModel rowM = (XModel)dataM.get(id);
/* 2025:2181 */       rowM.setId(id);
/* 2026:2183 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2027:     */       {
/* 2028:2185 */         String id1 = dt.getAttribName(j);
/* 2029:2186 */         XModel tt = (XModel)rowM.get(id1);
/* 2030:     */         
/* 2031:2188 */         Object val = dt.get(i).get(j).get();
/* 2032:2189 */         val = val == null ? "" : val;
/* 2033:2190 */         tt.set(val);
/* 2034:     */         
/* 2035:2192 */         rowM.append(tt);
/* 2036:     */       }
/* 2037:     */     }
/* 2038:2195 */     return dataM;
/* 2039:     */   }
/* 2040:     */   
/* 2041:     */   public XModel getHousedetails(String house, String area)
/* 2042:     */   {
/* 2043:2200 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2044:2201 */     dt.setupTable("houses", "*", "enum_area=" + area + " and houseno=" + house, "test", true);
/* 2045:2202 */     dt.setName("updatestatus");
/* 2046:2203 */     dt.setId("updatestatus");
/* 2047:2204 */     dt.setTagName("data");
/* 2048:2205 */     dt.retrieve();
/* 2049:2206 */     XBaseModel xm = new XBaseModel();
/* 2050:2207 */     xm.setId("updatestatus");
/* 2051:2209 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 2052:2211 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2053:     */       {
/* 2054:2213 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 2055:2214 */         tt.set(dt.get(i).get(j).get());
/* 2056:     */       }
/* 2057:     */     }
/* 2058:2219 */     return xm;
/* 2059:     */   }
/* 2060:     */   
/* 2061:     */   public XModel getHousedetails(String house, String area, XModel xm)
/* 2062:     */   {
/* 2063:2224 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2064:2225 */     dt.setupTable("houses", "*", "enum_area=" + area + " and houseno=" + house, "test", true);
/* 2065:2226 */     dt.setName("updatestatus");
/* 2066:2227 */     dt.setId("updatestatus");
/* 2067:2228 */     dt.setTagName("data");
/* 2068:2229 */     dt.retrieve();
/* 2069:2231 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 2070:2233 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2071:     */       {
/* 2072:2235 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 2073:2236 */         tt.set(dt.get(i).get(j).get());
/* 2074:     */       }
/* 2075:     */     }
/* 2076:2241 */     return xm;
/* 2077:     */   }
/* 2078:     */   
/* 2079:     */   public String getCurrentUser()
/* 2080:     */   {
/* 2081:2246 */     XModel rootModel = XProjectManager.getCurrentProject().getModel();
/* 2082:2247 */     String currentUser = (String)((XModel)rootModel.get("temp/currentuser")).get();
/* 2083:2248 */     return currentUser;
/* 2084:     */   }
/* 2085:     */   
/* 2086:     */   public synchronized void createChangeLog(String table, String where, Vector keyFields)
/* 2087:     */     throws Exception
/* 2088:     */   {
/* 2089:2254 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2090:     */     
/* 2091:2256 */     dt.setupTable(table, "*", where, "test", true);
/* 2092:     */     
/* 2093:2258 */     dt.retrieve();
/* 2094:2259 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 2095:     */     {
/* 2096:2261 */       String where1 = "";
/* 2097:2262 */       KenList kl = new KenList();
/* 2098:2263 */       for (int k = 0; k < keyFields.size(); k++)
/* 2099:     */       {
/* 2100:2265 */         String value = dt.get(i).get(keyFields.get(k).toString()).toString();
/* 2101:2266 */         kl.add(keyFields.get(k).toString() + "='" + value + "'");
/* 2102:     */       }
/* 2103:2268 */       where1 = kl.toString(" and ");
/* 2104:2269 */       ChangeLog.startLog(table, where1, getCurrentUser());
/* 2105:2270 */       String qry = "update " + table + " set ";
/* 2106:2271 */       int count = 0;
/* 2107:2272 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2108:     */       {
/* 2109:2274 */         String fld = dt.getAttribName(j);
/* 2110:     */         
/* 2111:2276 */         int colType = dt.getMetaData().getColumnType(j + 1);
/* 2112:     */         
/* 2113:2278 */         String value = dt.get(i).get(fld).toString();
/* 2114:2279 */         ChangeLog.logField(fld, value);
/* 2115:2280 */         count++;
/* 2116:     */       }
/* 2117:2282 */       ChangeLog.endLog();
/* 2118:     */     }
/* 2119:     */   }
/* 2120:     */   
/* 2121:     */   public Vector getRange(Vector bookmarks)
/* 2122:     */   {
/* 2123:2288 */     int from = -1;
/* 2124:2289 */     int to = -1;
/* 2125:2290 */     int prev = -1;
/* 2126:2291 */     int cur = -1;
/* 2127:2292 */     int rangebeg = -1;
/* 2128:2293 */     Vector range = new Vector();
/* 2129:2294 */     for (int i = 0; i < bookmarks.size(); i++)
/* 2130:     */     {
/* 2131:2296 */       cur = Integer.parseInt(bookmarks.get(i).toString());
/* 2132:2297 */       System.out.println(" " + prev + "--" + cur);
/* 2133:2298 */       if ((prev == -1) || (cur == prev + 1))
/* 2134:     */       {
/* 2135:2300 */         if (prev == -1) {
/* 2136:2302 */           from = cur;
/* 2137:     */         }
/* 2138:2304 */         prev = cur;
/* 2139:     */       }
/* 2140:     */       else
/* 2141:     */       {
/* 2142:2308 */         to = prev;
/* 2143:2309 */         range.add(from - 1 + "-" + to);
/* 2144:2310 */         prev = cur;
/* 2145:2311 */         from = cur;
/* 2146:     */       }
/* 2147:     */     }
/* 2148:2316 */     range.add(from - 1 + "-" + cur);
/* 2149:2317 */     return range;
/* 2150:     */   }
/* 2151:     */   
/* 2152:     */   public synchronized void createChangeLog1(String table, String where, Vector keyFields, Vector bookmarks)
/* 2153:     */     throws Exception
/* 2154:     */   {
/* 2155:2323 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2156:     */     
/* 2157:2325 */     dt.setupTable(table, "*", where, "test", true);
/* 2158:     */     
/* 2159:2327 */     dt.retrieve();
/* 2160:2328 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 2161:     */     {
/* 2162:2330 */       String where1 = "";
/* 2163:2331 */       KenList kl = new KenList();
/* 2164:2332 */       for (int k = 0; k < keyFields.size(); k++)
/* 2165:     */       {
/* 2166:2334 */         String value = dt.get(i).get(keyFields.get(k).toString()).toString();
/* 2167:2335 */         kl.add(keyFields.get(k).toString() + "='" + value + "'");
/* 2168:     */       }
/* 2169:2337 */       where1 = kl.toString(" and ");
/* 2170:2338 */       ChangeLog.startLog(table, where1, getCurrentUser());
/* 2171:2339 */       String qry = "update " + table + " set ";
/* 2172:2340 */       int count = 0;
/* 2173:2341 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2174:     */       {
/* 2175:2343 */         String fld = dt.getAttribName(j);
/* 2176:     */         
/* 2177:2345 */         int colType = dt.getMetaData().getColumnType(j + 1);
/* 2178:     */         
/* 2179:2347 */         String value = dt.get(i).get(fld).toString();
/* 2180:2348 */         ChangeLog.logField(fld, value);
/* 2181:2349 */         count++;
/* 2182:     */       }
/* 2183:2351 */       ChangeLog.endLog();
/* 2184:2352 */       bookmarks.add(getLastChangeLog());
/* 2185:     */     }
/* 2186:     */   }
/* 2187:     */   
/* 2188:     */   public void saveDataM(String table, String where, XModel dataM)
/* 2189:     */     throws Exception
/* 2190:     */   {
/* 2191:2359 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2192:     */     
/* 2193:2361 */     ChangeLog.startLog(table, where, getCurrentUser());
/* 2194:     */     
/* 2195:2363 */     dt.setupTable(table, "*", where, "test", true);
/* 2196:     */     
/* 2197:2365 */     dt.retrieve();
/* 2198:2366 */     if (dt.getNumChildren() > 0)
/* 2199:     */     {
/* 2200:2368 */       this.logger.info(" total " + dt.getNumChildren());
/* 2201:     */       
/* 2202:2370 */       String qry = "update " + table + " set ";
/* 2203:2371 */       int count = 0;
/* 2204:2372 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2205:     */       {
/* 2206:2374 */         String fld = dt.getAttribName(j);
/* 2207:2375 */         if (!fld.equals("id"))
/* 2208:     */         {
/* 2209:2377 */           int colType = dt.getMetaData().getColumnType(j + 1);
/* 2210:2378 */           String def = colType == 4 ? "0" : null;
/* 2211:2379 */           Object value = dataM.get("@" + fld);
/* 2212:2380 */           value = "'" + value + "'";
/* 2213:2381 */           qry = qry + (count == 0 ? "" : ",") + fld + "=" + value;
/* 2214:2382 */           ChangeLog.logField(fld, (String)value);
/* 2215:2383 */           count++;
/* 2216:     */         }
/* 2217:     */       }
/* 2218:2386 */       int updates = dt.executeUpdate(qry + " where " + where);
/* 2219:2387 */       this.logger.info("updates " + updates);
/* 2220:     */     }
/* 2221:     */     else
/* 2222:     */     {
/* 2223:2391 */       String s = "insert into " + table;
/* 2224:2392 */       String flds = "";
/* 2225:2393 */       String values = "";
/* 2226:2394 */       int count = 0;
/* 2227:2395 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2228:     */       {
/* 2229:2397 */         String fld = dt.getAttribName(j);
/* 2230:2398 */         if (!fld.equals("id"))
/* 2231:     */         {
/* 2232:2400 */           int colType = dt.getMetaData().getColumnType(j + 1);
/* 2233:2401 */           String def = colType == 4 ? "0" : null;
/* 2234:     */           
/* 2235:2403 */           Object value = dataM.get("@" + fld);
/* 2236:2404 */           value = "'" + value + "'";
/* 2237:2405 */           flds = flds + (count != 0 ? "," : "") + fld;
/* 2238:2406 */           values = values + (count != 0 ? "," : "") + value;
/* 2239:2407 */           ChangeLog.logField(fld, (String)value);
/* 2240:2408 */           count++;
/* 2241:     */         }
/* 2242:     */       }
/* 2243:2411 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 2244:     */       
/* 2245:2413 */       this.logger.info(" Debug sql " + s);
/* 2246:     */       
/* 2247:2415 */       dt.executeUpdate(s);
/* 2248:     */       
/* 2249:2417 */       ChangeLog.endLog();
/* 2250:     */     }
/* 2251:     */   }
/* 2252:     */   
/* 2253:     */   public void saveDataM1(String table, String where, XModel dataM)
/* 2254:     */     throws Exception
/* 2255:     */   {
/* 2256:2424 */     this.logger.info("----Here----");
/* 2257:2425 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2258:     */     
/* 2259:2427 */     ChangeLog.startLog(table, where, getCurrentUser());
/* 2260:     */     
/* 2261:2429 */     dt.setupTable(table, "*", where, "test", false);
/* 2262:     */     
/* 2263:2431 */     dt.retrieve();
/* 2264:2432 */     if (dt.getNumChildren() > 0)
/* 2265:     */     {
/* 2266:2434 */       this.logger.info(" total " + dt.getNumChildren());
/* 2267:     */       
/* 2268:2436 */       String qry = "update " + table + " set ";
/* 2269:2437 */       int count = 0;
/* 2270:2438 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 2271:     */       {
/* 2272:2440 */         String fld = dataM.get(j).getId();
/* 2273:2441 */         int col = dt.getAttribute(fld);
/* 2274:2442 */         if (!fld.equals("id"))
/* 2275:     */         {
/* 2276:2444 */           Object value = ((XModel)dataM.get(fld)).get();
/* 2277:     */           
/* 2278:2446 */           qry = qry + (count == 0 ? "" : ",") + fld + "='" + value + "'";
/* 2279:2447 */           ChangeLog.logField(fld, (String)value);
/* 2280:2448 */           count++;
/* 2281:     */         }
/* 2282:     */       }
/* 2283:2451 */       this.logger.info("Update query " + qry);
/* 2284:2452 */       int updates = dt.executeUpdate(qry + " where " + where);
/* 2285:2453 */       this.logger.info("updates " + updates);
/* 2286:2454 */       ChangeLog.endLog();
/* 2287:     */     }
/* 2288:     */     else
/* 2289:     */     {
/* 2290:2458 */       String s = "insert into " + table;
/* 2291:2459 */       String flds = "";
/* 2292:2460 */       String values = "";
/* 2293:2461 */       int count = 0;
/* 2294:2463 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 2295:     */       {
/* 2296:2465 */         String fld = dataM.get(j).getId();
/* 2297:2466 */         int col = dt.getAttribute(fld);
/* 2298:     */         
/* 2299:2468 */         Object value = ((XModel)dataM.get(fld)).get();
/* 2300:     */         
/* 2301:2470 */         flds = flds + (count != 0 ? "," : "") + fld;
/* 2302:2471 */         values = values + (count != 0 ? "," : "") + "'" + value + "'";
/* 2303:2472 */         ChangeLog.logField(fld, (String)value);
/* 2304:2473 */         count++;
/* 2305:     */       }
/* 2306:2475 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 2307:     */       
/* 2308:2477 */       this.logger.info(" Debug sql " + s);
/* 2309:     */       
/* 2310:2479 */       dt.executeUpdate(s);
/* 2311:     */       
/* 2312:2481 */       ChangeLog.endLog();
/* 2313:     */     }
/* 2314:     */   }
/* 2315:     */   
/* 2316:     */   public void saveDataM2(String table, String where, XModel dataM)
/* 2317:     */     throws Exception
/* 2318:     */   {
/* 2319:2488 */     this.logger.info("----Here----");
/* 2320:2489 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2321:     */     
/* 2322:2491 */     dt.setupTable(table, "*", where, "test", false);
/* 2323:     */     
/* 2324:2493 */     dt.retrieve();
/* 2325:2494 */     if (dt.getNumChildren() > 0)
/* 2326:     */     {
/* 2327:2496 */       this.logger.info(" total " + dt.getNumChildren());
/* 2328:     */       
/* 2329:2498 */       String qry = "update " + table + " set ";
/* 2330:2499 */       int count = 0;
/* 2331:2500 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 2332:     */       {
/* 2333:2502 */         String fld = dataM.get(j).getId();
/* 2334:2503 */         int col = dt.getAttribute(fld);
/* 2335:2504 */         if (!fld.equals("id"))
/* 2336:     */         {
/* 2337:2506 */           Object value = ((XModel)dataM.get(fld)).get();
/* 2338:     */           
/* 2339:2508 */           qry = qry + (count == 0 ? "" : ",") + fld + "='" + value + "'";
/* 2340:     */           
/* 2341:2510 */           count++;
/* 2342:     */         }
/* 2343:     */       }
/* 2344:2513 */       this.logger.info("Update query " + qry);
/* 2345:2514 */       int updates = dt.executeUpdate(qry + " where " + where);
/* 2346:2515 */       this.logger.info("updates " + updates);
/* 2347:     */     }
/* 2348:     */     else
/* 2349:     */     {
/* 2350:2519 */       String s = "insert into " + table;
/* 2351:2520 */       String flds = "";
/* 2352:2521 */       String values = "";
/* 2353:2522 */       int count = 0;
/* 2354:2524 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 2355:     */       {
/* 2356:2526 */         String fld = dataM.get(j).getId();
/* 2357:2527 */         int col = dt.getAttribute(fld);
/* 2358:     */         
/* 2359:2529 */         Object value = ((XModel)dataM.get(fld)).get();
/* 2360:     */         
/* 2361:2531 */         flds = flds + (count != 0 ? "," : "") + fld;
/* 2362:2532 */         values = values + (count != 0 ? "," : "") + "'" + value + "'";
/* 2363:     */         
/* 2364:2534 */         count++;
/* 2365:     */       }
/* 2366:2536 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 2367:     */       
/* 2368:2538 */       this.logger.info(" Debug sql " + s);
/* 2369:     */       
/* 2370:2540 */       dt.executeUpdate(s);
/* 2371:     */     }
/* 2372:     */   }
/* 2373:     */   
/* 2374:     */   public void updateTaskStatus(XTaskModel taskM, String status)
/* 2375:     */     throws Exception
/* 2376:     */   {
/* 2377:2547 */     updateTaskStatus(taskM.task, taskM.surveyType, taskM.area, taskM.house, taskM.household, taskM.member, taskM.get("@assignedto").toString(), status);
/* 2378:     */   }
/* 2379:     */   
/* 2380:     */   public void updateTaskStatus(String task, String surveyType, String area, String house, String hh, String idc, String assignedTo, String status)
/* 2381:     */     throws Exception
/* 2382:     */   {
/* 2383:2553 */     if (status == null) {
/* 2384:2554 */       return;
/* 2385:     */     }
/* 2386:2555 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2387:2556 */     String table = "tasks";
/* 2388:     */     
/* 2389:2558 */     String where = getTaskContext(task, area, house, hh, idc, assignedTo);
/* 2390:2559 */     dt.setupTable(table, "*", where, "test", false);
/* 2391:2560 */     dt.retrieve();
/* 2392:2561 */     this.logger.info(" STATUS " + status);
/* 2393:2563 */     if (dt.getNumChildren() > 0)
/* 2394:     */     {
/* 2395:2565 */       ChangeLog.startLog(table, where, getCurrentUser());
/* 2396:     */       
/* 2397:2567 */       String qry = "";
/* 2398:2569 */       if (status.equals("0"))
/* 2399:     */       {
/* 2400:2570 */         String currentStatus = (String)((XModel)dt.get(0).get("status")).get();
/* 2401:2572 */         if (currentStatus == null)
/* 2402:     */         {
/* 2403:2574 */           String starttime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
/* 2404:2575 */           ChangeLog.logField("status", status);
/* 2405:2576 */           ChangeLog.logField("starttime", starttime);
/* 2406:2577 */           qry = "update " + table + " set status='" + status + "' , starttime='" + starttime + "' where " + where;
/* 2407:     */         }
/* 2408:     */         else
/* 2409:     */         {
/* 2410:2580 */           return;
/* 2411:     */         }
/* 2412:     */       }
/* 2413:2583 */       if (status.equals("1"))
/* 2414:     */       {
/* 2415:2584 */         endtime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
/* 2416:2585 */         ChangeLog.logField("status", status);
/* 2417:2586 */         ChangeLog.logField("endtime", endtime);
/* 2418:     */         
/* 2419:2588 */         qry = "update " + table + " set status='" + status + "' , endtime='" + endtime + "' where " + where;
/* 2420:     */       }
/* 2421:2590 */       this.logger.info(" Query " + qry);
/* 2422:     */       
/* 2423:2592 */       String endtime = dt.executeUpdate(qry);
/* 2424:     */     }
/* 2425:     */   }
/* 2426:     */   
/* 2427:     */   public void saveDataMUTF(String table, String where, XModel dataM)
/* 2428:     */     throws Exception
/* 2429:     */   {
/* 2430:2599 */     this.logger.info("----Here----");
/* 2431:2600 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2432:     */     
/* 2433:2602 */     ChangeLog.startLog(table, where, getCurrentUser());
/* 2434:     */     
/* 2435:2604 */     dt.setupTable(table, "*", where, "test", false);
/* 2436:     */     
/* 2437:2606 */     dt.retrieve();
/* 2438:2607 */     if (dt.getNumChildren() > 0)
/* 2439:     */     {
/* 2440:2609 */       this.logger.info(" total " + dt.getNumChildren());
/* 2441:     */       
/* 2442:2611 */       String qry = "update " + table + " set ";
/* 2443:2612 */       int count = 0;
/* 2444:2613 */       Vector values1 = new Vector();
/* 2445:2614 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 2446:     */       {
/* 2447:2616 */         String fld = dataM.get(j).getId();
/* 2448:2617 */         int col = dt.getAttribute(fld);
/* 2449:     */         
/* 2450:2619 */         Object value = ((XModel)dataM.get(fld)).get();
/* 2451:     */         
/* 2452:2621 */         values1.add(value);
/* 2453:2622 */         qry = qry + (count == 0 ? "" : ",") + fld + "=?";
/* 2454:2623 */         ChangeLog.logField(fld, (String)value);
/* 2455:2624 */         count++;
/* 2456:     */       }
/* 2457:2627 */       String s = qry + " where " + where;
/* 2458:2628 */       this.logger.info("Update query " + s);
/* 2459:2629 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2460:2631 */       for (int i = 0; i < values1.size(); i++)
/* 2461:     */       {
/* 2462:2633 */         String val = (String)values1.get(i);
/* 2463:2634 */         if (val != null) {
/* 2464:2635 */           ps.setBytes(i + 1, val.getBytes("utf-8"));
/* 2465:     */         } else {
/* 2466:2637 */           ps.setString(i + 1, null);
/* 2467:     */         }
/* 2468:     */       }
/* 2469:2639 */       this.logger.info(" No of updates " + ps.executeUpdate());
/* 2470:2640 */       closePs(dt, ps);
/* 2471:2641 */       ChangeLog.endLog();
/* 2472:     */     }
/* 2473:     */     else
/* 2474:     */     {
/* 2475:2645 */       String s = "insert into " + table;
/* 2476:2646 */       String flds = "";
/* 2477:2647 */       String values = "";
/* 2478:2648 */       int count = 0;
/* 2479:2649 */       Vector values1 = new Vector();
/* 2480:2651 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 2481:     */       {
/* 2482:2653 */         String fld = dataM.get(j).getId();
/* 2483:2654 */         int col = dt.getAttribute(fld);
/* 2484:     */         
/* 2485:2656 */         Object value = ((XModel)dataM.get(fld)).get();
/* 2486:     */         
/* 2487:2658 */         flds = flds + (count != 0 ? "," : "") + fld;
/* 2488:     */         
/* 2489:2660 */         values = values + (count != 0 ? "," : "") + "?";
/* 2490:2661 */         values1.add(value);
/* 2491:2662 */         ChangeLog.logField(fld, (String)value);
/* 2492:2663 */         count++;
/* 2493:     */       }
/* 2494:2665 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 2495:     */       
/* 2496:2667 */       this.logger.info(" Debug sql " + s);
/* 2497:     */       
/* 2498:2669 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2499:2671 */       for (int i = 0; i < values1.size(); i++)
/* 2500:     */       {
/* 2501:2673 */         String val = (String)values1.get(i);
/* 2502:2674 */         if (val != null) {
/* 2503:2675 */           ps.setBytes(i + 1, val.getBytes("utf-8"));
/* 2504:     */         } else {
/* 2505:2677 */           ps.setString(i + 1, null);
/* 2506:     */         }
/* 2507:     */       }
/* 2508:2679 */       ps.execute();
/* 2509:2680 */       closePs(dt, ps);
/* 2510:2681 */       ChangeLog.endLog();
/* 2511:     */     }
/* 2512:     */   }
/* 2513:     */   
/* 2514:     */   public void saveDataM2UTF(String table, String where, XModel dataM)
/* 2515:     */     throws Exception
/* 2516:     */   {
/* 2517:2688 */     this.logger.info("----Here----");
/* 2518:2689 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2519:     */     
/* 2520:2691 */     dt.setupTable(table, "*", where, "test", false);
/* 2521:     */     
/* 2522:2693 */     dt.retrieve();
/* 2523:2694 */     if (dt.getNumChildren() > 0)
/* 2524:     */     {
/* 2525:2696 */       this.logger.info(" total " + dt.getNumChildren());
/* 2526:     */       
/* 2527:2698 */       String qry = "update " + table + " set ";
/* 2528:2699 */       int count = 0;
/* 2529:2700 */       Vector values1 = new Vector();
/* 2530:2701 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 2531:     */       {
/* 2532:2703 */         String fld = dataM.get(j).getId();
/* 2533:2704 */         int col = dt.getAttribute(fld);
/* 2534:     */         
/* 2535:2706 */         Object value = ((XModel)dataM.get(fld)).get();
/* 2536:     */         
/* 2537:2708 */         values1.add(value);
/* 2538:2709 */         qry = qry + (count == 0 ? "" : ",") + fld + "=?";
/* 2539:     */         
/* 2540:2711 */         count++;
/* 2541:     */       }
/* 2542:2714 */       String s = qry + " where " + where;
/* 2543:2715 */       this.logger.info("Update query " + s);
/* 2544:2716 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2545:2718 */       for (int i = 0; i < values1.size(); i++)
/* 2546:     */       {
/* 2547:2720 */         String val = (String)values1.get(i);
/* 2548:2721 */         if (val != null) {
/* 2549:2722 */           ps.setBytes(i + 1, val.getBytes("utf-8"));
/* 2550:     */         } else {
/* 2551:2724 */           ps.setString(i + 1, null);
/* 2552:     */         }
/* 2553:     */       }
/* 2554:2726 */       this.logger.info(" No of updates " + ps.executeUpdate());
/* 2555:2727 */       closePs(dt, ps);
/* 2556:     */     }
/* 2557:     */     else
/* 2558:     */     {
/* 2559:2731 */       String s = "insert into " + table;
/* 2560:2732 */       String flds = "";
/* 2561:2733 */       String values = "";
/* 2562:2734 */       int count = 0;
/* 2563:2735 */       Vector values1 = new Vector();
/* 2564:2737 */       for (int j = 0; j < dataM.getNumChildren(); j++)
/* 2565:     */       {
/* 2566:2739 */         String fld = dataM.get(j).getId();
/* 2567:2740 */         int col = dt.getAttribute(fld);
/* 2568:     */         
/* 2569:2742 */         Object value = ((XModel)dataM.get(fld)).get();
/* 2570:     */         
/* 2571:2744 */         flds = flds + (count != 0 ? "," : "") + fld;
/* 2572:     */         
/* 2573:2746 */         values = values + (count != 0 ? "," : "") + "?";
/* 2574:2747 */         values1.add(value);
/* 2575:     */         
/* 2576:2749 */         count++;
/* 2577:     */       }
/* 2578:2751 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 2579:     */       
/* 2580:2753 */       this.logger.info(" Debug sql " + s);
/* 2581:     */       
/* 2582:2755 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 2583:2757 */       for (int i = 0; i < values1.size(); i++)
/* 2584:     */       {
/* 2585:2759 */         String val = (String)values1.get(i);
/* 2586:2760 */         if (val != null) {
/* 2587:2761 */           ps.setBytes(i + 1, val.getBytes("utf-8"));
/* 2588:     */         } else {
/* 2589:2763 */           ps.setString(i + 1, null);
/* 2590:     */         }
/* 2591:     */       }
/* 2592:2765 */       ps.execute();
/* 2593:2766 */       closePs(dt, ps);
/* 2594:     */     }
/* 2595:     */   }
/* 2596:     */   
/* 2597:     */   public void validateMessage(String message)
/* 2598:     */     throws Exception
/* 2599:     */   {
/* 2600:2773 */     FileReader sr = new FileReader(message);
/* 2601:2774 */     XmlElement xe = XmlSource.read(sr);
/* 2602:2775 */     String logId = xe.getAttribute("id");
/* 2603:2776 */     XmlElement dt1 = xe.elementAt(0);
/* 2604:2777 */     String table = dt1.getAttribute("table");
/* 2605:2778 */     String where = dt1.getAttribute("key");
/* 2606:2779 */     String user1 = dt1.getAttribute("user");
/* 2607:2780 */     String op = dt1.getAttribute("op");
/* 2608:     */   }
/* 2609:     */   
/* 2610:     */   public void importChangeLog(String log)
/* 2611:     */     throws Exception
/* 2612:     */   {
/* 2613:2786 */     StringReader sr = new StringReader(log);
/* 2614:2787 */     XmlElement xe = XmlSource.read(sr);
/* 2615:2788 */     String logId = xe.getAttribute("id");
/* 2616:2789 */     XmlElement dt1 = xe.elementAt(0);
/* 2617:2790 */     String table = dt1.getAttribute("table");
/* 2618:2791 */     String where = dt1.getAttribute("key");
/* 2619:2792 */     String user1 = dt1.getAttribute("user");
/* 2620:2793 */     String op = dt1.getAttribute("op");
/* 2621:2794 */     XmlElement data = dt1.elementAt(0);
/* 2622:2795 */     Enumeration e = data.enumerateAttributeNames();
/* 2623:2796 */     if ((op != null) && (op.equals("deleteAll")))
/* 2624:     */     {
/* 2625:2798 */       deleteResources("./images");
/* 2626:2799 */       deleteAllData();
/* 2627:     */     }
/* 2628:2801 */     else if ((op != null) && (op.equals("deleteResource")))
/* 2629:     */     {
/* 2630:2803 */       deleteResource(table, user);
/* 2631:     */     }
/* 2632:2805 */     else if ((op != null) && (op.equals("delete")))
/* 2633:     */     {
/* 2634:2807 */       deleteData1(table, where, data, user);
/* 2635:     */     }
/* 2636:     */     else
/* 2637:     */     {
/* 2638:2810 */       saveData1(table, where, data, user1);
/* 2639:     */     }
/* 2640:     */   }
/* 2641:     */   
/* 2642:     */   public void logImport(String fname, String status, String reason)
/* 2643:     */     throws Exception
/* 2644:     */   {
/* 2645:     */     try
/* 2646:     */     {
/* 2647:2819 */       this.logger.info(" Inside log import");
/* 2648:2820 */       XModel dataM = new XBaseModel();
/* 2649:2821 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 2650:2822 */       ((XModel)dataM.get("time")).set(sdf.format(new Date()));
/* 2651:2823 */       ((XModel)dataM.get("filename")).set(fname);
/* 2652:2824 */       ((XModel)dataM.get("status")).set(status);
/* 2653:2825 */       ((XModel)dataM.get("reason")).set(reason.replaceAll("'", "").replaceAll("\"", ""));
/* 2654:2826 */       ((XModel)dataM.get("user")).set(getCurrentUser());
/* 2655:2827 */       saveDataM2("import", "filename='" + fname + "'", dataM);
/* 2656:     */     }
/* 2657:     */     catch (Exception e)
/* 2658:     */     {
/* 2659:2831 */       e.printStackTrace();
/* 2660:     */     }
/* 2661:     */   }
/* 2662:     */   
/* 2663:     */   public void processIncompleteImports()
/* 2664:     */     throws Exception
/* 2665:     */   {
/* 2666:2838 */     String table = "import";
/* 2667:2839 */     String where = " status='processing'";
/* 2668:2840 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2669:2841 */     dt.setupTable(table, "*", where, "test", true);
/* 2670:2842 */     if (dt.getNumChildren() > 0) {
/* 2671:2844 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 2672:     */       {
/* 2673:2846 */         XModel row = dt.get(i);
/* 2674:2847 */         String fname = (String)((XModel)row.get("filename")).get();
/* 2675:2848 */         this.logger.info(fname);
/* 2676:     */         
/* 2677:2850 */         new Client().reRun(fname);
/* 2678:     */       }
/* 2679:     */     }
/* 2680:     */   }
/* 2681:     */   
/* 2682:     */   public void processIncompleteImports1()
/* 2683:     */     throws Exception
/* 2684:     */   {
/* 2685:2858 */     String table = "import";
/* 2686:2859 */     String where = " status='processing'";
/* 2687:2860 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2688:2861 */     dt.setupTable(table, "*", where, "test", true);
/* 2689:2862 */     if (dt.getNumChildren() > 0) {
/* 2690:2864 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 2691:     */       {
/* 2692:2866 */         XModel row = dt.get(i);
/* 2693:2867 */         String fname = (String)((XModel)row.get("filename")).get();
/* 2694:2868 */         this.logger.info(fname);
/* 2695:2869 */         importChangeLogs1(fname);
/* 2696:     */       }
/* 2697:     */     }
/* 2698:     */   }
/* 2699:     */   
/* 2700:     */   public void importChangeLogs1(String fname)
/* 2701:     */     throws Exception
/* 2702:     */   {
/* 2703:2877 */     this.logger.info(" Inside import1");
/* 2704:     */     try
/* 2705:     */     {
/* 2706:2880 */       InputStreamReader r = new InputStreamReader(new FileInputStream(fname));
/* 2707:2881 */       this.logger.info(" Reader got" + r);
/* 2708:2882 */       XmlElement xe0 = XmlSource.read(r);
/* 2709:2883 */       r.close();
/* 2710:2884 */       this.logger.info(" Read " + fname);
/* 2711:2885 */       Vector children = xe0.getChildren();
/* 2712:2886 */       this.logger.info(" Children " + xe0.getChildren());
/* 2713:2887 */       for (int i = 0; i < children.size(); i++)
/* 2714:     */       {
/* 2715:2889 */         XmlElement xe = (XmlElement)children.get(i);
/* 2716:     */         
/* 2717:2891 */         String logId = xe.getAttribute("id");
/* 2718:2892 */         XmlElement dt1 = xe.elementAt(0);
/* 2719:2893 */         String table = dt1.getAttribute("table");
/* 2720:2894 */         String where = dt1.getAttribute("key");
/* 2721:2895 */         String user = dt1.getAttribute("user");
/* 2722:2896 */         String op = dt1.getAttribute("op");
/* 2723:2897 */         XmlElement data = dt1.elementAt(0);
/* 2724:2898 */         Enumeration e = data.enumerateAttributeNames();
/* 2725:2899 */         while (e.hasMoreElements()) {
/* 2726:2901 */           this.logger.info(data.getAttribute(e.nextElement().toString()));
/* 2727:     */         }
/* 2728:     */         try
/* 2729:     */         {
/* 2730:2906 */           if ((op != null) && (op.equals("deleteAll")))
/* 2731:     */           {
/* 2732:2908 */             deleteResources("./images");
/* 2733:2909 */             deleteAllData();
/* 2734:     */           }
/* 2735:2911 */           else if ((op != null) && (op.equals("deleteResource")))
/* 2736:     */           {
/* 2737:2913 */             deleteResource(table, user);
/* 2738:     */           }
/* 2739:2915 */           else if ((op != null) && (op.equals("delete")))
/* 2740:     */           {
/* 2741:2917 */             deleteData1(table, where, data, user);
/* 2742:     */           }
/* 2743:     */           else
/* 2744:     */           {
/* 2745:2921 */             saveData1(table, where, data, user);
/* 2746:     */           }
/* 2747:     */         }
/* 2748:     */         catch (Exception e1)
/* 2749:     */         {
/* 2750:2927 */           e1.printStackTrace();
/* 2751:     */         }
/* 2752:     */       }
/* 2753:     */     }
/* 2754:     */     catch (Exception e2)
/* 2755:     */     {
/* 2756:2935 */       e2.printStackTrace();
/* 2757:     */     }
/* 2758:     */   }
/* 2759:     */   
/* 2760:     */   public void importChangeLogs(String logs)
/* 2761:     */     throws Exception
/* 2762:     */   {
/* 2763:2942 */     this.logger.info(" Inside import");
/* 2764:     */     
/* 2765:2944 */     StringReader sr = new StringReader(logs);
/* 2766:2945 */     XmlElement xe0 = XmlSource.read(sr);
/* 2767:2946 */     Vector children = xe0.getChildren();
/* 2768:2947 */     for (int i = 0; i < children.size(); i++)
/* 2769:     */     {
/* 2770:2949 */       XmlElement xe = (XmlElement)children.get(i);
/* 2771:2950 */       String logId = xe.getAttribute("id");
/* 2772:2951 */       XmlElement dt1 = xe.elementAt(0);
/* 2773:2952 */       String table = dt1.getAttribute("table");
/* 2774:2953 */       String where = dt1.getAttribute("key");
/* 2775:2954 */       String user = dt1.getAttribute("user");
/* 2776:2955 */       String op = dt1.getAttribute("op");
/* 2777:2956 */       XmlElement data = dt1.elementAt(0);
/* 2778:2957 */       Enumeration e = data.enumerateAttributeNames();
/* 2779:     */       try
/* 2780:     */       {
/* 2781:2959 */         if ((op != null) && (op.equals("deleteAll")))
/* 2782:     */         {
/* 2783:2961 */           deleteResources("./images");
/* 2784:2962 */           deleteAllData();
/* 2785:     */         }
/* 2786:2964 */         else if ((op != null) && (op.equals("deleteResource")))
/* 2787:     */         {
/* 2788:2966 */           deleteResource(table, user);
/* 2789:     */         }
/* 2790:2968 */         else if ((op != null) && (op.equals("delete")))
/* 2791:     */         {
/* 2792:2970 */           deleteData1(table, where, data, user);
/* 2793:     */         }
/* 2794:     */         else
/* 2795:     */         {
/* 2796:2974 */           saveData1(table, where, data, user);
/* 2797:     */         }
/* 2798:     */       }
/* 2799:     */       catch (Exception e1)
/* 2800:     */       {
/* 2801:2979 */         e1.printStackTrace();
/* 2802:     */       }
/* 2803:     */     }
/* 2804:2982 */     clearPool();
/* 2805:     */   }
/* 2806:     */   
/* 2807:     */   public void distributeMessage(String msg, String to)
/* 2808:     */     throws Exception
/* 2809:     */   {
/* 2810:2988 */     String from = msg.split("-")[0];
/* 2811:2989 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2812:     */     
/* 2813:2991 */     dt.setupTable("msg_distribution", "*", "from1='" + from + "' and to1='" + to + "'", "test", false);
/* 2814:2992 */     dt.retrieve();
/* 2815:2993 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 2816:     */     {
/* 2817:2995 */       XModel row = dt.get(i);
/* 2818:2996 */       deliverMessage(msg, row.get("cc").toString());
/* 2819:     */     }
/* 2820:     */   }
/* 2821:     */   
/* 2822:     */   public void deliverMessage(String msg, String recepient)
/* 2823:     */     throws Exception
/* 2824:     */   {
/* 2825:3003 */     this.logger.info("Inside 2");
/* 2826:3004 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2827:     */     
/* 2828:3006 */     dt.setupTable("mqueue", "*", "", "test", false);
/* 2829:3007 */     String sql = "insert into mqueue (message,recepient,delivertime) VALUES('" + msg + "','" + recepient + "',NOW())";
/* 2830:3008 */     this.logger.info(sql);
/* 2831:3009 */     dt.executeUpdate(sql);
/* 2832:     */     
/* 2833:3011 */     this.logger.info(" Completed " + sql);
/* 2834:     */   }
/* 2835:     */   
/* 2836:     */   public void updateMessageStatus(String message, String recepient)
/* 2837:     */     throws Exception
/* 2838:     */   {
/* 2839:3017 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2840:3018 */     dt.setupTable("mqueue", "*", "", "test", true);
/* 2841:     */     
/* 2842:3020 */     dt.executeUpdate("update mqueue set status='1',downloadtime=NOW() where message='" + message + "' and recepient='" + recepient + "'");
/* 2843:     */   }
/* 2844:     */   
/* 2845:     */   public void updateAckStatus(String message, String recepient)
/* 2846:     */     throws Exception
/* 2847:     */   {
/* 2848:3026 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2849:3027 */     dt.setupTable("mqueue", "*", "", "test", true);
/* 2850:     */     
/* 2851:3029 */     dt.executeUpdate("update mqueue set ack='1',acktime=NOW() where message='" + message + "' and recepient='" + recepient + "'");
/* 2852:     */   }
/* 2853:     */   
/* 2854:     */   public Vector getMessages(String recepient)
/* 2855:     */     throws Exception
/* 2856:     */   {
/* 2857:3035 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2858:3036 */     Vector v = new Vector();
/* 2859:3037 */     String where = " recepient='" + recepient + "' and status is null";
/* 2860:3038 */     dt.setupTable("mqueue", "distinct message", where, "test", true);
/* 2861:     */     
/* 2862:3040 */     dt.retrieve();
/* 2863:3041 */     if (dt.getNumChildren() > 0) {
/* 2864:3043 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 2865:     */       {
/* 2866:3045 */         XModel row = dt.get(i);
/* 2867:     */         
/* 2868:3047 */         this.logger.info(" total " + dt.getNumChildren());
/* 2869:3048 */         String message = (String)row.get(0).get();
/* 2870:3049 */         v.add(message);
/* 2871:     */       }
/* 2872:     */     }
/* 2873:3052 */     return v;
/* 2874:     */   }
/* 2875:     */   
/* 2876:     */   public void saveChangeLog(String log)
/* 2877:     */     throws Exception
/* 2878:     */   {
/* 2879:3058 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2880:     */     
/* 2881:3060 */     dt.setupTable("changelogs", "*", "", "test", true);
/* 2882:     */     
/* 2883:3062 */     String s = "insert into changelogs";
/* 2884:3063 */     String flds = "";
/* 2885:3064 */     String values = "";
/* 2886:3065 */     s = s + " (value) VALUES(" + log + ")";
/* 2887:     */     
/* 2888:3067 */     this.logger.info(" Debug sql " + s);
/* 2889:     */     
/* 2890:3069 */     dt.executeUpdate(s);
/* 2891:     */   }
/* 2892:     */   
/* 2893:     */   public void addToResourceOutboundQueue(String recepient, String resource, String resource1, String participant, String packageId)
/* 2894:     */     throws Exception
/* 2895:     */   {
/* 2896:3075 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2897:     */     
/* 2898:3077 */     dt.setupTable("resource_outbound_queue", "*", "", "test", true);
/* 2899:     */     
/* 2900:3079 */     String s = "insert into resource_outbound_queue";
/* 2901:3080 */     String flds = "";
/* 2902:3081 */     String values = "";
/* 2903:3082 */     s = s + " (recepient,resource,resource1,participant,packageId) VALUES('" + recepient + "','" + resource + "','" + resource1 + "','" + participant + "','" + packageId + "')";
/* 2904:     */     
/* 2905:3084 */     this.logger.info(" Debug sql " + s);
/* 2906:     */     
/* 2907:3086 */     dt.executeUpdate(s);
/* 2908:     */   }
/* 2909:     */   
/* 2910:     */   public void addToResourceOutboundQueue(String recepient, String resource, String resource1, String participant)
/* 2911:     */     throws Exception
/* 2912:     */   {
/* 2913:3092 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2914:     */     
/* 2915:3094 */     dt.setupTable("resource_outbound_queue", "*", "", "test", true);
/* 2916:     */     
/* 2917:3096 */     String s = "insert into resource_outbound_queue";
/* 2918:3097 */     String flds = "";
/* 2919:3098 */     String values = "";
/* 2920:3099 */     s = s + " (recepient,resource,resource1,participant) VALUES('" + recepient + "','" + resource + "','" + resource1 + "','" + participant + "')";
/* 2921:     */     
/* 2922:3101 */     this.logger.info(" Debug sql " + s);
/* 2923:     */     
/* 2924:3103 */     dt.executeUpdate(s);
/* 2925:     */   }
/* 2926:     */   
/* 2927:     */   public void addToChangeLogOutboundQueue(String recepient, String frombookmark, String tobookmark, String packageId)
/* 2928:     */     throws Exception
/* 2929:     */   {
/* 2930:3109 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2931:3110 */     frombookmark = frombookmark == null ? "0" : frombookmark;
/* 2932:3111 */     dt.setupTable("changelog_outbound_queue", "*", "", "test", true);
/* 2933:     */     
/* 2934:3113 */     String s = "insert into changelog_outbound_queue";
/* 2935:3114 */     String flds = "";
/* 2936:3115 */     String values = "";
/* 2937:3116 */     s = s + " (recepient,frombookmark,tobookmark,package_id) VALUES('" + recepient + "','" + frombookmark + "','" + tobookmark + "','" + packageId + "')";
/* 2938:     */     
/* 2939:3118 */     this.logger.info(" Debug sql " + s);
/* 2940:     */     
/* 2941:3120 */     dt.executeUpdate(s);
/* 2942:     */   }
/* 2943:     */   
/* 2944:     */   public void sendChangesToRecepient(String recepient, String where)
/* 2945:     */     throws Exception
/* 2946:     */   {
/* 2947:3125 */     XModel xm = new XBaseModel();
/* 2948:3126 */     getData("changelogs", "id", where, xm);
/* 2949:3128 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 2950:     */     {
/* 2951:3130 */       String tobookmark = xm.get(i).get(0).get().toString();
/* 2952:3131 */       int frombookmark = Integer.parseInt(tobookmark) - 1;
/* 2953:3132 */       addToChangeLogOutboundQueue(recepient, frombookmark, tobookmark);
/* 2954:     */     }
/* 2955:3135 */     sendOutboundLogs(recepient);
/* 2956:     */   }
/* 2957:     */   
/* 2958:     */   public void addToChangeLogOutboundQueue(String recepient, String frombookmark, String tobookmark)
/* 2959:     */     throws Exception
/* 2960:     */   {
/* 2961:3146 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2962:3147 */     frombookmark = frombookmark == null ? "0" : frombookmark;
/* 2963:3148 */     dt.setupTable("changelog_outbound_queue", "*", "", "test", true);
/* 2964:     */     
/* 2965:3150 */     String s = "insert into changelog_outbound_queue";
/* 2966:3151 */     String flds = "";
/* 2967:3152 */     String values = "";
/* 2968:3153 */     s = s + " (recepient,frombookmark,tobookmark) VALUES('" + recepient + "','" + frombookmark + "','" + tobookmark + "')";
/* 2969:     */     
/* 2970:3155 */     this.logger.info(" Debug sql " + s);
/* 2971:     */     
/* 2972:3157 */     dt.executeUpdate(s);
/* 2973:     */   }
/* 2974:     */   
/* 2975:     */   public void saveData(String table, String where, XModel dataM)
/* 2976:     */     throws Exception
/* 2977:     */   {
/* 2978:3163 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 2979:     */     
/* 2980:3165 */     dt.setupTable(table, "*", where, "test", true);
/* 2981:     */     
/* 2982:3167 */     dt.retrieve();
/* 2983:3168 */     if (dt.getNumChildren() > 0)
/* 2984:     */     {
/* 2985:3170 */       this.logger.info(" total " + dt.getNumChildren());
/* 2986:     */       
/* 2987:3172 */       String qry = "update " + table + " set ";
/* 2988:3173 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 2989:     */       {
/* 2990:3175 */         String fld = dt.getAttribName(j);
/* 2991:3176 */         XModel valM = (XModel)dataM.get(fld);
/* 2992:3177 */         int colType = dt.getMetaData().getColumnType(j + 1);
/* 2993:3178 */         String def = colType == 4 ? "0" : null;
/* 2994:     */         
/* 2995:3180 */         Object value = valM == null ? null : valM.get();
/* 2996:3181 */         value = "'" + value + "'";
/* 2997:3182 */         qry = qry + (j == 0 ? "" : ",") + fld + "=" + value;
/* 2998:     */       }
/* 2999:3185 */       int updates = dt.executeUpdate(qry + " where " + where);
/* 3000:3186 */       this.logger.info("updates " + updates);
/* 3001:     */     }
/* 3002:     */     else
/* 3003:     */     {
/* 3004:3190 */       String s = "insert into " + table;
/* 3005:3191 */       String flds = "";
/* 3006:3192 */       String values = "";
/* 3007:3193 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 3008:     */       {
/* 3009:3195 */         String fld = dt.getAttribName(j);
/* 3010:3196 */         XModel valM = (XModel)dataM.get(fld);
/* 3011:3197 */         int colType = dt.getMetaData().getColumnType(j + 1);
/* 3012:3198 */         String def = colType == 4 ? "0" : null;
/* 3013:     */         
/* 3014:3200 */         Object value = valM == null ? null : valM.get();
/* 3015:3201 */         value = "'" + value + "'";
/* 3016:3202 */         flds = flds + (j != 0 ? "," : "") + fld;
/* 3017:3203 */         values = values + (j != 0 ? "," : "") + value;
/* 3018:     */       }
/* 3019:3205 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 3020:     */       
/* 3021:3207 */       this.logger.info(" Debug sql " + s);
/* 3022:     */       
/* 3023:3209 */       dt.executeUpdate(s);
/* 3024:     */     }
/* 3025:     */   }
/* 3026:     */   
/* 3027:     */   public void saveConflictData(String table, String where, XModel dataM)
/* 3028:     */     throws Exception
/* 3029:     */   {
/* 3030:3216 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3031:     */     
/* 3032:3218 */     dt.setupTable(table, "*", where, "test", false);
/* 3033:     */     
/* 3034:3220 */     dt.retrieve();
/* 3035:     */     
/* 3036:3222 */     String s = "insert into " + table;
/* 3037:3223 */     String flds = "";
/* 3038:3224 */     String values = "";
/* 3039:3225 */     for (int j = 0; j < dt.getNumAttributes(); j++)
/* 3040:     */     {
/* 3041:3227 */       String fld = dt.getAttribName(j);
/* 3042:3228 */       XModel valM = (XModel)dataM.get(fld);
/* 3043:3229 */       int colType = dt.getMetaData().getColumnType(j + 1);
/* 3044:3230 */       String def = colType == 4 ? "0" : null;
/* 3045:     */       
/* 3046:3232 */       Object value = valM == null ? null : valM.get();
/* 3047:     */       
/* 3048:3234 */       flds = flds + (j != 0 ? "," : "") + fld;
/* 3049:3235 */       values = values + (j != 0 ? "," : "") + value;
/* 3050:     */     }
/* 3051:3237 */     s = s + "(" + flds + ") VALUES(" + values + ")";
/* 3052:     */     
/* 3053:3239 */     this.logger.info(" Debug sql " + s);
/* 3054:     */     
/* 3055:3241 */     dt.executeUpdate(s);
/* 3056:     */   }
/* 3057:     */   
/* 3058:     */   public void closePs(DatabaseTableModel dt, PreparedStatement ps)
/* 3059:     */   {
/* 3060:     */     try
/* 3061:     */     {
/* 3062:3248 */       if (ps.getResultSet() != null) {
/* 3063:3249 */         ps.getResultSet().close();
/* 3064:     */       }
/* 3065:3250 */       dt.getTable().releasePreparedStatement(ps);
/* 3066:     */     }
/* 3067:     */     catch (SQLException e)
/* 3068:     */     {
/* 3069:3254 */       System.out.println(e.getMessage());
/* 3070:     */     }
/* 3071:     */   }
/* 3072:     */   
/* 3073:     */   public void saveData1(String table, String where, XmlElement dataM, String user1)
/* 3074:     */     throws Exception
/* 3075:     */   {
/* 3076:3261 */     XModel conflictData = new XBaseModel();
/* 3077:3262 */     XModel oldData = new XBaseModel();
/* 3078:3263 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 3079:3264 */     conflictData.set("date", "'" + sdf.format(new Date()) + "'");
/* 3080:3265 */     conflictData.set("surveyor", "'" + user1 + "'");
/* 3081:3266 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3082:3267 */     this.logger.info(" Where is " + where);
/* 3083:3268 */     dt.setupTable(table, "*", where, "test", true);
/* 3084:3269 */     Vector values1 = new Vector();
/* 3085:     */     
/* 3086:3271 */     dt.retrieve();
/* 3087:3272 */     if (table.equals("data"))
/* 3088:     */     {
/* 3089:3274 */       String value1 = dataM.getAttribute("value");
/* 3090:3275 */       if ((value1 == null) || (value1.equals(" Choose Any One"))) {
/* 3091:3276 */         return;
/* 3092:     */       }
/* 3093:     */     }
/* 3094:3278 */     this.logger.info(" Debug children " + dt.getNumChildren());
/* 3095:3279 */     if (dt.getNumChildren() > 0)
/* 3096:     */     {
/* 3097:3281 */       this.logger.info(" total " + dt.getNumChildren());
/* 3098:     */       
/* 3099:3283 */       String qry = "update " + table + " set ";
/* 3100:3284 */       int count = 0;
/* 3101:3285 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 3102:     */       {
/* 3103:3287 */         String fld = dt.getAttribName(j).toLowerCase();
/* 3104:3288 */         String fld1 = fld;
/* 3105:3289 */         if (fld.equals("id")) {
/* 3106:3290 */           fld = "_id";
/* 3107:     */         }
/* 3108:3291 */         String value = dataM.getAttribute(fld);
/* 3109:3292 */         String oldValue = "";
/* 3110:     */         try
/* 3111:     */         {
/* 3112:3294 */           oldValue = (String)((XModel)dt.get(0).get(fld1)).get();
/* 3113:3295 */           this.logger.info(" Value " + fld1 + " " + oldValue);
/* 3114:     */         }
/* 3115:     */         catch (Exception e)
/* 3116:     */         {
/* 3117:3299 */           e.printStackTrace();
/* 3118:     */         }
/* 3119:3301 */         int colType = dt.getMetaData().getColumnType(j + 1);
/* 3120:3302 */         String def = colType == 4 ? "0" : null;
/* 3121:     */         
/* 3122:3304 */         conflictData.set(fld, value);
/* 3123:3305 */         oldData.set(fld1, oldValue);
/* 3124:3307 */         if ((value != null) && (!fld.equals("id")) && ((!table.equals("keyvalue")) || (!fld.equals("key1"))))
/* 3125:     */         {
/* 3126:3309 */           qry = qry + (count == 0 ? "" : ",") + fld + "=?";
/* 3127:3310 */           values1.add(value);
/* 3128:3311 */           count++;
/* 3129:     */         }
/* 3130:     */       }
/* 3131:3315 */       if (!table.equals("data")) {
/* 3132:3317 */         if (table.equals("tasks"))
/* 3133:     */         {
/* 3134:3319 */           String status = dataM.getAttribute("status");
/* 3135:3320 */           String endtime = dataM.getAttribute("endtime");
/* 3136:3321 */           if (((status != null) && ((status.equals("2")) || (status.equals("5")))) || ((endtime != null) && (endtime.length() > 0)))
/* 3137:     */           {
/* 3138:3323 */             this.logger.info(qry + " where " + where);
/* 3139:     */             try
/* 3140:     */             {
/* 3141:3325 */               PreparedStatement ps = dt.getTable().getPreparedStatement(qry + " where " + where);
/* 3142:3327 */               for (int i = 0; i < values1.size(); i++) {
/* 3143:3329 */                 ps.setBytes(i + 1, values1.get(i).toString().getBytes("utf-8"));
/* 3144:     */               }
/* 3145:3332 */               int updates = ps.executeUpdate();
/* 3146:     */               
/* 3147:3334 */               closePs(dt, ps);
/* 3148:     */               
/* 3149:3336 */               this.logger.info("updates " + updates);
/* 3150:     */             }
/* 3151:     */             catch (Exception e)
/* 3152:     */             {
/* 3153:3340 */               e.printStackTrace();
/* 3154:     */             }
/* 3155:     */           }
/* 3156:     */         }
/* 3157:3346 */         else if (table.equals("keyvalue"))
/* 3158:     */         {
/* 3159:3348 */           this.logger.info(" query " + qry);
/* 3160:3349 */           PreparedStatement ps = dt.getTable().getPreparedStatement(qry + " where " + where);
/* 3161:3351 */           for (int i = 0; i < values1.size(); i++) {
/* 3162:3353 */             if (values1.get(i) != null) {
/* 3163:3355 */               ps.setBytes(i + 1, values1.get(i).toString().getBytes("utf-8"));
/* 3164:     */             } else {
/* 3165:3359 */               ps.setString(i + 1, null);
/* 3166:     */             }
/* 3167:     */           }
/* 3168:3364 */           int updates = ps.executeUpdate();
/* 3169:     */           
/* 3170:3366 */           closePs(dt, ps);
/* 3171:     */           
/* 3172:3368 */           this.logger.info("updates " + updates);
/* 3173:     */         }
/* 3174:     */         else
/* 3175:     */         {
/* 3176:3372 */           this.logger.info(" query " + qry);
/* 3177:     */           
/* 3178:3374 */           PreparedStatement ps = dt.getTable().getPreparedStatement(qry + " where " + where);
/* 3179:3376 */           for (int i = 0; i < values1.size(); i++)
/* 3180:     */           {
/* 3181:3378 */             this.logger.info("Value of " + i + " is " + values1.get(i));
/* 3182:3379 */             if (values1.get(i) != null) {
/* 3183:3380 */               ps.setBytes(i + 1, values1.get(i).toString().getBytes());
/* 3184:     */             } else {
/* 3185:3382 */               ps.setString(i + 1, null);
/* 3186:     */             }
/* 3187:     */           }
/* 3188:3384 */           int updates = ps.executeUpdate();
/* 3189:     */           
/* 3190:3386 */           this.logger.info("updates " + updates);
/* 3191:     */           
/* 3192:3388 */           closePs(dt, ps);
/* 3193:     */         }
/* 3194:     */       }
/* 3195:     */     }
/* 3196:     */     else
/* 3197:     */     {
/* 3198:3394 */       String s = "insert into " + table;
/* 3199:3395 */       String flds = "";
/* 3200:3396 */       String values = "";
/* 3201:3397 */       int count = 0;
/* 3202:3398 */       values1 = new Vector();
/* 3203:3399 */       Vector colTypes = new Vector();
/* 3204:3401 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 3205:     */       {
/* 3206:3403 */         String fld = dt.getAttribName(j);
/* 3207:3404 */         String fld1 = fld;
/* 3208:3405 */         if (fld.equals("id")) {
/* 3209:3406 */           fld1 = "_id";
/* 3210:     */         }
/* 3211:3407 */         String value = dataM.getAttribute(fld1.toLowerCase());
/* 3212:3408 */         int colType = dt.getMetaData().getColumnType(j + 1);
/* 3213:     */         
/* 3214:3410 */         String def = colType == 4 ? "0" : null;
/* 3215:3411 */         colTypes.add(dt.getMetaData().getColumnTypeName(j + 1));
/* 3216:3412 */         if ((!table.equals("tasks")) || (!fld.toLowerCase().equals("id")))
/* 3217:     */         {
/* 3218:3414 */           this.logger.info("Value is " + value);
/* 3219:     */           
/* 3220:3416 */           value = (value == null) || (value.equals("")) ? def : value;
/* 3221:3417 */           values1.add(value);
/* 3222:3418 */           flds = flds + (count != 0 ? "," : "") + fld;
/* 3223:3419 */           values = values + (count != 0 ? "," : "") + "?";
/* 3224:3420 */           count++;
/* 3225:     */         }
/* 3226:     */       }
/* 3227:3423 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 3228:     */       
/* 3229:3425 */       this.logger.info(" Debug sql " + s);
/* 3230:3426 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 3231:3428 */       for (int i = 0; i < values1.size(); i++) {
/* 3232:3430 */         if (values1.get(i) != null)
/* 3233:     */         {
/* 3234:3432 */           this.logger.info(" Coltypes " + i + " " + colTypes.get(i));
/* 3235:3433 */           ps.setBytes(i + 1, values1.get(i).toString().getBytes());
/* 3236:     */         }
/* 3237:     */         else
/* 3238:     */         {
/* 3239:3436 */           ps.setString(i + 1, null);
/* 3240:     */         }
/* 3241:     */       }
/* 3242:3439 */       ps.execute();
/* 3243:3440 */       closePs(dt, ps);
/* 3244:     */     }
/* 3245:3443 */     if (table.equals("tasks"))
/* 3246:     */     {
/* 3247:3445 */       String status = dataM.getAttribute("status");
/* 3248:     */       
/* 3249:3447 */       String task = dataM.getAttribute("task");
/* 3250:3448 */       if (task == null)
/* 3251:     */       {
/* 3252:3450 */         task = (String)((XModel)oldData.get("task")).get();
/* 3253:3451 */         this.logger.info("Old task " + task);
/* 3254:     */       }
/* 3255:3453 */       String pid = dataM.getAttribute("member");
/* 3256:3454 */       if (pid == null)
/* 3257:     */       {
/* 3258:3456 */         pid = (String)((XModel)oldData.get("member")).get();
/* 3259:3457 */         this.logger.info("Old pid " + pid);
/* 3260:     */       }
/* 3261:3459 */       String type = dataM.getAttribute("survey_type");
/* 3262:3460 */       if (type == null)
/* 3263:     */       {
/* 3264:3462 */         type = (String)((XModel)oldData.get("survey_type")).get();
/* 3265:3463 */         this.logger.info("Old type " + type);
/* 3266:     */       }
/* 3267:3465 */       String endtime = dataM.getAttribute("endtime");
/* 3268:3466 */       if (((status != null) && ((status.equals("2")) || (status.equals("5")))) || ((endtime != null) && (endtime.length() > 0))) {
/* 3269:3468 */         if ((type != null) && (type.equals("6"))) {
/* 3270:3470 */           Process.taskStatusUpdate(pid, task, status);
/* 3271:     */         }
/* 3272:     */       }
/* 3273:     */     }
/* 3274:     */   }
/* 3275:     */   
/* 3276:     */   public void deleteResources(String path)
/* 3277:     */     throws Exception
/* 3278:     */   {
/* 3279:3479 */     File dir = new File(path);
/* 3280:3480 */     if (dir.isDirectory())
/* 3281:     */     {
/* 3282:3482 */       String[] files = dir.list();
/* 3283:3483 */       for (int i = 0; i < files.length; i++) {
/* 3284:3485 */         deleteResource(files[i], "");
/* 3285:     */       }
/* 3286:     */     }
/* 3287:     */   }
/* 3288:     */   
/* 3289:     */   public void deleteAllData()
/* 3290:     */     throws Exception
/* 3291:     */   {
/* 3292:3493 */     deleteData1("tasks", "", null, null);
/* 3293:3494 */     deleteData1("keyvalue", "", null, null);
/* 3294:3495 */     deleteData1("changelogs", "", null, null);
/* 3295:     */   }
/* 3296:     */   
/* 3297:     */   public void deleteResource(String resource, String user1)
/* 3298:     */     throws Exception
/* 3299:     */   {
/* 3300:     */     try
/* 3301:     */     {
/* 3302:3503 */       File file1 = new File(resource);
/* 3303:3504 */       Logger logger = Logger.getLogger(getClass());
/* 3304:3505 */       logger.info("Deleting resource" + resource);
/* 3305:3506 */       if (file1.exists())
/* 3306:     */       {
/* 3307:3508 */         logger.info("Resource exists " + resource);
/* 3308:3509 */         if (file1.delete()) {
/* 3309:3510 */           logger.info("Deleted");
/* 3310:     */         } else {
/* 3311:3512 */           logger.info("Not Deleted");
/* 3312:     */         }
/* 3313:     */       }
/* 3314:     */     }
/* 3315:     */     catch (Exception e)
/* 3316:     */     {
/* 3317:3518 */       e.printStackTrace();
/* 3318:     */     }
/* 3319:     */   }
/* 3320:     */   
/* 3321:     */   public void deleteData(String table, String where, XmlElement dataM, String user1)
/* 3322:     */     throws Exception
/* 3323:     */   {
/* 3324:3525 */     XModel conflictData = new XBaseModel();
/* 3325:3526 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 3326:3527 */     conflictData.set("date", "'" + sdf.format(new Date()) + "'");
/* 3327:3528 */     conflictData.set("surveyor", "'" + user1 + "'");
/* 3328:3529 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3329:3530 */     this.logger.info(" Where is " + where);
/* 3330:3531 */     dt.setupTable(table, "*", where, "test", true);
/* 3331:3532 */     Vector values1 = new Vector();
/* 3332:     */     
/* 3333:3534 */     String qry = "delete " + table + " where  " + where;
/* 3334:3535 */     PreparedStatement ps = dt.getTable().getPreparedStatement(qry);
/* 3335:     */     try
/* 3336:     */     {
/* 3337:3537 */       ChangeLog.startLog(table, "delete", where, getCurrentUser());
/* 3338:     */       
/* 3339:3539 */       int updates = ps.executeUpdate();
/* 3340:     */       
/* 3341:3541 */       closePs(dt, ps);
/* 3342:3542 */       this.logger.info("updates " + updates);
/* 3343:     */     }
/* 3344:     */     catch (Exception e)
/* 3345:     */     {
/* 3346:3546 */       e.printStackTrace();
/* 3347:     */     }
/* 3348:3548 */     ChangeLog.endLog();
/* 3349:     */   }
/* 3350:     */   
/* 3351:     */   public void deleteData1(String table, String where, XmlElement dataM, String user1)
/* 3352:     */     throws Exception
/* 3353:     */   {
/* 3354:3554 */     XModel conflictData = new XBaseModel();
/* 3355:3555 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 3356:3556 */     conflictData.set("date", "'" + sdf.format(new Date()) + "'");
/* 3357:3557 */     conflictData.set("surveyor", "'" + user1 + "'");
/* 3358:3558 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3359:3559 */     this.logger.info(" Where is " + where);
/* 3360:3560 */     dt.setupTable(table, "*", where, "test", true);
/* 3361:3561 */     Vector values1 = new Vector();
/* 3362:     */     
/* 3363:3563 */     String qry = "delete from " + table + " where  " + where;
/* 3364:3564 */     PreparedStatement ps = dt.getTable().getPreparedStatement(qry);
/* 3365:     */     try
/* 3366:     */     {
/* 3367:3567 */       int updates = ps.executeUpdate();
/* 3368:3568 */       ps.close();
/* 3369:3569 */       this.logger.info("updates " + updates);
/* 3370:     */     }
/* 3371:     */     catch (Exception e)
/* 3372:     */     {
/* 3373:3573 */       e.printStackTrace();
/* 3374:     */     }
/* 3375:     */   }
/* 3376:     */   
/* 3377:     */   public String getTaskContext(String task, String area, String house, String hh, String idc)
/* 3378:     */   {
/* 3379:3579 */     String context = "";
/* 3380:3580 */     if (task != null)
/* 3381:     */     {
/* 3382:3582 */       context = context + " task = '" + task + "'";
/* 3383:3583 */       if (area != null)
/* 3384:     */       {
/* 3385:3585 */         context = context + "and area = '" + area + "'";
/* 3386:3587 */         if (house != null)
/* 3387:     */         {
/* 3388:3589 */           context = context + "and house = '" + house + "'";
/* 3389:3591 */           if (hh != null)
/* 3390:     */           {
/* 3391:3593 */             context = context + "and household = '" + hh + "'";
/* 3392:3595 */             if (idc != null) {
/* 3393:3597 */               context = context + "and member = '" + idc + "'";
/* 3394:     */             }
/* 3395:     */           }
/* 3396:     */         }
/* 3397:     */       }
/* 3398:     */     }
/* 3399:3606 */     return context;
/* 3400:     */   }
/* 3401:     */   
/* 3402:     */   public String getTaskContext(String task, String area, String house, String hh, String idc, String assignedTo)
/* 3403:     */   {
/* 3404:3611 */     String context = "";
/* 3405:3613 */     if (task != null)
/* 3406:     */     {
/* 3407:3615 */       context = context + " task = '" + task + "' ";
/* 3408:3616 */       if (area != null)
/* 3409:     */       {
/* 3410:3618 */         context = context + "and area = '" + area + "'";
/* 3411:3620 */         if (house != null)
/* 3412:     */         {
/* 3413:3622 */           context = context + "and house = '" + house + "'";
/* 3414:3624 */           if (hh != null)
/* 3415:     */           {
/* 3416:3626 */             context = context + "and household = '" + hh + "'";
/* 3417:3628 */             if (idc != null) {
/* 3418:3630 */               context = context + "and member = '" + idc + "'";
/* 3419:     */             }
/* 3420:     */           }
/* 3421:     */         }
/* 3422:     */       }
/* 3423:3639 */       if (assignedTo != null) {
/* 3424:3641 */         context = context + "and assignedTo = '" + assignedTo + "'";
/* 3425:     */       }
/* 3426:     */     }
/* 3427:3646 */     return context;
/* 3428:     */   }
/* 3429:     */   
/* 3430:     */   public void saveTask(XTaskModel taskM)
/* 3431:     */     throws Exception
/* 3432:     */   {
/* 3433:3652 */     saveTask(taskM.task, taskM.surveyType, taskM.area, taskM.house, taskM.household, taskM.member, taskM);
/* 3434:     */   }
/* 3435:     */   
/* 3436:     */   public void saveTask(String task, String surveyType, String area, String house, String hh, String idc, XModel dataM)
/* 3437:     */     throws Exception
/* 3438:     */   {
/* 3439:3658 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3440:3659 */     String table = "tasks";
/* 3441:3660 */     String assignedTo = (String)dataM.get("@assignedto");
/* 3442:3662 */     if (task.equals("taskdefinitions/healthcheckup_taskdefinition/task0")) {
/* 3443:3663 */       return;
/* 3444:     */     }
/* 3445:3664 */     String where = getTaskContext(task, area, house, hh, idc, assignedTo);
/* 3446:3665 */     ChangeLog.startLog(table, where, getCurrentUser());
/* 3447:3666 */     dt.setupTable(table, "*", where, "test", true);
/* 3448:3667 */     dt.retrieve();
/* 3449:3668 */     dataM.set("@task", task);
/* 3450:3669 */     dataM.set("@area", area);
/* 3451:3670 */     dataM.set("@house", house);
/* 3452:3671 */     dataM.set("@household", hh);
/* 3453:3672 */     dataM.set("@member", idc);
/* 3454:3673 */     dataM.set("@survey_type", surveyType);
/* 3455:3674 */     dataM.set("@status", dataM.get());
/* 3456:     */     
/* 3457:3676 */     System.out.println(" TASK " + dataM.get("@task"));
/* 3458:3678 */     if (dt.getNumChildren() > 0)
/* 3459:     */     {
/* 3460:3680 */       System.out.println(" total " + dt.getNumChildren());
/* 3461:     */       
/* 3462:3682 */       String qry = "update " + table + " set ";
/* 3463:3683 */       int count = 0;
/* 3464:3684 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 3465:     */       {
/* 3466:3687 */         String fld = dt.getAttribName(j);
/* 3467:3688 */         System.out.println(" DEBUG column " + dt.getMetaData().getColumnType(j + 1) + " " + fld);
/* 3468:3689 */         if (!fld.equals("id"))
/* 3469:     */         {
/* 3470:3692 */           int colType = dt.getMetaData().getColumnType(j + 1);
/* 3471:3693 */           String def = colType == 4 ? "0" : null;
/* 3472:3694 */           String value = (String)dataM.get("@" + fld);
/* 3473:     */           
/* 3474:3696 */           value = "'" + value + "'";
/* 3475:3697 */           qry = qry + (count == 0 ? "" : ",") + fld + "=" + value;
/* 3476:3698 */           ChangeLog.logField(fld, value);
/* 3477:3699 */           count++;
/* 3478:     */         }
/* 3479:     */       }
/* 3480:3702 */       int updates = dt.executeUpdate(qry + " where " + where);
/* 3481:3703 */       System.out.println("updates " + updates);
/* 3482:     */       
/* 3483:3705 */       System.out.println(" Debug update sql " + qry);
/* 3484:     */     }
/* 3485:     */     else
/* 3486:     */     {
/* 3487:3709 */       String s = "insert into " + table;
/* 3488:3710 */       String flds = "";
/* 3489:3711 */       String values = "";
/* 3490:3712 */       int count = 0;
/* 3491:3713 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 3492:     */       {
/* 3493:3715 */         String fld = dt.getAttribName(j);
/* 3494:3716 */         if (!fld.equals("id"))
/* 3495:     */         {
/* 3496:3718 */           System.out.println(" DEBUG column " + dt.getMetaData().getColumnType(j + 1) + " " + fld);
/* 3497:3719 */           int colType = dt.getMetaData().getColumnType(j + 1);
/* 3498:3720 */           String def = colType == 4 ? "0" : null;
/* 3499:3721 */           String value = (String)dataM.get("@" + fld);
/* 3500:3722 */           flds = flds + (count != 0 ? "," : "") + fld;
/* 3501:3723 */           value = "'" + value + "'";
/* 3502:3724 */           values = values + (count != 0 ? "," : "") + value;
/* 3503:3725 */           ChangeLog.logField(fld, value);
/* 3504:3726 */           count++;
/* 3505:     */         }
/* 3506:     */       }
/* 3507:3728 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 3508:     */       
/* 3509:3730 */       System.out.println(" Debug sql-- " + s);
/* 3510:     */       
/* 3511:     */ 
/* 3512:3733 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 3513:3734 */       ps.execute();
/* 3514:3735 */       ps.close();
/* 3515:3736 */       dt.getTable().releasePreparedStatement(ps);
/* 3516:     */     }
/* 3517:3740 */     ChangeLog.endLog();
/* 3518:     */   }
/* 3519:     */   
/* 3520:     */   public XModel getHouseholds(String area, String house, XModel houseM)
/* 3521:     */     throws Exception
/* 3522:     */   {
/* 3523:3747 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3524:3748 */     dt.setupTable("households", "household", "enum_area=" + area + " and house=" + house, "test", false);
/* 3525:3749 */     dt.retrieve();
/* 3526:     */     
/* 3527:3751 */     this.logger.info(Integer.valueOf(dt.getNumChildren()));
/* 3528:3752 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 3529:     */     {
/* 3530:3754 */       String id = dt.get(i).get("household").toString();
/* 3531:3755 */       XModel tt = (XModel)houseM.get(id);
/* 3532:     */       
/* 3533:3757 */       tt.setId(dt.get(i).get("household").toString());
/* 3534:3758 */       XModel xm = getHouseholddetails(id, house, area);
/* 3535:3759 */       xm.append(getDataM("data", "visitHistory", area, house, id, null));
/* 3536:     */       
/* 3537:3761 */       tt = getIndividuals(area, house, tt.getId(), tt);
/* 3538:3762 */       tt.append(xm);
/* 3539:     */       
/* 3540:3764 */       tt.append(getDataM("data", "characteristics", area, house, id, null));
/* 3541:     */     }
/* 3542:3767 */     return houseM;
/* 3543:     */   }
/* 3544:     */   
/* 3545:     */   public XModel getHouseholddetails(String household, String house, String area, XModel xm)
/* 3546:     */   {
/* 3547:3772 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3548:3773 */     String sql = "select a.*,a.head headcode,b.name  headname from households a left join members b on a.enum_area=b.enum_area and a.house=b.house and a.household=b.household and a.head=b.idc";
/* 3549:     */     
/* 3550:3775 */     dt.setSqlStatement(sql + " where a.enum_area='" + area + "' and a.house='" + house + "' and a.household='" + household + "'", "test", true);
/* 3551:3776 */     dt.setName("updatestatus");
/* 3552:3777 */     dt.setId("updatestatus");
/* 3553:3778 */     dt.setTagName("data");
/* 3554:3779 */     dt.retrieve();
/* 3555:3781 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 3556:3783 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 3557:     */       {
/* 3558:3785 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 3559:3786 */         tt.set(dt.get(i).get(j).get());
/* 3560:     */       }
/* 3561:     */     }
/* 3562:3791 */     return xm;
/* 3563:     */   }
/* 3564:     */   
/* 3565:     */   public XModel getHouseholddetails(String household, String house, String area)
/* 3566:     */   {
/* 3567:3796 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3568:3797 */     String sql = "select a.*,a.head headcode,b.name  headname from households a left join members b on a.enum_area=b.enum_area and a.house=b.house and a.household=b.household and a.head=b.idc";
/* 3569:     */     
/* 3570:3799 */     dt.setSqlStatement(sql + " where a.enum_area='" + area + "' and a.house='" + house + "' and a.household='" + household + "'", "test", true);
/* 3571:3800 */     dt.setName("updatestatus");
/* 3572:3801 */     dt.setId("updatestatus");
/* 3573:3802 */     dt.setTagName("data");
/* 3574:3803 */     dt.retrieve();
/* 3575:3804 */     XBaseModel xm = new XBaseModel();
/* 3576:3805 */     xm.setId("updatestatus");
/* 3577:3807 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 3578:3809 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 3579:     */       {
/* 3580:3811 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 3581:3812 */         tt.set(dt.get(i).get(j).get());
/* 3582:     */       }
/* 3583:     */     }
/* 3584:3817 */     return xm;
/* 3585:     */   }
/* 3586:     */   
/* 3587:     */   public String getNextContextType(XModel context)
/* 3588:     */     throws Exception
/* 3589:     */   {
/* 3590:3823 */     XModel areaM = (XModel)context.get("area");
/* 3591:3824 */     XModel houseM = (XModel)context.get("house");
/* 3592:3825 */     XModel householdM = (XModel)context.get("household");
/* 3593:3826 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3594:3827 */     String id = "";
/* 3595:3829 */     if ((householdM != null) && (householdM.get() != null) && (!householdM.get().equals(""))) {
/* 3596:3831 */       id = "individual";
/* 3597:3833 */     } else if ((houseM != null) && (houseM.get() != null) && (!houseM.equals(""))) {
/* 3598:3835 */       id = "household";
/* 3599:3837 */     } else if ((areaM != null) && (areaM.get() != null) && (!areaM.get().equals(""))) {
/* 3600:3839 */       id = "house";
/* 3601:     */     } else {
/* 3602:3843 */       id = "area";
/* 3603:     */     }
/* 3604:3844 */     return id;
/* 3605:     */   }
/* 3606:     */   
/* 3607:     */   public void applyAutoUpdate(XModel context, String contextType)
/* 3608:     */     throws Exception
/* 3609:     */   {
/* 3610:3850 */     XModel areaM = (XModel)context.get("area");
/* 3611:3851 */     XModel houseM = (XModel)context.get("house");
/* 3612:3852 */     XModel householdM = (XModel)context.get("household");
/* 3613:3853 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3614:3854 */     XModel details = new XBaseModel();
/* 3615:     */     
/* 3616:3856 */     XModel indivM = (XModel)context.get("individual");
/* 3617:3857 */     XModel visitM = (XModel)context.get("visit");
/* 3618:3858 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
/* 3619:3859 */     if (contextType.equals("visit"))
/* 3620:     */     {
/* 3621:3861 */       String fields = "max(visitid)";
/* 3622:3862 */       XModel dataM = new XBaseModel();
/* 3623:3863 */       getData("visit", fields, "household='" + householdM.get() + "' and teamid=" + surveyorM.get(), dataM);
/* 3624:3864 */       if (dataM.getNumChildren() > 0)
/* 3625:     */       {
/* 3626:3866 */         this.logger.info("Max " + dataM.get(0).get(0).getId() + " ");
/* 3627:3867 */         XModel maxTime = dataM.get(0).get(0);
/* 3628:3868 */         this.logger.info("Max " + maxTime.get() + " ");
/* 3629:3870 */         if (maxTime.get() == null)
/* 3630:     */         {
/* 3631:3872 */           String visitTime = sdf.format(new Date());
/* 3632:3873 */           XModel xm = new XBaseModel();
/* 3633:3874 */           ((XModel)xm.get("visittime")).set(visitTime);
/* 3634:3875 */           ((XModel)xm.get("teamid")).set(surveyorM.get());
/* 3635:3876 */           ((XModel)context.get("visit")).set(visitTime);
/* 3636:3877 */           saveEnumData(context, contextType, xm);
/* 3637:     */         }
/* 3638:     */         else
/* 3639:     */         {
/* 3640:3881 */           String lastVisitTime = maxTime.get().toString();
/* 3641:3882 */           Date l = sdf.parse(lastVisitTime);
/* 3642:3883 */           Calendar cal = Calendar.getInstance();
/* 3643:3884 */           cal.setTime(l);
/* 3644:3885 */           cal.add(10, 4);
/* 3645:3886 */           this.logger.info(" Next visit time" + sdf.format(cal.getTime()));
/* 3646:3887 */           this.logger.info(" Current time " + sdf.format(new Date()));
/* 3647:3888 */           this.logger.info(" Check  " + cal.getTime().after(new Date()) + " " + cal.getTime().equals(new Date()));
/* 3648:3889 */           Calendar cal2 = Calendar.getInstance();
/* 3649:3890 */           this.logger.info(" Check2  " + cal.compareTo(cal2));
/* 3650:3891 */           if (!cal.getTime().after(new Date()))
/* 3651:     */           {
/* 3652:3893 */             String visitTime = sdf.format(new Date());
/* 3653:3894 */             XModel xm = new XBaseModel();
/* 3654:3895 */             ((XModel)xm.get("visittime")).set(visitTime);
/* 3655:3896 */             ((XModel)xm.get("teamid")).set(surveyorM.get());
/* 3656:3897 */             ((XModel)context.get("visit")).set(visitTime);
/* 3657:3898 */             saveEnumData(context, contextType, xm);
/* 3658:     */           }
/* 3659:     */         }
/* 3660:     */       }
/* 3661:     */       else
/* 3662:     */       {
/* 3663:3904 */         String visitTime = sdf.format(new Date());
/* 3664:3905 */         XModel xm = new XBaseModel();
/* 3665:3906 */         ((XModel)xm.get("visittime")).set(visitTime);
/* 3666:3907 */         ((XModel)xm.get("visitid")).set(visitTime);
/* 3667:3908 */         ((XModel)xm.get("teamid")).set(surveyorM.get());
/* 3668:3909 */         ((XModel)context.get("visit")).set(visitTime);
/* 3669:3910 */         this.logger.info(" New visit");
/* 3670:3911 */         saveEnumData(context, contextType, xm);
/* 3671:     */       }
/* 3672:     */     }
/* 3673:     */   }
/* 3674:     */   
/* 3675:     */   public XModel getCMEData(XModel context, String contextType, String fields)
/* 3676:     */     throws Exception
/* 3677:     */   {
/* 3678:3919 */     XModel areaM = (XModel)context.get("area");
/* 3679:3920 */     XModel houseM = (XModel)context.get("house");
/* 3680:3921 */     XModel householdM = (XModel)context.get("household");
/* 3681:3922 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3682:3923 */     XModel details = new XBaseModel();
/* 3683:     */     
/* 3684:3925 */     XModel indivM = (XModel)context.get("cme");
/* 3685:3926 */     XModel visitM = (XModel)context.get("visit");
/* 3686:3927 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3687:     */     
/* 3688:3929 */     String where = "";
/* 3689:3930 */     String table = "";
/* 3690:3931 */     String idField = "";
/* 3691:3932 */     String id = "";
/* 3692:3933 */     String path = "/cme/" + indivM.get() + "/";
/* 3693:3934 */     StringTokenizer st = new StringTokenizer(fields, ",");
/* 3694:3935 */     XModel dataM = new XBaseModel();
/* 3695:3936 */     XModel dataM1 = (XModel)dataM.get("test");
/* 3696:3937 */     while (st.hasMoreTokens())
/* 3697:     */     {
/* 3698:3939 */       String key = st.nextToken();
/* 3699:3940 */       XModel xm = (XModel)dataM1.get(key);
/* 3700:3941 */       String value = getInstance().getValue("keyvalue", path + key);
/* 3701:3942 */       xm.set(value);
/* 3702:     */     }
/* 3703:3944 */     return dataM;
/* 3704:     */   }
/* 3705:     */   
/* 3706:     */   public XModel getVAData(XModel context, String contextType, String fields)
/* 3707:     */     throws Exception
/* 3708:     */   {
/* 3709:3950 */     XModel areaM = (XModel)context.get("area");
/* 3710:3951 */     XModel houseM = (XModel)context.get("house");
/* 3711:3952 */     XModel householdM = (XModel)context.get("household");
/* 3712:3953 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3713:3954 */     XModel details = new XBaseModel();
/* 3714:     */     
/* 3715:3956 */     XModel indivM = (XModel)context.get("va");
/* 3716:3957 */     XModel visitM = (XModel)context.get("visit");
/* 3717:3958 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3718:     */     
/* 3719:3960 */     String where = "";
/* 3720:3961 */     String table = "";
/* 3721:3962 */     String idField = "";
/* 3722:3963 */     String id = "";
/* 3723:3964 */     String path = "/va/" + areaM.get() + indivM.get() + "/" + surveyorM.get() + "/";
/* 3724:3965 */     StringTokenizer st = new StringTokenizer(fields, ",");
/* 3725:3966 */     XModel dataM = new XBaseModel();
/* 3726:3967 */     XModel dataM1 = (XModel)dataM.get("test");
/* 3727:3968 */     while (st.hasMoreTokens())
/* 3728:     */     {
/* 3729:3970 */       String key = st.nextToken();
/* 3730:3971 */       XModel xm = (XModel)dataM1.get(key);
/* 3731:3972 */       String value = getInstance().getValue("keyvalue", path + key);
/* 3732:3973 */       xm.set(value);
/* 3733:     */     }
/* 3734:3975 */     return dataM;
/* 3735:     */   }
/* 3736:     */   
/* 3737:     */   public void saveFlow(String id, String flow, String context, String surveyor, String status, String startTime, String endTime)
/* 3738:     */     throws Exception
/* 3739:     */   {
/* 3740:3981 */     this.logger.info(" Surveyor " + surveyor);
/* 3741:3982 */     DatabaseTableModel dtm = new DatabaseTableModel();
/* 3742:3983 */     dtm.setupTable("flow", "*", "", "test", false);
/* 3743:     */     
/* 3744:3985 */     ChangeLog.startLog("flow", "id1='" + id + "'", getCurrentUser());
/* 3745:3986 */     PreparedStatement ps = dtm.getTable().getPreparedStatement(" insert into flow  values(?,?,?,?,?,?,?)");
/* 3746:3987 */     ChangeLog.logField("id1", id);
/* 3747:3988 */     ChangeLog.logField("flow", flow);
/* 3748:     */     
/* 3749:3990 */     ps.setString(1, id);
/* 3750:3991 */     ps.setString(2, flow);
/* 3751:3992 */     ps.setString(3, context);
/* 3752:3993 */     ChangeLog.logField("context", context);
/* 3753:3994 */     ps.setString(4, status);
/* 3754:3995 */     ChangeLog.logField("status", status);
/* 3755:3996 */     ps.setString(5, startTime);
/* 3756:3997 */     ChangeLog.logField("starttime", startTime);
/* 3757:3998 */     ps.setString(6, endTime);
/* 3758:3999 */     ChangeLog.logField("endtime", endTime);
/* 3759:4000 */     ps.setString(7, surveyor);
/* 3760:4001 */     ChangeLog.logField("user", surveyor);
/* 3761:     */     
/* 3762:4003 */     this.logger.info(" Executing ..");
/* 3763:4004 */     ps.execute();
/* 3764:4005 */     ChangeLog.endLog();
/* 3765:     */   }
/* 3766:     */   
/* 3767:     */   public void saveFlowQuestion(String flowid, String flow, String surveyor, String qno, String startTime, String endTime)
/* 3768:     */     throws Exception
/* 3769:     */   {
/* 3770:4011 */     this.logger.info(" Surveyor " + surveyor);
/* 3771:4012 */     DatabaseTableModel dtm = new DatabaseTableModel();
/* 3772:4013 */     dtm.setupTable("flow_question", "*", "", "test", false);
/* 3773:4014 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 3774:     */     
/* 3775:4016 */     ChangeLog.startLog("flow_question", "flow_id='" + flowid + "'  and qno='" + qno + "'", getCurrentUser());
/* 3776:4017 */     PreparedStatement ps = dtm.getTable().getPreparedStatement(" insert into flow_question  values(?,?,?,?,?,?)");
/* 3777:4018 */     ChangeLog.logField("flow_id", flowid);
/* 3778:4019 */     ChangeLog.logField("flow", flow);
/* 3779:4020 */     ChangeLog.logField("qno", qno);
/* 3780:     */     
/* 3781:4022 */     ps.setString(1, flowid);
/* 3782:4023 */     ps.setString(2, flow);
/* 3783:4024 */     ps.setString(3, qno);
/* 3784:     */     
/* 3785:4026 */     ps.setString(4, startTime);
/* 3786:4027 */     ChangeLog.logField("starttime", startTime);
/* 3787:4028 */     ps.setString(5, endTime);
/* 3788:4029 */     ChangeLog.logField("endtime", endTime);
/* 3789:4030 */     ps.setString(6, surveyor);
/* 3790:4031 */     ChangeLog.logField("user", surveyor);
/* 3791:     */     
/* 3792:4033 */     this.logger.info(" Executing fq ..");
/* 3793:4034 */     ps.execute();
/* 3794:4035 */     ChangeLog.endLog();
/* 3795:     */   }
/* 3796:     */   
/* 3797:     */   public XModel getFlowParameters(XModel context, String contextType)
/* 3798:     */     throws Exception
/* 3799:     */   {
/* 3800:4041 */     this.logger.info("Flow context type " + contextType);
/* 3801:4042 */     XModel surveyM = (XModel)context.get("survey");
/* 3802:4043 */     XModel areaM = (XModel)context.get("area");
/* 3803:4044 */     XModel houseM = (XModel)context.get("house");
/* 3804:4045 */     XModel householdM = (XModel)context.get("household");
/* 3805:4046 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3806:4047 */     XModel details = new XBaseModel();
/* 3807:     */     
/* 3808:4049 */     XModel indivM = (XModel)context.get("member");
/* 3809:4050 */     XModel visitM = (XModel)context.get("visit");
/* 3810:4051 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3811:4052 */     XModel params = new XBaseModel();
/* 3812:4053 */     XModel newContext = new XBaseModel();
/* 3813:4054 */     for (int i = 0; i < context.getNumChildren(); i++) {
/* 3814:4056 */       if (!context.get(i).getId().equals(contextType)) {
/* 3815:4058 */         newContext.append(context.get(i));
/* 3816:     */       }
/* 3817:     */     }
/* 3818:4061 */     if (contextType.equals("va"))
/* 3819:     */     {
/* 3820:4063 */       ((XModel)newContext.get("member")).set(((XModel)context.get("va")).get());
/* 3821:4064 */       XModel values = getEnumData(newContext, "member", "sex,deathage,deathageunit");
/* 3822:4065 */       ((XModel)params.get("deceased_age")).set("@type", "age");
/* 3823:4066 */       this.logger.info(" Flow has " + values.get(0).getNumChildren());
/* 3824:4067 */       String sex = ((XModel)values.get(0).get("sex")).get().toString();
/* 3825:4068 */       String age = ((XModel)values.get(0).get("deathage")).get().toString();
/* 3826:4069 */       String ageUnit = ((XModel)values.get(0).get("deathageunit")).get().toString();
/* 3827:4070 */       ((XModel)params.get("deceased_age")).set(age + "," + ageUnit);
/* 3828:4071 */       ((XModel)params.get("deceased_sex")).set(sex);
/* 3829:4072 */       ((XModel)params.get("deceased_sex")).set("@type", "text");
/* 3830:     */     }
/* 3831:4075 */     return params;
/* 3832:     */   }
/* 3833:     */   
/* 3834:     */   public XModel getEnumData(XModel context, String contextType, String fields)
/* 3835:     */     throws Exception
/* 3836:     */   {
/* 3837:4081 */     XModel surveyM = (XModel)context.get("survey");
/* 3838:4082 */     XModel areaM = (XModel)context.get("area");
/* 3839:4083 */     XModel houseM = (XModel)context.get("house");
/* 3840:4084 */     XModel householdM = (XModel)context.get("household");
/* 3841:4085 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3842:4086 */     XModel details = new XBaseModel();
/* 3843:     */     
/* 3844:4088 */     XModel indivM = (XModel)context.get("member");
/* 3845:4089 */     XModel visitM = (XModel)context.get("visit");
/* 3846:4090 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3847:     */     
/* 3848:4092 */     String where = "";
/* 3849:4093 */     String table = "";
/* 3850:4094 */     String idField = "";
/* 3851:4095 */     String id = "";
/* 3852:4096 */     if (surveyM.get().equals("istp"))
/* 3853:     */     {
/* 3854:4098 */       XModel clinicM = (XModel)context.get("clinic");
/* 3855:     */       
/* 3856:4100 */       where = "ictc_code='" + clinicM.get() + "'";
/* 3857:4101 */       table = "clinic";
/* 3858:4102 */       idField = "ictc_code";
/* 3859:4103 */       XModel stdM = (XModel)context.get("std");
/* 3860:4104 */       if ((contextType.equals("std")) && (stdM != null) && (stdM.get() != null) && (!stdM.equals("")))
/* 3861:     */       {
/* 3862:4106 */         where = "uniqno='" + stdM.get() + "'";
/* 3863:4107 */         table = "patients";
/* 3864:4108 */         idField = "uniqno";
/* 3865:     */       }
/* 3866:4110 */       XModel resultM = (XModel)context.get("ictc_result");
/* 3867:4111 */       if ((contextType.equals("ictc_result")) && (resultM != null) && (resultM.get() != null) && (!resultM.equals("")))
/* 3868:     */       {
/* 3869:4113 */         where = "uniqno='" + resultM.get() + "'";
/* 3870:4114 */         table = "ictc_result";
/* 3871:4115 */         idField = "uniqno";
/* 3872:     */       }
/* 3873:     */     }
/* 3874:     */     else
/* 3875:     */     {
/* 3876:4120 */       if (contextType.equals("va")) {
/* 3877:4122 */         return getVAData(context, contextType, fields);
/* 3878:     */       }
/* 3879:4125 */       if ((contextType.equals("member")) && (indivM != null) && (indivM.get() != null) && (!indivM.equals("")))
/* 3880:     */       {
/* 3881:4127 */         where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "' and idc='" + indivM.get() + "'";
/* 3882:4128 */         table = "members";
/* 3883:4129 */         idField = "idc";
/* 3884:     */       }
/* 3885:4131 */       else if (((contextType.equals("births")) || (contextType.equals("marriages"))) && (contextTypeM != null))
/* 3886:     */       {
/* 3887:4133 */         where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "' and idc='" + contextTypeM.get() + "'";
/* 3888:4134 */         table = "births";
/* 3889:4135 */         if (contextType.equals("marriages")) {
/* 3890:4136 */           table = "marriages";
/* 3891:     */         }
/* 3892:4138 */         idField = "idc";
/* 3893:     */       }
/* 3894:4140 */       else if ((contextType.equals("marriages")) && (indivM != null) && (indivM.get() != null) && (!indivM.equals("")))
/* 3895:     */       {
/* 3896:4142 */         where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "' and idc='" + indivM.get() + "'";
/* 3897:4143 */         table = "marriages";
/* 3898:4144 */         idField = "idc";
/* 3899:     */       }
/* 3900:4146 */       else if ((contextType.equals("visit")) && (visitM != null) && (visitM.get() != null) && (!visitM.equals("")))
/* 3901:     */       {
/* 3902:4148 */         where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "'";
/* 3903:4149 */         table = "visit";
/* 3904:4150 */         idField = "visitid";
/* 3905:     */       }
/* 3906:4152 */       else if ((householdM != null) && (householdM.get() != null) && (!householdM.get().equals("")))
/* 3907:     */       {
/* 3908:4154 */         where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "'";
/* 3909:4155 */         table = "households";
/* 3910:4156 */         idField = "household";
/* 3911:4157 */         id = householdM.get().toString();
/* 3912:     */       }
/* 3913:4159 */       else if ((houseM != null) && (houseM.get() != null) && (!houseM.equals("")))
/* 3914:     */       {
/* 3915:4161 */         where = "enum_area='" + areaM.get() + "' and houseno='" + houseM.get() + "'";
/* 3916:     */         
/* 3917:4163 */         table = "houses";
/* 3918:     */         
/* 3919:4165 */         idField = "houseno";
/* 3920:4166 */         id = houseM.get().toString();
/* 3921:     */       }
/* 3922:4168 */       else if ((areaM != null) && (areaM.get() != null) && (!areaM.equals("")))
/* 3923:     */       {
/* 3924:4170 */         where = "id='" + areaM.get() + "' ";
/* 3925:4171 */         table = "hc_area";
/* 3926:4172 */         idField = "id";
/* 3927:     */       }
/* 3928:     */     }
/* 3929:4177 */     return getEnumData1(table, where, details, fields, idField);
/* 3930:     */   }
/* 3931:     */   
/* 3932:     */   private XModel getISTPData(XModel context, String contextType, String fields)
/* 3933:     */   {
/* 3934:4182 */     XModel areaM = (XModel)context.get("area");
/* 3935:4183 */     XModel houseM = (XModel)context.get("clinic");
/* 3936:     */     
/* 3937:4185 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 3938:4186 */     XModel details = new XBaseModel();
/* 3939:     */     
/* 3940:4188 */     XModel indivM = (XModel)context.get("std");
/* 3941:4189 */     XModel visitM = (XModel)context.get("visit");
/* 3942:4190 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 3943:     */     
/* 3944:4192 */     String where = "";
/* 3945:4193 */     String table = "";
/* 3946:4194 */     String idField = "";
/* 3947:4195 */     String id = "";
/* 3948:4196 */     String path = "/std/" + areaM.get() + indivM.get() + "/" + surveyorM.get() + "/";
/* 3949:4197 */     StringTokenizer st = new StringTokenizer(fields, ",");
/* 3950:4198 */     XModel dataM = new XBaseModel();
/* 3951:4199 */     XModel dataM1 = (XModel)dataM.get("test");
/* 3952:4200 */     while (st.hasMoreTokens())
/* 3953:     */     {
/* 3954:4202 */       String key = st.nextToken();
/* 3955:4203 */       XModel xm = (XModel)dataM1.get(key);
/* 3956:4204 */       String value = getInstance().getValue("keyvalue", path + key);
/* 3957:4205 */       xm.set(value);
/* 3958:     */     }
/* 3959:4207 */     return dataM;
/* 3960:     */   }
/* 3961:     */   
/* 3962:     */   public XModel getKeyValChildren(XModel context, String fields, String constraints, String contextType)
/* 3963:     */     throws Exception
/* 3964:     */   {
/* 3965:4213 */     String dom = "/" + contextType + "/";
/* 3966:4214 */     String key1 = "REPLACE(LEFT(key1,LOCATE ('/',key1," + (dom.length() + 1) + ")-1),'" + dom + "','')";
/* 3967:     */     
/* 3968:4216 */     String fld1 = "RIGHT(key1,INSTR(REVERSE(key1),'/')-1)";
/* 3969:     */     
/* 3970:4218 */     String where = constraints;
/* 3971:4219 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 3972:     */     
/* 3973:4221 */     dt.setupTable("keyvalue", key1 + "," + fld1 + ",value1", where, "test", false);
/* 3974:     */     
/* 3975:4223 */     dt.retrieve();
/* 3976:4224 */     String keyFld = "key1";
/* 3977:4225 */     String valFld = "value1";
/* 3978:4226 */     this.logger.info("---" + dt.getNumChildren());
/* 3979:4227 */     XModel xm = new XBaseModel();
/* 3980:4228 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 3981:     */     {
/* 3982:4230 */       XModel rowM = dt.get(i);
/* 3983:4231 */       String id = rowM.get(0).toString();
/* 3984:4232 */       String field = rowM.get(1).toString();
/* 3985:4233 */       String value = rowM.get(2).toString();
/* 3986:4234 */       XModel tt = (XModel)xm.get(id);
/* 3987:4235 */       XModel col = (XModel)tt.get(field);
/* 3988:4236 */       col.set(value);
/* 3989:     */     }
/* 3990:4239 */     return xm;
/* 3991:     */   }
/* 3992:     */   
/* 3993:     */   public XModel getISTPChildren(XModel context, String fields, String constraints, String contextType)
/* 3994:     */     throws Exception
/* 3995:     */   {
/* 3996:4245 */     return getKeyValChildren(context, fields, constraints, contextType);
/* 3997:     */   }
/* 3998:     */   
/* 3999:     */   public XModel getEnumDataChildren(XModel context, String fields, String constraints, String contextType)
/* 4000:     */     throws Exception
/* 4001:     */   {
/* 4002:4251 */     XModel surveyM = (XModel)context.get("survey");
/* 4003:4252 */     XModel areaM = (XModel)context.get("area");
/* 4004:4253 */     XModel houseM = (XModel)context.get("house");
/* 4005:     */     
/* 4006:4255 */     XModel householdM = (XModel)context.get("household");
/* 4007:4256 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 4008:     */     
/* 4009:4258 */     XModel details = new XBaseModel();
/* 4010:     */     
/* 4011:4260 */     XModel indivM = (XModel)context.get("individual");
/* 4012:4261 */     XModel contextTypeM = (XModel)context.get("individual");
/* 4013:     */     
/* 4014:4263 */     String where = "";
/* 4015:4264 */     String table = "";
/* 4016:4265 */     String idField = "";
/* 4017:4266 */     String id = "";
/* 4018:4268 */     if (surveyM.get().equals("istp"))
/* 4019:     */     {
/* 4020:4270 */       XModel clinicM = (XModel)context.get("clinic");
/* 4021:4271 */       where = "";
/* 4022:4272 */       table = "clinic";
/* 4023:4273 */       idField = "ictc_code";
/* 4024:4274 */       if (contextType.equals("std"))
/* 4025:     */       {
/* 4026:4276 */         where = "ictc_code='" + clinicM.get() + "'";
/* 4027:4277 */         table = "patients";
/* 4028:4278 */         idField = "uniqno";
/* 4029:     */       }
/* 4030:4280 */       if (contextType.equals("ictc_result"))
/* 4031:     */       {
/* 4032:4282 */         where = "ictc_code='" + clinicM.get() + "'";
/* 4033:4283 */         table = "ictc_result";
/* 4034:4284 */         idField = "uniqno";
/* 4035:     */       }
/* 4036:     */     }
/* 4037:4288 */     else if ((householdM != null) && (householdM.get() != null) && (!householdM.equals("")))
/* 4038:     */     {
/* 4039:4290 */       where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "'";
/* 4040:4291 */       if (contextType.equals("va"))
/* 4041:     */       {
/* 4042:4293 */         table = "members";
/* 4043:4294 */         idField = "idc";
/* 4044:     */       }
/* 4045:4296 */       if (contextType.equals("member"))
/* 4046:     */       {
/* 4047:4298 */         table = "members";
/* 4048:4299 */         idField = "idc";
/* 4049:     */       }
/* 4050:4302 */       if (contextType.equals("livemember"))
/* 4051:     */       {
/* 4052:4304 */         table = "members";
/* 4053:4305 */         idField = "idc";
/* 4054:4306 */         constraints = "deathStatus is null";
/* 4055:     */       }
/* 4056:4309 */       if (contextType.equals("death"))
/* 4057:     */       {
/* 4058:4311 */         table = "members";
/* 4059:4312 */         idField = "idc";
/* 4060:4313 */         constraints = "deathStatus is not null";
/* 4061:     */       }
/* 4062:4316 */       if (contextType.equals("visit"))
/* 4063:     */       {
/* 4064:4318 */         table = "visit";
/* 4065:4319 */         idField = "visitid";
/* 4066:     */       }
/* 4067:4321 */       if (contextType.equals("births"))
/* 4068:     */       {
/* 4069:4323 */         table = "births";
/* 4070:4324 */         idField = "idc";
/* 4071:     */       }
/* 4072:4326 */       if (contextType.equals("marriages"))
/* 4073:     */       {
/* 4074:4328 */         table = "marriages";
/* 4075:4329 */         idField = "idc";
/* 4076:     */       }
/* 4077:     */     }
/* 4078:4333 */     else if ((houseM != null) && (houseM.get() != null) && (!houseM.equals("")))
/* 4079:     */     {
/* 4080:4335 */       where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "'";
/* 4081:4336 */       table = "households";
/* 4082:4337 */       idField = "households.household";
/* 4083:4338 */       if (surveyM.get().equals("VA")) {
/* 4084:4340 */         where = "households.enum_area='" + areaM.get() + "' and households.house='" + houseM.get() + "' and  num_of_deaths is not null and num_of_deaths > 0 ";
/* 4085:     */       }
/* 4086:     */     }
/* 4087:4344 */     else if ((areaM != null) && (areaM.get() != null) && (!areaM.equals("")))
/* 4088:     */     {
/* 4089:4346 */       where = "enum_area='" + areaM.get() + "' ";
/* 4090:4347 */       table = "houses";
/* 4091:4348 */       idField = "houseno";
/* 4092:4349 */       if (surveyM.get().equals("VA"))
/* 4093:     */       {
/* 4094:4351 */         fields = "houses.houseno id," + fields;
/* 4095:4352 */         where = "houses.enum_area='" + areaM.get() + "' and households.num_of_deaths is not null and households.num_of_deaths > 0  ";
/* 4096:     */         
/* 4097:4354 */         table = "houses left join households on houses.enum_area=households.enum_area and houses.houseno=households.house where " + where + " group by houses.enum_area,houses.houseno";
/* 4098:4355 */         where = "";
/* 4099:     */       }
/* 4100:     */     }
/* 4101:     */     else
/* 4102:     */     {
/* 4103:4361 */       where = "";
/* 4104:4362 */       table = "hc_area";
/* 4105:4363 */       idField = "id";
/* 4106:4364 */       surveyM.get().equals("VA");
/* 4107:     */     }
/* 4108:4367 */     this.logger.info(">>" + fields + " " + where + " " + table + " " + contextType);
/* 4109:4368 */     return getEnumData1(table, where + (constraints != null ? " and " + constraints : ""), details, fields, idField);
/* 4110:     */   }
/* 4111:     */   
/* 4112:     */   public void saveVAData(XModel context, String contextType, XModel dataM)
/* 4113:     */     throws Exception
/* 4114:     */   {
/* 4115:4374 */     XModel areaM = (XModel)context.get("area");
/* 4116:4375 */     XModel houseM = (XModel)context.get("house");
/* 4117:4376 */     XModel householdM = (XModel)context.get("household");
/* 4118:4377 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 4119:4378 */     XModel indivM = (XModel)context.get("va");
/* 4120:4379 */     XModel visitM = (XModel)context.get("visit");
/* 4121:4380 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 4122:4381 */     XModel details = new XBaseModel();
/* 4123:4382 */     String where = "";
/* 4124:4383 */     String table = "";
/* 4125:4384 */     String idField = "";
/* 4126:4385 */     String id = "";
/* 4127:4386 */     String path = "/va/" + areaM.get() + indivM.get() + "/" + surveyorM.get() + "/";
/* 4128:4387 */     this.logger.info("Save VA called " + dataM.getNumChildren());
/* 4129:4388 */     for (int i = 0; i < dataM.getNumChildren(); i++) {
/* 4130:4390 */       getInstance().saveKeyValue("keyvalue", path + dataM.get(i).getId().replace("-", "/"), dataM.get(i).get().toString());
/* 4131:     */     }
/* 4132:     */   }
/* 4133:     */   
/* 4134:     */   public void saveCMEData(XModel context, String contextType, XModel dataM)
/* 4135:     */     throws Exception
/* 4136:     */   {
/* 4137:4397 */     XModel areaM = (XModel)context.get("area");
/* 4138:4398 */     XModel houseM = (XModel)context.get("house");
/* 4139:4399 */     XModel householdM = (XModel)context.get("household");
/* 4140:4400 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 4141:4401 */     XModel indivM = (XModel)context.get("va");
/* 4142:4402 */     XModel visitM = (XModel)context.get("visit");
/* 4143:4403 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 4144:4404 */     XModel details = new XBaseModel();
/* 4145:4405 */     String where = "";
/* 4146:4406 */     String table = "";
/* 4147:4407 */     String idField = "";
/* 4148:4408 */     String id = "";
/* 4149:4409 */     String path = "/cme/" + indivM.get() + "/";
/* 4150:4410 */     this.logger.info("Save VA called " + dataM.getNumChildren());
/* 4151:4411 */     for (int i = 0; i < dataM.getNumChildren(); i++) {
/* 4152:4413 */       getInstance().saveKeyValue("keyvalue", path + dataM.get(i).getId().replace("-", "/"), dataM.get(i).get().toString());
/* 4153:     */     }
/* 4154:     */   }
/* 4155:     */   
/* 4156:     */   public void saveEnumData(XModel context, String contextType, XModel dataM)
/* 4157:     */     throws Exception
/* 4158:     */   {
/* 4159:4420 */     XModel areaM = (XModel)context.get("area");
/* 4160:4421 */     XModel houseM = (XModel)context.get("house");
/* 4161:4422 */     XModel householdM = (XModel)context.get("household");
/* 4162:4423 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 4163:4424 */     XModel indivM = (XModel)context.get("member");
/* 4164:4425 */     XModel visitM = (XModel)context.get("visit");
/* 4165:4426 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 4166:4427 */     XModel details = new XBaseModel();
/* 4167:4428 */     String where = "";
/* 4168:4429 */     String table = "";
/* 4169:4430 */     String idField = "";
/* 4170:4431 */     String id = "";
/* 4171:4433 */     if (contextType.equals("std"))
/* 4172:     */     {
/* 4173:4435 */       XModel clinicM = (XModel)context.get("clinic");
/* 4174:4436 */       XModel stdM = (XModel)context.get("std");
/* 4175:4437 */       where = "uniqno='" + stdM.get() + "'";
/* 4176:4438 */       table = "patients";
/* 4177:4439 */       idField = "uniqno";
/* 4178:4440 */       ((XModel)dataM.get("ictc_code")).set(clinicM.get());
/* 4179:4441 */       ((XModel)dataM.get("uniqno")).set(stdM.get());
/* 4180:     */     }
/* 4181:4444 */     if (contextType.equals("ictc_result"))
/* 4182:     */     {
/* 4183:4446 */       XModel clinicM = (XModel)context.get("clinic");
/* 4184:4447 */       XModel resultM = (XModel)context.get("ictc_result");
/* 4185:4448 */       where = "uniqno='" + resultM.get() + "'";
/* 4186:4449 */       table = "ictc_result";
/* 4187:4450 */       idField = "uniqno";
/* 4188:4451 */       ((XModel)dataM.get("ictc_code")).set(clinicM.get());
/* 4189:4452 */       ((XModel)dataM.get("uniqno")).set(resultM.get());
/* 4190:     */     }
/* 4191:4455 */     if (contextType.equals("va"))
/* 4192:     */     {
/* 4193:4457 */       saveVAData(context, contextType, dataM);
/* 4194:4458 */       return;
/* 4195:     */     }
/* 4196:4461 */     if ((contextType.equals("member")) && (indivM != null) && (indivM.get() != null) && (!indivM.equals("")))
/* 4197:     */     {
/* 4198:4463 */       where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "' and idc='" + indivM.get() + "'";
/* 4199:4464 */       table = "members";
/* 4200:4465 */       idField = "idc";
/* 4201:4466 */       ((XModel)dataM.get("enum_area")).set(areaM.get());
/* 4202:4467 */       ((XModel)dataM.get("house")).set(houseM.get());
/* 4203:4468 */       ((XModel)dataM.get("household")).set(householdM.get());
/* 4204:4469 */       ((XModel)dataM.get("idc")).set(indivM.get());
/* 4205:     */     }
/* 4206:4471 */     else if (((contextType.equals("births")) || (contextType.equals("marriages"))) && (contextTypeM != null))
/* 4207:     */     {
/* 4208:4473 */       where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "' and idc='" + contextTypeM.get() + "'";
/* 4209:4474 */       table = contextType;
/* 4210:4475 */       idField = "idc";
/* 4211:4476 */       ((XModel)dataM.get("enum_area")).set(areaM.get());
/* 4212:4477 */       ((XModel)dataM.get("house")).set(houseM.get());
/* 4213:4478 */       ((XModel)dataM.get("household")).set(householdM.get());
/* 4214:4479 */       ((XModel)dataM.get("idc")).set(contextTypeM.get());
/* 4215:     */     }
/* 4216:4481 */     else if ((contextType.equals("visit")) && (visitM != null) && (visitM.get() != null) && (!visitM.equals("")))
/* 4217:     */     {
/* 4218:4483 */       where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "' and visitid='" + visitM.get() + "' and teamid='" + surveyorM.get() + "'";
/* 4219:4484 */       table = "visit";
/* 4220:4485 */       idField = "visitid";
/* 4221:4486 */       ((XModel)dataM.get("enum_area")).set(areaM.get());
/* 4222:4487 */       ((XModel)dataM.get("house")).set(houseM.get());
/* 4223:4488 */       ((XModel)dataM.get("household")).set(householdM.get());
/* 4224:4489 */       ((XModel)dataM.get("visitid")).set(visitM.get());
/* 4225:4490 */       ((XModel)dataM.get("visittime")).set(visitM.get());
/* 4226:4491 */       ((XModel)dataM.get("teamid")).set(surveyorM.get());
/* 4227:     */     }
/* 4228:4493 */     else if ((householdM != null) && (householdM.get() != null) && (!householdM.equals("")))
/* 4229:     */     {
/* 4230:4495 */       where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "'";
/* 4231:4496 */       table = "households";
/* 4232:4497 */       idField = "household";
/* 4233:4498 */       id = householdM.get().toString();
/* 4234:4499 */       ((XModel)dataM.get("enum_area")).set(areaM.get());
/* 4235:4500 */       ((XModel)dataM.get("house")).set(houseM.get());
/* 4236:4501 */       ((XModel)dataM.get("household")).set(householdM.get());
/* 4237:     */     }
/* 4238:4503 */     else if ((houseM != null) && (houseM.get() != null) && (!houseM.equals("")))
/* 4239:     */     {
/* 4240:4505 */       where = "enum_area='" + areaM.get() + "' and houseno='" + houseM.get() + "'";
/* 4241:4506 */       table = "houses";
/* 4242:4507 */       idField = "houseno";
/* 4243:4508 */       id = houseM.get().toString();
/* 4244:4509 */       ((XModel)dataM.get("enum_area")).set(areaM.get());
/* 4245:4510 */       ((XModel)dataM.get("houseno")).set(houseM.get());
/* 4246:     */     }
/* 4247:4513 */     saveDataM1(table, where, dataM);
/* 4248:     */   }
/* 4249:     */   
/* 4250:     */   private void saveISTPData(XModel context, String contextType, XModel dataM)
/* 4251:     */     throws Exception
/* 4252:     */   {
/* 4253:4519 */     XModel areaM = (XModel)context.get("area");
/* 4254:4520 */     XModel houseM = (XModel)context.get("clinic");
/* 4255:     */     
/* 4256:4522 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 4257:4523 */     XModel indivM = (XModel)context.get("std");
/* 4258:4524 */     XModel visitM = (XModel)context.get("visit");
/* 4259:4525 */     XModel contextTypeM = (XModel)context.get(contextType);
/* 4260:4526 */     XModel details = new XBaseModel();
/* 4261:4527 */     String where = "";
/* 4262:4528 */     String table = "";
/* 4263:4529 */     String idField = "";
/* 4264:4530 */     String id = "";
/* 4265:4531 */     String path = "/std/" + areaM.get() + indivM.get() + "/" + surveyorM.get() + "/";
/* 4266:4532 */     this.logger.info("Save VA called " + dataM.getNumChildren());
/* 4267:4533 */     for (int i = 0; i < dataM.getNumChildren(); i++) {
/* 4268:4535 */       getInstance().saveKeyValue("keyvalue", path + dataM.get(i).getId().replace("-", "/"), dataM.get(i).get().toString());
/* 4269:     */     }
/* 4270:     */   }
/* 4271:     */   
/* 4272:     */   public String getCount(String table, String where)
/* 4273:     */   {
/* 4274:4541 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4275:4542 */     dt.setupTable(table, "count(*) idc", where, "test", false);
/* 4276:4543 */     dt.retrieve();
/* 4277:4544 */     this.logger.info(" Count is " + ((XModel)dt.get(0).get("idc")).get().toString());
/* 4278:4545 */     return ((XModel)dt.get(0).get("idc")).get().toString();
/* 4279:     */   }
/* 4280:     */   
/* 4281:     */   public String getMaxId(XModel context, String contextType)
/* 4282:     */     throws Exception
/* 4283:     */   {
/* 4284:4551 */     XModel areaM = (XModel)context.get("area");
/* 4285:4552 */     XModel houseM = (XModel)context.get("house");
/* 4286:4553 */     XModel clinicM = (XModel)context.get("clinic");
/* 4287:4554 */     XModel householdM = (XModel)context.get("household");
/* 4288:4555 */     XModel surveyorM = (XModel)context.get("surveyor");
/* 4289:4556 */     String id = "0";
/* 4290:4558 */     if (contextType.equals("std"))
/* 4291:     */     {
/* 4292:4560 */       id = getMaxStdId(clinicM.get().toString());
/* 4293:4561 */       return id;
/* 4294:     */     }
/* 4295:4563 */     if (contextType.equals("ictc_result"))
/* 4296:     */     {
/* 4297:4565 */       id = getMaxStdId(clinicM.get().toString());
/* 4298:4566 */       return id;
/* 4299:     */     }
/* 4300:4568 */     if ((householdM != null) && (householdM.get() != null) && (!householdM.equals("")))
/* 4301:     */     {
/* 4302:4570 */       if (contextType.equals("member"))
/* 4303:     */       {
/* 4304:4572 */         id = getMaxIndivId(areaM.get().toString(), houseM.get().toString(), householdM.get().toString());
/* 4305:4573 */         id = Integer.parseInt(id) + 1;
/* 4306:     */       }
/* 4307:     */       else
/* 4308:     */       {
/* 4309:4577 */         String where = "enum_area='" + areaM.get() + "' and house='" + houseM.get() + "' and household='" + householdM.get() + "'";
/* 4310:4578 */         id = getCount(contextType, where);
/* 4311:4579 */         id = Integer.parseInt(id) + 1;
/* 4312:     */       }
/* 4313:     */     }
/* 4314:4582 */     else if ((houseM != null) && (houseM.get() != null) && (!houseM.equals("")))
/* 4315:     */     {
/* 4316:4584 */       id = getMaxHouseholdId(areaM.get().toString(), houseM.get().toString());
/* 4317:4585 */       id = Integer.parseInt(id) + 1;
/* 4318:     */     }
/* 4319:4587 */     else if ((areaM != null) && (areaM.get() != null) && (!areaM.equals("")))
/* 4320:     */     {
/* 4321:4589 */       id = getMaxHouseId(areaM.get().toString(), surveyorM.get().toString());
/* 4322:4590 */       this.logger.info(" Max house id =" + id);
/* 4323:4591 */       id = Integer.parseInt(id) + 1;
/* 4324:     */     }
/* 4325:4594 */     return id;
/* 4326:     */   }
/* 4327:     */   
/* 4328:     */   public String getMaxIndivId(String area, String house, String household)
/* 4329:     */     throws Exception
/* 4330:     */   {
/* 4331:4600 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4332:4601 */     dt.setupTable("members", "max(idc) idc", "enum_area=" + area + " and house=" + house + " and household=" + household, "test", false);
/* 4333:4602 */     dt.retrieve();
/* 4334:     */     
/* 4335:4604 */     this.logger.info(" Individuals " + dt.getNumChildren() + " " + household);
/* 4336:     */     
/* 4337:4606 */     this.logger.info(dt.get(0).get(0));
/* 4338:4607 */     String id = dt.get(0).get("idc").toString();
/* 4339:4608 */     if (id == null) {
/* 4340:4609 */       return household + "00";
/* 4341:     */     }
/* 4342:     */     try
/* 4343:     */     {
/* 4344:4612 */       Integer.parseInt(id);
/* 4345:     */     }
/* 4346:     */     catch (NumberFormatException e)
/* 4347:     */     {
/* 4348:4616 */       e.printStackTrace();
/* 4349:4617 */       return household + "00";
/* 4350:     */     }
/* 4351:4619 */     return id;
/* 4352:     */   }
/* 4353:     */   
/* 4354:     */   public String getMaxStdId(String clinic)
/* 4355:     */     throws Exception
/* 4356:     */   {
/* 4357:4625 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
/* 4358:4626 */     return clinic + "-" + sdf.format(new Date());
/* 4359:     */   }
/* 4360:     */   
/* 4361:     */   public String getMaxHouseId(String area, String surveyorId)
/* 4362:     */     throws Exception
/* 4363:     */   {
/* 4364:4632 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4365:4633 */     dt.setupTable("houses", "max(houseno) houseno", "enum_area=" + area + " and houseno like '" + surveyorId + "%'", "test", false);
/* 4366:4634 */     dt.retrieve();
/* 4367:     */     
/* 4368:4636 */     int i = 0;
/* 4369:4636 */     if (i < dt.getNumChildren())
/* 4370:     */     {
/* 4371:4638 */       this.logger.info(dt.get(i).get(0));
/* 4372:4639 */       String id = dt.get(i).get("houseno").toString();
/* 4373:4640 */       if (id == null) {
/* 4374:4641 */         return surveyorId + "000";
/* 4375:     */       }
/* 4376:4643 */       return id;
/* 4377:     */     }
/* 4378:4646 */     return surveyorId + "000";
/* 4379:     */   }
/* 4380:     */   
/* 4381:     */   public String getMaxHouseholdId(String area, String house)
/* 4382:     */     throws Exception
/* 4383:     */   {
/* 4384:4652 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4385:4653 */     dt.setupTable("households", "max(household) household", "enum_area=" + area + " and house = '" + house + "'", "test", false);
/* 4386:4654 */     dt.retrieve();
/* 4387:     */     
/* 4388:4656 */     int i = 0;
/* 4389:4656 */     if (i < dt.getNumChildren())
/* 4390:     */     {
/* 4391:4658 */       this.logger.info(dt.get(i).get(0));
/* 4392:4659 */       String id = dt.get(i).get("household").toString();
/* 4393:4660 */       if (id == null) {
/* 4394:4661 */         return house + "00";
/* 4395:     */       }
/* 4396:4662 */       return id;
/* 4397:     */     }
/* 4398:4665 */     return null;
/* 4399:     */   }
/* 4400:     */   
/* 4401:     */   public XModel getIndividuals(String area, String house, String household, XModel hhM)
/* 4402:     */     throws Exception
/* 4403:     */   {
/* 4404:4671 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4405:4672 */     dt.setupTable("members", "idc", "enum_area=" + area + " and house=" + house + " and household=" + household, "test", false);
/* 4406:4673 */     dt.retrieve();
/* 4407:     */     
/* 4408:4675 */     this.logger.info(" Individuals " + dt.getNumChildren() + " " + household);
/* 4409:4676 */     for (int i = 0; i < dt.getNumChildren(); i++)
/* 4410:     */     {
/* 4411:4678 */       String id = dt.get(i).get("idc").toString();
/* 4412:4679 */       XModel tt = (XModel)hhM.get(id);
/* 4413:     */       
/* 4414:4681 */       tt.setId(dt.get(i).get("idc").toString());
/* 4415:4682 */       XModel xm = getIndividualdetails(id, household, house, area);
/* 4416:     */       
/* 4417:4684 */       tt.append(xm);
/* 4418:4685 */       tt.append(getDataM("data", "interview", area, house, household, id));
/* 4419:     */     }
/* 4420:4688 */     return hhM;
/* 4421:     */   }
/* 4422:     */   
/* 4423:     */   public XModel getIndividualdetails(String individual, String household, String house, String area)
/* 4424:     */   {
/* 4425:4693 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4426:4694 */     dt.setupTable("members", "*", "enum_area=" + area + " and house=" + house + " and household=" + household + " and idc=" + individual, "test", true);
/* 4427:4695 */     dt.setName("updatestatus");
/* 4428:4696 */     dt.setId("updatestatus");
/* 4429:4697 */     dt.setTagName("data");
/* 4430:4698 */     dt.retrieve();
/* 4431:4699 */     XBaseModel xm = new XBaseModel();
/* 4432:4700 */     xm.setId("updatestatus");
/* 4433:4702 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 4434:4704 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 4435:     */       {
/* 4436:4706 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 4437:4707 */         tt.set(dt.get(i).get(j).get());
/* 4438:     */       }
/* 4439:     */     }
/* 4440:4712 */     return xm;
/* 4441:     */   }
/* 4442:     */   
/* 4443:     */   public XModel getInterview(String individual, String household, String house, String area, XModel xm)
/* 4444:     */   {
/* 4445:4717 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4446:4718 */     dt.setupTable("barshi_interview", "*", "enum_area=" + area + " and house=" + house + " and household=" + household + " and idc=" + individual, "test", true);
/* 4447:     */     
/* 4448:4720 */     dt.retrieve();
/* 4449:     */     
/* 4450:4722 */     xm.setId("interview");
/* 4451:4723 */     if (dt.getNumChildren() == 0)
/* 4452:     */     {
/* 4453:4725 */       getProto("barshi_interview", xm);
/* 4454:4726 */       xm.set("enum_area", area);
/* 4455:4727 */       xm.set("house", house);
/* 4456:4728 */       xm.set("household", household);
/* 4457:4729 */       xm.set("idc", individual);
/* 4458:4730 */       return xm;
/* 4459:     */     }
/* 4460:4732 */     getDetails(dt, xm);
/* 4461:     */     
/* 4462:4734 */     return xm;
/* 4463:     */   }
/* 4464:     */   
/* 4465:     */   public XModel getCC(String household, String house, String area, XModel xm)
/* 4466:     */   {
/* 4467:4739 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4468:4740 */     dt.setupTable("barshi_cc", "*", "enum_area=" + area + " and house=" + house + " and household=" + household, "test", true);
/* 4469:     */     
/* 4470:4742 */     dt.retrieve();
/* 4471:     */     
/* 4472:4744 */     xm.setId("characterstics");
/* 4473:4745 */     if (dt.getNumChildren() == 0)
/* 4474:     */     {
/* 4475:4747 */       getProto("barshi_cc", xm);
/* 4476:4748 */       xm.set("enum_area", area);
/* 4477:4749 */       xm.set("house", house);
/* 4478:4750 */       xm.set("household", household);
/* 4479:     */       
/* 4480:4752 */       return xm;
/* 4481:     */     }
/* 4482:4754 */     getDetails(dt, xm);
/* 4483:     */     
/* 4484:4756 */     return xm;
/* 4485:     */   }
/* 4486:     */   
/* 4487:     */   public void getDetails(DatabaseTableModel dt, XModel xm)
/* 4488:     */   {
/* 4489:4761 */     for (int i = 0; i < dt.getNumChildren(); i++) {
/* 4490:4763 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 4491:     */       {
/* 4492:4765 */         XModel tt = (XModel)xm.get(dt.getAttribName(j));
/* 4493:4766 */         tt.set(dt.get(i).get(j).get());
/* 4494:     */       }
/* 4495:     */     }
/* 4496:     */   }
/* 4497:     */   
/* 4498:     */   public void getProto(String table, XModel dataM)
/* 4499:     */   {
/* 4500:4773 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4501:4774 */     dt.setupTable(table, "*", "test", true);
/* 4502:4775 */     for (int i = 0; i < dt.getNumAttributes(); i++) {
/* 4503:4777 */       dataM.get(dt.getAttribName(i));
/* 4504:     */     }
/* 4505:     */   }
/* 4506:     */   
/* 4507:     */   public Object getBean(String bean)
/* 4508:     */   {
/* 4509:4783 */     return appContext.getBean(bean);
/* 4510:     */   }
/* 4511:     */   
/* 4512:     */   public void init() {}
/* 4513:     */   
/* 4514:     */   public void clearPool()
/* 4515:     */   {
/* 4516:4792 */     NamedConnectionManager.getInstance().reapObjects();
/* 4517:     */   }
/* 4518:     */   
/* 4519:     */   public void reset()
/* 4520:     */   {
/* 4521:4796 */     ((NamedConnectionManager)NamedConnectionManager.getInstance()).reset("test", driver, url, user, passwd);
/* 4522:     */   }
/* 4523:     */   
/* 4524:     */   public void test1(String[] args)
/* 4525:     */     throws Exception
/* 4526:     */   {
/* 4527:4802 */     NamedConnectionManager nc = (NamedConnectionManager)NamedConnectionManager.getInstance();
/* 4528:     */     
/* 4529:4804 */     nc.addConnection("test", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/vatest", "root", "password");
/* 4530:4805 */     ConnectionObject co = nc.getConnection("test");
/* 4531:     */     
/* 4532:4807 */     FileWriter fout = new FileWriter("c:\\testdbfetch.xml");
/* 4533:4808 */     XModel out = new XBaseModel();
/* 4534:4809 */     XModel taskM = out;
/* 4535:4810 */     getTasks(taskM, "12", "");
/* 4536:4811 */     XModel dataM = (XModel)out.get("survey/surveydata");
/* 4537:     */     
/* 4538:4813 */     dataM = getAreas("12", dataM, "2");
/* 4539:     */     
/* 4540:4815 */     XModel areaM = dataM.get(0);
/* 4541:4816 */     areaM = getHouses("13", areaM);
/* 4542:     */     
/* 4543:4818 */     String house = areaM.get(1).getId();
/* 4544:4819 */     this.logger.info(house);
/* 4545:4820 */     XModel houseM = areaM.get(1);
/* 4546:4821 */     houseM = getHouseholds("13", house, houseM);
/* 4547:     */     
/* 4548:4823 */     XDataSource.outputModel(fout, out);
/* 4549:     */     
/* 4550:4825 */     this.logger.info("Complete");
/* 4551:     */   }
/* 4552:     */   
/* 4553:     */   public String getTaskPath(String path)
/* 4554:     */   {
/* 4555:4830 */     return join(split("task0-1/task1-2/task3-3", "-", 0));
/* 4556:     */   }
/* 4557:     */   
/* 4558:     */   public String join(Vector v)
/* 4559:     */   {
/* 4560:4835 */     String t = "";
/* 4561:4836 */     for (int i = 0; i < v.size(); i++) {
/* 4562:4838 */       t = t + (i == 0 ? "" : "/") + v.get(i).toString();
/* 4563:     */     }
/* 4564:4841 */     return t;
/* 4565:     */   }
/* 4566:     */   
/* 4567:     */   public void testImport(String file)
/* 4568:     */     throws Exception
/* 4569:     */   {
/* 4570:4847 */     FileInputStream fin = new FileInputStream(file);
/* 4571:4848 */     DataInputStream din = new DataInputStream(fin);
/* 4572:4849 */     String line = "";
/* 4573:4850 */     StringBuffer logs = new StringBuffer();
/* 4574:4851 */     while (line != null)
/* 4575:     */     {
/* 4576:4853 */       line = din.readLine();
/* 4577:4854 */       logs.append(line + "\r\n");
/* 4578:     */     }
/* 4579:4857 */     this.logger.info(logs);
/* 4580:4858 */     importChangeLogs(logs.toString());
/* 4581:     */   }
/* 4582:     */   
/* 4583:     */   public void rollbackOutkeyvalue(String path, String table)
/* 4584:     */     throws Exception
/* 4585:     */   {
/* 4586:4864 */     XModel xm1 = new XBaseModel();
/* 4587:4865 */     getInstance().getData("outkeyvalue", "value1", "key1 like '" + path + "'", xm1);
/* 4588:4866 */     String value = xm1.get(0).get(0).get().toString();
/* 4589:4867 */     XModel testM = new XBaseModel();
/* 4590:4868 */     StringReader sr = new StringReader(value);
/* 4591:4869 */     XmlElement xe = XmlSource.read(sr);
/* 4592:4870 */     XDataSource xd = new XDataSource();
/* 4593:4871 */     xd.loadTable(xe, testM);
/* 4594:     */     
/* 4595:4873 */     this.logger.info(" Value " + value + " " + testM.get(0).getId());
/* 4596:4874 */     getInstance().saveKeyValue1(table, path, testM);
/* 4597:     */   }
/* 4598:     */   
/* 4599:     */   public static void main(String[] args)
/* 4600:     */     throws Exception
/* 4601:     */   {
/* 4602:4880 */     getInstance().validateMessage("D:/apache-tomcat-6.0.16/webapps/va/mbox/15-2011-08-09-02-08-06-687-received.xml");
/* 4603:4881 */     getInstance().validateMessage("c:/cghr/test/test.xml");
/* 4604:     */     
/* 4605:4883 */     System.in.read();
/* 4606:     */     
/* 4607:4885 */     getInstance().isPhysicianAway("Sagar", "2011-1-21");
/* 4608:4886 */     getInstance().isPhysicianAway("Sagar", "2011-1-21");
/* 4609:4887 */     getInstance().saveKeyValue("keyvalue", "test1", "world");
/* 4610:4888 */     getInstance().saveKeyValue("keyvalue", "test2", "world");
/* 4611:4889 */     getInstance().saveKeyValue("keyvalue", "test3", "world");
/* 4612:4890 */     System.in.read();
/* 4613:     */     
/* 4614:4892 */     Logger logger = Logger.getLogger(TestXUIDB.class);
/* 4615:4893 */     XModel xm1 = new XBaseModel();
/* 4616:     */     
/* 4617:4895 */     String[] keys1 = {
/* 4618:4896 */       "/cme/06300059_01_01", 
/* 4619:4897 */       "/cme/06300060_01_09", 
/* 4620:4898 */       "/cme/06300060_01_10", 
/* 4621:4899 */       "/cme/06300061_01_06", 
/* 4622:4900 */       "/cme/06300061_01_08", 
/* 4623:4901 */       "/cme/06300063_01_04", 
/* 4624:4902 */       "/cme/06300065_01_03" };
/* 4625:4904 */     for (int i = 0; i < keys1.length; i++) {
/* 4626:4906 */       getInstance().rollbackOutkeyvalue(keys1[i], "keyvalue_copy");
/* 4627:     */     }
/* 4628:4908 */     System.in.read();
/* 4629:4909 */     getInstance().createNotification("test", "test", "test\\'1\\'", "test", "test", null, null, "email");
/* 4630:4910 */     System.in.read();
/* 4631:4911 */     UserAuthentication ua = new UserAuthentication();
/* 4632:4912 */     ua.authenticate1("admin", "password");
/* 4633:     */     
/* 4634:4914 */     getInstance().sendOutboundLogs("admin");
/* 4635:     */     
/* 4636:4916 */     System.in.read();
/* 4637:     */     
/* 4638:4918 */     logger.info("Starting");
/* 4639:     */     
/* 4640:4920 */     XModel xm = getInstance().getKeyValChildren(null, "", "", "va");
/* 4641:4921 */     logger.info(Integer.valueOf(xm.getNumChildren()));
/* 4642:4922 */     System.in.read();
/* 4643:4923 */     String tt = getInstance().getTranslation("Yes", "mr");
/* 4644:4924 */     logger.info(tt);
/* 4645:4925 */     logger.info(getInstance().getTranslation("Had a doctor EVER stated that the deceased had the following diseases?", "Marathi"));
/* 4646:     */     
/* 4647:4927 */     System.in.read();
/* 4648:4928 */     ChangeLog.startLog("test", "test", "test");
/* 4649:     */     
/* 4650:4930 */     ChangeLog.endLog();
/* 4651:4931 */     System.in.read();
/* 4652:4932 */     logger.info("Starting");
/* 4653:     */     
/* 4654:4934 */     logger.info(getInstance().getTranslation("Date of birth of deceased", "Marathi"));
/* 4655:4935 */     logger.info(getInstance().getTranslation1(tt, "Marathi"));
/* 4656:     */     
/* 4657:4937 */     System.in.read();
/* 4658:     */     
/* 4659:4939 */     System.in.read();
/* 4660:     */     
/* 4661:4941 */     logger.info(Boolean.valueOf(getInstance().isPhysicianAway("Sagar", "2011-1-21")));
/* 4662:4942 */     logger.info(Boolean.valueOf(getInstance().updateAwayDate("Sagar", new Date("01/22/2011"))));
/* 4663:4943 */     System.in.read();
/* 4664:     */     
/* 4665:4945 */     logger.info(Boolean.valueOf(getInstance().isValidIcdAge("0.769", "C50")));
/* 4666:4946 */     logger.info(Boolean.valueOf(getInstance().isValidIcdSex("Male", "O34")));
/* 4667:4947 */     logger.info(Boolean.valueOf(getInstance().checkEquivalence("A40", "A41")));
/* 4668:     */     
/* 4669:4949 */     Vector keys = new Vector();
/* 4670:4950 */     keys.add("household");
/* 4671:     */     
/* 4672:4952 */     System.in.read();
/* 4673:     */     
/* 4674:4954 */     logger.info(xm.getNumChildren() + " " + xm.get(0).getId());
/* 4675:     */     
/* 4676:4956 */     String str = "<l id='Tue Apr 13 17:26:00 IST 2010'>\t\t<dt id='dt' table='data' key=\"name='cookingPlace' and area = '22' and house = '16048' and household = '1604801' and member is null\">\t\t<d id='data' area='22' house='16048' household='1604801'  name='cookingPlace' value=' Choose Any One' /></dt></l>";
/* 4677:     */   }
/* 4678:     */   
/* 4679:     */   public void saveLogistics(XModel xm)
/* 4680:     */     throws Exception
/* 4681:     */   {
/* 4682:4962 */     xm.set("@name", xm.getId());
/* 4683:4963 */     Object path = xm.get("@path");
/* 4684:4964 */     String where = "name='" + xm.getId() + "'";
/* 4685:4965 */     where = where + " and path='" + path + "'";
/* 4686:4966 */     saveDataM("logistics", where, xm);
/* 4687:     */   }
/* 4688:     */   
/* 4689:     */   public void saveTree(XModel root, String table, String parentPath)
/* 4690:     */     throws Exception
/* 4691:     */   {
/* 4692:4972 */     String key = parentPath + "/" + root.getId();
/* 4693:4973 */     String value = (String)root.get();
/* 4694:4974 */     if (value != null) {
/* 4695:4975 */       saveKeyValue(table, key, value);
/* 4696:     */     }
/* 4697:4977 */     for (int i = 0; i < root.getNumChildren(); i++) {
/* 4698:4979 */       saveTree(root.get(i), table, key);
/* 4699:     */     }
/* 4700:     */   }
/* 4701:     */   
/* 4702:     */   public void deleteKeyValue(String table, String key)
/* 4703:     */     throws Exception
/* 4704:     */   {
/* 4705:4986 */     String where = " key1 like '" + key + "'";
/* 4706:4987 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4707:4988 */     dt.setupTable(table, "*", where, "test", true);
/* 4708:     */     
/* 4709:4990 */     ChangeLog.startLog(table, "delete", where, getCurrentUser());
/* 4710:     */     
/* 4711:4992 */     String qry = "delete from " + table + " where " + where;
/* 4712:4993 */     PreparedStatement ps = dt.getTable().getPreparedStatement(qry);
/* 4713:4994 */     ps.execute();
/* 4714:4995 */     closePs(dt, ps);
/* 4715:     */     
/* 4716:4997 */     ChangeLog.endLog();
/* 4717:     */   }
/* 4718:     */   
/* 4719:     */   public synchronized void createKeyValueChangeLog(String table, String key, String value)
/* 4720:     */     throws Exception
/* 4721:     */   {
/* 4722:5003 */     String where = " key1 like '" + key + "'";
/* 4723:5004 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4724:5005 */     dt.setupTable(table, "*", where, "test", true);
/* 4725:     */     
/* 4726:5007 */     ChangeLog.startLog(table, where, getCurrentUser());
/* 4727:5008 */     ChangeLog.logField("key1", key);
/* 4728:5009 */     ChangeLog.logField("value1", value);
/* 4729:     */     
/* 4730:5011 */     ChangeLog.endLog();
/* 4731:     */   }
/* 4732:     */   
/* 4733:     */   public synchronized void createDeleteLog(String table, String where)
/* 4734:     */     throws Exception
/* 4735:     */   {
/* 4736:5017 */     ChangeLog.startLog(table, "delete", where, getCurrentUser());
/* 4737:5018 */     ChangeLog.endLog();
/* 4738:     */   }
/* 4739:     */   
/* 4740:     */   public synchronized void saveKeyValue(String table, String key, String value)
/* 4741:     */     throws Exception
/* 4742:     */   {
/* 4743:5024 */     if ((key == null) || (key.equals("")))
/* 4744:     */     {
/* 4745:5026 */       this.logger.info("Error :Key is null");
/* 4746:5027 */       return;
/* 4747:     */     }
/* 4748:5030 */     String where = " key1='" + key + "'";
/* 4749:5031 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4750:     */     
/* 4751:5033 */     ChangeLog.startLog(table, where, getCurrentUser());
/* 4752:     */     
/* 4753:5035 */     dt.setupTable(table, "*", where, "test", true);
/* 4754:     */     
/* 4755:5037 */     dt.retrieve();
/* 4756:5038 */     if (dt.getNumChildren() > 0)
/* 4757:     */     {
/* 4758:5040 */       this.logger.info(" total " + dt.getNumChildren());
/* 4759:     */       
/* 4760:5042 */       String qry = "update " + table + " set value1=?";
/* 4761:5043 */       int count = 0;
/* 4762:5044 */       this.logger.info("Query is " + qry);
/* 4763:     */       
/* 4764:5046 */       PreparedStatement ps = dt.getTable().getPreparedStatement(qry + " where " + where);
/* 4765:5047 */       ps.setBytes(1, value.getBytes("utf-8"));
/* 4766:5048 */       int updates = ps.executeUpdate();
/* 4767:     */       
/* 4768:5050 */       closePs(dt, ps);
/* 4769:     */     }
/* 4770:     */     else
/* 4771:     */     {
/* 4772:5054 */       String s = "insert into " + table;
/* 4773:5055 */       String flds = "key1,value1";
/* 4774:     */       
/* 4775:5057 */       String values = "'" + key + "',?";
/* 4776:5058 */       int count = 0;
/* 4777:     */       
/* 4778:5060 */       s = s + "(" + flds + ") VALUES(" + values + ")";
/* 4779:     */       
/* 4780:5062 */       this.logger.info(" Debug sql " + s);
/* 4781:     */       
/* 4782:5064 */       PreparedStatement ps = dt.getTable().getPreparedStatement(s);
/* 4783:5065 */       ps.setBytes(1, value.getBytes("utf-8"));
/* 4784:5066 */       ps.execute();
/* 4785:5067 */       closePs(dt, ps);
/* 4786:     */     }
/* 4787:5070 */     ChangeLog.logField("key1", key);
/* 4788:5071 */     ChangeLog.logField("value1", value);
/* 4789:5072 */     ChangeLog.endLog();
/* 4790:     */   }
/* 4791:     */   
/* 4792:     */   public synchronized void saveKeyValue1(String table, String key, XModel xm)
/* 4793:     */     throws Exception
/* 4794:     */   {
/* 4795:5078 */     if ((key == null) || (key.equals("")))
/* 4796:     */     {
/* 4797:5080 */       this.logger.info("Error :Key is null");
/* 4798:5081 */       return;
/* 4799:     */     }
/* 4800:5083 */     if (xm.get() != null)
/* 4801:     */     {
/* 4802:5085 */       this.logger.info(" Saving " + key + " Value= " + (String)xm.get());
/* 4803:     */       
/* 4804:5087 */       saveKeyValue(table, key, xm.get().toString());
/* 4805:     */     }
/* 4806:5089 */     for (int i = 0; i < xm.getNumChildren(); i++) {
/* 4807:5091 */       saveKeyValue1(table, key + "/" + xm.get(i).getId(), xm.get(i));
/* 4808:     */     }
/* 4809:     */   }
/* 4810:     */   
/* 4811:     */   public void readTree(XModel root, String table, String path)
/* 4812:     */   {
/* 4813:5097 */     getKeyValues(root, "keyvalue", path);
/* 4814:     */   }
/* 4815:     */   
/* 4816:     */   public String getKeyValuesSerialized(String table, String parentPath)
/* 4817:     */   {
/* 4818:5102 */     XModel xm = new XBaseModel();
/* 4819:5103 */     getKeyValues(xm, table, parentPath);
/* 4820:5104 */     StringWriter sw = new StringWriter();
/* 4821:     */     
/* 4822:5106 */     XDataSource.outputModel(sw, xm);
/* 4823:5107 */     return sw.getBuffer().toString();
/* 4824:     */   }
/* 4825:     */   
/* 4826:     */   public String getKeyValues(XModel xm, String table, String parentPath)
/* 4827:     */   {
/* 4828:5112 */     String where = " key1 like '" + parentPath + "/%' ";
/* 4829:5113 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4830:     */     
/* 4831:5115 */     dt.setupTable(table, "*", where, "test", false);
/* 4832:     */     
/* 4833:5117 */     dt.retrieve();
/* 4834:5118 */     String keyFld = "key1";
/* 4835:5119 */     String valFld = "value1";
/* 4836:5120 */     this.logger.info("---" + table + "   " + where + " " + dt.getNumChildren());
/* 4837:5121 */     if (dt.getNumChildren() > 0) {
/* 4838:5123 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 4839:     */       {
/* 4840:5125 */         String key = dt.get(i).get(keyFld).toString();
/* 4841:5126 */         String value = dt.get(i).get(valFld).toString();
/* 4842:     */         
/* 4843:5128 */         KenList kl = new KenList(key);
/* 4844:5129 */         KenList k2 = new KenList(parentPath);
/* 4845:5130 */         String path1 = kl.subset(k2.size(), kl.size() - 1).toString();
/* 4846:5131 */         this.logger.info("keyy====::" + key);
/* 4847:5132 */         this.logger.info("valuepath====::" + path1);
/* 4848:5133 */         this.logger.info("Value of key ====::" + value);
/* 4849:5134 */         this.logger.info(key + " " + parentPath + " " + path1);
/* 4850:5135 */         xm.set(path1, value);
/* 4851:     */       }
/* 4852:     */     }
/* 4853:5140 */     return null;
/* 4854:     */   }
/* 4855:     */   
/* 4856:     */   public String getValue(String table, String key)
/* 4857:     */   {
/* 4858:5145 */     String where = " key1='" + key + "'";
/* 4859:5146 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 4860:     */     
/* 4861:5148 */     dt.setupTable(table, "*", where, "test", true);
/* 4862:     */     
/* 4863:5150 */     dt.retrieve();
/* 4864:5151 */     if (dt.getNumChildren() > 0)
/* 4865:     */     {
/* 4866:5153 */       for (int j = 0; j < dt.getNumAttributes(); j++)
/* 4867:     */       {
/* 4868:5155 */         String attrib = dt.getAttribName(j);
/* 4869:5156 */         String attrib1 = attrib.toLowerCase();
/* 4870:5158 */         if (attrib1.equals("value1")) {
/* 4871:5159 */           return dt.get(0).get(attrib).toString();
/* 4872:     */         }
/* 4873:     */       }
/* 4874:5163 */       return dt.get(0).get("value1").toString();
/* 4875:     */     }
/* 4876:5165 */     return null;
/* 4877:     */   }
/* 4878:     */   
/* 4879:     */   public void saveInterview1(XModel xm)
/* 4880:     */     throws Exception
/* 4881:     */   {
/* 4882:5171 */     xm.set("@name", xm.getId());
/* 4883:5172 */     Object area = xm.get("@area");
/* 4884:5173 */     Object house = xm.get("@house");
/* 4885:5174 */     Object household = xm.get("@household");
/* 4886:5175 */     Object member = xm.get("@member");
/* 4887:5176 */     String where = "name='" + xm.getId() + "'";
/* 4888:5177 */     where = where + " and " + (area == null ? "area is null" : new StringBuilder("area = '").append(area).append("'").toString());
/* 4889:5178 */     where = where + " and " + (house == null ? "house is null" : new StringBuilder("house = '").append(house).append("'").toString());
/* 4890:5179 */     where = where + " and " + (household == null ? "household is null" : new StringBuilder("household = '").append(household).append("'").toString());
/* 4891:5180 */     where = where + " and " + (member == null ? "member is null" : new StringBuilder("member = '").append(member).append("'").toString());
/* 4892:5181 */     saveDataM("data", where, xm);
/* 4893:     */   }
/* 4894:     */   
/* 4895:     */   public void saveHouse(String area, String id, XModel houseM)
/* 4896:     */     throws Exception
/* 4897:     */   {
/* 4898:5187 */     saveData("houses", "enum_area='" + area + "' and houseno='" + id + "'", houseM);
/* 4899:     */   }
/* 4900:     */   
/* 4901:     */   public void saveHouseHold(String area, String house, String id, XModel hhM)
/* 4902:     */     throws Exception
/* 4903:     */   {
/* 4904:5193 */     saveData("households", "enum_area='" + area + "' and  house='" + house + "' and household='" + id + "'", hhM);
/* 4905:     */   }
/* 4906:     */   
/* 4907:     */   public void saveMember(String area, String house, String hh, String id, XModel indvM)
/* 4908:     */     throws Exception
/* 4909:     */   {
/* 4910:5199 */     saveData("members", "enum_area='" + area + "' and house='" + house + "' and household='" + hh + "' and idc='" + id + "'", indvM);
/* 4911:     */   }
/* 4912:     */   
/* 4913:     */   public void saveVisitInfo(String area, String house, String hh, String idc, String team, String doneBy, XModel visitM)
/* 4914:     */     throws Exception
/* 4915:     */   {
/* 4916:5205 */     saveData("members", "area='" + area + "' and houseno='" + house + "' and  householdno='" + hh + "' and idc='" + idc + "' and team=" + team + " and doneby=" + doneBy, visitM);
/* 4917:     */   }
/* 4918:     */   
/* 4919:     */   public void saveInterview(String area, String house, String hh, String idc, XModel interviewM)
/* 4920:     */     throws Exception
/* 4921:     */   {
/* 4922:5211 */     saveData("barshi_interview", "enum_area='" + area + "' and house='" + house + "' and household='" + hh + "' and idc='" + idc + "'", interviewM);
/* 4923:     */   }
/* 4924:     */   
/* 4925:     */   public void saveResponse(String area, String house, String hh, String idc, XModel interviewM)
/* 4926:     */     throws Exception
/* 4927:     */   {
/* 4928:5217 */     saveData("responsedetails", "area='" + area + "' and house='" + house + "' householdno='" + hh + "' idc='" + idc + "'", interviewM);
/* 4929:     */   }
/* 4930:     */   
/* 4931:     */   public void saveCommon(String area, String house, String hh, XModel ccM)
/* 4932:     */     throws Exception
/* 4933:     */   {
/* 4934:5223 */     saveData("barshi_cc", "enum_area='" + area + "' and house='" + house + "' and household='" + hh + "' ", ccM);
/* 4935:     */   }
/* 4936:     */   
/* 4937:     */   public void saveTask2(String taskPath, String surveyType, String area, String house, String hh, String individual, XModel taskM)
/* 4938:     */     throws Exception
/* 4939:     */   {
/* 4940:5229 */     String table = "tasks";
/* 4941:     */     
/* 4942:5231 */     String where = "task='" + taskPath + "' and area=" + area + " and house='" + house + "' and household='" + hh + "' and member='" + individual + "' and survey_type='" + surveyType + "' and status != 1";
/* 4943:     */     
/* 4944:5233 */     XModel dataM = taskM;
/* 4945:     */   }
/* 4946:     */   
/* 4947:     */   public void save(XModel xM, String area, String house, String hh, String idc)
/* 4948:     */     throws Exception
/* 4949:     */   {
/* 4950:5239 */     this.logger.info("/*********** Save Called " + xM.getId() + "***/");
/* 4951:5240 */     if (xM.getId().equals("characterstics")) {
/* 4952:5242 */       saveCommon(area, house, hh, xM);
/* 4953:     */     }
/* 4954:5244 */     if (xM.getId().equals("interview")) {
/* 4955:5246 */       saveInterview(area, house, hh, idc, xM);
/* 4956:     */     }
/* 4957:5248 */     xM.getId().equals("responsedetails");
/* 4958:     */   }
/* 4959:     */   
/* 4960:     */   public void get(XModel xM, String area, String house, String hh, String idc)
/* 4961:     */     throws Exception
/* 4962:     */   {
/* 4963:5254 */     this.logger.info("/*********** Save Called " + xM.getId() + "***/");
/* 4964:5255 */     if (xM.getId().equals("characterstics")) {
/* 4965:5257 */       xM = getCC(hh, house, area, xM);
/* 4966:     */     }
/* 4967:5259 */     if (xM.getId().equals("interview")) {
/* 4968:5261 */       xM = getInterview(idc, hh, house, area, xM);
/* 4969:     */     }
/* 4970:5263 */     xM.getId().equals("responsedetails");
/* 4971:     */   }
/* 4972:     */   
/* 4973:     */   public XModel get1(String path, String area, String house, String hh, String idc)
/* 4974:     */     throws Exception
/* 4975:     */   {
/* 4976:5269 */     StringTokenizer st = new StringTokenizer(path, "/");
/* 4977:5270 */     String name = path;
/* 4978:5272 */     while (st.hasMoreTokens()) {
/* 4979:5274 */       name = st.nextToken();
/* 4980:     */     }
/* 4981:5276 */     this.logger.info("/*********** Get Called " + name + "***/");
/* 4982:5277 */     XDataModel xM = getDataM("data", name, area, house, hh, idc);
/* 4983:5278 */     return xM;
/* 4984:     */   }
/* 4985:     */   
/* 4986:     */   public void authenticateUser(String user, String passwd)
/* 4987:     */   {
/* 4988:5283 */     DatabaseTableModel dtm = DatabaseTableModel.getTable("team");
/* 4989:     */     
/* 4990:5285 */     dtm.first();
/* 4991:     */   }
/* 4992:     */   
/* 4993:     */   public void authoriseUser(String roles)
/* 4994:     */   {
/* 4995:5290 */     DatabaseTableModel dtm = DatabaseTableModel.getTable("team");
/* 4996:     */     
/* 4997:5292 */     dtm.first();
/* 4998:     */   }
/* 4999:     */   
/* 5000:     */   public boolean isPhysicianAway(String id, String date)
/* 5001:     */   {
/* 5002:5297 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 5003:     */     
/* 5004:5299 */     dt.setupTable("physician_away", "*", "physician='" + id + "' and away_date='" + date + "'", "test", false);
/* 5005:5300 */     dt.retrieve();
/* 5006:5301 */     this.logger.info("No of Rows:" + dt.getNumChildren());
/* 5007:5302 */     if (dt.getNumChildren() > 0) {
/* 5008:5303 */       return true;
/* 5009:     */     }
/* 5010:5304 */     return false;
/* 5011:     */   }
/* 5012:     */   
/* 5013:     */   public boolean isPhysician(String username, String password)
/* 5014:     */   {
/* 5015:5309 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 5016:     */     
/* 5017:5311 */     dt.setupTable("accounts", "*", "username='" + username + "' and password='" + password + "'", "test", false);
/* 5018:5312 */     dt.retrieve();
/* 5019:5313 */     this.logger.info("No of Rows:" + dt.getNumChildren());
/* 5020:5314 */     if (dt.getNumChildren() > 0) {
/* 5021:5315 */       return true;
/* 5022:     */     }
/* 5023:5316 */     return false;
/* 5024:     */   }
/* 5025:     */   
/* 5026:     */   public boolean updateAwayDate(String id, Date date)
/* 5027:     */     throws Exception
/* 5028:     */   {
/* 5029:5322 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* 5030:5323 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 5031:     */     
/* 5032:5325 */     dt.setupTable("physician_away", "*", "physician='" + id + "'", "test", false);
/* 5033:5326 */     String sql = "insert into physician_away (physician, away_date) values ('" + id + "','" + sdf.format(date) + "')";
/* 5034:5327 */     PreparedStatement ps = dt.getTable().getPreparedStatement(sql);
/* 5035:5328 */     ps.execute();
/* 5036:5329 */     closePs(dt, ps);
/* 5037:     */     
/* 5038:5331 */     return true;
/* 5039:     */   }
/* 5040:     */   
/* 5041:     */   public void createPhysician(String username, String name, String languages, String coder, String adjudicator, String id, String status)
/* 5042:     */     throws Exception
/* 5043:     */   {
/* 5044:5337 */     XModel xm = new XBaseModel();
/* 5045:     */     
/* 5046:5339 */     XModel dataM = new XBaseModel();
/* 5047:5340 */     dataM.setId(username);
/* 5048:5341 */     ((XModel)dataM.get("username")).set(username);
/* 5049:5342 */     ((XModel)dataM.get("name")).set(name);
/* 5050:5343 */     ((XModel)dataM.get("languages")).set(languages);
/* 5051:5344 */     ((XModel)dataM.get("coder")).set(coder);
/* 5052:5345 */     ((XModel)dataM.get("adjudicator")).set(adjudicator);
/* 5053:5346 */     ((XModel)dataM.get("status")).set(status);
/* 5054:5347 */     if (!id.equals("0")) {
/* 5055:5348 */       ((XModel)dataM.get("id")).set(id);
/* 5056:     */     }
/* 5057:5350 */     getInstance().saveData("physician", "username='" + username + "'", dataM);
/* 5058:     */   }
/* 5059:     */   
/* 5060:     */   public void createAccount(String username, String password, String roles)
/* 5061:     */     throws Exception
/* 5062:     */   {
/* 5063:5356 */     XModel xm = new XBaseModel();
/* 5064:     */     
/* 5065:5358 */     XModel dataM = new XBaseModel();
/* 5066:5359 */     dataM.setId(username);
/* 5067:5360 */     ((XModel)dataM.get("username")).set(username);
/* 5068:5361 */     ((XModel)dataM.get("password")).set(password);
/* 5069:5362 */     ((XModel)dataM.get("roles")).set(roles);
/* 5070:5363 */     getInstance().saveData("accounts", "username='" + username + "'", dataM);
/* 5071:     */   }
/* 5072:     */   
/* 5073:     */   public boolean physicianExists(String username)
/* 5074:     */   {
/* 5075:5368 */     XModel xm = new XBaseModel();
/* 5076:5369 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 5077:5370 */     dt.setupTable("physician", "*", "username='" + username + "'", "test", false);
/* 5078:5371 */     dt.retrieve();
/* 5079:5372 */     if (dt.getNumChildren() > 0) {
/* 5080:5373 */       return true;
/* 5081:     */     }
/* 5082:5376 */     return false;
/* 5083:     */   }
/* 5084:     */   
/* 5085:     */   public boolean userExists(String username)
/* 5086:     */   {
/* 5087:5381 */     XModel xm = new XBaseModel();
/* 5088:5382 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 5089:5383 */     dt.setupTable("accounts", "*", "username='" + username + "'", "test", false);
/* 5090:5384 */     dt.retrieve();
/* 5091:5385 */     if (dt.getNumChildren() > 0) {
/* 5092:5386 */       return true;
/* 5093:     */     }
/* 5094:5389 */     return false;
/* 5095:     */   }
/* 5096:     */   
/* 5097:     */   public boolean getWorkLoad(String physician, XModel xm)
/* 5098:     */   {
/* 5099:5394 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 5100:5395 */     workload = getInstance().getProperty("workload");
/* 5101:     */     
/* 5102:5397 */     String sql = "SELECT a.id,SUM(IF(task LIKE '%task0/%',1,0)) number ,IF(b.STATUS='1','Complete','In Process') STATUS,IF(task LIKE '%task0','Coding',IF(task LIKE '%task1','Reconciliation',IF(task LIKE '%task2','Adjudication',NULL))) stage, a.username FROM physician  a LEFT JOIN tasks b ON a.id=b.assignedTo AND (task LIKE '%task0/%' OR task IS NULL)  WHERE task LIKE '%task0/%' AND task IS NOT NULL AND a.id LIKE '" + physician + "' GROUP BY a.id,IF(b.STATUS='1','Complete','In Process'),IF(task LIKE '%task0','Coding',IF(task LIKE '%task1','Reconciliation',IF(task LIKE '%task2','Adjudication',NULL)))";
/* 5103:5398 */     this.logger.info("sql::" + sql);
/* 5104:     */     
/* 5105:5400 */     dt.setSqlStatement(sql, "test", false);
/* 5106:     */     
/* 5107:5402 */     dt.retrieve();
/* 5108:5403 */     this.logger.info("No of Rows1:" + dt.getNumChildren());
/* 5109:5404 */     if (dt.getNumChildren() > 0) {
/* 5110:5406 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 5111:     */       {
/* 5112:5408 */         XModel row = (XModel)xm.get(i);
/* 5113:5409 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 5114:     */         {
/* 5115:5411 */           ((XModel)row.get(dt.getAttribName(j))).set(dt.get(i).get(j).get());
/* 5116:5412 */           this.logger.info(dt.getAttribName(j) + "=" + dt.get(i).get(j).get());
/* 5117:     */         }
/* 5118:     */       }
/* 5119:     */     }
/* 5120:5418 */     return false;
/* 5121:     */   }
/* 5122:     */   
/* 5123:     */   public boolean getPhysiciansWithLessWorkload(String language, String status, XModel xm)
/* 5124:     */   {
/* 5125:5423 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 5126:     */     
/* 5127:5425 */     workload = getInstance().getProperty("workload");
/* 5128:     */     
/* 5129:5427 */     String sql = "SELECT a.id,SUM(IF((task LIKE '%task0/%' ) AND (b.STATUS IS NULL OR b.STATUS =0),1,0)) number, IF(task LIKE '%task0','Coding',IF(task LIKE '%task1','Reconciliation',IF(task LIKE '%task2','Adjudication',NULL))) stage, a.username  FROM  physician  a LEFT JOIN tasks   b ON a.id=b.assignedTo WHERE  " + status + " AND a.languages LIKE '%" + language + "%'GROUP BY a.id  HAVING number < " + workload + " ORDER BY number,RAND() ASC";
/* 5130:5428 */     this.logger.info("sql::" + sql);
/* 5131:     */     
/* 5132:5430 */     dt.setSqlStatement(sql, "test", false);
/* 5133:     */     
/* 5134:5432 */     dt.retrieve();
/* 5135:5433 */     this.logger.info("No of Rows1:" + dt.getNumChildren());
/* 5136:5434 */     if (dt.getNumChildren() > 0) {
/* 5137:5436 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 5138:     */       {
/* 5139:5438 */         XModel row = (XModel)xm.get(i);
/* 5140:5439 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 5141:     */         {
/* 5142:5441 */           ((XModel)row.get(dt.getAttribName(j))).set(dt.get(i).get(j).get());
/* 5143:5442 */           this.logger.info(dt.getAttribName(j) + "=" + dt.get(i).get(j).get());
/* 5144:     */         }
/* 5145:     */       }
/* 5146:     */     }
/* 5147:5448 */     return false;
/* 5148:     */   }
/* 5149:     */   
/* 5150:     */   public int execSQL(String sql, StringBuffer buf)
/* 5151:     */     throws Exception
/* 5152:     */   {
/* 5153:5454 */     DatabaseTableModel dtm = new DatabaseTableModel();
/* 5154:     */     
/* 5155:5456 */     String[] cmd = sql.split(" ");
/* 5156:5457 */     if (cmd[0].toLowerCase().equals("update"))
/* 5157:     */     {
/* 5158:5459 */       dtm.setupTable(cmd[1], "*", "", "test", false);
/* 5159:5460 */       int count = dtm.executeUpdate(sql);
/* 5160:5461 */       return count;
/* 5161:     */     }
/* 5162:5464 */     if (cmd[0].toLowerCase().equals("insert"))
/* 5163:     */     {
/* 5164:5466 */       dtm.setupTable(cmd[2], "*", "", "test", false);
/* 5165:5467 */       int count = dtm.executeUpdate(sql);
/* 5166:5468 */       System.out.println(count);
/* 5167:5469 */       return count;
/* 5168:     */     }
/* 5169:5472 */     if ((cmd[0].toLowerCase().equals("delete")) || (cmd[0].toLowerCase().equals("truncate")))
/* 5170:     */     {
/* 5171:5474 */       dtm.setupTable(cmd[2], "*", "", "test", false);
/* 5172:5475 */       int count = dtm.executeUpdate(sql);
/* 5173:5476 */       System.out.println(count);
/* 5174:5477 */       return count;
/* 5175:     */     }
/* 5176:5480 */     if ((cmd[0].toLowerCase().equals("select")) || (cmd[0].toLowerCase().equals("show")) || (cmd[0].toLowerCase().equals("desc")) || (cmd[0].toLowerCase().equals("check")) || (cmd[0].toLowerCase().equals("repair")))
/* 5177:     */     {
/* 5178:5482 */       dtm.setSqlStatement(sql, "test", false);
/* 5179:5483 */       dtm.retrieve();
/* 5180:     */       
/* 5181:5485 */       System.out.println(dtm.getNumChildren());
/* 5182:5486 */       if (dtm.getNumChildren() > 0) {
/* 5183:5488 */         for (int i = 0; i < dtm.getNumChildren(); i++)
/* 5184:     */         {
/* 5185:5490 */           buf.append("\r\nRow " + i + "\r\n");
/* 5186:5491 */           System.out.println(dtm.get(i).getNumChildren());
/* 5187:5493 */           for (int j = 0; j < dtm.get(i).getNumChildren(); j++)
/* 5188:     */           {
/* 5189:5495 */             System.out.println(dtm.getAttribName(j) + "=" + dtm.get(i).get(j).get() + ",");
/* 5190:5496 */             buf.append(dtm.getAttribName(j) + "=" + dtm.get(i).get(j).get() + ",");
/* 5191:     */           }
/* 5192:     */         }
/* 5193:     */       }
/* 5194:5501 */       return 0;
/* 5195:     */     }
/* 5196:5504 */     return 0;
/* 5197:     */   }
/* 5198:     */   
/* 5199:     */   public boolean getData(String table, String fields, String where, XModel xm)
/* 5200:     */   {
/* 5201:5509 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 5202:     */     
/* 5203:5511 */     dt.setupTable(table, fields, where, "test", false);
/* 5204:5512 */     dt.retrieve();
/* 5205:5513 */     this.logger.info("No of Rows1:" + dt.getNumChildren());
/* 5206:5514 */     if (dt.getNumChildren() > 0) {
/* 5207:5516 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 5208:     */       {
/* 5209:5518 */         XModel row = (XModel)xm.get(i);
/* 5210:5519 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 5211:     */         {
/* 5212:5521 */           ((XModel)row.get(dt.getAttribName(j))).set(dt.get(i).get(j).get());
/* 5213:5522 */           this.logger.info(dt.getAttribName(j) + "=" + dt.get(i).get(j).get());
/* 5214:     */         }
/* 5215:5525 */         xm.append(row);
/* 5216:     */       }
/* 5217:     */     }
/* 5218:5529 */     return false;
/* 5219:     */   }
/* 5220:     */   
/* 5221:     */   public boolean getPhysicianDetails(String where, XModel xm)
/* 5222:     */   {
/* 5223:5534 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 5224:     */     
/* 5225:5536 */     dt.setupTable("physician", "*", where, "test", false);
/* 5226:5537 */     dt.retrieve();
/* 5227:5538 */     this.logger.info("No of Rows:" + dt.getNumChildren());
/* 5228:5539 */     if (dt.getNumChildren() > 0) {
/* 5229:5541 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 5230:     */       {
/* 5231:5543 */         XModel row = (XModel)xm.get(i);
/* 5232:5544 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 5233:     */         {
/* 5234:5546 */           ((XModel)row.get(dt.getAttribName(j))).set(dt.get(i).get(j).get());
/* 5235:5547 */           this.logger.info(dt.getAttribName(j) + "=" + dt.get(i).get(j).get());
/* 5236:     */         }
/* 5237:     */       }
/* 5238:     */     }
/* 5239:5553 */     return false;
/* 5240:     */   }
/* 5241:     */   
/* 5242:     */   public boolean getAccountDetails(String where, XModel xm)
/* 5243:     */   {
/* 5244:5558 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 5245:     */     
/* 5246:5560 */     dt.setupTable("accounts", "*", where, "test", false);
/* 5247:5561 */     dt.retrieve();
/* 5248:5562 */     this.logger.info("No of Rows:" + dt.getNumChildren());
/* 5249:5563 */     if (dt.getNumChildren() > 0) {
/* 5250:5565 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 5251:     */       {
/* 5252:5567 */         XModel row = (XModel)xm.get(i);
/* 5253:5568 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 5254:     */         {
/* 5255:5570 */           ((XModel)row.get(dt.getAttribName(j))).set(dt.get(i).get(j).get());
/* 5256:5571 */           this.logger.info(dt.getAttribName(j) + "=" + dt.get(i).get(j).get());
/* 5257:     */         }
/* 5258:     */       }
/* 5259:     */     }
/* 5260:5577 */     return false;
/* 5261:     */   }
/* 5262:     */   
/* 5263:     */   public void getAllPhysicians(XModel xm)
/* 5264:     */   {
/* 5265:5582 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 5266:5583 */     dt.setupTable("physician", "name,id", null, "test", false);
/* 5267:5584 */     dt.retrieve();
/* 5268:5585 */     if (dt.getNumChildren() > 0) {
/* 5269:5586 */       for (int i = 0; i < dt.getNumChildren(); i++)
/* 5270:     */       {
/* 5271:5587 */         XModel row = (XModel)xm.get(i);
/* 5272:5588 */         for (int j = 0; j < dt.getNumAttributes(); j++)
/* 5273:     */         {
/* 5274:5589 */           ((XModel)row.get(dt.getAttribName(j))).set(dt.get(i)
/* 5275:5590 */             .get(j).get());
/* 5276:5591 */           this.logger.info(dt.getAttribName(j) + "=" + 
/* 5277:5592 */             dt.get(i).get(j).get());
/* 5278:     */         }
/* 5279:     */       }
/* 5280:     */     }
/* 5281:     */   }
/* 5282:     */   
/* 5283:     */   public boolean removePhysician(int physicianId)
/* 5284:     */   {
/* 5285:5599 */     DatabaseTableModel dt = new DatabaseTableModel();
/* 5286:5600 */     dt.setupTable("physician", "status", "id=" + physicianId, "test", true);
/* 5287:5601 */     dt.retrieve();
/* 5288:5602 */     int i = 0;
/* 5289:5603 */     String sql = "update physician set status='inactive' where id=" + physicianId;
/* 5290:5604 */     this.logger.info("Remove phy::" + sql);
/* 5291:     */     try
/* 5292:     */     {
/* 5293:5606 */       i = dt.executeUpdate(sql);
/* 5294:5607 */       if (i > 0) {
/* 5295:5608 */         return true;
/* 5296:     */       }
/* 5297:     */     }
/* 5298:     */     catch (Exception e)
/* 5299:     */     {
/* 5300:5612 */       e.printStackTrace();
/* 5301:     */     }
/* 5302:5615 */     return false;
/* 5303:     */   }
/* 5304:     */   
/* 5305:     */   public XModel getChildren(String type, String subtype, XModel context, String fields, String constraints)
/* 5306:     */     throws Exception
/* 5307:     */   {
/* 5308:5621 */     if (type.equals("Enumeration")) {
/* 5309:5623 */       return getEnumDataChildren(context, fields, constraints, subtype);
/* 5310:     */     }
/* 5311:5625 */     if (type.equals("va")) {
/* 5312:5627 */       return getEnumDataChildren(context, fields, constraints, subtype);
/* 5313:     */     }
/* 5314:5629 */     return null;
/* 5315:     */   }
/* 5316:     */   
/* 5317:     */   public XModel getData(String type, String subtype, XModel context, String fields)
/* 5318:     */     throws Exception
/* 5319:     */   {
/* 5320:5635 */     if (type.equals("Enumeration")) {
/* 5321:5637 */       return getEnumData(context, subtype, fields);
/* 5322:     */     }
/* 5323:5640 */     if (type.equals("va")) {
/* 5324:5642 */       return getVAData(context, subtype, fields);
/* 5325:     */     }
/* 5326:5645 */     if (type.equals("CME")) {
/* 5327:5647 */       return getCMEData(context, subtype, fields);
/* 5328:     */     }
/* 5329:5650 */     return null;
/* 5330:     */   }
/* 5331:     */   
/* 5332:     */   public String getTranslation(String text, String language)
/* 5333:     */   {
/* 5334:5655 */     this.logger.info(language);
/* 5335:5656 */     if (language.equals("en")) {
/* 5336:5657 */       return text;
/* 5337:     */     }
/* 5338:5658 */     DatabaseTableModel dtm = new DatabaseTableModel();
/* 5339:5659 */     dtm.setupTable("dictionary", "localtext", "text='" + text + "' and lang='" + language + "'", "test", false);
/* 5340:5660 */     dtm.retrieve();
/* 5341:5662 */     if (dtm.getNumChildren() > 0) {
/* 5342:5663 */       return dtm.get(0).get(0).toString();
/* 5343:     */     }
/* 5344:5665 */     return text;
/* 5345:     */   }
/* 5346:     */   
/* 5347:     */   public String getTranslation1(String text, String language)
/* 5348:     */     throws Exception
/* 5349:     */   {
/* 5350:5671 */     if (language.equals("en")) {
/* 5351:5672 */       return text;
/* 5352:     */     }
/* 5353:5673 */     DatabaseTableModel dtm = new DatabaseTableModel();
/* 5354:     */     
/* 5355:5675 */     String where = "localtext='" + text + "'";
/* 5356:     */     try
/* 5357:     */     {
/* 5358:5677 */       this.logger.info(new String(where.getBytes("utf-8"), "utf-8"));
/* 5359:     */     }
/* 5360:     */     catch (UnsupportedEncodingException e)
/* 5361:     */     {
/* 5362:5681 */       e.printStackTrace();
/* 5363:     */     }
/* 5364:5683 */     this.logger.info(where);
/* 5365:5684 */     dtm.setupTable("vadictionary.dictionary where " + where, "*", "", "test", false);
/* 5366:5685 */     PreparedStatement ps = dtm.getTable().getPreparedStatement(" select text from dictionary where localtext=?");
/* 5367:     */     
/* 5368:5687 */     ps.setBytes(1, text.getBytes("utf-8"));
/* 5369:5688 */     ResultSet rs = ps.executeQuery();
/* 5370:5690 */     if (rs.next()) {
/* 5371:5691 */       return rs.getString("text");
/* 5372:     */     }
/* 5373:5693 */     return text;
/* 5374:     */   }
/* 5375:     */   
/* 5376:     */   public void saveData(String type, String subtype, XModel context, XModel dataM)
/* 5377:     */     throws Exception
/* 5378:     */   {
/* 5379:5699 */     if (type.equals("Enumeration")) {
/* 5380:5701 */       saveEnumData(context, subtype, dataM);
/* 5381:5703 */     } else if (type.equals("VA")) {
/* 5382:5704 */       saveVAData(context, subtype, dataM);
/* 5383:5706 */     } else if (type.equals("CME")) {
/* 5384:5707 */       saveCMEData(context, subtype, dataM);
/* 5385:     */     }
/* 5386:     */   }
/* 5387:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.db.TestXUIDB
 * JD-Core Version:    0.7.0.1
 */