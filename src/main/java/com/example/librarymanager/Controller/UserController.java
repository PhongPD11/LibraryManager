package com.example.librarymanager.Controller;

import com.example.librarymanager.Commons.ResponseCommon;
import com.example.librarymanager.DTOs.*;
import com.example.librarymanager.Entity.ContactEntity;
import com.example.librarymanager.Entity.UserContactEntity;
import com.example.librarymanager.Entity.UserScheduleEntity;
import com.example.librarymanager.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
public class UserController {

//    @Autowired
//    JavaMailSender mailSender;

    @Autowired
    UserService service;

    @PostMapping("/login")
    public ApiResponse authenticateUser(@RequestBody Login login) {
        try {
            return ResponseCommon.response(service.login(login), "Success");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ApiResponse userRegistration(@RequestBody Register register) {
        try {
            return ResponseCommon.response(service.register(register), "Success");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/change-device")
    public ApiResponse userRegistration(@RequestBody String fcm, @RequestBody Long uid) {
        try {
            return ResponseCommon.response(service.changeDevice(fcm, uid), "Success");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/verify")
    public Object userConfirm(@RequestParam Long activeCode, String email, String fcm) {
        try {
            return ResponseCommon.response(service.confirm(activeCode, email, fcm), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ApiResponse getUser(@RequestParam Long uid) {
        try {
            return ResponseCommon.response(service.getProfile(uid), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PutMapping("/profile")
    public ApiResponse editUser(
            @RequestPart("model") String json,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Profile dataModel = mapper.readValue(json, Profile.class);
            return ResponseCommon.response(service.editProfile(dataModel, file), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PutMapping("/schedule")
    public ApiResponse updateSchedule(@RequestBody UserScheduleEntity schedule) {
        try {
            return ResponseCommon.response(service.updateSchedule(schedule), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/schedule")
    public ApiResponse addSchedule(@RequestBody UserScheduleEntity schedule) {
        try {
            return ResponseCommon.response(service.addSchedule(schedule), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @DeleteMapping("/schedule")
    public ApiResponse addSchedule(@RequestParam Long id) {
        try {
            return ResponseCommon.response(service.deleteSchedule(id), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }
    @GetMapping ("/schedule")
    public ApiResponse getSchedule(@RequestParam Long uid) {
        try {
            return ResponseCommon.response(service.getScheduleByUid(uid), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/contact/send")
    public ApiResponse sendContact(@RequestBody ContactEntity contact) {
        try {
            return ResponseCommon.response(service.sendContact(contact), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }
    @PutMapping("/contact/status")
    public ApiResponse changeContactStatus(@RequestBody UserContactEntity userContact) {
        try {
            return ResponseCommon.response(service.changeContactStatus(userContact), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/contact/create")
    public ApiResponse createContact(@RequestBody ContactRequest contact) {
        try {
            return ResponseCommon.response(service.createContact(contact), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @DeleteMapping("/contact/delete")
    public ApiResponse deleteContact(@RequestParam Long id) {
        try {
            return ResponseCommon.response(service.deleteContact(id), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/contact")
    public ApiResponse getContacts() {
        try {
            return ResponseCommon.response(service.getContacts(), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }
    @GetMapping("/contact/user")
    public ApiResponse getContacts(@RequestParam Long uid) {
        try {
            return ResponseCommon.response(service.getUserContacts(uid), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/contact/detail")
    public ApiResponse getContactDetail(@RequestParam Long enquiryId) {
        try {
            return ResponseCommon.response(service.getContactDetail(enquiryId), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @GetMapping("/users")
    public ApiResponse getUsers() {
        try {
            return ResponseCommon.response(service.getUsers(), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }

    @PostMapping("/user/status")
    public ApiResponse changeUserStatus(@RequestBody Profile user) {
        try {
            return ResponseCommon.response(service.changeStatus(user), "Success!");
        } catch (Exception e) {
            return ResponseCommon.response(null, e.getMessage());
        }
    }


//    @PutMapping("/profile/edit")
//    public ApiResponse editProfile(@RequestBody )

}
