package com.livebarn.blogposts.common;

/**
 * @author sing-fung
 * @since 12/20/2021
 */

public class PingResult
{
    private Boolean success;

    public PingResult(Boolean success)
    { this.success = success; }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
