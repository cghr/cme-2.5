/*  1:   */ package org.springframework.web.servlet.view.feed;
/*  2:   */ 
/*  3:   */ import com.sun.syndication.feed.atom.Entry;
/*  4:   */ import com.sun.syndication.feed.atom.Feed;
/*  5:   */ import java.util.List;
/*  6:   */ import java.util.Map;
/*  7:   */ import javax.servlet.http.HttpServletRequest;
/*  8:   */ import javax.servlet.http.HttpServletResponse;
/*  9:   */ 
/* 10:   */ public abstract class AbstractAtomFeedView
/* 11:   */   extends AbstractFeedView<Feed>
/* 12:   */ {
/* 13:   */   public static final String DEFAULT_FEED_TYPE = "atom_1.0";
/* 14:51 */   private String feedType = "atom_1.0";
/* 15:   */   
/* 16:   */   public AbstractAtomFeedView()
/* 17:   */   {
/* 18:55 */     setContentType("application/atom+xml");
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setFeedType(String feedType)
/* 22:   */   {
/* 23:65 */     this.feedType = feedType;
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected Feed newFeed()
/* 27:   */   {
/* 28:75 */     return new Feed(this.feedType);
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected final void buildFeedEntries(Map<String, Object> model, Feed feed, HttpServletRequest request, HttpServletResponse response)
/* 32:   */     throws Exception
/* 33:   */   {
/* 34:86 */     List<Entry> entries = buildFeedEntries(model, request, response);
/* 35:87 */     feed.setEntries(entries);
/* 36:   */   }
/* 37:   */   
/* 38:   */   protected abstract List<Entry> buildFeedEntries(Map<String, Object> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/* 39:   */     throws Exception;
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.feed.AbstractAtomFeedView
 * JD-Core Version:    0.7.0.1
 */