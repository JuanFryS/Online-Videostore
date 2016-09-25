package videoclub;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.QueryMap;

public interface FilmService {
	@GET("/")
	FilmResult getInfo(@QueryMap Map<String, String> params);
}
