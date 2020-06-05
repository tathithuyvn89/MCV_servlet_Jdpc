package service;

import model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    void insertUser(User user);
    User selectUser(int id);
    List<User> sellectAllUser();
    boolean deleteUser(int id);
    boolean updateUser(User user);
    List<User> sellectAllUserByName();
    List<User> seachByCountry(String country);
    User getUserByID( int id) throws SQLException;
    void insertIntoStore(User user);
    void addUserTransaction( User user, int[] permision);
    void insertUpdateWithoutTransaction();
    List<User>showAllUserCallableStatement();
    boolean updateUserCallabelStatement(User user);

}