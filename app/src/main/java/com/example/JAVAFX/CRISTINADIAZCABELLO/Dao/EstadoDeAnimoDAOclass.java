package com.example.JAVAFX.CRISTINADIAZCABELLO.Dao;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.EstadoDeAnimo;

import java.sql.*;
import java.time.LocalDate;


public class EstadoDeAnimoDAOclass implements EstadoDeAnimoDAO {

    private final Connection connection;

    public EstadoDeAnimoDAOclass(Connection connection) {
        this.connection = connection;
    }


    public int findIdByAttributes(EstadoDeAnimo estadoDeAnimo) {
        String query = "SELECT id_estado FROM Estado_de_Animo WHERE emoji = ? AND paciencia = ? AND fuerza_sentimiento = ? AND grado_productividad = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, estadoDeAnimo.getEmoji());
            ps.setInt(2, estadoDeAnimo.getPaciencia());
            ps.setInt(3, estadoDeAnimo.getFuerzaSentimiento());
            ps.setInt(4, estadoDeAnimo.getGradoProductividad());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_estado");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public EstadoDeAnimo findById(int idEstado) {
        String query = "SELECT * FROM Estado_de_Animo WHERE id_estado = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idEstado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EstadoDeAnimo(
                            rs.getString("emoji"),
                            rs.getInt("paciencia"),
                            rs.getInt("fuerza_sentimiento"),
                            rs.getInt("grado_productividad")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(EstadoDeAnimo estado) throws SQLException {
        String sql = "INSERT INTO Estado_de_Animo (emoji, paciencia, fuerza_sentimiento, grado_productividad) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, estado.getEmoji());
            stmt.setInt(2, estado.getPaciencia());
            stmt.setInt(3, estado.getFuerzaSentimiento());
            stmt.setInt(4, estado.getGradoProductividad());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    System.out.println("Generated ID: " + keys.getInt(1));
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo generar el ID para EstadoDeAnimo.");
    }

    @Override
    public void update(EstadoDeAnimo estadoDeAnimo) {
        System.out.println("Updating EstadoDeAnimo with ID: " + estadoDeAnimo.getId_estado());
        String query = "UPDATE Estado_de_Animo SET emoji = ?, paciencia = ?, fuerza_sentimiento = ?, grado_productividad = ? WHERE id_estado = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, estadoDeAnimo.getEmoji());
            ps.setInt(2, estadoDeAnimo.getPaciencia());
            ps.setInt(3, estadoDeAnimo.getFuerzaSentimiento());
            ps.setInt(4, estadoDeAnimo.getGradoProductividad());
            ps.setInt(5, estadoDeAnimo.getId_estado());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No se actualizó ningún registro. Verifica el ID: " + estadoDeAnimo.getId_estado());
            } else {
                System.out.println("Filas actualizadas: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(int id) {
        String query = "DELETE FROM Estado_de_Animo WHERE id_estado = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public EstadoDeAnimo getEstadoDeAnimo(LocalDate fecha, String momento) {
        String query = "SELECT e.* FROM `Estado_de_Animo` as e INNER JOIN Dia_EstadoAnimo_CR as cr ON cr.id_estado = e.id_estado WHERE cr.momento_dia = ? AND cr.fecha = ?;";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, momento);
            ps.setDate(2, Date.valueOf(fecha));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                EstadoDeAnimo e = new EstadoDeAnimo(
                        rs.getString("emoji"),
                        rs.getInt("paciencia"),
                        rs.getInt("fuerza_sentimiento"),
                        rs.getInt("grado_productividad")
                );

                e.setId_estado(rs.getInt("id_estado"));

                return e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}

