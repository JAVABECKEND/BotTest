package com.example.bottest.model;
import com.example.bottest.model.enums.State;
import lombok.*;
import org.hibernate.annotations.Table;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String phone;
    private String chatId;
    private String lastName;
    private String firstName;
    private LocalDate localDate=LocalDate.now();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Story> story;
    @Enumerated(EnumType.STRING)
    private State state;
}
