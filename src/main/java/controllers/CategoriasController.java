package controllers;

import diccionarioDeInputs.DatosFaltantesException;
import diccionarioDeInputs.DiccionarioDeInputs;
import entidadOrganizativa.*;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.util.List;

public class CategoriasController extends ControllerGenerico implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    private Utils utils = new Utils();

    public ModelAndView showCategorias(Request req, Response res) {
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            ViewModelTuneado viewModel = new ViewModelTuneado();
            Organizacion organizacion = utils.getOrganizacion(request);
            List<CategoriaEntidad> categorias = organizacion.getCategorias();
            viewModel.cargarDatosGenerales(request,"Categorias");
            viewModel.put("categorias", categorias);
            if(!categorias.isEmpty())
            {
                viewModel.put("hayResultados", true);
            }
            return new ModelAndView(viewModel.getViewModel(), "categorias.hbs");
        });
    }

    public ModelAndView showFormularioNuevaCategoria(Request req, Response res) {
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            ViewModelTuneado viewModel = new ViewModelTuneado();
            viewModel.cargarDatosGenerales(request,"Crear categoria");
            //       viewModel.put("reglas", getOrganizacion(request).getCategorias());
            return new ModelAndView(viewModel.getViewModel(), "nuevaCategoria.hbs");
        });
    }
    public ModelAndView agregarCategoria(Request request, Response response) {
        ViewModelTuneado viewModel = new ViewModelTuneado();

        DiccionarioDeInputs inputs = new DiccionarioDeInputs();
        inputs.putSimple("nombre", request.queryParams("nombre"));
        inputs.putSimple("regla1", request.queryParams("reglaEntidadBaseNoIncorporable"));
        inputs.putSimple("regla2", request.queryParams("reglaProhibidoAgregarEntidadesBase"));
        inputs.putSimple("regla3", request.queryParams("reglaBloqueoEgresoPorMonto"));
        inputs.putSimpleObligatorioSi("montoLimite",request.queryParams("montoLimite"),inputs.getSimple("regla3") != null);

        try {
            inputs.chequearDatosFaltantes();
        }
        catch(DatosFaltantesException e) {
            viewModel.cargarDatosGenerales(request,"Crear categoria");
            viewModel.agregarMensajeDeError(e.getMessage());
            viewModel.rellenarDatosAnteError(inputs);
            return new ModelAndView(viewModel.getViewModel(),"nuevaCategoria.hbs");
        }

        CategoriaEntidad nuevaCategoria = new CategoriaEntidad(inputs.getSimple("nombre"));

        if(inputs.getSimple("regla1") != null){
            nuevaCategoria.agregarRegla(new ReglaEntidadBaseNoIncorporable());
        }
        if(inputs.getSimple("regla2") != null){
            nuevaCategoria.agregarRegla(new ReglaProhibidoAgregarEntidadesBase());
        }
        if(inputs.getSimple("regla3") != null){
            nuevaCategoria.agregarRegla(new ReglaBloqueoEgresoPorMonto(new BigDecimal(inputs.getSimple("montoLimite"))));
        }
        withTransaction(() -> {
            Organizacion organizacion = utils.getOrganizacion(request);
            organizacion.agregarCategoria(nuevaCategoria);
            viewModel.put("categorias", organizacion.getCategorias());
        });

        viewModel.cargarDatosGenerales(request,"Categorias");
        viewModel.agregarMensajeDeExito("la categoria " + inputs.getSimple("nombre") + " fue ingresada con éxito.");
        return new ModelAndView(viewModel.getViewModel(), "categorias.hbs");
    }
}
