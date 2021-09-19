package ru.skillbox.socialnetwork.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skillbox.socialnetwork.model.dto.PersonRequest;
import ru.skillbox.socialnetwork.model.dto.PersonResponse;
import ru.skillbox.socialnetwork.model.dto.PersonResponse.Data;
import ru.skillbox.socialnetwork.model.entities.Person;
import ru.skillbox.socialnetwork.model.repositories.PersonRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public PersonResponse getPersonDetail(Principal principal) {
        return createPersonResponse(findPerson(principal));
    }

    @Override
    public PersonResponse putPersonDetail(PersonRequest personRequest, Principal principal) {
        Person person = findPerson(principal);
        updatePersonDetail(personRequest, person);
        return createPersonResponse(person);
    }

    private void updatePersonDetail(PersonRequest request, Person person) {
        if (Objects.nonNull(request.getFirstName())) {
            person.setFirstName(request.getFirstName());
        }

        if (Objects.nonNull(request.getLastName())) {
            person.setLastName(request.getLastName());
        }

        if (Objects.nonNull(request.getBirthDate())) {
            person.setBirthTime(request.getBirthDate());
        }

        if (Objects.nonNull(request.getPhone())) {
            person.setPhone(getFormattedPhone(request.getPhone()));
        }

        if (Objects.nonNull(request.getPhotoId())) {
            person.setPhoto(request.getPhotoId());
        }

        if (Objects.nonNull(request.getAbout())) {
            person.setAbout(request.getAbout());
        }

        if (Objects.isNull(request.getMessagePermission())) {
            person.setMessagePermission(request.getMessagePermission());
        }

        if (Objects.nonNull(request.getTownId()) && Objects.nonNull(request.getCountryId())) {
            person.setTown(request.getCountryId().concat(" \u2588 ").concat(request.getTownId()));
        }

        personRepository.flush();
    }

    private String getFormattedPhone(String phone) {
        phone = phone.replaceAll("\\D", "");
        return String.format("+7%s", phone.matches("7|8\\d{10}")
                ? phone.substring(1, 10)
                : phone);
    }

    private Person findPerson(Principal principal) {
        return personRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private PersonResponse createPersonResponse(Person person) {
        String[] location = person.getTown().split("\u2588");
        return PersonResponse.builder()
                .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                .data(Data.builder()
                        .id(person.getId())
                        .firstName(person.getFirstName())
                        .lastName(person.getLastName())
                        .regDate(person.getRegTime())
                        .birthDate(person.getBirthTime())
                        .email(person.getEmail())
                        .phone(person.getPhone())
                        .photo(person.getPhoto())
                        .about(person.getAbout())
                        .country(location[0].trim())
                        .city(location[1].trim())
                        .messagePermission(person.getMessagePermission())
                        .lastOnlineTime(person.getLastOnlineTime())
                        .isBlocked(person.getBlocked())
                        .build())
                .build();
    }
}
