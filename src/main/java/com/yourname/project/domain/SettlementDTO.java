package com.yourname.project.domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementDTO {

    private Integer settlement_id;
    private Integer funding_id;
    private Integer store_id;
    private Integer fee;
    private Integer amount;
    private String status;
    private Date settled_at;
}
