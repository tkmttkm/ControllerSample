package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@jakarta.persistence.Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ENTITY_TEST_TABLE")
public class Entity {

	@Id
	private Integer id;
	@Column(name = "full_name")
	private String fullName;
	private Integer insert_date;
}
