package grp1.motorhomes.repository;

import grp1.motorhomes.model.Motorhome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * @author Patrick
 */
@Repository
public class MotorhomeRepo {

    @Autowired
    JdbcTemplate template;

    /**
     * @author Patrick
     */
    public List<Motorhome> fetchAllMotorhomes() {
        String sql = "SELECT registration as licencePlate, type, brand, model, description FROM motorhomes JOIN models using(model_id);";
        RowMapper<Motorhome> rowMapper = new BeanPropertyRowMapper<>(Motorhome.class);
        return template.query(sql, rowMapper);
    }

    /**
     * @param motorhome
     * @author Patrick
     */
    public void createMotorhome(Motorhome motorhome) {

        String insertModel = "INSERT INTO models(brand, model) SELECT ?, ? WHERE NOT EXISTS ( SELECT * FROM models WHERE brand = ? AND model = ?)";
        template.update(insertModel, motorhome.getBrand(), motorhome.getModel(), motorhome.getBrand(), motorhome.getModel());

        String insertMotorhome = "INSERT INTO motorhomes(registration, type, description, model_id) select ?, ?,? , " +
                "model_id FROM models WHERE brand = ? AND model = ?";
        template.update(insertMotorhome, motorhome.getLicencePlate(), motorhome.getType(), motorhome.getDescription(),
                motorhome.getBrand(), motorhome.getModel());
    }

    /**
     * @param licencePlate
     * @author Patrick og Sverri
     */

    public Motorhome findMotorhome(String licencePlate) {
        String selectSql = "SELECT registration as licencePlate, type, brand, model, description FROM motorhomes" +
                " JOIN models using(model_id) WHERE registration = ?";
        RowMapper<Motorhome> rowMapper = new BeanPropertyRowMapper<>(Motorhome.class);
        return template.queryForObject(selectSql, rowMapper, licencePlate);
    }

    /**
     * @param motorhome
     * @author Sverri
     */
    public void editMotorhome(Motorhome motorhome) {

        // if model was edited get modelId and insert new model if needed
        int modelId;
        try { //try and find the model
            String modelSelect = "SELECT model_id FROM models WHERE brand = ? AND model = ?";
            modelId = template.queryForObject(modelSelect, Integer.class, motorhome.getBrand(), motorhome.getModel());
            System.out.println(modelId);
        } catch (EmptyResultDataAccessException e) { //if model not found insert
            String insertModel = "INSERT INTO models(brand, model) VALUES(?,?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            // jdbc template does not by default support returning generated keys
            // code stub with much help from https://www.baeldung.com/spring-jdbc-autogenerated-keys
            template.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(insertModel, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, motorhome.getBrand());
                preparedStatement.setString(2, motorhome.getModel());
                return preparedStatement;
            }, keyHolder);

            modelId = keyHolder.getKey().intValue();
        }

        //update the motorhome
        String sqlUpdate = "UPDATE motorhomes SET registration = ?, type = ?, description = ?, image_path = ?, model_id = ? WHERE registration = ?";
        template.update(sqlUpdate, motorhome.getLicencePlate(), motorhome.getType(), motorhome.getDescription(),
                motorhome.getImagePath(), modelId, motorhome.getLicencePlate());
    }

    public void deleteMotorhome(String licencePlate) {
        String deleteSql = "DELETE FROM motorhomes WHERE registration = ? ";
        template.update(deleteSql, licencePlate);
    }
}