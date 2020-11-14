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
import usuario.Administrador;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.math.BigDecimal;
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
            Usuario nico = new Usuario("nico","altisima clave");
            Usuario charly = new Usuario("charly","altisima clave");
            Usuario mati = new Usuario("mati","altisima clave");
            Administrador admin = new Administrador("admin","alta clave");

            CiudadDTO ciudad = new CiudadDTO("3","La Plata");
            ProvinciaDTO provincia = new ProvinciaDTO("2","Buenos Aires", Arrays.asList(ciudad));
            PaisDTO pais = new PaisDTO("1","Argentina", Arrays.asList(provincia));
            ValidadorDeUbicacion validadorDeUbicacion = new ValidadorDeUbicacion(Arrays.asList(pais));
            ServiceLocator.getInstance().setValidadorDeUbicacion(validadorDeUbicacion);

            Organizacion organizacion1 = new Organizacion(new ArrayList<>(), new ArrayList<>());
            Organizacion organizacion2 = new Organizacion(new ArrayList<>(), new ArrayList<>());
            Organizacion organizacion3 = new Organizacion(new ArrayList<>(), new ArrayList<>());
            Entidad entidadBase1 = new EntidadBase("nombre1","razon social1",null);
            Entidad entidadBase2 = new EntidadBase("nombre2","razon social2",null);
            Entidad entidadBase3 = new EntidadBase("nombre3","razon social3",null);
            organizacion1.agregarEntidad(entidadBase1);
            organizacion2.agregarEntidad(entidadBase2);
            organizacion3.agregarEntidad(entidadBase3);

            CategoriaEntidad categoria1 = new CategoriaEntidad("categoria1",null);
            CategoriaEntidad categoria2 = new CategoriaEntidad("categoria2",null);
            CategoriaEntidad categoria3 = new CategoriaEntidad("categoria3",null);
            CategoriaEntidad categoria4 = new CategoriaEntidad("categoria4",null);

            organizacion1.agregarCategoria(categoria1);
            organizacion1.agregarCategoria(categoria2);
            organizacion2.agregarCategoria(categoria3);
            organizacion2.agregarCategoria(categoria4);
            organizacion3.agregarCategoria(categoria4);

            entidadBase1.agregarCategoria(categoria1);
            entidadBase2.agregarCategoria(categoria2);
            entidadBase3.agregarCategoria(categoria3);

            MonedaDTO pesoArgentino = new MonedaDTO(null,null,"Dolar");
            ValidadorDeMoneda validadorDeMoneda = new ValidadorDeMoneda(Arrays.asList(pesoArgentino));
            ServiceLocator.getInstance().setValidadorDeMoneda(validadorDeMoneda);
            Egreso egreso =  new Egreso( null, null, new ArrayList<>(), LocalDate.now(),false,"Dolar");
            RepositorioDeEgresos.getInstance().agregar(egreso);

            Producto producto1 = new Producto("Resma Autor");
            Producto producto2 = new Producto("Resma Boreal");
            Producto producto3 = new Producto("Lapicera Bic");
            Producto producto4 = new Producto("Lapicera Maped");

            Item item1 = new Item(producto1, 1, "Peso Argentino", new BigDecimal(500));
            Item item2 = new Item(producto2, 2, "Peso Argentino", new BigDecimal(450));
            Item item3 = new Item(producto3, 3, "Peso Argentino", new BigDecimal(40));
            Item item4 = new Item(producto4, 4, "Peso Argentino", new BigDecimal(60));

            MedioDePago medio1 = new TarjetaCredito("Visa Débito","123456");
            MedioDePago medio2 = new TarjetaDebito("Mastercard Crédito","654321");
            MedioDePago medio3 = new Efectivo("Efectivo Tienda XX","000123");

            Etiqueta etiqueta1 = new Etiqueta("materia prima");
            Etiqueta etiqueta2 = new Etiqueta("maquinaria");
            Etiqueta etiqueta3 = new Etiqueta("construcción");

            withTransaction(() ->
            {
                organizacion1.agregarUsuario(migue);
                organizacion1.agregarUsuario(charly);
                organizacion2.agregarUsuario(aure);
                organizacion2.agregarUsuario(nico);
                organizacion3.agregarUsuario(mati);
                egreso.agregarRevisor(migue);

                RepositorioDeOrganizaciones.getInstance().agregar(organizacion1);
                RepositorioDeOrganizaciones.getInstance().agregar(organizacion2);
                RepositorioDeOrganizaciones.getInstance().agregar(organizacion3);

                RepositorioDeItems.getInstance().agregar(item1);
                RepositorioDeItems.getInstance().agregar(item2);
                RepositorioDeItems.getInstance().agregar(item3);
                RepositorioDeItems.getInstance().agregar(item4);

                RepositorioDeMediosDePago.getInstance().agregar(medio1);
                RepositorioDeMediosDePago.getInstance().agregar(medio2);
                RepositorioDeMediosDePago.getInstance().agregar(medio3);

                RepositorioDeUsuarios.getInstance().agregar(migue);
                RepositorioDeUsuarios.getInstance().agregar(aure);
                RepositorioDeUsuarios.getInstance().agregar(nico);
                RepositorioDeUsuarios.getInstance().agregar(charly);
                RepositorioDeUsuarios.getInstance().agregar(mati);
                RepositorioDeUsuarios.getInstance().agregar(admin);

                RepositorioDeEtiquetas.getInstance().agregar(etiqueta1);
                RepositorioDeEtiquetas.getInstance().agregar(etiqueta2);
                RepositorioDeEtiquetas.getInstance().agregar(etiqueta3);

                ValidadorDeEgresos.getInstance().validarTodos();
            });
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}