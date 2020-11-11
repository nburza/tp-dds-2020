package controllers;

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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class EntidadesController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

    public ModelAndView showEntidades(Request request, Response response) {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        if(!RepositorioDeUsuarios.estaLogueado(request, response)){
            response.redirect("/login");
        }
        else {
            viewModel.put("anio", LocalDate.now().getYear());
            viewModel.put("titulo", "Entidades");
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
            viewModel.put("categorias", getOrganizacion(request).getCategorias());
        }
        return new ModelAndView(viewModel, "nuevaEntidad.hbs");
    }

    public Void agregarEntidad(Request request, Response response) {
        Entidad entidad;
        String nombreFicticio = request.queryParams("nombreFicticio");
        String razonSocial = request.queryParams("razonSocial");
        String tipoEntidad = request.queryParams("tipoEntidad");
        String cuit = request.queryParams("cuit");
        String pais = request.queryParams("pais");
        String provincia = request.queryParams("provincia");
        String ciudad = request.queryParams("ciudad");
        String direccion = request.queryParams("direccion");
        String codigoIgj = request.queryParams("codigoIgj");
        String tipoEntidadJuridica = request.queryParams("tipoEntidadJuridica");
        String categoriaEmpresa = request.queryParams("categoriaEmpresa");
        if(tipoEntidad.equals("base")) {
            entidad = new EntidadBase(nombreFicticio,razonSocial,null);
        } else {
            DireccionPostal direccionPostal = new DireccionPostal(pais,provincia,ciudad,direccion);
            if(tipoEntidadJuridica.equals("oss")) {
                entidad = new OrganizacionSectorSocial(razonSocial,nombreFicticio,Integer.parseInt(cuit),direccionPostal,Integer.valueOf(codigoIgj),null);
            } else {
                entidad = new Empresa(razonSocial,nombreFicticio,Integer.parseInt(cuit),direccionPostal,Integer.valueOf(codigoIgj),parsearCategoriaEmpresa(categoriaEmpresa),null);
            }
        }
        Organizacion organizacion = getOrganizacion(request);
        withTransaction(() -> organizacion.agregarEntidad(entidad));
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

    private Organizacion getOrganizacion(Request request) {
        Long idUsuario = request.session().attribute("idUsuario");
        return RepositorioDeOrganizaciones.getInstance().getOrganizacionDelUsuarioConId(idUsuario).get();
    }
}
