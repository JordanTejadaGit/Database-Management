/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import domain.Users;

public class UsersDB {
    public List<Users> getAll() throws Exception {
        EntityManager em = DBUtils.getEmFactory().createEntityManager();
        
        try {
            List<Users> users = em.createNamedQuery("Users.findAll", Users.class).getResultList();
            return users;
        } finally {
            em.close();
        }
    }
    
    public Users get(String username) throws Exception {
        EntityManager em = DBUtils.getEmFactory().createEntityManager();
        
        try {
            Users user = em.find(Users.class, username);
            return user;
        } finally {
            em.close();
        }
    }
    
    public List<Users> getEmail(String email) throws Exception {
        EntityManager em = DBUtils.getEmFactory().createEntityManager();
        
        try {
            List<Users> user = em.createNamedQuery("Users.findByEmail").setParameter("email", email).getResultList();
            return user;
        } finally {
            em.close();
        }
    }

    public void insert(Users user) throws Exception {
        EntityManager em = DBUtils.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
           
            trans.begin();
            em.persist(user);
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
    
    public void update(Users user) throws Exception {
           EntityManager em = DBUtils.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
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
    
    public void delete(Users userdelete) throws Exception {
           EntityManager em = DBUtils.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        
        try {
            Users user = em.find(Users.class, userdelete.getUsername());
            trans.begin();
            em.remove (em.merge(user));
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
