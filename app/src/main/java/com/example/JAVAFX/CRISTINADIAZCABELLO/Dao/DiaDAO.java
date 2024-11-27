package com.example.JAVAFX.CRISTINADIAZCABELLO.Dao;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.Dia;

import java.time.LocalDate;

public interface DiaDAO {
    Dia findByFecha(LocalDate fecha);
    void insert(Dia dia);
    void update(Dia dia);
    void delete(LocalDate fecha);
}


