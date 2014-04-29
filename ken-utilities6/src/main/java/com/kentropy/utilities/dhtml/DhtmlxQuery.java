/*   1:    */ package com.kentropy.utilities.dhtml;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.text.MessageFormat;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import net.xoetrope.xui.data.XBaseModel;
/*   9:    */ import net.xoetrope.xui.data.XModel;
/*  10:    */ import org.apache.commons.lang3.StringUtils;
/*  11:    */ 
/*  12:    */ public class DhtmlxQuery
/*  13:    */ {
/*  14: 15 */   public StringBuffer output = new StringBuffer();
/*  15: 16 */   public StringBuffer headers1 = new StringBuffer();
/*  16: 17 */   String table = "";
/*  17: 18 */   String fields = "";
/*  18: 19 */   String where = "";
/*  19:    */   String[] h1;
/*  20:    */   String[] d1;
/*  21:    */   String[] col;
/*  22: 23 */   ArrayList<String> headers = new ArrayList();
/*  23: 24 */   ArrayList<Integer> datatypes = new ArrayList();
/*  24: 25 */   StringBuffer colTypes = new StringBuffer();
/*  25: 26 */   StringBuffer ids = new StringBuffer();
/*  26: 27 */   Object[] params = null;
/*  27:    */   String[] additional_header;
/*  28: 30 */   ArrayList<String> add_headers = new ArrayList();
/*  29: 31 */   ArrayList<String> col_widths = new ArrayList();
/*  30:    */   private Object reportHeader;
/*  31:    */   private Object reportFooter;
/*  32:    */   
/*  33:    */   public void getReportDef(int reportId, XModel xm)
/*  34:    */   {
/*  35: 37 */     TestXUIDB.getInstance().getData("excelreports", "*", "id=" + reportId, xm);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public boolean checkIfIdField(String fld, String ids)
/*  39:    */   {
/*  40: 42 */     String[] ids1 = ids.split(",");
/*  41: 43 */     for (int i = 0; i < ids1.length; i++) {
/*  42: 45 */       if (fld.equals(ids1[i])) {
/*  43: 46 */         return true;
/*  44:    */       }
/*  45:    */     }
/*  46: 48 */     return false;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void getReportDef1(int reportId)
/*  50:    */   {
/*  51: 53 */     XModel reportDef = new XBaseModel();
/*  52: 54 */     getReportDef(reportId, reportDef);
/*  53: 55 */     this.table = reportDef.get(0).get("table/@value").toString();
/*  54: 56 */     this.fields = reportDef.get(0).get("fields/@value").toString();
/*  55: 57 */     this.where = reportDef.get(0).get("where/@value").toString();
/*  56: 58 */     this.colTypes.append(reportDef.get(0).get("col_types/@value").toString());
/*  57: 59 */     this.ids.append(reportDef.get(0).get("id_fields/@value").toString());
/*  58: 60 */     this.h1 = reportDef.get(0).get("headers/@value").toString().split(",");
/*  59: 61 */     this.d1 = reportDef.get(0).get("datatypes/@value").toString().split(",");
/*  60:    */     
/*  61:    */ 
/*  62: 64 */     this.reportHeader = ((String)reportDef.get(0).get("report_header/@value"));
/*  63: 65 */     this.reportFooter = ((String)reportDef.get(0).get("report_footer/@value"));
/*  64: 66 */     for (int i = 0; i < this.h1.length; i++)
/*  65:    */     {
/*  66: 69 */       this.headers.add(this.h1[i]);
/*  67: 70 */       this.datatypes.add(Integer.valueOf(Integer.parseInt(this.d1[i])));
/*  68:    */     }
/*  69: 73 */     createHeaders(StringUtils.join(this.headers.toArray(), ","), StringUtils.join(this.datatypes.toArray(), ","), this.colTypes.toString());
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void getReportData(XModel xm)
/*  73:    */   {
/*  74: 95 */     MessageFormat form = new MessageFormat(this.where);
/*  75: 96 */     String where1 = form.format(this.params);
/*  76:    */     
/*  77: 98 */     System.out.println("excel Query: select " + this.fields + " from " + this.table + " where " + where1);
/*  78: 99 */     TestXUIDB.getInstance().getData(this.table, this.fields, where1, xm);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void genHeaders(int reportId, Object[] params)
/*  82:    */   {
/*  83:105 */     XModel xm = new XBaseModel();
/*  84:106 */     ArrayList headers = new ArrayList();
/*  85:    */     
/*  86:108 */     ArrayList datatypes = new ArrayList();
/*  87:109 */     StringBuffer colTypes = new StringBuffer();
/*  88:110 */     StringBuffer ids = new StringBuffer();
/*  89:111 */     getReportDef1(reportId);
/*  90:    */     
/*  91:    */ 
/*  92:    */ 
/*  93:    */ 
/*  94:116 */     createHeaders(StringUtils.join(headers.toArray(), ","), StringUtils.join(datatypes.toArray(), ","), colTypes.toString());
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void generateReport(int reportId, Object[] params1)
/*  98:    */     throws IOException
/*  99:    */   {
/* 100:123 */     this.params = params1;
/* 101:124 */     XModel xm = new XBaseModel();
/* 102:125 */     this.headers = new ArrayList();
/* 103:    */     
/* 104:127 */     this.datatypes = new ArrayList();
/* 105:128 */     this.colTypes = new StringBuffer();
/* 106:129 */     this.ids = new StringBuffer();
/* 107:    */     
/* 108:    */ 
/* 109:    */ 
/* 110:133 */     getReportDef1(reportId);
/* 111:    */     
/* 112:    */ 
/* 113:136 */     getReportData(xm);
/* 114:    */     
/* 115:138 */     this.output.append("\r\n<rows>\r\n");
/* 116:139 */     for (int i = 0; i < xm.getNumChildren(); i++)
/* 117:    */     {
/* 118:141 */       ArrayList data = new ArrayList();
/* 119:142 */       String idVal = "";
/* 120:143 */       for (int j = 0; j < xm.get(i).getNumChildren(); j++)
/* 121:    */       {
/* 122:145 */         String fld = xm.get(i).get(j).getId();
/* 123:146 */         if (checkIfIdField(fld, this.ids.toString())) {
/* 124:147 */           idVal = idVal + xm.get(i).get(j).get();
/* 125:    */         }
/* 126:148 */         data.add(xm.get(i).get(j).get());
/* 127:    */       }
/* 128:150 */       writeRow(data, idVal);
/* 129:    */     }
/* 130:152 */     this.output.append("</rows>");
/* 131:153 */     save();
/* 132:    */   }
/* 133:    */   
/* 134:    */   private void save() {}
/* 135:    */   
/* 136:    */   public void createHeaders(String headers, String datatypes, String colTypes)
/* 137:    */   {
/* 138:162 */     this.headers1.append("<script>var headers='" + headers + "';");
/* 139:163 */     this.headers1.append("var sorting='" + datatypes.replaceAll("[1-3]", "str") + "';");
/* 140:164 */     this.headers1.append("var attachHeaders='" + datatypes.replaceAll("[1-3]", "#text_filter") + "';");
/* 141:165 */     this.headers1.append(" var reportHeader=" + (this.reportHeader == null ? "null" : new StringBuilder("'").append(this.reportHeader).append("'").toString()) + ";");
/* 142:166 */     this.headers1.append(" var reportFooter=" + (this.reportFooter == null ? "null" : new StringBuilder("'").append(this.reportFooter).append("'").toString()) + ";");
/* 143:167 */     this.headers1.append("var colTypes='" + colTypes + "';");
/* 144:168 */     this.headers1.append("var initWidths='" + datatypes.replaceAll("[1-3]", "90 ") + "';");
/* 145:169 */     this.headers1.append("</script>");
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void writeRow(ArrayList<String> data, String id)
/* 149:    */   {
/* 150:173 */     this.output.append("<row id='" + id + "'>\r\n");
/* 151:174 */     for (String cell : data)
/* 152:    */     {
/* 153:176 */       String data1 = cell != null ? cell.replaceAll("\\n", " ").replaceAll("\\r", " ") : "";
/* 154:177 */       this.output.append("<cell><![CDATA[" + data1 + "]]></cell>\r\n");
/* 155:    */     }
/* 156:179 */     this.output.append("</row>\r\n");
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void writeRow1(ArrayList<String> data, String id)
/* 160:    */   {
/* 161:185 */     this.output.append("+\"<row id='" + id + "'>\"\r\n");
/* 162:186 */     for (String cell : data)
/* 163:    */     {
/* 164:188 */       String data1 = cell != null ? cell.replaceAll("\\n", " ").replaceAll("\\r", " ") : "";
/* 165:189 */       this.output.append("+\"<cell>" + data1 + "</cell>\"\r\n");
/* 166:    */     }
/* 167:191 */     this.output.append("+\"</row>\"\r\n");
/* 168:    */   }
/* 169:    */   
/* 170:    */   public static void main(String[] args)
/* 171:    */     throws IOException
/* 172:    */   {
/* 173:202 */     Object[] params = { "2013-03" };
/* 174:203 */     DhtmlxQuery teq = new DhtmlxQuery();
/* 175:    */   }
/* 176:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.utilities.dhtml.DhtmlxQuery
 * JD-Core Version:    0.7.0.1
 */