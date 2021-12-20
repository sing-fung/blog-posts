package com.livebarn.blogposts.controller;

import com.livebarn.blogposts.mapper.Posts;
import com.livebarn.blogposts.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author sing-fung
 * @since 12/20/2021
 */

@RestController
public class PostsController
{
    private PostsService postsService;

    @Autowired
    public PostsController(PostsService postsService)
    { this.postsService = postsService; }

    @GetMapping("/api/posts")
    public Posts posts(@RequestParam(required = false) String tags, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction)
    {
        if(tags == null)
        { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tags parameter is required"); }

        if(sortBy != null) {
            if(sortBy.length() == 0) { sortBy = "id"; }

            if(!(sortBy.equals("id") || sortBy.equals("reads") || sortBy.equals("likes") || sortBy.equals("popularity")))
            { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sortBy parameter is invalid"); }
        } else {
            sortBy = "id";
        }

        if(direction != null) {
            if(direction.length() == 0) { direction = "asc"; }

            if(!(direction.equals("asc") || direction.equals("desc")))
            { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "direction parameter is invalid"); }
        } else {
            direction = "asc";
        }

        String[] tag_array;
        tag_array = tags.split(",");

        return postsService.posts(tag_array, sortBy, direction);
    }
}