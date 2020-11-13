package main;

import controllers.EgresosController;
import controllers.EntidadesController;
import controllers.HomeController;
import controllers.LoginController;
import controllers.MensajesController;
import controllers.CategoriasController;
import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Routes {
    public static void main(String[] args) {
        Spark.port(8088);
        Spark.staticFileLocation("/public");

        new Bootstrap().run();

        HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
        LoginController loginController = new LoginController();
        HomeController homeController = new HomeController();
        EntidadesController entidadesController = new EntidadesController();
        MensajesController mensajesController = new MensajesController();
        EgresosController egresosController = new EgresosController();
        CategoriasController categoriasController = new CategoriasController();

        Spark.get("/", homeController::index);

        Spark.get("/home",(request, response) ->  homeController.show(request, response), engine);
        Spark.get("/login", (request, response) -> loginController.show(request, response), engine);
        Spark.post("/login", (request, response) -> loginController.login(request, response), engine);
        Spark.get("/logout", (request, response) -> loginController.logout(request, response));

        Spark.get("/entidades", entidadesController::showEntidades, engine);
        Spark.get("/entidades/nueva", entidadesController::showFormularioNuevaEntidad, engine);
        Spark.get("/entidades/:id/entidadesBase/asignar", entidadesController::showAsignarEntidadesBase, engine);
        Spark.post("/entidades/:id/entidadesBase", entidadesController::asignarEntidadesBase);
        Spark.get("/mensajes",mensajesController::showMensajes, engine);
        Spark.post("/entidades", entidadesController::agregarEntidad,engine);
        Spark.get("/altaEgresos", (request, response) -> egresosController.showEgresos(request, response), engine);
        Spark.post("/altaEgresos", (request, response) -> egresosController.altaEgresos(request, response), engine);

        Spark.get("/categorias",categoriasController::showCategorias,engine);
        Spark.get("/categorias/nueva",categoriasController::showFormularioNuevaCategoria,engine);
        Spark.post("/categorias", categoriasController::agregarCategoria);

        Spark.get("/entidades/asignarCategoria",entidadesController::showFormularioAsignarCategoria,engine);
        Spark.post("/entidades/asignarCategoria",entidadesController::agregarCategoriaAEntidad);
    }
}
