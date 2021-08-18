package org.edgegallery.mecm.inventory.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MEC Platform manager schema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mepminventory")
public final class Mepm implements BaseModel {

    @Id
    @Column(name = "mepm_id")
    private String mepmId;

    @Column(name = "mepm_name")
    private String mepmName;

    @Column(name = "mepm_ip")
    private String mepmIp;

    @Column(name = "mepm_port")
    private String mepmPort;

    @Column(name = "user_name")
    private String userName;

    @OneToMany(mappedBy = "mepm", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<MecHost> mecHosts;

    @Override
    public String getIdentifier() {
        return mepmId;
    }

    @Override
    public String getTenantId() {
        return null;
    }

    @Override
    public ModelType getType() {
        return ModelType.MEPM;
    }
}
