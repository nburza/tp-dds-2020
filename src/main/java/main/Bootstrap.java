package main;

import apiMercadoLibre.DTO.CiudadDTO;
import apiMercadoLibre.DTO.MonedaDTO;
import apiMercadoLibre.DTO.PaisDTO;
import apiMercadoLibre.DTO.ProvinciaDTO;
import apiMercadoLibre.ServiceLocator;
import apiMercadoLibre.ServicioAPIMercadoLibre;
import apiMercadoLibre.ValidadorDeMoneda;
import apiMercadoLibre.ValidadorDeUbicacion;
import egreso.Egreso;
import egreso.RepositorioDeEgresos;
import entidadOrganizativa.*;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.util.ArrayList;
import java.util.Arrays;

public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    public static void main(String[] args) { new Bootstrap().run();}

    public void run() {
        CiudadDTO ciudad = new CiudadDTO("3","La Plata");
        ProvinciaDTO provincia = new ProvinciaDTO("2","Buenos Aires", Arrays.asList(ciudad));
        PaisDTO pais = new PaisDTO("1","Argentina", Arrays.asList(provincia));
        ValidadorDeUbicacion validadorDeUbicacion = new ValidadorDeUbicacion(Arrays.asList(pais));
        ServiceLocator.getInstance().setValidadorDeUbicacion(validadorDeUbicacion);
        Organizacion organizacion = new Organizacion(new ArrayList<>(), new ArrayList<>());
        Entidad entidadBase = new EntidadBase("nombre","razon social",null);
        organizacion.agregarEntidad(entidadBase);
        CategoriaEntidad categoria1 = new CategoriaEntidad("categoria1",null);
        CategoriaEntidad categoria2 = new CategoriaEntidad("categoria2",null);
        CategoriaEntidad categoria3 = new CategoriaEntidad("categoria3",null);
        CategoriaEntidad categoria4 = new CategoriaEntidad("categoria4",null);
        organizacion.agregarCategoria(categoria1);
        organizacion.agregarCategoria(categoria2);
        organizacion.agregarCategoria(categoria3);
        organizacion.agregarCategoria(categoria4);
        entidadBase.agregarCategoria(categoria3);
        MonedaDTO pesoArgentino = new MonedaDTO(null,null,"Peso argentino");
        ValidadorDeMoneda validadorDeMoneda = new ValidadorDeMoneda(Arrays.asList(pesoArgentino));
        ServiceLocator.getInstance().setValidadorDeMoneda(validadorDeMoneda);
        Egreso egreso =  new Egreso(null, null, new ArrayList<>(),null,"Peso argentino");
        RepositorioDeEgresos.getInstance().agregar(egreso);
        withTransaction(() -> {
            try {
                RepositorioDeOrganizaciones.getInstance().agregar(organizacion);
                Usuario migue = new Usuario("migue","alta clave");
                RepositorioDeUsuarios.getInstance().agregar(migue);
                organizacion.agregarUsuario(migue);
                egreso.agregarRevisor(migue);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}
