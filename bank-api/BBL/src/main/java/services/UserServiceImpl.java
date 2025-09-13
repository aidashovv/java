package services;

import enums.HairColor;
import models.Friend;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.FriendRepository;
import repositories.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Autowired
    public UserServiceImpl(UserRepository inputUserRepositoryImpl,
                           FriendRepository inputFriendRepositoryImpl) {
        userRepository = inputUserRepositoryImpl;
        friendRepository = inputFriendRepositoryImpl;
    }

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUser(String login) {
        Optional<User> foundedUser = userRepository.findByLogin(login);
        if (foundedUser.isEmpty()) {
            throw new NoSuchElementException("Пользователь с данным логином не найден.");
        }
        return foundedUser.get();
    }

    @Override
    @Transactional
    public void updateFriendsList(String operation, String friendLogin, String login) {
        Optional<User> foundedUser = userRepository.findByLogin(login);
        Optional<User> foundedFriend = userRepository.findByLogin(friendLogin);

        if (foundedUser.isEmpty()) {
            throw new NoSuchElementException("Пользователь с данным логином не найден.");
        }

        if (foundedFriend.isEmpty()) {
            throw new NoSuchElementException("Друг с переданным логином не найден.");
        }

        User user = foundedUser.get();
        User friend = foundedFriend.get();

        Friend newMainFriend = new Friend(friend.getLogin(), user);
        Friend newAdditionalFriend = new Friend(user.getLogin(), friend);

        if ("+".equals(operation)) {
            user.getFriends().add(newMainFriend);
            friend.getFriends().add(newAdditionalFriend);
        } else if ("-".equals(operation)) {
            user.getFriends().remove(newMainFriend);
            friend.getFriends().remove(newAdditionalFriend);
        } else {
            throw new IllegalArgumentException("Неверная операция. Ожидалось '+' или '-'.");
        }

        friendRepository.save(newMainFriend);
        friendRepository.save(newAdditionalFriend);

        userRepository.save(user);
        userRepository.save(friend);
    }

    @Override
    public List<Friend> getFriends(String login) {
        Optional<User> foundedUser = userRepository.findByLogin(login);
        if (foundedUser.isEmpty()) {
            throw new NoSuchElementException("Пользователь с данным логином не найден.");
        }
        User user = foundedUser.get();
        return user.getFriends();
    }

    @Override
    public List<User> getUsersWithFilter(HairColor hairColor, String sex) {
        if (hairColor != null) {
            Optional<List<User>> resultList = userRepository.findByHairColorAndSex(hairColor, sex);
            return resultList.orElse(null);
        }
        return null;
    }
}