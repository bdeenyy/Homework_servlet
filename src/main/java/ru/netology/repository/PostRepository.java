package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;

public class PostRepository {

    private final Set<Post> posts;

    public PostRepository() {
        this.posts = new HashSet<>();
    }

    public List<Post> all() {
        return Collections.unmodifiableList(new ArrayList<>(posts));
    }

    public Optional<Post> getById(long id) {
        for (Post post : posts) {
            if (post.getId() == id) {
                return Optional.of(post);
            }
        }
        return Optional.empty();
    }

    public Post save(Post post) {
        Objects.requireNonNull(post, "Post cannot be null");
        Optional<Post> postToUpdate = getById(post.getId());
        postToUpdate.ifPresent(this::delete);
        posts.add(post);
        return post;
    }

    public void delete(Post post) {
        Objects.requireNonNull(post, "Post cannot be null");
        posts.remove(post);
    }

    public void removeById(long id) {
        Optional<Post> postToRemove = getById(id);
        postToRemove.ifPresent(this::delete);
    }

}