package com.livebarn.blogposts.controller;

import com.livebarn.blogposts.common.TestResult;
import com.livebarn.blogposts.service.TestService;
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
public class TestController
{
    private TestService testService;

    @Autowired
    public TestController(TestService testService)
    { this.testService = testService; }

    @GetMapping("/api/test")
    public TestResult posts(@RequestParam(required = false) String tags, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction)
    {
        if(tags == null || tags.length() == 0)
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

        // remove space
        for(int i=0; i<tag_array.length; i++)
        { tag_array[i] = tag_array[i].trim(); }

        return testService.test(tag_array, sortBy, direction);
    }
}
