package laptopRentage.rentages.model;

import laptopRentage.rentages.AuditEntity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "laptops")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Laptop extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer laptopId;
    @Column
    private Cores laptopCores; //core what
    @Column
    private String laptopType;  //7th or 8th gen
    @Column
    private String laptopBrand;  //whose brand

    @Column
    private Color laptopColor; //laptop color

    @Column
    private Double laptopPrice; //price of laptop

    @Column
    private LocalDateTime rentedAt;
}
