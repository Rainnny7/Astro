package me.braydon.astro.route;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This represents an HTTP response code.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter
public class ResponseCode {
    // Successful
    public static final ResponseCode OK = new ResponseCode(200, "Request was successful");
    public static final ResponseCode CREATED = new ResponseCode(201, "Request was successful and a new resource has been created");

    // Client Error
    public static final ResponseCode BAD_REQUEST = new ResponseCode(400, "The server could not understand the request due to invalid syntax");
    public static final ResponseCode UNAUTHORIZED = new ResponseCode(401, "The client must authenticate to get the requested response");
    public static final ResponseCode FORBIDDEN = new ResponseCode(403, "The client does not have access rights");
    public static final ResponseCode NOT_FOUND = new ResponseCode(404, "The server could not find the requested resource");
    public static final ResponseCode METHOD_NOT_ALLOWED = new ResponseCode(405, "The request method is known by the server but is not supported by the target resource");
    public static final ResponseCode GONE = new ResponseCode(410, "The requested content has been permanently deleted from the server");
    public static final ResponseCode IM_A_TEAPOT = new ResponseCode(418, "The server refuses the attempt to brew coffee with a teapot");
    public static final ResponseCode TOO_MANY_REQUESTS = new ResponseCode(429, "The client has sent too many requests");
    public static final ResponseCode UNAVAILABLE_FOR_LEGAL_REASONS = new ResponseCode(451, "The client has requested a resource that cannot legally be provided");

    // Server Error
    public static final ResponseCode INTERNAL_SERVER_ERROR = new ResponseCode(500, "The server has encountered a situation it doesn't know how to handle");
    public static final ResponseCode SERVICE_UNAVAILABLE = new ResponseCode(503, "The server is not ready to handle the request");
    public static final ResponseCode INSUFFICIENT_STORAGE = new ResponseCode(507, "The server is unable to store the representation needed to successfully complete the request");
    public static final ResponseCode LOOP_DETECTED = new ResponseCode(508, "The server detected an infinite loop whilst processing the request");

    private final int code;
    private final String message;

    /**
     * Check if the response is informational.
     */
    public boolean isInformational() {
        return code >= 100 && code <= 199;
    }

    /**
     * Check if the response is successful.
     */
    public boolean isSuccessful() {
        return code >= 200 && code <= 299;
    }

    /**
     * Check if the response is a redirect.
     */
    public boolean isRedirect() {
        return code >= 300 && code <= 399;
    }

    /**
     * Check if the response is a client error.
     */
    public boolean isClientError() {
        return code >= 400 && code <= 499;
    }

    /**
     * Check if the response is a server error.
     */
    public boolean isServerError() {
        return code >= 500 && code <= 599;
    }
}