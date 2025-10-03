package controlador;

import dao.PeliculaDAO;
import modelo.Pelicula;
import java.util.List;

public class PeliculaController {
    private PeliculaDAO dao = new PeliculaDAO();

    public void agregarPelicula(Pelicula p) throws Exception {
        dao.agregar(p);
    }

    public void modificarPelicula(Pelicula p) throws Exception {
        dao.modificar(p);
    }

    public void eliminarPelicula(int id) throws Exception {
        dao.eliminar(id);
    }

    public List<Pelicula> listarPeliculas() throws Exception {
        return dao.listar();
    }

    public Pelicula buscarPelicula(int id) throws Exception {
        return dao.buscar(id);
    }
}