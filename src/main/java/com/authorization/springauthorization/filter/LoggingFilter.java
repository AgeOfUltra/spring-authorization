package com.authorization.springauthorization.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (shouldLogRequest(request)) {
            // Wrap both request and response
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

            try {
                // Pass BOTH wrapped objects to the filter chain
                filterChain.doFilter(wrappedRequest, wrappedResponse);

                // Log after processing
                logRequestBody(wrappedRequest);
                logResponseBody(wrappedResponse);

            } finally {
                //Copy cached response body back to the original response
                // Without this, the client won't receive the response!
                wrappedResponse.copyBodyToResponse();
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void logRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            String jsonBody = new String(content, StandardCharsets.UTF_8);
            System.out.println("Request JSON: " + jsonBody);
        }
    }

    private void logResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String jsonBody = new String(content, StandardCharsets.UTF_8);
            System.out.println("Response JSON: " + jsonBody);
            System.out.println("Response Status: " + response.getStatus());
            System.out.println("Response Content-Type: " + response.getContentType());
        }
    }

    private boolean shouldLogRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        String method = request.getMethod();

        return ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method))
                && contentType != null
                && contentType.contains("application/json");
    }
}