package ru.skillbox.socialnetwork.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.socialnetwork.data.entity.Notification;
import ru.skillbox.socialnetwork.data.entity.NotificationType;
import ru.skillbox.socialnetwork.data.entity.Person;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private String error;
    private Long timestamp;
    private Long total;
    private Long offset;
    private Long perPage;
    private List<Data> data;

    @lombok.Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data{
        private Long id;
        @JsonProperty("type_id")
        private NotificationType typeId;
        @JsonProperty("sent_time")
        private Long sentTime;
        @JsonProperty("entity_id")
        private Long entityId;
        private Author author;
        private String info;

        public Data(Notification notification, Person person){
            this.id = notification.getId();
            this.typeId = notification.getType();
            this.sentTime = notification.getTime().toEpochSecond(OffsetDateTime.now().getOffset());
            this.entityId = notification.getTargetPersonId();
            this.author = new Author(person);

        }

        @lombok.Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Author {
            @JsonProperty("author_id")
            private long authorId;
            @JsonProperty("first_name")
            private String firstName;

            @JsonProperty("last_name")
            private String lastName;
            private String photo;
            @JsonProperty("birth_date")
            private Long birthDate;

            public Author(Person person){
                this.authorId = person.getId();
                this.firstName = person.getFirstName();
                this.lastName = person.getLastName();
                this.photo = person.getPhoto();
                this.birthDate = person.getBirthTime().toEpochSecond(ZoneOffset.UTC);
            }

        }

    }
}
