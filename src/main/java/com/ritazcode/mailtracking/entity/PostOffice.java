package com.ritazcode.mailtracking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post_office")
public class PostOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "office_id")
    Long id;

    String index;

    String name;

    String address;

    /**
     * Each office contains many postal items
     */
    @OneToMany(mappedBy = "postOffice", cascade = CascadeType.ALL)
    @Builder.Default
    private Collection<PostalItem> postalItems = new ArrayList<>();
}
