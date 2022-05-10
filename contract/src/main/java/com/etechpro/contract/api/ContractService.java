package com.etechpro.contract.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ContractService {

    /**
     * Sample usage: "curl $HOST:$PORT/contract/1".
     *
     * @param contractId Id of the product
     * @return the contract, if found, else null
     */
    @GetMapping(
            value = "/contract/{contractId}",
            produces = "application/json")
    Contract getContract(@PathVariable int contractId);
}
