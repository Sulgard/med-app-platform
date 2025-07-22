package com.example.med_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
public class BaseEntityAudit extends BaseEntity implements Serializable {

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_on", updatable = false)
    private Instant createdOn;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "updated_on")
    private Instant updatedOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntityAudit)) return false;
        if (!super.equals(o)) return false;
        BaseEntityAudit that = (BaseEntityAudit) o;
        return  createdOn.equals(that.createdOn) &&
                updatedOn.equals(that.updatedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
                createdOn, updatedOn);
    }

    @Override
    public String toString() {
        return "BaseEntityAudit{" +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                "}" +
                super.toString();
    }
}
