package br.com.ottimizza.integradorcloud.domain;

public enum OrganizationTypes { // @formatter:off

    CLIENT(2),
    ACCOUNTING(1);

    private Integer type;

    private OrganizationTypes(Integer type) {
        this.type = type;
    }

    public Integer getValue() {
        return this.type;
    }

}
