/*  1:   */ package org.springframework.web.servlet.view.feed;
/*  2:   */ 
/*  3:   */ import com.sun.syndication.feed.rss.Channel;
/*  4:   */ import com.sun.syndication.feed.rss.Item;
/*  5:   */ import java.util.List;
/*  6:   */ import java.util.Map;
/*  7:   */ import javax.servlet.http.HttpServletRequest;
/*  8:   */ import javax.servlet.http.HttpServletResponse;
/*  9:   */ 
/* 10:   */ public abstract class AbstractRssFeedView
/* 11:   */   extends AbstractFeedView<Channel>
/* 12:   */ {
/* 13:   */   public AbstractRssFeedView()
/* 14:   */   {
/* 15:49 */     setContentType("application/rss+xml");
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected Channel newFeed()
/* 19:   */   {
/* 20:58 */     return new Channel("rss_2.0");
/* 21:   */   }
/* 22:   */   
/* 23:   */   protected final void buildFeedEntries(Map<String, Object> model, Channel channel, HttpServletRequest request, HttpServletResponse response)
/* 24:   */     throws Exception
/* 25:   */   {
/* 26:69 */     List<Item> items = buildFeedItems(model, request, response);
/* 27:70 */     channel.setItems(items);
/* 28:   */   }
/* 29:   */   
/* 30:   */   protected abstract List<Item> buildFeedItems(Map<String, Object> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/* 31:   */     throws Exception;
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.feed.AbstractRssFeedView
 * JD-Core Version:    0.7.0.1
 */