import java.sql.*;

public class OdontologicClinic {

    private static final String SQL_CREATE_TABLE = "DROP TABLE IF EXISTS DENTIST;" +
        "CREATE TABLE DENTIST ( " +
        "ID INT PRIMARY KEY," +
        "REGISTRATION INT NOT NULL," +
        "NAME VARCHAR(100) NOT NULL," +
        "LASTNAME VARCHAR(100) NOT NULL);";

    private static final String SQL_INSERT_DATA = "INSERT INTO DENTIST  VALUES (?,?,?,?);";
    private static final String SQL_SELECT_ALL = "SELECT * FROM DENTIST;";

    private static final String SQL_UPDATE = "UPDATE  DENTIST SET NAME= ? WHERE ID = ?";

    private static final String SQL_SELECT_ID = "SELECT * FROM DENTIST WHERE ID = ?";

    private static final String SQL_BORRAR_REGISTRO = "DELETE FROM DENTIST WHERE ID =?";





    public static void main(String[] args) throws SQLException {


        Dentist dentist1 = new Dentist(1,123,"Vanina", "Lopez");
        Dentist dentist2 = new Dentist(2,345,"Juan", "Perez");

        Connection connection = null;



        try {
            connection = getConnection();

            Statement statement = connection.createStatement();

            statement.execute(SQL_CREATE_TABLE);

            // Insertar Valores en la base de datos

            PreparedStatement psInsert = connection.prepareStatement(SQL_INSERT_DATA);

            psInsert.setInt(1,dentist1.getId());
            psInsert.setInt(2,dentist1.getRegistration());
            psInsert.setString(3,dentist1.getName());
            psInsert.setString(4,dentist1.getLastName());
            psInsert.execute();

            psInsert.setInt(1,dentist2.getId());
            psInsert.setInt(2,dentist2.getRegistration());
            psInsert.setString(3,dentist2.getName());
            psInsert.setString(4,dentist2.getLastName());
            psInsert.execute();


            // Consultar datos en la tabla
            ResultSet rs = statement.executeQuery(SQL_SELECT_ALL);

            while (rs.next()) {
                System.out.println("Los valores insertados en la tabla son " +
                        "ID: " + rs.getInt(1) +  " - Matricula: " +
                        rs.getInt(2) +" - Nombre: " + rs.getString(3) +
                        " - Apellido: " + rs.getString(4));
            };

            // Actualizar un registro
            connection.setAutoCommit(false);

            PreparedStatement updateRegistro = connection.prepareStatement(SQL_UPDATE);

            String nombreCambiado = "Gonzalo";
            updateRegistro.setString(1,nombreCambiado);
            updateRegistro.setInt(2,dentist1.getId());
            updateRegistro.execute();
            connection.commit();

            connection.setAutoCommit(true);




        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Chequeamos si actualizó los datos

        try {
            connection = getConnection();

            PreparedStatement chequeoDatos = connection.prepareStatement(SQL_SELECT_ID);

            chequeoDatos.setInt(1, 1);

            ResultSet rs2 = chequeoDatos.executeQuery();

            while (rs2.next()) {
                System.out.println("El registro cuyo valor ha sido actualizado en la tabla es: " +
                        "ID: " + rs2.getInt(1) +  " - Matricula: " +
                        rs2.getInt(2) + " - Nombre: " + rs2.getString(3) +
                        " - Apellido: " + rs2.getString(4));
            }

            // Borramos un registro

            PreparedStatement borrarRegistro = connection.prepareStatement(SQL_BORRAR_REGISTRO);

            borrarRegistro.setInt(1,dentist1.getId());
            borrarRegistro.execute();
            connection.commit();
            connection.setAutoCommit(true);













        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // Chequear que se hayan borrado los datos
        try {


            connection = getConnection();

            Statement statement = connection.createStatement();

            ResultSet rFinal = statement.executeQuery(SQL_SELECT_ALL);

            while (rFinal.next()) {
                System.out.println("Luego de borrado el registro queda: " +
                        "ID: " + rFinal.getInt(1) +  " - Matricula: " +
                        rFinal.getInt(2) + " - Nombre: " + rFinal.getString(3) +
                        " - Apellido: " + rFinal.getString(4));
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (Exception close) {
                close.printStackTrace();
            }
        }

    }

    private static Connection getConnection() throws Exception {
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:~/OdontologicClinic");

    }
}
