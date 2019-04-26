package rockets.model;

import java.util.ArrayList;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

public class RocketFamily extends Entity {
    private String familyName;
    private String country;
    private String develop_OR;
    private ArrayList<Rocket> rocketList;

    //public Rocket newRocket(String name, String manufacture){
    //    Rocket rocket = new Rocket (name,getCountry (),manufacture);
    //    return rocket;
    //}
    public void addRocket(Rocket rocket) {
        notNull (rocket,"The input rocket can not be null");
        this.rocketList.add (rocket);
    }

    public ArrayList<Rocket> getRocketList() {

        return rocketList;
    }



    public RocketFamily(String familyName,String country,String dpr){


        setFamilyName(familyName);
        setCountry(country);
        setDevelop_OR(dpr);

    }



    public String formating(String str){
        str = str.replace(" ","");
        String f1 = str.substring(0,1);
        String f2 = str.substring(1,str.length());
        f1 = f1.toUpperCase();
        f2 = f2.toLowerCase();
        str = f1 + f2;
        return str;
    }



    public String getFamilyName() {

        return familyName;
    }

    public void setFamilyName(String familyName) {
        notBlank(familyName,"family name can not be null");
        familyName = formating(familyName);
        this.familyName = familyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        notBlank(country,"country name can not be null");
        country = formating(country);
        String reg = "[a-zA-Z]+";
        if (country.matches(reg)){
            this.country = country;
        }
        else{
            throw new IllegalArgumentException("country name should only contain letter");
        }

    }

    public String getAbbrevation() {
        String ab = this.country;
        ab = ab.toUpperCase().substring(0,2);
        return ab;

    }



    public String getDevelop_OR() {
        return develop_OR;
    }

    public void setDevelop_OR(String develop_OR) {
        notBlank (develop_OR,"development organization can not be null");
        develop_OR = develop_OR.trim();
        this.develop_OR = develop_OR;
    }


    @Override
    public int hashCode() {
        return Objects.hash(familyName, country, develop_OR);
    }

    @Override
    public String toString() {
        return "Rocket{" +
                "rocketFamilyName='" + familyName + '\'' +
                ", country='" + country + '\'' +
                ", development organization ='" + develop_OR + '\'' +
                '}';
    }

}
