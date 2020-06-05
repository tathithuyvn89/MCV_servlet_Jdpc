package controller;

import model.User;
import service.UserDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private static final long serialversionUID=1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO= new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action= request.getParameter("action");
        if (action==null){
            action="";
        }
        switch (action){
            case "create":
                insertUser(request,response);
                break;
            case "edit":
                updateUser(request,response);
                break;
            default:
                break;
        }

    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("newName");
        String email = request.getParameter("newEmail");
        String country = request.getParameter("newCountry");
        User user = new User(id,name,email,country);
//        userDAO.updateUser(user);
        userDAO.updateUserCallabelStatement(user);
//        RequestDispatcher dispatcher= request.getRequestDispatcher("user/edituser.jsp");
//        dispatcher.forward(request,response);
        response.sendRedirect("./users");

    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String coutry = request.getParameter("country");
        User newUser= new User(name,email,coutry);
//        userDAO.insertUser(newUser);
        userDAO.insertIntoStore(newUser);
        RequestDispatcher dispatcher= request.getRequestDispatcher("user/createuser.jsp");
        request.setAttribute("message","Create new product success!!!");
        dispatcher.forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action= request.getParameter("action");
        if (action==null){
            action="";
        }
        switch (action){
            case "create":
                showCreateForm(request,response);
                break;
            case "edit":
                showEditForm(request,response);
                break;
            case "delete":
                deletUser(request,response);
                break;
            case "oder":
                listOrderByName(request,response);
                break;
            case "permision":
                addUserPermision(request, response);
                break;
            case "test-without-tran":
                testWithoutTran(request, response);
                break;

            default:
                listUser(request,response);
                break;
        }

    }

    private void testWithoutTran(HttpServletRequest request, HttpServletResponse response) {
        userDAO.insertUpdateWithoutTransaction();
    }

    private void addUserPermision(HttpServletRequest request, HttpServletResponse response) {
        User user = new User("kien", "kienhoang@gmail.com", "vn");

        int[] permision = {1, 2, 4};

        userDAO.addUserTransaction(user, permision);

    }

    private void listOrderByName(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        List<User> users= userDAO.sellectAllUser();
        request.setAttribute("listuser",users);
        RequestDispatcher dispatcher= request.getRequestDispatcher("user/listuserorderbyname.jsp");
        dispatcher.forward(request,response);
    }

    private void deletUser(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
         userDAO.deleteUser(id);

         listUser(request,response);

    }

    private void listUser(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
//        List<User> users= userDAO.sellectAllUser();
        List<User> users=userDAO.showAllUserCallableStatement();
        request.setAttribute("listuser",users);
        RequestDispatcher dispatcher= request.getRequestDispatcher("user/listuser.jsp");
        dispatcher.forward(request,response);

    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
//        User userExiting =userDAO.selectUser(id);
        User userExiting= null;
        try {
            userExiting = userDAO.getUserByID(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        RequestDispatcher dispatcher;
       if (userExiting!=null){
           request.setAttribute("userExiting",userExiting);
          dispatcher= request.getRequestDispatcher("user/edituser.jsp");
           dispatcher.forward(request,response);
       } else {
           dispatcher=request.getRequestDispatcher("index.jsp");
       }
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
       RequestDispatcher dispatcher = request.getRequestDispatcher("user/createuser.jsp");
       dispatcher.forward(request,response);

    }
}
