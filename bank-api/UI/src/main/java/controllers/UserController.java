package controllers;

import dtos.ResponseUserDTO;
import enums.HairColor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mappers.CreateUserRequestMapper;
import mappers.ResponseUserMapper;
import models.Friend;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dtos.CreateUserRequestDTO;
import services.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Контроллер для работы с пользователями")
public class UserController {

    private final UserServiceImpl userService;
    private final CreateUserRequestMapper createUserRequestMapper;
    private final ResponseUserMapper responseUserMapper;

    @Autowired
    public UserController(UserServiceImpl userService,
                          CreateUserRequestMapper createUserRequestMapper,
                          ResponseUserMapper responseUserMapper) {
        this.userService = userService;
        this.createUserRequestMapper = createUserRequestMapper;
        this.responseUserMapper = responseUserMapper;
    }

    @Tag(name = "User Operations")
    @Operation(summary = "Создать нового пользователя", description = "Создание нового пользователя по данным, переданным в теле запроса")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequestDTO createUserRequestDTO) {
        User user = createUserRequestMapper.map(createUserRequestDTO);
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Tag(name = "User Operations")
    @Operation(summary = "Получить пользователя по логину", description = "Получение данных пользователя по его логину")
    @ApiResponse(responseCode = "200", description = "Пользователь найден и возвращён")
    @ApiResponse(responseCode = "404", description = "Пользователь с таким логином не найден")
    @GetMapping("/{login}")
    public ResponseEntity<ResponseUserDTO> getUser(@PathVariable("login") String login) {
        User user = userService.getUser(login);
        ResponseUserDTO responseUserDTO = responseUserMapper.map(user);
        if (responseUserDTO != null) {
            return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Tag(name = "Friend Operations")
    @Operation(summary = "Обновить список друзей пользователя", description = "Добавить или удалить друга из списка друзей пользователя")
    @ApiResponse(responseCode = "200", description = "Список друзей успешно обновлен")
    @ApiResponse(responseCode = "400", description = "Некорректный аргумент (например, неверная операция)")
    @ApiResponse(responseCode = "404", description = "Пользователь или друг не найден")
    @PostMapping("/{login}/update-friend")
    public ResponseEntity<Void> updateFriendsList(
            @PathVariable("login") String login,
            @RequestParam(value = "operation", required = false) String operation,
            @RequestParam(value = "friendLogin", required = false) String friendLogin) {
        userService.updateFriendsList(operation, friendLogin, login);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Tag(name = "Friend Operations")
    @Operation(summary = "Получить список друзей пользователя", description = "Получение списка друзей пользователя по логину")
    @ApiResponse(responseCode = "200", description = "Список друзей пользователя")
    @ApiResponse(responseCode = "204", description = "У пользователя нет друзей")
    @GetMapping("/{login}/friends")
    public ResponseEntity<List<Friend>> getFriends(@PathVariable("login") String login) {
        List<Friend> friends = userService.getFriends(login);
        if (friends != null && !friends.isEmpty()) {
            return new ResponseEntity<>(friends, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Tag(name = "User Operations")
    @Operation(summary = "Получить пользователей по фильтру", description = "Получение списка пользователей по фильтрам (цвет волос и пол)")
    @ApiResponse(responseCode = "200", description = "Список пользователей по фильтрам")
    @ApiResponse(responseCode = "204", description = "Нет пользователей, соответствующих фильтрам")
    @GetMapping("/list-with-filter")
    public ResponseEntity<List<ResponseUserDTO>> getUsers(
            @RequestParam(value = "hairColor", required = false) String hairColor,
            @RequestParam(value = "sex", required = false) String sex) {

        List<User> users = userService.getUsersWithFilter(HairColor.valueOf(hairColor.toUpperCase()), sex);
        List<ResponseUserDTO> responseList = new ArrayList<>();
        for (User user : users) {
            responseList.add(responseUserMapper.map(user));
        }

        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}