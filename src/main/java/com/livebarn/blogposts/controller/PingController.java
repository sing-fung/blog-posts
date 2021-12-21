package com.livebarn.blogposts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.livebarn.blogposts.common.PingResult;

/**
 * @author sing-fung
 * @since 12/20/2021
 */

@RestController
public class PingController
{
    @GetMapping("/api/ping")
    public PingResult ping()
   {
      return new PingResult(Boolean.TRUE);
   }
}