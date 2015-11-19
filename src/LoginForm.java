import model.Account;

import javax.swing.*;
import java.awt.*;

/**
 * Created by renancastro on 18/11/15.
 */
public class LoginForm extends JFrame{
    private JButton registrarButton;
    private JTextField usuárioTextField;
    private JPasswordField senhaPasswordField;
    private JButton loginButton1;
    private JPanel mainPanel;

    public LoginForm(){
        super("Home");
        setContentPane(mainPanel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(this.getExtendedState());

    }

    public void setData(Account data) {
        usuárioTextField.setText(data.getName());
        senhaPasswordField.setText(data.getPassword());
    }

    public void getData(Account data) {
        data.setName(usuárioTextField.getText());
        data.setPassword(senhaPasswordField.getText());
    }

    public boolean isModified(Account data) {
        if (usuárioTextField.getText() != null ? !usuárioTextField.getText().equals(data.getName()) : data.getName() != null)
            return true;
        if (senhaPasswordField.getText() != null ? !senhaPasswordField.getText().equals(data.getPassword()) : data.getPassword() != null)
            return true;
        return false;
    }
}
