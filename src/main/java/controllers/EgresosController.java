package controllers;

import apiMercadoLibre.DTO.MonedaDTO;
import apiMercadoLibre.ServicioAPIMercadoLibre;
import egreso.RepositorioDeItems;
import egreso.TipoDocComercial;
import mediosDePago.RepositorioDeMediosDePago;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RepositorioDeUsuarios;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EgresosController
{
    ServicioAPIMercadoLibre servicioAPIMercadoLibre = new ServicioAPIMercadoLibre();

    public ModelAndView show(Request req, Response res){
        Map<String, Object> viewModel = new HashMap<String, Object>();

        List<String> monedas = new ArrayList<String>();
        List<MonedaDTO> monedasValidas = servicioAPIMercadoLibre.getMonedas();
        for (MonedaDTO moneda : monedasValidas)
        {
            monedas.add(moneda.getSymbol() + " " + moneda.getId() + " (" + moneda.getDescription() + ")");
        }

        viewModel.put("documentos", TipoDocComercial.values());
        viewModel.put("usuarios", RepositorioDeUsuarios.getInstance().getAllInstances());
        viewModel.put("items", RepositorioDeItems.getInstance().getAllInstances());
        viewModel.put("medios", RepositorioDeMediosDePago.getInstance().getAllInstances());
        viewModel.put("monedas", monedas);

        return new ModelAndView(viewModel, "altaEgresos.hbs");
    }

    public void altaEgresos(Request req, Response res)
    {
        return;
    }
}
