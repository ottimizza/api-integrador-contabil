package br.com.ottimizza.integradorcloud.domain.models.roteiro_layouts;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import br.com.ottimizza.integradorcloud.domain.models.roteiro.LayoutPadrao;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roteiros_layouts")
@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
public class RoteiroLayout implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private RoteiroLayoutId roteiroLayout;

    @ManyToOne
    @MapsId("fk_roteiro_id")
    @JoinColumn(name = "fk_roteiro_id", insertable = false, updatable = false)
    private Roteiro roteiro;
    
    @ManyToOne
    @MapsId("fk_layout_id")
    @JoinColumn(name = "fk_layout_id", insertable = false, updatable = false)
    private LayoutPadrao layout;

}
