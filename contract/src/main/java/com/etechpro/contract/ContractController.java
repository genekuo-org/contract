package com.etechpro.contract;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Gene Kuo
 */
@RestController
public class ContractController {

    @RequestMapping(value = "/create")
    public String checkout(@RequestHeader HttpHeaders headers) {
        String result = "You have successfully create your contract V14.";
        return result;
    }
}
