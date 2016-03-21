package garage.api.service.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LambaUserAuthService implements UserDetailsService {
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new User(username, "password", getGrantedAuthorities(username));
	}

	private Collection<? extends GrantedAuthority> getGrantedAuthorities(String username) {
		Collection<? extends GrantedAuthority> authorities;
		if (username.equals("admin")) {
			authorities = Arrays.asList(() -> "ROLE_ADMIN", () -> "ROLE_BASIC");
		} else {
			authorities = Arrays.asList(() -> "ROLE_BASIC");
		}
		return authorities;
	}
}