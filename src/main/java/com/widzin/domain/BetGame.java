package com.widzin.domain;

import javax.persistence.*;

@Entity
public class BetGame {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Version
	private Integer version;


}
