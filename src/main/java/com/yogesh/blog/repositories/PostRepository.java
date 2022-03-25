package com.yogesh.blog.repositories;

import com.yogesh.blog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(nativeQuery = true, value = "select u.name from users u join user_roles ur on u.id = ur.user_id join roles r on r.id = ur.role_id and r.name = 'ROLE_AUTHOR'")
    List<String> findAllAuthors();

    @Query(nativeQuery = true, value = "select * from posts p where (p.title like %?1% or p.excerpt like %?1% or p.content like %?1%) and p.user_id in (select u.id from users u where u.name = ?2) and p.id in (select pt.post_id from post_tags pt where pt.tag_id in (select t.id from tags t where t.name in (?3))) and p.published_at between ?4 and ?5")
    Page<Post> findByKeywordAndAuthorAndTagAndPublishedDateDuration(String keyword, String author, List<String> tags, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts p where (p.title like %?1% or p.excerpt like %?1% or p.content like %?1% or p.id in (select pt.post_id from post_tags pt join tags t on t.id = pt.tag_id and t.name = ?1)) and p.user_id in (select u.id from users u where u.name = ?2) and p.published_at between ?3 and ?4")
    Page<Post> findByKeywordAndAuthorAndPublishedDateDuration(String keyword, String author, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts p where (p.title like %?1% or p.excerpt like %?1% or p.content like %?1% or p.user_id in (select u.id from users u where u.name like %?1%)) and p.id in (select pt.post_id from post_tags pt where pt.tag_id in (select t.id from tags t where t.name in (?2))) and p.published_at between ?3 and ?4")
    Page<Post> findByKeywordAndTagsAndPublishedDateDuration(String keyword, List<String> tags, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts p where (p.title like %?1% or p.excerpt like %?1% or p.content like %?1% or p.user_id in (select u.id from users u where u.name like %?1%) or p.id in (select pt.post_id from post_tags pt where pt.tag_id in (select t.id from tags t where t.name like %?1%))) and p.published_at between ?2 and ?3")
    Page<Post> findByKeywordAndPublishedDateDuration(String keyword, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts p join users u on u.id = p.user_id and u.name = ?1 and p.published_at between ?2 and ?3 join post_tags pt on p.id = pt.post_id and pt.tag_id in (select t.id from tags t where t.name in (?4))")
    Page<Post> findByAuthorAndTagsAndPublishedDateDuration(String author, LocalDateTime startDateTime, LocalDateTime endDateTime, List<String> tags, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts p join users u on u.id = p.user_id and u.name = ?1 and p.published_at between ?2 and ?3")
    Page<Post> findByAuthorAndPublishedDateDuration(String author, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts where id in (select post_id from post_tags where tag_id in (select id from tags where name in (?1))) and published_at >= ?2 and published_at <= ?3")
    Page<Post> findByTagsAndPublishedDateDuration(List<String> tags, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from posts where published_at between ?1 and ?2")
    Page<Post> findByPublishedDateDuration(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);
}
