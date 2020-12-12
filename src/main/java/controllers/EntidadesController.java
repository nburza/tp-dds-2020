package controllers;

import apiMercadoLibre.exceptions.DireccionInvalidaException;
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

    public ModelAndView showEntidades(Request req, Response res) {
        return ejecutarConControlDeLogin(req, res, (request, response) ->
        {
            Map<String, Object> viewModel = new HashMap<String, Object>();
            this.cargarDatosGeneralesA(viewModel, request,"Entidades");
            Organizacion organizacion = getOrganizacion(request);
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
            return new ModelAndView(viewModel, "entidades.hbs");
        });
    }

    public ModelAndView showFormularioNuevaEntidad(Request req, Response res) {
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            Map<String, Object> viewModel = new HashMap<String, Object>();
            this.cargarDatosGeneralesA(viewModel,request,"Crear entidad");
            viewModel.put("categoriasEntidad", getOrganizacion(request).getCategorias());
            return new ModelAndView(viewModel, "nuevaEntidad.hbs");
        });
    }

    public ModelAndView agregarEntidad(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();

        String nombreFicticio = request.queryParams("nombreFicticio");
        String razonSocial = request.queryParams("razonSocial");
        List<String> categorias = Arrays.asList(request.queryMap("categorias").values());
        String tipoEntidad = request.queryParams("tipoEntidad");
        String cuit = request.queryParams("cuit");
        String pais = request.queryParams("pais");
        String provincia = request.queryParams("provincia");
        String ciudad = request.queryParams("ciudad");
        String direccion = request.queryParams("direccion");
        String codigoIgj = request.queryParams("codigoIgj");
        String tipoEntidadJuridica = request.queryParams("tipoEntidadJuridica");
        String categoriaEmpresa = request.queryParams("categoriaEmpresa");

        Organizacion organizacion = getOrganizacion(request);

        EntidadBuilder entidadBuilder = new EntidadBuilder();
        entidadBuilder.setNombreFicticio(nombreFicticio);
        entidadBuilder.setRazonSocial(razonSocial);
        entidadBuilder.setTipoEntidad(tipoEntidad);
        entidadBuilder.setCuit(cuit);
        entidadBuilder.setPais(pais);
        entidadBuilder.setProvincia(provincia);
        entidadBuilder.setCiudad(ciudad);
        entidadBuilder.setDireccion(direccion);
        entidadBuilder.setCodigoIgj(codigoIgj);
        entidadBuilder.setTipoEntidadJuridica(tipoEntidadJuridica);
        entidadBuilder.setCategoriaEmpresa(categoriaEmpresa);

        try {
            Entidad entidad = new EntidadBuilder().crear();
            List<CategoriaEntidad> categoriasSeleccionadas = parsearCategoriasSeleccionadas(categorias, organizacion);
            withTransaction(() -> {
                organizacion.agregarEntidad(entidad);
                categoriasSeleccionadas.forEach(entidad::agregarCategoria);
            });
        }
        catch(DireccionInvalidaException exception) {
            this.cargarDatosGeneralesA(viewModel,request,"Crear entidad");
            this.agregarMensajeDeErrorA(viewModel,"La direccion ingresada es incorrecta. Ingrese nuevamente.");
            viewModel.put("nombreFicticioValue", nombreFicticio);
            viewModel.put("razonSocialValue", razonSocial);
            viewModel.put("cuitValue", cuit);
            viewModel.put("paisValue", pais);
            viewModel.put("ciudadValue", ciudad);
            viewModel.put("provinciaValue", provincia);
            viewModel.put("direccionValue", direccion);
            viewModel.put("codigoIgjValue", codigoIgj);

            viewModel.put("categoriasEntidad", getOrganizacion(request).getCategorias());
            return new ModelAndView(viewModel, "nuevaEntidad.hbs");
        }
        this.cargarDatosGeneralesA(viewModel,request,"Entidades");
        this.agregarMensajeDeExitoA(viewModel,"La entidad " + nombreFicticio + " fue agregada con éxito.");
        viewModel.put("categorias", organizacion.getCategorias());
        viewModel.put("entidades", organizacion.getEntidades());
        return new ModelAndView(viewModel, "entidades.hbs");
    }

    public ModelAndView showAsignarEntidadesBase(Request req, Response res) {
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            Map<String, Object> viewModel = new HashMap<String, Object>();
            Organizacion organizacion = getOrganizacion(request);
            String idEntidad = request.params(":id");
            this.cargarDatosGeneralesA(viewModel,request,"Asignar entidades");
            viewModel.put("nombreEntidad", organizacion.getEntidadPorId(Long.parseLong(idEntidad)).getNombreFicticio());
            viewModel.put("idEntidad", idEntidad);
            viewModel.put("entidadesBase",organizacion.getEntidadesBaseAsignables());
            return new ModelAndView(viewModel, "asignarEntidadesBase.hbs");
        });
    }

    public ModelAndView asignarEntidadesBase(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        List<String> entidadesBase = Arrays.asList(request.queryMap("entidadesSeleccionadas").values());
        String idEntidadJuridica = request.params(":id");
        Organizacion organizacion = getOrganizacion(request);
        EntidadJuridica entidadJuridica = (EntidadJuridica) organizacion.getEntidades().stream().filter(e -> idEntidadJuridica.equals(e.getId().toString())).findFirst().get();
        List<Entidad> entidadesBaseSeleccionadas = parsearEntidadesBaseSeleccionadas(entidadesBase, organizacion);
        withTransaction(() -> {
            organizacion.asignarEntidadesBaseAUnaJuridica(entidadesBaseSeleccionadas,entidadJuridica);
        });
        this.cargarDatosGeneralesA(viewModel,request,"Entidades");
        this.agregarMensajeDeExitoA(viewModel,"La asignación fue realizada con éxito.");
        viewModel.put("categorias", organizacion.getCategorias());
        viewModel.put("entidades", organizacion.getEntidades());
        return new ModelAndView(viewModel, "entidades.hbs");
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
        Map<String, Object> viewModel = new HashMap<String, Object>();
        Organizacion organizacion = getOrganizacion(request);
        String idEntidad = request.params(":id");
        String categoria = request.queryParams("categoriaSeleccionada");
        Entidad laEntidad = organizacion.getEntidadPorId(Long.parseLong(idEntidad));
        CategoriaEntidad laCategoria = organizacion.getCategorias().stream().filter(categoriaEntidad -> categoriaEntidad.getNombre().equals(categoria)).findFirst().get();
        withTransaction(()->laEntidad.agregarCategoria(laCategoria));

        this.cargarDatosGeneralesA(viewModel,request,"Entidades");
        this.agregarMensajeDeExitoA(viewModel,"La categoria " + categoria + " fue asignada a la entidad " + laEntidad.getNombreFicticio() + " con éxito.");
        viewModel.put("categorias", organizacion.getCategorias());
        viewModel.put("entidades", organizacion.getEntidades());

        return new ModelAndView(viewModel, "entidades.hbs");
    }

    public ModelAndView showFormularioAsignarCategoria(Request req, Response res) {
        Organizacion organizacion = getOrganizacion(req);
        String idEntidad = req.params(":id");
        return ejecutarConControlDeLogin(req, res, (request, response) -> {
            Map<String, Object> viewModel = new HashMap<String, Object>();
            this.cargarDatosGeneralesA(viewModel,request,"Asignar categorias");
            viewModel.put("nombreEntidad", organizacion.getEntidadPorId(Long.parseLong(idEntidad)).getNombreFicticio());
            viewModel.put("idEntidad", idEntidad);
            viewModel.put("entidades", organizacion.getEntidades());
            viewModel.put("categorias", organizacion.getCategorias());
            return new ModelAndView(viewModel, "agregarCategoria.hbs");
        });
    }


}
