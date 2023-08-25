package com.ritazcode.mailtracking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "history_item")
public class HistoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private PostalItem item;

    @Enumerated
    private ItemStatus status;

    private Date timestamp;

    public HistoryItem(ItemStatus status, PostalItem item) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.status = status;
        this.item = item;
    }
}
