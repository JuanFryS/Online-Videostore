package videoclub;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private RepositorioUsuarios RepositorioUsuarios;

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String nombre = authentication.getName();
		String password = (String) authentication.getCredentials();

		Usuario usuario = RepositorioUsuarios.findByNombre(nombre);

		if (usuario == null) {
			throw new BadCredentialsException("User not found");
		}
		if (!new BCryptPasswordEncoder().matches(password, usuario.getPasswordHash())) {
			throw new BadCredentialsException("Wrong password");
		}

		List<GrantedAuthority> roles = usuario.getRoles();

		return new UsernamePasswordAuthenticationToken(nombre, password, roles);
	}

	public boolean supports(Class<?> arg0) {
		return true;
	}

}
