package pl.poznan.put;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;
import pl.poznan.put.planner_endpoints.Teacher.TeacherService;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Component
public class AdminCheckFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AdminCheckFilter.class);
    private final TeacherService userService;
    private final Set<String> adminEndpoints = Set.of ("/admins");

    public AdminCheckFilter(TeacherService userRepository) {
        this.userService = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getRequestURI ();

        var authentication = SecurityContextHolder.getContext ().getAuthentication ();
        logger.info("Authorities: {}", authentication.getAuthorities());

        if (authentication != null && authentication.getPrincipal () instanceof Jwt jwt) {
            String email = jwt.getClaimAsString ("sub");
            logger.info("User with uid {} is attempting to access {}", email, requestPath);

            Optional<Teacher> teacher = userService.findByEmail (email);
            logger.info ("User with uid {}", teacher);
            if (teacher.isPresent ()) {
                if (adminEndpoints.contains (requestPath)) {
                    Teacher user = teacher.get ();
                    if (user.isAdmin) {
                        logger.info("User {} has admin access to {}", email, requestPath);
                        filterChain.doFilter (request, response);
                    } else {
                        logger.warn("User {} unauthorized attempt to access admin endpoint {}", email, requestPath);
                        response.setStatus (HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter ().write ("Unauthorized: Admin access required");
                    }
                } else {
                    filterChain.doFilter (request, response);
                }
            } else {
                logger.warn("New user {} is not registered. Registering now.", email);
                Teacher newUser = new Teacher ();
                newUser.firstName = jwt.getClaimAsString ("gnm");
                newUser.lastName = jwt.getClaimAsString ("snm");
                newUser.email = jwt.getClaimAsString ("sub");
                newUser.isAdmin = false;
                userService.createTeacher (newUser);
                logger.warn("User {} was not registered and has been created with admin privileges set to false.", email);
                response.setStatus (HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter ().write ("Unauthorized: User was not registered");
            }
        } else {
            logger.warn("Unauthenticated request to {}", requestPath);
            filterChain.doFilter (request, response);
        }
    }

}
