package pl.poznan.put;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    private final TeacherService userService;
    private final Set<String> adminEndpoints = Set.of("/admins"); // TODO Tu w przyszłości może być endpoint do zarządzania adminami oraz inne rzeczy do których user nie potrzebuje dostępu

    public AdminCheckFilter(TeacherService userRepository) {
        this.userService = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getRequestURI();

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String email = jwt.getClaimAsString("sub");

            Optional<Teacher> teacher = userService.findByEloginId(email);

            if (teacher.isPresent()) {
                if (adminEndpoints.contains(requestPath)) {
                    Teacher user = teacher.get();
                    if (user.isAdmin) {
                        filterChain.doFilter(request, response);
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Unauthorized: Admin access required");
                    }
                }
            } else {
                Teacher newUser = new Teacher();

                newUser.firstName = jwt.getClaimAsString("gnm");
                newUser.lastName = jwt.getClaimAsString("snm");
                newUser.email = jwt.getClaimAsString("sub");
                newUser.isAdmin = false;

                userService.createTeacher(newUser);

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: User was not registered");

            }
        }
        filterChain.doFilter(request, response);
    }
}
