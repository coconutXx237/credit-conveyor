package ru.klimkin.deal.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApplicationErrorResponse {
   private String message;
   private long timestamp;
}
