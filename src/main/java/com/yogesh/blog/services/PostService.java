package com.yogesh.blog.services;

import com.yogesh.blog.entities.Post;
import com.yogesh.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<Post> findAllPost(String sortingField, String sortingOrder, Integer page, Integer item) {
        Sort sort = Sort.by(sortingField);
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findAll(PageRequest.of(page, item, sort)).getContent();
    }

    public void deletePostById(Integer id) {
        postRepository.deleteById(id);
    }

    public List<Post> findPostsByCriteria(String keyword, LocalDateTime startDateTime, LocalDateTime endDateTime, String sortingField, String sortingOrder, Integer page, Integer size) {
        Sort sort = Sort.by(getDbTableFieldName(sortingField));
        sort = sortingOrder.equals("asc") ? sort.ascending() : sort.descending();
        return postRepository.findPostsByCriteria(keyword, startDateTime, endDateTime,PageRequest.of(page,size,sort));
    }

    private String getDbTableFieldName(String EntityFieldName) {
        StringBuilder dbTableFieldName = new StringBuilder();
        for (Character c : EntityFieldName.toCharArray()) {
            if (Character.isUpperCase(c)) {
                dbTableFieldName.append('_');
                dbTableFieldName.append(Character.toLowerCase(c));
            } else {
                dbTableFieldName.append(c);
            }
        }
        return dbTableFieldName.toString();
    }
}
