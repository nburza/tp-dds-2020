package controllers;

import apiMercadoLibre.exceptions.DireccionInvalidaException;
import entidadOrganizativa.*;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import presupuesto.DireccionPostal;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EntidadesController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    public ModelAndView showEntidades(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();

        if(!RepositorioDeUsuarios.estaLogueado(request, response)){
            response.redirect("/login");
        }
        else {
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Entidades");
            viewModel.put("nombreUsuario", RepositorioDeUsuarios.getUsuarioLogueado(request).getNombreUsuario());
            viewModel.put("idOrganizacion", getOrganizacion(request).getId());
            viewModel.put("categorias", getOrganizacion(request).getCategorias());
            String categoriaFiltrada = request.queryParams("categoriaFiltrada");
            if(categoriaFiltrada == null)
                viewModel.put("entidades", getOrganizacion(request).getEntidades());
            else
                viewModel.put("entidades", getOrganizacion(request).getEntidadesPorCategoria(categoriaFiltrada));
        }
        return new ModelAndView(viewModel, "entidades.hbs");
    }

    public ModelAndView showFormularioNuevaEntidad(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        if(!RepositorioDeUsuarios.estaLogueado(request, response)){
            response.redirect("/login");
        }
        else {
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Crear entidad");
            viewModel.put("nombreUsuario", RepositorioDeUsuarios.getUsuarioLogueado(request).getNombreUsuario());
            viewModel.put("categoriasEntidad", getOrganizacion(request).getCategorias());
        }
        return new ModelAndView(viewModel, "nuevaEntidad.hbs");
    }

    public ModelAndView agregarEntidad(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        viewModel.put("nombreUsuario", RepositorioDeUsuarios.getUsuarioLogueado(request).getNombreUsuario());
        viewModel.put("anio", LocalDate.now().getYear());
        Entidad entidad;
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
        try {
            if (tipoEntidad.equals("base")) {
                entidad = new EntidadBase(nombreFicticio, razonSocial, null);
            } else {
                DireccionPostal direccionPostal = new DireccionPostal(pais, provincia, ciudad, direccion);
                if (tipoEntidadJuridica.equals("oss")) {
                    entidad = new OrganizacionSectorSocial(razonSocial, nombreFicticio, Integer.parseInt(cuit), direccionPostal, Integer.valueOf(codigoIgj), null);
                } else {
                    entidad = new Empresa(razonSocial, nombreFicticio, Integer.parseInt(cuit), direccionPostal, Integer.valueOf(codigoIgj), parsearCategoriaEmpresa(categoriaEmpresa), null);
                }
            }
            Organizacion organizacion = getOrganizacion(request);
            List<CategoriaEntidad> categoriasSeleccionadas = parsearCategoriasSeleccionadas(categorias, organizacion);
            withTransaction(() -> {
                organizacion.agregarEntidad(entidad);
                categoriasSeleccionadas.forEach(c -> entidad.agregarCategoria(c));
            });
        }
        catch(DireccionInvalidaException exception) {
            viewModel.put("titulo", "Crear entidad");
            viewModel.put("mensaje", true);
            viewModel.put("tipoMensaje", "danger");
            viewModel.put("tituloMensaje", "Error!");
            viewModel.put("textoMensaje", "La direccion ingresada es incorrecta. Ingrese nuevamente.");
            viewModel.put("categoriasEntidad", getOrganizacion(request).getCategorias());
            return new ModelAndView(viewModel, "nuevaEntidad.hbs");
        }
        viewModel.put("titulo", "Entidades");
        viewModel.put("mensaje", true);
        viewModel.put("tipoMensaje", "success");
        viewModel.put("tituloMensaje", "Exito!");
        viewModel.put("textoMensaje", "La entidad " + nombreFicticio + " fue agregada con éxito.");
        viewModel.put("idOrganizacion", getOrganizacion(request).getId());
        viewModel.put("categorias", getOrganizacion(request).getCategorias());
        String categoriaFiltrada = request.queryParams("categoriaFiltrada");
        if(categoriaFiltrada == null)
            viewModel.put("entidades", getOrganizacion(request).getEntidades());
        else
            viewModel.put("entidades", getOrganizacion(request).getEntidadesPorCategoria(categoriaFiltrada));
        return new ModelAndView(viewModel, "entidades.hbs");
    }

    public ModelAndView showAsignarEntidadesBase(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        if(!RepositorioDeUsuarios.estaLogueado(request, response)){
            response.redirect("/login");
        }
        else {
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Asignar entidades");
            viewModel.put("nombreUsuario", RepositorioDeUsuarios.getUsuarioLogueado(request).getNombreUsuario());
            viewModel.put("idEntidad", "2");
            viewModel.put("entidadesBase", getOrganizacion(request).getEntidadesBaseAsignables());
        }
        return new ModelAndView(viewModel, "asignarEntidadesBase.hbs");
    }

    public Void asignarEntidadesBase(Request request, Response response) {
        List<String> entidadesBase = Arrays.asList(request.queryMap("entidadesSeleccionadas").values());
        String idEntidadJuridica = request.params(":id");
        Organizacion organizacion = getOrganizacion(request);
        EntidadJuridica entidadJuridica = (EntidadJuridica) organizacion.getEntidades().stream().filter(e -> idEntidadJuridica.equals(e.getId().toString())).findFirst().get();
        List<Entidad> entidadesBaseSeleccionadas = parsearEntidadesBaseSeleccionadas(entidadesBase, organizacion);
        withTransaction(() -> {
            organizacion.asignarEntidadesBaseAUnaJuridica(entidadesBaseSeleccionadas,entidadJuridica);
        });
        response.redirect("/entidades");
        return null;
    }

    private CategoriaEmpresa parsearCategoriaEmpresa(String categoriaEmpresa) {
        Hashtable<String, CategoriaEmpresa> DiccionarioDeCategorias = new Hashtable<>();
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

    private Organizacion getOrganizacion(Request request) {
        Long idUsuario = request.session().attribute("idUsuario");
        return RepositorioDeOrganizaciones.getInstance().getOrganizacionDelUsuarioConId(idUsuario).get();
    }

    public Void agregarCategoriaAEntidad(Request request, Response response) {
        String entidad = request.queryParams("entidadSeleccionada");
        String categoria = request.queryParams("categoriaSeleccionada");
        Entidad laEntidad = getOrganizacion(request).getEntidades().stream().filter(entidad1->entidad1.getNombreFicticio().equals(entidad)).findFirst().get();
        CategoriaEntidad laCategoria = getOrganizacion(request).getCategorias().stream().filter(categoriaEntidad -> categoriaEntidad.getNombre().equals(categoria)).findFirst().get();
        withTransaction(()->laEntidad.agregarCategoria(laCategoria));
        response.redirect("/entidades");
        return null;
    }

    public ModelAndView showFormularioAsignarCategoria(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        if(!RepositorioDeUsuarios.estaLogueado(request, response)){
            response.redirect("/login");
        }
        else {
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Asignar categorias");
            viewModel.put("nombreUsuario", RepositorioDeUsuarios.getUsuarioLogueado(request).getNombreUsuario());
            viewModel.put("entidades", getOrganizacion(request).getEntidades());
            viewModel.put("categorias",getOrganizacion(request).getCategorias());
        }
        return new ModelAndView(viewModel, "agregarCategoria.hbs");
    }
}
