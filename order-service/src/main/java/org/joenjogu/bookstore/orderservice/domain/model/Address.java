package org.joenjogu.bookstore.orderservice.domain.model;

import jakarta.validation.constraints.NotBlank;

public record Address(
        @NotBlank(message = "Address is required") String addressLine1,
        String addressLine2,
        @NotBlank(message = "City is required") String city,
        @NotBlank(message = "State is required") String state,
        @NotBlank(message = "Zipcode is required") String zipcode,
        @NotBlank(message = "Country is required") String country) {}
