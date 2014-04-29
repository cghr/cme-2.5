/*   1:    */ package com.kentropy.graphs;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.SpringApplicationContext;
/*   4:    */ import com.kentropy.util.SpringUtils;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.util.Hashtable;
/*   7:    */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*   8:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*   9:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  10:    */ 
/*  11:    */ public class Graph
/*  12:    */ {
/*  13: 16 */   public static Hashtable<String, Graph> graphs = new Hashtable();
/*  14:    */   
/*  15:    */   public class GraphNode
/*  16:    */   {
/*  17: 20 */     Hashtable<String, GraphNode> rel = null;
/*  18:    */     Graph graph;
/*  19:    */     
/*  20:    */     public GraphNode() {}
/*  21:    */     
/*  22:    */     public Hashtable<String, String> getRelationships()
/*  23:    */     {
/*  24: 25 */       return this.graph.getRelationships(this);
/*  25:    */     }
/*  26:    */     
/*  27: 31 */     String id = "";
/*  28:    */     
/*  29:    */     public String getId()
/*  30:    */     {
/*  31: 34 */       return this.id;
/*  32:    */     }
/*  33:    */     
/*  34:    */     public void setId(String id2)
/*  35:    */     {
/*  36: 38 */       this.id = id2;
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40: 43 */   public static int graphId = 0;
/*  41:    */   private String id;
/*  42:    */   private String type;
/*  43:    */   
/*  44:    */   public static Graph createGraph(String type)
/*  45:    */   {
/*  46: 46 */     Graph gr = new Graph();
/*  47: 47 */     gr.setType(type);
/*  48: 48 */     gr.setId(graphId);
/*  49: 49 */     graphId += 1;
/*  50: 50 */     return gr;
/*  51:    */   }
/*  52:    */   
/*  53:    */   private void setType(String type2)
/*  54:    */   {
/*  55: 58 */     this.type = type2;
/*  56:    */   }
/*  57:    */   
/*  58:    */   private String getType()
/*  59:    */   {
/*  60: 62 */     return this.type;
/*  61:    */   }
/*  62:    */   
/*  63:    */   private void setId(String id)
/*  64:    */   {
/*  65: 67 */     this.id = id;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public String getId()
/*  69:    */   {
/*  70: 71 */     return this.id;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static Graph getGraph(String type, String id)
/*  74:    */   {
/*  75: 76 */     Graph gr = (Graph)graphs.get(id);
/*  76: 77 */     if (gr == null) {
/*  77: 78 */       return createGraph(type);
/*  78:    */     }
/*  79: 80 */     return gr;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void save() {}
/*  83:    */   
/*  84:    */   public JdbcTemplate getTemp()
/*  85:    */   {
/*  86: 92 */     new SpringApplicationContext().setApplicationContext(new ClassPathXmlApplicationContext("appContext.xml"));
/*  87:    */     
/*  88: 94 */     return new SpringUtils().getJdbcTemplate();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public Hashtable<String, String> getRelationships(GraphNode gn)
/*  92:    */   {
/*  93:101 */     Hashtable<String, String> ht = new Hashtable();
/*  94:    */     
/*  95:103 */     JdbcTemplate jt = getTemp();
/*  96:    */     
/*  97:105 */     String sql = "select * from graphs where entity1='" + gn.getId() + "' and type='" + getType() + "'";
/*  98:106 */     System.out.println(">> GR " + sql);
/*  99:107 */     SqlRowSet rs = jt.queryForRowSet(sql);
/* 100:108 */     while (rs.next())
/* 101:    */     {
/* 102:110 */       System.out.println(rs.getString("relationship") + " " + rs.getString("entity2"));
/* 103:111 */       ht.put(rs.getString("relationship"), rs.getString("entity2"));
/* 104:    */     }
/* 105:114 */     return ht;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public GraphNode getNode(String id1)
/* 109:    */   {
/* 110:121 */     JdbcTemplate jt = getTemp();
/* 111:122 */     System.out.println("select * from graphs where entity1='" + id1 + "' and type='" + getType() + "'");
/* 112:123 */     SqlRowSet rs = jt.queryForRowSet("select * from graphs where entity1='" + id1 + "' and type='" + getType() + "'");
/* 113:125 */     if (rs.next())
/* 114:    */     {
/* 115:127 */       System.out.println("found");
/* 116:128 */       GraphNode gn = new GraphNode();
/* 117:129 */       gn.setId(id1);
/* 118:    */       
/* 119:131 */       gn.graph = this;
/* 120:132 */       return gn;
/* 121:    */     }
/* 122:136 */     return null;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public static void main(String[] args)
/* 126:    */   {
/* 127:145 */     Graph gr = createGraph("Form");
/* 128:146 */     System.out.println(gr.getId() + " " + gr.getType());
/* 129:147 */     GraphNode formgn = gr.getNode("Leave");
/* 130:148 */     System.out.println(">>>" + formgn.getId() + " " + formgn.graph.getType());
/* 131:149 */     String wfstr = (String)formgn.getRelationships().get("WF");
/* 132:150 */     System.out.println(">>>" + wfstr);
/* 133:151 */     Graph wf = createGraph(wfstr);
/* 134:152 */     System.out.println(wf.getId() + " " + wf.getNode("WF.Start").getRelationships());
/* 135:    */   }
/* 136:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.graphs.Graph
 * JD-Core Version:    0.7.0.1
 */