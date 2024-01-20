package com.shop.ua.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "goods")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "price")
    private int price;
    @Column(name = "approved")
    private boolean approved;
    @Column(name = "previewImageId")
    private Long previewImageId;
    @Column(name = "is_personal")
    private boolean isPersonal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @JsonManagedReference
    private Store store;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "goods")
    @JsonBackReference
    private List<Image> images = new ArrayList<>();

    private LocalDateTime dateOfCreate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "goods_categories",
            joinColumns = @JoinColumn(name = "goods_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonManagedReference
    private Set<Category> categories = new HashSet<>();


    public void addCategory(Category category) {
        categories.add(category);
        category.getGoods().add(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.getGoods().remove(this);
    }

    @PrePersist
    private void init(){
        dateOfCreate = LocalDateTime.now();
    }

    public void addImageToGoods(Image image){
        if(images.size() < 9) {
            image.setGoods(this);
            images.add(image);
            if (images.size() == 1) {
                // Встановлення першу додану фотографію як preview, якщо це перша фотографія
                this.previewImageId = image.getId();
            }
        } else {
            throw new RuntimeException("Досягнуто максимальну кількість фотографій для товару");
        }
    }
    public void setIsPersonal(boolean isPersonal) {
        this.isPersonal = isPersonal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
