package ru.skillbox.socialnetwork.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.socialnetwork.data.dto.SupportMessageRequest;
import ru.skillbox.socialnetwork.service.SupportService;

@RestController
@Api(tags = "Поддержка")
@Slf4j
public class SupportController {
    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }


    @PostMapping("/api/v1/support")
    @ApiOperation(value="Сообщение в поддержку")
    public ResponseEntity<?> supportMessage(@RequestBody SupportMessageRequest request){
        log.info("Сообщение в поддержку " + request.getMessage());
       return supportService.supportMessage(request);
    }

}
