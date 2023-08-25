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
@Table(name = "postal_item")
public class PostalItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    Long id;

    @Enumerated(EnumType.STRING)
    ItemType type;

    String recipientIndex;

    String recipientAddress;

    String recipientName;

    @Enumerated(EnumType.STRING)
    ItemStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_id")
    private PostOffice postOffice;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @Builder.Default
    private Collection<HistoryItem> history = new ArrayList<>();
}
