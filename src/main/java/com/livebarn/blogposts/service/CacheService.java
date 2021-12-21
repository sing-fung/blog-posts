package com.livebarn.blogposts.service;

import com.livebarn.blogposts.model.CachePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sing-fung
 * @since 12/20/2021
 */

@Transactional
@Service
public class CacheService
{
    private MongoTemplate mongoTemplate;

    @Autowired
    public CacheService(MongoTemplate mongoTemplate)
    { this.mongoTemplate = mongoTemplate; }

    public void saveCachePost(List<CachePost> cachePostList)
    {
        mongoTemplate.insertAll(cachePostList);
    }

    public List<CachePost> getCachePostByTag(String tag)
    {
        Query query = Query.query(Criteria.where("tag").is(tag));

        return mongoTemplate.find(query, CachePost.class);
    }

    public void deleteCachePost(String tag)
    {
        Query query = Query.query(Criteria.where("tag").is(tag));
        mongoTemplate.remove(query, CachePost.class);
    }
}
