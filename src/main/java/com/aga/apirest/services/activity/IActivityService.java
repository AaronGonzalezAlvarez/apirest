package com.aga.apirest.services.activity;

import com.aga.apirest.models.Activity;
import com.aga.apirest.models.Message;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IActivityService {

    public List<Activity> listActivity();

    public Activity getActivity(int id);

    public void save(Activity c);

    public void delete(Activity c);

    List<Activity> filterActivityForNameConcat(String x);

    List<Activity> filterActivityFree();

    List<Activity> filterActivityForPronvince(String x);

    List<Activity> filterActivityForDate(LocalDate date1, LocalDate date2);
}
