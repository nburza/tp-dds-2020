package main;

import apiMercadoLibre.DTO.CiudadDTO;
import apiMercadoLibre.DTO.MonedaDTO;
import apiMercadoLibre.DTO.PaisDTO;
import apiMercadoLibre.DTO.ProvinciaDTO;
import apiMercadoLibre.ServiceLocator;
import apiMercadoLibre.ValidadorDeMoneda;
import apiMercadoLibre.ValidadorDeUbicacion;
import egreso.*;
import entidadOrganizativa.*;
import mediosDePago.*;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    public static void main(String[] args) { new Bootstrap().run();}

    public void run()
    {
        try
        {
            Usuario migue = new Usuario("migue","alta clave");
            Usuario aure = new Usuario("aure","altisima clave");

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

            MonedaDTO pesoArgentino = new MonedaDTO(null,null,"Dolar");
            ValidadorDeMoneda validadorDeMoneda = new ValidadorDeMoneda(Arrays.asList(pesoArgentino));
            ServiceLocator.getInstance().setValidadorDeMoneda(validadorDeMoneda);
            Egreso egreso =  new Egreso( null, null, new ArrayList<>(), LocalDate.now(),false,"Dolar");
            RepositorioDeEgresos.getInstance().agregar(egreso);

            Producto producto1 = new Producto("Resma Autor");
            Producto producto2 = new Producto("Resma Boreal");
            Producto producto3 = new Producto("Lapicera Bic");
            Producto producto4 = new Producto("Lapicera Maped");

            Item item1 = new Item(producto1, 500, "ARS");
            Item item2 = new Item(producto2, 450, "ARS");
            Item item3 = new Item(producto3, 40, "ARS");
            Item item4 = new Item(producto4, 60, "ARS");

            MedioDePago medio1 = new TarjetaCredito("Visa Débito","123456");
            MedioDePago medio2 = new TarjetaDebito("Mastercard Crédito","654321");
            MedioDePago medio3 = new Efectivo("Efectivo Tienda XX","000123");


            withTransaction(() ->
            {
                organizacion.agregarUsuario(migue);
                egreso.agregarRevisor(migue);

                RepositorioDeOrganizaciones.getInstance().agregar(organizacion);

                RepositorioDeItems.getInstance().agregar(item1);
                RepositorioDeItems.getInstance().agregar(item2);
                RepositorioDeItems.getInstance().agregar(item3);
                RepositorioDeItems.getInstance().agregar(item4);

                RepositorioDeMediosDePago.getInstance().agregar(medio1);
                RepositorioDeMediosDePago.getInstance().agregar(medio2);
                RepositorioDeMediosDePago.getInstance().agregar(medio3);

                RepositorioDeUsuarios.getInstance().agregar(migue);
                RepositorioDeUsuarios.getInstance().agregar(aure);

                ValidadorDeEgresos.getInstance().validarTodos();
            });
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}