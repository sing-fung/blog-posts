# Blog Posts

**Developer**: Peter Ruan 

**Email**: singfung.work@gmail.com

## Prerequisites
* Maven 3.6.0 or higher versions
* MongoDB 4.4.5 or higher versions

## How to run this project
1. Start your local MongoDB. The configuration of MongoDB is defined in `\src\main\resources\application.properties`;
2. Run `\src\main\java\com\livebarn\blogposts\BlogPostsApplication`;
3. The port used in this project is `8080`. Therefore, the URLs of the two routes are `http://localhost:8080/api/ping` and `http://localhost:8080/api/posts` respectively.

## Error handling

I didn't strictly follow the format defined in the document since I believe the following would be better:

```json
{
    "timestamp": "2021-12-21T19:23:47.380+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Tags parameter is required",
    "path": "/api/posts"
}
```

## Test without using the solution API route

A route is designed to achieve this function.

**URL**: http://localhost:8080/api/test

**Method**: GET

**Query Parameters**: same as that of Route 2

**Example Response**:
``````json
{
    "numberofPosts": "correct",
    "sequence": "correct"
}
``````

**Note**: The test could be divided into two parts. The first part is to check whether the number of posts is correct, and the other part is to check whether the sequence is correct.

## Caching

Posts would be saved into MongoDB every time the program needs to call the Hatchways API. Therefore, next time the program meets a tag that has been queried, it will fetch data from MongoDB directly.