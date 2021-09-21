package ru.skillbox.socialnetwork.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.socialnetwork.annotations.NullOrPattern;
import ru.skillbox.socialnetwork.data.entity.MessagePermission;

@Data
public class PersonRequest {
    @JsonProperty("first_name")
    @NullOrPattern(pattern = "\\w{2,30}",
            message = "Имя указано неверно (допускается от 2 до 30 буквенно-цифровых символов или знак подчёркивания.")
    private String firstName;

    @JsonProperty("last_name")
    @NullOrPattern(pattern = "\\w{2,30}",
            message = "амилия указана неверно (допускается от 2 до 30 буквенно-цифровых символов или знак подчёркивания.")
    private String lastName;

    @JsonProperty("birth_date")
    private Long birthDate;

    @NullOrPattern(pattern = "^\\+?[78]?-?\\s?\\(?\\d{3}\\)?-?\\s?\\d{3}-?\\s?\\d{2}-?\\s?\\d{2}$",
            message = "Неверный формат телефона")
    private String phone;

    @JsonProperty("photo_id")
    private String photoId;

    private String about;

    @JsonProperty("town_id")
    private String townId;

    @JsonProperty("country_id")
    private String countryId;

    @JsonProperty("messages_permission")
    private MessagePermission messagePermission;
}