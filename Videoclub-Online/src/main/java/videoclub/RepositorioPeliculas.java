package videoclub;

import org.springframework.data.repository.CrudRepository;

public interface RepositorioPeliculas extends CrudRepository<Pelicula, Long>{

	Pelicula findByNombre(String nombre);

	Pelicula findByNombreAndUrl(String nombre, String url);

}
