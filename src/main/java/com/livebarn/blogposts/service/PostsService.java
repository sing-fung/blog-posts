package com.livebarn.blogposts.service;

import com.livebarn.blogposts.mapper.Post;
import com.livebarn.blogposts.mapper.Posts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.List;

/**
 * @author sing-fung
 * @since 12/20/2021
 */

@Service
public class PostsService
{
    private RestTemplate restTemplate;

    @Autowired
    public PostsService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public Posts posts(String[] tags, String sortBy, String direction)
    {
        HashSet<Integer> set = new HashSet<>();

        List<Post> postList = new ArrayList<>();

        for(String tag : tags)
        {
            String url = "https://api.hatchways.io/assessment/blog/posts?tag=" + tag;
            Posts result = restTemplate.getForObject(url, Posts.class);

            for(Post post : result.getPosts())
            {
                if(!set.contains(post.getId()))
                {
                    postList.add(post);
                    set.add(post.getId());
                }
            }
        }

        PriorityQueue<Post> queue;
        if(sortBy.equals("id")) {
            IdComparator idComparator = new IdComparator();
            queue = new PriorityQueue<>(idComparator);
        } else if(sortBy.equals("reads")) {
            ReadsComparator readsComparator = new ReadsComparator();
            queue = new PriorityQueue<>(readsComparator);
        } else if(sortBy.equals("likes")) {
            LikesComparator likesComparator = new LikesComparator();
            queue = new PriorityQueue<>(likesComparator);
        } else {
            PopularityComparator popularityComparator = new PopularityComparator();
            queue = new PriorityQueue<>(popularityComparator);
        }

        for(Post post : postList)
        { queue.add(post); }

        Post[] post_array = new Post[postList.size()];
        int index;
        if(direction.equals("asc")) {
            index = 0;
        } else {
            index = postList.size() - 1;
        }

        while (!queue.isEmpty())
        {
            Post p = queue.poll();
            if(direction.equals("asc")) {
                post_array[index++] = p;
            } else {
                post_array[index--] = p;
            }
        }

        return new Posts(post_array);
    }

    class IdComparator implements Comparator<Post>
    {
        public int compare(Post p1, Post p2)
        {
            int id1 = p1.getId();
            int id2 = p2.getId();

            return (id1 - id2) > 0 ? 1 : -1;
        }
    }

    class ReadsComparator implements Comparator<Post>
    {
        public int compare(Post p1, Post p2)
        {
            int reads1 = p1.getReads();
            int reads2 = p2.getReads();

            return (reads1 - reads2) >= 0 ? 1 : -1;
        }
    }

    class LikesComparator implements Comparator<Post>
    {
        public int compare(Post p1, Post p2)
        {
            int likes1 = p1.getLikes();
            int likes2 = p2.getLikes();

            return (likes1 - likes2) >= 0 ? 1 : -1;
        }
    }

    class PopularityComparator implements Comparator<Post>
    {
        public int compare(Post p1, Post p2)
        {
            double popularity1 = p1.getPopularity();
            double popularity2 = p2.getPopularity();

            return (popularity1 - popularity2) >= 0 ? 1 : -1;
        }
    }
}