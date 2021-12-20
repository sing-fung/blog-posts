package com.livebarn.blogposts.common;

/**
 * @author sing-fung
 * @since 12/20/2021
 */

public class Result
{
    private Boolean success;

    public Result(Boolean success)
    { this.success = success; }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
