package com.yogesh.blog.repositories;

import com.yogesh.blog.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("select distinct post.author from Post post")
    List<String> findAllAuthors();

    @Query(nativeQuery = true, value = "select * from posts where published_at between ?1 and ?2")
    Page<Post> findPostsByPublishedDateDuration(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts where author = ?1 and published_at between ?2 and ?3")
    Page<Post> findPostsByAuthorAndPublishedDateDuration(String author, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts where id in (select post_id from post_tags where tag_id in (select id from tags where name in (?1))) and published_at >= ?2 and published_at <= ?3")
    Page<Post> findPostsByTagsAndPublishedDateDuration(List<String> tags, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts where author = ?1 and id in (select post_id from post_tags where tag_id in (select id from tags where name in (?2))) and published_at >= ?3 and published_at <= ?4")
    Page<Post> findPostsByAuthorAndTagsAndPublishedDateDuration(String author, List<String> tags, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts where ((content like %?1% or author like %?1% or excerpt like %?1% or title like %?1%) or id in (select post_id from post_tags where tag_id in (select id from tags where name = ?1))) and published_at >= ?2 and published_at <= ?3")
    Page<Post> findPostsByKeywordAndPublishedDateDuration(String keyword, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts where ((title like %?1% or excerpt like %?1% or content like %?1%) or (id in (select post_id from post_tags where tag_id in (select id from tags where name like %?1%)))) and author = ?2 and published_at >= ?3 and published_at <= ?4")
    Page<Post> findPostsByKeywordAndAuthorAndPublishedDateDuration(String keyword, String author, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts where ((title like %?1% or excerpt like %?1% or content like %?1% or author like %?1%) and (id in (select post_id from post_tags where tag_id in (select id from tags where name in (?2))))) and published_at >= ?3 and published_at <= ?4")
    Page<Post> findPostsByKeywordAndTagsAndPublishedDateDuration(String keyword, List<String> tags, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts where (title like %?1% or excerpt like %?1% or content like %?1%) and author = ?2 and id in (select post_id from post_tags where tag_id in (select id from tags where name in (?3))) and published_at >= ?4 and published_at <= ?5")
    Page<Post> findPostsByKeywordAndTagAndAuthorAndPublishedDateDuration(String keyword, String author, List<String> tags, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
