package com.etechpro.contract.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "ProductComposite", description = "REST API for contract information.")
public interface ContractService {

    /**
     * Sample usage: "curl $HOST:$PORT/contract/1".
     *
     * @param contractId Id of the product
     * @return the contract, if found, else null
     */
    @Operation(
            summary = "${api.contract.get-contract.description}",
            description = "${api.contract.get-contract.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @GetMapping(
            value = "/contract/{contractId}",
            produces = "application/json")
    Contract getContract(@PathVariable int contractId);
}
