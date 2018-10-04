package com.sungwoo.tcp.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@ToString
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "salaries")
public class SalaryEntity {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;
}
