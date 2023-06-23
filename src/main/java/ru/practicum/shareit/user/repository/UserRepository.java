package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserRepository {
    private final Map<Integer, User> usersMap = new HashMap<>();
    private  final Set<String> emails = new HashSet<>();
    private Integer id = 0;

    public Optional<User> getUserById(Integer id) {
        return Optional.ofNullable(usersMap.get(id));
    }

    public Set<User> getAllUsers() {
        return new HashSet<>(usersMap.values());
    }

    public User create(User user) {
        emails.add(user.getEmail());
        user.setId(++id);
        usersMap.put(user.getId(), user);
        return user;
    }

    public User update(User user, Integer id) {
        usersMap.put(id, user);
        return user;
    }

    public void delete(User user) {
        emails.remove(user.getEmail());
        usersMap.remove(user.getId());
    }

    public Set<String> allEmails() {
        return emails;
    }
}
