package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "point")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointEntity implements BaseEntity {

    @Id
    private String id;

    private String name;

    private LocalDateTime begin;

    private LocalDateTime end;

    private Boolean isRepeat;

    private String scanId;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = RouterDataEntity.class, mappedBy = "pointId")
    private List<RouterDataEntity> routerDates;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "name", "begin", "end", "isRepeat");
    }

    @Override
    public List<String> getJoinFields() {
        return Collections.singletonList("routerDates");
    }
}
