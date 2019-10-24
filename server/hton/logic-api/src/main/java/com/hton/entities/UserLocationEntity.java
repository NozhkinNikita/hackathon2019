package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "user_location")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationEntity implements BaseEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(updatable = false, insertable = false)
    private String userId;

    @Column(updatable = false, insertable = false)
    private String locationId;

    private Boolean actualRelation;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
    @JoinColumn(name = "userId")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = LocationEntity.class)
    @JoinColumn(name = "locationId")
    private LocationEntity location;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "actualRelation");
    }

    @Override
    public List<String> getJoinFields() {
        return Arrays.asList("user", "location");
    }

    public void setUser(UserEntity user) {
        if (user != null) {
            userId = user.getId();
        }
        this.user = user;
    }

    public void setLocation(LocationEntity location) {
        if (location != null) {
            locationId = location.getId();
        }
        this.location = location;
    }
}
