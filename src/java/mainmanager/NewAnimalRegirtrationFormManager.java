package mainmanager;

import entities.Animal;
import entities.Owner;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.Part;
import repository.OwnerRepository;
import static sun.misc.IOUtils.readFully;

/**
 * New Animal Registration form controller
 * @author Vladi Colton
 */
@SessionScoped /*Each user gets new instance of the been during the session (as defined in "web.xml" 60 min)*/
@ManagedBean (name = "newAnimalRegirtrationFormManager", eager = true)
public class NewAnimalRegirtrationFormManager {
    private Part image;
    private double age;
    private String description;
    private String type;
    private String subType;
    private String name;
    private Animal newAnimal;
    
    public NewAnimalRegirtrationFormManager()
    {
        age = 0;
        name = "";
        type = "";
        description = "";
        subType = "";
    }
    
    public double getAge() 
    {
        return age;
    }
    public void setAge(double age) 
    {
        this.age = age;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getType() 
    {
        return type;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getSubType() 
    {
        return subType;
    }
    public void setSubType(String subType) 
    {
        this.subType = subType;
    }

    public String getName() 
    {
        return name;
    }
    public void setName(String name) 
    {
        this.name = name;
    }
    
    public String getOwnersLocation()
    {
        return SessionUtils.getUserLocation();
    }
    
    public String getOwnersPhone()
    {
        return Long.toString(SessionUtils.getUserPhone());
    }
    
    public boolean allPropertiesFilled(AjaxBehaviorEvent event)
    {
        return (!name.equalsIgnoreCase("") && age != 0 && !type.equalsIgnoreCase(""));
    }
    
    public Part getImage()
    {
        return image;
    }
    public void setImage(Part image)
    {
        this.image = image;
    }
    
    public void addNewAnimal()
    {
        //Verify that user connected and only then update the information
        if(!SessionUtils.isUserConnected())
            return;
        
        //Create default animal image
        byte[] animalProfPic = null;
        try 
        {
            if(image == null) //If image not selected to be uploaded -> set default image
            {

                    //Get directory with default image
                    File f = new File(NewAnimalRegirtrationFormManager.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                    String imageLocation = f.getPath(); //Set relative path to the DB
                    imageLocation = Paths.get(imageLocation.substring(0, imageLocation.indexOf("Adopt-A-Pet")+12), "web", "images").toString();
                    //Get default image
                    File usrImageFile = new File(Paths.get(imageLocation, "DefaultAnimalProfileImage.png").toString());
                    //Create byte array for saving in the DB
                    animalProfPic = Files.readAllBytes(usrImageFile.toPath());
            }
            else //Use uploaded image for the animal
            {
                InputStream in = image.getInputStream();
                animalProfPic = readFully(in, Integer.MAX_VALUE, true/*ignored since Integer.MAX_VALUE set*/);
            }
        }
        catch (IOException ex) 
        {
            ex.printStackTrace(System.out);
        }
        
        //Get Owner to be assigned to the new animal record
        OwnerRepository repOwner = new OwnerRepository();
        Owner owner = repOwner.getOwner(SessionUtils.getUserEmail());
        
        //Create Animal from the recieved details
        newAnimal = Animal.builder()
                .name(name)
                .age(age)
                .type(type)
                .subType(subType)
                .description(description)
                .animalPic(animalProfPic)
                .build();
        
        //Create the animal in the DB
        owner.addAnimal(newAnimal);
        repOwner.update(owner);
    }
}
