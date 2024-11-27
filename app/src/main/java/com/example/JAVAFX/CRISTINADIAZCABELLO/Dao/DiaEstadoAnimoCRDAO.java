package com.example.JAVAFX.CRISTINADIAZCABELLO.Dao;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.DiaEstadoAnimoCR;

import java.time.LocalDate;

public interface DiaEstadoAnimoCRDAO {
    DiaEstadoAnimoCR findByFechaAndMomento(LocalDate fecha, String momento);

    void insert(DiaEstadoAnimoCR diaEstadoAnimoCR);
    void update(DiaEstadoAnimoCR diaEstadoAnimoCR);
    void delete(LocalDate fecha, String momento);
}
