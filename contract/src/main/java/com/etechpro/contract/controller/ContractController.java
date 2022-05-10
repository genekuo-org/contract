package com.etechpro.contract.controller;

import com.etechpro.contract.api.Contract;
import com.etechpro.contract.api.ContractService;
import com.etechpro.contract.api.exceptions.InvalidInputException;
import com.etechpro.contract.api.exceptions.NotFoundException;
import com.etechpro.contract.util.http.ServiceUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Gene Kuo
 */
@RestController
public class ContractController implements ContractService {

    private static final Logger LOG = LoggerFactory.getLogger(ContractService.class);

    private final ServiceUtil serviceUtil;

    @Autowired
    public ContractController(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Contract getContract(int contractId) {
        LOG.debug("/product return the found contract for contractId={}", contractId);

        if (contractId < 1) {
            throw new InvalidInputException("Invalid contractId: " + contractId);
        }

        if (contractId == 13) {
            throw new NotFoundException("No contract found for contractId: " + contractId);
        }

        return new Contract(contractId, "name-" + contractId, serviceUtil.getServiceAddress());
    }
}
