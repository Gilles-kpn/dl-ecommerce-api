package fr.gilles.auth.entities.audit;

import fr.gilles.auth.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Audit {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    protected Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    protected Instant updatedAt;

    @Column(updatable = false)
    protected String code = UUID.randomUUID().toString();

    @JsonIgnore
    protected boolean deleted  = false;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(code, user.code);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
