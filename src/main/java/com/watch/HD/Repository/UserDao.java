package com.watch.HD.Repository;

import com.watch.HD.Response.UserResponse;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class UserDao {
    private static final String URL = "jdbc:postgresql://localhost:5432/HD";
    private static final String USER = "user";
    private static final String PASSWORD = "postgres";

    public UserResponse getUserResponseBySession(String session) {
        UserResponse user = new UserResponse();
        try (Connection connection = DriverManager.getConnection(URL,USER,PASSWORD)){
            try (PreparedStatement query = connection.prepareStatement("SELECT user_name,banner_url,picture_url,subs FROM _user WHERE id = (SELECT user_id FROM sessions WHERE session = '" + session+  "')")){
                ResultSet result = query.executeQuery();
                while (result.next()) {
                    user.setUserName(result.getString("user_name"));
                    user.setPictureUrl(result.getString("picture_url"));
                    user.setBannerUrl(result.getString("banner_url"));
                    user.setSubs(result.getInt("subs"));
                }
                } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }
    public UserResponse getUserResponseById(String userId) {
        UserResponse user = new UserResponse();
        try (Connection connection = DriverManager.getConnection(URL,USER,PASSWORD)){
            try (PreparedStatement query = connection.prepareStatement("SELECT user_name,banner_url,picture_url,subs FROM _user WHERE id = '" + userId+  "'")){
                ResultSet result = query.executeQuery();
                while (result.next()){
                    user.setUserName(result.getString("user_name"));
                    user.setPictureUrl(result.getString("picture_url"));
                    user.setBannerUrl(result.getString("banner_url"));
                    user.setSubs(result.getInt("subs"));
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }
}
