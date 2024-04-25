package hexlet.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    private Integer index;
    private String description;
    @ManyToOne
    private TaskStatus taskStatus;
    @ManyToOne
    private User assignee;
    @CreatedDate
    private LocalDate createdAt;
    @ManyToMany
    private Set<Label> labels = new HashSet<>();

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


}
