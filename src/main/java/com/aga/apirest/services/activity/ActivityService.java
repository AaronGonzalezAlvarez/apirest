package com.aga.apirest.services.activity;

import com.aga.apirest.models.Activity;
import com.aga.apirest.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ActivityService implements IActivityService {

    @Autowired
    ActivityRepository activityRepository;

    @Override
    public List<Activity> listActivity() {return activityRepository.findAll();}

    @Override
    public Activity getActivity(int id) {return activityRepository.findById(id).orElse(null);}

    @Override
    public void save(Activity c) {
        activityRepository.save(c);
    }

    @Override
    public void delete(Activity c) {
        activityRepository.delete(c);
    }

    @Override
    public List<Activity> filterActivityForNameConcat(String x) {
        return activityRepository.filterActivityForNameConcat(x);
    }

    @Override
    public List<Activity> filterActivityFree() {
        return activityRepository.filterActivityFree();
    }

    @Override
    public List<Activity> filterActivityForPronvince(String province) {
        return activityRepository.filterActivityForPronvince(province);
    }

    @Override
    public List<Activity> filterActivityForDate(LocalDate date1, LocalDate date2) {
        return activityRepository.filterActivityForDate(date1,date2);
    }

    public List<Activity> getActivityAllOrderDesc(){ return activityRepository.getActivityOrderDesc();}


}
