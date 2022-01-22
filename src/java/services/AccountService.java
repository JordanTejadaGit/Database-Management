/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dataaccess.UsersDB;
import java.util.List;
import domain.Users;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountService {
    public List<Users> getAll() throws Exception {
        UsersDB userDB = new UsersDB();
        List<Users> users = userDB.getAll();
        return users;
    }
    
    public Users get(String username) throws Exception {
        UsersDB userDB = new UsersDB();
        Users user = userDB.get(username);
        return user;
    }
    public List<Users> getEmail(String email) throws Exception {
        UsersDB userDB = new UsersDB();
        List<Users> user = userDB.getEmail(email);
        return user;
    }
    
    public void insert(String username, String password, String email, String firstName, String lastName, boolean active, boolean isAdmin) throws Exception{
        Users user = new Users(username, password, email, firstName, lastName, active, isAdmin);
        UsersDB userDB = new UsersDB();
        userDB.insert(user);
    }
    
    public void update(String username, String password, String email, String firstName, String lastName, boolean active, boolean isAdmin, String oldUsername) throws Exception{
        Users newUser = new Users();
        Users oldUser = new Users();
        UsersDB userDB = new UsersDB();
        
        oldUser = userDB.get(oldUsername);
        newUser = userDB.get(oldUsername);
        
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setActive(active);
        newUser.setIsAdmin(isAdmin);
        
        userDB.delete(oldUser);
        userDB.update(newUser);
    }
    
    public void delete(String username, String loginUsername) throws Exception {
        UsersDB userDB = new UsersDB();
        Users user = userDB.get(username);
        userDB.delete(user);
    }
    
    public Users login(String username, String password) throws Exception{
        UsersDB userDB = new UsersDB();
        Users user = userDB.get(username);
        if (user.getPassword().equals(password)) {
            return user;
        }
        else {
        return null;
        }
    }
    
    public void confirmEmail(String username, String email, String url, String path) throws Exception {
        UsersDB userDB = new UsersDB();
        Users newUser = userDB.get(username);
        Users oldUser = userDB.get(username);
        String uuid = UUID.randomUUID().toString();
        String link = url + "?uuid=" + uuid;
        newUser.setEmailConfirmationUUID(uuid);
        userDB.delete(oldUser);
        userDB.update(newUser);

       String to = newUser.getEmail();
                String subject = "Email Confirmation";
                String template = path + "/emailtemplates/emailConfirmation.html";
                
                HashMap<String, String> tags = new HashMap<>();
                tags.put("firstname", newUser.getFirstName());
                tags.put("lastname", newUser.getLastName());
                tags.put("link", link);
                
                GmailService.sendMail(to, subject, template, tags);    
       
    }
    
    public void welcomeEmail(String username, String url, String path) throws Exception {
        UsersDB userDB = new UsersDB();
        Users newUser = userDB.get(username);
        Users oldUser = userDB.get(username);
        newUser.setEmailConfirmationUUID(null);
        newUser.setActive(true);
        userDB.delete(oldUser);
        userDB.update(newUser);


       String to = newUser.getEmail();
                String subject = "Welcome Email";
                String template = path + "/emailtemplates/welcomeEmail.html";
                
                HashMap<String, String> tags = new HashMap<>();
                tags.put("firstname", newUser.getFirstName());
                tags.put("lastname", newUser.getLastName());
                
                GmailService.sendMail(to, subject, template, tags);

                
    }
    public void passwordEmail(String username, String path, String url) throws Exception {
        UsersDB userDB = new UsersDB();
        Users newUser = userDB.get(username);
        String uuid = UUID.randomUUID().toString();
        String link = url + "?uuidPassword=" + uuid;
        newUser.setResetPasswordUUID(uuid);
        userDB.update(newUser);
        
        String to = newUser.getEmail();
                String subject = "Reset Password";
                String template = path + "/emailtemplates/resetpassword.html";
                
                HashMap<String, String> tags = new HashMap<>();
                tags.put("firstname", newUser.getFirstName());
                tags.put("lastname", newUser.getLastName());
                tags.put("username", newUser.getUsername());
                tags.put("link", link);
                
                GmailService.sendMail(to, subject, template, tags);
    }
    
    public Users uuidPassword(String uuid) throws Exception{
        UsersDB userDB = new UsersDB();
        List<Users> userList =  userDB.getAll();

        
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getResetPasswordUUID() != null) {
                if (userList.get(i).getResetPasswordUUID().equals(uuid)) {
                    return userList.get(i);
                }
            }    
        }
        return null;
    }
    
    public void resetPassword(String username, String password) throws Exception{
        UsersDB userDB = new UsersDB();
        Users user = userDB.get(username);
        user.setPassword(password);
        user.setResetPasswordUUID(null);
        userDB.update(user);
        
    }
    
}
