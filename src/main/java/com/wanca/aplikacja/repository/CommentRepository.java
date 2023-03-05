package com.wanca.aplikacja.repository;

import com.wanca.aplikacja.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT c.* FROM Shop s JOIN shop_comments sc on s.id = sc.shop_id JOIN comment c on c.id = sc.comments_id WHERE s.id = ?1 ORDER BY c.date ASC", nativeQuery = true)
    List<Comment> findShopComments(long shopId);
}
