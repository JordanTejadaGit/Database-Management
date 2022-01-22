/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dataaccess.CategoriesDB;
import dataaccess.ItemsDB;
import dataaccess.UsersDB;
import java.util.List;
import domain.Items;
import domain.Users;
import domain.Categories;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;

public class InventoryService {
    public List<Items> getAll(String username) throws Exception {
        ItemsDB itemsdb = new ItemsDB();
        List<Items> items = itemsdb.getAll(username);
        return items;
    }
    
    public List<Categories> getAllCat() throws Exception {
        CategoriesDB categoriesdb = new CategoriesDB();
        List<Categories> categories = categoriesdb.getAll();
        return categories;
    }
    
    
    public Items get(int itemsID) throws Exception {
        ItemsDB itemsdb = new ItemsDB();
        Items items = itemsdb.get(itemsID);
        return items;
    }
    
    public Categories getCategory(int categoryID) throws Exception {
        CategoriesDB categoriesdb = new CategoriesDB();
        Categories categores = categoriesdb.get(categoryID);
        return categores;
    }
    
    public void insert(Integer itemID, String itemName, double price, String username, Integer CategoryID) throws Exception{
        UsersDB userdb = new UsersDB();
        CategoriesDB categoriesdb = new CategoriesDB();
        Categories category = categoriesdb.get(CategoryID);
        Items items = new Items(itemID, itemName, price);
        Users user = userdb.get(username);
        
        items.setCategory(category);
        items.setOwner(user);
        
        user.getItemsList().add(items);
        ItemsDB itemsdb = new ItemsDB();
        itemsdb.insert(items);
    }
    
    public void update(int itemID, String itemName, double price, int CategoryID) throws Exception{
        CategoriesDB categoriesdb = new CategoriesDB();
        Categories category = categoriesdb.get(CategoryID);
        ItemsDB itemsDB = new ItemsDB();
        Items items = itemsDB.get(itemID);
        
        items.setCategory(category);
        items.setItemName(itemName);
        items.setPrice(price);
 
        itemsDB.update(items);
    }
    
    public String delete(int itemsID, String username) throws Exception {
        ItemsDB itemsdb = new ItemsDB();
        Items items = itemsdb.get(itemsID);
        itemsdb.delete(items);
        String success = "success";
        
        String owner = items.getOwner().getUsername();
        if (owner.equals(username)) {
            itemsdb.delete(items);
            return success;
        }
        else {
            return null;
        }
    }
    
    public void insertCategory(String categoryName, int categoryID) throws Exception {
        CategoriesDB categoriesdb = new CategoriesDB(); 
        Categories categories = new Categories(categoryID, categoryName);
        
        categoriesdb.insert(categories);
        
    }
    
    public void updateCategory(String newCategoryName, int categoryID) throws Exception {
        CategoriesDB categoriesdb = new CategoriesDB(); 
        Categories categories = categoriesdb.get(categoryID);
        
        categories.setCategoryName(newCategoryName);
        categoriesdb.update(categories);
    }
    
    public List<Items> getAllItems() throws Exception {
        UsersDB user = new UsersDB();
        List<Users> userList = user.getAll();
        List<Items> itemList = new ArrayList<Items>();
        
        for (int i = 0; i < userList.size(); i++) {
            for (int j = 0; j < userList.get(i).getItemsList().size(); j++) {
                itemList.add(userList.get(i).getItemsList().get(j));
            }
        }
        return itemList;
    }
    
    public List<Items> getSearchItem(String search) throws Exception {

        InventoryService is = new InventoryService();
        List<Items> itemList = is.getAllItems();
        List<Items> searchedItems = new ArrayList<Items>();
        
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getItemName().toLowerCase().startsWith(search.toLowerCase())){
                searchedItems.add(itemList.get(i));
            }
        }
        return searchedItems;
    }
    
     public List<Items> getSearchItemUser(String search, String username) throws Exception {

        InventoryService is = new InventoryService();
        List<Items> itemList = is.getAll(username);
        List<Items> searchedItems = new ArrayList<Items>();
        
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getItemName().toLowerCase().startsWith(search.toLowerCase())){
                searchedItems.add(itemList.get(i));
            }
        }
        return searchedItems;
    }
}
