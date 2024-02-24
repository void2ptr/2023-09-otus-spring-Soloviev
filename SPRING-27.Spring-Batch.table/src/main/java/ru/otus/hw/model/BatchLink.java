package ru.otus.hw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("unused")
@Data
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "batch_links")
public class BatchLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String className;

    private String importLink;

    private String exportLink;

    boolean imported;

    public BatchLink(String className, String importLink, String exportLink) {
        this.className = className;
        this.importLink = importLink;
        this.exportLink = exportLink;
        this.imported = false;
    }
}
