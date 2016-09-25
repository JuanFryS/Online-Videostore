package videoclub;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private long id;
	private String nombre;
	private String password;
	private String email;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<GrantedAuthority> roles;

	public Usuario() {
	};

	public Usuario(String nombre, String password, String email, List<GrantedAuthority> roles) {
		this.nombre = nombre;
		this.password = new BCryptPasswordEncoder().encode(password);
		this.email = email;
		this.roles = roles;
	}

	// Getters and Setters

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPasswordHash() {
		return password;
	}

	public void setPasswordHash(String passwordHash) {
		this.password = passwordHash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<GrantedAuthority> getRoles() {
		return roles;
	}

	public void setRoles(List<GrantedAuthority> roles) {
		this.roles = roles;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", password=" + password + ", email=" + email + ", roles=" + roles + "]";
	}

}
