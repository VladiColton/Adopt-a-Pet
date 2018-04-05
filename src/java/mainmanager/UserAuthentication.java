package mainmanager;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;
import sqlmanager.*;

/**
 * User authentication class responsible to authenticate users vs DB
 * @author Vladi Colton
 */
@SessionScoped /*Each user gets new instance of the been during the session (as defined in "web.xml" 60 min)*/
@ManagedBean (name = "userAuthentication")
public class UserAuthentication implements Serializable{
    private String _mailAddress;
    private String _password;
    private String _autoErrMsg;
    
    public UserAuthentication()
    {
        _mailAddress = "";
        _password = "";
        _autoErrMsg = "";
    }
    
    public void setEmail(String email)
    {
        this._mailAddress = email;
    }
    public String getEmail()
    {
        return this._mailAddress;
    }
    
    public void setPassword(String password)
    {
        this._password = password;
    }
    public String getPassword()
    {
        return this._password;
    }
    
    public String getAutoErrorMSG() 
    {
	return _autoErrMsg;
    }
    public void setAutoErrorMSG(String msg) 
    {
        this._autoErrMsg = msg;
    }
    
    public String validateEmailPassword()
    {
        boolean valid = false;
        
        if(_mailAddress != null && _password != null)
        {
            valid = Login.validateUser(_mailAddress, _password); //validate user name and password with DB
        }
        
        if(valid)
        {
            this.setAutoErrorMSG("");
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("useremail", this._mailAddress);
            return "index";
        }
        else
        {
            this.setAutoErrorMSG("Incorrect Email or Password!!");
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Incorrect Username and Passowrd", "Please enter correct username and Password"));
            return "index";
        }
    }
    
    //Logout event, invalidate session
    public String logout() 
    {
	HttpSession session = SessionUtils.getSession();
        this._mailAddress = "";
        this._password = "";
	session.invalidate();
        return "index.xhtml?faces-redirect=true";
    }
}