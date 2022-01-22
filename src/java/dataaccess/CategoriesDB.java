/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import java.util.List;
import javax.persistence.EntityManager;
import domain.Categories;
import javax.persistence.EntityTransaction;

public class CategoriesDB {
    public List<Categories> getAll() throws Exception {
       EntityManager em = DBUtils.getEmFactory().createEntityManager();
        
        try {
            List<Categories> categories = em.createNamedQuery("Categories.findAll", Categories.class).getResultList();
            return categories;
        } finally {
            em.close();
        }
    }
    
    public Categories get(int categoryID) throws Exception {
        EntityManager em = DBUtils.getEmFactory().createEntityManager();
        
        try {
            Categories categories = em.find(Categories.class, categoryID);
            return categories;
        } finally {
            em.close();
        }
    }
    
    public void insert(Categories categories) throws Exception {
        EntityManager em = DBUtils.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            em.persist(categories);
            em.merge(categories);
            trans.commit();
        }
        catch (Exception ex) {
            trans.rollback();
        }
        finally {
            em.close();
        }
    }
    
    public void update(Categories categories) throws Exception {
           EntityManager em = DBUtils.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            em.merge(categories);
            trans.commit();
            
        } 
        catch (Exception ex) {
            trans.rollback();
        }
        finally {
            em.close();
        }
    }
}
