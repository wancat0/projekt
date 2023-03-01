package com.wanca.aplikacja.entity;

import com.wanca.aplikacja.exceptions.ProductNotAvailableException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Setter
@Getter
@Entity
@NoArgsConstructor
public class ShopStore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int count;

    public ShopStore(Shop shop, Product product) {
        this.shop = shop;
        this.product = product;
        this.count = 0;
    }

    public boolean isAvailable(int count) {
        return getCount() >= count;
    }

    public void removeProduct(int count) {
        if (isAvailable(count)) {
            this.count -= count;
            product.addProduct(count);
        } else throw new ProductNotAvailableException();
    }

    public void addProduct(int count) {
        this.count += count;
        product.removeProduct(count);
    }
}
