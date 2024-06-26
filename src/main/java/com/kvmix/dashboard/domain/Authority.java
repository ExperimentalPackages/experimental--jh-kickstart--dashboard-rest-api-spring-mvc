package com.kvmix.dashboard.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

/**
 * A Authority.
 */
@Entity
@Table(name = "jhi_authority")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = {"new", "id"})
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Authority implements Serializable, Persistable<String> {

  private static final long serialVersionUID = 1L;

  @NotNull
  @Size(max = 50)
  @Id
  @Column(name = "name", length = 50, nullable = false)
  private String name;

  @Transient
  private boolean isPersisted;

  // kvmix.needle-entity-add-field - JHipster will add fields here

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Authority name(String name) {
    this.setName(name);
    return this;
  }

  @PostLoad
  @PostPersist
  public void updateEntityState() {
    this.setIsPersisted();
  }

  @Override
  public String getId() {
    return this.name;
  }

  @Transient
  @Override
  public boolean isNew() {
    return !this.isPersisted;
  }

  public Authority setIsPersisted() {
    this.isPersisted = true;
    return this;
  }

  // kvmix.needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Authority)) {
      return false;
    }
    return getName() != null && getName().equals(((Authority) o).getName());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getName());
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "Authority{" +
           "name=" + getName() +
           "}";
  }
}