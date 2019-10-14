package org.interview.data;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class TransferTransaction {

    private Long id;
    private TransferStatus status;
    private String errorMessage;
    private TransferRequest transferRequest;
    private ZonedDateTime createDateTime;
    private ZonedDateTime updateDateTime;


}
