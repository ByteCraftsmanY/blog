package com.yogesh.blog.services;

import com.yogesh.blog.model.Comment;
import com.yogesh.blog.model.Post;
import com.yogesh.blog.model.User;
import com.yogesh.blog.model.UserPrincipal;
import com.yogesh.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserPrincipalService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserPrincipalService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUserName(username);
        user.orElseThrow(() -> new UsernameNotFoundException("This user name is not found"));
        return new UserPrincipal(user.get());
    }

    public UserPrincipal getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            return (UserPrincipal) principal;
        }
        return null;
    }

    public Boolean isUserAdmin(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public Boolean isUserAuthorizedForPostOperation(Post post) {
        UserPrincipal userPrincipal = getLoggedInUser();
        if (Objects.isNull(userPrincipal)) {
            return false;
        } else if (isUserAdmin(userPrincipal)) {
            return true;
        }
        return Objects.equals(userPrincipal.getUser().getId(), post.getUser().getId());
    }

    public Boolean isUserAuthorizedForCommentOperation(Comment comment) {
        UserPrincipal userPrincipal = getLoggedInUser();
        if (Objects.isNull(userPrincipal)) {
            return false;
        } else if (isUserAdmin(userPrincipal)) {
            return true;
        }
        return Objects.equals(userPrincipal.getUser().getId(), comment.getUser().getId());
    }
}
