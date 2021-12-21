package com.livebarn.blogposts.model;

import com.livebarn.blogposts.mapper.Post;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author sing-fung
 * @since 12/20/2021
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "blogposts")
public class CachePost
{
    private String tag;
    private String author;
    private int authorId;
    private int id;
    private int likes;
    private double popularity;
    private int reads;
    private String[] tags;

    public Post toPost()
    {
        Post post = new Post();
        post.setAuthor(this.getAuthor());
        post.setAuthorId(this.getAuthorId());
        post.setId(this.getId());
        post.setLikes(this.getLikes());
        post.setPopularity(this.getPopularity());
        post.setReads(this.getReads());
        post.setTags(this.getTags());

        return post;
    }
}
