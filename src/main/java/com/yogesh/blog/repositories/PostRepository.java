package com.yogesh.blog.repositories;

import com.yogesh.blog.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("select p from Post p where p.content like %?1% or p.author like %?1% or p.excerpt like %?1% or p.title like %?1%")
    List<Post> findPostsHavingKeyword(String keyword);

    @Query(nativeQuery = true, value = "select * from posts where author like %?1% and published_at between ?2 and ?3 and id in (select post_id from post_tags where tag_id in (select id from tags where name in (?4)))")
    List<Post> findPostsWithCriteria(String author,LocalDateTime startDateTime, LocalDateTime endDateTime, List<String> tagList);

    @Query(nativeQuery = true, value = "select * from posts where id in (select post_id from post_tags where tag_id in (select id from tags where name=?1))")
    List<Post> findPostsHavingTag(String tag);

    @Query(nativeQuery = true, value = "select * from posts offset ?1 limit ?2")
    List<Post> findPostsWithOffsetAndLimit(Integer start, Integer limit);
}
