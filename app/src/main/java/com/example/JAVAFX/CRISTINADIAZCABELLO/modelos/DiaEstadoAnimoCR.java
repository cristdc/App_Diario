/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JAVAFX.CRISTINADIAZCABELLO.modelos;

/**
 *
 * @author crist
 */
import java.util.Date;

public class DiaEstadoAnimoCR {
    private Date fecha;
    private int idEstado;
    private String momentoDia;
    private String descripcion;

    public DiaEstadoAnimoCR(Date fecha, int idEstado, String momentoDia, String descripcion) {
        this.fecha = fecha;
        this.idEstado = idEstado;
        this.momentoDia = momentoDia;
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getMomentoDia() {
        return momentoDia;
    }

    public void setMomentoDia(String momentoDia) {
        this.momentoDia = momentoDia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "DiaEstadoAnimoCR{" +
                "fecha=" + fecha +
                ", idEstado=" + idEstado +
                ", momentoDia='" + momentoDia + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}