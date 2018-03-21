package medicineguru.dto;

import com.google.firebase.database.Exclude;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Brinder Saini on 09/02/2018.
 */

public class Medicine {
    public String medId;
    public String name;
    public String title;
    public String description;
    public int size;
    public String color;
    public List<Symptom> symptoms;
    public List<Image> images;
    public  Dose dosage;
    public  String form;

    public Medicine() {
    }

    public Medicine(String medId,String name, String title, String description, int size, String color, List<Symptom> symptoms, List<Image> images, Dose dosage, String form) {
       this.medId=medId;
        this.name = name;
        this.title = title;
        this.description = description;
        this.size = size;
        this.color = color;
        this.symptoms = symptoms;
        this.images = images;
        this.dosage = dosage;
        this.form = form;
    }

    public Medicine(String name, String title, String description, int size, String color, List<Symptom> symptoms, List<Image> images, Dose dosage, String form) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.size = size;
        this.color = color;
        this.symptoms = symptoms;
        this.images = images;
        this.dosage = dosage;
        this.form = form;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("medId", medId);
        result.put("name",name);
        result.put("title", title);
        result.put("title", title);
        result.put("description", description);
        result.put("size", size);
        result.put("symptoms", symptoms);
        result.put("images", images);
        result.put("dosage", dosage);
        result.put("form", form);

        return result;
    }
    public String getMedId() {
        return medId;
    }

    public void setMedId(String medId) {
        this.medId = medId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Dose getDosage() {
        return dosage;
    }

    public void setDosage(Dose dosage) {
        this.dosage = dosage;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
