package com.intuit.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.intuit.model.URL;
import com.intuit.util.HibernateUtil;

public class URLDao {
	public static URLDao INSTANCE = new URLDao();
	
	private URLDao() {}
	
	public void addURL(String domain) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
 
        URL url = (URL) session.createQuery(
                "FROM URL WHERE domain = '" + domain + "'").uniqueResult();
        
        if(url != null) {
        	url.setCount(url.getCount() + 1);
        } else {
        	url = new URL();
    		url.setDomain(domain);
    		url.setCount(1);
    		session.save(url);
        }
 
        session.getTransaction().commit();
        session.close();
	}
	
	public Map<String, String> getStats() {
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
 
        @SuppressWarnings("unchecked")
        List<URL> urls = (List<URL>) session.createQuery(
                "FROM URL user ORDER BY user.count DESC").setMaxResults(5).list();
 
        session.getTransaction().commit();
        session.close();
		
		Map<String, String> urlMap = new HashMap<>();
		
		int domainCount = 0;
		for(URL url : urls) {
			urlMap.put("domain" + domainCount, url.getDomain());
			urlMap.put("domainCount" + domainCount++, String.valueOf(url.getCount()));
		}
		
		return urlMap;
	}
}
