package videoclub;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader {
	@Autowired
	private RepositorioUsuarios RepositorioUsuarios;

	@PostConstruct
	private void initDatabase() {
		// Usuario Administrador
		GrantedAuthority[] Roles = { new SimpleGrantedAuthority("ROLE_ADMIN"),
				new SimpleGrantedAuthority("ROLE_USER") };
		RepositorioUsuarios.save(new Usuario("admin", "admin", "", Arrays.asList(Roles)));
	}
}
