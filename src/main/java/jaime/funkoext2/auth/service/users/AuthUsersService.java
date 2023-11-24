package jaime.funkoext2.auth.service.users;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthUsersService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username);
}
