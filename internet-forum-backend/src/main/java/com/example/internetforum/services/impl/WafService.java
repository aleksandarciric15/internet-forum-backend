package com.example.internetforum.services.impl;

import com.example.internetforum.exceptions.BadRequestException;
import com.example.internetforum.services.WafServiceInterface;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;


@Service
public class WafService implements WafServiceInterface {
    private final SiemService siemService;

    public WafService(SiemService siemService) {
        this.siemService = siemService;
    }

    public void checkRequest(BindingResult request) {
        if (!request.hasErrors()) {
            return;
        }
        siemService.warning("User created bad request! Potential risks for app!", WafService.class.toString());
        throw new BadRequestException();
    }



//    private String sanitization(String value) {
//
//     /*
//      <dependency>
//      <groupId>org.jsoup</groupId>
//      <artifactId>jsoup</artifactId>
//      <version>1.14.3</version> <!-- Current Jsoup version -->
//      </dependency>
//     */
//        String cleanedValue = Jsoup.clean(value, Whitelist.basic());
//
//        return cleanedValue;
//    }
}
