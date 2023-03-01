package com.wanca.aplikacja.util;

import com.wanca.aplikacja.dto.*;
import com.wanca.aplikacja.entity.Address;
import com.wanca.aplikacja.entity.Comment;
import com.wanca.aplikacja.entity.Shop;
import com.wanca.aplikacja.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DtoConverter {

    public static AddressDto convertAddress(Address address) {
        return new AddressDto(address.getStreet(), address.getCity(), address.getPostalCode());
    }

    public static CommentDto convertComment(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getDate());
    }

    public static EmployeeDto convertEmployee(User user) {
        return new EmployeeDto(user.getName(), user.getSurname());
    }

    public static ShopDto convertShop(Shop shop, Collection<ProductDto> products) {
        var addressDto = DtoConverter.convertAddress(shop.getAddress());
        Collection<EmployeeDto> employees = shop.getEmployee().stream().map(DtoConverter::convertEmployee).toList();
        Collection<CommentDto> comments = shop.getComments().stream().filter(comment -> !comment.isArchived() && comment.getDate().toLocalDate().isEqual(LocalDate.now())).map(DtoConverter::convertComment).toList();
        return new ShopDto(shop.getId(), shop.getName(), addressDto, employees, products, comments);
    }
}
