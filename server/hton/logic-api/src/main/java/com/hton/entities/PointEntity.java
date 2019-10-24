package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "point")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointEntity implements BaseEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String name;

    private LocalDateTime begin;

    private LocalDateTime end;

    private Boolean isRepeat;

    private String scanId;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = RouterDataEntity.class, mappedBy = "pointId",
            cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.PERSIST})
    private List<RouterDataEntity> routerDatas;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "name", "begin", "end", "isRepeat", "scanId");
    }

    @Override
    public List<String> getJoinFields() {
        return Arrays.asList("routerDatas");
    }
}
