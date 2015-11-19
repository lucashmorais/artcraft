package db;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import model.Account;

import java.sql.SQLException;

public class DatabaseHelper {
    ConnectionSource connectionSource;
    static DatabaseHelper helper;
    Dao<Account, String> accountDao;

    public static DatabaseHelper getHelper() {
        if (helper == null) {
            helper = new DatabaseHelper();
        }
        return helper;
    }

    public DatabaseHelper() {
        try {
            // this uses h2 by default but change to match your database
            String databaseUrl = "jdbc:derby:derbydb2;create=true;create=true";
            // create a connection source to our database
            connectionSource =
                    new JdbcConnectionSource(databaseUrl);

            // instantiate the dao
            accountDao = DaoManager.createDao(this.connectionSource, Account.class);


            // if you need to create the 'accounts' table make this call
            try {

                TableUtils.createTableIfNotExists(this.connectionSource, Account.class);
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Dao<Account, String> getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(Dao<Account, String> accountDao) {
        this.accountDao = accountDao;
    }

}