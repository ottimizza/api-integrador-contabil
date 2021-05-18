package br.com.ottimizza.integradorcloud.domain.models.roteiro;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vladmihalcea.hibernate.type.array.ListArrayType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
@Entity
@Table(name = "layouts_padroes")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class LayoutPadrao {
    
    @Id
	@SequenceGenerator(name = "layout_padrao_sequence", sequenceName = "layout_padrao_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "layout_padrao_sequence")
	@Column(name = "id")
    private BigInteger id;

    @Column(name = "id_sales_force")
    private String idSalesForce;

    @Column(name = "link_referencia")
    private String linkReferencia;

    @Column(name = "descricao_documento")
    private String descricaoDocumento;

    @Column(name = "tipo_integracao")
    private Short tipoIntegracao;

    @Column(name = "tipo_arquivo")
    private String tipoArquivo;

    @Column(name = "icone")
    private String icone;

    @Type(type = "list-array")
    @Column(name = "tags", columnDefinition = "varchar[]")
    private List<String> tags;

    @Column(name = "pagamentos")
    private Boolean pagamentos;

    @Column(name = "recebimentos")
    private Boolean recebimentos;

    public static class TipoIntegracao  {
        public static final Short EXTRATOS = 0;
        public static final Short CARTOES = 1;
        public static final Short ERPS = 2;
	}

}
