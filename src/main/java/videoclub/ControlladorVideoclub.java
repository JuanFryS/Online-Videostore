package videoclub;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import retrofit.RestAdapter;

@Controller
public class ControlladorVideoclub {

	@Autowired
	private RepositorioUsuarios repositorioUsuarios;
	@Autowired
	private RepositorioPeliculas repositorioPeliculas;

	RestAdapter adapter = new RestAdapter.Builder().setEndpoint("http://www.omdbapi.com").build();
	FilmService service = adapter.create(FilmService.class);

	/*
	 * Login de Usuario
	 */
	@RequestMapping(value = "/login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}

	/*
	 * Pagina de inicio. Se muestra cuadro de busqueda de películas.
	 */
	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@RequestMapping(value = "/home")
	public ModelAndView home() {
		return new ModelAndView("home");
	}

	/*
	 * Gestiona la busqueda de la pelicula por el nombre. La busca en la base de
	 * datos y la envia como objeto a pagina resultado.
	 */
	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@RequestMapping(value = "/resultado", method = RequestMethod.POST)
	public ModelAndView resultado_busqueda_pelicula(Model model, String nombre) {
		ModelAndView modelo = null;
		boolean error;
		try {
			Pelicula pelicula = repositorioPeliculas.findByNombre(nombre);
			if (pelicula != null) {
				error = false;
				modelo = new ModelAndView("resultado").addObject("pelicula", pelicula);
			} else {
				error = true;
				String desc_error = "Lo sentimos, pero la pelicula no se encuentra en nuestro sistema";
				modelo = new ModelAndView("home").addObject("error", error).addObject("desc_error", desc_error);
			}
		} catch (Exception e) {
		}
		return modelo;
	}

	/*
	 * Pagina que se encargará de mostrar la pelicula
	 */
	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	@RequestMapping(value = "/ver")
	public ModelAndView ver(@RequestParam String url) {
		String id = url;
		id = id.substring(id.length() - 11, id.length());
		String iframe_url = "https://www.youtube-nocookie.com/embed/" + id;
		return new ModelAndView("ver").addObject("url", iframe_url);
	}
	
	/*
	 * Depende de la opcion, va a la pagina de gestion en cuestion
	 */
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/admin_usuarios/{opcion}", method = RequestMethod.GET)
	public ModelAndView admin_usuarios(@PathVariable String opcion) {
		if (opcion.equals("añadir")) {
			return new ModelAndView("añadir_usuario");
		} else if (opcion.equals("borrar")) {
			return new ModelAndView("borrar_usuario");
		} else if (opcion.equals("modificar")) {
			return new ModelAndView("modificar_usuario");
		}
		return new ModelAndView("error");
	}
	
	/*
	 * Al añadir un usuario, se guarda dicho usuario en la base de datos con el
	 * rol de usuario normal
	 */
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/añadir_usuario", method = RequestMethod.POST)
	public ModelAndView añadir_usuario(Model model, String nombre, String password, String password2, String email) {
		boolean error = false;
		String desc_error;
		ModelAndView modelo = null;
		try {
			Usuario user = repositorioUsuarios.findByNombre(nombre);
			if (user == null) {
				if (password.equals(password2)) {
					GrantedAuthority[] UserRoles = { new SimpleGrantedAuthority("ROLE_USER") };
					repositorioUsuarios.save(new Usuario(nombre, password, email, Arrays.asList(UserRoles)));
					// Cambiar pagina
					String mensaje = "El usuario ha sido añadido correctamente";
					modelo = new ModelAndView("insert").addObject("mensaje", mensaje);
				} else {
					error = true;
					desc_error = "Las contraseñas deben de ser iguales!";
					modelo = new ModelAndView("añadir_usuario").addObject("error", error).addObject("desc_error",
							desc_error);
				}
			} else {
				error = true;
				desc_error = "El usuario ya existe!";
				modelo = new ModelAndView("añadir_usuario").addObject("error", error).addObject("desc_error",
						desc_error);
			}
		} catch (Exception e) {
		}
		return modelo;
	}

	/*
	 * Al borrar un usuario, se elimina dicho usuario de la base de datos Los
	 * datos que se piden son nombre y email
	 */
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/borrar_usuario", method = RequestMethod.POST)
	public ModelAndView borar_usuario(Model model, String nombre, String email) {
		boolean error = false;
		String desc_error;
		ModelAndView modelo = null;
		try {
			Usuario usuario = repositorioUsuarios.findByNombre(nombre);
			if (usuario != null) {
				if (usuario.getEmail().equals(email)) {
					repositorioUsuarios.delete(usuario.getId());
					// Cambiar pagina
					String mensaje = "El usuario ha sido borrado correctamente";
					modelo = new ModelAndView("insert").addObject("mensaje", mensaje);
				} else {
					error = true;
					desc_error = "Los emails no coinciden!";
					modelo = new ModelAndView("borrar_usuario").addObject("error", error).addObject("desc_error",
							desc_error);
				}
			} else {
				error = true;
				desc_error = "El nombre introducido no corresponde a ningún usuario";
				modelo = new ModelAndView("borrar_usuario").addObject("error", error).addObject("desc_error",
						desc_error);
			}
		} catch (Exception e) {
		}
		return modelo;
	}

	/*
	 * Update del usuario
	 */
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/modificar_usuario", method = RequestMethod.POST)
	public ModelAndView modificar_usuario(Model model, String nombre, String password, String password2, String email) {
		boolean error = false;
		String desc_error;
		ModelAndView modelo = null;
		try {
			Usuario usuario = repositorioUsuarios.findByNombre(nombre);
			if (usuario != null) {
				if (email != "") {
					usuario.setEmail(email);
					String mensaje = "Los datos ha sido actualizados correctamente";
					modelo = new ModelAndView("insert").addObject("mensaje", mensaje);
				}
				if (password != "") {
					if (password.equals(password2)) {
						usuario.setPasswordHash(new BCryptPasswordEncoder().encode(password));
						String mensaje = "Los datos ha sido actualizados correctamente";
						modelo = new ModelAndView("insert").addObject("mensaje", mensaje);
					} else {
						error = true;
						desc_error = "Las contraseñas deben de ser iguales!";
						modelo = new ModelAndView("modificar_usuario").addObject("error", error).addObject("desc_error",
								desc_error);
					}
				}
				repositorioUsuarios.save(usuario);
			} else {
				error = true;
				desc_error = "El nombre introducido no corresponde a ningún usuario";
				modelo = new ModelAndView("modificar_usuario").addObject("error", error).addObject("desc_error",
						desc_error);
			}
		} catch (Exception e) {
		}
		return modelo;
	}

	/*
	 * Depende de la opcion, va a la pagina de gestion en cuestion
	 */
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/admin_peliculas/{opcion}", method = RequestMethod.GET)
	public ModelAndView admin_peliculas(@PathVariable String opcion) {
		if (opcion.equals("añadir")) {
			return new ModelAndView("añadir_pelicula");
		} else if (opcion.equals("borrar")) {
			return new ModelAndView("borrar_pelicula");
		} else if (opcion.equals("modificar")) {
			return new ModelAndView("modificar_pelicula");
		}
		return new ModelAndView("error");
	}
	
	/*
	 * Al añadir una pelicula, se guarda en la base de datos
	 */
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/añadir_pelicula", method = RequestMethod.POST)
	public ModelAndView nueva_pelicula(Model model, String nombre, String url, String año, String director,
			String valoracion, String descripcion, String reparto, String url_portada) {
		ModelAndView modelo = null;
		Boolean error;
		String desc_error;
		try {
			Pelicula pelicula = repositorioPeliculas.findByNombre(nombre);
			if (pelicula == null) {
				if (url.length() > 43) {
					error = true;
					desc_error = "Formato de url no válido. Formatos de URL admitido: "
							+ "https://www.youtube.com/watch?v=KYz2wyBy3kc o https://youtu.be/KYz2wyBy3kc";
					modelo = new ModelAndView("añadir_pelicula").addObject("error", error).addObject("desc_error",
							desc_error);
				} else {
					Map<String, String> params = new HashMap<String, String>();
					params.put("t", nombre);
					FilmResult lista = service.getInfo(params);
					if (lista.getResponse().equals("True")) {
						if (año == "")
							año = lista.getYear();
						if (director == "")
							director = lista.getDirector();
						if (valoracion == "")
							valoracion = lista.getImdbRating();
						if (reparto == "")
							reparto = lista.getActors();
						if (url_portada == "")
							url_portada = lista.getPoster();
						if (descripcion == "")
							descripcion = "No existe una descripción para esta película";
						pelicula = new Pelicula(nombre, url, descripcion, año, director, reparto, url_portada,
								valoracion);
						repositorioPeliculas.save(pelicula);
						String mensaje = "La pelicula ha sido añadida correctamente";
						modelo = new ModelAndView("insert").addObject("mensaje", mensaje);
					} else {
						error = true;
						desc_error = "El nombre de la pelicula no coincide con ninguna película";
						modelo = new ModelAndView("añadir_pelicula").addObject("error", error).addObject("desc_error",
								desc_error);
					}
				}
			} else {
				error = true;
				desc_error = "La pelicula ya existe en el sistema";
				modelo = new ModelAndView("añadir_pelicula").addObject("error", error).addObject("desc_error",
						desc_error);
			}

		} catch (Exception e) {
		}
		return modelo;
	}

	/*
	 * Pensar que hacer aquí Update de la pelicula
	 */
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/modificar_pelicula", method = RequestMethod.POST)
	public ModelAndView modificar_pelicula(Model model, String nombre, String url, String año, String director,
			String valoracion, String descripcion, String reparto, String url_portada) {
		ModelAndView modelo = null;
		Boolean error;
		String desc_error;
		try {
			Pelicula pelicula = repositorioPeliculas.findByNombre(nombre);
			if (pelicula != null) {
				if (url.length() > 43) {
					error = true;
					desc_error = "Formato de url no válido. Formatos de URL admitido: "
							+ "https://www.youtube.com/watch?v=KYz2wyBy3kc o https://youtu.be/KYz2wyBy3kc";
					modelo = new ModelAndView("añadir_pelicula").addObject("error", error).addObject("desc_error",
							desc_error);
				} else {
					pelicula.setUrl(url);
					// Interpreto que todo lo demás se obtiene directamente de
					// la
					// API y que el nombre no se puede cambiar, no?
					repositorioPeliculas.save(pelicula);
					String mensaje = "Los datos han sido actualizados correctamente";
					modelo = new ModelAndView("insert").addObject("mensaje", mensaje);
				}
			} else {
				error = true;
				desc_error = "La pelicula no existe en el sistema";
				modelo = new ModelAndView("modificar_pelicula").addObject("error", error).addObject("desc_error",
						desc_error);
			}
		} catch (Exception e) {
		}
		return modelo;
	}

	/*
	 * Al borrar una pelicula, se elimina dicha pelicula de la base de datos Los
	 * datos que se piden son nombre y url
	 */
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/borrar_pelicula", method = RequestMethod.POST)
	public ModelAndView borrar_pelicula(Model model, String nombre, String url) {
		ModelAndView modelo = null;
		boolean error = false;
		String desc_error;
		try {
			Pelicula pelicula = repositorioPeliculas.findByNombreAndUrl(nombre, url);
			if (pelicula != null) {
				repositorioPeliculas.delete(pelicula.getId());
				String mensaje = "La pelicula se ha eliminado correctamente";
				modelo = new ModelAndView("insert").addObject("mensaje", mensaje);
			} else {
				error = true;
				desc_error = "La pelicula no existe en el sistema";
				modelo = new ModelAndView("borrar_pelicula").addObject("error", error).addObject("desc_error",
						desc_error);
			}
		} catch (Exception e) {
		}
		return modelo;
	}

}
