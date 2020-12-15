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

import java.util.*;
import java.util.stream.Collectors;

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
                entidades = organizacion.getEntidadesPorCategoria(categoriaFiltrada);
                Collections.swap(categorias, 0 , categorias.indexOf(organizacion.getLaCategoria(categoriaFiltrada)));
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
        inputs.put("nombreFicticio", request.queryParams("nombreFicticio"));
        inputs.put("razonSocial", request.queryParams("razonSocial"));
        inputs.put("tipoEntidad", request.queryParams("tipoEntidad"));
        inputs.putObligatorioSi("cuit", request.queryParams("cuit"), inputs.get("tipoEntidad").equals("juridica"));
        inputs.putObligatorioSi("pais", request.queryParams("pais"), inputs.get("tipoEntidad").equals("juridica"));
        inputs.putObligatorioSi("provincia", request.queryParams("provincia"), inputs.get("tipoEntidad").equals("juridica"));
        inputs.putObligatorioSi("ciudad", request.queryParams("ciudad"), inputs.get("tipoEntidad").equals("juridica"));
        inputs.putObligatorioSi("direccion", request.queryParams("direccion"), inputs.get("tipoEntidad").equals("juridica"));
        inputs.putObligatorioSi("codigoIgj", request.queryParams("codigoIgj"), inputs.get("tipoEntidad").equals("juridica"));
        inputs.putObligatorioSi("tipoEntidadJuridica", request.queryParams("tipoEntidadJuridica"), inputs.get("tipoEntidad").equals("juridica"));
        inputs.putObligatorioSi("categoriaEmpresa", request.queryParams("categoriaEmpresa"),
                inputs.get("tipoEntidad").equals("juridica") && inputs.get("tipoEntidadJuridica").equals("empresa"));

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

        List<String> categorias = Arrays.asList(request.queryMap("categorias").values());

        Organizacion organizacion = utils.getOrganizacion(request);

        EntidadBuilder entidadBuilder = new EntidadBuilder();
        entidadBuilder.setNombreFicticio(inputs.get("nombreFicticio"));
        entidadBuilder.setRazonSocial(inputs.get("razonSocial"));
        entidadBuilder.setTipoEntidad(inputs.get("tipoEntidad"));
        entidadBuilder.setCuit(inputs.get("cuit"));
        entidadBuilder.setPais(inputs.get("pais"));
        entidadBuilder.setProvincia(inputs.get("provincia"));
        entidadBuilder.setCiudad(inputs.get("ciudad"));
        entidadBuilder.setDireccion(inputs.get("direccion"));
        entidadBuilder.setCodigoIgj(inputs.get("codigoIgj"));
        entidadBuilder.setTipoEntidadJuridica(inputs.get("tipoEntidadJuridica"));
        entidadBuilder.setCategoriaEmpresa(inputs.get("categoriaEmpresa"));

        try {
            Entidad entidad = entidadBuilder.crear();
            List<CategoriaEntidad> categoriasSeleccionadas = parsearCategoriasSeleccionadas(categorias, organizacion);
            withTransaction(() -> {
                organizacion.agregarEntidad(entidad);
                categoriasSeleccionadas.forEach(entidad::agregarCategoria);
            });
        }
        catch(DireccionInvalidaException exception) {
            viewModel.cargarDatosGenerales(request,"Crear entidad");
            viewModel.agregarMensajeDeError("La direccion ingresada es incorrecta. Ingrese nuevamente.");
            viewModel.rellenarDatosAnteError(inputs);
            viewModel.put("categoriasEntidad", utils.getOrganizacion(request).getCategorias());
            return new ModelAndView(viewModel.getViewModel(), "nuevaEntidad.hbs");
        }
        viewModel.cargarDatosGenerales(request,"Entidades");
        viewModel.agregarMensajeDeExito("La entidad " + inputs.get("nombreFicticio") + " fue agregada con éxito.");
        viewModel.put("categorias", organizacion.getCategorias());
        viewModel.put("entidades", organizacion.getEntidades());
        return new ModelAndView(viewModel.getViewModel(), "entidades.hbs");
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
        List<String> entidadesBase = Arrays.asList(request.queryMap("entidadesSeleccionadas").values());
        String idEntidadJuridica = request.params(":id");
        Organizacion organizacion = utils.getOrganizacion(request);
        EntidadJuridica entidadJuridica = (EntidadJuridica) organizacion.getEntidades().stream().filter(e -> idEntidadJuridica.equals(e.getId().toString())).findFirst().get();
        List<Entidad> entidadesBaseSeleccionadas = parsearEntidadesBaseSeleccionadas(entidadesBase, organizacion);
        withTransaction(() -> {
            organizacion.asignarEntidadesBaseAUnaJuridica(entidadesBaseSeleccionadas,entidadJuridica);
        });
        viewModel.cargarDatosGenerales(request,"Entidades");
        viewModel.agregarMensajeDeExito("La asignación fue realizada con éxito.");
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

    private List<CategoriaEntidad> parsearCategoriasSeleccionadas(List<String> categoriasSeleccionadas, Organizacion organizacion) {
        List<CategoriaEntidad> categoriasOrganizacion = organizacion.getCategorias();
        return categoriasOrganizacion
                .stream().filter(co -> categoriasSeleccionadas
                .stream().anyMatch(cs -> cs.equals(co.getNombre())))
                .collect(Collectors.toList());
    }

    private List<Entidad> parsearEntidadesBaseSeleccionadas(List<String> entidadesBaseSeleccionadas, Organizacion organizacion) {
        List<Entidad> entidadesBaseOrganizacion = organizacion.getEntidadesBaseAsignables();
        return entidadesBaseOrganizacion
                .stream().filter(ebo -> entidadesBaseSeleccionadas
                .stream().anyMatch(ebs -> ebs.equals(ebo.getId().toString())))
                .collect(Collectors.toList());
    }

    public ModelAndView agregarCategoriaAEntidad(Request request, Response response) {
        ViewModelTuneado viewModel = new ViewModelTuneado();
        Organizacion organizacion = utils.getOrganizacion(request);

        String idEntidad = request.params(":id");

        DiccionarioDeInputs inputs = new DiccionarioDeInputs();
        inputs.putObligatorio("categoriaSeleccionada", request.queryParams("categoriaSeleccionada"));
        //String categoria = request.queryParams("categoriaSeleccionada");

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
        CategoriaEntidad laCategoria = organizacion.getCategorias().stream().filter(categoriaEntidad -> categoriaEntidad.getNombre().equals(inputs.get("categoriaSeleccionada"))).findFirst().get();
        withTransaction(()->laEntidad.agregarCategoria(laCategoria));

        viewModel.cargarDatosGenerales(request,"Entidades");
        viewModel.agregarMensajeDeExito("La categoria " + inputs.get("categoriaSeleccionada") + " fue asignada a la entidad " + laEntidad.getNombreFicticio() + " con éxito.");
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
