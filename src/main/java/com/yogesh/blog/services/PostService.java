package com.yogesh.blog.services;

import com.yogesh.blog.entities.Post;
import com.yogesh.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }

    public Post findPostById(Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    public Page<Post> findAllPost(String sortingField, String sortingOrder, Integer page, Integer item) {
        Sort sort = Sort.by(sortingField);
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findAll(PageRequest.of(page, item, sort));
    }

    public void deletePostById(Integer id) {
        postRepository.deleteById(id);
    }

    public Page<Post> findPostsByKeyword(String keyword, LocalDateTime startDateTime, LocalDateTime endDateTime, String sortingField, String sortingOrder, Integer page, Integer size) {
        Sort sort = Sort.by(getFieldNameForNativeQuery(sortingField));
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findPostsByKeyword(keyword, startDateTime, endDateTime, PageRequest.of(page, size, sort));
    }

    public Page<Post> findPostsByDuration(LocalDateTime startDateTime, LocalDateTime endDateTime, String sortingField, String sortingOrder, Integer page, Integer size) {
        Sort sort = Sort.by(getFieldNameForNativeQuery(sortingField));
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findPostsByDuration(startDateTime, endDateTime, PageRequest.of(page, size, sort));
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
