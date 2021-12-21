package com.livebarn.blogposts.mapper;

import com.livebarn.blogposts.model.CachePost;
import lombok.*;

/**
 * @author sing-fung
 * @since 12/20/2021
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post
{
    private String author;
    private int authorId;
    private int id;
    private int likes;
    private double popularity;
    private int reads;
    private String[] tags;

    public CachePost toCachePost(String tag)
    {
        CachePost cachePost = new CachePost();
        cachePost.setTag(tag);
        cachePost.setAuthor(this.getAuthor());
        cachePost.setAuthorId(this.getAuthorId());
        cachePost.setPostId(this.getId());
        cachePost.setLikes(this.getLikes());
        cachePost.setPopularity(this.getPopularity());
        cachePost.setReads(this.getReads());
        cachePost.setTags(this.getTags());

        return cachePost;
    }
}