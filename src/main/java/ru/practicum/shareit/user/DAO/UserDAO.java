package ru.practicum.shareit.user.DAO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDAO {

    private final Map<Long, User> users;
    private Long generatedId = 1L;

    public User addUser(User user) {
        user.setId(generatedId);
        validateEmailExist(user.getId(), user.getEmail());
        users.put(user.getId(), user);
        generatedId++;
        return user;
    }

    public User updateUser(User user) {
        validateEmailExist(user.getId(), user.getEmail());
        users.put(user.getId(), user);
        return user;
    }

    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    public Optional<User> findUser(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void validateEmailExist(Long userId, String email) {
        boolean existEmail = users.values().stream()
                .filter(user -> !user.getId().equals(userId))
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
        if (existEmail) {
            log.error("Пользователь с таким Email: " + email + " уже существует");
            throw new DuplicateEmailException("Пользователь с таким Email: " + email + " уже существует");
        }
    }
}
