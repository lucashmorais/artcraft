package services;

import db.DatabaseHelper;
import model.Account;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by renancastro on 31/12/14.
 */
public class LoginManager {
    static LoginManager manager;
    Account currentAccount;

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public boolean loginUser(Account user) {
        List list = null;
        try {
            list = DatabaseHelper.getHelper().getAccountDao().queryForMatching(user);
            if (list != null && list.size() > 0) {
                this.currentAccount = user;
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static LoginManager getManager() {
        if (manager == null){
            manager = new LoginManager();
        }
        return manager;
    }

}
