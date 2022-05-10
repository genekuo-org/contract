package com.etechpro.contract.api;

public class Contract {

    private final int contractId;
    private final String name;

    private final String serviceAddress;

    public Contract() {
        contractId = 0;
        name = null;
        serviceAddress = null;
    }

    public Contract(int contractId, String name, String serviceAddress) {
        this.contractId = contractId;
        this.name = name;
        this.serviceAddress = serviceAddress;
    }

    public int getContractId() {
        return contractId;
    }

    public String getName() {
        return name;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }
}
