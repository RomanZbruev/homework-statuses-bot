package app.filatov.homeworkstatusesbot.model;

import app.filatov.homeworkstatusesbot.bot.handle.texthandler.state.UserState;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "users")
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "api_type")
    private String apiType;

    @Enumerated(EnumType.STRING)
    private UserState state;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Homework> homeworks;

    @PrePersist
    void onCreate() {
        apiType = "OAuth";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
