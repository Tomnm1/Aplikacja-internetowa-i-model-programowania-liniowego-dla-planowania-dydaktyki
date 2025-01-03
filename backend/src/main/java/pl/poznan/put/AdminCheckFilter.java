package pl.poznan.put;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;
import pl.poznan.put.planner_endpoints.Teacher.TeacherService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class AdminCheckFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AdminCheckFilter.class);
    private final TeacherService userService;

    // W tym przykładzie "adminEndpoints" zawiera tylko "/admins".
    // Ale jeśli chcesz, żeby "/teachers/**" sprawdzało role w Spring Security
    // (zamiast w tym filtrze), po prostu nie musisz tu dodawać "/teachers/...".
    private final Set<String> adminEndpoints = Set.of("/admins");

    public AdminCheckFilter(TeacherService userRepository) {
        this.userService = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestPath = request.getRequestURI();

        // Pobieramy bieżące Authentication ze Spring Security
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authorities before role assignment: {}", authentication.getAuthorities());

        // Sprawdzamy, czy w principal mamy JWT
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String email = jwt.getClaimAsString("sub");
            logger.info("User with uid {} is attempting to access {}", email, requestPath);

            // Szukamy użytkownika w bazie
            Optional<Teacher> teacher = userService.findByEmail(email);


            if (teacher.isPresent()) {
                Teacher user = teacher.get ();

                // 1. Zbuduj listę ról (Authorities) na podstawie isAdmin
                List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
                logger.info("Showing user {} and isAdmin is: {}, i jeszzcze dodatkowo wex se zobacz imię {}", user.lastName, user.isAdmin, user.firstName);
                if (user.isAdmin) {
                    updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                } else {
                    updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_TEACHER"));
                }

                // 2. Stwórz nowe Authentication, które ma aktualne role
                var newAuth = new UsernamePasswordAuthenticationToken(
                        authentication.getPrincipal(),
                        authentication.getCredentials(),
                        updatedAuthorities
                );
                // 3. Wstaw do SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(newAuth);

                // Od tej chwili Spring Security "widzi" użytkownika z nadanymi rolami
                logger.info("Assigned roles based on teacher.isAdmin. Current roles: {}", newAuth.getAuthorities());

                // 4. Jeśli ten endpoint jest w adminEndpoints, to samodzielnie sprawdź isAdmin
                if (adminEndpoints.contains(requestPath)) {
                    if (user.isAdmin) {
                        logger.info("User {} has admin access to {}", email, requestPath);
                        filterChain.doFilter(request, response);
                    } else {
                        logger.warn("User {} unauthorized attempt to access admin endpoint {}", email, requestPath);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Unauthorized: Admin access required");
                    }
                } else {
                    // Jeśli to nie jest endpoint admina, przepuszczamy dalej
                    filterChain.doFilter(request, response);
                }
            } else {
                // Użytkownik nie istnieje w bazie -> rejestrujemy go
                logger.warn("New user {} is not registered. Registering now.", email);
                Teacher newUser = new Teacher();
                newUser.firstName = jwt.getClaimAsString("gnm");
                newUser.lastName = jwt.getClaimAsString("snm");
                newUser.email = email;
                newUser.isAdmin = false;
                userService.createTeacher(newUser);

                logger.warn("User {} was not registered and has been created with admin = false.", email);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: User was not registered");
            }
        } else {
            logger.warn("Unauthenticated request to {}", requestPath);
            filterChain.doFilter(request, response);
        }
    }
}
