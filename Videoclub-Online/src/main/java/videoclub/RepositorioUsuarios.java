package videoclub;

import org.springframework.data.repository.CrudRepository;

public interface RepositorioUsuarios extends CrudRepository<Usuario, Long>{
	
	Usuario findByNombre(String nombre);

}
