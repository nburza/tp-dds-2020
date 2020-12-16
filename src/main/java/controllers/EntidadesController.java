package controllers;

import apiMercadoLibre.exceptions.DireccionInvalidaException;
import diccionarioDeInputs.DatosFaltantesException;
import diccionarioDeInputs.DiccionarioDeInputs;
import entidadOrganizativa.*;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;

public class EntidadesController extends ControllerGenerico implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    private Utils utils = new Utils();

    public ModelAndView showEntidades(Request req, Response res) {
        return ejecutarConControlDeLogin(req, res, (request, response) ->
        {
            ViewModelTuneado viewModel = new ViewModelTuneado();
            viewModel.cargarDatosGenerales(request,"Entidades");
            Organizacion organizacion = utils.getOrganizacion(request);
            List<Entidad> entidades;
            List<CategoriaEntidad> categorias = organizacion.getCategorias();
            String categoriaFiltrada = request.queryParams("categoriaFiltrada");

            if(categoriaFiltrada == null)
            {
                entidades = organizacion.getEntidades();
            }else{
                viewModel.put("categoriaFiltrada", organizacion.getCategoriaPorId(Long.parseLong(categoriaFiltrada)));
                entidades = organizacion.getEntidadesPorCategoria(Long.parseLong(categoriaFiltrada));
            }
            if(!entidades.isEmpty())
            {
                viewModel.put("hayResultados", true);
            }
            viewModel.put("entidades", entidades);
            viewModel.put("categorias", categorias);
            return new ModelAndView(viewModel.getViewModel(), "entidades.hbs");
        });
    }

    public ModelAndView showFormularioNuevaEntidad(Request req, Response res) {
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            ViewModelTuneado viewModel = new ViewModelTuneado();
            viewModel.cargarDatosGenerales(request,"Crear entidad");
            viewModel.put("categoriasEntidad", utils.getOrganizacion(request).getCategorias());
            return new ModelAndView(viewModel.getViewModel(), "nuevaEntidad.hbs");
        });
    }

    public ModelAndView agregarEntidad(Request request, Response response) {
        ViewModelTuneado viewModel = new ViewModelTuneado();

        DiccionarioDeInputs inputs = new DiccionarioDeInputs();
        inputs.putSimple("nombreFicticio", request.queryParams("nombreFicticio"));
        inputs.putSimple("razonSocial", request.queryParams("razonSocial"));
        inputs.putMultiple("categorias", utils.getValuesComoLista(request,"categorias"));
        inputs.putSimple("tipoEntidad", request.queryParams("tipoEntidad"));
        inputs.putSimpleObligatorioSi("cuit", request.queryParams("cuit"), inputs.getSimple("tipoEntidad").equals("juridica"));
        inputs.putSimpleObligatorioSi("pais", request.queryParams("pais"), inputs.getSimple("tipoEntidad").equals("juridica"));
        inputs.putSimpleObligatorioSi("provincia", request.queryParams("provincia"), inputs.getSimple("tipoEntidad").equals("juridica"));
        inputs.putSimpleObligatorioSi("ciudad", request.queryParams("ciudad"), inputs.getSimple("tipoEntidad").equals("juridica"));
        inputs.putSimpleObligatorioSi("direccion", request.queryParams("direccion"), inputs.getSimple("tipoEntidad").equals("juridica"));
        inputs.putSimpleObligatorioSi("codigoIgj", request.queryParams("codigoIgj"), inputs.getSimple("tipoEntidad").equals("juridica"));
        inputs.putSimpleObligatorioSi("tipoEntidadJuridica", request.queryParams("tipoEntidadJuridica"), inputs.getSimple("tipoEntidad").equals("juridica"));
        inputs.putSimpleObligatorioSi("categoriaEmpresa", request.queryParams("categoriaEmpresa"),
                              inputs.getSimple("tipoEntidad").equals("juridica") &&
                                       inputs.getSimple("tipoEntidadJuridica").equals("empresa"));

        try {
            inputs.chequearDatosFaltantes();
        }
        catch(DatosFaltantesException e) {
            viewModel.cargarDatosGenerales(request,"Crear entidad");
            viewModel.agregarMensajeDeError(e.getMessage());
            viewModel.rellenarDatosAnteError(inputs);
            viewModel.put("categoriasEntidad", utils.getOrganizacion(request).getCategorias());
            return new ModelAndView(viewModel.getViewModel(),"nuevaEntidad.hbs");
        }

        Organizacion organizacion = utils.getOrganizacion(request);

        EntidadBuilder entidadBuilder = new EntidadBuilder();
        entidadBuilder.setNombreFicticio(inputs.getSimple("nombreFicticio"));
        entidadBuilder.setRazonSocial(inputs.getSimple("razonSocial"));
        entidadBuilder.setTipoEntidad(inputs.getSimple("tipoEntidad"));
        entidadBuilder.setCuit(inputs.getSimple("cuit"));
        entidadBuilder.setPais(inputs.getSimple("pais"));
        entidadBuilder.setProvincia(inputs.getSimple("provincia"));
        entidadBuilder.setCiudad(inputs.getSimple("ciudad"));
        entidadBuilder.setDireccion(inputs.getSimple("direccion"));
        entidadBuilder.setCodigoIgj(inputs.getSimple("codigoIgj"));
        entidadBuilder.setTipoEntidadJuridica(inputs.getSimple("tipoEntidadJuridica"));
        entidadBuilder.setCategoriaEmpresa(parsearCategoriaEmpresa(inputs.getSimple("categoriaEmpresa")));

        try {
            Entidad entidad = entidadBuilder.crear();
            List<CategoriaEntidad> categoriasSeleccionadas = organizacion.getCategoriasPorListaDeIds(utils.parsearIds(inputs.getMultiple("categorias")));
            withTransaction(() -> {
                organizacion.agregarEntidad(entidad);
                categoriasSeleccionadas.forEach(entidad::agregarCategoria);
            });
        }
        catch(DireccionInvalidaException exception) {
            viewModel.cargarDatosGenerales(request,"Crear entidad");
            viewModel.agregarMensajeDeError("La direccion ingresada es incorrecta. Ingrese nuevamente.");
            viewModel.rellenarDatosAnteError(inputs);
            viewModel.put("categoriasEntidad", organizacion.getCategorias());
            return new ModelAndView(viewModel.getViewModel(), "nuevaEntidad.hbs");
        }
        viewModel.cargarDatosGenerales(request,"Entidades");
        viewModel.agregarMensajeDeExito("La entidad " + inputs.getSimple("nombreFicticio") + " fue agregada con éxito.");
        viewModel.put("categorias", organizacion.getCategorias());
        viewModel.put("entidades", organizacion.getEntidades());
        return new ModelAndView(viewModel.getViewModel(), "entidades.hbs");
    }

    private CategoriaEmpresa parsearCategoriaEmpresa(String categoriaEmpresa) {
        HashMap<String, CategoriaEmpresa> DiccionarioDeCategorias = new HashMap<>();
        DiccionarioDeCategorias.put("micro", CategoriaEmpresa.MICRO);
        DiccionarioDeCategorias.put("pequenia",CategoriaEmpresa.PEQUENIA);
        DiccionarioDeCategorias.put("medianaTramo1", CategoriaEmpresa.MEDIANA_TRAMO_1);
        DiccionarioDeCategorias.put("medianaTramo2", CategoriaEmpresa.MEDIANA_TRAMO_2);
        return DiccionarioDeCategorias.get(categoriaEmpresa);
    }

    public ModelAndView showAsignarEntidadesBase(Request req, Response res) {
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            ViewModelTuneado viewModel = new ViewModelTuneado();
            Organizacion organizacion = utils.getOrganizacion(request);
            String idEntidad = request.params(":id");
            viewModel.cargarDatosGenerales(request,"Asignar entidades");
            viewModel.put("nombreEntidad", organizacion.getEntidadPorId(Long.parseLong(idEntidad)).getNombreFicticio());
            viewModel.put("idEntidad", idEntidad);
            viewModel.put("entidadesBase",organizacion.getEntidadesBaseAsignables());
            return new ModelAndView(viewModel.getViewModel(), "asignarEntidadesBase.hbs");
        });
    }

    public ModelAndView asignarEntidadesBase(Request request, Response response) {
        ViewModelTuneado viewModel = new ViewModelTuneado();
        Organizacion organizacion = utils.getOrganizacion(request);

        DiccionarioDeInputs inputs = new DiccionarioDeInputs();
        inputs.putMultipleObligatorio("entidadesBase", utils.getValuesComoLista(request,"entidadesSeleccionadas"));
        String idEntidadJuridica = request.params(":id");
        try {
            inputs.chequearDatosFaltantes();
        }
        catch (DatosFaltantesException e) {
            viewModel.cargarDatosGenerales(request,"Asignar entidades");
            viewModel.agregarMensajeDeError("Debe seleccionar al menos una entidad a asignar");
            viewModel.put("nombreEntidad", organizacion.getEntidadPorId(Long.parseLong(idEntidadJuridica)).getNombreFicticio());
            viewModel.put("idEntidad", idEntidadJuridica);
            viewModel.put("entidadesBase",organizacion.getEntidadesBaseAsignables());
            return new ModelAndView(viewModel.getViewModel(), "asignarEntidadesBase.hbs");
        }

        EntidadJuridica entidadJuridica = organizacion.getEntidadJuridica(Long.parseLong(idEntidadJuridica));
        List<Entidad> entidadesBaseSeleccionadas = organizacion.getEntidadesBaseSeleccionadas(utils.parsearIds(inputs.getMultiple("entidadesBase")));
        withTransaction(() -> {
            organizacion.asignarEntidadesBaseAUnaJuridica(entidadesBaseSeleccionadas,entidadJuridica);
        });
        viewModel.cargarDatosGenerales(request,"Entidades");
        viewModel.agregarMensajeDeExito("La asignación fue realizada con éxito.");
        viewModel.put("categorias", organizacion.getCategorias());
        viewModel.put("entidades", organizacion.getEntidades());
        return new ModelAndView(viewModel.getViewModel(), "entidades.hbs");
    }

    public ModelAndView agregarCategoriaAEntidad(Request request, Response response) {
        ViewModelTuneado viewModel = new ViewModelTuneado();
        Organizacion organizacion = utils.getOrganizacion(request);

        String idEntidad = request.params(":id");

        DiccionarioDeInputs inputs = new DiccionarioDeInputs();
        inputs.putSimpleObligatorio("categoriaSeleccionada", request.queryParams("categoriaSeleccionada"));

        try {
            inputs.chequearDatosFaltantes();
        }
        catch(DatosFaltantesException e) {
            viewModel.cargarDatosGenerales(request,"Asignar categorias");
            viewModel.agregarMensajeDeError("Debe seleccionar una categoria");
            viewModel.put("nombreEntidad", organizacion.getEntidadPorId(Long.parseLong(idEntidad)).getNombreFicticio());
            viewModel.put("idEntidad", idEntidad);
            viewModel.put("categorias", organizacion.getCategorias());
            return new ModelAndView(viewModel.getViewModel(),"agregarCategoria.hbs");
        }

        Entidad laEntidad = organizacion.getEntidadPorId(Long.parseLong(idEntidad));
        CategoriaEntidad laCategoria = organizacion.getCategoriaPorId(Long.parseLong(inputs.getSimple("categoriaSeleccionada")));
        withTransaction(()->laEntidad.agregarCategoria(laCategoria));

        viewModel.cargarDatosGenerales(request,"Entidades");
        viewModel.agregarMensajeDeExito("La categoria " + inputs.getSimple("categoriaSeleccionada") + " fue asignada a la entidad " + laEntidad.getNombreFicticio() + " con éxito.");
        viewModel.put("categorias", organizacion.getCategorias());
        viewModel.put("entidades", organizacion.getEntidades());

        return new ModelAndView(viewModel.getViewModel(), "entidades.hbs");
    }

    public ModelAndView showFormularioAsignarCategoria(Request req, Response res) {
        Organizacion organizacion = utils.getOrganizacion(req);
        String idEntidad = req.params(":id");
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            ViewModelTuneado viewModel = new ViewModelTuneado();
            viewModel.cargarDatosGenerales(request,"Asignar categorias");
            viewModel.put("nombreEntidad", organizacion.getEntidadPorId(Long.parseLong(idEntidad)).getNombreFicticio());
            viewModel.put("idEntidad", idEntidad);
            viewModel.put("categorias", organizacion.getCategorias());
            return new ModelAndView(viewModel.getViewModel(), "agregarCategoria.hbs");
        });
    }


}
