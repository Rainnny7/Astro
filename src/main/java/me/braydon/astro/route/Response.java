package me.braydon.astro.route;

import lombok.Getter;

/**
 * @author Braydon
 */
@Getter
public class Response {
    private ResponseCode responseCode = ResponseCode.OK;

    /**
     * Set the response code.
     *
     * @param responseCode the response code
     */
    public void responseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }
}