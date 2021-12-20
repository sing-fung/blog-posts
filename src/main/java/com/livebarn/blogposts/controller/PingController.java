package com.livebarn.blogposts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.livebarn.blogposts.common.Result;

/**
 * @author sing-fung
 * @since 12/20/2021
 */

@RestController
public class PingController
{
   @GetMapping("/api/ping")
   public Result ping()
   {
      return new Result(Boolean.TRUE);
   }
}