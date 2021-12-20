package com.livebarn.blogposts.mapper;

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
}