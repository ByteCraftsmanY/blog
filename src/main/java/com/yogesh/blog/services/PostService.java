package com.yogesh.blog.services;

import com.yogesh.blog.models.Post;
import com.yogesh.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePostById(Integer id) {
        postRepository.deleteById(id);
    }

    public Optional<Post> findPostById(Integer id) {
        return postRepository.findById(id);
    }

    public Page<Post> findAllPost(String sortingField, String sortingOrder, Integer page, Integer item) {
        Sort sort = Sort.by(sortingField);
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findAll(PageRequest.of(page, item, sort));
    }

    public Page<Post> findPostsByKeywordAndAuthorAndTagAndPublishedDateDuration(String keyword, String author, List<String> tags, LocalDateTime startDateTime, LocalDateTime endDateTime, String sortingField, String sortingOrder, Integer page, Integer size) {
        Sort sort = Sort.by(getFieldNameForNativeQuery(sortingField));
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findByKeywordAndAuthorAndTagAndPublishedDateDuration(keyword, author, tags, startDateTime, endDateTime, PageRequest.of(page, size, sort));
    }

    public Page<Post> findPostsByKeywordAndAuthorAndPublishedDateDuration(String keyword, String author, LocalDateTime startDateTime, LocalDateTime endDateTime, String sortingField, String sortingOrder, Integer page, Integer size) {
        Sort sort = Sort.by(getFieldNameForNativeQuery(sortingField));
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findByKeywordAndAuthorAndPublishedDateDuration(keyword, author, startDateTime, endDateTime, PageRequest.of(page, size, sort));
    }

    public Page<Post> findPostsByKeywordAndTagsAndPublishedDateDuration(String keyword, List<String> tags, LocalDateTime startDateTime, LocalDateTime endDateTime, String sortingField, String sortingOrder, Integer page, Integer size) {
        Sort sort = Sort.by(getFieldNameForNativeQuery(sortingField));
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findByKeywordAndTagsAndPublishedDateDuration(keyword, tags, startDateTime, endDateTime, PageRequest.of(page, size, sort));
    }

    public Page<Post> findPostsByKeywordAndPublishedDateDuration(String keyword, LocalDateTime startDateTime, LocalDateTime endDateTime, String sortingField, String sortingOrder, Integer page, Integer size) {
        Sort sort = Sort.by(getFieldNameForNativeQuery(sortingField));
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findByKeywordAndPublishedDateDuration(keyword, startDateTime, endDateTime, PageRequest.of(page, size, sort));
    }

    public Page<Post> findPostByAuthorAndTagsAndPublishedDateDuration(String author, List<String> tags, LocalDateTime startDateTime, LocalDateTime endDateTime, String sortingField, String sortingOrder, Integer page, Integer size) {
        Sort sort = Sort.by(getFieldNameForNativeQuery(sortingField));
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findByAuthorAndTagsAndPublishedDateDuration(author, startDateTime, endDateTime, tags, PageRequest.of(page, size, sort));
    }

    public Page<Post> findPostsByAuthorAndPublishedDateDuration(String author, LocalDateTime startDateTime, LocalDateTime endDateTime, String sortingField, String sortingOrder, Integer page, Integer size) {
        Sort sort = Sort.by(getFieldNameForNativeQuery(sortingField));
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findByAuthorAndPublishedDateDuration(author, startDateTime, endDateTime, PageRequest.of(page, size, sort));
    }

    public Page<Post> findPostByTagsAndPublishedDateDuration(List<String> tags, LocalDateTime startDateTime, LocalDateTime endDateTime, String sortingField, String sortingOrder, Integer page, Integer size) {
        Sort sort = Sort.by(getFieldNameForNativeQuery(sortingField));
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findByTagsAndPublishedDateDuration(tags, startDateTime, endDateTime, PageRequest.of(page, size, sort));
    }

    public Page<Post> findPostsByPublishedDateDuration(LocalDateTime startDateTime, LocalDateTime endDateTime, String sortingField, String sortingOrder, Integer page, Integer size) {
        Sort sort = Sort.by(getFieldNameForNativeQuery(sortingField));
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findByPublishedDateDuration(startDateTime, endDateTime, PageRequest.of(page, size, sort));
    }

    private String getFieldNameForNativeQuery(String fieldName) {
        StringBuilder nativeFiledName = new StringBuilder();
        for (Character c : fieldName.toCharArray()) {
            if (Character.isUpperCase(c)) {
                nativeFiledName.append('_');
                nativeFiledName.append(Character.toLowerCase(c));
            } else {
                nativeFiledName.append(c);
            }
        }
        return nativeFiledName.toString();
    }
}
