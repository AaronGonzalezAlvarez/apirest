package com.aga.apirest.controllers;

import com.aga.apirest.models.Activity;
import com.aga.apirest.models.Post;
import com.aga.apirest.models.User;
import com.aga.apirest.services.activity.ActivityService;
import com.aga.apirest.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static com.aga.apirest.utils.JwtUtils.checkJWTForEndPointReturnId;

@RestController
@RequestMapping("activity")
@CrossOrigin
public class ActivityController {

    private final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    private Object o1 = new Object();
    private Object o2 = new Object();

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addActivity(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Activity activity) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);
        User userAux = userService.getUser(idUser);

        if(userAux.getRol() != 2){
            return ResponseEntity.badRequest().body("Sin permisos para esta acción.");
        }

        Activity a = new Activity(activity.getName(),activity.getSummary(),activity.getDescription(),activity.getLocation(),activity.getStartTime(),activity.getEndTime(),activity.getDate(),activity.getTotal(),activity.getZone(),activity.getProvince(),activity.getMaterial(),activity.getDisplacement(),activity.getHourlyPrice(),activity.getActivityprice(),userAux);
        activityService.save(a);

        return ResponseEntity.ok("Nueva Actividad");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateActivity(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Activity activity) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);
        User userAux = userService.getUser(idUser);

        if(userAux.getRol() != 2){
            return ResponseEntity.badRequest().body("Sin permisos para esta acción.");
        }

        if(!checkId(activity.getId())){
            return ResponseEntity.badRequest().body("Id no existe en la base de datos");
        }

        Activity a = activityService.getActivity(activity.getId());
        a.setDate(activity.getDate());
        a.setActivityprice(activity.getActivityprice());
        a.setHourlyPrice(activity.getHourlyPrice());
        a.setStartTime(activity.getStartTime());
        a.setEndTime(activity.getEndTime());
        activityService.save(a);
        return ResponseEntity.ok("Actividad actualizada.");
    }

    @GetMapping("/getActivityUser")
    public ResponseEntity<?> getActivityUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);
        User userAux = userService.getUser(idUser);

        return ResponseEntity.ok(userAux.getActivitiescreated());
    }

    @GetMapping("/getActivity")
    public ResponseEntity<?> getActivity(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestParam String id) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        return ResponseEntity.ok(activityService.getActivity(Integer.parseInt(id)));
    }

    @GetMapping("/getActivitiesAll")
    public ResponseEntity<?> getActivitiesAll(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }
        //return ResponseEntity.ok(activityService.listActivity());
        return ResponseEntity.ok(activityService.getActivityAllOrderDesc());

    }

    @GetMapping("/getInscriptions")
    public ResponseEntity<?> getInscriptions(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }

        int idUser = Integer.parseInt(checkAuth);
        User userAux = userService.getUser(idUser);

        return ResponseEntity.ok(userAux.getRegisteredActivities());
    }

    @GetMapping("/addInscriptions")
    public ResponseEntity<?> addInscriptions(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestParam String id) {
        synchronized (o1){
            String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

            if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
                return ResponseEntity.badRequest().body(checkAuth);
            }

            Activity activityAux = activityService.getActivity(Integer.parseInt(id));
            if(activityAux.getTotal() ==0){
                return ResponseEntity.badRequest().body("Sin plazas disponibles");
            }
            int idUser = Integer.parseInt(checkAuth);
            User userAux = userService.getUser(idUser);
            activityAux.getUsers().add(userAux);
            activityAux.setTotal(activityAux.getTotal() -1);
            //
            for (Activity x : userAux.getRegisteredActivities()) {
                if (x.getDate().isEqual(activityAux.getDate())) {
                    if ((activityAux.getStartTime().isAfter(x.getStartTime()) && activityAux.getStartTime().isBefore(x.getEndTime())) ||
                            (activityAux.getEndTime().isAfter(x.getStartTime()) && activityAux.getEndTime().isBefore(x.getEndTime())) ||
                            (activityAux.getStartTime().isBefore(x.getStartTime()) && activityAux.getEndTime().isAfter(x.getEndTime()))) {
                        return ResponseEntity.badRequest().body(String.format("Te solapa con la actividad: %s el día %s de %s a %s", x.getName(), x.getDate(), x.getStartTime(), x.getEndTime()));
                    }
                }
            }
            //
            activityService.save(activityAux);

            return ResponseEntity.ok("nueva inscripción");
        }
    }

    @DeleteMapping("/deleteInscriptions")
    public ResponseEntity<?> deleteInscriptions(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestParam String id) {
        synchronized (o2){
            String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

            if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
                return ResponseEntity.badRequest().body(checkAuth);
            }

            Activity activityAux = activityService.getActivity(Integer.parseInt(id));
            int idUser = Integer.parseInt(checkAuth);
            User userAux = userService.getUser(idUser);
            int index = activityAux.getUsers().indexOf(userAux);
            activityAux.getUsers().remove(index);
            activityAux.setTotal(activityAux.getTotal()+1);
            activityService.save(activityAux);
            return ResponseEntity.ok("Inscripción borrada");
        }
    }

    @GetMapping("/filterForName/{name}")
    public ResponseEntity<?> filterForName(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String name) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }
        return ResponseEntity.ok(activityService.filterActivityForNameConcat(name));
    }

    @GetMapping("/filterActivityFree")
    public ResponseEntity<?> filterActivityFree(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }
        return ResponseEntity.ok(activityService.filterActivityFree());
    }

    @GetMapping("/filterActivityForPronvince/{name}")
    public ResponseEntity<?> filterActivityForPronvince(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String name) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }
        return ResponseEntity.ok(activityService.filterActivityForPronvince(name));
    }

    @GetMapping("/filterActivityForDate/{date1}/{date2}")
    public ResponseEntity<?> filterActivityForDate(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String date1,@PathVariable String date2) {

        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }
        LocalDate d1 = LocalDate.parse(date1);
        LocalDate d2 = LocalDate.parse(date2);
        return ResponseEntity.ok(activityService.filterActivityForDate(d1,d2));
    }

    @DeleteMapping("/deleteActivity")
    public ResponseEntity<?> deleteActivity(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestParam String id) {


        String checkAuth = checkJWTForEndPointReturnId(authorizationHeader);

        if(checkAuth.equals("header vacío") || checkAuth.equals("JWT corructo")){
            return ResponseEntity.badRequest().body(checkAuth);
        }
        int idUser = Integer.parseInt(checkAuth);
        User userAux = userService.getUser(idUser);

        if(userAux.getRol() != 2){
            return ResponseEntity.badRequest().body("Sin permisos para esta acción.");
        }

        Activity activityAux = activityService.getActivity(Integer.parseInt(id));
        if(activityAux.getUsers().size() ==0){
            activityService.delete(activityAux);
        }else{
            activityAux.getUsers().clear();
            activityService.delete(activityAux);
        }
        return ResponseEntity.ok("Actividad borrada");
    }

    private boolean checkId(int id){
        boolean check = true;
        Activity aux = activityService.getActivity(id);
        if(aux == null){
            check = false;
        }
        return check;
    }
}
