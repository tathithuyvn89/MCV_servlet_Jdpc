package service;

import model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO{
    private String jdbcURL="jdbc:mysql://localhost:3306/jdbcpractice";
    private String jdbcUser="root";
    private String jdbcPassword="12345";
    private static final String INSERT_USERS_SQL="INSERT INTO users"+"(name,email,country)VALUES"+"(?,?,?);";
    private static final String SELECT_USERS_BY_ID= "select id,name,email,country from users where id=?";
    private static final String SELECT_ALL_USERS="select * from users";
    private static final String DELETE_USERS_SQL ="delete from users where id=?;";
    private static final String UPDATE_USERS_SQL = "update users set name=?,email=?,country=? where id=?;";
    private static final String ORDER_BY_NAME_USERS_SQL= "select * from users order by name";
    private static final String SQL_INSERT = "INSERT INTO EMPLOYEE (NAME, SALARY, CREATED_DATE) VALUES (?,?,?)";

    private static final String SQL_UPDATE = "UPDATE EMPLOYEE SET SALARY=? WHERE NAME=?";

    private static final String SQL_TABLE_CREATE = "CREATE TABLE EMPLOYEE"

            + "("

            + " ID serial,"

            + " NAME varchar(100) NOT NULL,"

            + " SALARY numeric(15, 2) NOT NULL,"

            + " CREATED_DATE timestamp,"

            + " PRIMARY KEY (ID)"

            + ")";

    private static final String SQL_TABLE_DROP = "DROP TABLE IF EXISTS EMPLOYEE";
    private List<User> users =new ArrayList<>();

    public UserDAO() {
    }
    private Connection getConnection(){
        Connection connection=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection= DriverManager.getConnection(jdbcURL,jdbcUser,jdbcPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;

    }
    @Override
    public void insertUser(User user) {
        System.out.println(INSERT_USERS_SQL);
        Connection connection=getConnection();
        try {
            PreparedStatement preparedStatement =connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,user.getCountry());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public User selectUser(int id) {
        User user= null;
        Connection connection= getConnection();
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(SELECT_USERS_BY_ID);
            preparedStatement.setInt(1,id);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                user = new User(id,name,email,country);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User>sellectAllUser() {
        users.clear();

        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(ORDER_BY_NAME_USERS_SQL);
            System.out.println(preparedStatement);
            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                users.add(new User(id,name,email,country));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) {
       boolean rowDeleted=false;
       Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(DELETE_USERS_SQL);
            preparedStatement.setInt(1,id);
            rowDeleted = preparedStatement.executeUpdate()>0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowDeleted;
    }

    @Override
    public boolean updateUser(User user) {
        boolean rowUpdate=false;
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(UPDATE_USERS_SQL);
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getEmail());
            preparedStatement.setString(3,user.getCountry());
            preparedStatement.setInt(4,user.getId());
            rowUpdate = preparedStatement.executeUpdate()>0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowUpdate;
    }

    @Override
    public List<User> seachByCountry(String country) {
        return null;
    }

    @Override
    public User getUserByID(int id) throws SQLException {
        User user= null;
        String query= "{call get_user_by_id(?)}";
        Connection connection= getConnection();
        CallableStatement callableStatement= connection.prepareCall(query);
        callableStatement.setInt(1,id);
        ResultSet resultSet = callableStatement.executeQuery();
        while (resultSet.next()){
            String name= resultSet.getString("name");
            String email= resultSet.getString("email");
            String country= resultSet.getString("country");
            user= new User(id, name,email,country);
        }
        return user;
    }

    @Override
    public void insertIntoStore(User user) {
        String query= "{call insert_user(?,?,?)}";
        Connection connection= getConnection();
        try {
            CallableStatement callableStatement= connection.prepareCall(query);
            callableStatement.setString(1,user.getName());
            callableStatement.setString(2,user.getEmail());
            callableStatement.setString(3,user.getCountry());
            callableStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void addUserTransaction(User user, int[] permisions) {
        Connection conn = null;

        // for insert a new user

        PreparedStatement pstmt = null;

        // for assign permision to user

        PreparedStatement pstmtAssignment = null;

        // for getting user id

        ResultSet rs = null;

        try {

            conn = getConnection();

            // set auto commit to false

            conn.setAutoCommit(false);

            //

            // Insert user

            //

            pstmt = conn.prepareStatement(INSERT_USERS_SQL, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, user.getName());

            pstmt.setString(2, user.getEmail());

            pstmt.setString(3, user.getCountry());

            int rowAffected = pstmt.executeUpdate();

            // get user id

            rs = pstmt.getGeneratedKeys();

            int userId = 0;

            if (rs.next())

                userId = rs.getInt(1);

            //

            // in case the insert operation successes, assign permision to user

            //

            if (rowAffected == 1) {

                // assign permision to user

                String sqlPivot = "INSERT INTO user_permision(user_id,permision_id) "

                        + "VALUES(?,?)";

                pstmtAssignment = conn.prepareStatement(sqlPivot);

                for (int permisionId : permisions) {

                    pstmtAssignment.setInt(1, userId);

                    pstmtAssignment.setInt(2, permisionId);

                    pstmtAssignment.executeUpdate();

                }

                conn.commit();

            } else {

                conn.rollback();

            }

        } catch (SQLException ex) {

            // roll back the transaction

            try {

                if (conn != null)

                    conn.rollback();

            } catch (SQLException e) {

                System.out.println(e.getMessage());

            }

            System.out.println(ex.getMessage());

        } finally {

            try {

                if (rs != null) rs.close();

                if (pstmt != null) pstmt.close();

                if (pstmtAssignment != null) pstmtAssignment.close();

                if (conn != null) conn.close();

            } catch (SQLException e) {

                System.out.println(e.getMessage());

            }

        }
    }

    @Override
    public void insertUpdateWithoutTransaction() {

        try (Connection conn = getConnection();

             Statement statement = conn.createStatement();

             PreparedStatement psInsert = conn.prepareStatement(SQL_INSERT);

             PreparedStatement psUpdate = conn.prepareStatement(SQL_UPDATE)) {



            statement.execute(SQL_TABLE_DROP);

            statement.execute(SQL_TABLE_CREATE);



            // Run list of insert commands

            psInsert.setString(1, "Quynh");

            psInsert.setBigDecimal(2, new BigDecimal(10));

            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            psInsert.execute();



            psInsert.setString(1, "Ngan");

            psInsert.setBigDecimal(2, new BigDecimal(20));

            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            psInsert.execute();



            // Run list of update commands



            // below line caused error, test transaction

            // org.postgresql.util.PSQLException: No value specified for parameter 1.

            psUpdate.setBigDecimal(2, new BigDecimal(999.99));



            //psUpdate.setBigDecimal(1, new BigDecimal(999.99));

            psUpdate.setString(2, "Quynh");

            psUpdate.execute();



        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @Override
    public List<User> showAllUserCallableStatement() {
        users.clear();
        Connection connection=getConnection();
        String query = "{call show_all_user()}";
        try {
            CallableStatement callableStatement=connection.prepareCall(query);
            ResultSet resultSet= callableStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                users.add(new User(id,name,email,country));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;

    }

    @Override
    public boolean updateUserCallabelStatement(User user) {
        boolean rowUpdate=false;
        Connection connection=getConnection();

        String query = "{call edit_use_by_id(?,?,?,?)}";
        try {

            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1,user.getId());
            callableStatement.setString(2,user.getName());
            callableStatement.setString(3,user.getEmail());
            callableStatement.setString(4,user.getCountry());
            rowUpdate = callableStatement.executeUpdate()>0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowUpdate;
    }


    @Override
    public List<User> sellectAllUserByName() {

        users=null;
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(ORDER_BY_NAME_USERS_SQL);
            System.out.println(preparedStatement);
            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                users.add(new User(id,name,email,country));

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    private void printSQLException(SQLException e){
        for (Throwable ex: e ){
            if (ex instanceof SQLException){
                ex.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }

        }

    }

}

