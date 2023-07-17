package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> getUserByEmail(String email);
}

/*package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.EmailExistsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;

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
        if (allEmails().contains(user.getEmail())) {
            throw new EmailExistsException("This email- {}, has been used and cannot be created");
        }
        emails.add(user.getEmail());
        user.setId(++id);
        usersMap.put(user.getId(), user);
        return user;
    }

    public User update(User user, Integer id) {
        usersMap.put(id, user);
        return user;
    }

    public void delete(Integer id) {
        User user = getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with this id %d not found", id)));
        emails.remove(user.getEmail());
        usersMap.remove(user.getId());
    }

    public Set<String> allEmails() {
        return emails;
    }
}
*/
