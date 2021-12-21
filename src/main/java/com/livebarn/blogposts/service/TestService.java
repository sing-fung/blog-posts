package com.livebarn.blogposts.service;

import com.livebarn.blogposts.common.TestResult;
import com.livebarn.blogposts.mapper.Post;
import com.livebarn.blogposts.mapper.Posts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;

/**
 * @author sing-fung
 * @since 12/21/2021
 */

@Service
public class TestService
{
    private PostsService postsService;
    private RestTemplate restTemplate;

    @Autowired
    public TestService(PostsService postsService, RestTemplate restTemplate)
    {
        this.postsService = postsService;
        this.restTemplate = restTemplate;
    }

    public TestResult test(String[] tags, String sortBy, String direction)
    {
        // posts received from my own method
        Post[] myPost_array = postsService.posts(tags, sortBy, direction).getPosts();
        // check the number of posts
        Boolean numberofPosts = checkNumberofPosts(tags, myPost_array.length);
        // check the sequence of posts
        Boolean sequence = checkSequence(myPost_array, sortBy, direction);

        TestResult testResult = new TestResult();
        if(numberofPosts == Boolean.TRUE) {
            testResult.setNumberofPosts("correct");
        } else {
            testResult.setNumberofPosts("incorrect");
        }

        if(sequence == Boolean.TRUE) {
            testResult.setSequence("correct");
        } else {
            testResult.setSequence("incorrect");
        }

        return testResult;
    }

    public Boolean checkNumberofPosts(String[] tags, int myPost_size)
    {
        HashSet<String> set = new HashSet<>();
        int post_size = 0;

        for(String tag : tags)
        {
            String url = "https://api.hatchways.io/assessment/blog/posts?tag=" + tag;
            Post[] curr_posts = restTemplate.getForObject(url, Posts.class).getPosts();

            for(Post curr_post : curr_posts)
            {
                Boolean added = Boolean.FALSE;
                for(String curr_tag : curr_post.getTags())
                {
                    if(set.contains(curr_tag))
                    {
                        added = Boolean.TRUE;
                        break;
                    }
                }
                // this post has not been added
                if(added == Boolean.FALSE) { post_size++; }
            }

            set.add(tag);
        }

        return post_size == myPost_size;
    }

    public Boolean checkSequence(Post[] posts, String sortBy, String direction)
    {
        for(int i=1; i<posts.length; i++)
        {
            double value1, value2;

            if(sortBy.equals("id")) {
                value1 = posts[i-1].getId();
                value2 = posts[i].getId();
            } else if(sortBy.equals("reads")) {
                value1 = posts[i-1].getReads();
                value2 = posts[i].getReads();
            } else if(sortBy.equals("likes")) {
                value1 = posts[i-1].getLikes();
                value2 = posts[i].getLikes();
            } else {
                value1 = posts[i-1].getPopularity();
                value2 = posts[i].getPopularity();
            }

            if(direction.equals("asc")) {
                if(value1 > value2) { return Boolean.FALSE; }
            } else {
                if(value1 < value2) { return Boolean.FALSE; }
            }
        }

        return Boolean.TRUE;
    }
}