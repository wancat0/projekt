package com.wanca.aplikacja.entity;

import com.wanca.aplikacja.exceptions.ProductNotAvailableException;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int count;

    public void addProduct() {
        this.count++;
    }

    public void removeProduct() {
        if (isAvailable())
            this.count--;
        else throw new ProductNotAvailableException();
    }

    public boolean isAvailable() {
        return getCount() > 0;
    }
}
