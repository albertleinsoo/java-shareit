package ru.practicum.shareit.item.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Entity
@Table(name = "items")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @Positive
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Название предмета не может быть пустым.")
    @Length(max = 255)
    private String name;

    @NotNull(message = "Описание предмета не может быть пустым.")
    @Length(max = 512)
    private String description;

    @Column(name = "is_available")
    @NotNull(message = "Статус предмета не может быть пустым.")
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public Item(Integer id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;
        return getId() != null && Objects.equals(getId(), item.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
