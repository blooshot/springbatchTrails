package com.krishnadev.InventoryManagement.product.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProductEntity {

    private static final Logger log = LoggerFactory.getLogger(ProductEntity.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private  Long Id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "title")
    private  String title;

    @Column(name = "description")
    private  String description;

    @Column(name = "instock")
    private  Boolean instock;

    @Column(name = "quantity")
    private  Integer quantity;

    @Column(name = "price")
    private  double price;

    @Column(name = "isdeleted")
    private  Boolean isdeleted;

    @Column(name = "media_url")
    private  List<String> media;

    @Column(name = "categories")
    private  List<String> categories;

    @Column(name = "tags")
    private  List<String> tags;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;


//    @OneToMany(mappedBy = "order", targetEntity = OrderItemEntity.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY,   orphanRemoval = true)
//    private List<OrderItemEntity> orderItems;

    @Override
    public String toString() {
        return "ProductEntity [Id=" + Id + "," +
                " userId=" + userId + ", title=" + title + ", description=" + description +
                ", instock=" + instock + ", quantity=" + quantity + ", price=" + price +
                ", isdeleted=" + isdeleted + ", media=" + media + ", categories=" + categories +
                ", tags=" + tags + ", created_at=" + created_at + ", updated_at=" + updated_at + "]";
    }
}
