package laptopRentage.rentages.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer laptopId;
    @Column
    private String laptopCores;
    @Column
    private String laptopType;
    @Column
    private Brand laptopBrand;

    @Column
    private String laptopColor;

    @Column
    private Double laptopPrice;
}
