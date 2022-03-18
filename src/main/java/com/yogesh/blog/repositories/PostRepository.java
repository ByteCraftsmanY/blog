package com.yogesh.blog.repositories;

import com.yogesh.blog.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    String q = "select * from posts where id in (select post_id from post_tags where tag_id in (select id from tags where name=?1))";
    @Query(value = q,nativeQuery = true)
    List<Post> findPostWithTagKeyword(String keyword);

    @Query("select p from Post p where p.content like %?1% or p.author like %?1% or p.excerpt like %?1% or p.title like %?1%")
    List<Post> findPostsWithKeyword(String keyword);

    String limitQuery = "select * from posts offset ?1 limit ?2";
    @Query(value = limitQuery,nativeQuery = true)
    List<Post> findPostsByLimit(Integer start, Integer limit);
}
