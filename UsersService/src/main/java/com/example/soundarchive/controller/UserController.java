package com.example.soundarchive.controller;

import com.example.soundarchive.model.dto.UpdateUserDTO;
import com.example.soundarchive.model.dto.UserDTO;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.dto.pagination.PagedResultDTO;
import com.example.soundarchive.service.UserService;
import com.example.soundarchive.util.constants.Constants;
import com.example.soundarchive.util.pagination.PageableHelper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/soundArchive/user")
public class UserController {

    @Value("${conf.pagination.default-page}")
    private int defaultPage;

    @Value("${conf.pagination.default-size}")
    private int defaultSize;

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@Positive @PathVariable("id") Integer id) {

        UserDTO userDTO = userService.findById(id);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedResultDTO<UserDTO>> findAll(@RequestParam(required = false, defaultValue = "1", name = "page") Integer page,
                                                            @RequestParam (required = false, defaultValue = "10", name = "size") Integer size,
                                                            @RequestParam (required = false)
                                                            @Pattern(regexp = Constants.SORT_ORDER_ASC+"|"+ Constants.SORT_ORDER_DESC,
                                                                    message = "Field order can have either value "+Constants.SORT_ORDER_ASC
                                                                            +" or value "+Constants.SORT_ORDER_DESC) String order,
                                                            @RequestParam (required = false, name = "sortBy") String sortBy,
                                                            @RequestParam (required = false, name = "firstName") String firstName,
                                                            @RequestParam (required = false, name = "lastName") String lastName,
                                                            @RequestParam (required = false, name = "username") String username) {

        Pageable pageable = PageableHelper.createPageable(page, size, sortBy, order, defaultPage, defaultSize);

        ListPagedResultDTO<UserDTO> userList = userService.findAll(pageable, firstName, lastName, username);

        int totalPages =(int) Math.ceil((double) userList.getTotal() / pageable.getPageSize());
        if (totalPages == 0) {
            totalPages = 1;
        }

        PagedResultDTO<UserDTO> userPagedResultDTO = new PagedResultDTO<>(pageable.getPageNumber(), totalPages,
                pageable.getPageSize(), userList.getTotal(), userList.getList());

        return new ResponseEntity<>(userPagedResultDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> save(@Valid @RequestBody UserDTO userDTO) {

        UserDTO userDTOReturn = userService.save(userDTO);

        return new ResponseEntity<>(userDTOReturn, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable("id") Integer id,
                                           @RequestBody UpdateUserDTO updateUserDTO) {

        UserDTO userDTOReturn = userService.update(updateUserDTO, id);

        return new ResponseEntity<>(userDTOReturn, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {

        userService.delete(id);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(value = "/giveArtistPermission")
    public ResponseEntity<Void> giveArtistPermission(Authentication authentication) {

        String username = authentication.getName();
        Integer permissionLevel = 2;

        userService.changeUserPermission(username, permissionLevel);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(value = "/revokeArtistPermission")
    public ResponseEntity<Void> revokeArtistPermission(Authentication authentication) {

        String username = authentication.getName();
        Integer permissionLevel = 1;

        userService.changeUserPermission(username, permissionLevel);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(value = "/updateUserArtist/userId/{userId}/artistId/{artistId}")
    public ResponseEntity<UserDTO> updateUserArtist(@PathVariable("userId") Integer userId, @PathVariable("artistId") Integer artistId) {

        System.out.println("Method updateUserArtist is called");
        UserDTO userDTO = userService.updateUserArtist(userId, artistId);
        System.out.println("Method updateUserArtist is succesfully called");

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


    @PutMapping(value = "/giveAdminPermission")
    public ResponseEntity<Void> giveAdminPermission(Authentication authentication) {

        String username = authentication.getName();
        Integer permissionLevel = 3;

        userService.changeUserPermission(username, permissionLevel);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(value = "/revokeAdminPermission")
    public ResponseEntity<Void> revokeAdminPermission(Authentication authentication) {

        String username = authentication.getName();
        Integer permissionLevel = 1;

        userService.changeUserPermission(username, permissionLevel);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}

