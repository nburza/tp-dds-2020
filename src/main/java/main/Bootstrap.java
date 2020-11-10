package main;

import entidadOrganizativa.CategoriaEntidad;
import entidadOrganizativa.Organizacion;
import entidadOrganizativa.RepositorioDeOrganizaciones;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.util.ArrayList;

public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    public static void main(String[] args) { new Bootstrap().run();}

    public void run() {
        Organizacion organizacion = new Organizacion(new ArrayList<>(), new ArrayList<>());
        CategoriaEntidad categoria1 = new CategoriaEntidad("categoria1",null);
        CategoriaEntidad categoria2 = new CategoriaEntidad("categoria2",null);
        CategoriaEntidad categoria3 = new CategoriaEntidad("categoria3",null);
        CategoriaEntidad categoria4 = new CategoriaEntidad("categoria4",null);
        organizacion.agregarCategoria(categoria1);
        organizacion.agregarCategoria(categoria2);
        organizacion.agregarCategoria(categoria3);
        organizacion.agregarCategoria(categoria4);
        withTransaction(() -> {
            try {
                RepositorioDeOrganizaciones.getInstance().agregar(organizacion);
                Usuario migue = new Usuario("migue","alta clave");
                RepositorioDeUsuarios.getInstance().agregar(migue);
                organizacion.agregarUsuario(migue);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        entityManager().clear();
    }
}
