package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Positive
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Имя пользователя не может быть null.")
    @NotEmpty(message = "Имя пользователя не может быть пустым.")
    @NotBlank(message = "Имя пользователя не может быть пустой строкой.")
    @Length(max = 255)
    private String name;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    @NotNull(message = "Email пользователя не может быть null.")
    @NotEmpty(message = "Email пользователя не может быть пустым.")
    @Email
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Incorrect email format.")
    private String email;
}
