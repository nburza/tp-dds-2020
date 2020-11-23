package controllers;

import apiMercadoLibre.DTO.MonedaDTO;
import apiMercadoLibre.ServicioAPIMercadoLibre;
import egreso.*;
import entidadOrganizativa.Entidad;
import mediosDePago.MedioDePago;
import mediosDePago.RepositorioDeMediosDePago;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;
import usuario.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class EgresosController extends ControllerGenerico implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps
{
    ServicioAPIMercadoLibre servicioAPIMercadoLibre = new ServicioAPIMercadoLibre();

    public ModelAndView showEgresos(Request request, Response response){
        return ejecutarConControlDeLogin(request, response, (req, res) -> {
            Map<String, Object> viewModel = new HashMap<String, Object>();

            List<String> monedas = new ArrayList<String>();
            List<MonedaDTO> monedasValidas = servicioAPIMercadoLibre.getMonedas();

            for (MonedaDTO moneda : monedasValidas) {
                monedas.add(moneda.getSymbol() + " " + moneda.getId() + " (" + moneda.getDescription() + ")");
            }
            this.cargarDatosGeneralesA(viewModel,request,"Cargar Usuario");
            if (this.getUsuarioLogueado(req).esAdmin()) {
                viewModel.put("esAdmin", true);
            }
            viewModel.put("documentos", TipoDocComercial.values());
            viewModel.put("usuarios", RepositorioDeUsuarios.getInstance().getAllInstances());
            viewModel.put("items", RepositorioDeItems.getInstance().getAllInstances());
            viewModel.put("medios", RepositorioDeMediosDePago.getInstance().getAllInstances());
            viewModel.put("monedas", monedasValidas);
            viewModel.put("etiquetas", RepositorioDeEtiquetas.getInstance().getAllInstances());
            //viewModel.put("entidades", getOrganizacion(req).getEntidadesConSubentidades());
            viewModel.put("entidades", getOrganizacion(req).getEntidades());

            return new ModelAndView(viewModel, "altaEgresos.hbs");
        });
    }

    public ModelAndView altaEgresos(Request req, Response res)
    {
        Map<String, Object> viewModel = new HashMap<String, Object>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String tipoDocumento  = req.queryParams("tipoDocumento");
        String identificadorDoc = req.queryParams("identificadorDocumento");
        String medioDePago = req.queryParams("medios");
        String moneda = req.queryParams("moneda");
        List<String> items = Arrays.asList(req.queryMap("items").values());
        String fecha = req.queryParams("fecha");
        String requierePresu = req.queryMap("requierePresu").value();
        List<String> usuarios = Arrays.asList(req.queryMap("usuarios").values());
        String criterio = req.queryParams("criterio");
        List<String> etiquetas = Arrays.asList(req.queryMap("etiquetas").values());
        String entidad = req.queryParams("entidad");

        DocComercial docComercial = new DocComercial(Integer.parseInt(identificadorDoc), TipoDocComercial.valueOf(tipoDocumento));
        List<DocComercial> documentos = new ArrayList<DocComercial>();
        documentos.add(docComercial);

        MedioDePago medio = RepositorioDeMediosDePago.getInstance().getPorId(Long.parseLong(medioDePago)).get();

        List<Item> itemsMapeados = items.stream().map(item -> RepositorioDeItems.getInstance().getPorId(Long.parseLong(item)).get()).collect(Collectors.toList());
        List<Usuario> usuariosMapeados = usuarios.stream().map(usuario -> RepositorioDeUsuarios.getInstance().getPorId(Long.parseLong(usuario)).get()).collect(Collectors.toList());
        List<Etiqueta> etiquetasMapeadas = etiquetas.stream().map(etiqueta -> RepositorioDeEtiquetas.getInstance().getPorId(Long.parseLong(etiqueta)).get()).collect(Collectors.toList());
        LocalDate fechaMapeada = LocalDate.parse(fecha, formatter);

        //Entidad entidadMapeada = getOrganizacion(req).getEntidadesConSubentidades().stream().filter(e -> e.getId().equals(Long.parseLong(entidad))).findFirst().get();
        Entidad entidadMapeada = getOrganizacion(req).getEntidades().stream().filter(e -> e.getId().equals(Long.parseLong(entidad))).findFirst().get();

        Egreso egreso = new Egreso(documentos, medio, itemsMapeados, fechaMapeada, moneda);

        if(requierePresu == null)
            egreso.setRequierePresupuesto(false);
        else
            egreso.setRequierePresupuesto(true);

        if(criterio.equals("Presupuesto de menor valor"))
            egreso.setCriterioDeSeleccion(CriterioMenorValor.getInstance());

        egreso.setRevisores(usuariosMapeados);
        egreso.setEtiquetas(etiquetasMapeadas);

        List<Egreso> egresos = new ArrayList<Egreso>();
        egresos.add(egreso);
        entidadMapeada.setEgresos(egresos);

       withTransaction(() -> {
           RepositorioDeEgresos.getInstance().agregar(egreso);
       });

        this.cargarDatosGeneralesA(viewModel,req,"Cargar Usuario");
        if(this.getUsuarioLogueado(req).esAdmin())
        {
            viewModel.put("esAdmin",true);
        }
        res.redirect("/home");

        return new ModelAndView(viewModel, "altaEgresos.hbs");
    }
}
