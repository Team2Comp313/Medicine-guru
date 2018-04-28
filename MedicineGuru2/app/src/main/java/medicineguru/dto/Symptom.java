package medicineguru.dto;

/**
 * Created by Brinder Saini on 09/02/2018.
 */

public class Symptom {
    private String name;

    public Symptom() {
    }

    public Symptom(String name){

        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
