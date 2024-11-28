package com.example.JAVAFX.CRISTINADIAZCABELLO.Dao;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.DiaEstadoAnimoCR;

import java.sql.*;
import java.time.LocalDate;

public class DiaEstadoAnimoCRDAOclass implements DiaEstadoAnimoCRDAO {

    private final Connection connection;

    public DiaEstadoAnimoCRDAOclass(Connection connection) {
        this.connection = connection;
    }

    @Override
    public DiaEstadoAnimoCR findByFechaAndMomento(LocalDate fecha, String momento) {
        String query = "SELECT * FROM Dia_EstadoAnimo_CR WHERE fecha = ? AND momento_dia = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDate(1, Date.valueOf(fecha));
            ps.setString(2, momento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DiaEstadoAnimoCR(
                            rs.getDate("fecha"),
                            rs.getString("momento_dia"),
                            rs.getString("descripcion")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insert(DiaEstadoAnimoCR diaEstadoAnimoCR) throws SQLException {
        String sql = "INSERT INTO Dia_EstadoAnimo_CR (fecha, momento_dia, descripcion, id_estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, diaEstadoAnimoCR.getFecha());
            stmt.setString(2, diaEstadoAnimoCR.getMomentoDia());
            stmt.setString(3, diaEstadoAnimoCR.getDescripcion());
            stmt.setInt(4, diaEstadoAnimoCR.getIdEstado());
            stmt.executeUpdate();
        }
    }


    @Override
    public void update(DiaEstadoAnimoCR diaEstadoAnimoCR) {
        String query = "UPDATE Dia_EstadoAnimo_CR SET momento_dia = ?, descripcion = ? WHERE fecha = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, diaEstadoAnimoCR.getMomentoDia());
            ps.setString(2, diaEstadoAnimoCR.getDescripcion());
            ps.setDate(3, (Date) diaEstadoAnimoCR.getFecha());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(LocalDate fecha, String momento) {
        String query = "DELETE FROM Dia_EstadoAnimo_CR WHERE fecha = ? AND momento_dia = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDate(1, Date.valueOf(fecha));
            ps.setString(2, momento);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

