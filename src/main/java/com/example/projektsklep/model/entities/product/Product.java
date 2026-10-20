package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.enums.ProductType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "products")
public class Product {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @ManyToOne
 @JoinColumn(name = "author_id")
 private Producer author;

 @ManyToOne
 @JoinColumn(name = "category_id")
 private Category category;

 private String title;
 private String description;
 private String miniature;
 private BigDecimal price;
 private ProductType productType;
 private boolean published;

 @Column(name = "date_created")
 @DateTimeFormat(pattern = "yyyy-MM-dd")
 private LocalDate dateCreated;

 @Column(nullable = false, columnDefinition = "integer default 0")
 private int stockQuantity;

 private Instant unavailableUntil;

 @Version
 @Column(nullable = false, columnDefinition = "bigint default 0")
 private Long version;

 public Product(Long id, Producer author, Category category, String title, String description,
                String miniature, BigDecimal price, ProductType productType, boolean published, LocalDate dateCreated) {
  this.id = id; this.author = author; this.category = category; this.title = title;
  this.description = description; this.miniature = miniature; this.price = price;
  this.productType = productType; this.published = published; this.dateCreated = dateCreated;
 }

 public int availableStock(Instant now) {
  return unavailableUntil != null && unavailableUntil.isAfter(now) ? 0 : stockQuantity;
 }

 public Product(String title, BigDecimal price) {
  this.title = title;
  this.price = price;

 }
 public Product(String description) {
 }

}
