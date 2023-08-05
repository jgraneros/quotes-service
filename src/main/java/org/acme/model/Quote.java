package org.acme.model;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class Quote extends PanacheEntity {

    public String quote;
    public String category;
}
