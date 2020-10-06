package com.azure.samples.models;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Container(containerName = "User", ru = "400")
public class User  {

    private String id;
    private String firstName;

    @PartitionKey
    private String lastName;

    @Override
    public String toString() {
        return String.format("com.azure.spring.data.cosmos.User: %s %s, %s", firstName, lastName, id);
    }
}
