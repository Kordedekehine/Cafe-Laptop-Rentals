package laptopRentage.rentages.AuditEntity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime rentedAt;
}
