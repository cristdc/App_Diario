/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.JAVAFX.CRISTINADIAZCABELLO.modelos;

import java.sql.Date;

/**
 *
 * @author crist
 */

public class DiaEstadoAnimoCR {

    private int id_estado;
    private Date fecha;
    private String momentoDia;
    private String descripcion;

    public DiaEstadoAnimoCR(Date fecha, String momentoDia, String descripcion) {
        this.fecha = fecha;
        this.momentoDia = momentoDia;
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
                ", momentoDia='" + momentoDia + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", id_estado=" + id_estado +
                '}';
    }

    public void setIdEstado(int idEstado) {
        this.id_estado=idEstado;
    }
    public int getIdEstado() {
        return id_estado;
    }
}