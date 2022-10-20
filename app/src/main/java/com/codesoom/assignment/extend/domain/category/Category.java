package com.codesoom.assignment.extend.domain.category;

import com.codesoom.assignment.extend.domain.BaseTime;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Table(name = "categories")
@Entity
public class Category extends BaseTime {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false)
    private String name;

    protected Category() {
    }

    @Builder
    private Category(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
