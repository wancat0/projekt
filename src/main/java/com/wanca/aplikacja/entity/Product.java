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

    public void addProduct(int count) {
        this.count += count;
    }

    public void removeProduct(int count) {
        if (isAvailable(count))
            this.count -= count;
        else throw new ProductNotAvailableException();
    }

    public boolean isAvailable(int count) {
        return getCount() >= count;
    }
}
