package ru.skillbox.socialnetwork.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.data.dto.AddPostRequest;
import ru.skillbox.socialnetwork.data.dto.Posts.*;
import ru.skillbox.socialnetwork.data.entity.*;
import ru.skillbox.socialnetwork.data.repository.*;
import ru.skillbox.socialnetwork.service.PostService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PersonRepo personRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final Post2TagRepository post2TagRepository;
    private final PostCommentsRepository postCommentsRepository;
    private final PostLikesRepository postLikesRepository;

    private final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Override
    public AddNewPostResponse addNewPost(Long authorId, AddPostRequest addPostRequest, Long publicationTimestamp) {

        Person person = personRepository.findById(authorId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = new Post();
        post.setAuthor(person);
        post.setTime(publicationTimestamp == null ? LocalDateTime.now() : LocalDateTime.ofEpochSecond(publicationTimestamp, 0, ZoneOffset.UTC));
        post.setTitle(addPostRequest.getTitle());
        post.setTextHtml(addPostRequest.getText());
        post.setBlocked(false);
        postRepository.save(post);

        for (String tagString : addPostRequest.getTags()) {
            Tag tag = tagRepository.findByTag(tagString);
            if(tag == null){
                tag = new Tag(tagString);
                tagRepository.save(tag);
            }
            post2TagRepository.save(new Post2Tag(post, tag));
        }

        return createFullPostResponse(person, post, 0, null);
    }

    @Override
    public GetUserPostsResponse getUserPosts(Long personId, int offset, int limit) {

        Person person = personRepository.findById(personId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<PostDto> posts = new ArrayList<>();

        for (Post post : postRepository.findPostsByAuthor(person, PageRequest.of(offset/limit, limit))) {
            List<CommentDto> comments = new ArrayList<>();
            for (PostComment postComment : postCommentsRepository.findAllByPostId(post.getId())) {
                //TODO
                //comments.add(new CommentDto(postComment));
            }
            posts.add(new PostDto(
                    post,
                    postLikesRepository.countByPost(post),
                    comments
            ));
        }

        return GetUserPostsResponse.builder()
                .error("string")
                .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                .total(posts.size())
                .offset(offset)
                .perPage(limit)
                .posts(posts)
                .build();
    }

    private AddNewPostResponse createFullPostResponse(Person person, Post post, int likesCount, ArrayList<CommentDto> comments) {
        return AddNewPostResponse.builder()
                .error("string")
                .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                .data(AddNewPostResponse.Data.builder()
                        .id(post.getId())
                        .timestamp(post.getTime().toEpochSecond(ZoneOffset.UTC))
                        .author(new AuthorDto(person))
                        .title(post.getTitle())
                        .text(post.getTextHtml())
                        .isBlocked(post.isBlocked())
                        .likes(likesCount)
                        .comments(comments)
                        .build())
                .build();
    }
}
