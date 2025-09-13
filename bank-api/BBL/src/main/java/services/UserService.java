package services;

import enums.HairColor;
import models.Friend;
import models.User;

import java.util.List;

public interface UserService {

    void createUser(User user);

    User getUser(String login);

    void updateFriendsList(String operation, String friendLogin, String login);

    List<Friend> getFriends(String login);

    List<User> getUsersWithFilter(HairColor hairColor, String sex);
}
