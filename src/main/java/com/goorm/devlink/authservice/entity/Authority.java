package com.goorm.devlink.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authority")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Authority extends AuditingFields {

    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;
}
