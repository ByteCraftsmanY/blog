package com.yogesh.blog.services;

import com.yogesh.blog.entities.Comment;
import com.yogesh.blog.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Optional<Comment> fetchComment(Integer id) {
        return commentRepository.findById(id);
    }

    public void saveComment(Comment comment){
        commentRepository.save(comment);
    }

    public void deleteComment(Integer id){
        commentRepository.deleteById(id);
    }

}
