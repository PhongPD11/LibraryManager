package com.example.librarymanager.Jwt;

import com.example.librarymanager.Services.Implement.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class JwtAuthenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @Autowired
    public JwtAuthenFilter(JwtTokenProvider tokenProvider, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return nonAuthenUrl(requestURI);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);
            Long userId = tokenProvider.getUserIdFromJwt(jwt);
            UserDetail user = userService.loadUserById(userId);
            if (user != null) {
                UsernamePasswordAuthenticationToken
                        authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                if (!user.getUser().getIsAdmin() && !isUserAPI(request)) {
                    SecurityContextHolder.getContext().setAuthentication(null);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                    return;
                }
            } else {
                SecurityContextHolder.getContext().setAuthentication(null);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

        } catch (Exception ex) {
            log.error("failed on set user authentication", ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Boolean isUserAPI(HttpServletRequest request) {
        String method = request.getMethod();
        return "GET".equals(method);
    }

    private Boolean nonAuthenUrl(String uri) {
        List<String> urls = List.of("/login", "/register", "/library/test", "/change-device", "/notification", "/verify", "/email");
        for (String path : urls) {
            if (uri.startsWith(path)) {
                return true;
            }
        }
        return false;
    }
}