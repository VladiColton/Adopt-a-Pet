package entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Animal Representation Class
 */
@Entity
public class Animal implements Persistable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private double age;
    private String description;
    private String type;
    private String subType;
    private String name;
    private byte[] animalPic;

    @ManyToOne
    private Owner owner;

    public Animal() 
    {
    }

    public Animal(Owner owner, String description, String type, String subType, String name, double age) {
        if (!isAgeValid(age)) 
        {
            throw new IllegalArgumentException("age cannot be negative");
        }

        this.description = description;
        this.name = name;
        this.type = type;
        this.subType = subType;
        this.owner = owner;
        this.age = age;
    }

    private static boolean isAgeValid(double age) 
    {
        return age >= 0;
    }

    @Override
    public Long getId() 
    {
        return id;
    }

    public void setAge(float age) 
    {
        if (!isAgeValid(age)) {
            throw new IllegalArgumentException("age cannot be negative");
        }
        this.age = age;
    }

    public double getAge() 
    {
        return this.age;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return this.description;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return this.name;
    }

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return this.type;
    }

    public void setSubType(String subType) 
    {
        this.subType = subType;
    }

    public String getSubType() 
    {
        return this.subType;
    }
    
    public byte[] getAnimalPic() 
    {
        return animalPic;
    }

    public void setAnimalPic(byte[] animalPic) 
    {
        this.animalPic = animalPic;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Owner getOwner() {
        return this.owner;
    }
    
    public String getOwnerName()
    {
        return this.owner != null ? this.owner.getName() : "Owner obj is NULL (in Animal Class)";
    }
    public long getOwnerPhoneNum()
    {
        return this.owner != null ? this.owner.getPhoneNumber() : 0;
    }
    public String getOwnerAddress()
    {
        return this.owner != null ? this.owner.getLocation() : "Israel";
    }
    
    @Override
    public int hashCode() 
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) 
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Animal)) 
        {
            return false;
        }
        Animal other = (Animal) object;
        if((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) 
        {
            return false;
        }
        return true;
    }
}
