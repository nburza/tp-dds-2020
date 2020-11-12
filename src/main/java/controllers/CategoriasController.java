package controllers;

import entidadOrganizativa.*;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CategoriasController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {
    public ModelAndView showCategorias(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        if(!RepositorioDeUsuarios.estaLogueado(request, response)){
            response.redirect("/login");
        }else{
            viewModel.put("idOrganizacion", getOrganizacion(request).getId());
            viewModel.put("categorias", getOrganizacion(request).getCategorias());
        }
        return new ModelAndView(viewModel, "categorias.hbs");
    }

    public ModelAndView showFormularioNuevaCategoria(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        if(!RepositorioDeUsuarios.estaLogueado(request, response)){
            response.redirect("/login");
        }
        else {
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Crear categoria");
            //       viewModel.put("reglas", getOrganizacion(request).getCategorias());
        }
        return new ModelAndView(viewModel, "nuevaCategoria.hbs");
    }
    public Void agregarCategoria(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();

        Entidad entidad;
        String nombre = request.queryParams("nombre");
        String regla1 = request.queryParams("reglaEntidadBaseNoIncorporable");
        String regla2 = request.queryParams("reglaProhibidoAgregarEntidadesBase");
        String regla3 = request.queryParams("reglaBloqueoEgresoPorMonto");
        String montoLimite = request.queryParams("montoLimite");
        CategoriaEntidad nuevaCategoria =new CategoriaEntidad(nombre);

        if(regla1=="on"){
            nuevaCategoria.agregarRegla(new ReglaEntidadBaseNoIncorporable());
        }
        if (regla2=="on"){
            nuevaCategoria.agregarRegla(new ReglaProhibidoAgregarEntidadesBase());
        }
        if (regla3=="on"){
            nuevaCategoria.agregarRegla(new ReglaBloqueoEgresoPorMonto(new BigDecimal(montoLimite)));
        }
        Organizacion organizacion = getOrganizacion(request);
        withTransaction(() -> organizacion.agregarCategoria(nuevaCategoria));
        response.redirect("/categorias");
        return null;
    }

    private Organizacion getOrganizacion(Request request) {
        Long idUsuario = request.session().attribute("idUsuario");
        return RepositorioDeOrganizaciones.getInstance().getOrganizacionDelUsuarioConId(idUsuario).get();
    }

}
