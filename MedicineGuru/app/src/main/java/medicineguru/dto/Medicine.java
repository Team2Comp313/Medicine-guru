package medicineguru.dto;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by Brinder Saini on 09/02/2018.
 */

public class Medicine {

    public String name;
    public String title;
    public String description;
    public int size;
    public String color;
    public Collection<Symptom> symptoms;
    public Collection<Image> images;
    public  Dose dosage;
    public  String form;

    public Medicine() {
    }

    public Medicine(String name, String title, String description, int size, String color, Collection<Symptom> symptoms, Collection<Image> images, Dose dosage, String form) {
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

    public Collection<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(Collection<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    public Collection<Image> getImages() {
        return images;
    }

    public void setImages(Collection<Image> images) {
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
