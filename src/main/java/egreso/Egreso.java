package egreso;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import mediosDePago.MedioDePago;
import persistencia.EntidadPersistente;
import presupuesto.Presupuesto;
import apiMercadoLibre.ServiceLocator;
import usuario.Usuario;
import javax.persistence.*;

@Entity
@Table(name = "Egresos")
public class Egreso extends EntidadPersistente
{

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "egreso_id")
    private List<DocComercial> documentosComerciales = new ArrayList<>();
    @ManyToOne
    private MedioDePago medioDePago;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "egreso_id")
    private List<Item> items = new ArrayList<>();
    private LocalDate fecha;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "egreso_id")
    private List<Presupuesto> presupuestos = new ArrayList<>();
    private boolean requierePresupuesto = true;
    @ManyToMany
    @JoinTable(name = "revisor_x_egreso")
    private List<Usuario> revisores = new ArrayList<>();
    private String moneda;
    @ManyToOne(cascade = CascadeType.ALL)
    private CriterioCompra criterioDeSeleccion = CriterioMenorValor.getInstance();
    @Enumerated(EnumType.STRING)
    private EstadoValidacion estado = EstadoValidacion.PENDIENTE;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "egreso_x_etiqueta")
    private List<Etiqueta> etiquetas = new ArrayList<>();

    public Egreso(List<DocComercial> unosDC, MedioDePago unMedioDePago, List<Item> unosItems, LocalDate unaFecha, String moneda)
    {
        ServiceLocator.getInstance().getValidadorDeMoneda().validarMoneda(moneda);
        this.documentosComerciales = unosDC;
        this.medioDePago = unMedioDePago;
        this.items = unosItems;
        this.fecha = unaFecha;
        this.moneda = moneda;
    }

    public Egreso(List<DocComercial> unosDC, MedioDePago unMedioDePago, List<Item> unosItems, LocalDate unaFecha, boolean requierePresupuesto, String moneda)
    {
        ServiceLocator.getInstance().getValidadorDeMoneda().validarMoneda(moneda);
        this.documentosComerciales = unosDC;
        this.medioDePago = unMedioDePago;
        this.items = unosItems;
        this.fecha = unaFecha;
        this.requierePresupuesto = requierePresupuesto;
        this.moneda = moneda;
    }

    public Egreso() {
    }

    public void setDocumentosComerciales(List<DocComercial> documentosComerciales) {
        this.documentosComerciales = documentosComerciales;
    }

    public void setMedioDePago(MedioDePago medioDePago) {
        this.medioDePago = medioDePago;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setPresupuestos(List<Presupuesto> presupuestos) {
        this.presupuestos = presupuestos;
    }

    public void setRequierePresupuesto(boolean requierePresupuesto) {
        this.requierePresupuesto = requierePresupuesto;
    }

    public void setRevisores(List<Usuario> revisores) {
        this.revisores = revisores;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getMoneda(){
        return moneda;
    }

    public BigDecimal getImporteTotal(){
        return totalEgreso();
    }

    public void setCriterioDeSeleccion(CriterioCompra criterioDeSeleccion) {
        this.criterioDeSeleccion = criterioDeSeleccion;
    }

    public void setEtiquetas(List<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public EstadoValidacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoValidacion estado) {
        this.estado = estado;
    }

    public List<Presupuesto> getPresupuestos() {
        return presupuestos;
    }

    private static final int presupuestosRequeridos = 3;

    public BigDecimal totalEgreso()
    {
        BigDecimal total = new BigDecimal("0");
        BigDecimal totalFinal = items.stream()
                .map(x -> total.add(x.precioTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalFinal;
    }

    public void agregarPresupuesto(Presupuesto presupuesto) throws Exception {
        if(requierePresupuesto){
            presupuestos.add(presupuesto);
        }else throw new Exception("El egreso no requiere presupuesto");
    }

    public void agregarEtiqueta(Etiqueta etiqueta){
        etiquetas.add(etiqueta);
    }

    public void quitarEtiqueta(Etiqueta etiqueta){
        etiquetas.remove(etiqueta);
    }

    public void validar(){
        if (this.esValido()){
            this.setEstado(EstadoValidacion.VALIDO);
        }
    }

    public boolean esValido()
    {
        return (!requierePresupuesto||this.cumpleCondicionDeValidacion());

    }
    public boolean cumpleCondicionDeValidacion (){
        return this.cumplePresupuestosMinimos() &&
                this.estaBasadoEnUnPresupuesto() &&
            this.cumpleCriterioSeleccion();

}

    public boolean cumplePresupuestosMinimos()
    {

            return presupuestos.size() >= presupuestosRequeridos;

    }

    public boolean estaBasadoEnUnPresupuesto()
    {

            return presupuestos.stream().anyMatch(x -> x.getDetalle().equals(items));

    }

    public boolean cumpleCriterioSeleccion()
    {

            return presupuestos.stream().anyMatch(x -> this.getCriterioDeSeleccion().presupuestosQueCumplen(presupuestos).contains(x));

    }

    public List<Usuario> getRevisores()
    {
        return revisores;
    }

    public void agregarRevisor(Usuario usuario)
    {
        revisores.add(usuario);
    }

    public List<Item> getItems() {
        return items;
    }

    public CriterioCompra getCriterioDeSeleccion() { return criterioDeSeleccion; }

    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    public LocalDate getFecha(){
        return fecha;
    }

    public boolean tieneLaEtiqueta(Etiqueta etiqueta){
        return etiquetas.contains(etiqueta);
    }

    public boolean estaEnElUltimoMes() {
        if(fecha == null){
            fecha = LocalDate.now();
        }
        return fecha.compareTo(LocalDate.now().minusMonths(1))==1;
    }

    public boolean esRevisor(Usuario unUsuario){
        return revisores.contains(unUsuario);
    }

}