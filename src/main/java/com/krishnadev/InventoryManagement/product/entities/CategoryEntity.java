package com.krishnadev.InventoryManagement.product.entities;


import com.krishnadev.Inventory.commons.ENUM.CommonEnums;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     private  Long Id;

    @Column(name = "name")
    private Long name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CommonEnums type;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;


//    @OneToMany(mappedBy = "order", targetEntity = OrderItemEntity.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY,   orphanRemoval = true)
//    private List<OrderItemEntity> orderItems;

}
