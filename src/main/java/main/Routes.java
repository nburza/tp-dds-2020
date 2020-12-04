package main;

import apiMercadoLibre.DTO.CiudadDTO;
import apiMercadoLibre.DTO.PaisDTO;
import apiMercadoLibre.DTO.ProvinciaDTO;
import apiMercadoLibre.ServiceLocator;
import apiMercadoLibre.ServicioAPIMercadoLibre;
import apiMercadoLibre.ValidadorDeMoneda;
import apiMercadoLibre.ValidadorDeUbicacion;
import controllers.*;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Arrays;

public class Routes {
    public static void main(String[] args) {
        Spark.port(getHerokuAssignedPort());
        Spark.staticFileLocation("/public");

        CiudadDTO ciudad = new CiudadDTO("3","La Plata");
        ProvinciaDTO provincia = new ProvinciaDTO("2","Buenos Aires", Arrays.asList(ciudad));
        PaisDTO pais = new PaisDTO("1","Argentina", Arrays.asList(provincia));
        ValidadorDeUbicacion validadorDeUbicacion = new ValidadorDeUbicacion(Arrays.asList(pais));

        ServicioAPIMercadoLibre servicioAPIMercadoLibre = new ServicioAPIMercadoLibre();
        ValidadorDeMoneda validadorDeMoneda = new ValidadorDeMoneda(servicioAPIMercadoLibre.getMonedas());
        //ValidadorDeUbicacion validadorDeUbicacion = new ValidadorDeUbicacion(servicioAPIMercadoLibre.getUbicaciones());
        ServiceLocator.getInstance().setValidadorDeUbicacion(validadorDeUbicacion);
        ServiceLocator.getInstance().setValidadorDeMoneda(validadorDeMoneda);
        //new Bootstrap().run();

        HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
        LoginController loginController = new LoginController();
        HomeController homeController = new HomeController();
        EntidadesController entidadesController = new EntidadesController();
        MensajesController mensajesController = new MensajesController();
        EgresosController egresosController = new EgresosController();
        CategoriasController categoriasController = new CategoriasController();
        UsuarioController usuarioController = new UsuarioController();

        Spark.get("/", homeController::index);

        Spark.get("/home", homeController::show, engine);
        Spark.get("/login", loginController::show, engine);
        Spark.post("/login", loginController::login, engine);
        Spark.get("/logout", loginController::logout);

        Spark.get("/entidades", entidadesController::showEntidades, engine);
        Spark.get("/entidades/nueva", entidadesController::showFormularioNuevaEntidad, engine);
        Spark.get("/entidades/:id/entidadesBase/asignado", entidadesController::showAsignarEntidadesBase, engine);
        Spark.post("/entidades/:id/entidadesBase", entidadesController::asignarEntidadesBase, engine);
        Spark.get("/mensajes/pag/:numPag",mensajesController::showMensajes, engine);
        Spark.post("/entidades", entidadesController::agregarEntidad,engine);
        Spark.get("/egresos/nuevo", egresosController::showEgresos, engine);
        Spark.post("/egresos", egresosController::altaEgresos, engine);

        Spark.get("/categorias",categoriasController::showCategorias,engine);
        Spark.get("/categorias/nueva",categoriasController::showFormularioNuevaCategoria,engine);
        Spark.post("/categorias", categoriasController::agregarCategoria, engine);

        Spark.get("/entidades/:id/categorias/asignado",entidadesController::showFormularioAsignarCategoria,engine);
        Spark.post("/entidades/:id/categorias",entidadesController::agregarCategoriaAEntidad, engine);
        Spark.get("/usuarios/nuevo", usuarioController::showAgregarUsuario, engine);
        Spark.post("/usuarios", usuarioController::agregarUsuario, engine);

        Spark.after((request, response) -> {
            if(PerThreadEntityManagers.getEntityManager().getTransaction().isActive()) {
                PerThreadEntityManagers.getEntityManager().flush();
            }
            PerThreadEntityManagers.closeEntityManager();
        });

        /*Spark.before((request, response) -> {
            if (!PerThreadEntityManagers.getEntityManager().isJoinedToTransaction())
                PerThreadEntityManagers.getEntityManager().getTransaction().begin();
        });*/
    }
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 8088;
    }
}
