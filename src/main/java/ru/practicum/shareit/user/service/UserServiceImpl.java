package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.DAO.UserDAO;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.interfaces.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final UserMapper userMapper;

    public UserDto addUser(UserDto userDto) {
        var user = userMapper.toUser(userDto);
        var createdUser = userDAO.addUser(user);
        userDto.setId(createdUser.getId());
        return userDto;
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        var user = getUserById(userId);
        userDto.setId(userId);
        userDto.setEmail(userDto.getEmail() == null ? user.getEmail() : userDto.getEmail());
        userDto.setName(userDto.getName() == null ? user.getName() : userDto.getName());

        var updatedUser = userDAO.updateUser(userMapper.toUser(userDto));
        return userMapper.toUserDto(updatedUser);
    }

    public void deleteUserById(Long userId) {
        getUserById(userId);
        userDAO.deleteUser(userId);
    }

    public UserDto getUserById(Long userId) {
        var user = userDAO.findUser(userId).orElseThrow(() -> {
            log.error("Пользователь с таким id: " + userId + ", отсутствует.");
            return new NotFoundException("Пользователь с таким id: " + userId + ", отсутствует.");
        });
        return userMapper.toUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userDAO.getAllUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
