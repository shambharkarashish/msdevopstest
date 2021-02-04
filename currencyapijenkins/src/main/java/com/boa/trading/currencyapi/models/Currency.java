package com.boa.trading.currencyapi.models;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Table(name="BOA_Currency")
@Data
@ApiModel
public class Currency {
	@Id
	@ApiModelProperty(example = "INR")
	@Column(name="Code",length = 3)
	private String code;
	@ApiModelProperty(example = "/pictures/inr.jpg")
	@Column(name="Symbol_Location",length = 150,nullable = false)
	private String symbolLocation;
	@ApiModelProperty(example = "true")
	@Column(name="Tradeable_Flag",nullable = false)
	private Boolean tradeableFlag;
	@ApiModelProperty(example = "Indian Rupees")
	@Column(name="Description",length = 150,nullable = false)
	private String description;	
	
	

}
