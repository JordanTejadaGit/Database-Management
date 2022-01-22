/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import domain.Items;
import domain.Users;

public class ItemsDB {
    public List<Items> getAll(String owner) throws Exception {
       EntityManager em = DBUtils.getEmFactory().createEntityManager();
        
        try {
            Users user = em.find(Users.class, owner);
            return user.getItemsList();
        } finally {
            em.close();
        }
    }
    
    public Items get(int itemID) throws Exception {
        EntityManager em = DBUtils.getEmFactory().createEntityManager();
        
        try {
            Items items = em.find(Items.class, itemID);
            return items;
        } finally {
            em.close();
        }
    }
    
    public void insert(Items items) throws Exception {
        EntityManager em = DBUtils.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            Users user = items.getOwner();
            user.getItemsList().add(items);
            trans.begin();
            em.persist(items);
            em.merge(user);
            trans.commit();
            
        } 
        catch (Exception ex) {
            trans.rollback();
        }
        finally {
            em.close();
        }
    }
    
    public void update(Items items) throws Exception {
           EntityManager em = DBUtils.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            em.merge(items);
            trans.commit();
            
        } 
        catch (Exception ex) {
            trans.rollback();
        }
        finally {
            em.close();
        }
    }
    
    
    public void delete(Items items) throws Exception {
           EntityManager em = DBUtils.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            Users user = items.getOwner();
            user.getItemsList().remove(items);
            trans.begin();
            em.remove (em.merge(items));
            em.merge(user);
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
