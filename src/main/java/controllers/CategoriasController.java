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
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Categorias");
            viewModel.put("nombreUsuario", RepositorioDeUsuarios.getUsuarioLogueado(request).getNombreUsuario());
            viewModel.put("idOrganizacion", getOrganizacion(request).getId());
            viewModel.put("categorias", getOrganizacion(request).getCategorias());
            if(RepositorioDeUsuarios.getUsuarioLogueado(request).esAdmin())
            {
                viewModel.put("esAdmin",true);
            }
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
            viewModel.put("nombreUsuario", RepositorioDeUsuarios.getUsuarioLogueado(request).getNombreUsuario());
            if(RepositorioDeUsuarios.getUsuarioLogueado(request).esAdmin())
            {
                viewModel.put("esAdmin",true);
            }
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
        Organizacion organizacion = getOrganizacion(request);

        if(regla1!=null){
            nuevaCategoria.agregarRegla(new ReglaEntidadBaseNoIncorporable());
        }
        if (regla2!=null){
            nuevaCategoria.agregarRegla(new ReglaProhibidoAgregarEntidadesBase());
        }
        if (regla3!=null){
            nuevaCategoria.agregarRegla(new ReglaBloqueoEgresoPorMonto(new BigDecimal(montoLimite)));
        }
        withTransaction(() -> organizacion.agregarCategoria(nuevaCategoria));
        response.redirect("/categorias");
        return null;
    }

    private Organizacion getOrganizacion(Request request) {
        Long idUsuario = request.session().attribute("idUsuario");
        return RepositorioDeOrganizaciones.getInstance().getOrganizacionDelUsuarioConId(idUsuario).get();
    }

}
