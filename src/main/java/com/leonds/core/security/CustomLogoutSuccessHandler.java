package com.leonds.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonds.core.resp.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Leon
 */
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final HttpStatus httpStatusToReturn;

    public CustomLogoutSuccessHandler(HttpStatus httpStatusToReturn) {
        Assert.notNull(httpStatusToReturn, "The provided HttpStatus must not be null.");
        this.httpStatusToReturn = httpStatusToReturn;
    }

    public CustomLogoutSuccessHandler() {
        this.httpStatusToReturn = HttpStatus.OK;
    }

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setStatus(this.httpStatusToReturn.value());
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(Response.ok().build());
        out.write(result);
        response.getWriter().flush();
    }
}
