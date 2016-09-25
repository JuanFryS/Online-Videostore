package videoclub;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pelicula {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private long id;
	private String nombre;
	private String url;
	private String descripcion;
	private String ano;
	private String director;
	private String reparto;
	private String url_portada;
	private String valoracion; // String o entero, revisar.

	public Pelicula() {
	};

	public Pelicula(String nombre, String url, String descripcion, String ano, String director, String reparto,
			String url_portada, String valoracion) {
		this.nombre = nombre;
		this.url = url;
		this.descripcion = descripcion;
		this.ano = ano;
		this.director = director;
		this.reparto = reparto;
		this.url_portada = url_portada;
		this.valoracion = valoracion;
	}

	// public Pelicula(String nombre, String url) {
	// this.nombre = nombre;
	// this.url = url;
	// this.descripcion = "";
	// this.ano = "";
	// this.director = "";
	// this.reparto = "";
	// this.url_portada = "";
	// this.valoracion = "";
	// }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getReparto() {
		return reparto;
	}

	public void setReparto(String reparto) {
		this.reparto = reparto;
	}

	public String getUrl_portada() {
		return url_portada;
	}

	public void setUrl_portada(String url_portada) {
		this.url_portada = url_portada;
	}

	public String getValoracion() {
		return valoracion;
	}

	public void setValoracion(String valoracion) {
		this.valoracion = valoracion;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Pelicula [nombre=" + nombre + ", url=" + url + ", descripcion=" + descripcion + ", año=" + ano
				+ ", director=" + director + ", reparto=" + reparto + ", url_portada=" + url_portada + ", valoracion="
				+ valoracion + "]";
	};

}
