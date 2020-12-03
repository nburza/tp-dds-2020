package controllers;

import entidadOrganizativa.*;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriasController extends ControllerGenerico implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    public ModelAndView showCategorias(Request req, Response res) {
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            Map<String, Object> viewModel = new HashMap<String, Object>();
            Organizacion organizacion = getOrganizacion(request);
            List<CategoriaEntidad> categorias = organizacion.getCategorias();
            this.cargarDatosGeneralesA(viewModel,request,"Categorias");
            viewModel.put("nombreOrganizacion", organizacion.getNombre());
            viewModel.put("categorias", categorias);
            if(!categorias.isEmpty())
            {
                viewModel.put("hayResultados", true);
            }
            return new ModelAndView(viewModel, "categorias.hbs");
        });
    }

    public ModelAndView showFormularioNuevaCategoria(Request req, Response res) {
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            Map<String, Object> viewModel = new HashMap<String, Object>();
            this.cargarDatosGeneralesA(viewModel,request,"Crear categoria");
            //       viewModel.put("reglas", getOrganizacion(request).getCategorias());
            return new ModelAndView(viewModel, "nuevaCategoria.hbs");
        });
    }
    public ModelAndView agregarCategoria(Request request, Response response) {
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

        this.cargarDatosGeneralesA(viewModel,request,"Categorias");
        viewModel.put("nombreOrganizacion", organizacion.getNombre());
        viewModel.put("categorias", organizacion.getCategorias());
        this.agregarMensajeDeExitoA(viewModel, "la categoria " + nombre + " fue ingresado con éxito.");
        return new ModelAndView(viewModel, "nuevaCategoria.hbs");
    }
}
