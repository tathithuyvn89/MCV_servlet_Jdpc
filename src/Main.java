import model.User;
import service.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<User> users= new ArrayList<>();
        UserDAO userDAO= new UserDAO();
        users=userDAO.sellectAllUser();
        for (int i=0; i<users.size();i++){
            System.out.println(users.get(i).getCountry());
        }

    }
}
