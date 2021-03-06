package mainmanager;

import entities.Animal;
import entities.Owner;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import repository.OwnerRepository;
import static sun.misc.IOUtils.readFully;


/**
 * Manages update user information form
 * @author Vladi Colton
 */
@SessionScoped /*Each user gets new instance of the been during the session (as defined in "web.xml" 60 min)*/
@ManagedBean (name = "userUpdateInformationFormManager", eager = true)
public class UserUpdateInformationFormManager{
    private long _phoneNumber;
    private String _streetAddress;
    private String _city;
    private String _password;
    private Part image;
    
    public UserUpdateInformationFormManager()
    {
        //Get all user information from the session
        this._streetAddress = "";
        this._city = "";
        this._password = "";
        this._phoneNumber = SessionUtils.getUserPhone();
    }
    
    public long getPhoneNumber() 
    {
        return _phoneNumber;
    }
    public void setPhoneNumber(long phoneNumber) 
    {
        if(phoneNumber != 0)
            this._phoneNumber = phoneNumber;
    }

    public String getStreetAddress() 
    {
        return _streetAddress;
    }
    public void setStreetAddress(String streetAddress) 
    {
        this._streetAddress = streetAddress;
    }

    public String getCity() 
    {
        return _city;
    }
    public void setCity(String city) 
    {
        this._city = city;
    }

    public String getPassword() 
    {
        return _password;
    }
    public void setPassword(String password) 
    {
        this._password = password;
    }
    
    public Part getImage()
    {
        return image;
    }
    public void setImage(Part image)
    {
        this.image = image;
    }
    
    public String getUserName()
    {
        return SessionUtils.getUserName();
    }
    
    public boolean allPropertiesFilled(AjaxBehaviorEvent event)
    {
        return (_phoneNumber != 0);
    }
    
    public void updateRregistrationInfo()
    {
        //Verify that user connected and only then update the information
        if(!SessionUtils.isUserConnected())
            return;
        
        //Update session details to reflect imideatly all changes in the current session
        SessionUtils.setUserPhone(_phoneNumber);
        SessionUtils.setUserLocation(_streetAddress + ", " + _city);
        
        OwnerRepository rep = new OwnerRepository();
        Owner user = rep.getOwner(SessionUtils.getUserEmail());
        
        boolean needDBUpdate = false;
        if(!this._password.equalsIgnoreCase("") && !user.getPassword().equals(this._password))
        {
            //Replase existing password
            user.setPassword(_password);
            needDBUpdate = true;
        }
        if(!this._streetAddress.equalsIgnoreCase("") || !this._city.equalsIgnoreCase(""))
        {
            //Replase existing location
            user.setLocation(_streetAddress + ", " + _city);
            needDBUpdate = true;
        }
        if(this._phoneNumber != user.getPhoneNumber())
        {
            //Replase existing PhoneNumber
            user.setPhoneNumber(_phoneNumber);
            needDBUpdate = true;
        }
        if(this.image != null)
        {
            try
            {
                InputStream in = image.getInputStream();
                //Create byte array to save the image in the DB
                byte[] fileAsByteArray = readFully(in, Integer.MAX_VALUE, true/*ignored since Integer.MAX_VALUE set*/);
                user.setProfilePic(fileAsByteArray);
            }
            catch(IOException ex)
            {
                ex.printStackTrace(System.out);
            }
            needDBUpdate = true;
        }
        
        //Update DB with new details if needed
        if(needDBUpdate)
        {
            rep.update(user);
        }
    }
    
    public String deleteUserAccount()
    {
         //Verify that user connected and only then update the information
        if(SessionUtils.isUserConnected())
        {
            //Get user to be deleted from data base
            OwnerRepository userRep = new OwnerRepository();
            Owner user = userRep.getOwner(SessionUtils.getUserEmail());
            
            //Close the session for the user on delete action
            HttpSession session = SessionUtils.getSession();
            session.invalidate();
            
            //Remove all animals user have registered
            List<Animal> userAnimalsList = user.getAnimals();
            if(userAnimalsList.size() > 0)
            {
                for(int i = 0; i < userAnimalsList.size(); i++)
                {
                    user.removeAnimal(userAnimalsList.get(i));
                }
            }
            //Update user
            userRep.update(user);
            //Now after all user Animals removed delete the user from the DB
            userRep.remove(user.getId());
        }

        return "index.xhtml?faces-redirect=true";
    }
}
