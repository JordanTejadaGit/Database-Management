/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import domain.Users;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author 694952
 */
public class AdminFilter implements Filter {
    

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpSession session = httpRequest.getSession();
        
        Users loginUser = new Users();
        loginUser = (Users) session.getAttribute("loginUser");
        
        if (loginUser == null) {
            HttpServletResponse httpResponse =  (HttpServletResponse)response;
            httpResponse.sendRedirect("login");
            return;
        }
        
        else if (loginUser.getIsAdmin() == false) {
            HttpServletResponse httpResponse =  (HttpServletResponse)response;
            httpResponse.sendRedirect("inventory");
            return;
        }
        
        chain.doFilter(request, response);
        
    }

    @Override
    public void destroy() {
        
    }
    
}
