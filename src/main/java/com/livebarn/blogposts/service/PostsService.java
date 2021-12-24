package com.livebarn.blogposts.service;

import com.livebarn.blogposts.mapper.Post;
import com.livebarn.blogposts.mapper.Posts;
import com.livebarn.blogposts.model.CachePost;
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
    private CacheService cacheService;

    @Autowired
    public PostsService(RestTemplate restTemplate, CacheService cacheService)
    {
        this.restTemplate = restTemplate;
        this.cacheService = cacheService;
    }

    private PriorityQueue<Post> initializeQueue(String sortBy)
    {
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

        return queue;
    }

    private Post[] sortPosts(String direction, PriorityQueue<Post> queue, List<Post> postList)
    {
        Post[] post_array = new Post[postList.size()];

        for(Post post : postList)
        { queue.add(post); }

        int index;
        if(direction.equals("asc")) {
            index = 0;
        } else {
            index = postList.size() - 1;
        }

        // handle asc & desc
        while (!queue.isEmpty())
        {
            Post p = queue.poll();
            if(direction.equals("asc")) {
                post_array[index++] = p;
            } else {
                post_array[index--] = p;
            }
        }

        return post_array;
    }

    public Posts posts(String[] tags, String sortBy, String direction)
    {
        // give each thread an individual tag for query
        QueryTask[] threads = new QueryTask[tags.length];
        for(int i=0; i<tags.length; i++)
        {
            threads[i] = new QueryTask(tags[i], restTemplate, cacheService);
            threads[i].start();
        }

        // wait for all threads to finish their tasks
        try {
            for (int i = 0; i < tags.length; i++)
            { threads[i].join(); }
        } catch (InterruptedException e) {

        }

        // save ids of posts
        HashSet<Integer> set = new HashSet<>();
        List<Post> postList = new ArrayList<>();

        for(int i=0; i<tags.length; i++)
        {
            List<Post> curr_postList = threads[i].getMyPostList();
            for(Post post : curr_postList)
            {
                if(!set.contains(post.getId()))
                {
                    postList.add(post);
                    set.add(post.getId());
                }
            }
        }

        // initialize different comparators
        PriorityQueue<Post> queue = initializeQueue(sortBy);
        // sort posts
        Post[] post_array = sortPosts(direction, queue, postList);

        return new Posts(post_array);
    }
}

class QueryTask extends Thread
{
    private final String tag;
    private List<Post> myPostList;
    private RestTemplate restTemplate;
    private CacheService cacheService;

    public QueryTask(String tag, RestTemplate restTemplate, CacheService cacheService)
    {
        this.tag = tag;
        this.restTemplate = restTemplate;
        this.cacheService = cacheService;
        this.myPostList = new ArrayList<>();
    }

    public List<Post> getMyPostList()
    { return this.myPostList; }

    public void run()
    {
        // check whether these posts are in MongoDB
        List<CachePost> cachePostList = cacheService.getCachePostByTag(tag);
        // there are records in MongoDB
        if(cachePostList.size() != 0) {
            for(CachePost cachePost : cachePostList)
            {
                // turn into Post
                Post curr_post = cachePost.toPost();
                myPostList.add(curr_post);
            }
        } else { // no records in MongoDB, have to call API
            String url = "https://api.hatchways.io/assessment/blog/posts?tag=" + tag;
            Posts curr_posts = restTemplate.getForObject(url, Posts.class);
            List<CachePost> curr_cachePostList = new ArrayList<>();

            for (Post post : curr_posts.getPosts())
            {
                myPostList.add(post);

                CachePost cachePost = post.toCachePost(tag);
                curr_cachePostList.add(cachePost);
            }

            // save these records into MongoDB
            cacheService.saveCachePost(curr_cachePostList);
        }
    }
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